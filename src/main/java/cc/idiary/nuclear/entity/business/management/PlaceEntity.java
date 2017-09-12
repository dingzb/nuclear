package cc.idiary.nuclear.entity.business.management;

import cc.idiary.nuclear.entity.system.GroupEntity;
import cc.idiary.nuclear.entity.system.UserEntity;
import cc.idiary.nuclear.entity.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * Place entity.
 * <p>
 * 参考：《公共场所无线上网安全管理系统无线上网接入安全技术要求》【场所基础信息（WA_ BASIC_FJ_ 0001）】
 * </p>
 * 
 * @author Dzb
 */
//@Entity
//@Table(name = "bus_place")
public class PlaceEntity extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -9067371073801568433L;

	private UserEntity user;
	private PlaceTypeEntity placeType;// 6、场所服务类型(要求中指明是数值型，但其代码类型表中有ABCDE存在所以这里设置成字符型)
	private String regionCode; // 3、区域编码
	private String name; // 2、上网服务场所名称 256
	private String description;
	private String address; // 3、场所详细地址（地址中的省市县信息可以由regionCode查询自动生成） 256
	private String longitude; // 4、场所经度 10
	private String latitude; // 5、场所纬度 10
	private String outip;// 16、网络接入账号或固定IP地址 64
	private Date createTime;
	private Date modifyTime;
	private Set<ApEntity> aps;
	private VendorEntity vendor;
	private String code;// 1、上网服务场所编码 14
	private String businessType;// 7、场所经营性质 0,表示经营；1，表示非经营；3其他
	private String businessHourStart; // 12、营业开始时间 5
	private String businessHourEnd; // 13、营业结束时间 5
	private String accessModelCode; // 14、场所网络接入方式 2
									// 參考《信息安全管理代码》【互联网上网服务营业场所信息安全管理代码第6部分：营业场所接入方式代码】
	private String ispCode;// 15、场所网络接入服务商 2 参考《公共场所无线上网安全管理系统无线上网接入安全技术要求》
							// 【场所网络接入服务商代码表】
	private String securityCode;// 17、安全厂商组织机构代码 9
	private String legalPersonName;// 8、场所经营法人 64
	private String legalPersonIdType;// 9、经营法人有效证件类型 3
	private String legalPersonIdNo;// 10、经营法人有效证件号码 128
	private Integer status; // 场所状态 (场所营业/停业状态/其他状态)

	private String serialNumber;// 流水号
	private String employess;// 从业人数
	private String machine;// 报装机器数
	private String server;// 服务器数
	private String organization;// organization
	private String postal;// 邮编
	private String legalPersonPhone;// 法定代表人
	private String duty;// 场所负责人
	private String dutyPhone;// 场所负责人电话
	private String safety;// 信息安全员
	private String safetyPhone;// 安全员电话
	private String safetyEmail;// 安全员email
	private String netControl;// 所属网监
	private String netPeople;// 网监负责人
	private String netPhone;// 网监负责人电话
	private Set<GroupEntity> groups; // 组
	private Boolean inService; // 在服-该场所是否还在服务
	private Integer approve;

	@Column(name = "approve", length = 11)
	public Integer getApprove() {
		return approve;
	}

	public void setApprove(Integer approve) {
		this.approve = approve;
	}

	
	
	public String getBusinessHourStart() {
		return businessHourStart;
	}

	public void setBusinessHourStart(String businessHourStart) {
		this.businessHourStart = businessHourStart;
	}

	public String getBusinessHourEnd() {
		return businessHourEnd;
	}

	public void setBusinessHourEnd(String businessHourEnd) {
		this.businessHourEnd = businessHourEnd;
	}

	// Constructors
	/** default constructor */
	public PlaceEntity() {
	}

	public PlaceEntity(String id) {
		setId(id);
	}

	/** minimal constructor */
	public PlaceEntity(String id, PlaceTypeEntity placeType, String name, String description, Date createTime,
			Date modifyTime) {
		setId(id);
		this.placeType = placeType;
		this.name = name;
		this.description = description;
		this.createTime = createTime;
		this.modifyTime = modifyTime;
	}

	/** full constructor */
	public PlaceEntity(String id, UserEntity user, PlaceTypeEntity placeType, String regionCode, String name, String description, String address, String longitude, String latitude,
					   String outip, Date createTime, Date modifyTime, Set<ApEntity> aps) {
		setId(id);
		this.user = user;
		this.placeType = placeType;
		this.regionCode = regionCode;
		this.name = name;
		this.description = description;
		this.address = address;
		this.longitude = longitude;
		this.latitude = latitude;
		this.outip = outip;
		this.createTime = createTime;
		this.modifyTime = modifyTime;
		this.aps = aps;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	public UserEntity getUser() {
		return this.user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "type_id", nullable = true)
	public PlaceTypeEntity getPlaceType() {
		return this.placeType;
	}

	public void setPlaceType(PlaceTypeEntity placeType) {
		this.placeType = placeType;
	}

	@Column(name = "region_code")
	public String getRegionCode() {
		return this.regionCode;
	}

	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}

	@Column(name = "name", nullable = false, length = 256)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "description", nullable = false, length = 500)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "address", length = 500)
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "longitude", length = 10)
	public String getLongitude() {
		return this.longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	@Column(name = "latitude", length = 10)
	public String getLatitude() {
		return this.latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	@Column(name = "outip", length = 15)
	public String getOutip() {
		return this.outip;
	}

	public void setOutip(String outip) {
		this.outip = outip;
	}

	@Column(name = "createTime", nullable = false, length = 19)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "modifyTime", nullable = false, length = 19)
	public Date getModifyTime() {
		return this.modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "place")
	public Set<ApEntity> getAps() {
		return this.aps;
	}

	public void setAps(Set<ApEntity> aps) {
		this.aps = aps;
	}

	@Column(name = "code", length = 14)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "businessType", length = 1)
	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	@Column(name = "securityCode", length = 9)
	public String getSecurityCode() {
		return securityCode;
	}

	public void setSecurityCode(String securityCode) {
		this.securityCode = securityCode;
	}

	@Column(name = "accessmodel_code", length = 32)
	public String getAccessModelCode() {
		return accessModelCode;
	}

	public void setAccessModelCode(String accessModelCode) {
		this.accessModelCode = accessModelCode;
	}

	@Column(name = "isp_code", length = 32)
	public String getIspCode() {
		return ispCode;
	}

	public void setIspCode(String ispCode) {
		this.ispCode = ispCode;
	}

	@Column(name = "legalPersonName", length = 64)
	public String getLegalPersonName() {
		return legalPersonName;
	}

	public void setLegalPersonName(String legalPersonName) {
		this.legalPersonName = legalPersonName;
	}

	@Column(name = "legalPersonIdType", length = 3)
	public String getLegalPersonIdType() {
		return legalPersonIdType;
	}

	public void setLegalPersonIdType(String legalPersonIdType) {
		this.legalPersonIdType = legalPersonIdType;
	}

	@Column(name = "legalPersonIdNo", length = 128)
	public String getLegalPersonIdNo() {
		return legalPersonIdNo;
	}

	public void setLegalPersonIdNo(String legalPersonIdNo) {
		this.legalPersonIdNo = legalPersonIdNo;
	}

	@Column(name = "status", length = 1)
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	/** 11111111 **/
	@Column(name = "serialNumber", length = 32)
	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	@Column(name = "employess", length = 32)
	public String getEmployess() {
		return employess;
	}

	public void setEmployess(String employess) {
		this.employess = employess;
	}

	@Column(name = "machine", length = 32)
	public String getMachine() {
		return machine;
	}

	public void setMachine(String machine) {
		this.machine = machine;
	}

	@Column(name = "server", length = 32)
	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	@Column(name = "organization", length = 32)
	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	@Column(name = "postal", length = 32)
	public String getPostal() {
		return postal;
	}

	public void setPostal(String postal) {
		this.postal = postal;
	}

	@Column(name = "legalPersonPhone", length = 32)
	public String getLegalPersonPhone() {
		return legalPersonPhone;
	}

	public void setLegalPersonPhone(String legalPersonPhone) {
		this.legalPersonPhone = legalPersonPhone;
	}

	@Column(name = "duty", length = 32)
	public String getDuty() {
		return duty;
	}

	public void setDuty(String duty) {
		this.duty = duty;
	}

	@Column(name = "dutyPhone", length = 32)
	public String getDutyPhone() {
		return dutyPhone;
	}

	public void setDutyPhone(String dutyPhone) {
		this.dutyPhone = dutyPhone;
	}

	@Column(name = "safety", length = 32)
	public String getSafety() {
		return safety;
	}

	public void setSafety(String safety) {
		this.safety = safety;
	}

	@Column(name = "safetyPhone", length = 32)
	public String getSafetyPhone() {
		return safetyPhone;
	}

	public void setSafetyPhone(String safetyPhone) {
		this.safetyPhone = safetyPhone;
	}

	@Column(name = "safetyEmail", length = 32)
	public String getSafetyEmail() {
		return safetyEmail;
	}

	public void setSafetyEmail(String safetyEmail) {
		this.safetyEmail = safetyEmail;
	}

	@Column(name = "netControl", length = 32)
	public String getNetControl() {
		return netControl;
	}

	public void setNetControl(String netControl) {
		this.netControl = netControl;
	}

	@Column(name = "netPeople", length = 32)
	public String getNetPeople() {
		return netPeople;
	}

	public void setNetPeople(String netPeople) {
		this.netPeople = netPeople;
	}

	@Column(name = "netPhone", length = 32)
	public String getNetPhone() {
		return netPhone;
	}

	public void setNetPhone(String netPhone) {
		this.netPhone = netPhone;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vendor_id")
	public VendorEntity getVendor() {
		return vendor;
	}

	public void setVendor(VendorEntity vendor) {
		this.vendor = vendor;
	}

	@ManyToMany
	@JoinTable(name = "sys_group_place", joinColumns = { @JoinColumn(name = "place_id") }, inverseJoinColumns = {
			@JoinColumn(name = "group_id") })
	public Set<GroupEntity> getGroups() {
		return groups;
	}

	public void setGroups(Set<GroupEntity> groups) {
		this.groups = groups;
	}

	@Column(name = "inService", nullable = false)
	public Boolean getInService() {
		return inService;
	}

	public void setInService(Boolean inService) {
		this.inService = inService;
	}

}