/**
 * jjquery的扩展功能
 * @param $
 */
!function($){
	$.fn.serializeObject = function() {
		var o = {};
		var a = this.serializeArray();
		$.each(a, function() {
			if (o[this.name] !== undefined) {
				if (!o[this.name].push) {
					o[this.name] = [ o[this.name] ];
				}
				o[this.name].push(this.value || '');
			} else {
				o[this.name] = this.value || '';
			}
		});
		return o;
	};
	
	/**
	 * 通用ajax的配置属性,为了支持_csrf的配置
	 */
	var token = $("meta[name='_csrf']").attr("content");  
	var header = $("meta[name='_csrf_header']").attr("content");  
	if(token && header){
		$(document).ajaxSend(function(e, xhr, options) {  
		     xhr.setRequestHeader(header, token);
		});
	}
	
}(jQuery);

!function(){
	//配置提示框的配置 toastr
	if(toastr){
		toastr.options = {
		  "closeButton": true,
		  "debug": false,
		  "newestOnTop": true,
		  "progressBar": true,
		  "positionClass": "toast-top-center",
		  "preventDuplicates": false,
		  "showDuration": "300",
		  "hideDuration": "1000",
		  "timeOut": "5000",
		  "extendedTimeOut": "1000",
		  "showEasing": "swing",
		  "hideEasing": "linear",
		  "showMethod": "fadeIn",
		  "hideMethod": "fadeOut"
		}
	}
}();
