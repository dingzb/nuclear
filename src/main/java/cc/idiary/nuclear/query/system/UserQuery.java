package cc.idiary.nuclear.query.system;

import java.util.Date;
import java.util.Set;

import cc.idiary.nuclear.config.UserType;
import cc.idiary.nuclear.query.PagingQuery;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 用户查询用query参数对象
 *
 * @author Dzb
 */
public class UserQuery extends PagingQuery {

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date createTimeStart;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date createTimeEnd;
    private String username;
    private String name;
    private String password;
    private Set<String> roleIds;
    private Set<String> groupIds;
    private Set<UserType> types; // null:all empty:none

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(Set<String> roleIds) {
        this.roleIds = roleIds;
    }

    public Set<String> getGroupIds() {
        return groupIds;
    }

    public void setGroupIds(Set<String> groupIds) {
        this.groupIds = groupIds;
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

    public Set<UserType> getTypes() {
        return types;
    }

    public void setTypes(Set<UserType> types) {
        this.types = types;
    }
}
