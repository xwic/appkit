/*******************************************************************************
 * Copyright 2015 xWic group (http://www.xwic.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 *  
 *******************************************************************************/
package de.xwic.appkit.core.dao.impl.remote;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.xwic.appkit.core.dao.IUseCaseService;
import de.xwic.appkit.core.dao.UseCase;
import de.xwic.appkit.core.dao.UseCaseMonitor;
import de.xwic.appkit.core.remote.client.IRemoteDataAccessClient;
import de.xwic.appkit.core.transport.xml.TransportException;

/**
 * @author Adrian Ionescu
 */
public class RemoteUseCaseService implements IUseCaseService {

	private final Log log = LogFactory.getLog(getClass());
	private IRemoteDataAccessClient client;

    /**
     * @param client
     */
    public RemoteUseCaseService(IRemoteDataAccessClient client) {
    	this.client = client;
    }

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.IUseCaseService#execute(de.xwic.appkit.core.dao.UseCase)
	 */
	@Override
	public Object execute(final UseCase useCase) {
		if (log.isDebugEnabled()) {
			log.debug("Executing useCase " + useCase.getClass().getName());
		}

		try {
			return client.executeUseCase(useCase);
		} catch (TransportException e) {
			log.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.IUseCaseService#getMonitor(long)
	 */
	@Override
	public UseCaseMonitor getMonitor(final long ticketNr) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.IUseCaseService#releaseMonitor(long)
	 */
	@Override
	public void releaseMonitor(final long ticketNr) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.IUseCaseService#abortMonitor(long)
	 */
	@Override
	public void abortMonitor(final long ticketNr) {
		throw new UnsupportedOperationException();
	}

}
