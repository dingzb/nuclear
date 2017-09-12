package cc.idiary.nuclear.model.business.tax;

import cc.idiary.nuclear.model.BaseModel;

/**
 * Created by Neo on 2017/5/10.
 */
public class BusCategoryMode extends BaseModel{
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
