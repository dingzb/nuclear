package cc.idiary.nuclear.dao.selection;

import cc.idiary.nuclear.dao.BaseDao;
import cc.idiary.nuclear.entity.selection.CheckCriterionEntity;
import cc.idiary.nuclear.query.selection.CheckCriterionQuery;

import java.util.List;

public interface CheckCriterionDao extends BaseDao<CheckCriterionEntity> {

    /**
     *
     * @param query
     * @return
     */
    List<CheckCriterionEntity> paging(CheckCriterionQuery query);

    boolean existByName(String name);
}
