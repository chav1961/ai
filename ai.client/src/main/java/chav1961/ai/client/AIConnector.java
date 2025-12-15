package chav1961.ai.client;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import com.google.gson.Gson;

import chav1961.ai.client.interfaces.APIAction;

/**
 * Класс для выполнения HTTP POST-запроса с JSON-объектом и получения ответа в виде экземпляра класса.
 * Использует только стандартные средства JRE.
 */
public class AIConnector {
	private static final String ENCODING = "UTF-8";
	private static final Gson GSON = new Gson();
	
    private final URI serverUri;

    /**
     * Конструктор принимает URL сервера.
     *
     * @param urlString строка URL сервера
     * @throws IOException если URL некорректен
     */
    public AIConnector(final URI uri) throws NullPointerException {
    	if (uri == null) {
    		throw new NullPointerException("Server URI can't be null");
    	}
    	else {
            this.serverUri = uri;
    	}
    }

    /**
     * Отправляет объект в формате JSON на сервер и получает ответ, преобразованный обратно в объект.
     *
     * @param responseClazz класс, в который нужно десериализовать ответ
     * @param <T>  тип возвращаемого объекта
     * @return экземпляр класса clazz, созданный из ответа
     * @throws IOException при ошибках сети или потоков
     */
    public <T> T call(final APIAction action, final Class<T> responseClazz) throws IOException {
    	if (action == null) {
    		throw new NullPointerException("Action can't be null");
    	}
    	else if (responseClazz == null) {
    		throw new NullPointerException("Response class can't be null");
    	}
    	else {
    		return call(action, null, responseClazz);
    	}
    }    
    
    /**
     * Отправляет объект в формате JSON на сервер и получает ответ, преобразованный обратно в объект.
     *
     * @param obj  объект, который нужно отправить (старайтесь подавать хорошо сериализуемый объект)
     * @param responseClazz класс, в который нужно десериализовать ответ
     * @param <T>  тип возвращаемого объекта
     * @return экземпляр класса clazz, созданный из ответа
     * @throws IOException при ошибках сети или потоков
     */
    public <T> T call(final APIAction action, final Object request, final Class<T> responseClazz) throws IOException {
    	if (action == null) {
    		throw new NullPointerException("Action can't be null");
    	}
    	else if (request == null && action.isDoOutput()) {
    		throw new NullPointerException("Request can't be null for action ["+action+"]");
    	}
    	else if (responseClazz == null) {
    		throw new NullPointerException("Response class can't be null");
    	}
    	else {
            final HttpURLConnection connection = (HttpURLConnection) getServerURL(action).openConnection();
            
            connection.setDoInput(action.isDoInput());
            connection.setRequestMethod(action.getMethod());
            connection.setDoOutput(action.isDoOutput());
            connection.setRequestProperty("Content-Type", "application/json; "+ENCODING);
            connection.setRequestProperty("Accept", "application/json");

            if (action.isDoOutput()) {
	            try(final OutputStream os = connection.getOutputStream();
	            	final Writer wr = new OutputStreamWriter(os, ENCODING)) {
	            	
	            	GSON.toJson(request, wr);
	            	wr.flush();
	            }
            }
            final int responseCode = connection.getResponseCode();
            
            if (responseCode >= 200 && responseCode < 300) {
                try(final InputStream is = connection.getInputStream();
					final Reader rdr = new InputStreamReader(is, ENCODING)){
					
				    return GSON.fromJson(rdr, responseClazz);
				}
            }
            else {
                try(final InputStream is = connection.getErrorStream();
					final Reader rdr = new InputStreamReader(is, ENCODING)){
					
				    return GSON.fromJson(rdr, responseClazz);
				}
            }
            
       	}
   }

	private URL getServerURL(final APIAction action) throws MalformedURLException {
		return serverUri.resolve(action.getPath()).toURL();
	}
}