package com.hm.his.module.login.facade;

import com.hm.his.module.login.model.SystemFrame;
import com.hm.his.module.user.model.Doctor;
import com.hm.his.module.user.model.Hospital;

/**
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016-08-10
 * Time: 14:28
 * CopyRight:HuiMei Engine
 */
public interface LoginFacade {
    SystemFrame getHisSystemFrame(Doctor doctor,Hospital hospital);

    SystemFrame getSupplierSystemFrame(Doctor doctor);

    void deleteSystemFrameCache(Long doctorId);

    void deleteSystemFrameCacheByHospitalId(Long hospitalId);

    void deleteAllSystemFrameCache();
}
