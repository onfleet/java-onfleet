package com.onfleet.api;

import com.onfleet.exceptions.ApiException;
import com.onfleet.models.organization.Delegatee;
import com.onfleet.models.organization.Organization;
import com.onfleet.utils.HttpMethodType;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class OrganizationApi extends BaseApi {

	public OrganizationApi(OkHttpClient client) {
		super(client, "/organization");
	}

	public Organization getDetails() throws ApiException {
		Response response = sendRequest(HttpMethodType.GET, baseUrl);
		return handleResponse(response, Organization.class);
	}

	public Delegatee getDelegateeDetails(String id) throws ApiException {
		String url = String.format("%s/%s", baseUrl + "s", id);
		Response response = sendRequest(HttpMethodType.GET, url);
		return handleResponse(response, Delegatee.class);
	}
}
