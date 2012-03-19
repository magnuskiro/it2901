package no.ntnu.qos.server.mediators.test;

import no.ntnu.qos.server.store.test.PrioritizedMessageStoreTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ MetadataMediatorTest.class, MSMediatorTest.class,
		PersistentPriorityDataTest.class, SAMLMediatorTest.class,
		ThrottleMediatorTest.class, PrioritizedMessageStoreTest.class,
		QosContextTest.class})
public class ServerTestSuite {

}
