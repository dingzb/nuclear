package cc.idiary.nuclear.model.system;

import com.fasterxml.jackson.annotation.JsonFormat;
import cc.idiary.nuclear.model.BaseModel;

import java.util.Date;
import java.util.List;

/**
 * 状态模型
 * 
 * @author Dzb
 *
 */
public class StateModel extends BaseModel implements Comparable<StateModel> {

	private String parentId;
	private String name;
	private String code;
	private Integer sequence;
	private String description;
	private String icon;
	// 0：normalState 1：menuGroup 2：subSystem null:virtual没有视图
	private Integer type;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm", locale = "zh", timezone = "GMT+8")
	private Date createTime;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm", locale = "zh", timezone = "GMT+8")
	private Date modifyTime;
	private List<String> childrenIds;
	private List<StateModel> children;
	private List<String> actionIds;
	private List<String> roleIds;
	// tree使用
	private Boolean checked;

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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public List<StateModel> getChildren() {
		return children;
	}

	public void setChildren(List<StateModel> children) {
		this.children = children;
	}

	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	@Override
	public int compareTo(StateModel o) {
		if (this.sequence == null && o.sequence == null)
			return 0;
		if (this.sequence == null && o.sequence != null)
			return Integer.MIN_VALUE;
		if (this.sequence != null && o.sequence == null)
			return Integer.MAX_VALUE;
		return this.sequence.compareTo(o.sequence);
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	@Override
	public String toString() {
		return "StateModel{" +
				"parentId='" + parentId + '\'' +
				", name='" + name + '\'' +
				", code='" + code + '\'' +
				", sequence=" + sequence +
				", description='" + description + '\'' +
				", icon='" + icon + '\'' +
				", type=" + type +
				", createTime=" + createTime +
				", modifyTime=" + modifyTime +
				", childrenIds=" + childrenIds +
				", children=" + children +
				", actionIds=" + actionIds +
				", roleIds=" + roleIds +
				", checked=" + checked +
				'}';
	}
}
