package com.onfleet.api;

import okhttp3.OkHttpClient;

public class ContainerApi {
	private OkHttpClient client;

	public ContainerApi(OkHttpClient client) {
		this.client = client;
	}
}