package cc.idiary.nuclear.dao.selection;

import cc.idiary.nuclear.dao.BaseDaoImpl;
import cc.idiary.nuclear.entity.selection.CategoryEntity;
import cc.idiary.nuclear.query.BaseQuery;
import org.springframework.stereotype.Repository;

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
}
