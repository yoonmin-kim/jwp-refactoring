package kitchenpos.menus.menu.domain;

import java.util.List;

public interface ProductRepository {

    boolean existAll(final List<Long> productIds);
}
