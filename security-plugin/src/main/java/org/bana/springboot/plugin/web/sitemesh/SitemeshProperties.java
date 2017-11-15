/**
 * 
 */
package org.bana.springboot.plugin.web.sitemesh;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author liuwenjie
 *
 */
@ConfigurationProperties(prefix="sitemesh")
public class SitemeshProperties {
	
	public static final String DEFAULT_SITEMESH_PATH="/sitemesh/*";
	
	public static List<String> banaDecoratorPath = new ArrayList<String>();
	
	static{
		banaDecoratorPath.add("/user/*,/sitemesh/common");
		banaDecoratorPath.add("/index,/sitemesh/common");
		banaDecoratorPath.add("/login,/sitemesh/common");
		banaDecoratorPath.add("/register,/sitemesh/common");
	}

	private List<String> decoratorPath;
	
	private String excludedPath;

	public List<String> getDecoratorPath() {
		return decoratorPath;
	}

	public void setDecoratorPath(List<String> decoratorPath) {
		this.decoratorPath = decoratorPath;
	}

	public String getExcludedPath() {
		return excludedPath;
	}

	public void setExcludedPath(String excludedPath) {
		this.excludedPath = excludedPath;
	}
	
}
