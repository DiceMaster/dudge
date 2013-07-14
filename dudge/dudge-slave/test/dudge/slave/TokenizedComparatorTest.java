/*
 * TokenizedComparatorTest.java
 * JUnit based test
 *
 * Created on June 3, 2007, 11:00 PM
 */

package dudge.slave;

import java.io.ByteArrayInputStream;
import junit.framework.*;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author Vladimir Shabanov
 */
public class TokenizedComparatorTest extends TestCase {
	
	public TokenizedComparatorTest(String testName) {
		super(testName);
	}

	@Override
	protected void setUp() throws Exception {
	}

	@Override
	protected void tearDown() throws Exception {
	}

	/**
	 * Test of compare method, of class dudge.slave.TokenizedComparator.
	 */
	public void testCompareDigitsSuccess() throws IOException {
		InputStream o1 = new ByteArrayInputStream("324 453\n21 4\n".getBytes());
		InputStream o2 = new ByteArrayInputStream("324 453\n21 4\n".getBytes());
		TokenizedComparator instance = new TokenizedComparator();
		
		boolean result = instance.compare(o1, o2);
		assertTrue(result);
	}

	/**
	 * Test of compare method, of class dudge.slave.TokenizedComparator.
	 */
	public void testCompareDigitsFail() throws IOException {
		InputStream o1 = new ByteArrayInputStream("324 453\n".getBytes());
		InputStream o2 = new ByteArrayInputStream("42 69\n".getBytes());
		TokenizedComparator instance = new TokenizedComparator();
		
		boolean result = instance.compare(o1, o2);
		assertFalse(result);
	}
	
	/**
	 * Test of compare method, of class dudge.slave.TokenizedComparator.
	 */
	public void testCompareStringsSuccess() throws IOException {
		InputStream o1 = new ByteArrayInputStream("Что не убивает меня...\n".getBytes());
		InputStream o2 = new ByteArrayInputStream("Что не убивает меня...\n".getBytes());
		TokenizedComparator instance = new TokenizedComparator();
		
		boolean result = instance.compare(o1, o2);
		assertTrue(result);
	}

	/**
	 * Test of compare method, of class dudge.slave.TokenizedComparator.
	 */
	public void testCompareStringsFail() throws IOException {
		InputStream o1 = new ByteArrayInputStream("тест\n".getBytes());
		InputStream o2 = new ByteArrayInputStream("тесТ\n".getBytes());
		TokenizedComparator instance = new TokenizedComparator();
		
		boolean result = instance.compare(o1, o2);
		assertFalse(result);
	}
}
