package cc.idiary.nuclear.entity.selection;

import cc.idiary.nuclear.entity.BaseEntity;
import cc.idiary.nuclear.entity.system.UserEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * 形式审查标准
 */
@Entity
@Table(name = "sel_check_criterion")
public class CheckCriterionEntity extends BaseEntity {
    private String name;
    private String description;
    private Date createTime;
    private UserEntity createUser;

    private ActivityEntity activity;

    @Column(name = "name", length = 64)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "description", length = 255)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "create_time")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "create_user_id")
    public UserEntity getCreateUser() {
        return createUser;
    }

    public void setCreateUser(UserEntity createUser) {
        this.createUser = createUser;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id")
    public ActivityEntity getActivity() {
        return activity;
    }

    public void setActivity(ActivityEntity activity) {
        this.activity = activity;
    }
}
