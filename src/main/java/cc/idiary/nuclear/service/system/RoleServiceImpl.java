package cc.idiary.nuclear.service.system;

import cc.idiary.nuclear.config.UserType;
import cc.idiary.nuclear.dao.system.RoleDao;
import cc.idiary.nuclear.dao.system.StateDao;
import cc.idiary.nuclear.dao.system.UserDao;
import cc.idiary.nuclear.entity.system.UserEntity;
import cc.idiary.nuclear.logger.BusinessLog;
import cc.idiary.nuclear.query.system.RoleQuery;
import cc.idiary.nuclear.config.RoleType;
import cc.idiary.nuclear.dao.system.ActionDao;
import cc.idiary.nuclear.entity.system.ActionEntity;
import cc.idiary.nuclear.entity.system.RoleEntity;
import cc.idiary.nuclear.entity.system.StateEntity;
import cc.idiary.nuclear.logger.LogOperationType;
import cc.idiary.nuclear.model.PagingModel;
import cc.idiary.nuclear.model.system.RoleModel;
import cc.idiary.nuclear.model.system.UserModel;
import cc.idiary.nuclear.service.BaseServiceImpl;
import cc.idiary.nuclear.service.ServiceException;
import cc.idiary.utils.common.StringTools;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.*;

@Service("roleService")
public class RoleServiceImpl extends BaseServiceImpl<RoleEntity> implements RoleService {

    private static final Logger logger = LogManager
            .getLogger(RoleServiceImpl.class);

    @Autowired
    private RoleDao roleDao;
    @Autowired
    private StateDao stateDao;
    @Autowired
    private ActionDao actionDao;
    @Autowired
    private UserDao userDao;

