package cc.idiary.nuclear.query.system;

import java.util.Date;
import java.util.Set;

import cc.idiary.nuclear.query.PagingQuery;
import cc.idiary.nuclear.config.GroupType;

/**
 * 组查询模型
 * 
 * @since 2015-11-9
 * @author Dzb
 *
 */
public class GroupQuery extends PagingQuery {

	private String name;// 名称
	private String code;// 编码
	private String description;// 描述
	private Date createTime;// 创建时间
	private Date modifyTime;// 修改时间
	private Set<GroupType> types; // null:all empty:none
	private String monitorById; // 配置监视时使用
	private String exMonitorById; // 配置监视时使用
	private String inclUserId; // 用户配置组时使用
	private String exclUserId; // 用户配置组时使用
	private Boolean inclUserType; // 查询时是否显示用户类型

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Set<GroupType> getTypes() {
		return types;
	}

	public void setTypes(Set<GroupType> types) {
		this.types = types;
	}

	public String getMonitorById() {
		return monitorById;
	}

	public void setMonitorById(String monitorId) {
		this.monitorById = monitorId;
	}

	public String getExMonitorById() {
		return exMonitorById;
	}

	public void setExMonitorById(String exMonitorId) {
		this.exMonitorById = exMonitorId;
	}

	public String getInclUserId() {
		return inclUserId;
	}

	public void setInclUserId(String inclUserId) {
		this.inclUserId = inclUserId;
	}

	public String getExclUserId() {
		return exclUserId;
	}

	public void setExclUserId(String exclUserId) {
		this.exclUserId = exclUserId;
	}

	public Boolean getInclUserType() {
		return inclUserType;
	}

	public void setInclUserType(Boolean inclUserType) {
		this.inclUserType = inclUserType;
	}
}
