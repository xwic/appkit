/**
 * 
 */
package de.xwic.appkit.core.remote.client;

import static org.junit.Assert.*;

import org.junit.Test;

import de.xwic.appkit.core.transfer.EntityTransferObject;
import de.xwic.appkit.core.transport.xml.TransportException;

/**
 * @author lippisch
 *
 */
public class TestClient {

	@Test
	public void testCall() throws RemoteDataAccessException, TransportException {

		String remoteUrl = "http://localhost:8080/pulse/";
		String remoteSystemId = "lippisch";
		
		RemoteDataAccessClient client = new RemoteDataAccessClient(remoteUrl, remoteSystemId);
		EntityTransferObject eto = client.getETO("com.netapp.pulse.model.entities.IMitarbeiter", 15);
		
		System.out.println(eto);
		
		assertTrue(true);
		
	}

}
