package cc.idiary.nuclear.model.business.tax;

import java.util.Set;

/**
 * Created by Neo on 2017/5/18.
 */
public class ExamineModel {
    private int step;
    private String busId;
    private Boolean hasIssue;
    private Set<BusIssueModel> issues;
    private String issueIdStrs; // xx,xx,xx
    private String description;

    public Boolean getHasIssue() {
        return hasIssue;
    }

    public void setHasIssue(Boolean hasIssue) {
        this.hasIssue = hasIssue;
    }

    public Set<BusIssueModel> getIssues() {
        return issues;
    }

    public void setIssues(Set<BusIssueModel> issues) {
        this.issues = issues;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBusId() {
        return busId;
    }

    public void setBusId(String busId) {
        this.busId = busId;
    }

    public String getIssueIdStrs() {
        return issueIdStrs;
    }

    public void setIssueIdStrs(String issueIdStrs) {
        this.issueIdStrs = issueIdStrs;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }
}
