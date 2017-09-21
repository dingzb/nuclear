package cc.idiary.nuclear.dao.selection;

import cc.idiary.nuclear.dao.BaseDao;
import cc.idiary.nuclear.entity.selection.CategoryGroupEntity;
import cc.idiary.nuclear.query.selection.CategoryGroupQuery;

import java.util.List;

public interface CategoryGroupDao extends BaseDao<CategoryGroupEntity> {
    /**
     *
     * @param query
     * @return
     */
    List<CategoryGroupEntity> paging(CategoryGroupQuery query);

    /**
     *
     * @param name
     * @return
     */
    boolean existByName(String name);
}
