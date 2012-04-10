package no.ntnu.qos.client.test;

import java.net.URI;
import java.net.URISyntaxException;

import no.ntnu.qos.client.DataObject;
import no.ntnu.qos.client.credentials.Token;
import no.ntnu.qos.client.credentials.impl.TokenImpl;

public class ManualDataObjectTest {
	public static void main(String[] args) throws URISyntaxException {
		String xml = "<?xml version=\"1.0\" ?><S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\"><S:Body><ns2:hello xmlns:ns2=\"http://me.test.org/\"><name>My text goes here</name></ns2:hello></S:Body></S:Envelope>";
		URI destination = new URI("http://122.22.33.44/destination");
		Token samlToken = new TokenImpl("<validToken>Not very useful, though</validToken>", System.currentTimeMillis()+60000, destination);
		DataObject data = new DataObject(null, xml, destination, null);
		data.setToken(samlToken);
		System.out.println(data.getSoap());
	}
}
