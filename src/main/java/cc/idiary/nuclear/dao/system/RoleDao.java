package cc.idiary.nuclear.dao.system;

import cc.idiary.nuclear.dao.BaseDao;
import cc.idiary.nuclear.config.RoleType;
import cc.idiary.nuclear.entity.system.RoleEntity;
import cc.idiary.nuclear.query.system.RoleQuery;

import java.util.List;
import java.util.Set;

/**
 * 
 * @author Dzb
 *
 */
public interface RoleDao extends BaseDao<RoleEntity> {
	/**
	 * 根据角色名称判断是否存在
	 * 
	 * @param name
	 * @return
	 */
	boolean existByName(String name);

	/**
	 * 获取角色列表
	 * 
	 * @param query
	 * @return
	 */
	List<RoleEntity> getPaging(RoleQuery query);

	/**
	 * 根据类型获取系统中默认的组 <br/>
	 * <b>依赖系统中该类型唯一</b>
	 * 
	 * @param type
	 * @return
	 */
	RoleEntity getUniqueRole(RoleType type);

	/**
	 * 根据角色code集合获取角色集合
	 * 
	 * @param roleCodes
	 * @return
	 */
	Set<RoleEntity> getCodes(Set<String> roleCodes);

	/**
	 * 根据用户Id查找对应的用户类型角色
	 * @param userId
	 * @return
     */
	RoleEntity getUserRole(String userId);
}
