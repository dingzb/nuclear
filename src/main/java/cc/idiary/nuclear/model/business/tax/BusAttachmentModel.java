package cc.idiary.nuclear.model.business.tax;


import cc.idiary.nuclear.model.BaseModel;

/**
 * Created by Neo on 2017/6/1.
 */
public class BusAttachmentModel extends BaseModel {
    private String busId;
    private String fileName;
    private Long size;
    private String url;
    private Integer sort;

    public String getBusId() {
        return busId;
    }

    public void setBusId(String busId) {
        this.busId = busId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
