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
                <h1><small>登记详情</small></h1>
                <h2><small><a href="javascript:void(0)" class="" id="taskJob">任务调度</a></small></h2>
            </section>

            <!-- Main content -->
            <section class="content">
                <div class="row" id="form">
                    <div class="row">
                        <div class="col-xs-3">
                            <div class="input-group">
                		<span class="input-group-addon">
	                  		用户名
	                	</span>
                                <input type="text" class="form-control" name="username" id="username" >
                            </div>
                        </div>
                        <div class="col-xs-3">
                            <div class="input-group">
                		<span class="input-group-addon">
	                  		鞋子款号
	                	</span>
                                <input type="text" class="form-control" name="itemModel" id="itemModel">
                            </div>
                        </div>
                        <div class="col-xs-3">
                            <div class="input-group">
                            <span class="input-group-addon">
                                状态
                            </span>
                                <select class="form-control glueType bootstrap-select" name="state" id="state">
                                    <option value="9">未同步</option>
                                    <option value="1">未抽签</option>
                                    <option value="2">未中签</option>
                                    <option value="3" selected="selected">已中签</option>
                                </select>
                            </div>
                        </div><div class="col-xs-3">
                            <div class="input-group">
                            <span class="input-group-addon">
                                更新日期
                            </span>
                                <input type="text" name="updateDate" placeholder="填写时间" />
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-1">
                            <button class="btn btn-block btn-info" id="searchBtn">搜索</button>
                        </div>

                        <div class="col-xs-1">
                            <button class="btn btn-block btn-nomal" id="clearLog">清理</button>
                        </div>

                        <div class="col-xs-1">
                            <button class="btn btn-block btn-success" onclick="fileInput.click()">导入</button>
                            <input id="fileInput" style="display: none;" type="file" onchange="importFile(this)"/>
                        </div>

                       <#-- <div class="col-xs-1">
                            <button class="btn btn-block btn-danger" id="queryResult">同步</button>
                        </div>-->
                    </div>
                </div>

                <div class="row">
                    <div class="col-xs-12">
                        <div class="box">
                            <div class="box-header hide"><h3 class="box-title">调度日志</h3></div>
                            <div class="box-body">
                                <table id="shoot_list" class="table table-bordered table-striped display" width="100%" >
                                    <thead>
                                    <tr>
                                        <th name="uid" >uid</th>
                                        <th name="username" >用户名</th>
                                        <th name="itemName" >鞋子名称</th>
                                        <th name="itemModel" >鞋子款号</th>
                                        <th name="registerNo" >登记号码</th>
                                        <th name="state" >状态</th>
                                        <th name="shopName" >店铺名称</th>
                                        <th name="createTime" >创建时间</th>
                                        <th name="updateTime" >更新时间</th>
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
    <script src="${request.contextPath}/static/js/loginList.js"></script>

</body>
</html>