/**
 * 
 */
package de.xwic.appkit.core.remote.client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpResponse;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

/**
 * Http Connection Pool Manager.
 * 
 * @author dotto
 */
public class PoolingHttpConnectionManager {

	private static PoolingHttpConnectionManager manager = null;
	private PoolingHttpClientConnectionManager connectionManager;
	private static Map<RemoteSystemConfiguration, CloseableHttpClient> clientMap = null;

	private static Log log = LogFactory.getLog(PoolingHttpConnectionManager.class);

	/**
	 * Keep/Alive strategy
	 */
	private ConnectionKeepAliveStrategy myStrategy = new ConnectionKeepAliveStrategy() {
		@Override
		public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
			HeaderElementIterator it = new BasicHeaderElementIterator(
					response.headerIterator(HTTP.CONN_KEEP_ALIVE));
			while (it.hasNext()) {
				HeaderElement he = it.nextElement();
				String param = he.getName();
				String value = he.getValue();
				if (value != null && param.equalsIgnoreCase("timeout")) {
					return Long.parseLong(value) * 1000;
				}
			}
			return 30 * 1000;
		}
	};

	/**
	 * 
	 */
	private PoolingHttpConnectionManager() {
		connectionManager = new PoolingHttpClientConnectionManager();
		connectionManager.setMaxTotal(40);
		connectionManager.setDefaultMaxPerRoute(10);
	}

	/**
	 * Returns the instance
	 * @return
	 */
	public static PoolingHttpConnectionManager getInstance() {

		if (manager == null) {
			manager = new PoolingHttpConnectionManager();
		}
		return manager;
	}

	/**
	 * Returns a client instance for given config
	 * @param config
	 * @return
	 */
	public CloseableHttpClient getClientInstance(RemoteSystemConfiguration config) {

		if(clientMap == null){
			clientMap = new HashMap<RemoteSystemConfiguration, CloseableHttpClient>();
		}
		
		CloseableHttpClient client = null;
		if (clientMap.containsKey(config)) {
			client = clientMap.get(config);
			log.debug(connectionManager.getTotalStats().toString());
			return client;
		}
		

		client = HttpClients.custom().setKeepAliveStrategy(myStrategy).setConnectionManager(connectionManager).setConnectionManagerShared(true)
				.build();
		clientMap.put(config, client);

		return client;
	}

	/**
	 * Close clients of current thread
	 */
	public void closeClients() {
		if (clientMap == null)
			return;
		for (CloseableHttpClient client : clientMap.values()) {
			try {
				client.close();
			} catch (IOException e) {
				log.error("Error while closing client connection: ", e);
			}
		}
		clientMap = null;
	}
	
	/**
	 * Close all
	 */
	public void closeManager() {
		connectionManager.close();
		manager = null;
	}
}
