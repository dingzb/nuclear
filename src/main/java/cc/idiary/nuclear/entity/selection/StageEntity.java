package cc.idiary.nuclear.entity.selection;

import cc.idiary.nuclear.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "sel_stage")
public class StageEntity extends BaseEntity {

    private String comment;
    private Date startTime;
    private Date endTime;

    private ActivityEntity startActivity;
    private ActivityEntity commitActivity;
    private ActivityEntity checkActivity;
    private ActivityEntity internetActivity;
    private ActivityEntity sessionActivity;
    private ActivityEntity finalActivity;
    private ActivityEntity publicityActivity;
    private ActivityEntity finishActivity;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @OneToOne(mappedBy = "startStage")
    public ActivityEntity getStartActivity() {
        return startActivity;
    }

    public void setStartActivity(ActivityEntity startActivity) {
        this.startActivity = startActivity;
    }

    @OneToOne(mappedBy = "commitStage")
    public ActivityEntity getCommitActivity() {
        return commitActivity;
    }

    public void setCommitActivity(ActivityEntity commitActivity) {
        this.commitActivity = commitActivity;
    }

    @OneToOne(mappedBy = "checkStage")
    public ActivityEntity getCheckActivity() {
        return checkActivity;
    }

    public void setCheckActivity(ActivityEntity checkActivity) {
        this.checkActivity = checkActivity;
    }

    @OneToOne(mappedBy = "internetStage")
    public ActivityEntity getInternetActivity() {
        return internetActivity;
    }

    public void setInternetActivity(ActivityEntity internetActivity) {
        this.internetActivity = internetActivity;
    }

    @OneToOne(mappedBy = "sessionStage")
    public ActivityEntity getSessionActivity() {
        return sessionActivity;
    }

    public void setSessionActivity(ActivityEntity sessionActivity) {
        this.sessionActivity = sessionActivity;
    }

    @OneToOne(mappedBy = "finalStage")
    public ActivityEntity getFinalActivity() {
        return finalActivity;
    }

    public void setFinalActivity(ActivityEntity finalActivity) {
        this.finalActivity = finalActivity;
    }

    @OneToOne(mappedBy = "publicityStage")
    public ActivityEntity getPublicityActivity() {
        return publicityActivity;
    }

    public void setPublicityActivity(ActivityEntity publicityActivity) {
        this.publicityActivity = publicityActivity;
    }

    @OneToOne(mappedBy = "finishStage")
    public ActivityEntity getFinishActivity() {
        return finishActivity;
    }

    public void setFinishActivity(ActivityEntity finishActivity) {
        this.finishActivity = finishActivity;
    }
}
