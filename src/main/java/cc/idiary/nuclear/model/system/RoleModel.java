package cc.idiary.nuclear.model.system;

import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import cc.idiary.nuclear.model.BaseModel;

public class RoleModel extends BaseModel {

    private String name;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", locale = "zh", timezone = "GMT+8")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", locale = "zh", timezone = "GMT+8")
    private Date modifyTime;
    private Set<String> userIds;
    private Set<String> stateIds;
    private Set<String> actionIds;
    private String type;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

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

    public Set<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(Set<String> userIds) {
        this.userIds = userIds;
    }

    public Set<String> getStateIds() {
        return stateIds;
    }

    public void setStateIds(Set<String> stateIds) {
        this.stateIds = stateIds;
    }

    public Set<String> getActionIds() {
        return actionIds;
    }

    public void setActionIds(Set<String> actionIds) {
        this.actionIds = actionIds;
    }

    @Override
    public String toString() {
        return "RoleModel [" + super.toString() + "name=" + name
                + ", description=" + description + ", createTime=" + createTime
                + ", modifyTime=" + modifyTime + ", userIds=" + userIds
                + ", stateIds=" + stateIds + ", actionIds=" + actionIds + "]";
    }
}
