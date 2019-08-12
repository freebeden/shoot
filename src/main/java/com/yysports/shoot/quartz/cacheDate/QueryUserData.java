package com.yysports.shoot.quartz.cacheDate;

import com.yysports.shoot.model.ShootBean;
import com.yysports.shoot.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class QueryUserData {
    Logger LOGGER = LoggerFactory.getLogger(QueryUserData.class);
    private List<ShootBean> userList;

    public List<ShootBean> getUserList() {
        return userList;
    }

    public synchronized void setUserList(List<ShootBean> userList) {
        this.userList = userList;
    }
    private QueryUserData() {
        LOGGER.warn("单例模式下该查询用户被实例化时间是:" + DateUtils
                .formatDate(DateUtils.getCurrentDate(), DateUtils.DEFAULT_DATETIME_FORMAT1));
    }
    private static class SingleTon {
        private static final QueryUserData queryUserData = new QueryUserData();
    }

    public static QueryUserData getInstall() {
        return SingleTon.queryUserData;
    }

}
