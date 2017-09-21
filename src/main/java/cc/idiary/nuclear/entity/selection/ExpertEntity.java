package cc.idiary.nuclear.entity.selection;

import cc.idiary.nuclear.entity.BaseEntity;
import cc.idiary.nuclear.entity.system.UserEntity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "sel_expert")
public class ExpertEntity extends BaseEntity {
    //TODO 还有好多字段没有添加，这里只将暂时需要的罗列出来

    private UserEntity account;
    private ExpertTypeEntity type;
    private Set<CategoryEntity> categories;
    private Set<CategoryGroupEntity> categoryGroups;

    private ActivityEntity activity;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    public UserEntity getAccount() {
        return account;
    }

    public void setAccount(UserEntity account) {
        this.account = account;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id")
    public ExpertTypeEntity getType() {
        return type;
    }

    public void setType(ExpertTypeEntity type) {
        this.type = type;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "sel_expert_category", joinColumns = {@JoinColumn(name = "expert_id")}, inverseJoinColumns = {@JoinColumn(name = "category_id")})
    public Set<CategoryEntity> getCategories() {
        return categories;
    }

    public void setCategories(Set<CategoryEntity> categories) {
        this.categories = categories;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "sel_experts_category_group", joinColumns = {@JoinColumn(name = "expert_id")}, inverseJoinColumns = {@JoinColumn(name = "category_group_id")})
    public Set<CategoryGroupEntity> getCategoryGroups() {
        return categoryGroups;
    }

    public void setCategoryGroups(Set<CategoryGroupEntity> categoryGroups) {
        this.categoryGroups = categoryGroups;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id")
    public ActivityEntity getActivity() {
        return activity;
    }

    public void setActivity(ActivityEntity activity) {
        this.activity = activity;
    }
}
