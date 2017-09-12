package cc.idiary.nuclear.service.system;

import java.io.Serializable;
import java.util.List;

import cc.idiary.nuclear.entity.system.UserEntity;
import cc.idiary.nuclear.model.system.UserModel;
import cc.idiary.nuclear.query.system.UserQuery;
import cc.idiary.nuclear.model.PagingModel;
import cc.idiary.nuclear.service.BaseService;
import cc.idiary.nuclear.service.ServiceException;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户服务
 * 
 * @author Dzb
 *
 */
public interface UserService extends BaseService<UserEntity> {

	/**
	 * 用户登录
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	@Transactional
    UserModel login(String username, String password) throws ServiceException;

	/**
	 * 修改密码
	 * 
	 * @param userId
	 * @param password
     * @param newPassword
	 * @param rePassword
	 * @return
	 * @throws ServiceException
	 */
	@Transactional
	UserModel modPassWord(String  userId, String password, String newPassword, String rePassword) throws ServiceException;

	/**
	 * <h2>功能描述</h2>
	 * <p>
	 * 获取用户列表
	 * </p>
	 * 
	 * @param query
	 * @return
	 * @throws ServiceException
	 */
	PagingModel getPaging(UserQuery query, String[] merchantIds) throws ServiceException;

	/**
	 * <h2>功能描述</h2>
	 * <p>
	 * 删除用户
	 * </p>
	 * 
	 * @since 2015-07-25
	 * @param userId
	 * @return
	 * @throws ServiceException
	 */
	void removeUser(String userId) throws ServiceException;

	/**
	 * <h2>功能描述</h2>
	 * <p>
	 * 给用户设置角色
	 * </p>
	 * 
	 * @since 2015-08-04
	 * @throws ServiceException
	 * @param userParam
	 * @param roleParam
	 * @return
	 * @throws ServiceException
	 */
	Serializable addRolesTouser(String[] userParam, String[] roleParam)
			throws ServiceException;

	/**
	 * 向用户同添加组
	 * 
	 * @param id
	 *            用户id
	 * @param ids
	 *            组ids
	 * @throws ServiceException
	 */
	void addGroups(String id, String[] ids) throws ServiceException;

	/**
	 * 移除用户的组
	 * 
	 * @param id
	 *            用户id
	 * @param ids
	 *            组ids
	 * @throws ServiceException
	 */
	void removeGroups(String id, String[] ids) throws ServiceException;

	void editUserModel(UserModel userModel) throws ServiceException;

	Serializable add(UserModel userModel) throws ServiceException;

	/**
	 * 根据用户Id获取其能显示的用户类型列表
	 * 
	 * @param user
	 * @return
	 * @throws ServiceException
	 */
	List<String> getTypes(UserModel user) throws ServiceException;

	void addRoles(String id, String[] ids) throws ServiceException;

	void removeRoles(String id, String[] ids) throws ServiceException;

	UserModel getById(String id) throws ServiceException;
}
