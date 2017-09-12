package cc.idiary.nuclear.model.business.tax;

import cc.idiary.nuclear.entity.BaseEntity;
import cc.idiary.nuclear.model.BaseModel;

import java.util.List;

/**
 * Created by Neo on 2017/5/9.
 * 税务业务类型分组
 */

public class BusCategoryTypeModel extends BaseModel {
    private String name;
    private List<BusCategoryMode> categories;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<BusCategoryMode> getCategories() {
        return categories;
    }

    public void setCategories(List<BusCategoryMode> categories) {
        this.categories = categories;
    }
}
