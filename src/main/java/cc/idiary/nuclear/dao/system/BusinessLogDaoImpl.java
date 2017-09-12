package cc.idiary.nuclear.dao.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.idiary.nuclear.entity.system.BusinessLogEntity;
import cc.idiary.nuclear.query.system.BusinessLogQuery;
import cc.idiary.nuclear.dao.BaseDaoImpl;
import cc.idiary.nuclear.query.BaseQuery;
import cc.idiary.utils.common.StringTools;
import org.springframework.stereotype.Repository;

import cc.idiary.nuclear.logger.LogOperationType;

@Repository("bussinessLogDao")
public class BusinessLogDaoImpl extends BaseDaoImpl<BusinessLogEntity> implements BusinessLogDao{

	public BusinessLogDaoImpl() {
		super(BusinessLogEntity.class);
	}

	@Override
	protected StringBuilder getExtensionSql(BaseQuery query,
			Map<String, Object> params) {
		BusinessLogQuery busLogQuery = (BusinessLogQuery) query;
		StringBuilder sbhql = new StringBuilder(
				" from BusinessLogEntity {0} where 1=1");
		if (!StringTools.isEmpty(busLogQuery.getUsername())) {
			sbhql.append(" and {0}.username like :username");
			params.put("username", "%" + busLogQuery.getUsername() + "%");
		}
		if (!StringTools.isEmpty(busLogQuery.getIpAddr())) {
			sbhql.append(" and {0}.ipAddr like :ipAddr");
			params.put("ipAddr", "%" + busLogQuery.getIpAddr() + "%");
		}
		if (!StringTools.isEmpty(busLogQuery.getUserAgent())) {
			sbhql.append(" and {0}.userAgent like :userAgent");
			params.put("userAgent", "%" + busLogQuery.getUserAgent() + "%");
		}
		if (!StringTools.isEmpty(busLogQuery.getContent())) {
			sbhql.append(" and {0}.content like :content");
			params.put("content", "%" + busLogQuery.getContent() + "%");
		}
		if (!StringTools.isEmpty(busLogQuery.getSignature())) {
			sbhql.append(" and {0}.signature like :signature");
			params.put("signature", "%" + busLogQuery.getSignature() + "%");
		}
		if (!StringTools.isEmpty(busLogQuery.getArgs())) {
			sbhql.append(" and {0}.args like :args");
			params.put("args", "%" + busLogQuery.getArgs() + "%");
		}
		if (!StringTools.isEmpty(busLogQuery.getException())) {
			sbhql.append(" and {0}.exception like :exception");
			params.put("args", "%" + busLogQuery.getException() + "%");
		}
		if (!StringTools.isEmpty(busLogQuery.getOperation())) {
			sbhql.append(" and {0}.operation = :operation");
			params.put("operation", busLogQuery.getOperation());
		}
		if (!StringTools.isEmpty(busLogQuery.getOperation())) {
			sbhql.append(" and {0}.operation = :operation");
			params.put("operation", LogOperationType.valueOf(busLogQuery.getOperation()));
		}
		if (busLogQuery.getIsException() != null) {
			sbhql.append(" and {0}.isException = :isException");
			params.put("isException", busLogQuery.getIsException());
		}
		if (busLogQuery.getStartTime() != null) {
			sbhql.append(" and {0}.createTime >= :createTimeStart");
			params.put("createTimeStart", busLogQuery.getStartTime());
		}
		if (busLogQuery.getEndTime() != null) {
			sbhql.append(" and {0}.createTime <= :createTimeEnd");
			params.put("createTimeEnd", busLogQuery.getEndTime());
		}
		//添加必要的逻辑删除过滤
		sbhql.append(" and {0}.delFlag <> :delFlag");
		params.put("delFlag", true);
		
		return sbhql;
	}

	@Override
	public List<BusinessLogEntity> getPaging(BusinessLogQuery query) {
		Map<String, Object> params = new HashMap<>();
		String hql = getHql(query, "buslog", params);
		hql += " order by buslog.createTime desc";
		return getByHqlPaging(hql, params, query.getPage(), query.getSize());
	}

	@Override
	public int logicalDelete(String[] param) {
		String hql = "update BusinessLogEntity bl set "
				+ "bl.delFlag = true where bl.id in (:ids)";
		Map<String, Object> params = new HashMap<>();
		params.put("ids", param);
		return executeHql(hql, params);
	}

}
