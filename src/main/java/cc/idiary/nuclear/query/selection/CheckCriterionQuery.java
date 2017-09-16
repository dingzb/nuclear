package cc.idiary.nuclear.query.selection;

import cc.idiary.nuclear.query.BaseQuery;
import cc.idiary.nuclear.query.PagingQuery;

public class CheckCriterionQuery extends PagingQuery {
    private String name;
    private String activityId;

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
}
