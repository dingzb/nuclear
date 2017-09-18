package cc.idiary.nuclear.query.selection;

public class AwardQuery extends SelectionBaseQuery {
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
