package cc.idiary.nuclear.dao.selection;

import cc.idiary.nuclear.dao.BaseDaoImpl;
import cc.idiary.nuclear.entity.selection.AwardCriterionEntity;
import cc.idiary.nuclear.query.BaseQuery;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("awardCriterionDao")
public class AwardCriterionDaoImpl extends BaseDaoImpl<AwardCriterionEntity> implements AwardCriterionDao {
    public AwardCriterionDaoImpl() {
        super(AwardCriterionEntity.class);
    }

    @Override
    protected StringBuilder getExtensionSql(BaseQuery query, Map<String, Object> params) {
        return null;
    }

    @Override
    public List<AwardCriterionEntity> listByAward(String awardId) {
        String hql = "from AwardCriterionEntity a where a.award.id = :awardId";
        Map<String, Object> param = new HashMap<>();
        param.put("awardId", awardId);
        return getByHql(hql, param);
    }
}
