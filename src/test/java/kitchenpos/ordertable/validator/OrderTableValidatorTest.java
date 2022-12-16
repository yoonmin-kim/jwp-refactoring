package kitchenpos.ordertable.validator;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Collections;
import java.util.Optional;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.repository.TableGroupRepository;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderTableValidatorTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private TableGroupRepository tableGroupRepository;
    @Mock
    private Order order;
    private OrderTableValidator orderTableValidator;

    @BeforeEach
    void setUp() {
        orderTableValidator = new OrderTableValidator(orderRepository, tableGroupRepository);
    }

    @Test
    void 이미_단체_지정이_된_주문_테이블은_비어있음_여부를_변경할_수_없다() {
        OrderTable orderTable = new OrderTable(1, false);
        orderTable.changeTableGroupId(1L);
        given(tableGroupRepository.findById(any())).willReturn(Optional.of(new TableGroup()));

        ThrowingCallable 이미_단체_지정된_주문_테이블일_경우 = () -> orderTableValidator.validateChangeEmpty(orderTable);

        assertThatIllegalArgumentException().isThrownBy(이미_단체_지정된_주문_테이블일_경우);
    }

    @Test
    void 조리_식사_상태의_주문이_포함_된_주문_테이블은_상태를_변경할_수_없다() {
        OrderTable orderTable = new OrderTable(1, false);
        orderTable.changeTableGroupId(null);
        given(orderRepository.findByOrderTableId(any())).willReturn(Collections.singletonList(order));
        given(order.getOrderStatus()).willReturn(OrderStatus.COOKING.name());

        ThrowingCallable 조리_식사_상태의_주문이_포함_된_경우 = () -> orderTableValidator.validateChangeEmpty(orderTable);

        assertThatIllegalArgumentException().isThrownBy(조리_식사_상태의_주문이_포함_된_경우);
    }
}
