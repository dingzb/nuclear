package cc.idiary.nuclear.entity.selection;

import cc.idiary.nuclear.entity.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "sel_award_criterion")
public class AwardCriterionEntity extends BaseEntity {

    private String name;
    private String criterion;
    private Integer percent;

    private AwardEntity award;

    @Column(name = "name", length = 64)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "criterion", length = 1024)
    public String getCriterion() {
        return criterion;
    }

    public void setCriterion(String criterion) {
        this.criterion = criterion;
    }

    @Column(name = "percent")
    public Integer getPercent() {
        return percent;
    }

    public void setPercent(Integer percent) {
        this.percent = percent;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "award_id")
    public AwardEntity getAward() {
        return award;
    }

    public void setAward(AwardEntity award) {
        this.award = award;
    }
}
