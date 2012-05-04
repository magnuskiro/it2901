package no.ntnu.qos.client.test;

import no.ntnu.qos.client.impl.SanityCheckerImpl;

public class ManualSanityCheckTest {

    // todo automate the test.

	public static void main(String[] args) {
		SanityCheckerImpl checker = new SanityCheckerImpl();
		String xml = "<?xml version=\"1.0\" ?>"+
		"<S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\">"+
		"<S:Body><ns2:hello xmlns:ns2=\"http://me.test.org/\">"+
		"<name>My text goes here</name></ns2:hello></S:Body></S:Envelope>";
		String xml2 = "Muahaha";
		System.out.println("checking1");
		System.out.println(checker.isSane(xml));
		System.out.println("Validated1\nchecking2");
		System.out.println(checker.isSane(xml2));
		System.out.println("validated2");
	}
}
