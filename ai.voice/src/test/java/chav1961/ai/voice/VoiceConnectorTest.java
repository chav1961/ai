package chav1961.ai.voice;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;

import org.junit.Assert;
import org.junit.Test;

import chav1961.purelib.basic.Utils;

public class VoiceConnectorTest {
	private static final Proxy	PROXY = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy.compassplus.ru",3128));
	
	@Test
	public void test() throws IOException {
		final VoiceConnector	vc = new VoiceConnector(PROXY);
		
		try(final InputStream	is = this.getClass().getResourceAsStream("test.wav")) {
			Assert.assertEquals("1, 2, 3, 4, 5.", vc.recognize(is));
			
		}
		try(final InputStream	is = vc.vocalize("Хорошо живет на свете Винни-Пух!");
			final OutputStream	os = new FileOutputStream(new File("c:/tmp/zzz.wav"))) {
			
			Utils.copyStream(is, os);
		}
	}
}
