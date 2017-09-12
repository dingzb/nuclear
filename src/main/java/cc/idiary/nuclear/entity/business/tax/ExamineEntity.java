package cc.idiary.nuclear.entity.business.tax;

import cc.idiary.nuclear.entity.BaseEntity;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by Neo on 2017/5/17.
 * 审查
 */
@Entity
@Table(name = "bus_tax_examine")
public class ExamineEntity extends BaseEntity {
    private Boolean hasIssue;
    private Set<BusIssueEntity> issues;
    private String description;

    private BusinessEntity first;
    private BusinessEntity second;
    private BusinessEntity third;

    @Column(name = "has_issue")
    public Boolean getHasIssue() {
        return hasIssue;
    }

    public void setHasIssue(Boolean hasIssue) {
        this.hasIssue = hasIssue;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "bus_tax_examine_issue", joinColumns = {@JoinColumn(name = "examine_id")}, inverseJoinColumns = {@JoinColumn(name = "issue_id")})

    public Set<BusIssueEntity> getIssues() {
        return issues;
    }

    public void setIssues(Set<BusIssueEntity> issues) {
        this.issues = issues;
    }

    @Column(name = "description", length = 255)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "firstExamine")
    public BusinessEntity getFirst() {
        return first;
    }

    public void setFirst(BusinessEntity first) {
        this.first = first;
    }

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "secondExamine")
    public BusinessEntity getSecond() {
        return second;
    }

    public void setSecond(BusinessEntity second) {
        this.second = second;
    }

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "thirdExamine")
    public BusinessEntity getThird() {
        return third;
    }

    public void setThird(BusinessEntity third) {
        this.third = third;
    }
}
