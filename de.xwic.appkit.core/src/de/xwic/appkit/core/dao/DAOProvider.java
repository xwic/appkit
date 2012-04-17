/*
 * de.xwic.appkit.core.dao.DAO
 * Created on 04.04.2005
 *
 */
package de.xwic.appkit.core.dao;

/**
 * Implements basic CRUD (Create Read Update Delete) operations using the
 * callback pattern. These operations are used by the DAO implementation.</p>
 * 
 * <p>Sample:<br>
 * <code> 
 * provider.execute(new DAOCallback() {
 *   public Object run(DAOProviderAPI api) {
 *     MyEntity e = new MyEntity();
 *     // do some
 *     api.update(e);
 *     return e;
 *	 }
 * });
 *  
 * 
 * @author Florian Lippisch
 */
public interface DAOProvider {

	/**
	 * Execute an operation.
	 * @param operation
	 * @return
	 */
	public Object execute(DAOCallback operation);
	/**
	 * Register a query resolver.
	 * @param queryClass
	 * @param resolver
	 */
	public void registerQuery(Class<? extends EntityQuery> queryClass, IEntityQueryResolver resolver); 

}
