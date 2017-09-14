package cc.idiary.nuclear.service.system;

import cc.idiary.nuclear.config.BaseInfo;
import cc.idiary.nuclear.config.GroupType;
import cc.idiary.nuclear.config.RoleType;
import cc.idiary.nuclear.config.UserType;
import cc.idiary.nuclear.dao.system.GroupDao;
import cc.idiary.nuclear.dao.system.RoleDao;
import cc.idiary.nuclear.dao.system.UserDao;
import cc.idiary.nuclear.entity.system.GroupEntity;
import cc.idiary.nuclear.entity.system.RoleEntity;
import cc.idiary.nuclear.entity.system.UserEntity;
import cc.idiary.nuclear.logger.BusinessLog;
import cc.idiary.nuclear.logger.LogOperationType;
import cc.idiary.nuclear.model.PagingModel;
import cc.idiary.nuclear.model.system.UserModel;
import cc.idiary.nuclear.query.system.UserQuery;
import cc.idiary.nuclear.service.BaseServiceImpl;
import cc.idiary.nuclear.service.ServiceException;
import cc.idiary.nuclear.utils.LocaleUtil;
import cc.idiary.utils.common.StringTools;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.*;

@Service("userService")
public class UserServiceImpl extends BaseServiceImpl<UserEntity> implements UserService {

    private static final Logger logger = LogManager
            .getLogger(UserServiceImpl.class);

    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private GroupDao groupDao;

    @BusinessLog(content = "用户登录", operation = LogOperationType.LOGIN)
    @Override
    public UserModel login(String username, String password) throws ServiceException {

        if (StringTools.isEmpty(username))
            throw new ServiceException(LocaleUtil.getMessage("exception.form.empty", LocaleUtil.getMessage("system.user.form.username")));

        if (StringTools.isEmpty(password))
            throw new ServiceException(LocaleUtil.getMessage("exception.form.empty", LocaleUtil.getMessage("system.user.form.password")));

        UserEntity userEntity = null;
        boolean exist = false;
        try {
            exist = userDao.existByUsername(username);
        } catch (Exception e) {
            logger.catching(e);
            throw new ServiceException();
        }
        if (!exist) {
            throw new ServiceException(LocaleUtil.getMessage("exception.validate.exist", LocaleUtil.getMessage("system.user.form.username")));
        }
        try {

            userEntity = userDao.login(username, StringTools.Md5(password));
        } catch (Exception e) {
            logger.catching(e);
            throw new ServiceException();
        }
        if (userEntity == null) {
            throw new ServiceException(LocaleUtil.getMessage("exception.system.error", LocaleUtil.getMessage("system.user.form.password")));
        }

        UserModel userModel = new UserModel();

        BeanUtils.copyProperties(userEntity, userModel);
        userModel.setGroupIds(getIds(userEntity.getGroups()));
        userModel.setRoleIds(getIds(userEntity.getRoles()));
        userModel.setType(userEntity.getType().toString());
        return userModel;
    }

    @Override
    @BusinessLog(content = "修改密码", operation = LogOperationType.MODIFY)
    public UserModel modPassWord(String userId, String password, String newPassword,
                                 String rePassword) throws ServiceException {
        if (StringTools.isEmpty(userId)) {
            throw new ServiceException("用户ID为空！");
        }
        UserEntity user = userDao.getById(userId);
        if (user == null)
            throw new ServiceException("当前用户不存在");
        if (!user.getPassword().equals(StringTools.Md5(password))) {
            throw new ServiceException("PASSWORD_ERROR");
        }
        if (!newPassword.equals(rePassword)) {
            throw new ServiceException("PASSWORD_NOT_EQUAL");
        }

        user.setPassword(StringTools.Md5(newPassword));
        user.setModifyTime(new Date());
        userDao.update(user);

        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(user, userModel);

        return userModel;
    }

