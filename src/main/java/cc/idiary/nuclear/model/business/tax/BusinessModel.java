package cc.idiary.nuclear.model.business.tax;

import com.fasterxml.jackson.annotation.JsonFormat;
import cc.idiary.nuclear.model.BaseModel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Created by Neo on 2017/5/9.
 */

public class BusinessModel extends BaseModel {

    private String taxpayerCode;
    private String taxpayerName;
    private String content;
    private String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", locale = "zh", timezone = "GMT+8")
    private Date busTime;

    private String agencyId;
    private String categoryId;
    private String categoryTypeId;

    private String agencyName;
    private String categoryName;
    private String categoryTypeName;

    private Boolean firstHasIssue; // to
    private String firstIssueIds; // xxxx,xxxx,xxx , to
    private ExamineModel firstExamine; //vo

    private Boolean secondHasIssue;
    private String secondIssueIds; // xxxx,xxxx,xxx
    private ExamineModel secondExamine;

    private Boolean thirdHasIssue;
    private String thirdIssueIds; // xxxx,xxxx,xxx
    private ExamineModel thirdExamine;

    private String createId;
    private String checkId;
    private String finalCheckId;
    private String createName;
    private String checkName;
    private String finalCheckName;

    private Integer amendmentCode;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", locale = "zh", timezone = "GMT+8")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", locale = "zh", timezone = "GMT+8")
    private Date modifyTime;

    private Integer status;


    public String getTaxpayerCode() {
        return taxpayerCode;
    }

    public void setTaxpayerCode(String taxpayerCode) {
        this.taxpayerCode = taxpayerCode;
    }

    public String getTaxpayerName() {
        return taxpayerName;
    }

    public void setTaxpayerName(String taxpayerName) {
        this.taxpayerName = taxpayerName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(String agencyId) {
        this.agencyId = agencyId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryTypeId() {
        return categoryTypeId;
    }

    public void setCategoryTypeId(String categoryTypeId) {
        this.categoryTypeId = categoryTypeId;
    }

    public String getAgencyName() {
        return agencyName;
    }

    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryTypeName() {
        return categoryTypeName;
    }

    public void setCategoryTypeName(String categoryTypeName) {
        this.categoryTypeName = categoryTypeName;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public String getCheckId() {
        return checkId;
    }

    public void setCheckId(String checkId) {
        this.checkId = checkId;
    }

    public String getFinalCheckId() {
        return finalCheckId;
    }

    public void setFinalCheckId(String finalCheckId) {
        this.finalCheckId = finalCheckId;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getCheckName() {
        return checkName;
    }

    public void setCheckName(String checkName) {
        this.checkName = checkName;
    }

    public String getFinalCheckName() {
        return finalCheckName;
    }

    public void setFinalCheckName(String finalCheckName) {
        this.finalCheckName = finalCheckName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Date getBusTime() {
        return busTime;
    }

    public void setBusTime(Date busTime) {
        this.busTime = busTime;
    }

    public Boolean getFirstHasIssue() {
        return firstHasIssue;
    }

    public void setFirstHasIssue(Boolean firstHasIssue) {
        this.firstHasIssue = firstHasIssue;
    }

    public String getFirstIssueIds() {
        return firstIssueIds;
    }

    public void setFirstIssueIds(String firstIssueIds) {
        this.firstIssueIds = firstIssueIds;
    }

    public ExamineModel getFirstExamine() {
        return firstExamine;
    }

    public void setFirstExamine(ExamineModel firstExamine) {
        this.firstExamine = firstExamine;
    }

    public Boolean getSecondHasIssue() {
        return secondHasIssue;
    }

    public void setSecondHasIssue(Boolean secondHasIssue) {
        this.secondHasIssue = secondHasIssue;
    }

    public String getSecondIssueIds() {
        return secondIssueIds;
    }

    public void setSecondIssueIds(String secondIssueIds) {
        this.secondIssueIds = secondIssueIds;
    }

    public ExamineModel getSecondExamine() {
        return secondExamine;
    }

    public void setSecondExamine(ExamineModel secondExamine) {
        this.secondExamine = secondExamine;
    }

    public Boolean getThirdHasIssue() {
        return thirdHasIssue;
    }

    public void setThirdHasIssue(Boolean thirdHasIssue) {
        this.thirdHasIssue = thirdHasIssue;
    }

    public String getThirdIssueIds() {
        return thirdIssueIds;
    }

    public void setThirdIssueIds(String thirdIssueIds) {
        this.thirdIssueIds = thirdIssueIds;
    }

    public ExamineModel getThirdExamine() {
        return thirdExamine;
    }

    public void setThirdExamine(ExamineModel thirdExamine) {
        this.thirdExamine = thirdExamine;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getAmendmentCode() {
        return amendmentCode;
    }

    public void setAmendmentCode(Integer amendmentCode) {
        this.amendmentCode = amendmentCode;
    }
}
