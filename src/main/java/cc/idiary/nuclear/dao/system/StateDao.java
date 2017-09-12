package cc.idiary.nuclear.dao.system;

import java.util.List;
import java.util.Set;

import cc.idiary.nuclear.query.system.StateQuery;
import cc.idiary.nuclear.dao.BaseDao;
import cc.idiary.nuclear.entity.system.StateEntity;

/**
 * 
 * @author Dzb
 *
 */
public interface StateDao extends BaseDao<StateEntity> {

	/**
	 * 根据Code获取Sate
	 * 
	 * @param code
	 * @return
	 */
	StateEntity getByCode(String code);

	/**
	 * 根据state.code判断状态是否存在
	 * 
	 * @param code
	 * @return
	 */
	Boolean existByCode(String code);

	/**
	 * 根据角色Ids获取状态
	 * 
	 * @param roleIds
	 * @return
	 */
	List<StateEntity> getByRoleIds(Set<String> roleIds);

	/**
	 * 获取子系统列表
	 * 
	 * @param query
	 * @return
	 */
	List<StateEntity> getPaging(StateQuery query);

	List<StateEntity> getByRoleId(String roleId);
}
