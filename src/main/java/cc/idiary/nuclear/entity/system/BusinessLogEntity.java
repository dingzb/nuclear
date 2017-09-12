package cc.idiary.nuclear.entity.system;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import cc.idiary.nuclear.entity.BaseEntity;
import cc.idiary.nuclear.logger.LogOperationType;

@Entity
@Table(name = "sys_buslog")
public class BusinessLogEntity extends BaseEntity {
	private String username;
	private Date createTime;
	private String ipAddr;
	private String userAgent;
	private String content;
	private String signature;// 方法
	private String args;
	private LogOperationType operation;
	private Boolean isException;
	private String exception;
	private Boolean delFlag; //为true表示逻辑删除

	@Column(name = "username", length = 50)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "create_time")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	@Column(name = "content", length = 500)
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "operation", length = 6)
	public LogOperationType getOperation() {
		return operation;
	}

	public void setOperation(LogOperationType operation) {
		this.operation = operation;
	}

	@Column(name = "args", length=5000)
	public String getArgs() {
		return args;
	}

	public void setArgs(String args) {
		this.args = args;
	}

	@Column(name = "ipaddr")
	public String getIpAddr() {
		return ipAddr;
	}

	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}

	@Column(name = "useragent", length = 500)
	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	@Column(name = "signature", length = 500)
	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	@Column(name = "isexception")
	public Boolean getIsException() {
		return isException;
	}

	public void setIsException(Boolean isException) {
		this.isException = isException;
	}

	@Column(name = "exception", length = 500)
	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}
	
	@Column(name = "delflag", columnDefinition="Boolean default false")
	public Boolean getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Boolean delFlag) {
		this.delFlag = delFlag;
	}

	@Override
	public String toString() {
		return "BusinessLogEntity [username=" + username + ", createTime="
				+ createTime + ", ipAddr=" + ipAddr + ", userAgent="
				+ userAgent + ", content=" + content + ", signature="
				+ signature + ", args=" + args + ", operation=" + operation
				+ ", isException=" + isException + ", exception=" + exception
				+ "]";
	}
}
