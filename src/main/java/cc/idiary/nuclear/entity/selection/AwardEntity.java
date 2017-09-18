package cc.idiary.nuclear.entity.selection;

import cc.idiary.nuclear.entity.BaseEntity;
import cc.idiary.nuclear.entity.system.UserEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "sel_awards")
public class AwardEntity extends BaseEntity {
    private String name;
    private String description;
    private AwardTypeEntity type;
    private ActivityEntity activity;

    private Date createTime;

    // 1、2、3等奖的标准
    private String first;
    private String second;
    private String third;
    private String deny;

    //具体的评判标准
    private Set<AwardCriterionEntity> criteria;

    @Column(name = "name", length = 64)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "type_id")
    public AwardTypeEntity getType() {
        return type;
    }

    public void setType(AwardTypeEntity type) {
        this.type = type;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "activity_id")
    public ActivityEntity getActivity() {
        return activity;
    }

    public void setActivity(ActivityEntity activity) {
        this.activity = activity;
    }

    @Column(name = "create_time")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "first", length = 1024)
    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    @Column(name = "second", length = 1024)
    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }

    @Column(name = "third", length = 1024)
    public String getThird() {
        return third;
    }

    public void setThird(String third) {
        this.third = third;
    }

    @Column(name = "deny", length = 1024)
    public String getDeny() {
        return deny;
    }

    public void setDeny(String deny) {
        this.deny = deny;
    }

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "award")
    public Set<AwardCriterionEntity> getCriteria() {
        return criteria;
    }

    public void setCriteria(Set<AwardCriterionEntity> criteria) {
        this.criteria = criteria;
    }
}
