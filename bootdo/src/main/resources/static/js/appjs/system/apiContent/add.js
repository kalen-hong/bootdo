$().ready(function() {
	validateRule();
});

$.validator.setDefaults({
	submitHandler : function() {
		save();
	}
});
function save() {
	$.ajax({
		cache : true,
		type : "POST",
		url : "/system/apiContent/save",
		data : $('#signupForm').serialize(),// 你的formid
		async : false,
		error : function(request) {
			parent.layer.alert("Connection error");
		},
		success : function(data) {
			if (data.code == 0) {
				parent.layer.msg("操作成功");
				parent.reLoad();
				var index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
				parent.layer.close(index);

			} else {
				parent.layer.alert(data.msg)
			}

		}
	});

}
function validateRule() {
	var icon = "<i class='fa fa-times-circle'></i> ";

	$("#signupForm").validate({
		rules : {
			apiUrl : {
				required : true,
				remote : {
					url : "/system/apiContent/exit", // 后台处理程序
					type : "post", // 数据发送方式
					dataType : "json", // 接受数据格式
					data : { // 要传递的数据
						apiUrl : function() {
							return $("#apiUrl").val();
						}
					}
				}
			},
			apiDesc : {
				required : true
			},
			requestMode : {
				required : true
			},
			requestCost : {
				required : true
			}
		},
		messages : {
			apiUrl : {
				required : icon + "请输入接口地址",
				remote : icon + "接口地址已经存在"
			},
			apiDesc : {
				required : icon + "请输入接口描述"
			},
			requestMode : {
				required : icon + "请选择接口请求方式"
			},
			requestCost : {
				required : icon + "请输入接口调用费用"
			}
		}
	})
}