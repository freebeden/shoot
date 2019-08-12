<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <#import "/common/common.macro.ftl" as netCommon>
    <@netCommon.commonStyle />
</head>
<body>
    <div class="wrapper">
        <!-- Content Wrapper. Contains page content -->
        <div class="content-wrapper container">
            <!-- Content Header (Page header) -->
            <section class="content-header">
                <h1><small>任务调度</small></h1>
                <h2><small><a href="javascript:void(0)" class="" id="loginList">登记列表</a></small></h2>
            </section>

            <!-- Main content -->
            <section class="content">
                <div class="row" id="form">
                    <div class="col-xs-4">
                        <div class="input-group">
                		<span class="input-group-addon">
	                  		任务名称
	                	</span>
                            <input type="text" class="form-control" name="jobName" id="jobName" >
                        </div>
                    </div>
                    <div class="col-xs-4">
                        <div class="input-group">
                		<span class="input-group-addon">
	                  		任务组
	                	</span>
                            <input type="text" class="form-control" name="jobGroup" id="jobGroup">
                        </div>
                    </div>
                    <div class="col-xs-1">
                        <button class="btn btn-block btn-info" id="searchBtn">搜索</button>
                    </div>

                    <div class="col-xs-1">
                        <button class="btn btn-block btn-nomal" id="clearLog">清理</button>
                    </div>

                </div>

                <div class="row">
                    <div class="col-xs-12">
                        <div class="box">
                            <div class="box-header hide"><h3 class="box-title">调度日志</h3></div>
                            <div class="box-body">
                                <table id="joblog_list" class="table table-bordered table-striped display" width="100%" >
                                    <thead>
                                    <tr>
                                        <th name="uuid" >uuid</th>
                                        <th name="jobName" >任务名</th>
                                        <th name="jobGroup" >任务组</th>
                                        <th name="jobStatus" >任务状态</th>
                                        <th name="cronExpression" >cron表达式</th>
                                        <th name="description" >任务描述</th>
                                        <th name="createTime" >创建时间</th>
                                        <th name="updateTime" >更新时间</th>
                                        <th name="handleMsg" >操作</th>
                                    </tr>
                                    </thead>
                                    <tbody></tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
        </div>

        <!-- footer -->
        <@netCommon.commonFooter />
        <@netCommon.commonScript />
    </div>
    <script src="${request.contextPath}/static/js/job.js"></script>

</body>
</html>