package com.yysports.shoot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 *
 * 该配置类是对固定参数设置
 * @author: zhouyou
 * @date: 2019年5月9日 下午10:58:42
 * @version: V1.0
 */
@Component
@ConfigurationProperties(prefix = "yysoprt")
public class RequestConfigInfo {

    //获取登记记录url
    private String alreadyUrl;

    //查询可登记和添加url
    private String queryOrAddUrl;

    private String threadCount;

    private String isUseAgency;

    private String tokenUrl;

    public String getTokenUrl() {
        return tokenUrl;
    }

    public void setTokenUrl(String tokenUrl) {
        this.tokenUrl = tokenUrl;
    }

    public String getIsUseAgency() {
        return isUseAgency;
    }

    public void setIsUseAgency(String isUseAgency) {
        this.isUseAgency = isUseAgency;
    }

    public String getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(String threadCount) {
        this.threadCount = threadCount;
    }

    public String getAlreadyUrl() {
        return alreadyUrl;
    }

    public void setAlreadyUrl(String alreadyUrl) {
        this.alreadyUrl = alreadyUrl;
    }

    public String getQueryOrAddUrl() {
        return queryOrAddUrl;
    }

    public void setQueryOrAddUrl(String queryOrAddUrl) {
        this.queryOrAddUrl = queryOrAddUrl;
    }
}
