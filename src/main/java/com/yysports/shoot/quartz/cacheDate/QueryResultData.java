package com.yysports.shoot.quartz.cacheDate;

import com.yysports.shoot.model.ShootBean;
import com.yysports.shoot.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class QueryResultData {

    Logger LOGGER = LoggerFactory.getLogger(QueryResultData.class);

    private List<ShootBean> shootBeansList = null;

    private Map<String,List<ShootBean>> shootMap = null;

    public Map<String, List<ShootBean>> getShootMap() {
        return shootMap;
    }

    public synchronized void  setShootMap(Map<String, List<ShootBean>> shootMap) {
        this.shootMap = shootMap;
    }

    private QueryResultData() {
        LOGGER.warn("单例模式下该查询同步被实例化时间是:" + DateUtils
                .formatDate(DateUtils.getCurrentDate(), DateUtils.DEFAULT_DATETIME_FORMAT1));
    }

    private static class SingleTonHandler {
        private static final QueryResultData instance = new QueryResultData();
    }
    public static QueryResultData getInstance(){
        return SingleTonHandler.instance;
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