    @Override
    @Transactional
    @BusinessLog(content = "用户管理查询", operation = LogOperationType.QUERY)
    public PagingModel getPaging(UserQuery query, String[] merchantIds) throws ServiceException {

        queryBasicAssert(query);
        queryTimeAssert(query.getCreateTimeStart(), query.getCreateTimeEnd());

        PagingModel paging = new PagingModel();
        List<UserEntity> tmpList = null;
        List<UserModel> rows = new ArrayList<UserModel>();
        Long total = null;

        if (!StringTools.isEmpty(query.getUserId())) {
            Set<UserType> types = new HashSet<UserType>();
            try {
                UserEntity user = userDao.getById(query.getUserId());
                if (user.getType().equals(UserType.ADMIN)) {            // 管理员显示 type!=ADMIN,user
                    types.add(UserType.NORMAL);
                } else if (user.getType().equals(UserType.NORMAL)) {
                    types.add(UserType.NORMAL);
                } else if (user.getType().equals(UserType.BUILD_IN)) {  // 超级管理员，没有限制
                    types.add(UserType.ADMIN);
                    types.add(UserType.NORMAL);
                }
                query.setTypes(types);
            } catch (Exception e) {
                logger.catching(e);
                throw new ServiceException();
            }
        }
        try {
            tmpList = userDao.getPaging(query);
            total = userDao.getCount(query);
            for (UserEntity tmp : tmpList) {
                UserModel row = new UserModel();
                BeanUtils.copyProperties(tmp, row);
                row.setType(tmp.getType().toString());
                rows.add(row);
            }
        } catch (Exception e) {
            logger.catching(e);
            throw new ServiceException();
        }
        paging.setRows(rows);
        paging.setTotal(total);

        return paging;
    }

    @BusinessLog(content = "添加用户", operation = LogOperationType.ADD)
    @Override
    @Transactional
    public Serializable add(UserModel userModel) throws ServiceException {

        if (StringTools.isEmpty(userModel.getUsername())) {
            throw new ServiceException("用户名不能为空");
        }
        if (StringTools.isEmpty(userModel.getName())) {
            throw new ServiceException("名称不能为空");
        }
        if (StringTools.isEmpty(userModel.getType())) {
//            throw new ServiceException("用户类型不能为空");
            userModel.setType(UserType.NORMAL.toString());
        }

        // 判断用户名是否存在
        boolean exist = false;
        try {
            exist = userDao.existByUsername(userModel.getUsername());
        } catch (Exception e) {
            logger.catching(e);
            throw new ServiceException();
        }
        if (exist) {
            throw new ServiceException("用户名已经存在！");
        }

        UserType type = null;
        try {
            type = UserType.valueOf(userModel.getType());
        } catch (Exception e) {
            logger.catching(e);
            throw new ServiceException("用户类型参数错误");
        }

        // 用户信息设置
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userModel, userEntity);
        userEntity.setPassword(StringTools.Md5(BaseInfo.PASSWORD_DEF));
        userEntity.setId(StringTools.randomUUID());
        userEntity.setCreateTime(new Date());
        userEntity.setModifyTime(new Date());
        userEntity.setType(type);

        // 创建用户组
        GroupEntity group = new GroupEntity();
        group.setType(GroupType.USER);
        group.setId(StringTools.randomUUID());
        group.setCreateTime(new Date());
        group.setModifyTime(new Date());
        group.setCode(userModel.getUsername() + "_" + StringTools.randomUUID());
        group.setName(userModel.getName() + "_" + StringTools.randomUUID());
        // 配置组的父组（配置为当前用户的用户组）
        try {
            group.setParent(groupDao.getUserGroup(userModel.getUserId()));
        } catch (Exception e) {
            logger.catching(e);
            throw new ServiceException();
        }

        // 创建用户角色
        RoleEntity role = new RoleEntity();
        role.setType(RoleType.USER);
        role.setId(StringTools.randomUUID());
        role.setCreateTime(new Date());
        role.setModifyTime(new Date());
        role.setCode(userModel.getUsername() + "_" + StringTools.randomUUID());
        role.setName(userModel.getName() + "_" + StringTools.randomUUID());

