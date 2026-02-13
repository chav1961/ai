package chav1961.ai.voice;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URI;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.Gson;

import chav1961.ai.voice.interfaces.VoiceConnectorInterface;
import chav1961.ai.voice.json.response.OAuthResponse;
import chav1961.ai.voice.json.response.RecognizeResponse;
import chav1961.purelib.basic.Utils;


// Client ID: 019bbad9-ac46-7822-943c-f9836c81cb81
// Scope: SALUTE_SPEECH_PERS
// Key: MDE5YmJhZDktYWM0Ni03ODIyLTk0M2MtZjk4MzZjODFjYjgxOjg1YWEyMWM0LTA4YTItNGE0MS1iYjhlLTMzMDNlZTNlNDdlMA==
public class VoiceConnector implements VoiceConnectorInterface {
	private static final Gson 		GSON = new Gson();
	private static final String 	ENCODING = "UTF-8";
	private static final String		OAUTH_PATH = "/api/v2/oauth";
	private static final String		RECOGNIZE_PATH = "/rest/v1/speech:recognize";	
	private static final String		SYNTHESIZE_PATH = "/rest/v1/text:synthesize";
	private static final URI		DEBUG_OAUTH_URI = URI.create("https://ngw.devices.sberbank.ru:9443");
	private static final URI		DEBUG_RECORNIZE_URI = URI.create("https://smartspeech.sber.ru");
	private static final String		DEBUG_PERSONAL_TOKEN = "MDE5YmJhZDktYWM0Ni03ODIyLTk0M2MtZjk4MzZjODFjYjgxOjg1YWEyMWM0LTA4YTItNGE0MS1iYjhlLTMzMDNlZTNlNDdlMA==";
	
    private final URI 		serverOAuthUri;
    private final URI 		serverUri;
    private final String	token;
    private final Proxy		proxy;
    private String			accessToken;
    private long 			expiration = 0; 

    /**
     * <p>Constructor of the debug instance</p>
     */
    public VoiceConnector() throws NullPointerException {
    	this(Proxy.NO_PROXY);
    }    

    /**
     * <p>Constructor of the debug instance</p>
     * @param proxy proxy to use. Can't be null
     */
    public VoiceConnector(final Proxy proxy) throws NullPointerException {
    	this(DEBUG_OAUTH_URI, DEBUG_RECORNIZE_URI, DEBUG_PERSONAL_TOKEN, proxy);
    }    
    
    /**
     * <p>Constructor of the class instance</p>
     * @param oAuthUri uri to make OAuth. Can't be null 
     * @param uri uri to recognize/speech. Can't be null
     * @param token Identification token to use. Can't be null
     * @param proxy proxy to use. Can't be null
     * @throws NullPointerException
     */
    public VoiceConnector(final URI oAuthUri, final URI uri, final String token, final Proxy proxy) throws NullPointerException {
    	if (oAuthUri == null) {
    		throw new NullPointerException("Server oAuth URI can't be null");
    	}
    	else if (uri == null) {
    		throw new NullPointerException("Server URI can't be null");
    	}
    	else if (Utils.checkEmptyOrNullString(token)) {
    		throw new IllegalArgumentException("TOken string can be neither null nor empty");
    	}
    	else if (proxy == null) {
    		throw new NullPointerException("Proxy descrptor can't be null");
    	}
    	else {
            this.serverUri = uri;
            this.serverOAuthUri = oAuthUri;
            this.token = token;
            this.proxy = proxy;
    	}
    }
    
    /**
     * <p>Recognize content and return it's string representation</p>
     * @param audioSource content to recognize. Can't be null and must have "audio/x-pcm;bit=16;rate=16000" format (*.wav file)
     * @return recognized string. Can't be null but can be empty
     * @throws IOException on any I/O errors
     */
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
            final HttpURLConnection connection = (HttpURLConnection) server.openConnection(proxy);
            
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
    
	public InputStream vocalize(final CharSequence source) throws IOException { 
    	if (Utils.checkEmptyOrNullString(source)) {
    		throw new IllegalArgumentException("Source string can  be neither null nor empty");
    	}
    	else {
    		if (System.currentTimeMillis() > expiration) {
    			try {
					expiration = queryAccessToken();
				} catch (KeyManagementException | NoSuchAlgorithmException e) {
					throw new IOException(e);
				}
    		}
        	final URL	server = URI.create(serverUri.toString()+SYNTHESIZE_PATH).toURL(); 
            final HttpURLConnection connection = (HttpURLConnection) server.openConnection(proxy);
            
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/text");
            connection.setRequestProperty("Accept", "audio/x-wav");
            connection.setRequestProperty("Authorization", "Bearer "+accessToken);

            try(final OutputStream 	os = connection.getOutputStream();
            	final Writer		wr = new OutputStreamWriter(os)) {
            	
            	Utils.copyStream(new StringReader(new StringBuilder(source).toString()), wr);
            }
            final int responseCode = connection.getResponseCode();
            
            if (responseCode >= 200 && responseCode < 300) {
                try(final InputStream 	is = connection.getInputStream();
                	final ByteArrayOutputStream	baos = new ByteArrayOutputStream()){
                	
                	Utils.copyStream(is, baos);
    				
    				return new ByteArrayInputStream(baos.toByteArray());
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

    private long queryAccessToken() throws IOException, NoSuchAlgorithmException, KeyManagementException {
        final UUID			connId = UUID.randomUUID();
        final String		body = "scope=SALUTE_SPEECH_PERS"; 
    	final URL			server = URI.create(serverOAuthUri.toString()+OAUTH_PATH).toURL(); 
        final HttpsURLConnection connection = (HttpsURLConnection) server.openConnection(proxy);
        
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("RqUID", ""+connId);
        connection.setRequestProperty("Authorization", "Basic "+token);

        try(final OutputStream 	os = connection.getOutputStream();
        	final Writer		wr = new OutputStreamWriter(os, ENCODING)) {
        	
        	Utils.copyStream(new StringReader(body), wr);
        }
        final int responseCode = connection.getResponseCode();
        
        if (responseCode >= 200 && responseCode < 300) {
            try(final InputStream 	is = connection.getInputStream();
				final Reader 		rdr = new InputStreamReader(is, ENCODING)){
				final OAuthResponse	resp = GSON.fromJson(rdr, OAuthResponse.class);
				
				this.accessToken = resp.access_token; 
				return resp.expires_at;
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
