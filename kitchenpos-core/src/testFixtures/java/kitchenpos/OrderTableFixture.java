package kitchenpos;

import kitchenpos.domain.ordertable.domain.OrderTable;

public class OrderTableFixture {
    private OrderTableFixture() {
    }

    public static OrderTable getOrderTable(Long id, boolean empty, int numberOfGuests) {
        return OrderTable.generate(id, numberOfGuests, empty);
    }
}
