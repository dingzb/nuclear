package cc.idiary.nuclear.dao.system;

import java.util.List;
import java.util.Set;

import cc.idiary.nuclear.dao.BaseDao;
import cc.idiary.nuclear.config.UserType;
import cc.idiary.nuclear.entity.system.GroupEntity;
import cc.idiary.nuclear.query.system.GroupQuery;

public interface GroupDao extends BaseDao<GroupEntity> {

	/**
	 * 获取组列表
	 * 
	 * @param query
	 * @return
	 */
	List<GroupEntity> getPaging(GroupQuery query);

	/**
	 * 根据名称判断是否存在
	 * 
	 * @param name
	 * @return
	 */
	boolean existByName(String name);

	/**
	 * 获取用户授权下的所有组id列表
	 * 
	 * @param userId
	 * @param option
	 *            0:none 1: children 2: monitors 3:all
	 * @return Empty set: 任何组权限,Null:全部权限
	 */
	Set<String> getAuthGroupIdsByUserId(String userId, int option);

	/**
	 * 
	 * 通过userid获取用户组
	 * 
	 * @param userId
	 * @param userType
	 * @return
	 */
	GroupEntity getUserGroup(String userId, UserType userType);

	/**
	 * 通过userId 获取用户组 前提条件： 每个用户必须有且只有一个用户组，admin 用户组为admin组
	 * 
	 * @param uerId
	 * @return
	 */
	GroupEntity getUserGroup(String uerId);

	/**
	 * 根据用户组code集合获取组集合
	 * 
	 * @param groupCodes
	 * @return
	 */
	Set<GroupEntity> getCodes(Set<String> groupCodes);
}
