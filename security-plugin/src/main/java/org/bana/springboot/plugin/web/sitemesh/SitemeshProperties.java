/**
 * 
 */
package org.bana.springboot.plugin.web.sitemesh;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author liuwenjie
 *
 */
@ConfigurationProperties(prefix="sitemesh")
public class SitemeshProperties {

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
