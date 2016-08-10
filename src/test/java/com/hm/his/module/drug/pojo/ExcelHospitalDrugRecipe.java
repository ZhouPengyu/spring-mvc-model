package com.hm.his.module.drug.pojo;

import com.google.common.collect.Lists;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.*;
import jxl.write.biff.RowsExceededException;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016-08-08
 * Time: 15:46
 * CopyRight:HuiMei Engine
 */
public class ExcelHospitalDrugRecipe {

    private String id;
    private String record_id;
    private String prescription;
    private String drug_id;
    private String drug_common_id;
    private String  common_name;
    private String drug_name;
    private String  approval_number;
    private String  bar_code;
    private String doctor_id;
    private String doctor_name;
    private String  patient_id;
    private String patient_name;
    private String data_source;

    public static List<ExcelHospitalDrugRecipe> parseExcel() throws IOException, BiffException {

        String examinationItemPath = "C:\\Users\\tangww\\Desktop\\temp\\hospital_drug_for_xiaochao.xls";

        Workbook rwb = Workbook.getWorkbook(new File(examinationItemPath));

        Sheet sheet = rwb.getSheet(0);
        System.out.println("xls==========行数：" + sheet.getRows() + "  列数" + sheet.getColumns());// 得到一共多少行多少列

        List<ExcelHospitalDrugRecipe> drugTrades = Lists.newArrayList();
        //跳过 前4行
        for (int i = 4; i < sheet.getRows(); i++) {
            ExcelHospitalDrugRecipe pojo = new ExcelHospitalDrugRecipe();
            pojo.setId(sheet.getCell(0, i).getContents());
            pojo.setRecord_id(sheet.getCell(1, i).getContents());
            pojo.setPrescription(sheet.getCell(2, i).getContents());
            pojo.setDrug_id(sheet.getCell(3, i).getContents());
            pojo.setDrug_name(sheet.getCell(6, i).getContents());
            pojo.setApproval_number(sheet.getCell(7, i).getContents());
            pojo.setBar_code(sheet.getCell(8, i).getContents());
            pojo.setDoctor_id(sheet.getCell(9, i).getContents());
            pojo.setDoctor_name(sheet.getCell(10, i).getContents());
            pojo.setPatient_id(sheet.getCell(11, i).getContents());
            pojo.setPatient_name(sheet.getCell(12, i).getContents());
            pojo.setData_source(sheet.getCell(13, i).getContents());
            drugTrades.add(pojo);
        }
        return drugTrades;
    }



    public static void matchResultToExcel(List<ExcelHospitalDrugRecipe> zzExaminationPojos ){
        WritableWorkbook wwb = null;
        String resultFile = "C:\\Users\\tangww\\Desktop\\temp\\处方开药明细-诊所药品.xls";
        try {

            wwb = Workbook.createWorkbook(new File(resultFile));
            WritableSheet ws = wwb.createSheet("处方开药明细-诊所药品", 0);
            String[] heads = new String[] { "id","record_id","prescription","drug_id","drug_common_id","common_name","drug_name","approval_number","bar_code","doctor_id","doctor_name","patient_id","patient_name","data_source" };
            for (int i = 0; i < heads.length; i++) {
                WritableCell cell = new Label(i, 0, heads[i]);
                WritableFont font1 = new WritableFont(WritableFont.TIMES, 12,
                        WritableFont.BOLD);
                WritableCellFormat format1 = new WritableCellFormat(font1);
                cell.setCellFormat(format1);
                ws.addCell(cell);
            }


            if (zzExaminationPojos != null && zzExaminationPojos.size() > 0) {
                for (int i = 0; i < zzExaminationPojos.size(); i++) {
                    ExcelHospitalDrugRecipe examPojo = zzExaminationPojos.get(i);
                    insertCell(ws, null, i, 0, examPojo.getId());
                    insertCell(ws, null, i, 1, examPojo.getRecord_id());
                    insertCell(ws, null, i, 2, examPojo.getPrescription());
                    insertCell(ws, null, i, 3, examPojo.getDrug_id());
                    insertCell(ws, null, i, 4, examPojo.getDrug_common_id());
                    insertCell(ws, null, i, 5, examPojo.getCommon_name());
                    insertCell(ws, null, i, 6, examPojo.getDrug_name());
                    insertCell(ws, null, i, 7, examPojo.getApproval_number());
                    insertCell(ws, null, i, 8, examPojo.getBar_code());
                    insertCell(ws, null, i, 9, examPojo.getDoctor_id());
                    insertCell(ws, null, i, 10, examPojo.getDoctor_name());//
                    insertCell(ws, null, i, 11, examPojo.getPatient_id());//
                    insertCell(ws, null, i, 12, examPojo.getPatient_name());//
                    insertCell(ws, null, i, 13, examPojo.getData_source());//
                }
            }

            wwb.write();
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (wwb != null) {
                try {
                    wwb.close();
                } catch (WriteException e) {
                    e.printStackTrace();

                } catch (IOException e) {
                    e.printStackTrace();

                }
            }
        }
    }


    private static void insertCell(WritableSheet ws, WritableCellFormat format1,
                                   int row, int column, String name) throws WriteException,
            RowsExceededException {
        WritableCell cell = new Label(column, row + 1, name);
        if (format1 != null) {
            cell.setCellFormat(format1);
        }
        ws.addCell(cell);
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRecord_id() {
        return record_id;
    }

    public void setRecord_id(String record_id) {
        this.record_id = record_id;
    }

    public String getPrescription() {
        return prescription;
    }

    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }

    public String getDrug_id() {
        return drug_id;
    }

    public void setDrug_id(String drug_id) {
        this.drug_id = drug_id;
    }

    public String getDrug_common_id() {
        return drug_common_id;
    }

    public void setDrug_common_id(String drug_common_id) {
        this.drug_common_id = drug_common_id;
    }

    public String getCommon_name() {
        return common_name;
    }

    public void setCommon_name(String common_name) {
        this.common_name = common_name;
    }

    public String getDrug_name() {
        return drug_name;
    }

    public void setDrug_name(String drug_name) {
        this.drug_name = drug_name;
    }

    public String getApproval_number() {
        return approval_number;
    }

    public void setApproval_number(String approval_number) {
        this.approval_number = approval_number;
    }

    public String getBar_code() {
        return bar_code;
    }

    public void setBar_code(String bar_code) {
        this.bar_code = bar_code;
    }

    public String getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }

    public String getDoctor_name() {
        return doctor_name;
    }

    public void setDoctor_name(String doctor_name) {
        this.doctor_name = doctor_name;
    }

    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }

    public String getPatient_name() {
        return patient_name;
    }

    public void setPatient_name(String patient_name) {
        this.patient_name = patient_name;
    }

    public String getData_source() {
        return data_source;
    }

    public void setData_source(String data_source) {
        this.data_source = data_source;
    }


}
