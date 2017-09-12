package cc.idiary.nuclear.dao.system;

import cc.idiary.nuclear.dao.BaseDaoImpl;
import cc.idiary.nuclear.entity.system.UserEntity;
import cc.idiary.nuclear.query.BaseQuery;
import cc.idiary.nuclear.query.system.UserQuery;
import cc.idiary.nuclear.service.ServiceException;
import cc.idiary.utils.common.StringTools;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.*;

/**
 * @author Dzb
 */
@Repository("userDao")
public class UserDaoImpl extends BaseDaoImpl<UserEntity> implements UserDao {

    public UserDaoImpl() {
        super(UserEntity.class);
    }

    public Boolean existByUsername(String username) {
        return existBy("username", username);
    }

    @Override
    public UserEntity getByUsername(String username) {
        String hql = "from UserEntity where username=:username";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("username", username);
        List<UserEntity> result = getByHql(hql, params);
        return result.isEmpty() ? null : result.get(0);
    }

    @Override
    public Boolean existById(String id) {
        return existBy("id", id);
    }

    @Override
    public UserEntity login(String username, String password) {
        String hql = "from UserEntity as user "
                + "where username=:username and password=:password";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("username", username);
        params.put("password", password);
        List<UserEntity> result = getByHql(hql, params);
        return result.isEmpty() ? null : result.get(0);
    }

    @Override
    public List<UserEntity> getPaging(UserQuery queryModel) {
        if (queryModel == null)
            return new ArrayList<UserEntity>();
        Map<String, Object> params = new HashMap<String, Object>();
        String hql = getHql(queryModel, "u", params);
        hql += " order by u.name";
        return getByHqlPaging(hql, params, queryModel.getPage(),
                queryModel.getSize());
    }

    @Override
    protected StringBuilder getExtensionSql(BaseQuery query,
                                            Map<String, Object> params) {
        UserQuery userQuery = (UserQuery) query;
        StringBuilder hqlsb = new StringBuilder(
                " from UserEntity {0} where 1=1");

        if (!StringTools.isEmpty(userQuery.getName())) {
            hqlsb.append(" and {0}.name like :name");
            params.put("name", "%" + userQuery.getName() + "%");
        }
        if (!StringTools.isEmpty(userQuery.getUsername())) {
            hqlsb.append(" and {0}.username like :username");
            params.put("username", "%" + userQuery.getUsername() + "%");
        }
        if (userQuery.getCreateTimeStart() != null) {
            hqlsb.append(" and {0}.createTime >= :createTimeStart");
            params.put("createTimeStart", userQuery.getCreateTimeStart());
        }
        if (userQuery.getCreateTimeEnd() != null) {
            hqlsb.append(" and {0}.createTime <= :createTimeEnd");
            params.put("createTimeEnd", userQuery.getCreateTimeEnd());
        }
        if (userQuery.getTypes() != null) {
            if (userQuery.getTypes().isEmpty()) {
                hqlsb.append(" and {0}.type in ('''')");
            } else {
                hqlsb.append(" and {0}.type in (:types)");
                params.put("types", userQuery.getTypes());
            }
        }

        return hqlsb;
    }

    @Override
    public boolean updateUserName(String id, String name) {
        String hql = "update UserEntity ue set ue.name=:username where ue.id=:id";
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("username", name);
        param.put("id", id);
        int num = executeHql(hql, param);
        if (num > 0)
            return true;

        return false;
    }

    @Override
    public Serializable updateUser(UserEntity userEntity) {
        String hql = "update UserEntity user set user.name=:name,user.username=:username,"
                + "user.create_time=:createTime,user.password=:password,user.contacts_id=:contacts_id"
                + "user.modify_time=:modifyTime where user.id=:id";
        Map<String, Object> params = new HashMap<>();
        params.put("name", userEntity.getName());
        params.put("username", userEntity.getUsername());
        params.put("createTime", userEntity.getCreateTime());
        params.put("password", userEntity.getPassword());
        params.put("modifyTime", new Date());
        return executeHql(hql, params);
    }

    @Override
    public boolean IsAdminGroup(String userId) {
        String hql = "from UserEntity u left join u.groups g"
                + " where g.name = '管理员' and u.id=:userId";
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userId", userId);
        List<UserEntity> userList = getByHql(hql, param);

        return userList != null && userList.size() >= 1;
    }
}
