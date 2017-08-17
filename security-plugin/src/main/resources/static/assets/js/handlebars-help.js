!function(){
	Handlebars.registerHelper('contains', function(source, target, options) {
		if(typeof source === "object"){
			source = rolesToString(source);
		}
		source = Handlebars.Utils.escapeExpression(source).toLocaleUpperCase();
		target  = Handlebars.Utils.escapeExpression(target).toLocaleUpperCase();

		// return new Handlebars.SafeString(result);
		
		if(source.indexOf(target) > -1){
	　　　　　　// 满足条件执行
	　　　　　　return options.fn(this);
	　　　　}else{
	　　　　　　// 不满足执行{{else}}部分
	　　　　　　return options.inverse(this);
	　　　　}
	});
	
	var rolesToString = function(value,row,index){
		var arr = new Array();
		for(v in value){
			arr.push(value[v].authority);
		}
		return '[' + arr.toString() + ']';
	}
}();