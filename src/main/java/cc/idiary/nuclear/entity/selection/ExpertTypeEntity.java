package cc.idiary.nuclear.entity.selection;

import cc.idiary.nuclear.entity.BaseEntity;

import javax.persistence.*;
import java.util.Set;

/**
 * 专家类别
 * 不依附活动存在
 */
@Entity
@Table(name = "sel_expert_type")
public class ExpertTypeEntity extends BaseEntity {
    private String name;
    private Set<ExpertEntity> experts;

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "type")
    public Set<ExpertEntity> getExperts() {
        return experts;
    }

    public void setExperts(Set<ExpertEntity> experts) {
        this.experts = experts;
    }
}
