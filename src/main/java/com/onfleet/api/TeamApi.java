package com.onfleet.api;

import com.google.gson.reflect.TypeToken;
import com.onfleet.exceptions.ApiException;
import com.onfleet.models.VehicleType;
import com.onfleet.models.team.*;
import com.onfleet.models.Metadata;
import com.onfleet.utils.GsonSingleton;
import com.onfleet.utils.HttpMethodType;
import com.onfleet.utils.MediaTypes;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.util.List;
import java.util.stream.Collectors;

public class TeamApi extends BaseApi {

	public TeamApi(OkHttpClient client) {
		super(client, "/teams");
	}

	/**
	 * Retrieve details about a specific team by its ID. This method allows you to obtain information about a team within your organization.
	 * <a href="https://docs.onfleet.com/reference/get-single-team">Api Docs</a>
	 * @param teamId The ID of the team to retrieve.
	 * @return Team object representing the details of the retrieved team.
	 * @throws ApiException If an error occurs during the API request or response handling.
	 */
	public Team getTeam(String teamId) throws ApiException {
		return handleResponse(
				sendRequest(HttpMethodType.GET,
						String.format("%s/%s", baseUrl, teamId)),
				Team.class);
	}

	/**
	 * Retrieve a list of teams. This method allows you to obtain a list of all teams in your organization.
	 * <a href="https://docs.onfleet.com/reference/list-teams">Api Docs</a>
	 * @return A list of Team objects representing the teams in your organization.
	 * @throws ApiException If an error occurs during the API request or response handling.
	 */
	public List<Team> listTeams() throws ApiException {
		return handleResponse(sendRequest(HttpMethodType.GET, baseUrl), new TypeToken<List<Team>>() {
		}.getType());
	}

	/**
	 * Create a new team based on the provided TeamCreateParams
	 * <a href="https://docs.onfleet.com/reference/create-team">Api Docs</a>
	 * @param team A TeamCreateParams object containing the parameters for creating the team.
	 * @return A Team object representing the newly created team.
	 * @throws ApiException If an error occurs during the API request or response handling.
	 */
	public Team createTeam(TeamCreateParams team) throws ApiException {
		String jsonPayload = GsonSingleton.getInstance().toJson(team);
		RequestBody body = RequestBody.create(jsonPayload, MediaTypes.JSON);
		Response response = sendRequest(HttpMethodType.POST, body, baseUrl);
		return handleResponse(response, Team.class);
	}

	/**
	 * Update an existing team based on the provided parameters.
	 * <a href="https://docs.onfleet.com/reference/update-team">Api Docs</a>
	 * @param teamId The ID of the team to update.
	 * @param params A TeamUpdateParams object containing the parameters for updating the team.
	 * @return A Team object representing the updated team.
	 * @throws ApiException If an error occurs during the API request or response handling.
	 */
	public Team updateTeam(String teamId, TeamUpdateParams params) throws ApiException {
		String url = String.format("%s/%s", baseUrl, teamId);
		String jsonPayload = GsonSingleton.getInstance().toJson(params);
		RequestBody body = RequestBody.create(jsonPayload, MediaTypes.JSON);
		Response response = sendRequest(HttpMethodType.PUT, body, url);
		return handleResponse(response, Team.class);
	}

	/**
	 * Delete a team based on its ID.
	 * <a href="https://docs.onfleet.com/reference/delete-team">Api Docs</a>
	 * @param teamId The ID of the team to delete.
	 * @throws ApiException If an error occurs during the API request or response handling.
	 */
	public void deleteTeam(String teamId) throws ApiException {
		sendRequest(HttpMethodType.DELETE,
				String.format("%s/%s", baseUrl, teamId));
	}

