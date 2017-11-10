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
    				throw new IllegalArgumentException("decoratorPath must contains ',' to sqlit to arguments to config!! like '/admin/*,decorator.jsp'");
    			}
				String[] split = config.split(",");
				if(split.length > 2){
					throw new IllegalArgumentException("decoratorPath must  contains only one ',' to split two arguments");
				}
				builder.addDecoratorPath(split[0], split[1]);
			}
    	}
    	String excludedPath = sitemeshProperties.getExcludedPath();
    	if(StringUtils.isNotEmpty(excludedPath)){
    		String[] split = excludedPath.split(",");
    		for (String string : split) {
    			 builder.addExcludedPath(string);
			}
    	}
    }

	public SitemeshProperties getSitemeshProperties() {
		return sitemeshProperties;
	}


	public void setSitemeshProperties(SitemeshProperties sitemeshProperties) {
		this.sitemeshProperties = sitemeshProperties;
	}
    
}