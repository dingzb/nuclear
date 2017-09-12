package cc.idiary.nuclear.dao.business.tax;

import cc.idiary.nuclear.dao.BaseDaoImpl;
import cc.idiary.nuclear.entity.business.tax.BusCategoryTypeEntity;
import cc.idiary.nuclear.query.business.tax.BusCategoryTypeQuery;
import cc.idiary.nuclear.query.BaseQuery;
import cc.idiary.utils.common.StringTools;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * Created by Neo on 2017/5/10.
 */

@Repository
public class BusinessCategoryTypeDaoImpl extends BaseDaoImpl<BusCategoryTypeEntity> implements BusinessCategoryTypeDao {
    public BusinessCategoryTypeDaoImpl() {
        super(BusCategoryTypeEntity.class);
    }

    @Override
    protected StringBuilder getExtensionSql(BaseQuery query, Map<String, Object> params) {
        BusCategoryTypeQuery bQuery = (BusCategoryTypeQuery) query;

        StringBuilder hqlsb = new StringBuilder("from BusCategoryTypeEntity {0} where 1=1");

        if (!StringTools.isEmpty(bQuery.getName())) {
            hqlsb.append(" and {0}.name = :name");
            params.put("name", bQuery.getName());
        }
        return hqlsb;
    }
}
