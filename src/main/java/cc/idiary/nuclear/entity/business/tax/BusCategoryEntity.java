package cc.idiary.nuclear.entity.business.tax;

import cc.idiary.nuclear.entity.BaseEntity;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by Neo on 2017/5/9.
 */
@Entity
@Table(name = "bus_tax_bus_category")
public class BusCategoryEntity extends BaseEntity {
    private String name;
    private BusCategoryTypeEntity type;
    private Set<BusinessEntity> businesses;

    @Column(name = "name", nullable = false, length = 50)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_type_id", nullable = false)
    public BusCategoryTypeEntity getType() {
        return type;
    }

    public void setType(BusCategoryTypeEntity type) {
        this.type = type;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "category")
    public Set<BusinessEntity> getBusinesses() {
        return businesses;
    }

    public void setBusinesses(Set<BusinessEntity> businesses) {
        this.businesses = businesses;
    }
}
