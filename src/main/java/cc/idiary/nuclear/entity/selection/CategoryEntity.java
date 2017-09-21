package cc.idiary.nuclear.entity.selection;

import cc.idiary.nuclear.entity.BaseEntity;

import javax.persistence.*;
import java.util.Set;

/**
 * 核能科技代码（专业代码）
 * 不依附活动存在
 */
@Entity
@Table(name = "sel_category")
public class CategoryEntity extends BaseEntity {
    private String code;
    private String name;
    private CategoryEntity parent;
    private Set<CategoryEntity> children;

    private Set<ExpertEntity> experts;

    @Column(name = "code", length = 32)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(name = "name", length = 32)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    public CategoryEntity getParent() {
        return parent;
    }

    public void setParent(CategoryEntity parent) {
        this.parent = parent;
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "parent")
    public Set<CategoryEntity> getChildren() {
        return children;
    }

    public void setChildren(Set<CategoryEntity> children) {
        this.children = children;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "sel_expert_category", joinColumns = {@JoinColumn(name = "category_id")}, inverseJoinColumns = {@JoinColumn(name = "expert_id")})
    public Set<ExpertEntity> getExperts() {
        return experts;
    }

    public void setExperts(Set<ExpertEntity> experts) {
        this.experts = experts;
    }
}
