package cc.idiary.nuclear.service.selection;

import cc.idiary.nuclear.config.ActivityStage;
import cc.idiary.nuclear.dao.selection.ActivityDao;
import cc.idiary.nuclear.dao.selection.AwardCriterionDao;
import cc.idiary.nuclear.dao.selection.AwardDao;
import cc.idiary.nuclear.entity.selection.ActivityEntity;
import cc.idiary.nuclear.entity.selection.AwardCriterionEntity;
import cc.idiary.nuclear.entity.selection.AwardEntity;
import cc.idiary.nuclear.model.selection.AwardCriterionModel;
import cc.idiary.nuclear.service.BaseServiceImpl;
import cc.idiary.nuclear.service.ServiceException;
import cc.idiary.utils.common.StringTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service("awardCriterionService")
public class AwardCriterionServiceImpl extends BaseServiceImpl<AwardCriterionEntity> implements AwardCriterionService {

    private static final Logger logger = LoggerFactory.getLogger(AwardCriterionServiceImpl.class);

    private final AwardCriterionDao awardCriterionDao;
    private final AwardDao awardDao;
    private final ActivityDao activityDao;

    @Autowired
    public AwardCriterionServiceImpl(AwardCriterionDao awardCriterionDao, AwardDao awardDao, ActivityDao activityDao) {
        this.awardCriterionDao = awardCriterionDao;
        this.awardDao = awardDao;
        this.activityDao = activityDao;
    }

    @Override
    @Transactional
    public List<AwardCriterionModel> listByAward(String awardId) throws ServiceException {
        List<AwardCriterionModel> acms = new ArrayList<>();
        try {
            List<AwardCriterionEntity> aces = awardCriterionDao.listByAward(awardId);
            for (AwardCriterionEntity ace : aces) {
                AwardCriterionModel acm = new AwardCriterionModel();
                BeanUtils.copyProperties(ace, acm);
                acms.add(acm);
            }

        } catch (Exception e) {
            logger.error("", e);
        }
        acms.sort((o1, o2) -> {
            if (o1.getPercent() != null && o2.getPercent() != null) {
                return o2.getPercent().compareTo(o1.getPercent());
            } else if (o1.getPercent() == null && o2.getPercent() != null) {
                return 1;
            } else if (o1.getPercent() != null && o2.getPercent() == null) {
                return -1;
            } else {
                return 0;
            }
        });
        return acms;
    }

    @Override
    @Transactional
    public void add(AwardCriterionModel awardCriterion) throws ServiceException {
        if (StringTools.isEmpty(awardCriterion.getAwardId())) {
            throw new ServiceException("关联奖项不能为空");
        }
        if (StringTools.isEmpty(awardCriterion.getName())) {
            throw new ServiceException("名称不能为空");
        }
        if (StringTools.isEmpty(awardCriterion.getCriterion())) {
            throw new ServiceException("标准不能为空");
        }
        if (StringTools.isEmpty(awardCriterion.getPercent())) {
            throw new ServiceException("占比不能为空");
        }

        AwardEntity award = null;
        ActivityEntity activity = null;
        try {
            award = awardDao.getById(awardCriterion.getAwardId());
        } catch (Exception e) {
            logger.error("", e);
            throw new ServiceException();
        }
        if (award == null) {
            throw new ServiceException("关联奖项不存在");
        }

        try {
            activity = award.getActivity();
        } catch (Exception e) {
            logger.error("", e);
            throw new ServiceException();
        }

        if (activity == null) {
            throw new ServiceException("关联奖项所关联活动已经不存在");
        }

        activityCheck(award.getActivity().getId());

        AwardCriterionEntity ace = new AwardCriterionEntity();
        BeanUtils.copyProperties(awardCriterion, ace);
        ace.setId(StringTools.randomUUID());
        ace.setAward(award);
        try {
            awardCriterionDao.save(ace);
        } catch (Exception e) {
            logger.error("", e);
            throw new ServiceException();
        }
    }

    @Override
    @Transactional
    public void edit(AwardCriterionModel awardCriterion) throws ServiceException {
        if (StringTools.isEmpty(awardCriterion.getId())) {
            throw new ServiceException("Id不能为空");
        }
        if (StringTools.isEmpty(awardCriterion.getName())) {
            throw new ServiceException("名称不能为空");
        }
        if (StringTools.isEmpty(awardCriterion.getCriterion())) {
            throw new ServiceException("标准不能为空");
        }
        if (StringTools.isEmpty(awardCriterion.getPercent())) {
            throw new ServiceException("占比不能为空");
        }

        AwardCriterionEntity ace = null;
        try {
            ace = awardCriterionDao.getById(awardCriterion.getId());
        } catch (Exception e) {
            logger.error("", e);
            throw new ServiceException();
        }

        if (ace == null) {
            throw new ServiceException("编辑的奖项标准不存在");
        }

        AwardEntity award = null;
        ActivityEntity activity = null;
        try {
            award = ace.getAward();
        } catch (Exception e) {
            logger.error("", e);
            throw new ServiceException();
        }
        if (award == null) {
            throw new ServiceException("关联奖项不存在");
        }

        try {
            activity = award.getActivity();
        } catch (Exception e) {
            logger.error("", e);
            throw new ServiceException();
        }

        if (activity == null) {
            throw new ServiceException("关联奖项所关联活动已经不存在");
        }

        activityCheck(award.getActivity().getId());

        BeanUtils.copyProperties(awardCriterion, ace);
        try {
            awardCriterionDao.update(ace);
        } catch (Exception e) {
            logger.error("", e);
            throw new ServiceException();
        }
    }

    @Override
    @Transactional
    public void del(String id) throws ServiceException {
        awardCriterionDao.delByIds(new String[]{id});
    }

    /**
     * 判断活动状态是否满足添加或修改
     *
     * @throws ServiceException
     */
    private void activityCheck(String activityId) throws ServiceException {
        ActivityEntity curAct = null;
        try {
            curAct = activityDao.current();
        } catch (Exception e) {
            logger.error("", e);
            throw new ServiceException();
        }
        if (curAct == null || !curAct.getId().equals(activityId)) {
            throw new ServiceException("当前活动不是正在进行的活动");
        }
        if (curAct.getStage() > ActivityStage.COMMIT.getValue()) {
            throw new ServiceException("当前活动已经开始提交项目，不可以再修改奖项配置");
        }

    }
}
