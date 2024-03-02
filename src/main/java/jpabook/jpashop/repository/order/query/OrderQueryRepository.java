package jpabook.jpashop.repository.order.query;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    private final EntityManager em;

    public List<OrderQueryDTO> findOrderQueryDTOs() {
        List<OrderQueryDTO> result = findOrders();

        result.forEach(orderQueryDTO -> {
            List<OrderItemQueryDTO> orderItems = findOrderItems(orderQueryDTO.getOrderId());
            orderQueryDTO.setOrderItems(orderItems);
        });

        return result;
    }

    private List<OrderItemQueryDTO> findOrderItems(Long orderId) {
        return em.createQuery("" +
                "select new jpabook.jpashop.repository.order.query.OrderItemQueryDTO(oi.order.id, i.name, oi.orderPrice, oi.count) " +
                    "from OrderItem oi " +
                    "join oi.item i " +
                    "where oi.order.id = :orderId", OrderItemQueryDTO.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }

    private List<OrderQueryDTO> findOrders() {
        return em.createQuery("" +
                "select new jpabook.jpashop.repository.order.query.OrderQueryDTO(o.id, m.name, o.orderDate, o.orderStatus, d.address) " +
                    "from Order o " +
                    "join o.member m " +
                    "join o.delivery d"
                , OrderQueryDTO.class)
                .getResultList();
    }

    public List<OrderQueryDTO> findAllByDtoOptimization() {
        List<OrderQueryDTO> orders = findOrders();

        List<Long> orderIds = orders.stream()
                .map(orderQueryDTO -> orderQueryDTO.getOrderId())
                .collect(Collectors.toList());

        List<OrderItemQueryDTO> orderItems = em.createQuery("" +
                        "select new jpabook.jpashop.repository.order.query.OrderItemQueryDTO(oi.order.id, i.name, oi.orderPrice, oi.count) " +
                        "from OrderItem oi " +
                        "join oi.item i " +
                        "where oi.order.id in :orderIds", OrderItemQueryDTO.class)
                .setParameter("orderIds", orderIds)
                .getResultList();

        Map<Long, List<OrderItemQueryDTO>> orderItemMap = orderItems.stream()
                .collect(Collectors.groupingBy(orderItemQueryDTO -> orderItemQueryDTO.getOrderId()));

        orders.forEach(orderQueryDTO -> orderQueryDTO.setOrderItems(orderItemMap.get(orderQueryDTO.getOrderId())));

        return orders;
    }
}
