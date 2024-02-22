package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired private EntityManager em;
    @Autowired private OrderRepository orderRepository;
    @Autowired private OrderService orderService;

    @Test
    public void 상품_주문_생성() throws Exception {
        //given
        Member member = createMemberForTest();
        Book book = createBookForTest("시골 JPA", 10000, 10);
        int orderCount = 2;

        //when
        Long orderId = orderService.makeOrder(member.getId(), book.getId(), orderCount);

        //then
        Order foundOrder = orderRepository.findOne(orderId);

        assertEquals("상품 주문 시 상태는 ORDER", OrderStatus.ORDER, foundOrder.getOrderStatus());
        assertEquals("주문한 상품 종류의 수가 정확해야 한다.", 1, foundOrder.getOrderItems().size());
        assertEquals("주문 가격은 가격 * 수량이다.", 10000 * orderCount, foundOrder.getTotalPrice());
        assertEquals("주문 수량만큼 재고가 줄어야 한다.", 10 - orderCount, book.getStockQuantity());
    }

    @Test(expected = NotEnoughStockException.class)
    public void 상품_주문_재고_수량_초과() throws Exception {
        //given
        Member member = createMemberForTest();
        Book book = createBookForTest("시골 JPA", 10000, 10);
        int orderCount = 11;

        //when
        orderService.makeOrder(member.getId(), book.getId(), orderCount);

        //then
        fail("재고 수량 부족 예외가 발생해야 한다.");

    }

    @Test
    public void 상품_주문_취소() throws Exception {
        //given
        Member member = createMemberForTest();
        Book book = createBookForTest("시골 JPA", 10000, 10);
        int orderCount = 2;
        Long orderId = orderService.makeOrder(member.getId(), book.getId(), orderCount);

        //when
        orderService.cancelOrder(orderId);

        //then
        Order foundOrder = orderRepository.findOne(orderId);

        assertEquals("주문 취소 시 상태는 CANCEL이다.", OrderStatus.CANCEL, foundOrder.getOrderStatus());
        assertEquals("주문이 취소된 상품의 재고가 원복되어야 한다.", 10, book.getStockQuantity());

    }

    public Member createMemberForTest() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "강가", "123123"));
        em.persist(member);
        return member;
    }

    public Book createBookForTest(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }


}