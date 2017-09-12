package cc.idiary.nuclear.model.business.tax.statistics;

import java.util.List;

/**
 * Created by Neo on 2017/5/26.
 */
public class FenjuModel {
    private String userName;
    private String userId;
    private List<StatisticsCategoryTypeModel> recs;

    private int detailCount;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<StatisticsCategoryTypeModel> getRecs() {
        return recs;
    }

    public void setRecs(List<StatisticsCategoryTypeModel> recs) {
        this.recs = recs;
    }

    public int getDetailCount() {
        return detailCount;
    }

    public void setDetailCount(int detailCount) {
        this.detailCount = detailCount;
    }
}
