package cc.idiary.nuclear.query.system;

import java.util.Date;
import java.util.Set;

import cc.idiary.nuclear.query.PagingQuery;
import cc.idiary.nuclear.config.RoleType;

public class RoleQuery extends PagingQuery {

	private String name;
	private String description;
	private Date createTime;
	private Date modifyTime;
	private Set<String> userIds;
	private Set<String> stateIds;
	private Set<String> actionIds;
	private Set<RoleType> types;
	private String inclUserId; // 用户配置组时使用
	private String exclUserId;
	private boolean userCheck;
	
	
	public boolean isUserCheck() {
		return userCheck;
	}

	public void setUserCheck(boolean userCheck) {
		this.userCheck = userCheck;
	}

	public Set<RoleType> getTypes() {
		return types;
	}

	public void setTypes(Set<RoleType> types) {
		this.types = types;
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

	public Set<String> getUserIds() {
		return userIds;
	}

	public void setUserIds(Set<String> userIds) {
		this.userIds = userIds;
	}

	public Set<String> getStateIds() {
		return stateIds;
	}

	public void setStateIds(Set<String> stateIds) {
		this.stateIds = stateIds;
	}

	public Set<String> getActionIds() {
		return actionIds;
	}

	public void setActionIds(Set<String> actionIds) {
		this.actionIds = actionIds;
	}
}
