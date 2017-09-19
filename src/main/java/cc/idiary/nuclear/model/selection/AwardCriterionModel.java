package cc.idiary.nuclear.model.selection;

import cc.idiary.nuclear.model.BaseModel;

public class AwardCriterionModel extends BaseModel{
    private String name;
    private String criterion;
    private Integer percent;
    private String awardId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCriterion() {
        return criterion;
    }

    public void setCriterion(String criterion) {
        this.criterion = criterion;
    }

    public Integer getPercent() {
        return percent;
    }

    public void setPercent(Integer percent) {
        this.percent = percent;
    }

    public String getAwardId() {
        return awardId;
    }

    public void setAwardId(String awardId) {
        this.awardId = awardId;
    }
}
