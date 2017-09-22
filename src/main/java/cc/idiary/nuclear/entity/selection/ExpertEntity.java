package cc.idiary.nuclear.entity.selection;

import cc.idiary.nuclear.entity.BaseEntity;
import cc.idiary.nuclear.entity.system.UserEntity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "sel_expert")
public class ExpertEntity extends BaseEntity {
    //TODO 还有好多字段没有添加，这里只将暂时需要的罗列出来

    private UserEntity account;                 //关联的账户信息
    private ExpertTypeEntity type;              //专家类别
    private Set<CategoryEntity> categories;     //主要专业方向分类代码
    private Set<CategoryGroupEntity> categoryGroups;    //专家分z组的对应的组

    private ActivityEntity activity;

    //专家信息
    private String idNumber;        //身份证号码
    private String duty;            //职务
    private String title;           //职称
    private String organization;    //工作单位
    private String department;      //工作单位部门
    private String speciality;      //专业特长
    private String mobilePhone;     //移动电话
    private String officePhone;     //办公电话
    private String fax;             //传真
    private String email;           //电子邮箱
    private String address;         //通信地址
    private String workExperience;  //工作经历
    private String otherSocialDuty; //其他社会兼职
    private String otherContactsName;   //其他联系人姓名
    private String getOtherContactsPhone;   //其他联系人移动电话


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    public UserEntity getAccount() {
        return account;
    }

    public void setAccount(UserEntity account) {
        this.account = account;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id")
    public ExpertTypeEntity getType() {
        return type;
    }

    public void setType(ExpertTypeEntity type) {
        this.type = type;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "sel_expert_category", joinColumns = {@JoinColumn(name = "expert_id")}, inverseJoinColumns = {@JoinColumn(name = "category_id")})
    public Set<CategoryEntity> getCategories() {
        return categories;
    }

    public void setCategories(Set<CategoryEntity> categories) {
        this.categories = categories;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "sel_experts_category_group", joinColumns = {@JoinColumn(name = "expert_id")}, inverseJoinColumns = {@JoinColumn(name = "category_group_id")})
    public Set<CategoryGroupEntity> getCategoryGroups() {
        return categoryGroups;
    }

    public void setCategoryGroups(Set<CategoryGroupEntity> categoryGroups) {
        this.categoryGroups = categoryGroups;
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
