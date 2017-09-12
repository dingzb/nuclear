package cc.idiary.nuclear.dao.business.tax;

import cc.idiary.nuclear.entity.business.tax.AgencyEntity;
import cc.idiary.nuclear.dao.BaseDao;

import java.util.List;

/**
 * Created by Neo on 2017/5/9.
 */

public interface AgencyDao extends BaseDao<AgencyEntity> {

    /**
     * 获取机构列表
     *
     * @param level
     * @return
     */
    List<AgencyEntity> getList(String level);
}