    @Override
    @Transactional
    @BusinessLog(content = "添加角色", operation = LogOperationType.ADD)
    public Serializable add(RoleModel roleModel) throws ServiceException {

        if (StringTools.isEmpty(roleModel.getName()))
            throw new ServiceException("名称不能为空");

        RoleEntity roleEntity = new RoleEntity();

        try {
            if (roleDao.existByName(roleModel.getName()))
                throw new ServiceException("该名称的角色已经存在");
            BeanUtils.copyProperties(roleModel, roleEntity);
            if (StringTools.isEmpty(roleEntity.getId())) {
                roleEntity.setId(StringTools.randomUUID());
            }
            RoleType type = RoleType.valueOf(roleModel.getType());
            roleEntity.setCreateTime(new Date());
            roleEntity.setModifyTime(new Date());
            roleEntity.setType(type);
            return roleDao.save(roleEntity);
        } catch (Exception e) {
            logger.catching(e);
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public PagingModel getPaging(RoleQuery query) throws ServiceException {
        PagingModel paging = new PagingModel();
        Long total = null;
        List<RoleEntity> roleEntities = new ArrayList<RoleEntity>();
        List<RoleModel> roleModels = new ArrayList<RoleModel>();
        Set<RoleType> types = new HashSet<RoleType>();
        if (query.isUserCheck()) {
            types.add(RoleType.USER);
        }
        types.add(RoleType.NORMAL);

        query.setTypes(types);

        UserEntity user = null;
        try {
            user = userDao.getById(query.getUserId());
        } catch (Exception e) {
            logger.catching(e);
            throw new ServiceException("获取角色列表失败");
        }
        if (user == null) {
            throw new ServiceException("当前用户已经不存在");
        }

        if (UserType.BUILD_IN.equals(user.getType())) {
            // 超级管理员supreme能够显示[管理员、普通]
            types.add(RoleType.ADMIN);
        }
        try {
            roleEntities = roleDao.getPaging(query);
            total = roleDao.getCount(query);
        } catch (Exception e) {
            logger.catching(e);
            throw new ServiceException("获取角色列表失败");
        }
        for (RoleEntity roleEntity : roleEntities) {
            RoleModel roleModel = new RoleModel();
            BeanUtils.copyProperties(roleEntity, roleModel);
            roleModel.setType(roleEntity.getType().toString());
            roleModels.add(roleModel);
        }
        paging.setRows(roleModels);
        paging.setTotal(total);
        return paging;
    }

    @Override
    @Transactional
    @BusinessLog(content = "编辑角色", operation = LogOperationType.MODIFY)
    public void edit(RoleModel roleModel) throws ServiceException {
        if (StringTools.isEmpty(roleModel.getId()))
            throw new ServiceException("没有指定Id");

        RoleEntity roleEntity = null;
        try {
            roleEntity = roleDao.getById(roleModel.getId());
        } catch (Exception e) {
            logger.catching(e);
            throw new ServiceException();
        }

        if (roleEntity == null) {
            throw new ServiceException("指定对象不存在");
        }
        if (!StringTools.isEmpty(roleModel.getName())) {
            roleEntity.setName(roleModel.getName());
        }
//		if (!StringTools.isEmpty(roleModel.getDescription())) {
        roleEntity.setDescription(roleModel.getDescription());
//		}
        roleEntity.setModifyTime(new Date());
        RoleType type = RoleType.valueOf(roleModel.getType());
        roleEntity.setType(type);
        try {
            roleDao.update(roleEntity);
        } catch (Exception e) {
            logger.catching(e);
            throw new ServiceException();
        }

    }

    @Override
    @Transactional
    @BusinessLog(content = "权限配置", operation = LogOperationType.MODIFY)
    public void saveAction(String roleId, String[] actionIds, String stateId)
            throws ServiceException {
        saveAuth(roleId, null, actionIds, stateId);
    }

    @Override
    @Transactional
    @BusinessLog(content = "目录配置", operation = LogOperationType.MODIFY)
    public void saveState(String roleId, String[] stateIds) throws ServiceException {
        saveAuth(roleId, stateIds, null, null);
    }

    /**
     * 保存权限
     *
     * @param roleId
     * @param stateIds
     * @param actionIds
     * @throws ServiceException
     */
    private void saveAuth(String roleId, String[] stateIds, String[] actionIds,
                          String stateId) throws ServiceException {

        if (StringTools.isEmpty(roleId))
            throw new ServiceException("没有指定角色");

        RoleEntity role = null;

        try {
            role = roleDao.getById(roleId);
            if (role == null) {
                throw new ServiceException("角色不存在");
            }
            if (stateIds != null && stateIds.length > 0) {
                Set<StateEntity> states = stateDao.getByIds(Arrays.asList(stateIds));
                Set<ActionEntity> actions = role.getActions();
                Iterator<ActionEntity> iterator = actions.iterator();
                while (iterator.hasNext()) {
                    ActionEntity ac = iterator.next();
                    if (!states.contains(ac.getState())) {
                        iterator.remove();
                    }
                }
                role.setStates(states);
                role.setActions(actions);
            }
            if (actionIds != null && actionIds.length > 0
                    && !StringTools.isEmpty(stateId)) {
                Set<ActionEntity> actions = actionDao.getByIds(Arrays
                        .asList(actionIds));
                Set<ActionEntity> existActions = role.getActions();
                List<ActionEntity> stateActions = actionDao
                        .getByStateId(stateId);
                existActions.removeAll(stateActions);
                existActions.addAll(actions);
            } else if (actionIds == null && !StringTools.isEmpty(stateId)) {
                Set<ActionEntity> actions = new HashSet<ActionEntity>();
                Set<ActionEntity> existActions = role.getActions();
                List<ActionEntity> stateActions = actionDao
                        .getByStateId(stateId);
                existActions.removeAll(stateActions);
                existActions.addAll(actions);
            }

            roleDao.update(role);
        } catch (Exception e) {
            logger.catching(e);
            throw new ServiceException();
        }

    }

    @Override
    @Transactional
    @BusinessLog(content = "删除角色", operation = LogOperationType.DELETE)
    public Integer delete(String[] ids) throws ServiceException {
        Integer result = null;
        try {
            result = roleDao.delByIds(ids);
        } catch (Exception e) {
            logger.catching(e);
            throw new ServiceException();
        }
        return result;
    }

    @Override
    @Transactional
    @BusinessLog(content = "删除角色", operation = LogOperationType.DELETE)
    public void delete(String id) throws ServiceException {
        RoleEntity e = roleDao.getById(id);
        if (e == null)
            throw new ServiceException("该记录不存在！");
        try {
            roleDao.delete(e);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ServiceException();
        }
    }

    @Override
    @Transactional
    public boolean existByName(String name) throws ServiceException {
        boolean exist = false;
        try {
            exist = roleDao.existByName(name);
        } catch (Exception e) {
            logger.catching(e);
            throw new ServiceException();
        }
        return exist;
    }

    @Transactional
    @Override
    public List<String> getTypes(UserModel user) throws ServiceException {
        List<String> types = new ArrayList<String>();

        if (user == null || StringTools.isEmpty(user.getType())) {
            types.add(RoleType.ADMIN.toString());
            types.add(RoleType.NORMAL.toString());
        } else {
            types.add(RoleType.NORMAL.toString());
        }
        return types;
    }

}
