package cc.idiary.nuclear.service.system;

import cc.idiary.nuclear.config.GroupType;
import cc.idiary.nuclear.config.UserType;
import cc.idiary.nuclear.dao.system.GroupDao;
import cc.idiary.nuclear.dao.system.UserDao;
import cc.idiary.nuclear.entity.system.GroupEntity;
import cc.idiary.nuclear.entity.system.UserEntity;
import cc.idiary.nuclear.logger.BusinessLog;
import cc.idiary.nuclear.model.system.UserModel;
import cc.idiary.nuclear.query.system.GroupQuery;
import cc.idiary.nuclear.logger.LogOperationType;
import cc.idiary.nuclear.model.PagingModel;
import cc.idiary.nuclear.model.system.GroupModel;
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

@Service("groupService")
public class GroupServiceImpl extends BaseServiceImpl<GroupEntity> implements GroupService {

    @Autowired
    private GroupDao groupDao;
    @Autowired
    private UserDao userDao;

    private static final Logger logger = LogManager.getLogger(GroupServiceImpl.class);

    @Override
    @Transactional
    @BusinessLog(content = "查询组", operation = LogOperationType.QUERY)
    public PagingModel getPaging(GroupQuery query) throws ServiceException {

        PagingModel paging = new PagingModel();
        Long total = null;
        List<GroupEntity> groupEntitys = new ArrayList<GroupEntity>();
        List<GroupModel> groupModels = new ArrayList<GroupModel>();
        UserEntity userEntity = null;
        Set<GroupType> typeList = new HashSet<GroupType>();

        typeList.add(GroupType.NORMAL);
        if (!StringTools.isEmpty(query.getUserId())) {
            try {
                userEntity = userDao.getById(query.getUserId());
            } catch (Exception e) {
                logger.catching(e);
                throw new ServiceException("获取组列表失败");
            }
            if (userEntity == null) {
                throw  new ServiceException("当前用户已经不存在");
            }
        } else {
            // 超级管理员supreme能够显示[管理员、普通]
            typeList.add(GroupType.ADMIN);
        }

        if (query.getTypes() == null) {
            query.setTypes(typeList);
        } else {
            query.getTypes().addAll(typeList);
        }

        try {
            groupEntitys = groupDao.getPaging(query);
            total = groupDao.getCount(query);
        } catch (Exception e) {
            logger.catching(e);
            throw new ServiceException("获取组列表失败");
        }
        for (GroupEntity groupEntity : groupEntitys) {
            GroupModel groupModel = new GroupModel();
            BeanUtils.copyProperties(groupEntity, groupModel);
            groupModel.setType(groupEntity.getType().toString());
            groupModels.add(groupModel);
        }
        paging.setRows(groupModels);
        paging.setTotal(total);

        return paging;
    }

    @Override
    @Transactional
    @BusinessLog(content = "添加组", operation = LogOperationType.ADD)
    public Serializable addGroup(GroupModel groupModel) throws ServiceException {
        if (StringTools.isEmpty(groupModel.getName())) {
            throw new ServiceException("名称不能为空");
        }
        if (StringTools.isEmpty(groupModel.getType())) {
            throw new ServiceException("类型不能为空");
        }
        GroupType type = null;
        try {
            type = GroupType.valueOf(groupModel.getType());
        } catch (Exception e) {
            throw new ServiceException("类型错误");
        }

        GroupEntity parent = null;
        try {
            parent = groupDao.getUserGroup(groupModel.getUserId());
        } catch (Exception e) {
            logger.catching(e);
            throw new ServiceException();
        }
        GroupEntity groupEntity = new GroupEntity();
        BeanUtils.copyProperties(groupModel, groupEntity);
        groupEntity.setCreateTime(new Date());
        groupEntity.setModifyTime(new Date());
        groupEntity.setId(StringTools.randomUUID());
        groupEntity.setType(type);
        groupEntity.setParent(parent);

        try {
            return groupDao.save(groupEntity);
        } catch (Exception e) {
            logger.catching(e);
            throw new ServiceException();
        }

    }

    @Override
    @Transactional
    @BusinessLog(content = "修改组", operation = LogOperationType.MODIFY)
    public void editGroup(GroupModel groupModel) throws ServiceException {
        try {
            GroupEntity groupEntity = groupDao.getById(groupModel.getId());
            if (groupEntity == null) {
                throw new ServiceException("组不存在");
            }
            groupEntity.setName(groupModel.getName());
            groupEntity.setType(GroupType.valueOf(groupModel.getType()));
            groupEntity.setDescription(groupModel.getDescription());
            groupEntity.setModifyTime(new Date());
            groupDao.update(groupEntity);
        } catch (Exception e) {
            logger.catching(e);
            throw new ServiceException(e.getMessage());
        }

    }

    @Override
    @Transactional
    @BusinessLog(content = "删除组", operation = LogOperationType.DELETE)
    public void deleteGroups(String[] groupIds) throws ServiceException {
        for (String id : groupIds) {
            GroupEntity groupEntity = groupDao.getById(id);
            if (groupEntity == null) {
                throw new ServiceException("组不存在");
            }
            Set<UserEntity> users = groupEntity.getUsers();
            if (users != null && !users.isEmpty()) {
                throw new ServiceException("组正在使用中，无法删除");
            }
            //TODO 其他涉及到组配置的信息不为空判断
            try {
                groupDao.delete(groupEntity);
            } catch (Exception e) {
                e.printStackTrace();
                logger.catching(e);
                throw new ServiceException("删除失败");
            }
        }
    }

    @Override
    @Transactional
    public boolean existByName(String name) throws ServiceException {
        try {
            return groupDao.existByName(name);
        } catch (Exception e) {
            logger.catching(e);
            throw new ServiceException();
        }
    }

    @Override
    public List<String> getTypes(UserModel user) throws ServiceException {
        List<String> types = new ArrayList<String>();
        if (user == null || StringTools.isEmpty(user.getType())) {
            types.add(GroupType.ADMIN.toString());
            types.add(GroupType.NORMAL.toString());
        } else {
            UserType type = UserType.valueOf(user.getType());
            if (type.equals(UserType.ADMIN)) {
                types.add(GroupType.NORMAL.toString());
            } else if (type.equals(UserType.NORMAL)) {
                types.add(GroupType.NORMAL.toString());
            }
        }
        return types;
    }
}
