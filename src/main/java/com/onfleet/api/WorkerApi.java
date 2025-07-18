package com.onfleet.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.onfleet.exceptions.ApiException;
import com.onfleet.models.Metadata;
import com.onfleet.models.worker.Worker;
import com.onfleet.models.worker.WorkerCreateParams;
import com.onfleet.models.worker.WorkerFilterFields;
import com.onfleet.models.worker.WorkerListQueryParams;
import com.onfleet.models.worker.WorkerQueryParams;
import com.onfleet.models.worker.WorkerScheduleEntries;
import com.onfleet.models.worker.WorkerScheduleEntry;
import com.onfleet.models.worker.WorkerStates;
import com.onfleet.models.worker.WorkerTasks;
import com.onfleet.models.worker.WorkerTasksQueryParams;
import com.onfleet.models.worker.WorkerUpdateParams;
import com.onfleet.models.worker.Workers;
import com.onfleet.utils.GsonSingleton;
import com.onfleet.utils.HttpMethodType;
import com.onfleet.utils.MediaTypes;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.util.List;
import java.util.stream.Collectors;

public class WorkerApi extends BaseApi {

	public WorkerApi(OkHttpClient client) {
		super(client, "/workers");
	}

	/**
	 * Create a new worker based on the provided WorkerCreateParams.
	 * <a href="https://docs.onfleet.com/reference/create-worker">Api Docs</a>
	 *
	 * @param worker A WorkerCreateParams object containing the parameters for creating the worker.
	 * @return A Worker object representing the newly created worker.
	 * @throws ApiException If an error occurs during the API request or response handling.
	 */
	public Worker createWorker(WorkerCreateParams worker) throws ApiException {
		String jsonPayload = GsonSingleton.getInstance().toJson(worker);
		RequestBody body = RequestBody.create(jsonPayload, MediaTypes.JSON);
		Response response = sendRequest(HttpMethodType.POST, body, baseUrl);
		return handleResponse(response, Worker.class);
	}

	/**
	 * Retrieve a list of workers based on query parameters. This method allows you to obtain a list of workers with optional filters.
	 * <a href="https://docs.onfleet.com/reference/list-workers">Api Docs</a>
	 *
	 * @param params WorkerListQueryParams object containing filtering options.
	 * @return A list of Worker objects representing the workers that match the specified filters.
	 * @throws ApiException If an error occurs during the API request or response handling.
	 */
	public List<Worker> listWorkers(WorkerListQueryParams params) throws ApiException {
		if (params == null) {
			return handleResponse(sendRequest(HttpMethodType.GET, baseUrl), new TypeToken<List<Worker>>() {
			}.getType());
		}
		HttpUrl.Builder urlBuilder = HttpUrl.parse(baseUrl).newBuilder();
		if (params.getFields() != null && !params.getFields().isEmpty()) {
			urlBuilder.addQueryParameter("fields", params
					.getFields()
					.stream()
					.map(WorkerFilterFields::getValue)
					.collect(Collectors.joining(",")));
		}
		if (params.getTeamIds() != null && !params.getTeamIds().isEmpty()) {
			urlBuilder.addQueryParameter("teamsIds", String.join(",", params.getTeamIds()));
		}
		if (params.getWorkerStates() != null && !params.getWorkerStates().isEmpty()) {
			urlBuilder.addQueryParameter("states", params.getWorkerStates()
					.stream()
					.map(WorkerStates::getValue)
					.map(Object::toString)
					.collect(Collectors.joining(",")));
		}
		if (params.getPhoneNumbers() != null && !params.getPhoneNumbers().isEmpty()) {
			urlBuilder.addQueryParameter("phoneNumbers", String.join(",", params.getPhoneNumbers()));
		}
		if (Boolean.TRUE.equals(params.getIncludePasswordDetails())) {
			urlBuilder.addQueryParameter("includePasswordDetails", "true");
		}
		Response response = sendRequest(HttpMethodType.GET, urlBuilder.toString());
		return handleResponse(response, new TypeToken<List<Worker>>() {
		}.getType());
	}

	/**
	 * Retrieve a list of workers.
	 * <a href="https://docs.onfleet.com/reference/list-workers">Api Docs</a>
	 *
	 * @return A list of Worker objects representing the workers that match the specified filters.
	 * @throws ApiException If an error occurs during the API request or response handling.
	 */
	public List<Worker> listWorkers() throws ApiException {
		return listWorkers(null);
	}

