package cc.idiary.nuclear.entity.business.management;

import cc.idiary.nuclear.entity.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * Firmware entity.
 *
 * @author Dzb
 */
//@Entity
//@Table(name = "bus_firmware")
public class FirmwareEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 8714501726724328800L;

    private String name;
    private String type;
    private String version;
    private String path;
    private String md5;
    private String userName;
    private String passWord;
    private String description;
    private Date createTime;
    private Date modifyTime;
    private Set<ApModelEntity> apModels;

    // Constructors

    /**
     * default constructor
     */
    public FirmwareEntity() {
    }

    /**
     * minimal constructor
     */
    public FirmwareEntity(String id, String name, String path) {
        setId(id);
        this.path = path;
    }

    /**
     * full constructor
     */
    public FirmwareEntity(String id, String name, String type, String path, String description,
                          Set<ApModelEntity> apModels) {
        setId(id);
        this.type = type;
        this.path = path;
        this.description = description;
        this.apModels = apModels;
    }

    @Column(name = "name", nullable = false, length = 20)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "type", nullable = false, length = 20)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(name = "version", nullable = false, length = 20)
    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Column(name = "path", nullable = false, length = 200)
    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Column(name = "md5", nullable = false, length = 50)
    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    @Column(name = "description", length = 500)
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "username", nullable = false, length = 50)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Column(name = "password", nullable = false, length = 50)
    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    @Column(name = "create_time", nullable = false, length = 19)
    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "modify_time", nullable = false, length = 19)
    public Date getModifyTime() {
        return this.modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "bus_firmware_apmodel", joinColumns = {@JoinColumn(name = "firmware_id", nullable = false, updatable = false)}, inverseJoinColumns = {@JoinColumn(name = "apmodel_id", nullable = false, updatable = false)})
    public Set<ApModelEntity> getApModels() {
        return this.apModels;
    }

    public void setApModels(Set<ApModelEntity> apModels) {
        this.apModels = apModels;
    }

}