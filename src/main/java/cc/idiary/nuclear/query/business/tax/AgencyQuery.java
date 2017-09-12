package cc.idiary.nuclear.query.business.tax;

import cc.idiary.nuclear.query.BaseQuery;

/**
 * Created by Neo on 2017/5/10.
 */
public class AgencyQuery extends BaseQuery {
    private String name;
    private String parentId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
