package cc.idiary.nuclear.entity.business.tax;

import cc.idiary.nuclear.entity.BaseEntity;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by Neo on 2017/5/9.
 * 税务业务类型分组
 */
@Entity
@Table(name = "bus_tax_bus_category_type")
public class BusCategoryTypeEntity extends BaseEntity {
    private String name;
    private Set<BusCategoryEntity> categories;

    @Column(name = "name", nullable = false, length = 50)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "type")
    public Set<BusCategoryEntity> getCategories() {
        return categories;
    }

    public void setCategories(Set<BusCategoryEntity> categories) {
        this.categories = categories;
    }
}
