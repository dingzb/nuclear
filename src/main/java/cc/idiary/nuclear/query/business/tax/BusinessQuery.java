package cc.idiary.nuclear.query.business.tax;

import cc.idiary.nuclear.query.PagingQuery;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.Set;

/**
 * Created by Neo on 2017/5/9.
 */
public class BusinessQuery extends PagingQuery {
    private String content;
    private String categoryId;
    private String[] incCategoryIds;
    private String categoryTypeId;

    private Boolean hasIssue;
    private Boolean firstHasIssue;
    private Boolean secondHasIssue;
    private Boolean thirdHasIssue;
    private Boolean amendmentIssue;

    private String issueId;
    private String agencyId;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date busTimeStart;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date busTimeEnd;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date busTime;

    private String taxpayerCode;
    private String taxpayerName;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date createTimeStart;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date createTimeEnd;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date createTime;

    /*
    * 为三层审查做权限区分
     */
    private Integer status;
    private Integer[] includeStatus;

    private Integer amendmentCode;
    private Integer[] incAmendmentCode;

    /*
    * 为不同部门、职责，做权限区分
     */
    private String createUserId;
    private String createUserName;
    private Set<String> createUserIds;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String[] getIncCategoryIds() {
        return incCategoryIds;
    }

    public void setIncCategoryIds(String[] incCategoryIds) {
        this.incCategoryIds = incCategoryIds;
    }

    public String getCategoryTypeId() {
        return categoryTypeId;
    }

    public void setCategoryTypeId(String categoryTypeId) {
        this.categoryTypeId = categoryTypeId;
    }

    public Boolean getHasIssue() {
        return hasIssue;
    }

    public void setHasIssue(Boolean hasIssue) {
        this.hasIssue = hasIssue;
    }

    public String getIssueId() {
        return issueId;
    }

    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }

    public String getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(String agencyId) {
        this.agencyId = agencyId;
    }

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

    public Date getCreateTimeStart() {
        return createTimeStart;
    }

    public void setCreateTimeStart(Date createTimeStart) {
        this.createTimeStart = createTimeStart;
    }

    public Date getCreateTimeEnd() {
        return createTimeEnd;
    }

    public void setCreateTimeEnd(Date createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getBusTimeStart() {
        return busTimeStart;
    }

    public void setBusTimeStart(Date busTimeStart) {
        this.busTimeStart = busTimeStart;
    }

    public Date getBusTimeEnd() {
        return busTimeEnd;
    }

    public void setBusTimeEnd(Date busTimeEnd) {
        this.busTimeEnd = busTimeEnd;
    }

    public Date getBusTime() {
        return busTime;
    }

    public void setBusTime(Date busTime) {
        this.busTime = busTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer[] getIncludeStatus() {
        return includeStatus;
    }

    public void setIncludeStatus(Integer[] includeStatus) {
        this.includeStatus = includeStatus;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public Set<String> getCreateUserIds() {
        return createUserIds;
    }

    public void setCreateUserIds(Set<String> createUserIds) {
        this.createUserIds = createUserIds;
    }

    public Boolean getFirstHasIssue() {
        return firstHasIssue;
    }

    public void setFirstHasIssue(Boolean firstHasIssue) {
        this.firstHasIssue = firstHasIssue;
    }

    public Boolean getSecondHasIssue() {
        return secondHasIssue;
    }

    public void setSecondHasIssue(Boolean secondHasIssue) {
        this.secondHasIssue = secondHasIssue;
    }

    public Boolean getThirdHasIssue() {
        return thirdHasIssue;
    }

    public void setThirdHasIssue(Boolean thirdHasIssue) {
        this.thirdHasIssue = thirdHasIssue;
    }

    public Boolean getAmendmentIssue() {
        return amendmentIssue;
    }

    public void setAmendmentIssue(Boolean amendmentIssue) {
        this.amendmentIssue = amendmentIssue;
    }

    public Integer getAmendmentCode() {
        return amendmentCode;
    }

    public void setAmendmentCode(Integer amendmentCode) {
        this.amendmentCode = amendmentCode;
    }

    public Integer[] getIncAmendmentCode() {
        return incAmendmentCode;
    }

    public void setIncAmendmentCode(Integer[] incAmendmentCode) {
        this.incAmendmentCode = incAmendmentCode;
    }
}
