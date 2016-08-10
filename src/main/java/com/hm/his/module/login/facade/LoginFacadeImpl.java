package com.hm.his.module.login.facade;

import com.hm.his.framework.cache.redis.CacheCaller;
import com.hm.his.framework.cache.redis.JedisHelper;
import com.hm.his.module.login.model.SystemFrame;
import com.hm.his.module.login.service.LoginService;
import com.hm.his.module.user.model.Doctor;
import com.hm.his.module.user.model.Hospital;
import com.hm.his.module.user.service.DoctorService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016-08-10
 * Time: 14:28
 * CopyRight:HuiMei Engine
 */
@Component
public class LoginFacadeImpl implements LoginFacade {

    @Autowired(required = false)
    LoginService loginService;

    @Autowired(required = false)
    DoctorService doctorService;

    @Override
    public SystemFrame getHisSystemFrame(Doctor doctor,Hospital hospital) {
        String key= LoginCache.getFarmeByDoctorId(doctor.getDoctorId());
        int expires=LoginCache.getFarmeByDoctorIdExpires;
        return JedisHelper.get(SystemFrame.class, key, expires, new CacheCaller<SystemFrame>(){
            public SystemFrame call() {
                return loginService.getHisSystemFrame(doctor,hospital);
            }
        });
    }

    @Override
    public SystemFrame getSupplierSystemFrame(Doctor doctor) {
        String key= LoginCache.getFarmeByDoctorId(doctor.getDoctorId());
        int expires=LoginCache.getFarmeByDoctorIdExpires;
        return JedisHelper.get(SystemFrame.class, key, expires, new CacheCaller<SystemFrame>(){
            public SystemFrame call() {
                return loginService.getSupplierSystemFrame(doctor);
            }
        });
    }

    @Override
    public void deleteSystemFrameCache(Long doctorId) {
        String key = LoginCache.getFarmeByDoctorId(doctorId);
        if(JedisHelper.exists(key)) {
            JedisHelper.del(key);
        }
    }

    @Override
    public void deleteSystemFrameCacheByHospitalId(Long hospitalId) {
        Doctor tempDoct = new Doctor();
        tempDoct.setHospitalId(hospitalId);
        List<Doctor> doctorList = doctorService.searchDoctor(tempDoct);
        if(CollectionUtils.isNotEmpty(doctorList)){
            doctorList.forEach(doctor -> {
                this.deleteSystemFrameCache(doctor.getDoctorId());
            });
        }
    }

    @Override
    public void deleteAllSystemFrameCache() {
        JedisHelper.delKeysLike(LoginCache.GETFARMEBYDOCTORID_PREFIX);
    }

    public static void main(String[] args) {
        System.out.println(LoginCache.GETFARMEBYDOCTORID_PREFIX);
    }
}
