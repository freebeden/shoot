package com.yysports.shoot.model;

import java.io.Serializable;

/**
 *
 * 与对应表进行映射
 * @author: zhouyou
 * @date: 2019年5月8日 下午10:58:42
 * @version: V1.0
 */
public class ShootBean implements Serializable {

    private static final long serialVersionUID = 1L;

    //用户主键
    private long id;
    //用户名
    private String username;
    //密码
    private String password;
    //用户java web token
    private String jwt;

    private long pid;//等级id 主键
    private String activityItemId;//鞋子列表编码
    private String itemName;
    private String itemModel;//鞋子款号
    private String registerNo;//登记号码
    private String isSize;
    private String shoesSize;//鞋子尺码
    private String activityShopId;//鞋店号码
    private String state;//状态 1:未抽奖，2：未中签，3：已中签
    private String shopName;//鞋店名称
    private long uid;//对应用户id
    private long minUid;
    private long maxUid;
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getMinUid() {
        return minUid;
    }

    public void setMinUid(long minUid) {
        this.minUid = minUid;
    }

    public long getMaxUid() {
        return maxUid;
    }

    public void setMaxUid(long maxUid) {
        this.maxUid = maxUid;
    }

    private String serialNo;//流水号
    private String createTime;//订单创建时间
    private String updateTime;//更新时间
    private String updateDate;//更新日期

    private String result;//返回请求的json字符串

    private String jwtStat;//1:正常 2:异常

    private String errStat;

    public String getErrStat() {
        return errStat;
    }

    public void setErrStat(String errStat) {
        this.errStat = errStat;
    }

    public String getJwtStat() {
        return jwtStat;
    }

    public void setJwtStat(String jwtStat) {
        this.jwtStat = jwtStat;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    private String uuid;//登记成功后返回的编码

    private int execStatus;//执行状态

    public int getExecStatus() {
        return execStatus;
    }

    public void setExecStatus(int execStatus) {
        this.execStatus = execStatus;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    private String sid;//登记记录表的id

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public String getActivityItemId() {
        return activityItemId;
    }

    public void setActivityItemId(String activityItemId) {
        this.activityItemId = activityItemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemModel() {
        return itemModel;
    }

    public void setItemModel(String itemModel) {
        this.itemModel = itemModel;
    }

    public String getRegisterNo() {
        return registerNo;
    }

    public void setRegisterNo(String registerNo) {
        this.registerNo = registerNo;
    }

    public String getIsSize() {
        return isSize;
    }

    public void setIsSize(String isSize) {
        this.isSize = isSize;
    }

    public String getShoesSize() {
        return shoesSize;
    }

    public void setShoesSize(String shoesSize) {
        this.shoesSize = shoesSize;
    }

    public String getActivityShopId() {
        return activityShopId;
    }

    public void setActivityShopId(String activityShopId) {
        this.activityShopId = activityShopId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }
}
