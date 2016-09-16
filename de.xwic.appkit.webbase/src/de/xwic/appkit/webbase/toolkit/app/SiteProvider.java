/**
 * 
 */
package de.xwic.appkit.webbase.toolkit.app;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import de.jwic.base.JWicRuntime;
import de.jwic.base.SessionContext;

/**
 * Provides access to the Site object in UI threads. The filter needs to be registered in the 
 * web.xml to capture the Site prior to processing the request with jWic.
 * 
 * @author lippisch
 */
public class SiteProvider implements Filter {

	private static ThreadLocal<Site> tlSite = new ThreadLocal<Site>();


	/**
	 * Returns the Site instance of the current thread or <code>null</code> if the
	 * current thread is not the UI thread. 
	 * 
	 * <p>This method may also return <code>null</code> if this class is not registred properly as 
	 * Filter in the web.xml</p> 
	 *  
	 * @return
	 */
	public static Site getSite() {
		return tlSite.get();
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest req, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		if (req instanceof HttpServletRequest) {
			HttpServletRequest hReq = (HttpServletRequest)req;
			String s = hReq.getParameter("_msid");
			if (s != null) { // JWic Session ID -> See if its a XWic App and store info.
				SessionContext ctx = JWicRuntime.getJWicRuntime().getSessionContext(hReq.getSession().getId(), s, hReq);
				if (ctx != null) {
					if (ctx.getApplication() instanceof ExtendedApplication) {
						ExtendedApplication eApp = (ExtendedApplication)ctx.getApplication();
						tlSite.set(eApp.getSite());
					}
				}
			}
		}
		try {
			chain.doFilter(req, response);
		} finally {
			tlSite.set(null);
		}

	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {
		
	}

}
