package de.xwic.appkit.webbase.modules;

import de.xwic.appkit.webbase.toolkit.app.Module;
import de.xwic.appkit.webbase.toolkit.app.Site;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.Class;import java.lang.ClassNotFoundException;import java.lang.Exception;import java.lang.IllegalAccessException;import java.lang.InstantiationException;import java.lang.Override;import java.lang.String;
import java.util.ArrayList;
import java.util.List;

/**
 * A servlet that will give the list of all modules that a specific user has access to
 * Created by boogie on 3/6/15.
 */
public final class ModuleProviderServlet extends HttpServlet {

	private static final Log log = LogFactory.getLog(ModuleProviderServlet.class);

	private static final String MODULE_FACTORY_INIT_PARAM = "module-factory";
	private static final String USER_NOT_FOUND_ERROR_MESSAGE = "User not found!";
	private static final String BASE_URL = "url";

	private IModuleFactory moduleFactory;
	private String baseUrl;
	private String applicationName;

	@Override
	public void init() throws ServletException {
		//create the module factory
		try {
			this.moduleFactory = createFactory(this.getInitParameter(MODULE_FACTORY_INIT_PARAM));
			this.baseUrl = this.getInitParameter(BASE_URL);
			this.applicationName = this.getInitParameter("app-name");

			System.out.println(baseUrl);
		} catch (FactoryInstantiationException e) {
			throw new ServletException(e);
		}

	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//create a fake site
		final Site fakeSite = new Site(new FakeContainer());

		//make sure that the requested user exists
		final String userName = getUserNameFromPath(req, resp);
		if (userName == null) {
			return;
		}

		final List<Module> modules;
		//catch any possible errors
		try {
			modules = moduleFactory.createModules(userName,fakeSite);
		} catch (Exception ex) {
			error("Error in determining modules for user " + userName, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, resp);
			log.error(ex.getMessage(), ex);
			return;
		}

		try {
			//respond with the modules that the requested user has access to
			ok(serializeModules(modules), resp);
		} catch (JSONException e) {
			//if anything goes wrong just 'throw'
			error("Error in creating content", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, resp);
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * user name from request path
	 *
	 * @param req
	 * @param resp
	 * @return
	 * @throws IOException
	 */
	private String getUserNameFromPath(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		final String pathInfo = req.getPathInfo();
		if (pathInfo == null || pathInfo.isEmpty()) {
			notFound(USER_NOT_FOUND_ERROR_MESSAGE, resp);
			return null;
		}
		return pathInfo.replaceFirst("/", "");
	}

	/**
	 * Create the module factory from given class
	 *
	 * @param className
	 * @return
	 * @throws FactoryInstantiationException
	 */
	private IModuleFactory createFactory(String className) throws FactoryInstantiationException {
		final Class<?> aClass;

		if (className == null || className.isEmpty()) {
			throw new FactoryInstantiationException(MODULE_FACTORY_INIT_PARAM + " init param is not configured!");
		}

		try {
			aClass = Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new FactoryInstantiationException("Factory of type " + className + " not found");
		}

		if (!IModuleFactory.class.isAssignableFrom(aClass)) {
			throw new FactoryInstantiationException("Class " + className + " is not of type " + IModuleFactory.class.getName());
		}

		try {
			return (IModuleFactory) aClass.newInstance();
		} catch (InstantiationException e) {
			throw new FactoryInstantiationException(
					"Can't create factory of type " + className + ". Make sure it has a public no-args constructor");
		} catch (IllegalAccessException e) {
			throw new FactoryInstantiationException("Class " + className + " is not accessible!. Make sure its public.");
		}
	}

	/**
	 * Respond with 404
	 *
	 * @param message
	 * @param response
	 * @throws IOException
	 */
	private static void notFound(String message, HttpServletResponse response) throws IOException {
		error(message, HttpServletResponse.SC_NOT_FOUND, response);
	}

	/**
	 * respond with given status code
	 *
	 * @param message
	 * @param status
	 * @param response
	 * @throws IOException
	 */
	private static void error(String message, int status, HttpServletResponse response) throws IOException {
		response.setStatus(status);
		response.setContentType("application/json");
		response.getWriter().println("{ \"error\" : \"" + message + "\", " + "\"status\" : " + status + "}");
	}

	/**
	 * 200 and send content
	 *
	 * @param content
	 * @param response
	 * @throws IOException
	 * @throws JSONException
	 */
	private static void ok(JSONObject content, HttpServletResponse response) throws IOException, JSONException {
		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType("application/json");
		response.getWriter().println(content.toString(2));
	}

	/**
	 * Make a json array from given list of modules
	 *
	 * @param modules
	 * @return
	 * @throws JSONException
	 */
	private JSONObject serializeModules(List<Module> modules) throws JSONException {
		final List<ModuleBean> allBeans = new ArrayList<ModuleBean>();
		for (Module m : modules) {
			allBeans.add(ModuleBean.fromModule(m, this.baseUrl));
		}
		final ModuleBean moduleBean = new ModuleBean("", this.applicationName, allBeans, this.baseUrl);
		return moduleBean.serialize();
	}

	private static final class FactoryInstantiationException extends Exception {

		public FactoryInstantiationException(String s) {
			super(s);
		}
	}

}
