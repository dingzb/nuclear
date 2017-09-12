package cc.idiary.nuclear.entity.business.tax;

import cc.idiary.nuclear.entity.BaseEntity;

import javax.persistence.*;

/**
 * Created by Neo on 2017/5/31.
 */
@Entity
@Table(name = "bus_tax_bus_attachment")
public class BusAttachmentEntity extends BaseEntity {
    private BusinessEntity business;
    private String fileName;
    private Long size;
    private String url;
    private Integer sort;   //暂时没啥用

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bus_id")
    public BusinessEntity getBusiness() {
        return business;
    }

    public void setBusiness(BusinessEntity business) {
        this.business = business;
    }

    @Column(name = "file_name", length = 255)
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Column(name = "size")
    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    @Column(name = "url", length = 255)
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Column(name = "sort")
    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
