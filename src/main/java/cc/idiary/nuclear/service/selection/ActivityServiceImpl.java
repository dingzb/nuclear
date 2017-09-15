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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;

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
        if (StringTools.isEmpty(activity.getName())){
            throw new ServiceException("名称不能为空");
        }
        ActivityEntity activityEntity = new ActivityEntity();
        activityEntity.setId(StringTools.randomUUID());
        activityEntity.setName(activity.getName());
        UserEntity user = userDao.getById(activity.getUserId());
        if (user != null){
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
        try{
            acte = activityDao.current();
        } catch (Exception e){
            logger.error("", e);
            throw new ServiceException();
        }
        if (acte != null) {
            ActivityModel actm = new ActivityModel();
            actm.setName(acte.getName());
            actm.setCreateTime(acte.getCreateTime());
            actm.setStage(acte.getStage());
            UserEntity creator = acte.getCreateUser();
            if (creator != null){
                actm.setCreateUserName(acte.getCreateUser().getName());
            }
            return actm;
        } else {
            return null;
        }
    }
}