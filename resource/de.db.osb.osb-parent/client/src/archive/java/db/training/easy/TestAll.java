/**
 * 
 */
package db.training.easy;

import db.training.easy.core.dao.model.AllModelTests;
import db.training.easy.core.service.AllServiceTests;

/**
 * @author MarkusHohloch
 * 
 */
public class TestAll {

    public static void main(String args[]) {
	AllModelTests.suite();
	AllServiceTests.suite();
    }

}
