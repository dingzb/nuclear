package cc.idiary.nuclear.query.business.management;

import cc.idiary.nuclear.query.PagingQuery;

import java.util.HashSet;
import java.util.Set;

/**
 * ap信息模型
 * 
 * @author lvjianyu
 */
public class ApQuery extends PagingQuery {

	private String placeId;
	private String userId;
	private String name;
	private String code;
	private String modelName; // 型号
	private String mac;
	private String placeName; // 场所名称
	private Integer status;
	// 是否查询ap配置列表 ，默认为否
	private Boolean isQueryCfg = false;
	private String updateScheduleId;
	private String apmac;
	private String inclGroupId; // 配置权限时使用
	private String exclGroupId; // 配置权限时使用
	private String[] inclGroupIds; // 查询列表时使用, 配置group权限查询出来的组id
	private Boolean isNew = false; // 是否新增
	private Integer terminalType;// 区分是终端采集0 还是无线接入设备(原ap)1
	private String approveStatus;// 审核状态过滤

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPlaceId() {
		return placeId;
	}

	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getPlaceName() {
		return placeName;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Boolean getIsQueryCfg() {
		return isQueryCfg;
	}

	public void setIsQueryCfg(Boolean isQueryCfg) {
		this.isQueryCfg = isQueryCfg;
	}

	public String getUpdateScheduleId() {
		return updateScheduleId;
	}

	public void setUpdateScheduleId(String updateScheduleId) {
		this.updateScheduleId = updateScheduleId;
	}

	public String getApmac() {
		return apmac;
	}

	public void setApmac(String apmac) {
		this.apmac = apmac;
	}

	public String getInclGroupId() {
		return inclGroupId;
	}

	public void setInclGroupId(String inclGroupId) {
		this.inclGroupId = inclGroupId;
	}

	public String getExclGroupId() {
		return exclGroupId;
	}

	public void setExclGroupId(String exclGroupId) {
		this.exclGroupId = exclGroupId;
	}

	public String[] getInclGroupIds() {
		return inclGroupIds;
	}

	public void setInclGroupIds(String[] inclGroupIds) {
		this.inclGroupIds = inclGroupIds;
	}

	public Boolean getIsNew() {
		return isNew;
	}

	public void setIsNew(Boolean isNew) {
		this.isNew = isNew;
	}

	public Integer getTerminalType() {
		return terminalType;
	}

	public void setTerminalType(Integer terminalType) {
		this.terminalType = terminalType;
	}

	public String getApproveStatus() {
		return approveStatus;
	}

	public void setApproveStatus(String approveStatus) {
		this.approveStatus = approveStatus;
	}

	public Set<Integer> getApproveStatusSet() {
		if (this.approveStatus != null && !"".equals(this.approveStatus)){
			Set<Integer> rSet= new HashSet<Integer>();
			String[] stus= this.approveStatus.split(",");
			for (String string : stus) {
				rSet.add(Integer.parseInt(string));
			}
			return rSet;
		}
		return null;
	}

}
