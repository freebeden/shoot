$(function() {
	// init date tables
	var logTable = $("#joblog_list").dataTable({
		"deferRender": true,
		"processing" : true,
	    "serverSide": true,
		"ajax": {
	        url: base_url + "/joblog/pageList" ,
	        data : function ( d ) {
	        	var obj = {};
	        	obj.jobGroup = $('#jobGroup').val();
	        	obj.jobName = $('#jobName').val();
	        	obj.start = d.start;
	        	obj.length = d.length;
                return obj;
            }
	    },
	    "searching": false,
	    "ordering": false,
	    "columns": [
	                { "data": 'uuid', "bSortable": false, "visible" : false},
					{ "data": 'jobName', "visible" : true},
	                { "data": 'jobGroup', "visible" : true},
	                { "data": 'jobStatus', "visible" : true},
	                { "data": 'cronExpression', "visible" : true},
	                { "data": 'description', "visible" : true},
	                { "data": 'createTime', "visible" : true},
	                { "data": 'updateTime', "visible" : true},
	                {
						"data": 'handleMsg' ,
						"bSortable": false,
						"width": "8%" ,
	                	"render": function ( data, type, row ) {
							var jobName = row.jobName;
							var status = row.jobStatus;
							var uuid = row.uuid;
							var jobGroup = row.jobGroup;
							var temp = '';
							return function () {
		                		if (status == 'PAUSED' || status == 'NONE'){
		                			temp = '<button class="btn btn-warning btn-xs" status="' + status + '" jobName="' + jobName + '" jobGroup="' + jobGroup + '" jobStatus="RESUME" uuid="' + uuid + '" onclick="changeState(this)">启动</button>';
		                		} else if (status == 'RESUME'|| status == 'NORMAL'){
									temp = '<button  class="btn btn-danger btn-xs" status="' + status + '" jobName="' + jobName + '" jobGroup="' + jobGroup + '" jobStatus="PAUSED" uuid="' + uuid + '" onclick="changeState(this)">停用</button>';
								}
								return temp;
	                		}
	                	}
	                }
	            ],
		"language" : {
			"sProcessing" : "处理中...",
			"sLengthMenu" : "每页 _MENU_ 条记录",
			"sZeroRecords" : "没有匹配结果",
			"sInfo" : "第 _PAGE_ 页 ( 总共 _PAGES_ 页，_TOTAL_ 条记录 )",
			"sInfoEmpty" : "无记录",
			"sInfoFiltered" : "(由 _MAX_ 项结果过滤)",
			"sInfoPostFix" : "",
			"sSearch" : "搜索:",
			"sUrl" : "",
			"sEmptyTable" : "表中数据为空",
			"sLoadingRecords" : "载入中...",
			"sInfoThousands" : ",",
			"oPaginate" : {
				"sFirst" : "首页",
				"sPrevious" : "上页",
				"sNext" : "下页",
				"sLast" : "末页"
			},
			"oAria" : {
				"sSortAscending" : ": 以升序排列此列",
				"sSortDescending" : ": 以降序排列此列"
			}
		}
	});


	
	// 日志弹框提示
	$('#joblog_list').on('click', '.logTips', function(){
		var msg = $(this).find('span').html();
		ComAlertTec.show(msg);
	});
	
	// 搜索按钮
	$('#searchBtn').on('click', function(){
		logTable.fnDraw();
	});
	
	// 查看执行器详细执行日志
	$('#joblog_list').on('click', '.logDetail', function(){
		var _id = $(this).attr('_id');
		
		window.open(base_url + '/joblog/logDetailPage?id=' + _id);
		return;
	});

	/**
	 * 终止任务
	 */
	$('#joblog_list').on('click', '.logKill', function(){
		var _id = $(this).attr('_id');

        layer.confirm('确认主动终止任务?', {icon: 3, title:'系统提示'}, function(index){
            layer.close(index);

            $.ajax({
                type : 'POST',
                url : base_url + '/joblog/logKill',
                data : {"id":_id},
                dataType : "json",
                success : function(data){
                    if (data.code == 200) {
                        layer.open({
                            title: '系统提示',
                            content: '操作成功',
                            icon: '1',
                            end: function(layero, index){
                                logTable.fnDraw();
                            }
                        });
                    } else {
                        layer.open({
                            title: '系统提示',
                            content: (data.msg || "操作失败"),
                            icon: '2'
                        });
                    }
                },
            });
        });

	});

	/**
	 * 清理任务Log
	 */
	$('#clearLog').on('click', function(){

		$("#form").find("input[type='text']").val('');

		$('#clearLogModal').modal('show');

	});
	$("#clearLogModal .ok").on('click', function(){
		$.post(base_url + "/joblog/clearLog",  $("#clearLogModal .form").serialize(), function(data, status) {
			if (data.code == "200") {
				$('#clearLogModal').modal('hide');
				layer.open({
					title: '系统提示',
					content: '日志清理成功',
					icon: '1',
					end: function(layero, index){
						logTable.fnDraw();
					}
				});
			} else {
				layer.open({
					title: '系统提示',
					content: (data.msg || "日志清理失败"),
					icon: '2'
				});
			}
		});
	});
	$("#clearLogModal").on('hide.bs.modal', function () {
		$("#clearLogModal .form")[0].reset();
	});



	$("#loginList").on('click',function () {
		var url = base_url + "/loginList"
		location.href = url;
	})

});


// 提示-科技主题
var ComAlertTec = {
	html:function(){
		var html =
			'<div class="modal fade" id="ComAlertTec" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">' +
			'<div class="modal-dialog">' +
			'<div class="modal-content-tec">' +
			'<div class="modal-body"><div class="alert" style="color:#fff;"></div></div>' +
			'<div class="modal-footer">' +
			'<div class="text-center" >' +
			'<button type="button" class="btn btn-info ok" data-dismiss="modal" >确认</button>' +
			'</div>' +
			'</div>' +
			'</div>' +
			'</div>' +
			'</div>';
		return html;
	},
	show:function(msg, callback){
		// dom init
		if ($('#ComAlertTec').length == 0){
			$('body').append(ComAlertTec.html());
		}

		// 弹框初始
		$('#ComAlertTec .alert').html(msg);
		$('#ComAlertTec').modal('show');

		$('#ComAlertTec .ok').click(function(){
			$('#ComAlertTec').modal('hide');
			if(typeof callback == 'function') {
				callback();
			}
		});
	}
};



function changeState(that){
	var $this = $(that);
	var jobStatus = $this.attr("jobStatus");
	var uuid = $this.attr("uuid");
	var status = $this.attr("status");
	layer.confirm('确认修改 ' + jobStatus + ' 状态?', {icon: 3, title: '系统提示'}, function (index) {
		layer.close(index);
		$.ajax({
			type: 'POST',
			url: base_url + '/joblog/changeState',
			data: {"jobStatus": jobStatus,"uuid":uuid,"status":status},
			dataType: "json",
			success: function (data) {
				if (data.code == 200) {
					layer.open({
						title: '系统提示',
						content: '状态修改成功',
						icon: '1',
						end: function (layero, index) {
							window.location.reload();
						}
					});
				} else {
					layer.open({
						title: '系统提示',
						content: (data.msg || "修改失败"),
						icon: '2'
					});
				}
			},
		});
	});
}

