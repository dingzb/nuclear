package cc.idiary.nuclear.service.system;

import java.io.Serializable;
import java.util.*;

import cc.idiary.nuclear.model.PagingModel;
import cc.idiary.nuclear.service.BaseServiceImpl;
import cc.idiary.nuclear.service.ServiceException;
import cc.idiary.utils.common.StringTools;
import cc.idiary.nuclear.dao.system.UserDao;
import cc.idiary.nuclear.entity.system.RoleEntity;
import cc.idiary.nuclear.entity.system.UserEntity;
import cc.idiary.nuclear.logger.BusinessLog;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cc.idiary.nuclear.dao.system.ActionDao;
import cc.idiary.nuclear.dao.system.StateDao;
import cc.idiary.nuclear.entity.system.ActionEntity;
import cc.idiary.nuclear.entity.system.StateEntity;
import cc.idiary.nuclear.logger.LogOperationType;
import cc.idiary.nuclear.model.system.ActionModel;
import cc.idiary.nuclear.query.system.ActionQuery;

@Service("actionService")
public class ActionServiceImpl extends BaseServiceImpl<ActionEntity> implements ActionService {

    private static final Logger logger = LogManager
            .getLogger(ActionServiceImpl.class);

    @Autowired
    private ActionDao actionDao;
    @Autowired
    private StateDao stateDao;
    @Autowired
    private UserDao userDao;

    @Override
    @Transactional
    public List<ActionModel> getByStateCodeIds(Set<String> stateCodeIds)
            throws ServiceException {
        List<ActionEntity> actionEntitys = actionDao
                .getByStateCodes(stateCodeIds);
        List<ActionModel> actionModels = new ArrayList<ActionModel>();
        for (ActionEntity actionEntity : actionEntitys) {
            ActionModel actionModel = new ActionModel();
            BeanUtils.copyProperties(actionEntity, actionModel);
            actionModels.add(actionModel);
        }
        return actionModels;
    }

    @Override
    @Transactional
    @BusinessLog(content = "添加访问控制", operation = LogOperationType.ADD)
    public Serializable add(ActionModel actionModel) throws ServiceException {

        if (StringTools.isEmpty(actionModel.getName()))
            throw new ServiceException("没有指定名称");
        if (StringTools.isEmpty(actionModel.getCode()))
            throw new ServiceException("没有指定编码");
        if (StringTools.isEmpty(actionModel.getUrl()))
            throw new ServiceException("没有指定URL");

        ActionEntity actionEntity = new ActionEntity();
        BeanUtils.copyProperties(actionModel, actionEntity);

        if (StringTools.isEmpty(actionEntity.getId())) {
            actionEntity.setId(StringTools.randomUUID());
        }
        if (actionModel.getStateId() != null
                && !actionModel.getStateId().isEmpty()) {
            StateEntity state = null;
            try {
                state = stateDao.getById(actionModel.getStateId());
            } catch (Exception e) {
                logger.catching(e);
                return new ServiceException();
            }
            actionEntity.setState(state);
        }
        actionEntity.setCreateTime(new Date());
        actionEntity.setModifyTime(new Date());

        try {
            return actionDao.save(actionEntity);
        } catch (Exception e) {
            logger.catching(e);
            throw new ServiceException();
        }
    }

    @Override
    @Transactional
    public PagingModel getPaging(ActionQuery query) throws ServiceException {
        Set<String> actionIds = null;
        if (query.getRoleIds() != null && !query.getRoleIds().isEmpty()) {
            List<ActionEntity> actionEntitys = null;
            try {
                actionEntitys = actionDao.getByRoleIds(query.getRoleIds());
            } catch (Exception e) {
                logger.catching(e);
                throw new ServiceException();
            }
            actionIds = getIds(actionEntitys);
        }
        return getPagingwithSelected(query, actionIds);

    }

    /**
     * 获取分页列表，根据角色判断是否包含在角色中并选中
     *
     * @param query
     * @param actionIds
     * @return
     * @throws ServiceException
     */
    private PagingModel getPagingwithSelected(ActionQuery query,
                                              Set<String> actionIds) throws ServiceException {
        PagingModel paging = new PagingModel();
        List<ActionEntity> actionEntitys = null;
        Long total = null;

        try {
            actionEntitys = actionDao.getPaging(query);
            total = actionDao.getCount(query);
        } catch (Exception e) {
            logger.catching(e);
            throw new ServiceException();
        }

        List<ActionModel> actionModels = new ArrayList<ActionModel>();

        for (ActionEntity actionEntity : actionEntitys) {
            ActionModel actionModel = new ActionModel();
            BeanUtils.copyProperties(actionEntity, actionModel);

            if (actionEntity.getState() != null) {
                actionModel.setStateId(actionEntity.getState().getId());
            }

            if (actionIds != null && actionIds.contains(actionEntity.getId())) {
                actionModel.setSelected(true);
            }

            actionModels.add(actionModel);
        }
        paging.setRows(actionModels);
        paging.setTotal(total);

        return paging;
    }

