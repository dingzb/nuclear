package cc.idiary.nuclear.service.system;

import cc.idiary.nuclear.dao.system.BusinessLogDao;
import cc.idiary.nuclear.logger.BusinessLog;
import cc.idiary.nuclear.model.system.BusinessLogModel;
import cc.idiary.nuclear.utils.ConfigTools;
import cc.idiary.nuclear.utils.SoftKey;
import cc.idiary.nuclear.entity.system.BusinessLogEntity;
import cc.idiary.nuclear.logger.LogOperationType;
import cc.idiary.nuclear.model.PagingModel;
import cc.idiary.nuclear.query.system.BusinessLogQuery;
import cc.idiary.nuclear.service.BaseServiceImpl;
import cc.idiary.nuclear.service.ServiceException;
import cc.idiary.utils.common.StringTools;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service("businessLogService")
public class BusinessLogServiceImpl extends BaseServiceImpl<BusinessLogEntity> implements BusinessLogService {

	private static final Logger logger = LogManager.getLogger(BusinessLogServiceImpl.class);
	
	@Autowired
	private BusinessLogDao businessLogDao;

	@Override
	@Transactional
	public Serializable add(BusinessLogEntity businessLogEntity)
			throws ServiceException {
		businessLogEntity.setId(StringTools.randomUUID());
		try {
			return businessLogDao.save(businessLogEntity);
		} catch (Exception e) {
			logger.catching(e);
			throw new ServiceException();
		}
	}

	@Override
	@Transactional
	@BusinessLog(content = "查看业务日志", operation = LogOperationType.QUERY)
	public PagingModel getPaging(BusinessLogQuery query)
			throws ServiceException {
		if (query == null) {
			throw new ServiceException("查询对象不能为空");
		}
		queryTimeAssert(query.getStartTime(), query.getEndTime());

		SoftKey softKey=new SoftKey();
		String key="66F63D5A57058CD8D11F8B5EF5AC524E";
		PagingModel paging = new PagingModel();
		List<BusinessLogEntity> businessLogEntities = null;
		Long total = null;
		try {
			if(!StringTools.isEmpty(query.getUsername())){
				query.setUsername(softKey.StrEnc(query.getUsername(), key));
			}
			businessLogEntities = businessLogDao.getPaging(query);
			total = businessLogDao.getCount(query);
		} catch (Exception e) {
			logger.catching(e);
			throw new ServiceException();
		}

		List<BusinessLogModel> businessLogModels = new ArrayList<BusinessLogModel>();

		for (BusinessLogEntity businessLogEntity : businessLogEntities) {
			BusinessLogModel businessLogModel = new BusinessLogModel();
			BeanUtils.copyProperties(businessLogEntity, businessLogModel);
			businessLogModel.setArgs(softKey.StrDec(businessLogEntity.getArgs(), key));
			businessLogModel.setContent(softKey.StrDec(businessLogEntity.getContent(), key));
			businessLogModel.setUserAgent(softKey.StrDec(businessLogEntity.getUserAgent(), key));
			businessLogModel.setSignature(softKey.StrDec(businessLogEntity.getSignature(), key));
			businessLogModel.setIpAddr(softKey.StrDec(businessLogEntity.getIpAddr(), key));
			businessLogModel.setUsername(softKey.StrDec(businessLogEntity.getUsername(), key));
			if(businessLogEntity.getException() !=null){
				businessLogModel.setException(softKey.StrDec(businessLogEntity.getException(), key));
			}
			businessLogModel.setCreateTime(businessLogEntity.getCreateTime());
			businessLogModel.setOperation(businessLogEntity.getOperation()
					.toString());
			businessLogModels.add(businessLogModel);
		}

		paging.setRows(businessLogModels);
		paging.setTotal(total);
		return paging;
	}

	@Override
	@Transactional
	@BusinessLog(content = "删除业务日志", operation = LogOperationType.DELETE)
	public int logicalDelete(String[] param) throws ServiceException {
		if(param==null||param.length<=0){
			throw new ServiceException("删除失败");
		}
		return businessLogDao.logicalDelete(param);
	}

	@Override
	public void setTime(String time) throws ServiceException {

		try {
			Properties properties = ConfigTools.getProperties("deleteLog.properties");
			properties.setProperty("delete.time",time);
			ConfigTools.sortProperties("deleteLog.properties",properties);
		} catch (IOException e) {
			logger.catching(e);
			throw new ServiceException("系统执行错误");
		}

	}

	@Override
	public String getTime() throws ServiceException {
		Properties properties = new Properties();
		try {
			return ConfigTools.getProperty("deleteLog.properties","delete.time");
		} catch (IOException e) {
			logger.catching(e);
			return "";
		}
	}
}
