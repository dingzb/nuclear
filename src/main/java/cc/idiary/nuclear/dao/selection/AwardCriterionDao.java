package cc.idiary.nuclear.dao.selection;

import cc.idiary.nuclear.dao.BaseDao;
import cc.idiary.nuclear.entity.selection.AwardCriterionEntity;

import java.util.List;

public interface AwardCriterionDao extends BaseDao<AwardCriterionEntity> {

    /**
     * 根据奖项列出奖项标准
     *
     * @param awardId
     * @return
     */
    List<AwardCriterionEntity> listByAward(String awardId);
}
