package kitchenpos.acceptance;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import kitchenpos.BaseAcceptanceTest;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.ProductRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

class TableAcceptanceTest extends BaseAcceptanceTest {

    MenuGroup 후라이드치킨_메뉴그룹 = new MenuGroup(1L, "후라이드치킨");
    ProductRequest 후라이드치킨_상품 = new ProductRequest(1L, "후라이드치킨", new BigDecimal(16000.00));
    MenuProductRequest 후라이드치킨_메뉴상품 = new MenuProductRequest(1L, 1L, 1L, 1);
    MenuRequest 후라이드치킨 = new MenuRequest(1L, "후라이드치킨", new BigDecimal(16000.00), 1L,
            Collections.singletonList(후라이드치킨_메뉴상품));

    @Test
    void 주문_테이블을_등록할_수_있다() throws Exception {
        OrderTable 주문_테이블 = new OrderTable(null, 1L, 1, false);

        ResultActions resultActions = 주문_테이블_등록(주문_테이블);

        주문_테이블_등록_성공(resultActions, 주문_테이블);
    }

    @Test
    void 주문_테이블_목록을_조회할_수_있다() throws Exception {
        OrderTable orderTable = 주문_테이블이_등록되어_있다();

        ResultActions resultActions = 주문_테이블_목록_조회();

        주문_테이블_목록_조회_성공(resultActions, orderTable);
    }

    @Test
    void 등록_된_주문_테이블에_대해서만_수정할_수_있다() throws Exception {
        OrderTable 등록_되어_있지_않은_주문_테이블 = new OrderTable(1L, 1L, 1, true);

        ResultActions resultActions = 주문_테이블_비어있음_여부_수정(등록_되어_있지_않은_주문_테이블);

        주문_테이블_수정_실패(resultActions);
    }

    @Test
    void 주문_테이블의_비어있음_여부를_수정할_수_있다() throws Exception {
        OrderTable orderTable = 주문_테이블이_등록되어_있다();

        ResultActions resultActions = 주문_테이블_비어있음_여부_수정(orderTable);

        주문_테이블_수정_성공(resultActions);
    }

    @Test
    void 이미_단체_지정이_된_주문_테이블은_수정할_수_없다() throws Exception {
        OrderTable orderTable = 이미_단체_지정이_된_주문_테이블();

        ResultActions resultActions = 주문_테이블_비어있음_여부_수정(orderTable);

        주문_테이블_수정_실패(resultActions);
    }

    @Test
    void 조리_식사_상태의_주문이_포함되어_있으면_수정할_수_없다() throws Exception {
        OrderTable orderTable = 조리_식사_상태의_주문이_포함된_테이블();

        ResultActions resultActions = 주문_테이블_비어있음_여부_수정(orderTable);

        주문_테이블_수정_실패(resultActions);
    }

    @Test
    void 방문한_손님수를_0명_이하로_수정할_수_없다() throws Exception {
        OrderTable orderTable = 주문_테이블이_등록되어_있다();
        orderTable.setNumberOfGuests(-1);

        ResultActions resultActions = 주문_테이블_방문한_손님수_수정(orderTable);

        주문_테이블_수정_실패(resultActions);
    }

    @Test
    void 등록_된_주문_테이블에_대해서만_손님수를_수정할_수_있다() throws Exception {
        OrderTable 등록되지_않은_주문_테이블 = new OrderTable(1L, 1L, 1, false);

        ResultActions resultActions = 주문_테이블_방문한_손님수_수정(등록되지_않은_주문_테이블);

        주문_테이블_수정_실패(resultActions);
    }

    @Test
    void 빈_테이블은_방문한_손님수를_수정할_수_없다() throws Exception {
        OrderTable orderTable = 빈_테이블이_등록되어_있다();

        ResultActions resultActions = 주문_테이블_방문한_손님수_수정(orderTable);

        주문_테이블_수정_실패(resultActions);
    }

    @Test
    void 주문_테이블의_방문한_손님수를_수정할_수_있다() throws Exception {
        OrderTable orderTable = 주문_테이블이_등록되어_있다();
        orderTable.setNumberOfGuests(2);

        ResultActions resultActions = 주문_테이블_방문한_손님수_수정(orderTable);

        주문_테이블_방문한_손님수_수정_성공(resultActions);
    }

