package chav1961.ai.voice;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.junit.Test;

public class VoiceConnectorTest {

	@Test
	public void test() throws FileNotFoundException, IOException, NoSuchAlgorithmException, KeyManagementException {
	      System.setProperty("javax.net.debug", "all");
	        String urlStr = "https://ngw.devices.sberbank.ru:9443/api/v2/oauth"; // замените на ваш URL
	        String postData = "param1=value1&param2=value2"; // ваши данные POST

	        try {
	            // Настройка SSL контекста
	            SSLContext sslContext = SSLContext.getInstance("TLS");

	            // Создаем TrustManager, который доверяет всем сертификатам (для тестирования)
	            TrustManager[] trustAllCerts = new TrustManager[] {
	                new X509TrustManager() {
	                    public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
	                    public void checkClientTrusted(X509Certificate[] certs, String authType) {}
	                    public void checkServerTrusted(X509Certificate[] certs, String authType) {}
	                }
	            };

	            sslContext.init(null, trustAllCerts, new SecureRandom());

	            // Устанавливаем SSLContext по умолчанию
	            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

	            // Опционально, настройка хэндлера проверки hostname
	            HostnameVerifier allHostsValid = (hostname, session) -> true; // доверять всем хостам
	            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

	            // Создаем соединение
	            URL url = new URL(urlStr);
	            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

	            conn.connect();
	            System.err.println("Certs: "+conn.getServerCertificates().length);
	            
	            // Настройка соединения
	            conn.setRequestMethod("POST");
	            conn.setDoOutput(true);
	            conn.setConnectTimeout(15000);
	            conn.setReadTimeout(15000);
	            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

	            // Отправка POST данных
	            try (OutputStream os = conn.getOutputStream()) {
	                byte[] input = postData.getBytes("UTF-8");
	                os.write(input, 0, input.length);
	            }

	            // Получение ответа
	            int responseCode = conn.getResponseCode();
	            InputStream is;
	            if (responseCode >= 200 && responseCode < 300) {
	                is = conn.getInputStream();
	            } else {
	                is = conn.getErrorStream();
	            }
	            try (BufferedReader in = new BufferedReader(new InputStreamReader(is))) {
	                String line;
	                StringBuilder response = new StringBuilder();
	                while ((line = in.readLine()) != null) {
	                    response.append(line).append("\n");
	                }
	                System.err.println("Ответ сервера: " + response.toString());
	            }

	            conn.disconnect();

	        } catch (NoSuchAlgorithmException | KeyManagementException | IOException e) {
	            e.printStackTrace();
	        }	
	}
}