	/**
	 * Get a driver time estimate for a specific team and location.
	 * <a href="https://docs.onfleet.com/reference/delivery-estimate">Api Docs</a>
	 * @param teamId The ID of the team for which you want to estimate the driver's time.
	 * @param params A TeamDriverEtaQueryParams object containing location and other parameters for the estimate.
	 * @return A WorkerRoute object representing the estimated driver route and time.
	 * @throws ApiException If an error occurs during the API request or response handling, or if required parameters are missing.
	 */
	public WorkerRoute getDriverTimeEstimate(String teamId,
	                                         TeamDriverEtaQueryParams params) throws ApiException {
		if ((params.getDropoffLocation() == null || params.getDropoffLocation().isEmpty()) && (params.getPickupLocation() == null || params.getPickupLocation().isEmpty())) {
			throw new IllegalArgumentException("Request must have at least one of dropoffLocation or pickupLocation.");
		}
		String url = String.format("%s/%s/estimate", baseUrl, teamId);
		HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
		if (params.getDropoffLocation() != null && !params.getDropoffLocation().isEmpty())
			urlBuilder.addQueryParameter("dropoffLocation", params.getDropoffLocation());
		if (params.getPickupLocation() != null && !params.getPickupLocation().isEmpty())
			urlBuilder.addQueryParameter("pickupLocation", params.getPickupLocation());
		if (params.getPickupTime() != null)
			urlBuilder.addQueryParameter("pickupTime", params.getPickupTime().toString());
		if (params.getRestrictedVehicleTypes() != null && !params.getRestrictedVehicleTypes().isEmpty()) {
			String restrictedTypesString = params.getRestrictedVehicleTypes().stream()
					.map(VehicleType::getValue)
					.collect(Collectors.joining(","));
			urlBuilder.addQueryParameter("restrictedVehiclesTypes", restrictedTypesString);
		}
		if (params.getServiceTime() != null)
			urlBuilder.addQueryParameter("serviceTime", params.getServiceTime().toString());
		Response response = sendRequest(HttpMethodType.GET, urlBuilder.build().toString());
		return handleResponse(response, WorkerRoute.class);
	}

	/**
	 * Retrieve unassigned tasks for a specific team.
	 * <a href="https://docs.onfleet.com/reference/list-tasks-in-team">Api Docs</a>
	 * @param teamId The ID of the team for which you want to retrieve unassigned tasks.
	 * @param params A TeamTasksQueryParams object containing optional parameters for filtering the unassigned tasks.
	 * @return A TeamTasks object representing the unassigned tasks for the team.
	 * @throws ApiException If an error occurs during the API request or response handling.
	 */
	public TeamTasks getUnassignedTasks(String teamId, TeamTasksQueryParams params) throws ApiException {
		String url = String.format("%s/%s/tasks", baseUrl, teamId);
		HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
		if (params.getIsPickupTask() != null) {
			urlBuilder.addQueryParameter("isPickupTask", params.getIsPickupTask().toString());
		}
		if (params.getFrom() != null) {
			urlBuilder.addQueryParameter("from", params.getFrom().toString());
		}
		if (params.getTo() != null) {
			urlBuilder.addQueryParameter("to", params.getTo().toString());
		}
		if (params.getLastId() != null && !params.getLastId().isEmpty()) {
			urlBuilder.addQueryParameter("lastId", params.getLastId());
		}
		return handleResponse(sendRequest(HttpMethodType.GET, urlBuilder.build().toString()), TeamTasks.class);
	}

	/**
	 * Query teams with associated metadata.
	 * <a href="https://docs.onfleet.com/reference/metadata">Api Docs</a>
	 *
	 * @param metadata A list of Metadata objects containing personalized data to be used as query criteria.
	 * @return A list of Task objects matching the specified metadata query criteria.
	 * @throws ApiException If an error occurs during the API request or response handling.
	 */
	public List<Team> queryWithMetadata(List<Metadata> metadata) throws ApiException {
		String url = String.format("%s/metadata", baseUrl);
		String jsonPayload = GsonSingleton.getInstance().toJson(metadata);
		RequestBody body = RequestBody.create(jsonPayload, MediaTypes.JSON);
		Response response = sendRequest(HttpMethodType.POST, body, url);
		return handleResponse(response, new TypeToken<List<Team>>() {
		}.getType());
	}

}
