package com.hm.his.module.outpatient.model;

import com.hm.his.framework.utils.ExamSampleConstant;

/**
 * @author ZhouPengyu
 * @company H.M
 * @date 2016-3-4 11:28:35
 * @description 附加费用实体类
 * @version 3.0
 */
public class PatientExam {
	private Long patientExamId;
    private Long recordId;
    private Long patientId;
    private Long doctorId;
    private Long examId;
    private String examName;	//检查名称
    private String sampleId;
    private String sampleName;
    private String examResult;
    private String unit;
    private Long status;//'0:正常，1:删除'
    private String createDate;
    private Double price;	//价格
    private Double examCost;	//成本
    private Long dataSource;	//检查来源
    private Long total;
    
    private Integer isCharged;	//是否收费

	public Long getPatientExamId() {
		return patientExamId;
	}

	public void setPatientExamId(Long patientExamId) {
		this.patientExamId = patientExamId;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public Long getExamId() {
        return examId;
    }

    public void setExamId(Long examId) {
        this.examId = examId;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public String getSampleId() {
        return sampleId;
    }

    public void setSampleId(String sampleId) {
        this.sampleId = sampleId;
        this.setSampleName(ExamSampleConstant.getSampleName(sampleId));
    }

    public String getSampleName() {
        return sampleName;
    }

    public void setSampleName(String sampleName) {
        this.sampleName = sampleName;
    }

    public String getExamResult() {
        return examResult;
    }

    public void setExamResult(String examResult) {
        this.examResult = examResult;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getExamCost() {
		return examCost;
	}

	public void setExamCost(Double examCost) {
		this.examCost = examCost;
	}

	public Integer getIsCharged() {
		return isCharged;
	}

	public void setIsCharged(Integer isCharged) {
		this.isCharged = isCharged;
	}

	public Long getDataSource() {
		return dataSource;
	}

	public void setDataSource(Long dataSource) {
		this.dataSource = dataSource;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}
}
