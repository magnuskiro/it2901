package no.ntnu.qos.server.mediators.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;

import no.ntnu.qos.server.mediators.impl.PersistentPriorityData;

import org.junit.Test;


public class PersistentPriorityDataTest {

	private static final String FILENAME = "ppdtest.xml";
	private static final String FILENAME2 = "ppdtest2.xml";
	private static final String FILENAME3 = "ppdtest3.xml";

	@Test
	public void testSetFilename() {
		PersistentPriorityData ppd = new PersistentPriorityData();
		assertEquals("EmptyFileName", "",ppd.getFilename());
		ppd.setFilename("test1.xml");
		assertEquals("SetFileName1", "test1.xml",ppd.getFilename());
		ppd.setFilename("test2.xml");
		assertEquals("SetFileName2", "test1.xml",ppd.getFilename());
	}

	@Test
	public void testIsDataAvailable() throws FileNotFoundException {
		PersistentPriorityData ppd = new PersistentPriorityData();
		assertFalse(ppd.isDataAvailable());
		ppd.setFilename(FILENAME);
		ppd.readData();
		assertTrue(ppd.isDataAvailable());
	}

	@Test (expected=FileNotFoundException.class)
	public void testIsDataAvailableFileNotFound() throws FileNotFoundException{
		PersistentPriorityData ppd = new PersistentPriorityData();
		assertFalse(ppd.isDataAvailable());
		ppd.setFilename("ppdtestfail.xml");
		ppd.readData();
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testReadDataMallformedXML() throws FileNotFoundException{
		PersistentPriorityData ppd = new PersistentPriorityData();
		assertFalse(ppd.isDataAvailable());
		ppd.setFilename(FILENAME3);
		ppd.readData();
	}

	@Test
	public void testGetPriority() throws FileNotFoundException {
		PersistentPriorityData ppd = new PersistentPriorityData();
		ppd.setFilename(FILENAME);
		ppd.readData();
		assertTrue(ppd.isDataAvailable());
		assertTrue(ppd.isUseDefault("testService"));
		assertEquals(123, ppd.getPriority("testRole", "testService"));
		assertEquals(321, ppd.getPriority("nonexistentrole", "testService"));

		ppd = new PersistentPriorityData();
		ppd.setFilename(FILENAME2);
		ppd.readData();
		assertTrue(ppd.isDataAvailable());
		assertFalse(ppd.isUseDefault("testService"));
		assertEquals(123, ppd.getPriority("testRole", "testService"));
		assertEquals(-1, ppd.getPriority("nonexistentrole", "testService"));
	}

	@Test
	public void testGetDiffserv() throws FileNotFoundException {
		PersistentPriorityData ppd = new PersistentPriorityData();
		ppd.setFilename(FILENAME);
		ppd.readData();
		assertTrue(ppd.isDataAvailable());
		assertTrue(ppd.isUseDefault("testService"));
		assertEquals(16, ppd.getDiffserv("testRole", "testService"));
		assertEquals(8, ppd.getDiffserv("nonexistentrole", "testService"));

		ppd = new PersistentPriorityData();
		ppd.setFilename(FILENAME2);
		ppd.readData();
		assertTrue(ppd.isDataAvailable());
		assertFalse(ppd.isUseDefault("testService"));
		assertEquals(16, ppd.getDiffserv("testRole", "testService"));
		assertEquals(-1, ppd.getDiffserv("nonexistentrole", "testService"));
	}

}