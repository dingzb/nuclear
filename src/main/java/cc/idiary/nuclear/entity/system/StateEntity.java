package cc.idiary.nuclear.entity.system;

import cc.idiary.nuclear.entity.BaseEntity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * StateEntity entity.
 * 
 * @author Dingzb
 */
@Entity
@Table(name = "sys_state")
public class StateEntity extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 5142760944305968496L;

	private StateEntity parent;
	private String name;
	private String code;
	private Integer sequence;
	private String description;
	private String icon;
	private Integer type; // 0：normalState 1：menuGroup 2：subSystem
							// null:virtual(没有视图)
	private Date createTime;
	private Date modifyTime;
	private Set<StateEntity> children;
	private Set<ActionEntity> actions;
	private Set<RoleEntity> roles;

	// Constructors

	/** default constructor */
	public StateEntity() {
	}

	/**
	 * 获取目录及子系统用
	 * 
	 * @param id
	 * @param name
	 * @param code
	 * @param sequence
	 */
	public StateEntity(String id, String name, String code, Integer type,
			Integer sequence, String icon) {
		setId(id);
		this.name = name;
		this.code = code;
		this.type = type;
		this.sequence = sequence;
		this.icon = icon;
	}

	/** minimal constructor */
	public StateEntity(String id, String name, String code, Date createTime,
			Date modifyTime) {
		setId(id);
		this.name = name;
		this.code = code;
		this.createTime = createTime;
		this.modifyTime = modifyTime;
	}

	/** full constructor */
	public StateEntity(String id, StateEntity state, String name, String code,
					   Integer sequence, String description, Date createTime,
					   Date modifyTime, Set<StateEntity> states,
					   Set<ActionEntity> actions, Set<RoleEntity> roles) {
		setId(id);
		this.parent = state;
		this.name = name;
		this.code = code;
		this.sequence = sequence;
		this.description = description;
		this.createTime = createTime;
		this.modifyTime = modifyTime;
		this.children = states;
		this.actions = actions;
		this.roles = roles;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id")
	public StateEntity getParent() {
		return this.parent;
	}

	public void setParent(StateEntity parent) {
		this.parent = parent;
	}

	@Column(name = "name", nullable = false, length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "code", nullable = false, length = 100)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "sequence")
	public Integer getSequence() {
		return this.sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	@Column(name = "icon", length = 64)
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	@Column(name = "description", length = 500)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "create_time", length = 19)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "modify_time", length = 19)
	public Date getModifyTime() {
		return this.modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	@OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, mappedBy = "parent")
	public Set<StateEntity> getChildren() {
		return this.children;
	}

	public void setChildren(Set<StateEntity> children) {
		this.children = children;
	}

	@OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, mappedBy = "state")
	public Set<ActionEntity> getActions() {
		return this.actions;
	}

	public void setActions(Set<ActionEntity> actions) {
		this.actions = actions;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "sys_role_state", joinColumns = { @JoinColumn(name = "state_id", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "role_id", nullable = false, updatable = false) })
	public Set<RoleEntity> getRoles() {
		return this.roles;
	}

	public void setRoles(Set<RoleEntity> roles) {
		this.roles = roles;
	}

	@Column(name = "type")
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

}