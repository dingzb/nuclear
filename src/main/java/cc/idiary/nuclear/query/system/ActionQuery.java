package cc.idiary.nuclear.query.system;

import cc.idiary.nuclear.query.PagingQuery;

import java.util.Date;
import java.util.Set;

public class ActionQuery extends PagingQuery {

	private String name;
	private String code;
	private String url;
	private Integer method;
	private String description;
	private Date createTime;
	private Date modifyTime;
	private String stateId;
	private Set<String> roleIds;
	private String subsystemId;
	private String menuGroupIds;

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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getMethod() {
		return method;
	}

	public void setMethod(Integer method) {
		this.method = method;
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

	public String getStateId() {
		return stateId;
	}

	public void setStateId(String stateId) {
		this.stateId = stateId;
	}

	public Set<String> getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(Set<String> roleIds) {
		this.roleIds = roleIds;
	}

	public String getSubsystemId() {
		return subsystemId;
	}

	public void setSubsystemId(String subsystemId) {
		this.subsystemId = subsystemId;
	}

	public String getMenuGroupIds() {
		return menuGroupIds;
	}

	public void setMenuGroupIds(String menuGroupIds) {
		this.menuGroupIds = menuGroupIds;
	}

}