        Set<GroupEntity> groups = new HashSet<GroupEntity>();
        groups.add(group);
        Set<RoleEntity> roles = new HashSet<RoleEntity>();
        roles.add(role);
        // 根据用户类型添加不同的默认角色[系统中默认存在代理商角色、商户角色]
        RoleEntity specifyRole = null;
        RoleType specifyRoleType = null;
        if (specifyRoleType != null) {
            try {
                specifyRole = roleDao.getUniqueRole(specifyRoleType);
            } catch (Exception e) {
                logger.catching(e);
                throw new ServiceException();
            }
            if (specifyRole != null) {
                roles.add(specifyRole);
            }
        }

        Serializable result = null;
        try {
            groupDao.save(group);
            roleDao.save(role);
            userEntity.setGroups(groups);
            userEntity.setRoles(roles);
            result = userDao.save(userEntity);
        } catch (Exception e) {
            logger.catching(e);
            throw new ServiceException();
        }

        return result;
    }

    @BusinessLog(content = "编辑用户", operation = LogOperationType.MODIFY)
    @Override
    @Transactional
    public void editUserModel(UserModel userModel) throws ServiceException {
        if (StringTools.isEmpty(userModel.getUsername())) {
            throw new ServiceException("用户名不能为空");
        }
        if (StringTools.isEmpty(userModel.getName())) {
            throw new ServiceException("名称不能为空");
        }
        UserEntity userEntity = null;
        try {
            userEntity = userDao.getById(userModel.getId());
            if (userEntity == null) {
                throw new ServiceException("用户已不存在");
            }
            if (!StringTools.isEmpty(userModel.getName())) {
                userEntity.setName(userModel.getName());
            }
            if (!StringTools.isEmpty(userModel.getIdCard())) {
                userEntity.setIdCard(userModel.getIdCard());
            }
            if (!StringTools.isEmpty(userModel.getEmail())) {
                userEntity.setEmail(userModel.getEmail());
            }
            if (!StringTools.isEmpty(userModel.getPhone())) {
                userEntity.setPhone(userModel.getPhone());
            }
            userEntity.setModifyTime(new Date());
            userDao.update(userEntity);
        } catch (Exception e) {
            logger.catching(e);
            throw new ServiceException(e.getMessage());
        }
    }

    @BusinessLog(content = "删除用户", operation = LogOperationType.DELETE)
    @Override
    @Transactional
    public void removeUser(String userId) throws ServiceException {
        if (userId == null) {
            throw new ServiceException("删除失败");
        }
        UserEntity user = null;

        try {
            user = userDao.getById(userId);
        } catch (Exception e) {
            logger.catching(e);
            throw new ServiceException();
        }
        try {
            // TODO
            //删除用户时删除 用户组 用户角色
            GroupEntity groupEntity = groupDao.getUserGroup(userId);
            RoleEntity roleEntity = roleDao.getUserRole(userId);
            groupDao.delete(groupEntity);
            roleDao.delete(roleEntity);

            userDao.delete(user);
        } catch (Exception e) {
            logger.catching(e);
            throw new ServiceException();
        }
    }

    @BusinessLog(content = "给用户设置角色", operation = LogOperationType.MODIFY)
    @Override
    @Transactional
    public Serializable addRolesTouser(String[] userParam, String[] roleParam) throws ServiceException {
        String userId = null;
        String roleId = null;
        Set<RoleEntity> roleEntity = new HashSet<RoleEntity>();
        try {
            for (int i = 0; i < userParam.length; i++) {
                userId = userParam[i];
                UserEntity userEntity = userDao.getById(userId);
                if (userEntity == null) {
                    throw new ServiceException("用户不存在");
                }
                for (int j = 0; j < roleParam.length; j++) {
                    roleId = roleParam[j];
                    roleEntity.add(roleDao.getById(roleId));
                    userEntity.setRoles(roleEntity);
                    userDao.update(userEntity);
                }
            }
        } catch (Exception e) {
            logger.catching(e);
            throw new ServiceException(e.getMessage());
        }
        return "999";
    }

    @Override
    @Transactional
    @BusinessLog(content = "添加组", operation = LogOperationType.ADD)
    public void addGroups(String id, String[] ids) throws ServiceException {
        authGroups(id, ids, true);
    }

    @Override
    @Transactional
    @BusinessLog(content = "移除组", operation = LogOperationType.MODIFY)
    public void removeGroups(String id, String[] ids) throws ServiceException {
        authGroups(id, ids, false);
    }

    /**
     * @param id
     * @param ids
     * @param operation true: add, false: remove
     * @throws ServiceException
     */
    private void authGroups(String id, String[] ids, boolean operation)
            throws ServiceException {
        if (StringTools.isEmpty(ids) || ids == null || ids.length == 0) {
            throw new ServiceException("参数错误");
        }
        Set<String> addIds = new HashSet<String>();
        addIds.addAll(Arrays.asList(ids));
        try {
            UserEntity users = userDao.getById(id);
            if (users == null) {
                throw new ServiceException("用户不存在");
            }
            Set<GroupEntity> groups = groupDao.getByIds(addIds);
            if (operation) {
                users.getGroups().addAll(groups);
            } else {
                users.getGroups().removeAll(groups);
            }
            userDao.update(users);
        } catch (Exception e) {
            logger.catching(e);
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<String> getTypes(UserModel user) throws ServiceException {
        List<String> types = new ArrayList<String>();
        if (user == null || StringTools.isEmpty(user.getType())) {
            types.add(UserType.ADMIN.toString());
            types.add(UserType.NORMAL.toString());
        } else {
            UserType type = UserType.valueOf(user.getType());
            if (type.equals(UserType.ADMIN)) {
                types.add(UserType.NORMAL.toString());
            } else if (UserType.NORMAL.equals(type)) {
                types.add(UserType.NORMAL.toString());
            }
        }
        return types;
    }

    @Override
    @Transactional
    @BusinessLog(content = "添加角色", operation = LogOperationType.ADD)
    public void addRoles(String id, String[] ids) throws ServiceException {
        authRoles(id, ids, true);
    }

    @Override
    @Transactional
    @BusinessLog(content = "移除角色", operation = LogOperationType.MODIFY)
    public void removeRoles(String id, String[] ids) throws ServiceException {
        authRoles(id, ids, false);
    }

    @Override
    @Transactional
    public UserModel getById(String id) throws ServiceException {
        if (StringTools.isEmpty(id)) {
            throw new ServiceException("id 不能为空！");
        }
        UserModel userModel = new UserModel();
        try {
            UserEntity userEntity = userDao.getById(id);
            BeanUtils.copyProperties(userEntity, userModel);
        } catch (Exception e) {
            throw new ServiceException();
        }
        return userModel;
    }

    /**
     * @param id
     * @param ids
     * @param operation true: add, false: remove
     * @throws ServiceException
     */
    private void authRoles(String id, String[] ids, boolean operation)
            throws ServiceException {
        if (StringTools.isEmpty(ids) || ids == null || ids.length == 0) {
            throw new ServiceException("参数错误");
        }
        Set<String> addIds = new HashSet<String>();
        addIds.addAll(Arrays.asList(ids));
        try {
            UserEntity users = userDao.getById(id);
            if (users == null) {
                throw new ServiceException("用户不存在");
            }
            Set<RoleEntity> roles = roleDao.getByIds(addIds);
            if (operation) {
                users.getRoles().addAll(roles);
            } else {
                users.getRoles().removeAll(roles);
            }
            userDao.update(users);
        } catch (Exception e) {
            logger.catching(e);
            throw new ServiceException(e.getMessage());
        }
    }
}
