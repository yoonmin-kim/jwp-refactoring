package kitchenpos.application;

import static kitchenpos.__fixture__.MenuGroupTestFixture.메뉴_그룹_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("MenuGroupService 테스트")
@ExtendWith(MockitoExtension.class)
public class MenuGroupServiceTest {
    @Mock
    private MenuGroupDao menuGroupDao;
    @InjectMocks
    private MenuGroupService menuGroupService;
    private MenuGroup 한마리_메뉴_그룹;

    @BeforeEach
    void setUp() {
        한마리_메뉴_그룹 = 메뉴_그룹_생성(1L, "한마리메뉴");
    }

    @Test
    @DisplayName("메뉴 그룹 생성")
    public void create() {
        given(menuGroupDao.save(any(MenuGroup.class))).willReturn(한마리_메뉴_그룹);

        final MenuGroup 저장된_메뉴_그룹 = menuGroupService.create(한마리_메뉴_그룹);
        assertThat(저장된_메뉴_그룹.getName()).isEqualTo(한마리_메뉴_그룹.getName());
    }

    @Test
    @DisplayName("메뉴 그룹 조회")
    public void list() {
        given(menuGroupDao.findAll()).willReturn(Arrays.asList(한마리_메뉴_그룹));

        final List<MenuGroup> 메뉴_그룹_리스트 = menuGroupService.list();

        assertThat(메뉴_그룹_리스트.size()).isEqualTo(1);
    }
}
