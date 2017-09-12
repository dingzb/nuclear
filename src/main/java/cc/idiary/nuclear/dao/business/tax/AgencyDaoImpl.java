package cc.idiary.nuclear.dao.business.tax;

import cc.idiary.nuclear.dao.BaseDaoImpl;
import cc.idiary.nuclear.entity.business.tax.AgencyEntity;
import cc.idiary.nuclear.query.BaseQuery;
import cc.idiary.nuclear.query.business.tax.AgencyQuery;
import cc.idiary.utils.common.StringTools;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by Neo on 2017/5/10.
 */
@Repository
public class AgencyDaoImpl extends BaseDaoImpl<AgencyEntity> implements AgencyDao {
    public AgencyDaoImpl() {
        super(AgencyEntity.class);
    }

    @Override
    protected StringBuilder getExtensionSql(BaseQuery query, Map<String, Object> params) {
        AgencyQuery aQuery = (AgencyQuery) query;

        StringBuilder hqlsb = new StringBuilder("from AgencyEntity {0} where 1=1");

        if (!StringTools.isEmpty(aQuery.getName())) {
            hqlsb.append(" and {0}.name = :name");
            params.put("name", aQuery.getName());
        }
        if (!StringTools.isEmpty(aQuery.getParentId())) {
            hqlsb.append(" and {0}.parent.id = :parentId");
            params.put("parentId", aQuery.getParentId());
        }
        return hqlsb;
    }

    @Override
    public List<AgencyEntity> getList(String level) {
        String hql = "from AgencyEntity where parent";
        if ("0".equals(level)){
            hql += " is null";
        } else {
            hql += " is not null";
        }
        return getByHql(hql, null);
    }
}
