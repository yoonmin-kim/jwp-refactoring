package kitchenpos.order.dto;

import java.util.List;
import java.util.stream.Collectors;

public class TableGroupRequest {
	private List<OrderTableRequest> orderTables;

	protected TableGroupRequest() {
	}

	public TableGroupRequest(List<OrderTableRequest> orderTables) {
		this.orderTables = orderTables;
	}

	public List<OrderTableRequest> getOrderTables() {
		return orderTables;
	}

	public List<Long> getOrderTableIds() {
		return orderTables.stream()
			.map(OrderTableRequest::getId)
			.collect(Collectors.toList());
	}
}
