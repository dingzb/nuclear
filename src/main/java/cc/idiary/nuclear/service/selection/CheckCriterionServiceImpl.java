package cc.idiary.nuclear.service.selection;

import cc.idiary.nuclear.config.ActivityStage;
import cc.idiary.nuclear.dao.selection.ActivityDao;
import cc.idiary.nuclear.dao.selection.CheckCriterionDao;
import cc.idiary.nuclear.dao.system.UserDao;
import cc.idiary.nuclear.entity.selection.ActivityEntity;
import cc.idiary.nuclear.entity.selection.CheckCriterionEntity;
import cc.idiary.nuclear.entity.system.UserEntity;
import cc.idiary.nuclear.model.PagingModel;
import cc.idiary.nuclear.model.selection.CheckCriterionModel;
import cc.idiary.nuclear.query.selection.CheckCriterionQuery;
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

@Service("checkCriterionService")
public class CheckCriterionServiceImpl extends BaseServiceImpl<CheckCriterionEntity> implements CheckCriterionService {

    private static final Logger logger = LoggerFactory.getLogger(CheckCriterionServiceImpl.class);

    private CheckCriterionDao checkCriterionDao;
    private ActivityDao activityDao;
    private UserDao userDao;

    @Autowired
    public CheckCriterionServiceImpl(CheckCriterionDao checkCriterionDao, ActivityDao activityDao, UserDao userDao) {
        this.checkCriterionDao = checkCriterionDao;
        this.activityDao = activityDao;
        this.userDao = userDao;
    }

    @Transactional
    @Override
    public PagingModel paging(CheckCriterionQuery query) throws ServiceException {
        PagingModel page = new PagingModel();
        List<CheckCriterionModel> ccms = new ArrayList<>();
        try {
            List<CheckCriterionEntity> cces = checkCriterionDao.paging(query);
            for (CheckCriterionEntity cce : cces) {
                CheckCriterionModel ccm = new CheckCriterionModel();
                BeanUtils.copyProperties(cce, ccm);
//                UserEntity creator = cce.getCreateUser();
//                if (creator != null) {
//                    ccm.setCreateUserName(creator.getName());
//                    ccm.setCreateUserId(creator.getId());
//                }
                ccms.add(ccm);
            }
            page.setRows(ccms);
            long total = checkCriterionDao.getCount(query);
            page.setTotal(total);
            return page;
        } catch (Exception e) {
            logger.error("", e);
            throw new ServiceException();
        }
    }

    @Override
    @Transactional
    public void add(CheckCriterionModel checkCriterion) throws ServiceException {
        if (StringTools.isEmpty(checkCriterion.getName())) {
            throw new ServiceException("名称不能为空");
        }
        ActivityEntity curAct = null;
        try {
            curAct = activityDao.current();
        } catch (Exception e) {
            logger.error("", e);
            throw new ServiceException();
        }
        if (curAct == null || !curAct.getId().equals(checkCriterion.getActivityId())) {
            throw new ServiceException("当前活动不是正在进行的活动");
        }
        if (curAct.getStage() > ActivityStage.CHECK.getValue()) {
            throw new ServiceException("当前活动已经完成审查工作，不可以再创建审查标准");
        }
        CheckCriterionEntity cce = new CheckCriterionEntity();
        cce.setId(StringTools.randomUUID());
        cce.setCreateTime(new Date());
        cce.setName(checkCriterion.getName());
        cce.setDescription(checkCriterion.getDescription());
        try {
            UserEntity creator = userDao.getById(checkCriterion.getUserId());
            cce.setCreateUser(creator);
            cce.setActivity(curAct);
            checkCriterionDao.save(cce);
        } catch (Exception e) {
            logger.error("", e);
            throw new ServiceException();
        }

    }

    @Override
    @Transactional
    public void edit(CheckCriterionModel checkCriterion) throws ServiceException {
        if (StringTools.isEmpty(checkCriterion.getId())){
            throw new ServiceException("编辑的标准不存在");
        }
        if (StringTools.isEmpty(checkCriterion.getName())){
            throw new ServiceException("名称不能为空");
        }

        CheckCriterionEntity cce = null;
        try {
            cce = checkCriterionDao.getById(checkCriterion.getId());
        } catch (Exception e) {
            logger.error("", e);
            throw new ServiceException();
        }
        if (cce == null) {
            throw new ServiceException("编辑的标准不存在");
        }

        ActivityEntity curAct = null;
        try {
            curAct = activityDao.current();
        } catch (Exception e) {
            logger.error("", e);
            throw new ServiceException();
        }
        if (curAct == null || !curAct.getId().equals(cce.getActivity().getId())) {
            throw new ServiceException("当前活动不是正在进行的活动");
        }
        if (curAct.getStage() > ActivityStage.CHECK.getValue()) {
            throw new ServiceException("当前活动已经完成审查工作，不可以再创建审查标准");
        }

        cce.setName(checkCriterion.getName());
        cce.setDescription(checkCriterion.getDescription());
        try {
            checkCriterionDao.update(cce);
        } catch (Exception e) {
            logger.error("", e);
            throw new ServiceException();
        }
    }

    @Override
    @Transactional
    public boolean existByName(String name) throws ServiceException {
        try {
            return checkCriterionDao.existByName(name);
        } catch (Exception e) {
            logger.error("", e);
            throw new ServiceException();
        }
    }

    @Override
    @Transactional
    public void del(String[] ids) throws ServiceException {
        checkCriterionDao.delByIds(ids);
    }
}
