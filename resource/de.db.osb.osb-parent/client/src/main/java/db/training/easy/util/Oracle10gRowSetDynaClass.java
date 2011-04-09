/**
 * 
 */
package db.training.easy.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import oracle.sql.CLOB;

import org.apache.commons.beanutils.BasicDynaBean;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.RowSetDynaClass;

/**
 * @author michels
 * 
 */
public class Oracle10gRowSetDynaClass extends RowSetDynaClass {

	private static final long serialVersionUID = -1976492272319522504L;

	/**
	 * @param resultSet
	 * @throws SQLException
	 */
	public Oracle10gRowSetDynaClass(ResultSet resultSet) throws SQLException {
		super(resultSet);
	}

	public Oracle10gRowSetDynaClass(ResultSet resultSet, boolean lowerCase, int limit)
	    throws SQLException {
		super(resultSet, lowerCase, limit);
		// TODO Auto-generated constructor stub
	}

	public Oracle10gRowSetDynaClass(ResultSet resultSet, boolean lowerCase) throws SQLException {
		super(resultSet, lowerCase);
		// TODO Auto-generated constructor stub
	}

	public Oracle10gRowSetDynaClass(ResultSet resultSet, int limit) throws SQLException {
		super(resultSet, limit);
		// TODO Auto-generated constructor stub
	}

	protected void copy(ResultSet resultSet) throws SQLException {
		while (resultSet.next()) {
			DynaBean bean = new Oracle10gBasicDynaBeanVeryDirtyHack(this);
			for (int i = 0; i < properties.length; i++) {
				String name = properties[i].getName();
				Object value = null;
				if (properties[i].getType().equals(Timestamp.class)) {
					value = resultSet.getTimestamp(name);
				} else if (properties[i].getType().equals(Double.class)) {
					if (resultSet.getObject(name) == null)
                		value = resultSet.getObject(name);
                	else
                		value = resultSet.getDouble(name);
				} else if (properties[i].getType().equals(CLOB.class)) {
					value = resultSet.getString(name);
				} else {
					value = resultSet.getObject(name);
				}
				bean.set(name, value);
			}
			rows.add(bean);
		}
	}
}
