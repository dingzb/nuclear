package cc.idiary.nuclear.dao.system;

import java.util.List;

import cc.idiary.nuclear.dao.BaseDao;
import cc.idiary.nuclear.entity.system.BusinessLogEntity;
import cc.idiary.nuclear.query.system.BusinessLogQuery;

public interface BusinessLogDao extends BaseDao<BusinessLogEntity> {
	/**
	 * 返回分页查询列表
	 * 
	 * @param query
	 * @return
	 */
	List<BusinessLogEntity> getPaging(BusinessLogQuery query);

	/**逻辑删除多个日志
	 * @param param
	 */
	int logicalDelete(String[] param);
}
