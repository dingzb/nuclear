package cc.idiary.nuclear.service.selection;

import cc.idiary.nuclear.entity.selection.AwardCriterionEntity;
import cc.idiary.nuclear.model.selection.AwardCriterionModel;
import cc.idiary.nuclear.service.BaseService;
import cc.idiary.nuclear.service.ServiceException;

import java.util.List;

public interface AwardCriterionService extends BaseService<AwardCriterionEntity> {

    /**
     * 根据奖项Id列出所有奖项标准
     *
     * @param awardId
     * @return
     * @throws ServiceException
     */
    List<AwardCriterionModel> listByAward(String awardId) throws ServiceException;

    void add(AwardCriterionModel awardCriterion) throws ServiceException;

    void edit(AwardCriterionModel awardCriterion) throws ServiceException;

    void del(String[] ids) throws ServiceException;

}
