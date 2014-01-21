/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.dao.AbstractUseCaseService
 * Created on 20.09.2005
 *
 */
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
