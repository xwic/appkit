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
package de.xwic.appkit.core.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Florian Lippisch
 */
public class DefaultUseCaseService implements IUseCaseService {

	private final Log log = LogFactory.getLog(getClass());
	/** The DAOProvider */
    protected DAOProvider provider = null;

    /**
     * Constructor.
     * @param provider
     */
    public DefaultUseCaseService(DAOProvider provider) {
    	this.provider = provider;
    }
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.IUseCaseService#execute(de.xwic.appkit.core.dao.UseCase)
	 */
	public Object execute(final UseCase useCase) {
		
		if (log.isDebugEnabled()) {
			log.debug("Executing useCase " + useCase.getClass().getName());
		}
		return provider.execute(new DAOCallback() {
    		public Object run(DAOProviderAPI api) {
    			return useCase.internalExecuteUseCase(api);
    		}
    	});
		
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.IUseCaseService#getMonitor(long)
	 */
	public UseCaseMonitor getMonitor(long ticketNr) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.IUseCaseService#releaseMonitor(long)
	 */
	public void releaseMonitor(long ticketNr) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.IUseCaseService#abortMonitor(long)
	 */
	public void abortMonitor(long ticketNr) {
		throw new UnsupportedOperationException();
	}

}
