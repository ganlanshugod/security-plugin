package org.bana.springboot.plugin.web.sitemesh;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.bana.common.util.basic.CollectionUtils;
import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.config.ConfigurableSiteMeshFilter;

public class WebSiteMeshFilter extends ConfigurableSiteMeshFilter{  
	
	private SitemeshProperties sitemeshProperties;
  
    @Override  
    protected void applyCustomConfiguration(SiteMeshFilterBuilder builder) {  
    	if(sitemeshProperties == null){
    		return ;
    	}
    	List<String> decoratorPath = sitemeshProperties.getDecoratorPath();
    	if(CollectionUtils.isNotEmpty(decoratorPath)){
    		for (String config : decoratorPath) {
    			if(!config.contains(",")){
    				throw new IllegalArgumentException("decoratorPath" + config + " must contains ',' to sqlit to arguments to config!! like '/admin/*,decorator.jsp'");
    			}
				String[] split = config.split(",");
				if(split.length > 2){
					throw new IllegalArgumentException("decoratorPath" + config + " must  contains only one ',' to split two arguments");
				}
				builder.addDecoratorPath(split[0], split[1]);
			}
    	}
    	for (String string : SitemeshProperties.banaDecoratorPath) {
    		if(!string.contains(",")){
				throw new IllegalArgumentException("decoratorPath" + string + " must contains ',' to sqlit to arguments to config!! like '/admin/*,decorator.jsp'");
			}
			String[] split = string.split(",");
			if(split.length > 2){
				throw new IllegalArgumentException("decoratorPath" + string + " must  contains only one ',' to split two arguments");
			}
			builder.addDecoratorPath(split[0], split[1]);
		}
    	String excludedPath = sitemeshProperties.getExcludedPath();
    	if(StringUtils.isNotEmpty(excludedPath)){
    		String[] split = excludedPath.split(",");
    		for (String string : split) {
    			 builder.addExcludedPath(string);
			}
    	}
    	//排除默认的sitemesh路径
    	builder.addExcludedPath(SitemeshProperties.DEFAULT_SITEMESH_PATH);
    }

	public SitemeshProperties getSitemeshProperties() {
		return sitemeshProperties;
	}


	public void setSitemeshProperties(SitemeshProperties sitemeshProperties) {
		this.sitemeshProperties = sitemeshProperties;
	}
    
}