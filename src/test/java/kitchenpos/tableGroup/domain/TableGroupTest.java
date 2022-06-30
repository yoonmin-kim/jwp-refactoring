package kitchenpos.tableGroup.domain;

import kitchenpos.orderTable.domain.OrderTable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TableGroupTest {
    @DisplayName("테이블 그룹을 생성한다")
    @Test
    void TableGroup_생성() {
        OrderTable orderTable = new OrderTable(1L, null, 0, true);
        OrderTable orderTable2 = new OrderTable(2L, null, 0, true);
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), Arrays.asList(orderTable, orderTable2));

        assertThat(tableGroup.getOrderTables()).containsExactly(orderTable, orderTable2);
    }

    @DisplayName("테이블그룹 내 주문테이블은 2개 이상이어야 한다")
    @Test
    void TableGroup_주문테이블_2개이상_검증(){
        OrderTable orderTable = new OrderTable(1L, null, 0, true);

        assertThrows(IllegalArgumentException.class,
                () -> new TableGroup(LocalDateTime.now(), Arrays.asList(orderTable)));
    }

    @DisplayName("테이블그룹 내 주문테이블은 중복될 수 없다")
    @Test
    void TableGroup_주문테이블_중복불가_검증(){
        OrderTable orderTable = new OrderTable(1L, null, 0, true);

        assertThrows(IllegalArgumentException.class,
                () -> new TableGroup(LocalDateTime.now(), Arrays.asList(orderTable, orderTable)));
    }

    @DisplayName("비어있는 주문테이블만 등록할 수 있다")
    @Test
    void TableGroup_주문테이블_비어있음_검증(){
        OrderTable orderTable = new OrderTable(1L, null, 0, true);
        OrderTable orderTable2 = new OrderTable(2L, null, 4, false);

        assertThrows(IllegalArgumentException.class,
                () -> new TableGroup(LocalDateTime.now(), Arrays.asList(orderTable, orderTable2)));

    }

    @DisplayName("이미 테이블그룹에 속해있는 주문테이블은 등록할 수 없다")
    @Test
    void TableGroup_주문테이블_다른테이블에_속하지않음_검증(){
        OrderTable orderTable = new OrderTable(1L, null, 0, true);
        OrderTable orderTable2 = new OrderTable(2L, null, 0, true);
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), Arrays.asList(orderTable, orderTable2));

        OrderTable orderTable3 = new OrderTable(2L, null, 4, false);

        assertThrows(IllegalArgumentException.class,
                () -> new TableGroup(LocalDateTime.now(), Arrays.asList(orderTable3, orderTable)));

    }

    @DisplayName("테이블 그룹을 삭제할 수 있다")
    @Test
    void TableGroup_삭제(){
        OrderTable orderTable = new OrderTable(1L, null, 0, true);
        OrderTable orderTable2 = new OrderTable(2L, null, 0, true);
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), Arrays.asList(orderTable, orderTable2));
        tableGroup.ungroup(new ArrayList<>());

        assertAll(
                () -> assertThat(orderTable.isGrouped()).isFalse(),
                () -> assertThat(orderTable2.isGrouped()).isFalse()
        );
    }
}