    private void 주문_테이블_방문한_손님수_수정_성공(ResultActions resultActions) throws Exception {
        resultActions.andExpect(status().isOk());
    }

    private OrderTable 빈_테이블이_등록되어_있다() throws Exception {
        OrderTable orderTable = 주문_테이블이_등록되어_있다();
        orderTable.setEmpty(true);
        ResultActions resultActions = 주문_테이블_비어있음_여부_수정(orderTable);
        주문_테이블_수정_성공(resultActions);
        return orderTable;
    }

    private OrderTable 조리_식사_상태의_주문이_포함된_테이블() throws Exception {
        OrderTable orderTable = 주문_테이블이_등록되어_있다();
        주문이_등록되어_있다(orderTable);
        return orderTable;
    }

    private Order 주문이_등록되어_있다(OrderTable orderTable) throws Exception {
        메뉴그룹_등록(후라이드치킨_메뉴그룹);
        상품_등록(후라이드치킨_상품);
        메뉴_등록(후라이드치킨);
        주문_테이블_등록(orderTable);
        Order 주문 = new Order(null, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(),
                Collections.singletonList(new OrderLineItem(1L, 1L, 1L, 1)));
        주문_등록(주문);
        return 주문;
    }

    private ResultActions 메뉴_등록(MenuRequest menu) throws Exception {
        return mvc.perform(post("/api/menus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(menu))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    private ResultActions 주문_등록(Order order) throws Exception {
        return mvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    private ResultActions 메뉴그룹_등록(MenuGroup menuGroup) throws Exception {
        return mvc.perform(post("/api/menu-groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(menuGroup))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    private ResultActions 상품_등록(ProductRequest product) throws Exception {
        return mvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    private void 주문_테이블_수정_실패(ResultActions resultActions) throws Exception {
        resultActions.andExpect(status().is4xxClientError());
    }

    private ResultActions 주문_테이블_방문한_손님수_수정(OrderTable orderTable) throws Exception {
        return mvc.perform(put("/api/tables/{orderTableId}/number-of-guests", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderTable))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    private ResultActions 주문_테이블_비어있음_여부_수정(OrderTable orderTable) throws Exception {
        return mvc.perform(put("/api/tables/{orderTableId}/empty", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderTable))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    private void 주문_테이블_목록_조회_성공(ResultActions resultActions, OrderTable orderTable) throws Exception {
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1L))
                .andExpect(jsonPath("$.[0].tableGroupId").isEmpty())
                .andExpect(jsonPath("$.[0].numberOfGuests").value(orderTable.getNumberOfGuests()))
                .andExpect(jsonPath("$.[0].empty").value(orderTable.isEmpty()));
    }

    private OrderTable 주문_테이블이_등록되어_있다() throws Exception {
        OrderTable 주문_테이블 = new OrderTable(null, 1L, 1, false);
        ResultActions resultActions = 주문_테이블_등록(주문_테이블);
        주문_테이블_등록_성공(resultActions, 주문_테이블);
        return 주문_테이블;
    }

    private ResultActions 주문_테이블_목록_조회() throws Exception {
        return mvc.perform(get("/api/tables")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    private void 주문_테이블_수정_성공(ResultActions resultActions) throws Exception {
        resultActions.andExpect(status().isOk());
    }

    private void 주문_테이블_등록_성공(ResultActions resultActions, OrderTable orderTable) throws Exception {
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("tableGroupId").isEmpty())
                .andExpect(jsonPath("numberOfGuests").value(orderTable.getNumberOfGuests()))
                .andExpect(jsonPath("empty").value(orderTable.isEmpty()));
    }

    private ResultActions 주문_테이블_등록(OrderTable orderTable) throws Exception {
        return mvc.perform(post("/api/tables")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderTable))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    private OrderTable 이미_단체_지정이_된_주문_테이블() throws Exception {
        OrderTable 주문_테이블 = new OrderTable(null, null, 1, true);
        OrderTable 주문_테이블2 = new OrderTable(null, null, 1, true);
        주문_테이블_등록(주문_테이블);
        주문_테이블_등록(주문_테이블2);
        주문_테이블.setId(1L);
        주문_테이블2.setId(2L);
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), Arrays.asList(주문_테이블, 주문_테이블2));
        mvc.perform(post("/api/table-groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tableGroup))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
        return 주문_테이블;
    }
}
