package cc.idiary.nuclear.service.selection;

import cc.idiary.nuclear.entity.selection.AwardTypeEntity;
import cc.idiary.nuclear.model.selection.AwardTypeModel;
import cc.idiary.nuclear.service.BaseService;
import cc.idiary.nuclear.service.ServiceException;

import java.util.List;

public interface AwardTypeService extends BaseService<AwardTypeEntity> {
    List<AwardTypeModel> list() throws ServiceException;
}
