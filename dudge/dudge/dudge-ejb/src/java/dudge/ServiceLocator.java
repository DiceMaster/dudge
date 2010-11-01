/*
 * ServiceLocator.java
 *
 * Created on June 20, 2007, 1:18 AM
 */
package dudge;


import java.net.URL;

import javax.ejb.EJBHome;
import javax.ejb.EJBLocalHome;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import javax.sql.DataSource;
import javax.mail.Session;

/**
 *
 * @author Vladimir Shabanov
 * @version
 */
public class ServiceLocator {
	
	private InitialContext ic;
	
	public ServiceLocator() {
		try {
			ic = new InitialContext();
		} catch (NamingException ne) {
			throw new RuntimeException(ne);
		}
	}
	
	private Object lookup(String jndiName) throws NamingException {
		return ic.lookup(jndiName);
	}
	
	/**
	 * will get the ejb Local home factory.
	 * clients need to cast to the type of EJBHome they desire
	 *
	 * @return the Local EJB Home corresponding to the homeName
	 */
	public EJBLocalHome getLocalHome(String jndiHomeName) throws NamingException {
		return (EJBLocalHome) lookup(jndiHomeName);
	}
	
	/**
	 * will get the ejb Remote home factory.
	 * clients need to cast to the type of EJBHome they desire
	 *
	 * @return the EJB Home corresponding to the homeName
	 */
	public EJBHome getRemoteHome(String jndiHomeName, Class className) throws NamingException {
		Object objref = lookup(jndiHomeName);
		return (EJBHome) PortableRemoteObject.narrow(objref, className);
	}
	
	/**
	 * This method helps in obtaining the jms connection factory
	 * @return the factory for obtaining jms connection
	 */
	public ConnectionFactory getConnectionFactory(String connFactoryName) throws NamingException {
		return (ConnectionFactory) lookup(connFactoryName);
	}
	
	/**
	 * This method obtains the topc itself for a caller
	 * @return the Topic Destination to send messages to
	 */
	public Destination getDestination(String destName) throws NamingException {
		return (Destination) lookup(destName);
	}
	
	/**
	 * This method obtains the datasource itself for a caller
	 * @return the DataSource corresponding to the name parameter
	 */
	public DataSource getDataSource(String dataSourceName) throws NamingException {
		return (DataSource) lookup(dataSourceName);
	}
	
	/**
	 * This method obtains the E-mail session itself for a caller
	 * @return the Session corresponding to the name parameter
	 */
	public Session getSession(String sessionName) throws NamingException {
		return (Session) lookup(sessionName);
	}
	
	/**
	 * @return the URL value corresponding
	 * to the env entry name.
	 */
	public URL getUrl(String envName) throws NamingException {
		return (URL) lookup(envName);
	}
	
	/**
	 * @return the boolean value corresponding
	 * to the env entry
	 */
	public boolean getBoolean(String envName) throws NamingException {
		Boolean bool = (Boolean) lookup(envName);
		return bool.booleanValue();
	}
	
	/**
	 * @return the String value corresponding
	 * to the env entry name.
	 */
	public String getString(String envName) throws NamingException {
		return (String) lookup(envName);
	}
}