	/**
	 * Retrieve tasks assigned to a specific worker based on query parameters. This method allows you to obtain tasks assigned to a worker with optional filters.
	 * <a href="https://docs.onfleet.com/reference/list-workers-assigned-tasks">Api Docs</a>
	 *
	 * @param workerId The ID of the worker for whom you want to retrieve tasks.
	 * @param params   WorkerTasksQueryParams object containing filtering options.
	 * @return A WorkerTasks object representing the tasks assigned to the specified worker.
	 * @throws ApiException If an error occurs during the API request or response handling.
	 */
	public WorkerTasks getWorkersTasks(String workerId, WorkerTasksQueryParams params) throws ApiException {
		String url = String.format("%s/%s/tasks", baseUrl, workerId);
		HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
		if (params.getFrom() != null)
			urlBuilder.addQueryParameter("from", params.getFrom().toString());
		if (params.getTo() != null)
			urlBuilder.addQueryParameter("to", params.getTo().toString());
		if (params.getLastId() != null)
			urlBuilder.addQueryParameter("lastId", params.getLastId());
		if (params.getPickUpTask() != null)
			urlBuilder.addQueryParameter("isPickUpTask", params.getPickUpTask().toString());
		Response response = sendRequest(HttpMethodType.GET, urlBuilder.build().toString());
		return handleResponse(response, WorkerTasks.class);
	}

	/**
	 * Retrieve tasks assigned to a specific worker without applying additional filters. This method allows you to obtain all tasks assigned to a worker.
	 * <a href="https://docs.onfleet.com/reference/list-workers-assigned-tasks">Api Docs</a>
	 *
	 * @param workerId The ID of the worker for whom you want to retrieve tasks.
	 * @return A WorkerTasks object representing all tasks assigned to the specified worker.
	 * @throws ApiException If an error occurs during the API request or response handling.
	 */
	public WorkerTasks getWorkersTasks(String workerId) throws ApiException {
		return this.getWorkersTasks(workerId, new WorkerTasksQueryParams.Builder().build());
	}

	/**
	 * Retrieve workers based on their geographical location. This method allows you to obtain a list of workers within a specified radius of a location.
	 * <a href="https://docs.onfleet.com/reference/get-workers-by-location">Api Docs</a>
	 *
	 * @param longitude The longitude coordinate of the location.
	 * @param latitude  The latitude coordinate of the location.
	 * @param radius    The radius within which to search for workers (optional).
	 * @return A Workers object representing the workers within the specified radius of the location.
	 * @throws ApiException If an error occurs during the API request or response handling.
	 */
	public Workers getWorkersByLocation(Double longitude, Double latitude, Integer radius) throws ApiException {
		HttpUrl.Builder urlBuilder = HttpUrl.parse(baseUrl + "/location").newBuilder();
		urlBuilder.addQueryParameter("longitude", longitude.toString());
		urlBuilder.addQueryParameter("latitude", latitude.toString());
		if (radius != null) {
			urlBuilder.addQueryParameter("radius", radius.toString());
		}
		Response response = sendRequest(HttpMethodType.GET, urlBuilder.build().toString());
		return handleResponse(response, Workers.class);
	}

	/**
	 * Retrieve details of a specific worker based on worker ID and query parameters. This method allows you to obtain worker details with optional filters.
	 * <a href="https://docs.onfleet.com/reference/get-single-worker">Api Docs</a>
	 *
	 * @param workerId The ID of the worker you want to retrieve.
	 * @param params   WorkerQueryParams object containing filtering options.
	 * @return A Worker object representing the details of the specified worker.
	 * @throws ApiException If an error occurs during the API request or response handling.
	 */
	public Worker getSingleWorker(String workerId, WorkerQueryParams params) throws ApiException {
		String url = String.format("%s/%s", baseUrl, workerId);
		HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
		if (params.getFilterFields() != null && !params.getFilterFields().isEmpty()) {
			String filterQuery = params.getFilterFields().stream()
					.map(WorkerFilterFields::getValue)
					.collect(Collectors.joining(","));
			urlBuilder.addQueryParameter("filter", filterQuery);
		}
		if (params.getPhones() != null && !params.getPhones().isEmpty()) {
			urlBuilder.addQueryParameter("phones", String.join(",", params.getPhones()));
		}
		if (params.getStates() != null) {
			urlBuilder.addQueryParameter("states", params.getStates()
					.stream()
					.map(Object::toString)
					.collect(Collectors.joining(",")));
		}
		if (params.getTeams() != null) {
			urlBuilder.addQueryParameter("teams", String.join(",", params.getTeams()));
		}
		if (params.getEnableAnalytics() != null) {
			urlBuilder.addQueryParameter("analytics", params.getEnableAnalytics().toString());
		}
		Response response = sendRequest(HttpMethodType.GET, urlBuilder.build().toString());
		return handleResponse(response, Worker.class);
	}

