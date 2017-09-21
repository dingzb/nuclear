package cc.idiary.nuclear.model.selection;

import cc.idiary.nuclear.model.BaseModel;

public class CategoryGroupModel extends BaseModel {
    private String name;
    private String description;
    private Integer limitFirst;
    private Integer limitSecond;
    private Integer limitThird;
    private String activityId;

    private Integer expertCount;
    private String[] categoryNames;
    private String[] categoryIds;
    private String categoryIdsStr;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getLimitFirst() {
        return limitFirst;
    }

    public void setLimitFirst(Integer limitFirst) {
        this.limitFirst = limitFirst;
    }

    public Integer getLimitSecond() {
        return limitSecond;
    }

    public void setLimitSecond(Integer limitSecond) {
        this.limitSecond = limitSecond;
    }

    public Integer getLimitThird() {
        return limitThird;
    }

    public void setLimitThird(Integer limitThird) {
        this.limitThird = limitThird;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public Integer getExpertCount() {
        return expertCount;
    }

    public void setExpertCount(Integer expertCount) {
        this.expertCount = expertCount;
    }

    public String[] getCategoryNames() {
        return categoryNames;
    }

    public void setCategoryNames(String[] categoryNames) {
        this.categoryNames = categoryNames;
    }

    public String[] getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(String[] categoryIds) {
        this.categoryIds = categoryIds;
    }

    public String getCategoryIdsStr() {
        return categoryIdsStr;
    }

    public void setCategoryIdsStr(String categoryIdsStr) {
        this.categoryIdsStr = categoryIdsStr;
    }
}
