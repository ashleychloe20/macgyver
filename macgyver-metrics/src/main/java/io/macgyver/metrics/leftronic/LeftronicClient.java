package io.macgyver.metrics.leftronic;

import io.macgyver.core.MultiToolException;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;

public class LeftronicClient {

	public static Logger logger = LoggerFactory.getLogger(LeftronicClient.class);
	public static final String DEFAULT_URL = "https://www.leftronic.com/customSend/";
	String url = DEFAULT_URL;
	String apiKey;

	public LeftronicClient(String apiKey) {
		this.apiKey = apiKey;
	}
	public void send(String streamName, long val) {

		AsyncHttpClient asyncHttpClient = null;
		try {
			Gson gson = new Gson();

			JsonObject data = new JsonObject();
			data.addProperty("accessKey", apiKey);
			data.addProperty("streamName", streamName);
			data.addProperty("point", val);

			asyncHttpClient = new AsyncHttpClient();

			AsyncCompletionHandler<String> h = new AsyncCompletionHandler<String>() {

				@Override
				public String onCompleted(Response response) throws Exception {
					// TODO Auto-generated method stub
					logger.info("GOT IT!");
					return null;
				}
			};

			asyncHttpClient.preparePost(url).setBody(data.toString())
					.execute(h);

		} catch (IOException e) {
			throw new MultiToolException(e);
		}
		finally {
			if (asyncHttpClient!=null) {
				asyncHttpClient.close();
			}
		}

	}

}