	/**
	 * Query workers with metadata. This method allows you to search for workers based on metadata.
	 * <a href="https://docs.onfleet.com/reference/metadata">Api Docs</a>
	 *
	 * @param metadata A list of Metadata objects for filtering workers based on custom metadata.
	 * @return A list of Worker objects that match the specified metadata.
	 * @throws ApiException If an error occurs during the API request or response handling.
	 */
	public List<Worker> queryWithMetadata(List<Metadata> metadata) throws ApiException {
		String url = String.format("%s/metadata", baseUrl);
		String jsonPayload = GsonSingleton.getInstance().toJson(metadata);
		RequestBody body = RequestBody.create(jsonPayload, MediaTypes.JSON);
		Response response = sendRequest(HttpMethodType.POST, body, url);
		return handleResponse(response, new TypeToken<List<Worker>>() {
		}.getType());
	}

	/**
	 * Update a worker's details based on their worker ID and WorkerUpdateParams.
	 * <a href="https://docs.onfleet.com/reference/update-worker">Api Docs</a>
	 *
	 * @param workerId The ID of the worker you want to update.
	 * @param params   WorkerUpdateParams object containing the updated worker details.
	 * @return A Worker object representing the updated worker details.
	 * @throws ApiException If an error occurs during the API request or response handling.
	 */
	public Worker updateWorker(String workerId, WorkerUpdateParams params) throws ApiException {
		String url = String.format("%s/%s", baseUrl, workerId);
		String jsonPayload = GsonSingleton.getInstance().toJson(params);
		RequestBody body = RequestBody.create(jsonPayload, MediaTypes.JSON);
		Response response = sendRequest(HttpMethodType.PUT, body, url);
		return handleResponse(response, Worker.class);
	}

	/**
	 * Delete a worker based on their worker ID.
	 * <a href="https://docs.onfleet.com/reference/delete-worker">Api Docs</a>
	 *
	 * @param workerId The ID of the worker to delete.
	 * @throws ApiException If an error occurs during the API request or response handling.
	 */
	public void deleteWorker(String workerId) throws ApiException {
		sendRequest(HttpMethodType.DELETE, String.format("%s/%s", baseUrl, workerId));
	}

	/**
	 * Retrieve the schedule entries for a specific worker.
	 * <a href="https://docs.onfleet.com/reference/get-workers-schedule">Api Docs</a>
	 *
	 * @param workerId The ID of the worker for whom you want to retrieve the schedule.
	 * @return A WorkerScheduleEntries object representing the work schedule entries of the specified worker.
	 * @throws ApiException If an error occurs during the API request or response handling.
	 */
	public WorkerScheduleEntries getWorkerSchedule(String workerId) throws ApiException {
		Response response = sendRequest(HttpMethodType.GET, String.format("%s/%s/schedule", baseUrl, workerId));
		return handleResponse(response, WorkerScheduleEntries.class);
	}

	/**
	 * Set the work schedule entries for a specific worker.
	 * <a href="https://docs.onfleet.com/reference/set-workers-schedule">Api Docs</a>
	 *
	 * @param workerId The ID of the worker for whom you want to set the schedule.
	 * @param entries  A list of WorkerScheduleEntry objects representing the work schedule entries.
	 * @return A WorkerScheduleEntries object representing the updated work schedule entries of the specified worker.
	 * @throws ApiException If an error occurs during the API request or response handling.
	 */
	public WorkerScheduleEntries setWorkerSchedule(String workerId, List<WorkerScheduleEntry> entries) throws ApiException {
		String url = String.format("%s/%s/schedule", baseUrl, workerId);
		JsonObject jsonPayload = new JsonObject();
		JsonArray entriesArray = new JsonArray();

		for (WorkerScheduleEntry entry : entries) {
			JsonObject entryObject = new JsonObject();
			entryObject.addProperty("date", entry.getDate());
			entryObject.addProperty("timezone", entry.getTimezone());

			JsonArray shiftsArray = new JsonArray();
			for (List<Long> shift : entry.getShifts()) {
				JsonArray shiftArray = new JsonArray();
				shiftArray.add(shift.get(0));
				shiftArray.add(shift.get(1));
				shiftsArray.add(shiftArray);
			}
			entryObject.add("shifts", shiftsArray);
			entriesArray.add(entryObject);
		}
		jsonPayload.add("entries", entriesArray);
		RequestBody body = RequestBody.create(jsonPayload.toString(), MediaTypes.JSON);
		Response response = sendRequest(HttpMethodType.POST, body, url);
		return handleResponse(response, WorkerScheduleEntries.class);
	}

}
