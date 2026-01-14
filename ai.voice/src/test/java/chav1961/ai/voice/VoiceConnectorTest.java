package chav1961.ai.voice;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509ExtendedTrustManager;
import java.security.cert.CertificateException;

import org.junit.Assert;

import org.junit.Test;

public class VoiceConnectorTest {

	@Test
	public void test() throws FileNotFoundException, IOException {
	      System.setProperty("https.protocols", "TLSv1.3,TLSv1.2,TLSv1.1,TLSv1");
	      System.setProperty("javax.net.debug", "all");
			
	      
	      System.setProperty("jdk.tls.client.protocols","TLSv1.2,TLSv1.3");
	      
        System.setProperty("http.proxyHost", "http://proxy.compassplus.ru");
        System.setProperty("http.proxyPort", "3128");
        System.setProperty("http.nonProxyHosts", "*.compassplus.ru;*.compassplus.com;compassplus.ru;compassplus.com;10.*;virtual;localhost;*.radixware.org;radixware.org;*.maven.apache.org");
        
        try{final SSLContext sslContext = SSLContext.getInstance("TLS");
            final TrustManager[] trustAllCerts = new TrustManager[]{
                                new X509ExtendedTrustManager() {
                                    @Override public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                        System.err.println("Certs query");
                                        return null;
                                    }
                                    @Override public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                                    @Override public void checkServerTrusted(X509Certificate[] certs, String authType) {}
                                    @Override public void checkClientTrusted(X509Certificate[] xcs, String string, Socket socket) throws CertificateException {}
                                    @Override public void checkServerTrusted(X509Certificate[] xcs, String string, Socket socket) throws CertificateException {}
                                    @Override public void checkClientTrusted(X509Certificate[] xcs, String string, SSLEngine ssle) throws CertificateException {}
                                    @Override public void checkServerTrusted(X509Certificate[] xcs, String string, SSLEngine ssle) throws CertificateException {}
                                    }
                            };
                        
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            final HostnameVerifier hostnameVerifier = new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    System.err.println("Verifier: "+s);
                    return true;
                }
            };
            HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);                            
        } catch (NoSuchAlgorithmException | KeyManagementException exc) {
            exc.printStackTrace();
        }
		final VoiceConnector	vc = new VoiceConnector();
		
		
		try(final InputStream is = new FileInputStream(new File("c:/tmp/test.wav"))) {
			System.err.println(vc.recognize(is));
		}
	}
}
