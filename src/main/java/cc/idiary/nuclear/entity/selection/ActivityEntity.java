package cc.idiary.nuclear.entity.selection;

import cc.idiary.nuclear.entity.BaseEntity;
import cc.idiary.nuclear.entity.system.UserEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "sel_activity")
public class ActivityEntity extends BaseEntity implements Serializable {

    private String name;
    private Date createTime;
    private UserEntity createUser;

    private Integer stage;  //当前活动阶段

    private StageEntity startStage;
    private StageEntity commitStage;
    private StageEntity checkStage;
    private StageEntity internetStage;
    private StageEntity sessionStage;
    private StageEntity finalStage;
    private StageEntity publicityStage;
    private StageEntity finishStage;

    private Set<CheckCriterionEntity> checkCriterion;

    @Column(name = "name", length = 64)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "create_time")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "create_user_id")
    public UserEntity getCreateUser() {
        return createUser;
    }

    public void setCreateUser(UserEntity createUser) {
        this.createUser = createUser;
    }

    @Column(name = "stage")
    public Integer getStage() {
        return stage;
    }

    public void setStage(Integer stage) {
        this.stage = stage;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "stage_start_id")
    public StageEntity getStartStage() {
        return startStage;
    }

    public void setStartStage(StageEntity startStage) {
        this.startStage = startStage;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "stage_commit_id")
    public StageEntity getCommitStage() {
        return commitStage;
    }

    public void setCommitStage(StageEntity commitStage) {
        this.commitStage = commitStage;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "stage_check_id")
    public StageEntity getCheckStage() {
        return checkStage;
    }

    public void setCheckStage(StageEntity checkStage) {
        this.checkStage = checkStage;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "stage_internet_id")
    public StageEntity getInternetStage() {
        return internetStage;
    }

    public void setInternetStage(StageEntity internetStage) {
        this.internetStage = internetStage;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "stage_session_id")
    public StageEntity getSessionStage() {
        return sessionStage;
    }

    public void setSessionStage(StageEntity sessionStage) {
        this.sessionStage = sessionStage;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "stage_final_id")
    public StageEntity getFinalStage() {
        return finalStage;
    }

    public void setFinalStage(StageEntity finalStage) {
        this.finalStage = finalStage;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "stage_publicity_id")
    public StageEntity getPublicityStage() {
        return publicityStage;
    }

    public void setPublicityStage(StageEntity publicityStage) {
        this.publicityStage = publicityStage;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "stage_finish_id")
    public StageEntity getFinishStage() {
        return finishStage;
    }

    public void setFinishStage(StageEntity finishStage) {
        this.finishStage = finishStage;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "activity")
    public Set<CheckCriterionEntity> getCheckCriterion() {
        return checkCriterion;
    }

    public void setCheckCriterion(Set<CheckCriterionEntity> checkCriterion) {
        this.checkCriterion = checkCriterion;
    }
}
