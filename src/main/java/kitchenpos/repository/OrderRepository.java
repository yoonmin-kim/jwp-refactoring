package kitchenpos.repository;

import java.util.List;
import kitchenpos.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(value = "select o from Order o join fetch o.orderTable join fetch o.orderLineItems.orderLineItems")
    List<Order> findAllWithOrderTableAndOrderLineItems();
}
