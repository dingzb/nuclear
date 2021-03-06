package cc.idiary.nuclear.dao.selection;

import cc.idiary.nuclear.config.ActivityStage;
import cc.idiary.nuclear.dao.BaseDaoImpl;
import cc.idiary.nuclear.entity.selection.CheckCriterionEntity;
import cc.idiary.nuclear.query.BaseQuery;
import cc.idiary.nuclear.query.selection.CheckCriterionQuery;
import cc.idiary.utils.common.StringTools;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("checkCriterionDao")
public class CheckCriterionDaoImpl extends BaseDaoImpl<CheckCriterionEntity> implements CheckCriterionDao {
    public CheckCriterionDaoImpl() {
        super(CheckCriterionEntity.class);
    }

    @Override
    protected StringBuilder getExtensionSql(BaseQuery query, Map<String, Object> params) {
        CheckCriterionQuery ccQuery = (CheckCriterionQuery) query;
        StringBuilder hqlsb = new StringBuilder(" from CheckCriterionEntity {0} where 1=1");

        if (!StringTools.isEmpty(ccQuery.getName())) {
            hqlsb.append(" and {0}.name like :name");
            params.put("name", "%" + ccQuery.getName() + "%");
        }

        if (!StringTools.isEmpty(ccQuery.getActivityId())) {
            hqlsb.append(" and {0}.activity.id = :activityId");
            params.put("activityId", ccQuery.getActivityId());
        }

        if (ccQuery.getCurrent()) {
            hqlsb.append(" and {0}.activity.stage != ").append(ActivityStage.FINISH.getValue());
        }

        return hqlsb;
    }

    @Override
    public List<CheckCriterionEntity> paging(CheckCriterionQuery query) {
        if (query == null) {
            return new ArrayList<CheckCriterionEntity>();
        }
        Map<String, Object> params = new HashMap<String, Object>();
        String hql = getHql(query, "c", params);
        hql += " order by c.name";
        return getByHqlPaging(hql, params, query.getPage(), query.getSize());
    }

    @Override
    public boolean existByName(String name) {
        return existBy("name", name);
    }
}
