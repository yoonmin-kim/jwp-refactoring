package kitchenpos.tablegroup.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.order.domain.Order;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.repository.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.repository.TableGroupRepository;
import kitchenpos.validator.tablegroup.TableGroupValidator;
import kitchenpos.validator.tablegroup.TableGroupValidatorsImpl;
import kitchenpos.validator.tablegroup.impl.AlreadyGroupedTableGroupValidator;
import kitchenpos.validator.tablegroup.impl.OrderStatusTableGroupValidator;
import kitchenpos.validator.tablegroup.impl.OrderTableEmptyValidator;
import kitchenpos.validator.tablegroup.impl.OrderTablesSizeValidator;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Mock
    private TableGroupRepository tableGroupRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private TableGroup tableGroup;
    @Mock
    private OrderTable orderTable;
    @Mock
    private OrderTable orderTable2;
    @Mock
    private Order order;
    @Mock
    private ApplicationEventPublisher eventPublisher;
    private TableGroupService tableGroupService;
    private TableGroupValidatorsImpl tableGroupValidatorImpl;

    @BeforeEach
    void setUp() {
        List<TableGroupValidator> tableGroupValidators = Arrays
                .asList(new OrderTableEmptyValidator(), new AlreadyGroupedTableGroupValidator(),
                        new OrderTablesSizeValidator(),
                        new OrderStatusTableGroupValidator(orderRepository));

        tableGroupValidatorImpl = new TableGroupValidatorsImpl(orderTableRepository, tableGroupValidators);
        tableGroupService = new TableGroupService(tableGroupRepository, tableGroupValidatorImpl, eventPublisher);
    }

    @Test
    void 단체_지정을_등록할_수_있다() {
        given(tableGroupRepository.save(any())).willReturn(tableGroup);
        given(tableGroup.getId()).willReturn(1L);
        given(orderTableRepository.findAllByIdIn(any()))
                .willReturn(Optional.of(Arrays.asList(orderTable, orderTable2)));
        given(orderTable.isEmpty()).willReturn(true);
        given(orderTable2.isEmpty()).willReturn(true);
        given(orderTable.getTableGroupId()).willReturn(null);
        given(orderTable2.getTableGroupId()).willReturn(null);
        TableGroupRequest tableGroupRequest = new TableGroupRequest(
                Arrays.asList(new OrderTableRequest(1L, 1, true), new OrderTableRequest(2L, 1, true)));

        TableGroup createTableGroup = tableGroupService.create(tableGroupRequest);

        assertThat(createTableGroup).isEqualTo(tableGroup);
    }

    @Test
    void 두개_이상의_주문_테이블만_단체_지정이_가능하다() {
        given(tableGroupRepository.save(any())).willReturn(tableGroup);
        given(tableGroup.getId()).willReturn(1L);
        given(orderTableRepository.findAllByIdIn(any()))
                .willReturn(Optional.of(Collections.singletonList(orderTable)));
        given(orderTable.isEmpty()).willReturn(true);
        given(orderTable.getTableGroupId()).willReturn(null);
        TableGroupRequest tableGroupRequest = new TableGroupRequest(
                Arrays.asList(new OrderTableRequest(1L, 1, true), new OrderTableRequest(2L, 1, true)));

        ThrowingCallable 두개_미만의_주문_테이블_단체_지정 = () -> tableGroupService.create(tableGroupRequest);

        assertThatIllegalArgumentException().isThrownBy(두개_미만의_주문_테이블_단체_지정);
    }

    @Test
    void 주문_테이블은_필수로_지정해야_한다() {
        given(tableGroupRepository.save(any())).willReturn(tableGroup);
        TableGroupRequest tableGroupRequest = new TableGroupRequest(Collections.emptyList());

        ThrowingCallable 주문_테이블을_지정하지_않은_단체_지정 = () -> tableGroupService.create(tableGroupRequest);

        assertThatIllegalArgumentException().isThrownBy(주문_테이블을_지정하지_않은_단체_지정);
    }

    @Test
    void 등록_된_주문_테이블만_단체_지정이_가능하다() {
        given(tableGroupRepository.save(any())).willReturn(tableGroup);
        given(tableGroup.getId()).willReturn(1L);
        given(orderTableRepository.findAllByIdIn(any()))
                .willReturn(Optional.empty());
        TableGroupRequest tableGroupRequest = new TableGroupRequest(
                Arrays.asList(new OrderTableRequest(1L, 1, true), new OrderTableRequest(2L, 1, true)));

        ThrowingCallable 등록되지_않은_주문_테이블_단체_지정 = () -> tableGroupService.create(tableGroupRequest);

        assertThatIllegalArgumentException().isThrownBy(등록되지_않은_주문_테이블_단체_지정);
    }

    @Test
    void 빈_테이블이_아닌_주문_테이블은_단체_지정이_불가능하다() {
        given(tableGroupRepository.save(any())).willReturn(tableGroup);
        given(tableGroup.getId()).willReturn(1L);
        given(orderTableRepository.findAllByIdIn(any()))
                .willReturn(Optional.of(Arrays.asList(orderTable, orderTable2)));
        given(orderTable.isEmpty()).willReturn(false);
        TableGroupRequest tableGroupRequest = new TableGroupRequest(
                Arrays.asList(new OrderTableRequest(1L, 1, true), new OrderTableRequest(2L, 1, true)));

        ThrowingCallable 빈_테이블_단체지정 = () -> tableGroupService.create(tableGroupRequest);

        assertThatIllegalArgumentException().isThrownBy(빈_테이블_단체지정);
    }

    @Test
    void 이미_단체_지정이_된_주문_테이블은_단체_지정이_불가능하다() {
        given(tableGroupRepository.save(any())).willReturn(tableGroup);
        given(tableGroup.getId()).willReturn(1L);
        given(orderTableRepository.findAllByIdIn(any()))
                .willReturn(Optional.of(Arrays.asList(orderTable, orderTable2)));
        given(orderTable.isEmpty()).willReturn(true);
        given(orderTable.getTableGroupId()).willReturn(1L);
        TableGroupRequest tableGroupRequest = new TableGroupRequest(
                Arrays.asList(new OrderTableRequest(1L, 1, true), new OrderTableRequest(2L, 1, true)));

        ThrowingCallable 이미_단체_지정이_된_주문_테이블_단체지정 = () -> tableGroupService.create(tableGroupRequest);

        assertThatIllegalArgumentException().isThrownBy(이미_단체_지정이_된_주문_테이블_단체지정);
    }

    @Test
    void 단체_지정을_해제할_수_있다() {
        given(orderTableRepository.findListByTableGroupId(any()))
                .willReturn(Optional.of(Collections.singletonList(orderTable)));
        given(orderRepository.findByOrderTableId(any())).willReturn(Collections.singletonList(order));
        given(order.isSameStatus(any())).willReturn(false);
        given(tableGroupRepository.findById(any())).willReturn(Optional.of(tableGroup));

        ThrowingCallable 단체_지정을_해제할_수_있다 = () -> tableGroupService.ungroup(1L);

        assertThatNoException().isThrownBy(단체_지정을_해제할_수_있다);
    }

    @Test
    void 주문_테이블에_조리_식사_상태가_포함된_주문이_있을경우_해제가_불가능하다() {
        given(tableGroupRepository.findById(any())).willReturn(Optional.of(tableGroup));
        given(orderTableRepository.findListByTableGroupId(any()))
                .willReturn(Optional.of(Collections.singletonList(orderTable)));
        given(orderRepository.findByOrderTableId(any())).willReturn(Collections.singletonList(order));
        given(order.isSameStatus(any())).willReturn(true);

        ThrowingCallable 주문_테이블에_조리_식사_상태가_포함된_주문이_있을경우 = () -> tableGroupService.ungroup(1L);

        assertThatIllegalArgumentException().isThrownBy(주문_테이블에_조리_식사_상태가_포함된_주문이_있을경우);
    }
}
