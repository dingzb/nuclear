package cc.idiary.nuclear.service.system;

import java.io.Serializable;

import cc.idiary.nuclear.model.PagingModel;
import cc.idiary.nuclear.service.BaseService;
import cc.idiary.nuclear.service.ServiceException;
import cc.idiary.nuclear.entity.system.BusinessLogEntity;
import cc.idiary.nuclear.query.system.BusinessLogQuery;

public interface BusinessLogService extends BaseService<BusinessLogEntity> {
	/**
	 * 只用于日志切面用于创建日志记录
	 * 
	 * @param businessLogEntity
	 * @return
	 * @throws ServiceException
	 */
	Serializable add(BusinessLogEntity businessLogEntity)
			throws ServiceException;

	/**
	 * 返回分页查询列列表
	 * 
	 * @param query
	 * @return
	 * @throws ServiceException
	 */
	PagingModel getPaging(BusinessLogQuery query) throws ServiceException;

	/**
	 * 逻辑删除
	 * @param param
	 * @throws ServiceException
	 */
	int logicalDelete(String[] param)throws ServiceException;

	/**
	 * 设置日志删除时间
	 *
	 * @param time
	 * @throws ServiceException
     */
	void setTime(String time) throws ServiceException;

	/**
	 * 获取日志删除时间
	 * @return
	 * @throws ServiceException
     */
	String getTime() throws ServiceException;
}
