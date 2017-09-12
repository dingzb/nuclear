package cc.idiary.nuclear.entity.business.management;


import cc.idiary.nuclear.entity.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

//@Entity
//@Table(name = "bus_vendor")
public class VendorEntity extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 290040982039123174L;

	private String name;// 厂商名称 70
	private String code;// 组织机构代码 9
	private String address;// 厂商地址 256
	private String contacts;// 厂商联系人 128
	private String contactsPhone;// 电话号码 128
	private String contactsMail;// 电子邮件地址 32
	private Set<PlaceEntity> places;

	@Column(name = "name", length = 70)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "code", length = 9)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "address", length = 256)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "contacts", length = 128)
	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	@Column(name = "phone", length = 128)
	public String getContactsPhone() {
		return contactsPhone;
	}

	public void setContactsPhone(String contactsPhone) {
		this.contactsPhone = contactsPhone;
	}

	@Column(name = "email", length = 32)
	public String getContactsMail() {
		return contactsMail;
	}

	public void setContactsMail(String contactsMail) {
		this.contactsMail = contactsMail;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "vendor")
	public Set<PlaceEntity> getPlaces() {
		return places;
	}

	public void setPlaces(Set<PlaceEntity> places) {
		this.places = places;
	}
}
