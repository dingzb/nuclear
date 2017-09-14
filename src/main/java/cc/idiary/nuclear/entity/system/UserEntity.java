package cc.idiary.nuclear.entity.system;

import cc.idiary.nuclear.config.UserType;
import cc.idiary.nuclear.entity.BaseEntity;
import cc.idiary.nuclear.entity.selection.ActivityEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * UserEntity entity.
 *
 * @author Dzb
 */
@Entity
@Table(name = "sys_user")
public class UserEntity extends BaseEntity implements java.io.Serializable {

    private static final long serialVersionUID = -372537249772009500L;

    private String name;
    private String username;
    private String password;
    private Date createTime;
    private Date modifyTime;
    private String email;
    private String phone;
    private String idCard;
    private UserType type;

    private Set<GroupEntity> groups = new HashSet<GroupEntity>(0);
    private Set<RoleEntity> roles = new HashSet<RoleEntity>(0);

    //for selection
    private Set<ActivityEntity> activities;

    public UserEntity() {
    }

    public UserEntity(String id) {
        setId(id);
    }


    public UserEntity(String id, String username, String password,
                      Date createTime, Date modifyTime) {
        super.setId(id);
        this.username = username;
        this.password = password;
        this.createTime = createTime;
        this.modifyTime = modifyTime;
    }

    public UserEntity(String id, String name, String username, Date createTime,
                      UserType type) {
        super();
        setId(id);
        this.name = name;
        this.username = username;
        this.createTime = createTime;
        this.type = type;
    }

    public UserEntity(String id, String name, String idCard) {
        super();
        setId(id);
//		this.contacts = new ContactsEntity();
//		contacts.setIdCard(idcard);
        this.name = name;
    }

    @Column(name = "username", nullable = false, length = 50)
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name = "password", nullable = false, length = 50)
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "sys_user_group", joinColumns = {@JoinColumn(name = "user_id", nullable = false, updatable = false)}, inverseJoinColumns = {@JoinColumn(name = "group_id", nullable = false, updatable = false)})
    public Set<GroupEntity> getGroups() {
        return this.groups;
    }

    public void setGroups(Set<GroupEntity> groups) {
        this.groups = groups;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "sys_user_role", joinColumns = {@JoinColumn(name = "user_id", nullable = false, updatable = false)}, inverseJoinColumns = {@JoinColumn(name = "role_id", nullable = false, updatable = false)})
    public Set<RoleEntity> getRoles() {
        return this.roles;
    }

    public void setRoles(Set<RoleEntity> roles) {
        this.roles = roles;
    }

    @Column(name = "name", length = 50, nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "email", length = 50)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "phone", length = 20)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Column(name = "id_card", length = 50)
    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 16)
    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "createUser")
    public Set<ActivityEntity> getActivities() {
        return activities;
    }

    public void setActivities(Set<ActivityEntity> activities) {
        this.activities = activities;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", createTime=" + createTime +
                ", modifyTime=" + modifyTime +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", idCard='" + idCard + '\'' +
                ", type=" + type +
                ", groups=" + groups +
                ", roles=" + roles +
                '}';
    }
}