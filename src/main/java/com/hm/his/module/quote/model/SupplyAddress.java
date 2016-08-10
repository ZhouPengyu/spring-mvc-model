package com.hm.his.module.quote.model;

/**
 * @author ZhouPengyu
 * @company H.M
 * @date 2016-5-25 17:20:04
 * @description 提货地址
 * @version 1.0
 */
public class SupplyAddress {
	
    private Integer supplyAddressId;
    private String address;
    private String contacts;
    private String phoneNo;
    private Integer creater;
    private String createDate;
	private Integer hospitalId;
	private String postcode;

	public Integer getSupplyAddressId() {
		return supplyAddressId;
	}
	public void setSupplyAddressId(Integer supplyAddressId) {
		this.supplyAddressId = supplyAddressId;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getContacts() {
		return contacts;
	}
	public void setContacts(String contacts) {
		this.contacts = contacts;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public Integer getCreater() {
		return creater;
	}
	public void setCreater(Integer creater) {
		this.creater = creater;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public Integer getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(Integer hospitalId) {
		this.hospitalId = hospitalId;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
}