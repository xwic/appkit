/*
 * Copyright (c) 2009 Network Appliance, Inc.
 * All rights reserved.
 */

package de.xwic.appkit.webbase.trace;

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
import de.xwic.appkit.core.trace.ITraceContext;
import de.xwic.appkit.core.trace.Trace;
import de.xwic.appkit.webbase.toolkit.app.BreadCrumbControl;
import de.xwic.appkit.webbase.toolkit.app.ExtendedApplication;
import de.xwic.appkit.webbase.toolkit.app.Site;

/**
 * Used to filter request based information and to log requests that exceed a certain threshold.
 * @author lippisch
 */
public class HttpTraceFilter implements Filter {

	public final static String ATTR_REMOTE_ADDR = "req-remote-address";
	public final static String ATTR_QUERY_STRING = "req-query-string";
	public final static String ATTR_METHOD = "req-query-string";
	public final static String ATTR_REQUEST_URI = "req-request-uri";
	public final static String ATTR_REMOTE_USER = "req-remote-user";
	public final static String ATTR_JWIC_CONTROL = "jwic-control";
	public final static String ATTR_JWIC_ACTION = "jwic-action";
	public final static String ATTR_SITE_MODULE = "site-module";
	public final static String ATTR_SITE_SUBMODULE = "site-submodule";
	public final static String ATTR_USER_PATH = "user-path";
	
	/* (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		
		if (!Trace.isEnabled()) {
			
			chain.doFilter(req, res);
		
		} else {
			
			ITraceContext traceCtx = Trace.startTrace();
			try {
				
				traceCtx.setAttribute(ATTR_REMOTE_ADDR, req.getRemoteAddr());
				if (req instanceof HttpServletRequest) {
					HttpServletRequest hReq = (HttpServletRequest)req;
					traceCtx.setAttribute(ATTR_QUERY_STRING, hReq.getQueryString());
					traceCtx.setAttribute(ATTR_METHOD, hReq.getMethod());
					traceCtx.setAttribute(ATTR_REQUEST_URI, hReq.getRequestURI());
					traceCtx.setAttribute(ATTR_REMOTE_USER, hReq.getRemoteUser());
					String s = hReq.getParameter("__action");
					if (s != null) {
						traceCtx.setAttribute(ATTR_JWIC_ACTION, s);
					}
					s = hReq.getParameter("__ctrlid");
					if (s != null) {
						traceCtx.setAttribute(ATTR_JWIC_CONTROL, s);
					}

				}
				
				chain.doFilter(req, res);
				if (req instanceof HttpServletRequest) {
					HttpServletRequest hReq = (HttpServletRequest)req;
					String s = hReq.getParameter("_msid");
					if (s != null) { // JWic Session ID -> See if its a XWic App and store info.
						SessionContext ctx = JWicRuntime.getJWicRuntime().getSessionContext(hReq.getSession().getId(), s, hReq);
						if (ctx != null) {
							if (ctx.getApplication() instanceof ExtendedApplication) {
								ExtendedApplication eApp = (ExtendedApplication)ctx.getApplication();
								Site site = eApp.getSite();
								if (site != null) {
									traceCtx.setAttribute(ATTR_SITE_MODULE, site.getActiveModuleKey());
									traceCtx.setAttribute(ATTR_SITE_SUBMODULE, site.getActiveSubModuleKey());
									BreadCrumbControl bcc = (BreadCrumbControl) site.getControl("breadCrumb");
									if (bcc != null) {
										StringBuilder path = new StringBuilder();
										for (String key : bcc.getBreadCrumbs()) {
											if (path.length() != 0) {
												path.append(" >> ");
											}
											path.append(bcc.getCrumbTitle(key));
										}
										traceCtx.setAttribute(ATTR_USER_PATH, path.toString());
									}
								}
							}
						}
					}
				}
				
			} finally {
				
				// log trace information?
				Trace.endTrace();
				Trace.getDataManager().handleTraceResult(traceCtx);
				
			}
		}
		
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

}
