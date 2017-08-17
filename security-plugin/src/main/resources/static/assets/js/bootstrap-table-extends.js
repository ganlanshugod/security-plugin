!function($){
	/**
	 * 重载bootstrapTable的方法或进行树形加强
	 */
	if(typeof $.fn.bootstrapTable === "function"){
		$.extend($.fn.bootstrapTable.defaults, $.fn.bootstrapTable.locales['zh-CN']);
		$.fn.bootstrapTable2 = $.fn.bootstrapTable ;
		$.fn.bootstrapTable = function(option){
			if(typeof option === "string"){
				return $.fn.bootstrapTable2.apply($(this),arguments);
			}
			option = option||{};
			var opt = {
				dataField:"content",
				totalField:"totalElements",
//				responseHandler:function(res){
//					console.log("result" ,res);
//				},
				ajax:function(params){
					console.log("custom ajaxRequest params",params);
					if(params.data.limit){
						params.data.size = params.data.limit;
						params.data.page = params.data.offset/params.data.limit;
						delete params.data.limit;
						delete params.data.offset;
					}else if(params.data.pageNumber){
						params.data.page = params.data.pageNumber - 1;
						params.data.size = params.data.pageSize;
						delete params.data.pageNumber;
						delete params.data.pageSize;
					}
					$.ajax(params);
				}
			};
			option = $.extend(option,opt);
			console.log("初始化了增强扩展的bootstrapTable方法",option);
			return $(this).bootstrapTable2(option);
		}
	}
	
	//扩展几个方法
	bootstrapTable = {};
	
	bootstrapTable.index = function(value,row,index){
		return index+1
	};
	
	bootstrapTable.toString = function(value,row,index){
		if(typeof value == "object"){
			if(value instanceof Array){
				return value.join(",");
			}else{
				return value;
			}
		}
		return value;
	}
}(jQuery);