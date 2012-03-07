package no.ntnu.qos.server.mediators.test;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import no.ntnu.qos.server.mediators.PersistentPriorityData;

import org.junit.Test;


public class PersistentPriorityDataTest {

	private static final String FILENAME = "/home/mahou/Documents/it2901/server/src/no/ntnu/qos/server/mediators/test/ppdtest.xml";
	private static final String FILENAME2 = "/home/mahou/Documents/it2901/server/src/no/ntnu/qos/server/mediators/test/ppdtest2.xml";

	@Test
	public void testSetFilename() {
		PersistentPriorityData ppd = new PersistentPriorityData();
		assertEquals("EmptyFileName", "",ppd.getFilename());
		ppd.setFilename("test1.xml");
		assertEquals("SetFileName1", "test1.xml",ppd.getFilename());
		ppd.setFilename("test2.xml");
		assertEquals("SetFileName2", "test2.xml",ppd.getFilename());
	}

	@Test
	public void testIsDataAvailable() throws FileNotFoundException {
		PersistentPriorityData ppd = new PersistentPriorityData();
		assertFalse(ppd.isDataAvailable());
		ppd.setFilename(FILENAME);
		assertTrue(ppd.isDataAvailable());
	}

	@Test (expected=FileNotFoundException.class)
	public void testIsDataAvailableFileNotFound() throws FileNotFoundException{
		PersistentPriorityData ppd = new PersistentPriorityData();
		assertFalse(ppd.isDataAvailable());
		ppd.setFilename("ppdtestfail.xml");
		ppd.isDataAvailable();
	}
	
	@Test
	public void testGetPriority() throws FileNotFoundException {
		PersistentPriorityData ppd = new PersistentPriorityData();
		ppd.setFilename(FILENAME);
		assertTrue(ppd.isDataAvailable());
		assertEquals(123, ppd.getPriority("testRole", "testService"));
		assertEquals(321, ppd.getPriority("nonexistentrole", "testService"));
		assertEquals(321, ppd.getPriority("testRole", "nonexistentservice"));
		
		ppd = new PersistentPriorityData();
		ppd.setFilename(FILENAME2);
		assertTrue(ppd.isDataAvailable());
		assertEquals(123, ppd.getPriority("testRole", "testService"));
		assertEquals(-1, ppd.getPriority("nonexistentrole", "testService"));
		assertEquals(-1, ppd.getPriority("testRole", "nonexistentservice"));
	}

	@Test
	public void testGetDiffserv() throws FileNotFoundException {
		PersistentPriorityData ppd = new PersistentPriorityData();
		ppd.setFilename(FILENAME);
		assertTrue(ppd.isDataAvailable());
		assertEquals(16, ppd.getDiffserv("testRole", "testService"));
		assertEquals(8, ppd.getDiffserv("nonexistentrole", "testService"));
		assertEquals(8, ppd.getDiffserv("testRole", "nonexistentservice"));
		
		ppd = new PersistentPriorityData();
		ppd.setFilename(FILENAME2);
		assertTrue(ppd.isDataAvailable());
		assertEquals(16, ppd.getDiffserv("testRole", "testService"));
		assertEquals(-1, ppd.getDiffserv("nonexistentrole", "testService"));
		assertEquals(-1, ppd.getDiffserv("testRole", "nonexistentservice"));
	}

}
