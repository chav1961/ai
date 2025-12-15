package chav1961.ai.client;

import java.io.IOException;
import java.net.URI;

import org.junit.Assert;
import org.junit.Test;

import chav1961.ai.api.json.response.VersionInfo;
import chav1961.ai.client.interfaces.APIAction;

public class AIConnectorTest {
	public static final URI SERVER = URI.create("http://localhost:11434");
	
	@Test
	public void versionTest() throws IOException {
		final AIConnector conn = new AIConnector(SERVER);
		final VersionInfo version = conn.call(APIAction.VERSION, VersionInfo.class);
		
		Assert.assertEquals("0.13.3", version.getVersion());
	}
}
