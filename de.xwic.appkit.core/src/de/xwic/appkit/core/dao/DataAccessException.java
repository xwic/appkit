/*
 * de.xwic.appkit.core.dao.DataAccessException
 * Created on 04.04.2005
 *
 */
package de.xwic.appkit.core.dao;


/**
 * DataAccessException is thrown by access layer classes when
 * errors occur regarding the database access.
 * 
 * @author Florian Lippisch
 */
public class DataAccessException extends RuntimeException {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 1L;
    
    private Object info = null;

    /**
     * 
     */
    public DataAccessException() {
        super();
    }
    /**
     * @param message
     */
    public DataAccessException(String message) {
        super(message);
    }
    /**
     * @param message
     * @param cause
     */
    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
    /**
     * @param cause
     */
    public DataAccessException(Throwable cause) {
        super(cause);
    }
	/**
	 * @return the info
	 */
	public Object getInfo() {
		return info;
	}
	/**
	 * @param info the info to set
	 */
	public void setInfo(Object info) {
		this.info = info;
	}
}
