package cc.idiary.nuclear.model.system;

import java.util.Date;
import java.util.List;

public class AuthenticateModel {

	private String apId;
	private String sn;
	private String mac;
	private String type;
	private String account;
	private String apmac;
	private Date authtime;
	private Boolean status;
	private String verificationCode;
	private UserModel user;
	private List<String> permCodes;
	private List<String> permStates;
	private String cmdkey;
	private String clientmac;

	public UserModel getUser() {
		return user;
	}

	public void setUser(UserModel user) {
		this.user = user;
	}

	public List<String> getPermCodes() {
		return permCodes;
	}

	public void setPermCodes(List<String> permCodes) {
		this.permCodes = permCodes;
	}

	public List<String> getPermStates() {
		return permStates;
	}

	public void setPermStates(List<String> permStates) {
		this.permStates = permStates;
	}

	public String getApId() {
		return apId;
	}

	public void setApId(String apId) {
		this.apId = apId;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public Date getAuthtime() {
		return authtime;
	}

	public void setAuthtime(Date authtime) {
		this.authtime = authtime;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getApmac() {
		return apmac;
	}

	public void setApmac(String apmac) {
		this.apmac = apmac;
	}

	public String getCmdkey() {
		return cmdkey;
	}

	public void setCmdkey(String cmdkey) {
		this.cmdkey = cmdkey;
	}

	public String getClientmac() {
		return clientmac;
	}

	public void setClientmac(String clientmac) {
		this.clientmac = clientmac;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "AuthenticateModel [" + super.toString() + "apId=" + apId
				+ ", sn=" + sn + ", mac=" + mac + ", account=" + account
				+ ", apmac=" + apmac + ", authtime=" + authtime + ", status="
				+ status + ", verificationCode=" + verificationCode + ", user="
				+ user + ", permCodes=" + permCodes + ", permStates="
				+ permStates + ", cmdkey=" + cmdkey + ", clientmac="
				+ clientmac + "]";
	}
}
