package cc.idiary.nuclear.dao.business.tax;

import cc.idiary.nuclear.entity.business.tax.BusCategoryEntity;
import cc.idiary.nuclear.dao.BaseDao;

import java.util.List;

/**
 * Created by Neo on 2017/5/9.
 */

public interface BusinessCategoryDao extends BaseDao<BusCategoryEntity> {
    List<BusCategoryEntity> list(String typeId);
}
