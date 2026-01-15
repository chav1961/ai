package chav1961.ai.voice;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.net.http.HttpResponse.BodyHandlers;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.google.gson.Gson;

import chav1961.ai.voice.json.response.OAuthResponse;
import chav1961.ai.voice.json.response.RecognizeResponse;
import chav1961.purelib.basic.Utils;


// Client ID: 019bbad9-ac46-7822-943c-f9836c81cb81
// Scope: SALUTE_SPEECH_PERS
// Key: MDE5YmJhZDktYWM0Ni03ODIyLTk0M2MtZjk4MzZjODFjYjgxOjg1YWEyMWM0LTA4YTItNGE0MS1iYjhlLTMzMDNlZTNlNDdlMA==
public class VoiceConnector {
	private static final Gson 			GSON = new Gson();
	private static final String 		ENCODING = "UTF-8";
	private static final String			OAUTH_PATH = "/api/v2/oauth";
	private static final String			RECOGNIZE_PATH = "/rest/v1/speech:recognize";	
	
    private final URI 		serverOAuthUri;
    private final URI 		serverUri;
    private final String	token;
    private String			accessToken;
    private long 			expiration = 0; 

    public VoiceConnector() throws NullPointerException {
    	this(URI.create("https://ngw.devices.sberbank.ru:9443"),
    		 URI.create("https://smartspeech.sber.ru"),
    		 "MDE5YmJhZDktYWM0Ni03ODIyLTk0M2MtZjk4MzZjODFjYjgxOjg1YWEyMWM0LTA4YTItNGE0MS1iYjhlLTMzMDNlZTNlNDdlMA==");
    }    
    
    /**
     * Конструктор принимает URL сервера.
     *
     * @param urlString строка URL сервера
     * @throws IOException если URL некорректен
     */
    public VoiceConnector(final URI oAuthUri, final URI uri, final String token) throws NullPointerException {
    	if (oAuthUri == null) {
    		throw new NullPointerException("Server oAuth URI can't be null");
    	}
    	else if (uri == null) {
    		throw new NullPointerException("Server URI can't be null");
    	}
    	else if (Utils.checkEmptyOrNullString(token)) {
    		throw new IllegalArgumentException("TOken string can be neither null nor empty");
    	}
    	else {
            this.serverUri = uri;
            this.serverOAuthUri = oAuthUri;
            this.token = token;
    	}
    }
    
    public String recognize(final InputStream audioSource) throws IOException {
    	if (audioSource == null) {
    		throw new NullPointerException("Audio source can't be null");
    	}
    	else {
    		if (System.currentTimeMillis() > expiration) {
    			try {
					expiration = queryAccessToken();
				} catch (KeyManagementException | NoSuchAlgorithmException e) {
					throw new IOException(e);
				}
    		}
        	final URL	server = URI.create(serverUri.toString()+RECOGNIZE_PATH).toURL(); 
            final HttpURLConnection connection = (HttpURLConnection) server.openConnection(Proxy.NO_PROXY);
            
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "audio/x-pcm;bit=16;rate=16000");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Authorization", "Bearer "+accessToken);

            try(final OutputStream os = connection.getOutputStream()) {
            	Utils.copyStream(audioSource, os);
            }
            final int responseCode = connection.getResponseCode();
            
            if (responseCode >= 200 && responseCode < 300) {
                try(final InputStream 	is = connection.getInputStream();
    				final Reader 		rdr = new InputStreamReader(is, ENCODING)){
    				final RecognizeResponse	resp = GSON.fromJson(rdr, RecognizeResponse.class);
    				
    				return String.join(System.lineSeparator(), resp.result);
    			}
            }
            else {
                try(final InputStream is = connection.getErrorStream();
    				final Reader rdr = new InputStreamReader(is, ENCODING)){
                	
    			    throw new IOException(Utils.fromResource(rdr));
    			}
            }
    	}
    }
    
	public OutputStream vocalize(final CharSequence source) throws IOException { 
    	// TODO:
    	return null;
    }

    private long queryAccessToken() throws IOException, NoSuchAlgorithmException, KeyManagementException {
    	TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        // Не выполняем проверку клиентских сертификатов
                    }
                    @Override
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        // Не выполняем проверку серверных сертификатов
                    }
                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                }
            };

            // Инициализируем SSLContext с нашим TrustManager
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());

            // Устанавливаем созданный SSLContext по умолчанию для HTTPS-соединений
            javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    	
    	
    	
    	final HttpClient	cli = HttpClient.newHttpClient();
    	final URI			server = URI.create("https://ngw.devices.sberbank.ru:9443/api/v2/oauth"); 
        final UUID			connId = UUID.randomUUID();
        final String		body = "scope=SALUTE_SPEECH_PERS"; 
    	final HttpRequest	rq = HttpRequest.newBuilder()
    								.uri(server)
    								.header("Content-Type", "application/x-www-form-urlencoded")
    								.header("Accept", "application/json")
    								.header("RqUID", ""+connId)
    								.header("Authorization", "Basic "+token)
    								.POST(BodyPublishers.ofString(body))
    								.build();
    	HttpResponse<String> resp;
		try {
			resp = cli.send(rq, BodyHandlers.ofString());
			
	    	System.err.println("Content="+resp.body());
	    	return 0;
		} catch (InterruptedException e) {
			throw new IOException(e);
		}

	}
}
