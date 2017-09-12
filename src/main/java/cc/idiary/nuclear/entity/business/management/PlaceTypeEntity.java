package cc.idiary.nuclear.entity.business.management;

import cc.idiary.nuclear.entity.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * PlaceType entity. <br>
 * <p>
 * 规范参考：《公共场所无线上网安全管理系统无线上网接入安全技术要求》附录【非经营性上网服务场所类型代码表】
 * </p>
 * 
 * @author Dzb
 */
//@Entity
//@Table(name = "bus_placetype")
public class PlaceTypeEntity extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -5693750058686337712L;

	private String name;// 场所名称
	private String code; // 场所编码 1
	private String description;// 场所描述（备注）
	private Date createTime;
	private Date modifyTime;
	private Set<PlaceEntity> places;

	// Constructors

	/** default constructor */
	public PlaceTypeEntity() {
	}

	/** minimal constructor */
	public PlaceTypeEntity(String id, String name, Date createTime,
			Date modifyTime) {
		setId(id);
		this.name = name;
		this.createTime = createTime;
		this.modifyTime = modifyTime;
	}

	/** full constructor */
	public PlaceTypeEntity(String id, String name, String description,
						   Date createTime, Date modifyTime, Set<PlaceEntity> places) {
		setId(id);
		this.name = name;
		this.description = description;
		this.createTime = createTime;
		this.modifyTime = modifyTime;
		this.places = places;
	}

	@Column(name = "name", nullable = false, length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "description", length = 500)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "placeType")
	public Set<PlaceEntity> getPlaces() {
		return this.places;
	}

	public void setPlaces(Set<PlaceEntity> places) {
		this.places = places;
	}

	@Column(name = "code", nullable = false, length = 1)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}