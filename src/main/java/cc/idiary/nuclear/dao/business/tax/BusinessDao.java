package cc.idiary.nuclear.dao.business.tax;

import cc.idiary.nuclear.entity.business.tax.BusinessEntity;
import cc.idiary.nuclear.query.business.tax.BusinessQuery;
import cc.idiary.nuclear.dao.BaseDao;

import java.util.List;

/**
 * Created by Neo on 2017/5/9.
 */

public interface BusinessDao extends BaseDao<BusinessEntity> {

    List<BusinessEntity> paging(BusinessQuery query);

    /**
     * 统计页面 业务详情专用
     * @param query
     * @return
     */
    List<BusinessEntity> pagingError(BusinessQuery query);
}
