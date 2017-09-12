package cc.idiary.nuclear.service.business.tax;

import cc.idiary.nuclear.entity.business.tax.BusIssueEntity;
import cc.idiary.nuclear.model.business.tax.BusIssueModel;
import cc.idiary.nuclear.service.BaseService;
import cc.idiary.nuclear.service.ServiceException;

import java.util.List;

/**
 * Created by Neo on 2017/5/11.
 */
public interface BusinessIssueService extends BaseService<BusIssueEntity> {
    List<BusIssueModel> list() throws ServiceException;
}
