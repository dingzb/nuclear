package cc.idiary.nuclear.query.system;

import java.util.Date;
import java.util.List;

import cc.idiary.nuclear.query.PagingQuery;
import cc.idiary.nuclear.model.system.StateModel;

/**
 * 
 * @author Dzb
 *
 */
public class StateQuery extends PagingQuery {
	private String parentId;
	private String name;
	private String code;
	private Integer sequence;
	private String description;
	// 0：normalState 1：menuGroup 2：subSystem null:virtual没有视图
	private Integer type;
	private Date createTime;
	private Date modifyTime;
	private List<String> childrenIds;
	private List<StateModel> children;
	private List<String> actionIds;
	private List<String> roleIds;
	private String subsystemId;
	
	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
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

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
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

	public List<String> getChildrenIds() {
		return childrenIds;
	}

	public void setChildrenIds(List<String> childrenIds) {
		this.childrenIds = childrenIds;
	}

	public List<StateModel> getChildren() {
		return children;
	}

	public void setChildren(List<StateModel> children) {
		this.children = children;
	}

	public List<String> getActionIds() {
		return actionIds;
	}

	public void setActionIds(List<String> actionIds) {
		this.actionIds = actionIds;
	}

	public List<String> getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(List<String> roleIds) {
		this.roleIds = roleIds;
	}

	public String getSubsystemId() {
		return subsystemId;
	}

	public void setSubsystemId(String subsystemId) {
		this.subsystemId = subsystemId;
	}
}
