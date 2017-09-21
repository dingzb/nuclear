package cc.idiary.nuclear.dao.selection;

import cc.idiary.nuclear.config.ActivityStage;
import cc.idiary.nuclear.dao.BaseDaoImpl;
import cc.idiary.nuclear.entity.selection.CategoryGroupEntity;
import cc.idiary.nuclear.query.BaseQuery;
import cc.idiary.nuclear.query.selection.CategoryGroupQuery;
import cc.idiary.utils.common.StringTools;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("categoryGroup")
public class CategoryGroupDaoImpl extends BaseDaoImpl<CategoryGroupEntity> implements CategoryGroupDao {
    public CategoryGroupDaoImpl() {
        super(CategoryGroupEntity.class);
    }

    @Override
    protected StringBuilder getExtensionSql(BaseQuery query, Map<String, Object> params) {
        CategoryGroupQuery cgQuery = (CategoryGroupQuery) query;
        StringBuilder hqlsb = new StringBuilder(" from CategoryGroupEntity {0} where 1=1");
        if (!StringTools.isEmpty(cgQuery.getName())) {
            hqlsb.append(" and {0}.name like :name");
            params.put("name", "%" + cgQuery.getName() + "%");
        }
        if (!StringTools.isEmpty(cgQuery.getActivityId())) {
            hqlsb.append(" and {0}.activity.id = :activityId");
            params.put("activityId", cgQuery.getActivityId());
        }
        return hqlsb;
    }

    @Override
    public List<CategoryGroupEntity> paging(CategoryGroupQuery query) {
        if (query == null) {
            return new ArrayList<CategoryGroupEntity>();
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
