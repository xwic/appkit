/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.dao.AbstractUseCaseService
 * Created on 20.09.2005
 *
 */
package de.xwic.appkit.core.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Florian Lippisch
 */
public class RemoteUseCaseService implements IUseCaseService {

	private final Log log = LogFactory.getLog(getClass());
	/** The DAOProvider */
    protected DAOProvider provider = null;

    /**
     * Constructor.
     * @param provider
     */
    public RemoteUseCaseService(final DAOProvider provider) {
    	this.provider = provider;
    }

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.IUseCaseService#execute(de.xwic.appkit.core.dao.UseCase)
	 */
	@Override
	public Object execute(final UseCase useCase) {

		if (log.isDebugEnabled()) {
			log.debug("Executing useCase " + useCase.getClass().getName());
		}
		return provider.execute(new DAOCallback() {
    		@Override
			public Object run(final DAOProviderAPI api) {
    			return useCase.internalExecuteUseCase(api);
    		}
    	});

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
