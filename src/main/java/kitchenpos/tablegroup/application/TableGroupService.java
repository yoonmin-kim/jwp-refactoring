package kitchenpos.tablegroup.application;

import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.repository.TableGroupRepository;
import kitchenpos.tablegroup.validator.TableGroupValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TableGroupService {

    private final TableGroupRepository tableGroupRepository;
    private final TableGroupValidator tableGroupValidator;

    public TableGroupService(TableGroupRepository tableGroupRepository,
                             TableGroupValidator tableGroupValidator) {
        this.tableGroupRepository = tableGroupRepository;
        this.tableGroupValidator = tableGroupValidator;
    }

    @Transactional
    public TableGroup create(final TableGroupRequest tableGroupRequest) {
        TableGroup tableGroup = tableGroupRepository.save(new TableGroup());
        tableGroupValidator.validateCreation(tableGroup.getId(), tableGroupRequest.getOrderTableIds());
        return tableGroup;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "등록 되지 않은 단체 지정은 해제할 수 없습니다[tableGroupId:" + tableGroupId + "]"));

        tableGroupValidator.validateUngroup(tableGroupId);
        tableGroupRepository.delete(tableGroup);
    }
}
