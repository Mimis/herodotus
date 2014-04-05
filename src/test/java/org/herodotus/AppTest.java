package org.herodotus;

import java.io.UnsupportedEncodingException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     * @throws UnsupportedEncodingException 
     */
    public void testApp() throws UnsupportedEncodingException
    {
    	String t="wikipedia:citation needed";
    	if(!t.contains("wikipedia"))
    		System.out.println(t);
    	
    	assertTrue(true);
    }
}
