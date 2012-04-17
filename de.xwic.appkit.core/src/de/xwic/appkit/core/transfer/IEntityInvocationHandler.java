/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.core.transfer.IEntityInvocationHandler
 * Created on 08.01.2007 by Florian Lippisch
 *
 */
package de.xwic.appkit.core.transfer;

/**
 * @author Florian Lippisch
 */
public interface IEntityInvocationHandler {

	/**
	 * @return the implementation class of the entity..
	 */
	public Class<?> getEntityImplClass();
	
}
