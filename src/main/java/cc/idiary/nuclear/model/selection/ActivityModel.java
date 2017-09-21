package cc.idiary.nuclear.model.selection;

import cc.idiary.nuclear.model.BaseModel;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class ActivityModel extends BaseModel {
    private String name;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date createTime;
    private String createUserName;
    private Integer limitFirst;
    private Integer limitSecond;
    private Integer stage;  //当前活动阶段

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public Integer getLimitFirst() {
        return limitFirst;
    }

    public void setLimitFirst(Integer limitFirst) {
        this.limitFirst = limitFirst;
    }

    public Integer getLimitSecond() {
        return limitSecond;
    }

    public void setLimitSecond(Integer limitSecond) {
        this.limitSecond = limitSecond;
    }

    public Integer getStage() {
        return stage;
    }

    public void setStage(Integer stage) {
        this.stage = stage;
    }
}
