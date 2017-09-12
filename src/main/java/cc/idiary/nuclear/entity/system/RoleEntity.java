package cc.idiary.nuclear.entity.system;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import cc.idiary.nuclear.config.RoleType;
import cc.idiary.nuclear.entity.BaseEntity;

/**
 * Role entity.
 * 
 * @author Dingzb
 */
@Entity
@Table(name = "sys_role")
public class RoleEntity extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 3051802885325972205L;

	private String code;
	private String name;
	private String description;
	private Date createTime;
	private Date modifyTime;
	private RoleType type;

	private Set<UserEntity> users;
	private Set<StateEntity> states;
	private Set<ActionEntity> actions;

	public RoleEntity() {
	}

	public RoleEntity(String id) {
		setId(id);
	}

	public RoleEntity(String id, String name, String description,
					  Date createTime, Date modifyTime, Set<UserEntity> users,
					  Set<StateEntity> states, Set<ActionEntity> actions) {
		setId(id);
		this.name = name;
		this.description = description;
		this.createTime = createTime;
		this.modifyTime = modifyTime;
		this.users = users;
		this.states = states;
		this.actions = actions;
	}

	@Column(name = "name", length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
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

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "sys_user_role", joinColumns = { @JoinColumn(name = "role_id", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "user_id", nullable = false, updatable = false) })
	public Set<UserEntity> getUsers() {
		return this.users;
	}

	public void setUsers(Set<UserEntity> users) {
		this.users = users;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "sys_role_state", joinColumns = { @JoinColumn(name = "role_id", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "state_id", nullable = false, updatable = false) })
	public Set<StateEntity> getStates() {
		return this.states;
	}

	public void setStates(Set<StateEntity> states) {
		this.states = states;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "sys_role_action", joinColumns = { @JoinColumn(name = "role_id", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "action_id", nullable = false, updatable = false) })
	public Set<ActionEntity> getActions() {
		return this.actions;
	}

	public void setActions(Set<ActionEntity> actions) {
		this.actions = actions;
	}

	@Column(name = "code", length = 50)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "type", length = 16)
	public RoleType getType() {
		return type;
	}

	public void setType(RoleType type) {
		this.type = type;
	}

}