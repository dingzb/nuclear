package cc.idiary.nuclear.dao.selection;

import cc.idiary.nuclear.config.ActivityStage;
import cc.idiary.nuclear.dao.BaseDaoImpl;
import cc.idiary.nuclear.entity.selection.AwardEntity;
import cc.idiary.nuclear.query.BaseQuery;
import cc.idiary.nuclear.query.selection.AwardQuery;
import cc.idiary.utils.common.StringTools;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("awardDao")
public class AwardDaoImpl extends BaseDaoImpl<AwardEntity> implements AwardDao {
    public AwardDaoImpl() {
        super(AwardEntity.class);
    }

    @Override
    protected StringBuilder getExtensionSql(BaseQuery query, Map<String, Object> params) {
        AwardQuery awardQuery = (AwardQuery) query;
        StringBuilder hqlsb = new StringBuilder(" from AwardEntity {0} where 1=1");

        if (!StringTools.isEmpty(awardQuery.getName())) {
            hqlsb.append(" and {0}.name like :name");
            params.put("name", "%" + awardQuery.getName() + "%");
        }

        if (!StringTools.isEmpty(awardQuery.getActivityId())) {
            hqlsb.append(" and {0}.activity.id = :activityId");
            params.put("activityId", awardQuery.getActivityId());
        }

        if (awardQuery.getCurrent()) {
            hqlsb.append(" and {0}.activity.stage != ").append(ActivityStage.FINISH.getValue());
        }

        return hqlsb;
    }

    @Override
    public List<AwardEntity> paging(AwardQuery query) {
        if (query == null) {
            return new ArrayList<AwardEntity>();
        }
        Map<String, Object> params = new HashMap<String, Object>();
        String hql = getHql(query, "a", params);
        hql += " order by a.name";
        return getByHqlPaging(hql, params, query.getPage(), query.getSize());
    }

    @Override
    public boolean existByName(String name) {
        return existBy("name", name);
    }
}
