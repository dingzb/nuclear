package cc.idiary.nuclear.model.system;

import com.fasterxml.jackson.annotation.JsonFormat;
import cc.idiary.nuclear.model.BaseModel;
import cc.idiary.nuclear.model.business.tax.AgencyModel;

import java.util.Date;
import java.util.Set;


public class UserModel extends BaseModel {
	private String name;
	private String username;
	private String password;
	private String idCard;
	private String email;
	private String phone;
	private String type;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
	private Date createTime;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
	private Date modifyTime;
	private Set<String> roleIds;
	private Set<String> groupIds;
	private Integer placeCount;
	private Boolean selected;//datagrid选中

//	for agency
	private AgencyModel agency;
	private String agencyId;
	private Boolean agencyBoss; //是否是用户所处机构负责人

	public Integer getPlaceCount() {
		return placeCount;
	}

	public void setPlaceCount(Integer placeCount) {	
		this.placeCount = placeCount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

    public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Boolean getSelected() {
		return selected;
	}

	public void setSelected(Boolean selected) {
		this.selected = selected;
	}

	public AgencyModel getAgency() {
		return agency;
	}

	public void setAgency(AgencyModel agency) {
		this.agency = agency;
	}

    public String getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(String agencyId) {
        this.agencyId = agencyId;
    }


    public Boolean getAgencyBoss() {
        return agencyBoss;
    }

    public void setAgencyBoss(Boolean agencyBoss) {
        this.agencyBoss = agencyBoss;
    }

    @Override
	public String toString() {
		return "UserModel [name=" + name + ", username=" + username + ", password=" + password + ", idCard=" + idCard + ", email=" + email + ", phone=" + phone + ", type=" + type
				+ ", createTime=" + createTime + ", roleIds=" + roleIds + ", groupIds=" + groupIds + ", placeCount="
				+ placeCount + ", selected=" + selected + "]";
	}
	
}