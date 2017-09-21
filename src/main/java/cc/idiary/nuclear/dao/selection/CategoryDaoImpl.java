package cc.idiary.nuclear.dao.selection;

import cc.idiary.nuclear.dao.BaseDaoImpl;
import cc.idiary.nuclear.entity.selection.CategoryEntity;
import cc.idiary.nuclear.query.BaseQuery;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("categoryDao")
public class CategoryDaoImpl extends BaseDaoImpl<CategoryEntity> implements CategoryDao {
    public CategoryDaoImpl() {
        super(CategoryEntity.class);
    }

    @Override
    protected StringBuilder getExtensionSql(BaseQuery query, Map<String, Object> params) {
        return null;
    }

    @Override
    public List<CategoryEntity> getByCategoryGroup(String cGroupId) {
        String hql = " from CategoryEntity c where :cGroupId member of c.categoryGroups";
        Map<String, Object> params = new HashMap<>();
        params.put("cGroupId", cGroupId);
        return getByHql(hql, params);
    }
}
