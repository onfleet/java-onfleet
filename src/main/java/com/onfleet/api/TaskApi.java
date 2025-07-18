package com.onfleet.api;

import com.google.gson.reflect.TypeToken;
import com.onfleet.exceptions.ApiException;
import com.onfleet.models.Metadata;
import com.onfleet.models.task.AutomaticallyAssignTaskResult;
import com.onfleet.models.task.BatchJobStatus;
import com.onfleet.models.task.Task;
import com.onfleet.models.task.TaskAutoAssignMultiParams;
import com.onfleet.models.task.TaskBatchCreateResponseAsync;
import com.onfleet.models.task.TaskCloneOptions;
import com.onfleet.models.task.TaskCloneParams;
import com.onfleet.models.task.TaskForceCompletion;
import com.onfleet.models.task.TaskForceCompletionParams;
import com.onfleet.models.task.TaskListQueryParams;
import com.onfleet.models.task.TaskParams;
import com.onfleet.models.task.TaskParamsList;
import com.onfleet.models.task.TaskState;
import com.onfleet.models.task.Tasks;
import com.onfleet.models.task.TasksPaginated;
import com.onfleet.utils.GsonSingleton;
import com.onfleet.utils.HttpMethodType;
import com.onfleet.utils.MediaTypes;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TaskApi extends BaseApi {

	public TaskApi(OkHttpClient client) {
		super(client, "/tasks");
	}

	/**
	 * Creates a new Task
	 * <a href="https://docs.onfleet.com/reference/create-task">Api Docs</a>
	 *
	 * @param taskParams The TaskParams object containing the data for the new task.
	 * @return A Task object representing the newly created task.
	 * @throws ApiException If an error occurs during the API request or response handling.
	 */
	public Task createTask(TaskParams taskParams) throws ApiException {
		String jsonPayload = GsonSingleton.getInstance().toJson(taskParams);
		RequestBody body = RequestBody.create(jsonPayload, MediaTypes.JSON);
		Response response = sendRequest(HttpMethodType.POST, body, baseUrl);
		return handleResponse(response, Task.class);
	}

	/**
	 * Creates multiple tasks in batch
	 * <a href="https://docs.onfleet.com/reference/create-tasks-in-batch">Api Docs</a>
	 *
	 * @param tasks A list of TaskParams objects containing the data for the new tasks.
	 * @return A Tasks object representing the result of the batch task creation.
	 * @throws ApiException If an error occurs during the API request or response handling.
	 */
	public Tasks createTasksBatch(List<TaskParams> tasks) throws ApiException {
		TaskParamsList taskList = new TaskParamsList(tasks);
		String url = String.format("%s/batch", baseUrl);
		String jsonPayload = GsonSingleton.getInstance().toJson(taskList);
		RequestBody body = RequestBody.create(jsonPayload, MediaTypes.JSON);
		Response response = sendRequest(HttpMethodType.POST, body, url);
		return handleResponse(response, Tasks.class);
	}

	/**
	 * Creates multiple tasks asynchronously in batch
	 * <a href="https://docs.onfleet.com/reference/create-tasks-in-batch-async">Api Docs</a>
	 *
	 * @param tasks A list of TaskParams objects containing the data for the new tasks.
	 * @return A TaskBatchCreateResponseAsync object representing the asynchronous result of the batch task creation.
	 * @throws ApiException If an error occurs during the API request or response handling.
	 */
	public TaskBatchCreateResponseAsync createTasksBatchAsync(List<TaskParams> tasks) throws ApiException {
		TaskParamsList taskList = new TaskParamsList(tasks);
		String url = String.format("%s/batch-async", baseUrl);
		String jsonPayload = GsonSingleton.getInstance().toJson(taskList);
		RequestBody body = RequestBody.create(jsonPayload, MediaTypes.JSON);
		Response response = sendRequest(HttpMethodType.POST, body, url);
		return handleResponse(response, TaskBatchCreateResponseAsync.class);
	}

	/**
	 * Retrieves the status of a batch job
	 * <a href="https://docs.onfleet.com/reference/batch-job-status">Api Docs</a>
	 *
	 * @param jobId The ID of the batch job for which to retrieve the status.
	 * @return A BatchJobStatus object representing the status of the batch job.
	 * @throws ApiException If an error occurs during the API request or response handling.
	 */
	public BatchJobStatus getBatchJobStatus(String jobId) throws ApiException {
		String url = String.format("%s/batch/%s", baseUrl, jobId);
		Response response = sendRequest(HttpMethodType.GET, url);
		return handleResponse(response, BatchJobStatus.class);
	}

	/**
	 * Automatically assigns tasks to on-duty drivers
	 * <a href="https://docs.onfleet.com/reference/automatic-assignment">Api Docs</a>
	 *
	 * @param params A TaskAutoAssignMultiParams object containing the parameters for task auto-assignment.
	 * @return An AutomaticallyAssignTaskResult object representing the result of the auto-assignment process.
	 * @throws ApiException If an error occurs during the API request or response handling.
	 */
	public AutomaticallyAssignTaskResult autoAssign(TaskAutoAssignMultiParams params) throws ApiException {
		String url = String.format("%s/autoAssign", baseUrl);
		String jsonPayload = GsonSingleton.getInstance().toJson(params);
		RequestBody body = RequestBody.create(jsonPayload, MediaTypes.JSON);
		Response response = sendRequest(HttpMethodType.POST, body, url);
		return handleResponse(response, AutomaticallyAssignTaskResult.class);
	}

	/**
	 * Retrieve a list of tasks based on specified query parameters. This method allows you to filter and
	 * retrieve tasks that match various criteria, such as their states, worker, completion dates, and task dependencies.
	 * <a href="https://docs.onfleet.com/reference/list-tasks">Api Docs</a>
	 *
	 * @param queryParams A TaskListQueryParams object containing the query parameters for filtering tasks.
	 * @return A TasksPaginated object representing a paginated list of tasks matching the specified criteria.
	 * @throws ApiException If an error occurs during the API request or response handling, or if required parameters are missing.
	 */
	public TasksPaginated listTasks(TaskListQueryParams queryParams) throws ApiException {
		String url = String.format("%s/%s",baseUrl, "all");
		HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(url)).newBuilder();
		if (queryParams.getFrom() > 0) {
			urlBuilder.addQueryParameter("from", String.valueOf(queryParams.getFrom()));
		} else {
			throw new IllegalArgumentException("'from' parameter is required");
		}
		if (queryParams.getTo() != null) {
			urlBuilder.addQueryParameter("to", String.valueOf(queryParams.getTo()));
		}
		if (queryParams.getLastId() != null) {
			urlBuilder.addQueryParameter("lastId", queryParams.getLastId());
		}
		if (queryParams.getStates() != null) {
			urlBuilder.addQueryParameter("state", queryParams.getStates()
					.stream()
					.map(TaskState::getValue)
					.map(Object::toString)
					.collect(Collectors.joining(",")));
		}
		if (queryParams.getWorker() != null) {
			urlBuilder.addQueryParameter("worker", queryParams.getWorker());
		}
		if (queryParams.getCompleteBeforeBefore() != null) {
			urlBuilder.addQueryParameter("completeBeforeBefore", String.valueOf(queryParams.getCompleteBeforeBefore()));
		}
		if (queryParams.getCompleteAfterAfter() != null) {
			urlBuilder.addQueryParameter("completeAfterAfter", String.valueOf(queryParams.getCompleteAfterAfter()));
		}
		if (queryParams.getTaskDependencies() != null) {
			urlBuilder.addQueryParameter("dependencies", String.join(",", queryParams.getTaskDependencies()));
		}
		return handleResponse(sendRequest(HttpMethodType.GET, urlBuilder.build().toString()), TasksPaginated.class);
	}

	/**
	 * Query tasks with associated metadata.
	 * <a href="https://docs.onfleet.com/reference/metadata">Api Docs</a>
	 *
	 * @param metadata A list of Metadata objects containing personalized data to be used as query criteria.
	 * @return A list of Task objects matching the specified metadata query criteria.
	 * @throws ApiException If an error occurs during the API request or response handling.
	 */
	public List<Task> queryWithMetadata(List<Metadata> metadata) throws ApiException {
		String url = String.format("%s/metadata", baseUrl);
		String jsonPayload = GsonSingleton.getInstance().toJson(metadata);
		RequestBody body = RequestBody.create(jsonPayload, MediaTypes.JSON);
		Response response = sendRequest(HttpMethodType.POST, body, url);
		return handleResponse(response, new TypeToken<List<Task>>() {
		}.getType());
	}

	/**
	 * Retrieves a single task by its ID
	 * <a href="https://docs.onfleet.com/reference/get-single-task">Api Docs</a>
	 *
	 * @param taskId The ID of the task to retrieve.
	 * @return A Task object representing the retrieved task.
	 * @throws ApiException If an error occurs during the API request or response handling.
	 */
	public Task getSingleTask(String taskId) throws ApiException {
		String url = String.format("%s/%s", baseUrl, taskId);
		Response response = sendRequest(HttpMethodType.GET, url);
		return handleResponse(response, Task.class);
	}

	/**
	 * Retrieves a task by its short ID
	 * <a href="https://docs.onfleet.com/reference/get-single-task-by-shortid">Api Docs</a>
	 *
	 * @param taskShortId The short ID of the task to retrieve.
	 * @return A Task object representing the retrieved task.
	 * @throws ApiException If an error occurs during the API request or response handling.
	 */
	public Task getTaskByShortId(String taskShortId) throws ApiException {
		String url = String.format("%s/shortId/%s", baseUrl, taskShortId);
		Response response = sendRequest(HttpMethodType.GET, url);
		return handleResponse(response, Task.class);
	}

	/**
	 * Updates a task by its ID with the provided TaskParams object
	 * <a href="https://docs.onfleet.com/reference/update-task">Api Docs</a>
	 *
	 * @param taskId The ID of the task to update.
	 * @param task   The TaskParams object containing the updated task data.
	 * @return A Task object representing the updated task.
	 * @throws ApiException If an error occurs during the API request or response handling.
	 */
	public Task updateTask(String taskId, TaskParams task) throws ApiException {
		String url = String.format("%s/%s", baseUrl, taskId);
		String jsonPayload = GsonSingleton.getInstance().toJson(task);
		RequestBody body = RequestBody.create(jsonPayload, MediaTypes.JSON);
		Response response = sendRequest(HttpMethodType.PUT, body, url);
		return handleResponse(response, Task.class);
	}

	/**
	 * Marks a task as completed, providing completion details
	 * <a href="https://docs.onfleet.com/reference/complete-task">Api Docs</a>
	 *
	 * @param taskId            The ID of the task to mark as completed.
	 * @param completionDetails The TaskForceCompletionParams object containing completion details.
	 * @throws ApiException If an error occurs during the API request or response handling.
	 */
	public void completeTask(String taskId, TaskForceCompletionParams completionDetails) throws ApiException {
		TaskForceCompletion details = new TaskForceCompletion(completionDetails);
		String url = String.format("%s/%s/complete", baseUrl, taskId);
		String jsonPayload = GsonSingleton.getInstance().toJson(details);
		RequestBody body = RequestBody.create(jsonPayload, MediaTypes.JSON);
		sendRequest(HttpMethodType.POST, body, url);
	}

	/**
	 * Clones a task by its ID with the provided TaskCloneParams options
	 * <a href="https://docs.onfleet.com/reference/clone-task">Api Docs</a>
	 *
	 * @param taskId      The ID of the task to clone.
	 * @param cloneParams The TaskCloneParams object containing cloning options.
	 * @return A Task object representing the cloned task.
	 * @throws ApiException If an error occurs during the API request or response handling.
	 */
	public Task cloneTask(String taskId, TaskCloneParams cloneParams) throws ApiException {
		String url = String.format("%s/%s/clone", baseUrl, taskId);
		if (cloneParams != null) {
			TaskCloneOptions options = new TaskCloneOptions(cloneParams);
			String jsonPayload = GsonSingleton.getInstance().toJson(options);
			RequestBody body = RequestBody.create(jsonPayload, MediaTypes.JSON);
			Response response = sendRequest(HttpMethodType.POST, body, url);
			return handleResponse(response, Task.class);
		}
		Response response = sendRequest(HttpMethodType.POST, url);
		return handleResponse(response, Task.class);
	}

	public Task cloneTask(String taskId) throws ApiException {
		return this.cloneTask(taskId, null);
	}

	/**
	 * Deletes a task by its ID
	 * <a href="https://docs.onfleet.com/reference/delete-task">Api Docs</a>
	 *
	 * @param taskId The ID of the task to delete.
	 * @throws ApiException If an error occurs during the API request or response handling.
	 */
	public void deleteTask(String taskId) throws ApiException {
		String url = String.format("%s/%s", baseUrl, taskId);
		sendRequest(HttpMethodType.DELETE, url);
	}

}
