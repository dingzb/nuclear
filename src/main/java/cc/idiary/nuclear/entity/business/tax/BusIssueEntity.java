package cc.idiary.nuclear.entity.business.tax;

import cc.idiary.nuclear.entity.BaseEntity;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by Neo on 2017/5/9.
 */
@Entity
@Table(name = "bus_tax_bus_issue")
public class BusIssueEntity extends BaseEntity {

    private String name;
    private Set<ExamineEntity> examines;

    @Column(name = "name", length = 50, nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "bus_tax_examine_issue", joinColumns = {@JoinColumn(name = "issue_id")}, inverseJoinColumns = {@JoinColumn(name = "examine_id")})
    public Set<ExamineEntity> getExamines() {
        return examines;
    }

    public void setExamines(Set<ExamineEntity> examines) {
        this.examines = examines;
    }
}
