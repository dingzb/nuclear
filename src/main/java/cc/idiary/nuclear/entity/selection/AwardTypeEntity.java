package cc.idiary.nuclear.entity.selection;

import cc.idiary.nuclear.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Set;

/**
 * 奖项类型
 * 不依附活动存在
 */
@Entity
@Table(name = "sel_awards_type")
public class AwardTypeEntity extends BaseEntity {
    private String name;
    private String description;
    private Set<AwardEntity> awards;

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
}
