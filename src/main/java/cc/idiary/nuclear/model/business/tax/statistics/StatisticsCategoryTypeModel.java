package cc.idiary.nuclear.model.business.tax.statistics;

import java.util.List;

public class StatisticsCategoryTypeModel {
    private String name;
    private String id;
    private List<StatisticsCategoryModel> recs;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<StatisticsCategoryModel> getRecs() {
        return recs;
    }

    public void setRecs(List<StatisticsCategoryModel> recs) {
        this.recs = recs;
    }
}