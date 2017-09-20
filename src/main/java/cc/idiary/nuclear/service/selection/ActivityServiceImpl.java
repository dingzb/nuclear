package cc.idiary.nuclear.service.selection;

import cc.idiary.nuclear.config.ActivityStage;
import cc.idiary.nuclear.dao.selection.ActivityDao;
import cc.idiary.nuclear.dao.system.UserDao;
import cc.idiary.nuclear.entity.selection.ActivityEntity;
import cc.idiary.nuclear.entity.selection.StageEntity;
import cc.idiary.nuclear.entity.system.UserEntity;
import cc.idiary.nuclear.model.selection.ActivityModel;
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
import java.util.Map;

@Service("activityService")
public class ActivityServiceImpl extends BaseServiceImpl<ActivityEntity> implements ActivityService {

    private Logger logger = LoggerFactory.getLogger(ActivityServiceImpl.class);

    private final ActivityDao activityDao;
    private final UserDao userDao;

    @Autowired
    public ActivityServiceImpl(ActivityDao activityDao, UserDao userDao) {
        this.activityDao = activityDao;
        this.userDao = userDao;
    }

    @Override
    @Transactional
    public boolean create(ActivityModel activity) throws ServiceException {
        if (StringTools.isEmpty(activity.getName())) {
            throw new ServiceException("名称不能为空");
        }
        if (activityDao.existByName(activity.getName())) {
            throw new ServiceException("名称已经存在");
        }
        ActivityEntity activityEntity = new ActivityEntity();
        activityEntity.setId(StringTools.randomUUID());
        activityEntity.setName(activity.getName());
        UserEntity user = userDao.getById(activity.getUserId());
        if (user != null) {
            activityEntity.setCreateUser(user);
        }
        Date now = new Date();
        activityEntity.setCreateTime(now);
        activityEntity.setStage(ActivityStage.START.getValue());
        StageEntity startStage = new StageEntity();
        startStage.setId(StringTools.randomUUID());
        startStage.setStartTime(now);
        activityEntity.setStartStage(startStage);
        activityDao.save(activityEntity);
        logger.debug("create activity with name " + activity.getName());
        return false;
    }

    @Transactional
    @Override
    public ActivityModel current() throws ServiceException {
        ActivityEntity acte = null;
        try {
            acte = activityDao.current();
        } catch (Exception e) {
            logger.error("", e);
            throw new ServiceException();
        }
        if (acte != null) {
            ActivityModel actm = new ActivityModel();
            BeanUtils.copyProperties(acte, actm);
            UserEntity creator = acte.getCreateUser();
            if (creator != null) {
                actm.setCreateUserName(acte.getCreateUser().getName());
            }
            return actm;
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public List<ActivityModel> list() throws ServiceException {
        List<ActivityModel> actms = new ArrayList<>();
        try {
            List<ActivityEntity> actes = activityDao.getList();
            if (actes != null) {

                for (ActivityEntity acte : actes) {
                    ActivityModel actm = new ActivityModel();
                    actm.setName(acte.getName());
                    actm.setCreateTime(acte.getCreateTime());
                    actm.setStage(acte.getStage());
                    UserEntity creator = acte.getCreateUser();
                    if (creator != null) {
                        actm.setCreateUserName(acte.getCreateUser().getName());
                    }
                    actms.add(actm);
                }
            }
            return actms;
        } catch (Exception e) {
            throw new ServiceException();
        }
    }

    @Override
    @Transactional
    public ActivityModel getLevelLimit(String id) throws ServiceException {
        ActivityEntity curActe = null;
        try {
            curActe = activityDao.current();
        } catch (Exception e) {
            logger.error("", e);
            throw new ServiceException();
        }
        if (curActe == null || !id.equals(curActe.getId())) {
            throw new ServiceException("当前活动不是正在进行的活动");
        }
        if (curActe.getStage() >= ActivityStage.FINAL.getValue()) {
            throw new ServiceException("当前活动已进行到终审阶段不能进行限制修改");
        }
        ActivityModel actm = new ActivityModel();
        actm.setId(curActe.getId());
        actm.setLimitFirst(curActe.getLimitFirst());
        actm.setLimitSecond(curActe.getLimitSecond());
        return actm;
    }

    @Override
    @Transactional
    public void editLevelLimit(ActivityModel activity) throws ServiceException {
        if (StringTools.isEmpty(activity.getId())){
            throw new ServiceException("指定活动不存在");
        }
        ActivityEntity curActe = null;
        try {
            curActe = activityDao.current();
        } catch (Exception e) {
            logger.error("", e);
            throw new ServiceException();
        }
        if (curActe == null || !curActe.getId().equals(activity.getId())) {
            throw new ServiceException("当前活动不是正在进行的活动");
        }
        if (curActe.getStage() >= ActivityStage.FINAL.getValue()) {
            throw new ServiceException("当前活动已进行到终审阶段不能进行限制修改");
        }

        curActe.setLimitFirst(activity.getLimitFirst());
        curActe.setLimitSecond(activity.getLimitSecond());

        try {
            activityDao.update(curActe);
        } catch (Exception e) {
            logger.error("", e);
            throw new ServiceException();
        }
    }
}
