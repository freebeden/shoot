package com.yysports.shoot.quartz.cacheDate;

import com.yysports.shoot.model.ShootBean;
import com.yysports.shoot.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 单例模式获取登记列表
 */
public class PayShareData {
    Logger LOGGER = LoggerFactory.getLogger(PayShareData.class);

    private List<ShootBean> shootBeansList = null;

    private PayShareData() {
        LOGGER.warn("单例模式下该对象被实例化时间是:" + DateUtils
                .formatDate(DateUtils.getCurrentDate(), DateUtils.DEFAULT_DATETIME_FORMAT1));
    }

    /**
     * 静态内部类
     */
    private static class SingletonHolder {
        private final static PayShareData instance = new PayShareData();
    }

    /**
     * @Author:john
     * @Description: 获得单例实例
     * @Date:13:50 2019/2/26
     */
    public static PayShareData getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * @Author:john
     * @Description: 获取缓存列表
     * @Date:13:51 2019/2/26
     */
    public List<ShootBean> getShareList() {
        return shootBeansList;
    }
    /**
     * @Author:john
     * @Description: 给列表赋值
     * @params:shareList
     * @Date:13:51 2019/2/26
     */
    public synchronized void setShareList(List<ShootBean> shareList) {
        this.shootBeansList = shareList;
    }
}
