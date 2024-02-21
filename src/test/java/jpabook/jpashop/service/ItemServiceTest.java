package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.ItemRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ItemServiceTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemRepository itemRepository;

    @Test
    @Rollback(value = false)
    public void 상품_등록() throws Exception {
        //given
        Item item = new Book();
        item.setName("book1");
        item.setStockQuantity(5);

        //when
        Long savedId = itemService.save(item);

        //then
        assertEquals(item, itemRepository.findOne(savedId));

    }

    @Test(expected = NotEnoughStockException.class)
    @Rollback(value = false)
    public void 상품_재고_감소() throws Exception {
        //given
        Item item = new Book();
        item.setStockQuantity(5);
        Long savedId = itemService.save(item);
        Item findItem = itemService.findOne(savedId);
        int stockToRemove = 3;

        //when
        findItem.removeStock(stockToRemove);
        findItem.removeStock(stockToRemove);

        //then
        fail("예외가 발생해야 한다.");

    }

}