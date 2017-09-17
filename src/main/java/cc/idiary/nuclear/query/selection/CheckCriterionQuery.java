package cc.idiary.nuclear.query.selection;

import cc.idiary.nuclear.query.BaseQuery;
import cc.idiary.nuclear.query.PagingQuery;

public class CheckCriterionQuery extends PagingQuery {
    private String name;
    private String activityId;
    private Boolean current;    //是否查询当前正在进行中的活动

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public Boolean getCurrent() {
        return current;
    }

    public void setCurrent(Boolean current) {
        this.current = current;
    }
}
