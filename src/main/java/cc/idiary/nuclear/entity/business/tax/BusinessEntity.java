package cc.idiary.nuclear.entity.business.tax;

import cc.idiary.nuclear.entity.BaseEntity;
import cc.idiary.nuclear.entity.system.UserEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * Created by Neo on 2017/5/9.
 */
@Entity
@Table(name = "bus_tax_bus")
public class BusinessEntity extends BaseEntity {

    private String taxpayerCode;
    private String taxpayerName;
    private Set<BusAttachmentEntity> attachments;
    private String description;
    private Date busTime;   //业务发生时间

    private ExamineEntity firstExamine;
    private Boolean firstHasIssue;
    private ExamineEntity secondExamine;
    private Boolean secondHasIssue;
    private ExamineEntity thirdExamine;
    private Boolean thirdHasIssue;

//    private Boolean amendment; //是否已经整改

    private Integer amendmentCode;   //参考 BUS_AMENDMENT
    private BusCategoryEntity category;

    private AgencyEntity agency;

    private UserEntity create;
    private UserEntity check;
    private UserEntity finalCheck;

    private Date createTime;
    private Date modifyTime;

    private Integer status; // 参考 BUS_STATUS

    public BusinessEntity() {
    }

    public BusinessEntity(String taxpayerCode, String taxpayerName, Set<BusAttachmentEntity> attachments, String description, Date busTime, ExamineEntity firstExamine, Boolean firstHasIssue, ExamineEntity secondExamine, Boolean secondHasIssue, ExamineEntity thirdExamine, Boolean thirdHasIssue, int amendmentCode, BusCategoryEntity category, AgencyEntity agency, UserEntity create, UserEntity check, UserEntity finalCheck, Date createTime, Date modifyTime, Integer status) {
        this.taxpayerCode = taxpayerCode;
        this.taxpayerName = taxpayerName;
        this.attachments = attachments;
        this.description = description;
        this.busTime = busTime;
        this.firstExamine = firstExamine;
        this.firstHasIssue = firstHasIssue;
        this.secondExamine = secondExamine;
        this.secondHasIssue = secondHasIssue;
        this.thirdExamine = thirdExamine;
        this.thirdHasIssue = thirdHasIssue;
//        this.amendment = amendment;
        this.amendmentCode = amendmentCode;
        this.category = category;
        this.agency = agency;
        this.create = create;
        this.check = check;
        this.finalCheck = finalCheck;
        this.createTime = createTime;
        this.modifyTime = modifyTime;
        this.status = status;
    }

    public BusinessEntity(String id, String taxpayerCode, String taxpayerName, String description, Date busTime, Boolean firstHasIssue, Boolean secondHasIssue, Boolean thirdHasIssue, BusCategoryEntity category, AgencyEntity agency, UserEntity create, Date createTime, Date modifyTime, Integer status) {
        setId(id);
        this.taxpayerCode = taxpayerCode;
        this.taxpayerName = taxpayerName;
        this.description = description;
        this.busTime = busTime;
        this.firstHasIssue = firstHasIssue;
        this.secondHasIssue = secondHasIssue;
        this.thirdHasIssue = thirdHasIssue;
        this.category = category;
        this.agency = agency;
        this.create = create;
//        this.check = check;
//        this.finalCheck = finalCheck;
        this.createTime = createTime;
        this.modifyTime = modifyTime;
        this.status = status;
    }

    @Column(name = "taxpayer_code", length = 50)
    public String getTaxpayerCode() {
        return taxpayerCode;
    }

    public void setTaxpayerCode(String taxpayerCode) {
        this.taxpayerCode = taxpayerCode;
    }

    @Column(name = "taxpayer_name", length = 50)
    public String getTaxpayerName() {
        return taxpayerName;
    }

    public void setTaxpayerName(String taxpayerName) {
        this.taxpayerName = taxpayerName;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "business", cascade = CascadeType.ALL)
    public Set<BusAttachmentEntity> getAttachments() {
        return attachments;
    }

    public void setAttachments(Set<BusAttachmentEntity> attachments) {
        this.attachments = attachments;
    }

