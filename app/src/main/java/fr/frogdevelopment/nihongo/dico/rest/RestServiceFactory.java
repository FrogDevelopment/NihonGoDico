package fr.frogdevelopment.nihongo.dico.rest;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class RestServiceFactory {

	// ******************************
	// Specific to each Retrofit Factory
	// ******************************
	private static final GsonConverterFactory GSON_CONVERTER_FACTORY = buildGsonConverterFactory();

	private static GsonConverterFactory buildGsonConverterFactory() {
		Gson gson = new GsonBuilder()
				.setDateFormat("yyyy-MM-dd\\'T\\'HH:mm:ss")
				.create();

		return GsonConverterFactory.create(gson);
	}

	private Retrofit retrofit;

	protected RestServiceFactory(String baseUrl) {
		retrofit = new Retrofit.Builder()
				.baseUrl(baseUrl)
				.addConverterFactory(GSON_CONVERTER_FACTORY)
				.client(getClientBuilder().build())
				.build();
	}

	protected <T> T getService(Class<T> clazz) {
		return retrofit.create(clazz);
	}

	@NonNull
	private OkHttpClient.Builder getClientBuilder() {
		return new OkHttpClient.Builder()
				.readTimeout(0, TimeUnit.SECONDS)
				.connectTimeout(0, TimeUnit.SECONDS);
	}

}
