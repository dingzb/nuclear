package cc.idiary.nuclear.dao.system;

import java.io.Serializable;
import java.util.List;

import cc.idiary.nuclear.entity.system.UserEntity;
import cc.idiary.nuclear.dao.BaseDao;
import cc.idiary.nuclear.query.system.UserQuery;
import cc.idiary.nuclear.service.ServiceException;

/**
 * 
 * @author Dzb
 *
 */
public interface UserDao extends BaseDao<UserEntity> {

	/**
	 * 根据用户名判断用户是否存在
	 * 
	 * @param username
	 * @return
	 */
	Boolean existByUsername(String username);

	/**
	 * 根据用户名获取用户实体<br/>
	 * 若用户存在返回用户实体对象，否则返回null
	 * 
	 * @param username
	 * @return
	 */
	UserEntity getByUsername(String username);

	/**
	 * 用户登录<br/>
	 * 登录成功后返回用户实体对象否则返回null
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	UserEntity login(String username, String password);

	/**
	 * 获取用户的分页列表
	 * 
	 * @param userQuery
	 * @return
	 */
	List<UserEntity> getPaging(UserQuery userQuery);

	/**
	 * 根据id更新用户登录名
	 * 
	 * @param id
	 * @param name
	 * @return
	 */
	boolean updateUserName(String id, String name);

	/**
	 * <h3>功能描述</h3>
	 * <p>
	 * 编辑用户
	 * </p>
	 * 
	 * @param userEntity
	 * @return
	 */
	Serializable updateUser(UserEntity userEntity);

	/**
	 * 判断用户是否为管理员分组
	 * 
	 * @param userid
	 * @return
	 */
	boolean IsAdminGroup(String userid);

}