    @Column(name = "description", length = 4096)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "bus_time", length = 19)
    public Date getBusTime() {
        return busTime;
    }

    public void setBusTime(Date busTime) {
        this.busTime = busTime;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    public BusCategoryEntity getCategory() {
        return category;
    }

    public void setCategory(BusCategoryEntity category) {
        this.category = category;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agency_id")
    public AgencyEntity getAgency() {
        return agency;
    }

    public void setAgency(AgencyEntity agency) {
        this.agency = agency;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "create_user_id")
    public UserEntity getCreate() {
        return create;
    }

    public void setCreate(UserEntity create) {
        this.create = create;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "check_user_id")
    public UserEntity getCheck() {
        return check;
    }

    public void setCheck(UserEntity check) {
        this.check = check;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "final_user_id")
    public UserEntity getFinalCheck() {
        return finalCheck;
    }

    public void setFinalCheck(UserEntity finalCheck) {
        this.finalCheck = finalCheck;
    }

    @Column(name = "create_time", length = 19)
    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "modify_time", length = 19)
    public Date getModifyTime() {
        return this.modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "examine_first_id")
    public ExamineEntity getFirstExamine() {
        return firstExamine;
    }

    public void setFirstExamine(ExamineEntity firstExamine) {
        this.firstExamine = firstExamine;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "examine_second_id")
    public ExamineEntity getSecondExamine() {
        return secondExamine;
    }

    public void setSecondExamine(ExamineEntity secondExamine) {
        this.secondExamine = secondExamine;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "examine_third_id")
    public ExamineEntity getThirdExamine() {
        return thirdExamine;
    }

    public void setThirdExamine(ExamineEntity thirdExamine) {
        this.thirdExamine = thirdExamine;
    }

//    @Column(name = "amendment")
//    public Boolean getAmendment() {
//        return amendment;
//    }
//
//    public void setAmendment(Boolean amendment) {
//        this.amendment = amendment;
//    }

    @Column(name = "amendment_code")
    public Integer getAmendmentCode() {
        return amendmentCode;
    }

    public void setAmendmentCode(Integer amendmentCode) {
        this.amendmentCode = amendmentCode;
    }

    @Column(name = "status")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Transient
    public Boolean getFirstHasIssue() {
        return firstHasIssue;
    }

    public void setFirstHasIssue(Boolean firstHasIssue) {
        this.firstHasIssue = firstHasIssue;
    }

    @Transient
    public Boolean getSecondHasIssue() {
        return secondHasIssue;
    }

    public void setSecondHasIssue(Boolean secondHasIssue) {
        this.secondHasIssue = secondHasIssue;
    }

    @Transient
    public Boolean getThirdHasIssue() {
        return thirdHasIssue;
    }

    public void setThirdHasIssue(Boolean thirdHasIssue) {
        this.thirdHasIssue = thirdHasIssue;
    }

    public static class BUS_STATUS {
        public static final int CREATE = 0;
        public static final int FIRST = 1;
        public static final int SECOND = 2;
        public static final int THIRD = 3;
        //public static final int HAS_ISSUE = 4;
        //public static final int FINISH = 5;
        public static final int FINISH = 4;

        public static final int ERROR_FIRST = -1;
        public static final int ERROR_SECOND = -2;
        public static final int ERROR_THIRD = -3;

        public static int next(int step) {
            step = Math.abs(step);
            return ++step > FINISH ? FINISH : step;
        }

        // 0: 创建, 1: 待自查, 2: 待审查, 3: 待核查, 4: 完成
        // -1: 自查有问题， -2: 审查有问题， -3: 核查有问题
    }

    /**
     * 业务审核状态，参考Linux权限配置
     * 0：未整改或不需要整改
     * 1：已整改
     *
     */
    public static class BusAmendment {

        public static int init() {
            return 0;
        }

        public static boolean first(int code) {
            return (code & 4) == 4;
        }

        public static boolean second(int code) {
            return (code & 2) == 2;
        }

        public static boolean third(int code) {
            return (code & 1) == 1;
        }

        public static int setAmendment(int step, Integer code, boolean a) {
            if (code == null) {
                code = 0;
            }
            switch (step) {
                case BUS_STATUS.FIRST:
                    return a ? code | 4 : code & (Integer.MAX_VALUE ^ 4);
                case BUS_STATUS.SECOND:
                    return a ? code | 2 : code & (Integer.MAX_VALUE ^ 2);
                case BUS_STATUS.THIRD:
                    return a ? code | 1 : code & (Integer.MAX_VALUE ^ 1);
                default:
                    return 0;
            }
        }
    }
}
