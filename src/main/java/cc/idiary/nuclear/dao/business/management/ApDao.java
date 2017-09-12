package cc.idiary.nuclear.dao.business.management;

import cc.idiary.nuclear.dao.BaseDao;
import cc.idiary.nuclear.entity.business.management.ApEntity;
import cc.idiary.nuclear.query.business.management.ApQuery;

import java.util.List;
import java.util.Set;

public interface ApDao extends BaseDao<ApEntity> {

	/**
	 * 分页查询
	 * 
	 * @param query
	 * @return
	 */
	List<ApEntity> getPaging(ApQuery query);

	/**
	 * 判断当前mac地址是否已经存在
	 * 
	 * @param macStr
	 * @return
	 */
	boolean exitsByMac(String macStr, String id);

	/**
	 * 重名判断
	 * @param name
	 * @param id
	 * @return
	 */
	boolean exitsByName(String name, String id, String type);
	/**
	 * 根据mac获取Ap
	 * 
	 * @param mac
	 * @return
	 */
	ApEntity getByMac(String mac);

	/**
	 * 根据mac集合获取ap实体列表
	 * 
	 * @param macs
	 * @return
	 */
	List<ApEntity> getByMacs(Set<String> macs);

	/**
	 * 根据Code获取Ap
	 * 
	 * @param code
	 * @return
	 */
	ApEntity getByCode(String code);

	/**
	 * 获取ap关联固件列表
	 * 
	 * @param query
	 * @return
	 */
	List<Object[]> getFirmPaging(ApQuery query);

	/**
	 * 获取带注册号的ap信息
	 * 
	 * @param ids
	 * @return
	 */
	List<ApEntity> getFwVersionLst(String[] ids);

	/**
	 * 根据场所ID获取AP列表
	 * 
	 * @param placeId
	 * @return
	 */
	List<ApEntity> getApListByPlaceId(String placeId);

	/**
	 * 根据userId获取其下的AP设备MAC地址列表
	 * 
	 * @param userId
	 * @return
	 */
	List<ApEntity> getByUserId(String userId);

	/**
	 * 根据升级任务id获取下面所有ap
	 * 
	 * @param id
	 * @return
	 */
	List<ApEntity> getByUpdateId(String id);

	/**
	 * 根据placeCode获取其下的AP设备MAC地址列表
	 * 
	 * @param placeCode
	 * @return
	 */
	List<ApEntity> getByPlaceCode(String placeCode);
	
	/**
	 * 判断当前所有配置任务中是否存在该认证策略
	 * @param id
	 * @return
	 */
	boolean exitByStrategyId(String id);

	/**
	 * 获取ap首页统计信息
	 * @param groupIds
	 * @param i
	 * @return
	 */
	int getApTip(Set<String> groupIds, int i, boolean isTerminal);

	/**
	 * 获取和配置任务关连的ap信息
	 * @param query
	 * @return
	 */
	List<Object[]> getByConfigSchedule(ApQuery query);
	
	
	
}
