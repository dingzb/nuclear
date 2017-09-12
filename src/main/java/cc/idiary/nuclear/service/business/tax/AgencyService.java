package cc.idiary.nuclear.service.business.tax;

import cc.idiary.nuclear.entity.business.tax.AgencyEntity;
import cc.idiary.nuclear.model.business.tax.AgencyModel;
import cc.idiary.nuclear.model.system.UserModel;
import cc.idiary.nuclear.query.business.tax.AgencyQuery;
import cc.idiary.nuclear.service.BaseService;
import cc.idiary.nuclear.service.ServiceException;

import java.util.List;

/**
 * Created by Neo on 2017/5/12.
 */
public interface AgencyService extends BaseService<AgencyEntity> {
    List<AgencyModel> list(String level) throws ServiceException;

    /**
     * 获取当前用户所在机构下所有基层用户
     *
     * @param query
     * @return
     * @throws ServiceException
     */
    List<UserModel> userList(AgencyQuery query) throws ServiceException;

    /**
     * 当前用户所处机构
     * @param query
     * @return
     * @throws ServiceException
     */
    AgencyModel current(AgencyQuery query) throws ServiceException;
}
