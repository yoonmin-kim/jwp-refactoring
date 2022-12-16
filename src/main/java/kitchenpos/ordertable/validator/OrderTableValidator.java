package kitchenpos.ordertable.validator;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.repository.TableGroupRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class OrderTableValidator {

    private final OrderRepository orderRepository;
    private final TableGroupRepository tableGroupRepository;

    public OrderTableValidator(OrderRepository orderRepository,
                               TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    public void validateChangeEmpty(OrderTable orderTable) {
        validateAlreadyTableGroup(orderTable);
        validateOrderStatus(orderTable.getId());
    }

    @Transactional(readOnly = true)
    void validateOrderStatus(Long orderTableId) {
        List<Order> orders = orderRepository.findByOrderTableId(orderTableId);
        List<String> orderStatuses = Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name());
        Optional<Order> findInOrderStatuses = orders.stream()
                .filter(order -> orderStatuses
                        .contains(order.getOrderStatus()))
                .findAny();

        if (findInOrderStatuses.isPresent()) {
            throw new IllegalArgumentException("조리, 식사 상태의 주문이 포함 된 주문 테이블은 상태를 변경할 수 없습니다");
        }
    }

    @Transactional(readOnly = true)
    void validateAlreadyTableGroup(OrderTable orderTable) {
        if (Objects.nonNull(orderTable.getTableGroupId()) && tableGroupRepository.findById(orderTable.getTableGroupId())
                .isPresent()) {
            throw new IllegalArgumentException("이미 단체 지정이 된 주문 테이블입니다[" + orderTable + "]");
        }
    }
}
