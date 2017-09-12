package cc.idiary.nuclear.model.business.tax.statistics;

import java.util.List;

/**
 * Created by Neo on 2017/5/11.
 */
public class XianjuModel {

    private String agencyName;
    private String agencyId;
    private List<StatisticsCategoryTypeModel> recs;

    private int detailCount;

    public int getDetailCount() {
        return detailCount;
    }

    public void setDetailCount(int detailCount) {
        this.detailCount = detailCount;
    }

    public String getAgencyName() {
        return agencyName;
    }

    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }

    public List<StatisticsCategoryTypeModel> getRecs() {
        return recs;
    }

    public void setRecs(List<StatisticsCategoryTypeModel> recs) {
        this.recs = recs;
    }

    public String getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(String agencyId) {
        this.agencyId = agencyId;
    }
}