    @Override
    @Transactional
    @BusinessLog(content = "编辑访问控制", operation = LogOperationType.MODIFY)
    public void edit(ActionModel actionModel) throws ServiceException {

        ActionEntity actionEntity = actionDao.getById(actionModel.getId());

        if (actionEntity == null)
            throw new ServiceException("修改对象不存在");
        if (actionModel.getName() != null && !actionModel.getName().isEmpty()) {
            actionEntity.setName(actionModel.getName());
        }
        if (actionModel.getCode() != null && !actionModel.getCode().isEmpty()) {
            actionEntity.setCode(actionModel.getCode());
        }
        if (actionModel.getUrl() != null && !actionModel.getUrl().isEmpty()) {
            actionEntity.setUrl(actionModel.getUrl());
        }
        if (!StringTools.isEmpty(actionModel.getStateId())) {
            try {
                StateEntity state = stateDao.getById(actionModel.getStateId());
                actionEntity.setState(state);
            } catch (Exception e) {
                logger.catching(e);
                throw new ServiceException();
            }
        }
        actionEntity.setModifyTime(new Date());
        try {
            actionDao.update(actionEntity);
        } catch (Exception e) {
            logger.catching(e);
            throw new ServiceException();
        }

    }

    @Override
    @Transactional
    public List<ActionModel> getByRoleIds(Set<String> roleIds)
            throws ServiceException {
        List<ActionEntity> actionEntitys = null;
        List<ActionModel> actionModels = new ArrayList<ActionModel>();
        try {
            actionEntitys = actionDao.getByRoleIds(roleIds);
        } catch (Exception e) {
            logger.catching(e);
            throw new ServiceException();
        }
        for (ActionEntity actionEntity : actionEntitys) {
            ActionModel actionModel = new ActionModel();
            BeanUtils.copyProperties(actionEntity, actionModel);
            actionModels.add(actionModel);
        }
        return actionModels;
    }

    @Override
    @Transactional
    public Integer delete(String[] ids) throws ServiceException {
        Integer result = null;
        try {
            result = actionDao.delByIds(ids);
        } catch (Exception e) {
            logger.catching(e);
            throw new ServiceException();
        }
        return result;
    }

    @Override
    @Transactional
    @BusinessLog(content = "删除访问控制", operation = LogOperationType.DELETE)
    public void delete(String id) throws ServiceException {
        ActionEntity e = actionDao.getById(id);
        if (e == null)
            throw new ServiceException("该记录不存在！");
        try {
            actionDao.delete(e);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ServiceException();
        }
    }

    @Override
    @Transactional
    public List<ActionModel> getList() throws ServiceException {
        List<ActionEntity> actionEntitys;
        List<ActionModel> actionModels = new ArrayList<ActionModel>();
        try {
            actionEntitys = actionDao.getList();
            if (actionEntitys != null && !actionEntitys.isEmpty()) {
                for (ActionEntity actionEntity : actionEntitys) {
                    ActionModel actionModel = new ActionModel();
                    BeanUtils.copyProperties(actionEntity, actionModel);
                    actionModels.add(actionModel);
                }
            }
        } catch (Exception e) {
            logger.catching(e);
            throw new ServiceException();
        }
        return actionModels;
    }

    @Override
    @Transactional
    public List<ActionModel> getByUserId(String userId) throws ServiceException {
        Set<String> roleIds = new HashSet<String>();
        if (StringTools.isEmpty(userId)) {
            return getByRoleIds(roleIds);
        }
        try {
            UserEntity userEntity = userDao.getById(userId);
            if (userEntity.getRoles() != null) {
                for (RoleEntity role : userEntity.getRoles()) {
                    roleIds.add(role.getId());
                }
            }
        } catch (Exception e) {
            logger.catching(e);
            throw new ServiceException();
        }
        return getByRoleIds(roleIds);
    }
}
