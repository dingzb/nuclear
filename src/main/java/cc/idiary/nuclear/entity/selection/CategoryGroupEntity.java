package cc.idiary.nuclear.entity.selection;

import cc.idiary.nuclear.entity.BaseEntity;

import javax.persistence.*;
import java.util.Set;

/**
 * 专家专业组
 */
@Entity
@Table(name = "sel_category_group")
public class CategoryGroupEntity extends BaseEntity{
    private String name;
    private String description;
    private Integer limitFirst;
    private Integer limitSecond;
    private Integer limitThird;
    private Set<ExpertEntity> experts;

    private Set<CategoryEntity> categories;

    private ActivityEntity activity;

    @Column(name = "name", length = 32)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "description", length = 4096)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "limit_first")
    public Integer getLimitFirst() {
        return limitFirst;
    }

    public void setLimitFirst(Integer limitFirst) {
        this.limitFirst = limitFirst;
    }

    @Column(name = "limit_second")
    public Integer getLimitSecond() {
        return limitSecond;
    }

    public void setLimitSecond(Integer limitSecond) {
        this.limitSecond = limitSecond;
    }

    @Column(name = "limit_third")
    public Integer getLimitThird() {
        return limitThird;
    }

    public void setLimitThird(Integer limitThird) {
        this.limitThird = limitThird;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "sel_category_group_category", joinColumns = {@JoinColumn(name = "category_group_id")}, inverseJoinColumns = {@JoinColumn(name = "category_id")})
    public Set<CategoryEntity> getCategories() {
        return categories;
    }

    public void setCategories(Set<CategoryEntity> categories) {
        this.categories = categories;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id")
    public ActivityEntity getActivity() {
        return activity;
    }

    public void setActivity(ActivityEntity activity) {
        this.activity = activity;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "sel_experts_category_group", joinColumns = {@JoinColumn(name = "category_group_id")}, inverseJoinColumns = {@JoinColumn(name = "expert_id")})
    public Set<ExpertEntity> getExperts() {
        return experts;
    }

    public void setExperts(Set<ExpertEntity> experts) {
        this.experts = experts;
    }
}
