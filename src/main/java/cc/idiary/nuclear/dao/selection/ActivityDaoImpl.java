package cc.idiary.nuclear.dao.selection;

import cc.idiary.nuclear.config.ActivityStage;
import cc.idiary.nuclear.dao.BaseDaoImpl;
import cc.idiary.nuclear.entity.selection.ActivityEntity;
import cc.idiary.nuclear.query.BaseQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("activityDao")
public class ActivityDaoImpl extends BaseDaoImpl<ActivityEntity> implements ActivityDao {
    public ActivityDaoImpl() {
        super(ActivityEntity.class);
    }

    @Override
    protected StringBuilder getExtensionSql(BaseQuery query, Map<String, Object> params) {
        throw new RuntimeException("not support yet");
    }

    @Override
    public Boolean existByUsername(String name) {
        return existBy("name", name);
    }

    @Override
    public ActivityEntity current() {
        String hql = "from ActivityEntity act where act.stage != " + ActivityStage.FINISH.getValue();
        List<ActivityEntity> acts = getByHql(hql, null);
        if (acts.isEmpty()){
            return null;
        } else {
            return acts.get(0);
        }
    }

    @Override
    public boolean existByName(String name) {
        return existBy("name", name);
    }
}
