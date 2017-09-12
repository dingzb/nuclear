package cc.idiary.nuclear.entity.business.management;


import cc.idiary.nuclear.entity.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * ApModel entity.
 * 
 * @author Dzb
 */
//@Entity
//@Table(name = "bus_apmodel")
public class ApModelEntity extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 2748603396071090669L;

	private String code;
	private String name;
	private String cpu;
	private Integer ram;
	private Integer rom;
	private Set<FirmwareEntity> firmwares;
	private Set<ApEntity> aps;

	// Constructors

	/** default constructor */
	public ApModelEntity() {
	}

	/** minimal constructor */
	public ApModelEntity(String code) {
		this.code = code;
	}

	/** full constructor */
	public ApModelEntity(String code, String name, String cpu, Integer ram,
                         Integer rom, Set<FirmwareEntity> firmwares,
                         Set<ApEntity> aps) {
		this.code = code;
		this.name = name;
		this.cpu = cpu;
		this.ram = ram;
		this.rom = rom;
		this.firmwares = firmwares;
		this.aps = aps;
	}

	@Column(name = "code", unique = true, nullable = false, length = 50)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "name", length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "cpu", length = 20)
	public String getCpu() {
		return this.cpu;
	}

	public void setCpu(String cpu) {
		this.cpu = cpu;
	}

	@Column(name = "ram")
	public Integer getRam() {
		return this.ram;
	}

	public void setRam(Integer ram) {
		this.ram = ram;
	}

	@Column(name = "rom")
	public Integer getRom() {
		return this.rom;
	}

	public void setRom(Integer rom) {
		this.rom = rom;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "bus_firmware_apmodel", joinColumns = { @JoinColumn(name = "apmodel_id", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "firmware_id", nullable = false, updatable = false) })
	public Set<FirmwareEntity> getFirmwares() {
		return this.firmwares;
	}

	public void setFirmwares(Set<FirmwareEntity> firmwares) {
		this.firmwares = firmwares;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "apModel")
	public Set<ApEntity> getAps() {
		return this.aps;
	}

	public void setAps(Set<ApEntity> aps) {
		this.aps = aps;
	}

}