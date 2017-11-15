/**
 * 
 */
package org.bana.springboot.plugin.web.sitemesh;

import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author liuwenjie
 *
 */
public class SitemeshController {

	@RequestMapping("/common")
	public String common(){
		return "sitemesh/bana-security";
	}
}
