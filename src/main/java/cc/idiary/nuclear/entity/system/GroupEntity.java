package cc.idiary.nuclear.entity.system;

import cc.idiary.nuclear.config.GroupType;
import cc.idiary.nuclear.entity.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * GroupEntity entity.
 *
 * @author Dzb
 */
@Entity
@Table(name = "sys_group")
public class GroupEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -3105755900668556971L;

    private String code;
    private String name;
    private String description;
    private Date createTime;
    private Date modifyTime;
    private GroupEntity parent;
    private Set<GroupEntity> children;
    private GroupType type;

    private Set<UserEntity> users;

    public GroupEntity() {
    }

    public GroupEntity(String id) {
        setId(id);
    }

    public GroupEntity(String id, String name, String description,
                       Date createTime, Date modifyTime, Set<UserEntity> users) {
        setId(id);
        this.name = name;
        this.description = description;
        this.createTime = createTime;
        this.modifyTime = modifyTime;
        this.users = users;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    public GroupEntity getParent() {
        return parent;
    }

    public void setParent(GroupEntity parent) {
        this.parent = parent;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    public Set<GroupEntity> getChildren() {
        return children;
    }

    public void setChildren(Set<GroupEntity> children) {
        this.children = children;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 16)
    public GroupType getType() {
        return type;
    }

    public void setType(GroupType type) {
        this.type = type;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "sys_user_group", joinColumns = {@JoinColumn(name = "group_id", nullable = false, updatable = false)}, inverseJoinColumns = {@JoinColumn(name = "user_id", nullable = false, updatable = false)})
    public Set<UserEntity> getUsers() {
        return this.users;
    }

    public void setUsers(Set<UserEntity> users) {
        this.users = users;
    }

    @Column(name = "code", length = 50)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}