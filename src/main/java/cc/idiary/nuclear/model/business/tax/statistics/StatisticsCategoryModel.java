package cc.idiary.nuclear.model.business.tax.statistics;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StatisticsCategoryModel {
    private String name;
    private String id;
    private int count = 0;
    private int issueCount = 0;
    private int firstIssueCount = 0;
    private int secondIssueCount = 0;
    private int thirdIssueCount = 0;
    private int amendmentCount = 0;             //已经整改的数量
    private Set<String> issueNames = new HashSet<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Set<String> getIssueNames() {
        return issueNames;
    }

    public void setIssueNames(Set<String> issueNames) {
        this.issueNames = issueNames;
    }

    public int getIssueCount() {
        return issueCount;
    }

    public void setIssueCount(int issueCount) {
        this.issueCount = issueCount;
    }

    public int getFirstIssueCount() {
        return firstIssueCount;
    }

    public void setFirstIssueCount(int firstIssueCount) {
        this.firstIssueCount = firstIssueCount;
    }

    public int getSecondIssueCount() {
        return secondIssueCount;
    }

    public void setSecondIssueCount(int secondIssueCount) {
        this.secondIssueCount = secondIssueCount;
    }

    public int getThirdIssueCount() {
        return thirdIssueCount;
    }

    public void setThirdIssueCount(int thirdIssueCount) {
        this.thirdIssueCount = thirdIssueCount;
    }

    public int getAmendmentCount() {
        return amendmentCount;
    }

    public void setAmendmentCount(int amendmentCount) {
        this.amendmentCount = amendmentCount;
    }
}