package cc.idiary.nuclear.query.business.tax;

import cc.idiary.nuclear.query.BaseQuery;

/**
 * Created by Neo on 2017/5/9.
 */

public class BusCategoryQuery extends BaseQuery {
    private String name;
    private String typeId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }
}
