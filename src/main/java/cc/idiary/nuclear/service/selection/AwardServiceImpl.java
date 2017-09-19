package cc.idiary.nuclear.service.selection;

import cc.idiary.nuclear.config.ActivityStage;
import cc.idiary.nuclear.dao.selection.ActivityDao;
import cc.idiary.nuclear.dao.selection.AwardDao;
import cc.idiary.nuclear.dao.selection.AwardTypeDao;
import cc.idiary.nuclear.entity.selection.ActivityEntity;
import cc.idiary.nuclear.entity.selection.AwardEntity;
import cc.idiary.nuclear.entity.selection.AwardTypeEntity;
import cc.idiary.nuclear.model.PagingModel;
import cc.idiary.nuclear.model.selection.AwardModel;
import cc.idiary.nuclear.query.selection.AwardQuery;
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
import java.util.Date;
import java.util.List;

@Service("awardService")
public class AwardServiceImpl extends BaseServiceImpl<AwardEntity> implements AwardService {

    private static final Logger logger = LoggerFactory.getLogger(AwardServiceImpl.class);

    private final AwardDao awardDao;
    private final ActivityDao activityDao;
    private final AwardTypeDao awardTypeDao;

    @Autowired
    public AwardServiceImpl(AwardDao awardDao, ActivityDao activityDao, AwardTypeDao awardTypeDao) {
        this.awardDao = awardDao;
        this.activityDao = activityDao;
        this.awardTypeDao = awardTypeDao;
    }

    @Transactional
    @Override
    public PagingModel paging(AwardQuery query) throws ServiceException {
        PagingModel page = new PagingModel();
        List<AwardModel> ams = new ArrayList<>();
        try {
            List<AwardEntity> aes = awardDao.paging(query);
            for (AwardEntity ae : aes) {
                AwardModel am = new AwardModel();
                BeanUtils.copyProperties(ae, am);
                AwardTypeEntity type = ae.getType();
                if (type != null) {
                    am.setTypeId(type.getId());
                    am.setTypeName(type.getName());
                }
                ams.add(am);
            }
            page.setRows(ams);
            long total = awardDao.getCount(query);
            page.setTotal(total);
            return page;
        } catch (Exception e) {
            logger.error("", e);
            throw new ServiceException();
        }
    }

    @Override
    @Transactional
    public void add(AwardModel award) throws ServiceException {
        if (StringTools.isEmpty(award.getName())) {
            throw new ServiceException("名称不能为空");
        }
        if (StringTools.isEmpty(award.getTypeId())) {
            throw new ServiceException("类型不能为空");
        }
        ActivityEntity curAct = activityCheck(award.getActivityId());
        AwardEntity ae = new AwardEntity();
        ae.setId(StringTools.randomUUID());
        ae.setCreateTime(new Date());
        ae.setName(award.getName());
        ae.setDescription(award.getDescription());
        try {
            AwardTypeEntity type = awardTypeDao.getById(award.getTypeId());
            ae.setActivity(curAct);
            ae.setType(type);
            awardDao.save(ae);
        } catch (Exception e) {
            logger.error("", e);
            throw new ServiceException();
        }

    }

    @Override
    @Transactional
    public void edit(AwardModel award) throws ServiceException {
        if (StringTools.isEmpty(award.getId())){
            throw new ServiceException("奖项不存在");
        }
        if (StringTools.isEmpty(award.getName())){
            throw new ServiceException("名称不能为空");
        }

        AwardEntity ae = null;
        try {
            ae = awardDao.getById(award.getId());
        } catch (Exception e) {
            logger.error("", e);
            throw new ServiceException();
        }
        if (ae == null) {
            throw new ServiceException("奖项不存在");
        }

        ActivityEntity curAct = activityCheck(ae.getActivity().getId());

        ae.setName(award.getName());
        ae.setDescription(award.getDescription());

        try {
            AwardTypeEntity type = awardTypeDao.getById(award.getTypeId());
            ae.setType(type);
            awardDao.update(ae);
        } catch (Exception e) {
            logger.error("", e);
            throw new ServiceException();
        }
    }

    @Override
    @Transactional
    public void updateLevel(AwardModel award) throws ServiceException {
        if (StringTools.isEmpty(award.getId())){
            throw new ServiceException("奖项不存在");
        }

        AwardEntity ae = null;
        try {
            ae = awardDao.getById(award.getId());
        } catch (Exception e) {
            logger.error("", e);
            throw new ServiceException();
        }
        if (ae == null) {
            throw new ServiceException("奖项不存在");
        }

        activityCheck(ae.getActivity().getId());

        ae.setFirst(award.getFirst());
        ae.setSecond(award.getSecond());
        ae.setThird(award.getThird());
        ae.setDeny(award.getDeny());

        try {
            awardDao.update(ae);
        } catch (Exception e) {
            logger.error("", e);
            throw new ServiceException();
        }
    }

    @Override
    @Transactional
    public boolean existByName(String name) throws ServiceException {
        try {
            return awardDao.existByName(name);
        } catch (Exception e) {
            logger.error("", e);
            throw new ServiceException();
        }
    }

    @Override
    @Transactional
    public void del(String[] ids) throws ServiceException {
        try {
            for (String id : ids) {
                AwardEntity award = awardDao.getById(id);
                if (award != null){
                    awardDao.delete(award);
                }
            }
        } catch (Exception e) {
            logger.error("", e);
            throw new ServiceException();
        }
    }

    /**
     * 判断活动状态是否满足添加或修改
     * @throws ServiceException
     */
    private ActivityEntity activityCheck(String activityId) throws ServiceException {
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
            throw new ServiceException("当前活动已经开始提交项目，不可以再创建奖项");
        }

        return curAct;
    }
}
