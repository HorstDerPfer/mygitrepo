package db.training.hibernate;

import org.hibernate.Session;

/**
 * Wird benutzt, wenn ein Befehl an das HibernateTemplate ueber geben wird. Der Befehl wird
 * innerhalb einer Transaction ausgefuehrt.
 * 
 * @author Sebastian Hennebrueder
 * 
 */
public interface HibernateCommand {
	public Object execute(Session session, Object...param);
}
