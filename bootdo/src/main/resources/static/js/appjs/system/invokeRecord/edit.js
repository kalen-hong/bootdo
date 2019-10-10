$().ready(function() {
	validateRule();
});

$.validator.setDefaults({
	submitHandler : function() {
		update();
	}
});
function update() {
	$.ajax({
		cache : true,
		type : "POST",
		url : "/system/invokeRecord/update",
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
			interfaceName : {
				required : true
			},
			url : {
				required : true
			},
			invokeTime : {
				required : true
			},
			clientId : {
				required : true
			},
			clientIp : {
				required : true
			}
		},
		messages : {
			interfaceName : {
				required : icon + "请输入接口名称"
			},
			url : {
				required : icon + "请输入接口url"
			},
			invokeTime : {
				required : icon + "请输入接口调用时间"
			},
			clientId : {
				required : icon + "请输入客户ID"
			},
			clientIp : {
				required : icon + "请输入客户IP"
			}
		}
	})
}