package com.hm.his.module.user.model;

/**
 * @author ZhouPengyu
 * @company H.M
 * @date 2016-3-23 12:03:22
 * @description 诊所实体类
 * @version 3.0
 */
public class Hospital {
	private Long hospitalId; // 诊所ID
	private String hospitalName; // 诊所名称
	private String hospitalNumber;	//诊所编号
	private String organizationLicense; // 诊所执照
	private String address;
	private String createDate;
	private String invitationCode;	//邀请码

	public Long getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(Long hospitalId) {
		this.hospitalId = hospitalId;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public String getHospitalNumber() {
		return hospitalNumber;
	}

	public void setHospitalNumber(String hospitalNumber) {
		this.hospitalNumber = hospitalNumber;
	}

	public String getOrganizationLicense() {
		return organizationLicense;
	}

	public void setOrganizationLicense(String organizationLicense) {
		this.organizationLicense = organizationLicense;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getInvitationCode() {
		return invitationCode;
	}

	public void setInvitationCode(String invitationCode) {
		this.invitationCode = invitationCode;
	}

}
