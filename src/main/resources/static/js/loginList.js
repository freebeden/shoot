$(function() {
    // init date tables
    var logTable = $("#shoot_list").dataTable({
        "deferRender": true,
        "processing": true,
        "serverSide": true,
        "ajax": {
            url: base_url + "/shoot/shootList",
            data: function (d) {
                var obj = {};
                obj.username = $('#username').val();
                obj.itemModel = $('#itemModel').val();
                obj.state = $('#state').val();
                obj.start = d.start;
                obj.length = d.length;
                return obj;
            }
        },
        "searching": false,
        "ordering": false,
        "columns": [
/*
            {"data": 'pid', "bSortable": false, "visible": false},
*/
            {"data": 'uid', "visible": true},
            {"data": 'username', "visible": true},
            {"data": 'itemName', "visible": true},
            {"data": 'itemModel', "visible": true},
            {"data": 'registerNo', "visible": true},
            {
                "data": "state",
                "width": 'auto',
                "render": function (data, type, row) {
                    var text =  '未同步';
                    if (data == '1') {
                        text = '未抽签';
                    } else if (data == '2'){
                        text = '未中签';
                    } else  if (data == '3') {
                        text = '已中签';
                    }
                    return function () {
                        var html = '<span style="color: green">'+text+'</span>';
                        return html;
                    };
                }
            },
            {"data": 'shopName', "visible": true},
            {"data": 'createTime', "visible": true},
            {"data": 'updateTime', "visible": true},
            {"data": 'jwt', "visible": false}
        ],
        "language": {
            "sProcessing": "处理中...",
            "sLengthMenu": "每页 _MENU_ 条记录",
            "sZeroRecords": "没有匹配结果",
            "sInfo": "第 _PAGE_ 页 ( 总共 _PAGES_ 页，_TOTAL_ 条记录 )",
            "sInfoEmpty": "无记录",
            "sInfoFiltered": "(由 _MAX_ 项结果过滤)",
            "sInfoPostFix": "",
            "sSearch": "搜索:",
            "sUrl": "",
            "sEmptyTable": "表中数据为空",
            "sLoadingRecords": "载入中...",
            "sInfoThousands": ",",
            "oPaginate": {
                "sFirst": "首页",
                "sPrevious": "上页",
                "sNext": "下页",
                "sLast": "末页"
            },
            "oAria": {
                "sSortAscending": ": 以升序排列此列",
                "sSortDescending": ": 以降序排列此列"
            }
        }
    });

    // 搜索按钮
    $('#searchBtn').on('click', function(){
        logTable.fnDraw();
    });

    $("#taskJob").on('click',function () {
        location.href = base_url + "/"
    })

    $("#queryResult").on('click', function () {
        layer.confirm('确认同步登记数据?', {icon: 3, title:'系统提示'}, function(index){
            layer.close(index);
            $.ajax({
                type : 'POST',
                url : base_url + '/joblog/queryState',
                data : {},
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

    $('#clearLog').on('click', function(){
        $("#form").find("input[type='text']").val('');
    });

    $("#start").datetimepicker({
        format:'yyyy-mm-dd',  //格式  如果只有yyyy-mm-dd那就是0000-00-00
        autoclose:true,//选择后是否自动关闭
        minView:0,//最精准的时间选择为日期  0-分 1-时 2-日 3-月
        language:  'zh-CN', //中文
        weekStart: 1, //一周从星期几开始
        todayBtn:  1,
        autoclose: 1,
        todayHighlight: 1,
        startView: 2,
        forceParse: 0,
        // daysOfWeekDisabled:"1,2,3", //一周的周几不能选 格式为"1,2,3"  数组格式也行
        todayBtn : true,  //在底部是否显示今天
        todayHighlight :false, //今天是否高亮显示
        keyboardNavigation:true, //方向图标改变日期  必须要有img文件夹 里面存放图标
        showMeridian:false,  //是否出现 上下午
        initialDate:new Date()
        //startDate: "2017-01-01" //日期开始时间 也可以是new Date()只能选择以后的时间
    }).on("changeDate",function(){
        var start = $("#start").val();
        $("#start").datetimepicker("setStartDate",start);
    });

});

function importFile(that) {
    var formData = new FormData();
    formData.append("file",that.files[0]);
    $.ajax({
        url: base_url + '/joblog/uploadFile',
        type : 'POST',
        data : formData,
        // 告诉jQuery不要去处理发送的数据
        processData : false,
        // 告诉jQuery不要去设置Content-Type请求头
        contentType : false,
        beforeSend:function(){
            console.log("正在进行，请稍候");
        },
        success: function (data) {
            if (data.code == 200) {
                layer.open({
                    title: '系统提示',
                    content: '导入数据成功',
                    icon: '1',
                    end: function (layero, index) {
                        window.location.reload();
                    }
                });
            } else {
                layer.open({
                    title: '系统提示',
                    content: (data.msg || " 导入失败"),
                    icon: '2'
                });
            }
        },
        error : function(data) {
            console.log("error");
        }
    });
}
