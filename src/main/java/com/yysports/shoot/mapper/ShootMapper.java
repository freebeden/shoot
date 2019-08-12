package com.yysports.shoot.mapper;

import com.yysports.shoot.model.JobTask;
import com.yysports.shoot.model.ShootBean;

import java.util.List;
import java.util.Map;

public interface ShootMapper {

    ShootBean loginByAccount(int id);

    /**
     * 查询用户列表
     * @return
     */
    List<ShootBean> queryUserList();

    List<JobTask> queryJobTaskList(Map<String, Object> map);

    int updateTaskStatus(Map<String, Object> map);

    JobTask queryJobTask(Map<String, Object> map);

    int updateUserById(ShootBean shootBean);

    void batchInsert(List<ShootBean> list);

    List<ShootBean> queryLoginShoot(Map<String,Object> map);

    int updateShootInfo(ShootBean shootBean);
    int updateRecordStat(ShootBean shootBean);

    int updateUserInfo(ShootBean shootBean);

    int addRecordShootBean(ShootBean shootBean);

    List<ShootBean> queryResultBean();

    int updateRecordList(ShootBean shootBean);

    int addRecordList(ShootBean shootBean);

    ShootBean queryShootBeanById(long id);

    int updateRecordUUid(ShootBean shootBean);

    List<ShootBean> queryShootLoginBean(Map<String,Object> map);

    List<ShootBean> getShootBeanList(ShootBean bean);

    void updateUser(Map<String,Object> map);
    List<ShootBean> queryAllUsers(Map<String,Object> map);
    int updateUserState(ShootBean shootBean);

    String queryThreadCount();

    int addNotReq(ShootBean shootBean);

    int updateNotReq(ShootBean shootBean);
}
