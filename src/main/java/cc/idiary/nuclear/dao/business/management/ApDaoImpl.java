package cc.idiary.nuclear.dao.business.management;

import cc.idiary.nuclear.dao.BaseDaoImpl;
import cc.idiary.nuclear.entity.business.management.ApEntity;
import cc.idiary.utils.common.StringTools;
import cc.idiary.nuclear.query.BaseQuery;
import cc.idiary.nuclear.query.business.management.ApQuery;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository("apDao")
public class ApDaoImpl extends BaseDaoImpl<ApEntity> implements ApDao {

	public ApDaoImpl() {
		super(ApEntity.class);
	}

	@Override
	protected StringBuilder getExtensionSql(BaseQuery query,
			Map<String, Object> params) {
		ApQuery apQuery = (ApQuery) query;

		StringBuilder hqlsb = new StringBuilder("from ApEntity {0} where 1=1");
		if (!StringTools.isEmpty(apQuery.getName())) {
			hqlsb.append(" and {0}.name like :name");
			params.put("name", "%" + apQuery.getName() + "%");
//				由于前台传入 ‘%%’ 类似这样的值时decode报错，且找不到这里需要decode的理由，注释掉
//				params.put("name", "%" + URLDecoder.decode(apQuery.getName(),"UTF-8") + "%");
		}
		if (!StringTools.isEmpty(apQuery.getCode())) {
			hqlsb.append(" and {0}.code like :code");
			params.put("code", "%" + apQuery.getCode() + "%");
		}

		if (!StringTools.isEmpty(apQuery.getMac())) {
			hqlsb.append(" and {0}.mac like :mac");
			params.put("mac", "%" + apQuery.getMac() + "%");
		}
		if (apQuery.getStatus() != null) {
			hqlsb.append(" and {0}.status = :status");
			params.put("status", apQuery.getStatus());
		}
		if (!StringTools.isEmpty(apQuery.getPlaceName())) {
			hqlsb.append(" and {0}.place.name like :placeName");
			params.put("placeName", "%" + apQuery.getPlaceName() + "%");
//				由于前台传入 ‘%%’ 类似这样的值时decode报错，且找不到这里需要decode的理由，注释掉
//				params.put("placeName", "%" + URLDecoder.decode(apQuery.getPlaceName(),"UTF-8") + "%");
		}
		if (!StringTools.isEmpty(apQuery.getModelName())) {
			hqlsb.append(" and {0}.apModel.name like :modelName");
			params.put("modelName", "%" + apQuery.getModelName() + "%");
		}
		if (apQuery.getInclGroupIds()!=null && apQuery.getInclGroupIds().length!=0){
			hqlsb.append(" and {0}.id in (select new ApEntity(ap.id) from ApEntity ap"
					+ " left join ap.groups gp where gp.id in (:groupIds))");
			params.put("groupIds", apQuery.getInclGroupIds());
		}
		if (StringTools.isNotEmpty(apQuery.getPlaceId())) {
			hqlsb.append(" and {0}.place.id = :placeid");
			params.put("placeid", apQuery.getPlaceId());
		}
		if (!StringTools.isEmpty(apQuery.getInclGroupId())) {
			hqlsb.append(" and :inclGroupId member of {0}.groups");
			params.put("inclGroupId", apQuery.getInclGroupId());
		}
		if (!StringTools.isEmpty(apQuery.getExclGroupId())) {
			hqlsb.append(" and :exclGroupId not member of {0}.groups");
			params.put("exclGroupId", apQuery.getExclGroupId());
		} 
		if(apQuery.getIsNew()){
			hqlsb.append(" and {0}.createtime > :createtime ");
			Calendar now= Calendar.getInstance();
			now.set(Calendar.HOUR_OF_DAY, 0);
			now.set(Calendar.MINUTE, 0);
			now.set(Calendar.SECOND, 0);
			params.put("createtime", now.getTime());
		}
		if(apQuery.getTerminalType()!=null&&apQuery.getTerminalType()==0){//过滤终端设备
			hqlsb.append(" and {0}.terminalType = :terminalType ");
			params.put("terminalType", 0);
		}else if(apQuery.getTerminalType()!=null&&apQuery.getTerminalType()==1){
			hqlsb.append(" and {0}.terminalType = :terminalType ");
			params.put("terminalType", 1);
		}
		
		if(StringTools.isNotEmpty(apQuery.getApproveStatus())){
			hqlsb.append(" and {0}.approve in :approve ");
			params.put("approve", apQuery.getApproveStatusSet());
		}
		return hqlsb;
	}

