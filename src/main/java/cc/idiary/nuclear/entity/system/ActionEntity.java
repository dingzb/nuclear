package cc.idiary.nuclear.entity.system;

import cc.idiary.nuclear.entity.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * Action entity.
 * 
 * @author Dingzb
 */
@Entity
@Table(name = "sys_action")
public class ActionEntity extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 8970635780887548693L;

	private String name;
	private String code;
	private String url;
	private Integer method;
	private String description;
	private Date createTime;
	private Date modifyTime;
	private StateEntity state;
	private Set<RoleEntity> roles;

	// Constructors

	/** default constructor */
	public ActionEntity() {
	}

	public ActionEntity(String id) {
		this.name=id;
	}

	public ActionEntity(String id, String name, String code, String url,
			Integer method) {
		setId(id);
		this.name = name;
		this.code = code;
		this.url = url;
		this.method = method;
	}

	public ActionEntity(String id, String name, String code, String url,
						Integer method, Date createTime, Date modifyTime, String description) {
		setId(id);
		this.name = name;
		this.code = code;
		this.url = url;
		this.method = method;
		this.createTime = createTime;
		this.modifyTime = modifyTime;
		this.description = description;
	}

	/** full constructor */
	public ActionEntity(String id, String name, String code, String url,
						Integer method, String description, Date createTime,
						Date modifyTime, StateEntity states, Set<RoleEntity> roles) {
		setId(id);
		this.name = name;
		this.code = code;
		this.url = url;
		this.method = method;
		this.description = description;
		this.createTime = createTime;
		this.modifyTime = modifyTime;
		this.state = states;
		this.roles = roles;
	}

	@Column(name = "name", nullable = false, length = 20)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "code", length = 100)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "url", length = 500)
	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Column(name = "method")
	public Integer getMethod() {
		return this.method;
	}

	public void setMethod(Integer method) {
		this.method = method;
	}

	@Column(name = "description", length = 500)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "create_time", nullable = false, length = 19)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "modify_time", nullable = false, length = 19)
	public Date getModifyTime() {
		return this.modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="state_id")
	public StateEntity getState() {
		return this.state;
	}

	public void setState(StateEntity state) {
		this.state = state;
	}

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "sys_role_action", joinColumns = { @JoinColumn(name = "action_id", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "role_id", nullable = false, updatable = false) })
	public Set<RoleEntity> getRoles() {
		return this.roles;
	}

	public void setRoles(Set<RoleEntity> roles) {
		this.roles = roles;
	}

}