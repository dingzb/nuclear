package cc.idiary.nuclear.dao.selection;

import cc.idiary.nuclear.dao.BaseDao;
import cc.idiary.nuclear.entity.selection.CategoryEntity;

import java.util.List;

public interface CategoryDao extends BaseDao<CategoryEntity> {

    /**
     * 根据专家专业组ID获取
     * @param cGroupId
     * @return
     */
    List<CategoryEntity> getByCategoryGroup(String cGroupId);
}
