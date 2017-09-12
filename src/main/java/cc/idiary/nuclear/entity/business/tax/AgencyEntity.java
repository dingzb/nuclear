package cc.idiary.nuclear.entity.business.tax;

import cc.idiary.nuclear.entity.system.UserEntity;
import cc.idiary.nuclear.entity.BaseEntity;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by Neo on 2017/5/8.
 */
@Entity
@Table(name = "bus_tax_agency")
public class AgencyEntity extends BaseEntity {
    private String name;
    private AgencyEntity parent;
    private Set<AgencyEntity> children;
    private Set<BusinessEntity> businesses;
    private Set<UserEntity> users;

    @Column(name = "name", nullable = false, length = 50)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    public AgencyEntity getParent() {
        return parent;
    }

    public void setParent(AgencyEntity parent) {
        this.parent = parent;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    public Set<AgencyEntity> getChildren() {
        return children;
    }

    public void setChildren(Set<AgencyEntity> children) {
        this.children = children;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "agency")
    public Set<BusinessEntity> getBusinesses() {
        return businesses;
    }

    public void setBusinesses(Set<BusinessEntity> businesses) {
        this.businesses = businesses;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "agency")
    public Set<UserEntity> getUsers() {
        return users;
    }

    public void setUsers(Set<UserEntity> users) {
        this.users = users;
    }
}
