package cc.idiary.nuclear.service.selection;

import cc.idiary.nuclear.entity.selection.ExpertEntity;
import cc.idiary.nuclear.model.PagingModel;
import cc.idiary.nuclear.query.selection.ExpertQuery;
import cc.idiary.nuclear.service.BaseService;
import cc.idiary.nuclear.service.ServiceException;

public interface ExpertService extends BaseService<ExpertEntity> {

    PagingModel paging(ExpertQuery query) throws ServiceException;
}
