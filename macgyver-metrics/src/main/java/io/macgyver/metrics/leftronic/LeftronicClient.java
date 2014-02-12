package io.macgyver.metrics.leftronic;

import io.macgyver.core.MacGyverException;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHandler.STATE;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.HttpResponseStatus;
import com.ning.http.client.Response;

public class LeftronicClient {

	public static Logger logger = LoggerFactory.getLogger(LeftronicClient.class);
	public static final String DEFAULT_URL = "https://www.leftronic.com/customSend/";
	String url = DEFAULT_URL;
	String apiKey;
	String prefix;

	@Autowired
	AsyncHttpClient client;
	
	public void LeftronicClient() {

	}
	
	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}


	public String qualifyStreamName(String stream) {
		if (!Strings.isNullOrEmpty(prefix)) {
			return prefix+"."+stream;
		}
		else {
			return stream;
		}
	}
	public void send(String streamName, long val) {

	
		try {
			Gson gson = new Gson();

			JsonObject data = new JsonObject();
			data.addProperty("accessKey", apiKey);
			data.addProperty("streamName", qualifyStreamName(streamName));
			data.addProperty("point", val);
			logger.trace("sending data leftronic: {}",data);
		

			AsyncCompletionHandler<String> h = new AsyncCompletionHandler<String>() {

			

				@Override
				public void onThrowable(Throwable t) {
					logger.warn("",t);
					super.onThrowable(t);
					
				}

				@Override
				public String onCompleted(Response response) throws Exception {
					logger.debug("sent metric to leftronic");
					return null;
				}
			};

			client.preparePost(url).setBody(data.toString())
					.execute(h);

		} catch (IOException e) {
			throw new MacGyverException(e);
		}
		finally {
		
		}

	}

}
