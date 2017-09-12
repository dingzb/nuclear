package cc.idiary.nuclear.model.system;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import cc.idiary.nuclear.model.BaseModel;

/**
 * 组模型
 * @author Dzb
 *
 */
public class GroupModel extends BaseModel {
	
	private String name;
	private String code;
	private String description;
	private String type;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm", locale = "zh", timezone = "GMT+8")
	private Date createTime;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm", locale = "zh", timezone = "GMT+8")
	private Date modifyTime;

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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return "GroupModel [name=" + name + ", code=" + code + ", description="
				+ description + ", createTime=" + createTime + ", modifyTime="
				+ modifyTime + "]";
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
