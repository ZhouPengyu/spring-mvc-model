package com.hm.his.module.login.service;

import com.hm.his.module.login.model.SystemFrame;
import com.hm.his.module.user.model.Doctor;
import com.hm.his.module.user.model.Hospital;

/**
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016-08-10
 * Time: 10:51
 * CopyRight:HuiMei Engine
 */
public interface LoginService {
    SystemFrame getHisSystemFrame(Doctor doctor,Hospital hospital);

    SystemFrame getSupplierSystemFrame(Doctor doctor);
}
