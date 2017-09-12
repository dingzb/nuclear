package cc.idiary.nuclear.entity.business.management;

import cc.idiary.nuclear.entity.BaseEntity;
import cc.idiary.nuclear.entity.system.GroupEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * Ap entity.
 * 
 * @author Dzb
 */
//@Entity
//@Table(name = "bus_ap")
public class ApEntity extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -5285900370984266684L;

	private ApModelEntity apModel;
	private PlaceEntity place;// 上网服务场所编码(见实体)
	private String code;// AP 设备编号 21
	private String name;
	private String mac;// AP 设备MAC地址 17
	private String position;
	private Integer status; // AP 状态
							// ：1、服务在/离线状态；2、数据在/离线状态；3、设备工作/维护/异常状态；(设备报警程序维护)
	private String description;
	private Date createTime;
	private Date modifyTime;
	private Integer type;// 移动或者固定 前端定义 0固定 1移动
	private String floor;// 楼层 16
	private String longitude; // AP设备经度 10
	private String latitude; // AP设备纬度 10
	private String bdLng;
	private String bdLat;
	private String siteInfo;// 站点信息 128
	private String subwayLine;// 地铁线路信息 256
	private String subwayVehicle;// 地铁车辆信息 256
	private String subwayCarriage;// 地铁车厢编号(地铁车厢信息) 256
	private String plateNumber;// 车牌号码 64

	private String ndpi;
	private String fwversion; // 注册成功之后会有一个版本信息

	private Set<GroupEntity> groups;
	private Date validity; // license 有效期
	private VendorEntity vendor; // 安全厂商

	private Integer terminalType;// 区分是终端采集0 还是无线接入设备(原ap)1
	private Integer uploadTimeInterval; // 数据上传采集间隔，单位秒（s）
	private Integer collectionRadius; // 单位米（m）

	private Integer baseInfoUploadInterval; // 数据上传采集间隔，单位小时（h）
	private Integer approve;

	// Constructors

	/** default constructor */
	public ApEntity() {
	}

	public ApEntity(String id) {
		setId(id);
	}

	/** minimal constructor */
	public ApEntity(String id, ApModelEntity apModel, PlaceEntity place, String sn, String name, String mac,
                    Integer status, Date createTime, Date modfiytime) {
		setId(id);
		this.apModel = apModel;
		this.place = place;
		this.code = sn;
		this.name = name;
		this.mac = mac;
		this.status = status;
		this.createTime = createTime;
		this.modifyTime = modfiytime;
	}

	/** full constructor */
	public ApEntity(String id, ApModelEntity apModel, PlaceEntity place, String sn, String name, String mac, String position, Integer status, String description, Date createTime, Date modifyTime) {
		setId(id);
		this.apModel = apModel;
		this.place = place;
		this.code = sn;
		this.name = name;
		this.mac = mac;
		this.position = position;
		this.status = status;
		this.description = description;
		this.createTime = createTime;
		this.modifyTime = modifyTime;
	}
	
	public ApEntity(String id, String name, String mac, Integer status) {
		setId(id);
		this.name = name;
		this.mac = mac;
		this.status = status;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "model_id", nullable = false)
	public ApModelEntity getApModel() {
		return this.apModel;
	}

	public void setApModel(ApModelEntity apModel) {
		this.apModel = apModel;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "place_id", nullable = false)
	public PlaceEntity getPlace() {
		return this.place;
	}

	public void setPlace(PlaceEntity place) {
		this.place = place;
	}

	@Column(name = "code", nullable = false, length = 21)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "name", nullable = false, length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "mac", nullable = false, length = 17)
	public String getMac() {
		return this.mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	@Column(name = "position", length = 200)
	public String getPosition() {
		return this.position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	@Column(name = "status", nullable = false)
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name = "description", length = 500)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public void setModifyTime(Date modfiytime) {
		this.modifyTime = modfiytime;
	}

	@Column(name = "type")
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Column(name = "floor", length = 16)
	public String getFloor() {
		return floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}

	@Column(name = "longitude", length = 10)
	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	@Column(name = "latitude", length = 10)
	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getBdLng() {
		return bdLng;
	}

	public void setBdLng(String bdLng) {
		this.bdLng = bdLng;
	}

	public String getBdLat() {
		return bdLat;
	}

	public void setBdLat(String bdLat) {
		this.bdLat = bdLat;
	}

	@Column(name = "siteInfo", length = 128)
	public String getSiteInfo() {
		return siteInfo;
	}

	public void setSiteInfo(String siteInfo) {
		this.siteInfo = siteInfo;
	}

	@Column(name = "subwayLine", length = 256)
	public String getSubwayLine() {
		return subwayLine;
	}

	public void setSubwayLine(String subwayLine) {
		this.subwayLine = subwayLine;
	}

	@Column(name = "subwayVehicle", length = 256)
	public String getSubwayVehicle() {
		return subwayVehicle;
	}

	public void setSubwayVehicle(String subwayVehicle) {
		this.subwayVehicle = subwayVehicle;
	}

	@Column(name = "subwayCarriage", length = 256)
	public String getSubwayCarriage() {
		return subwayCarriage;
	}

	public void setSubwayCarriage(String subwayCarriage) {
		this.subwayCarriage = subwayCarriage;
	}

	@Column(name = "plateNumber", length = 64)
	public String getPlateNumber() {
		return plateNumber;
	}

	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}

	@Column(name = "ndpi", length = 32)
	public String getNdpi() {
		return ndpi;
	}

	public void setNdpi(String ndpi) {
		this.ndpi = ndpi;
	}

	@Column(name = "fwversion", length = 32)
	public String getFwversion() {
		return fwversion;
	}

	public void setFwversion(String fwversion) {
		this.fwversion = fwversion;
	}

	@Column(name = "validity", length = 19, columnDefinition = "DateTime default '1970-01-01 00:00:00'")
	public Date getValidity() {
		return validity;
	}

	public void setValidity(Date validity) {
		this.validity = validity;
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
	@JoinTable(name = "sys_group_ap", joinColumns = { @JoinColumn(name = "ap_id") }, inverseJoinColumns = {
			@JoinColumn(name = "group_id") })
	public Set<GroupEntity> getGroups() {
		return groups;
	}

	public void setGroups(Set<GroupEntity> groups) {
		this.groups = groups;
	}

	@Column(name = "terminalType")
	public Integer getTerminalType() {
		return terminalType;
	}

	public void setTerminalType(Integer terminalType) {
		this.terminalType = terminalType;
	}

	@Column(name = "uploadTimeInterval")
	public Integer getUploadTimeInterval() {
		return uploadTimeInterval;
	}

	public void setUploadTimeInterval(Integer uploadTimeInterval) {
		this.uploadTimeInterval = uploadTimeInterval;
	}

	@Column(name = "collectionRadius")
	public Integer getCollectionRadius() {
		return collectionRadius;
	}

	public void setCollectionRadius(Integer collectionRadius) {
		this.collectionRadius = collectionRadius;
	}

	@Column(name = "baseInfoUploadInterval")
	public Integer getBaseInfoUploadInterval() {
		return baseInfoUploadInterval;
	}

	public void setBaseInfoUploadInterval(Integer baseInfoUploadInterval) {
		this.baseInfoUploadInterval = baseInfoUploadInterval;
	}

	@Column(name = "approve", length = 11)
	public Integer getApprove() {
		return approve;
	}

	public void setApprove(Integer approve) {
		this.approve = approve;
	}

}