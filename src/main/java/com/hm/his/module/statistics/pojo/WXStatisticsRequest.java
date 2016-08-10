package com.hm.his.module.statistics.pojo;

import com.hm.his.framework.utils.LangUtils;
import com.hm.his.framework.utils.SessionUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * @param
 * @author SuShaohua
 * @date 2016/4/29
 * @description
 */
public class WXStatisticsRequest extends StatisticsRequest {
    Integer section;

    public Integer getSection() {
        return section;
    }

    public void setSection(Integer section) {
        this.section = section;
    }

    public static void handleStatisticsDate(WXStatisticsRequest req){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        String endDate = sdf.format(calendar.getTime());
        String startDate = "";
        Integer section = LangUtils.getInteger(req.getSection() == null ? 1 : req.getSection());
        if (section == 2){
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            startDate = sdf.format(calendar.getTime());
        }else if (section == 3){
            calendar.set(Calendar.DATE, 1);
            startDate = sdf.format(calendar.getTime());
        }else
            startDate = sdf.format(calendar.getTime());

        req.setStartDate(startDate);
        req.setEndDate(endDate);
    }
}