	@Override
	public List<ApEntity> getPaging(ApQuery query) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = getHql(query, "ap", params);
		return getByHqlPaging(hql, params, query.getPage(), query.getSize());
	}

	@Override
	public boolean exitsByMac(String macStr, String id) {
		String hql = " from ApEntity ap where ap.mac= :mac";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mac", macStr);
		if (id != null) {
			hql += " and ap.id != :id";
			params.put("id", id);
		}
		List<ApEntity> list = getByHql(hql, params);
		if (list != null && list.size() > 0) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean exitsByName(String name, String id,String type) {
		String hql = " from ApEntity ap where ap.name= :name";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", name);
		
		if (StringTools.isNotEmpty(type)) {
			hql += " and ap.terminalType = :terminalType";
			params.put("terminalType", Integer.parseInt(type));
		}
		if (StringTools.isNotEmpty(id)) {
			hql += " and ap.id != :id";
			params.put("id", id);
		}
		List<ApEntity> list = getByHql(hql, params);
		if (list != null && list.size() > 0) {
			return true;
		}
		return false;
	}

	@Override
	public ApEntity getByMac(String apmac) {
		String hql = "from ApEntity where mac=:mac";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mac", apmac);
		List<ApEntity> result = getByHql(hql, params);
		return result.isEmpty() ? null : result.get(0);
	}

	@Override
	public List<ApEntity> getByMacs(Set<String> macs) {
		String hql = "from ApEntity where mac in (:macs)";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("macs", macs);
		return getByHql(hql, params);
	}

	@Override
	public ApEntity getByCode(String code) {
		String hql = "from ApEntity where code=:code";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("code", code);
		List<ApEntity> result = getByHql(hql, params);
		return result.isEmpty() ? null : result.get(0);
	}

	@Override
	public List<Object[]> getFirmPaging(ApQuery query) {
		Map<String, Object> params = new HashMap<String, Object>();
		String baseSql="SELECT ap.id ap_id, ap. NAME ap_name, ap. CODE CODE FROM bus_ap ap"
				+ " WHERE ap.STATUS = 1 AND ap.terminalType = :terminalType";
		
		StringBuffer sb=new StringBuffer(baseSql);
		
		if(query.getInclGroupIds()!=null&&query.getInclGroupIds().length>0){
			sb.append(" /*组限制*/"
					+ " AND ap.id IN (select id from bus_ap ap2 LEFT JOIN sys_group_ap sya"
					+ " on ap2.id=sya.ap_id where sya.group_id in (:groupIds))");
			params.put("groupIds", query.getInclGroupIds());
		}
		
		sb.append(" AND ap.id NOT IN"
				+ " (SELECT ua.ap_id id FROM bus_updateschedule up1 LEFT JOIN"
				+ " bus_update_ap ua ON up1.id = ua.update_id"
				+ " WHERE up1. STATUS IN (0, 1) OR up1. STATUS IS NULL"
				+ " GROUP BY ua.ap_id )");
		
		params.put("terminalType", query.getTerminalType());
		List<Object[]> result = findBySql(sb.toString(), params, query.getPage(),
				query.getSize());
		return result;
	}

	@Override
	public List<ApEntity> getFwVersionLst(String[] ids) {
		String hql = "from ApEntity ap where ap.id in (:ids) and ap.fwversion is null";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ids", ids);
		return getByHql(hql, params);
	}

	@Override
	public List<ApEntity> getApListByPlaceId(String placeId) {
		String hql = "from ApEntity ap where ap.place.id=:placeId";
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("placeId", placeId);
		return getByHql(hql, paramMap);
	}

	@Override
	public List<ApEntity> getByUserId(String userId) {
		String hql = "from ApEntity ap where ap.place.user.id = :userId";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		return getByHql(hql, params);
	}

	@Override
	public List<ApEntity> getByUpdateId(String id) {
		String hql = "from ApEntity ap left join ap.updateApRelations r "
				+ "where r.updateSchedule.id = :scid";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("scid", id);
		List<Object[]> list = getByJoinHql(hql, params);
		List<ApEntity> result = new ArrayList<ApEntity>();
		for (Object[] object : list) {
			ApEntity apEntity = (ApEntity) object[0];
			result.add(apEntity);
		}
		return result;
	}

	@Override
	public List<ApEntity> getByPlaceCode(String placeCode) {
		String hql = "from ApEntity ap where ap.place.code=:placeCode";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("placeCode", placeCode);
		return getByHql(hql, params);
	}

	@Override
	public boolean exitByStrategyId(String id) {
		String hql = "from ApEntity a where a.authStrategy.id=:id";
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("id", id);
		List<ApEntity> list = getByHql(hql, param);
		if (list != null && list.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int getApTip(Set<String> groupIds, int type,boolean isTerminal) {
		StringBuilder hqlsb=new StringBuilder("select count(a.id) from ApEntity a where 1=1 ");
		Map<String, Object> params =new HashMap<String, Object>();
		if (isTerminal) {
			hqlsb.append("and a.terminalType=0 ");
		} else {
			hqlsb.append("and a.terminalType=1 ");
		}
		if (groupIds!=null && groupIds.size()>0){
			hqlsb.append(" and a.id in (select new ApEntity(ap.id) from ApEntity ap"
					+ " left join ap.groups gp where gp.id in (:groupIds)) ");
			params.put("groupIds", groupIds);
		}
		//新增
		if(type==1){
			hqlsb.append(" and a.createtime > :createtime ");
			Calendar now= Calendar.getInstance();
			now.set(Calendar.HOUR_OF_DAY, 0);
			now.set(Calendar.MINUTE, 0);
			now.set(Calendar.SECOND, 0);
			params.put("createtime", now.getTime());
		}
		//在线
		if(type==2){
			hqlsb.append(" and a.status = :status ");
			params.put("status", 1);
		}
		Object object= getUnique(hqlsb.toString(), params);
		return Integer.parseInt(String.valueOf(object));
	}

	@Override
	public List<Object[]> getByConfigSchedule(ApQuery query) {
		String baseSql="SELECT ap.id ap_id, ap. NAME ap_name, ap. CODE CODE FROM bus_ap ap"
				+ " WHERE ap.STATUS = 1 AND ap.terminalType = :terminalType";
				
		StringBuffer sb= new StringBuffer(baseSql);
		Map<String, Object> params=new HashMap<>();
		
		if(query.getInclGroupIds()!=null&&query.getInclGroupIds().length>0){
			sb.append(" /*组限制*/"
					+ " AND ap.id IN (select id from bus_ap ap2 LEFT JOIN sys_group_ap sya"
					+ " on ap2.id=sya.ap_id where sya.group_id in (:groupIds))");
			params.put("groupIds", query.getInclGroupIds());
		}
		
		if(StringTools.isNotEmpty(query.getCode())){
			sb.append(" /*code过滤条件*/"
					+" and ap.code like :code");
			params.put(":code", "%"+query.getCode()+"%");
		}
		sb.append("/*任务id过滤*/"
				+ " AND ap.id NOT IN (SELECT ca1.ap_id id FROM bus_configschedule cs1"
				+ " LEFT JOIN bus_config_ap ca1 ON cs1.id = ca1.config_id"
				+ " WHERE cs1. STATUS IN (0, 1) OR cs1. STATUS IS NULL GROUP BY ca1.ap_id )");
		
		params.put("terminalType", query.getTerminalType());
		List<Object[]> result = findBySql(sb.toString(), params, query.getPage(),
				query.getSize());
		
		return result;
	}
	
}
