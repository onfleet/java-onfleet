package com.onfleet.api;

import com.google.gson.reflect.TypeToken;
import com.onfleet.exceptions.ApiException;
import com.onfleet.models.Administrator;
import com.onfleet.models.AdministratorBuilder;
import com.onfleet.models.ErrorResponse;
import com.onfleet.utils.GsonSingleton;
import com.onfleet.utils.HttpMethodType;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.HttpURLConnection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class AdministratorApiTest extends BaseApiTest {
	private AdministratorApi administratorApi;

	@BeforeEach
	void setUp() throws Exception {
		HttpUrl baseUrl = mockWebServer.url("/admins");
		administratorApi = new AdministratorApi(okHttpClient);
		setBaseUrl(administratorApi, baseUrl.toString());
	}

	@Test
	void testCreateAdminSuccessfully() throws Exception {
		String mockResponseJson = "{\"id\": \"8AxaiKwMd~np7I*YP2NfukBE\", \"timeCreated\": 1455156651000, \"timeLastModified\": 1455156651779, \"organization\": \"yAM*fDkztrT3gUcz9mNDgNOL\", \"email\": \"cm@onf.lt\", \"type\": \"standard\", \"name\": \"Chelsea M\", \"isActive\": false, \"metadata\": [],\"isAccountOwner\": false, \"teams\": []}";
		MockResponse mockResponse = new MockResponse()
				.setResponseCode(HttpURLConnection.HTTP_OK)
				.setBody(mockResponseJson);
		Administrator administrator = new AdministratorBuilder().setName("name").setEmail("cm@onf.lt").setPhone("8885551234").setIsReadOnly(true).setType(Administrator.Type.SUPER).build();

		mockWebServer.enqueue(mockResponse);
		Administrator admin = administratorApi.createAdministrator(administrator);
		RecordedRequest recordedRequest = mockWebServer.takeRequest();

		assertEquals(HttpMethodType.POST.name(), recordedRequest.getMethod());
		assertEquals("/admins", recordedRequest.getPath());
		assertEquals("8AxaiKwMd~np7I*YP2NfukBE", admin.getId());
		Assertions.assertThat(admin).usingRecursiveComparison().isEqualTo(GsonSingleton.getInstance().fromJson(mockResponseJson, Administrator.class));
	}

	@Test
	void testCreateAdminDuplicatedEmail() throws Exception {
		String mockResponseJson = "{\"code\":\"InvalidContent\",\"message\":{\"error\":1004,\"message\":\"The values of one or more parameters break a uniqueness constraint.\",\"cause\":{\"type\":\"duplicateKey\",\"key\":\"unknown\",\"value\":\"unknown\"},\"request\":\"fbf5580f-d4bd-458b-950d-f7dd057afcc8\"}}";
		MockResponse mockResponse = new MockResponse()
				.setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST)
				.setBody(mockResponseJson);
		mockWebServer.enqueue(mockResponse);

		Administrator administrator = new AdministratorBuilder().setName("name").setEmail("cm@onf.lt").setPhone("8885551234").setIsReadOnly(true).setType(Administrator.Type.SUPER).build();
		ApiException apiException = assertThrows(ApiException.class, () -> administratorApi.createAdministrator(administrator));
		RecordedRequest recordedRequest = mockWebServer.takeRequest();

		assertEquals(HttpMethodType.POST.name(), recordedRequest.getMethod());
		assertEquals("/admins", recordedRequest.getPath());
		Assertions.assertThat(apiException.getErrorResponse())
				.usingRecursiveComparison()
				.isEqualTo(GsonSingleton.getInstance().fromJson(mockResponseJson, ErrorResponse.class));
	}

	@Test
	void testListAdmins() throws Exception {
		String mockResponseJson = "[{\"id\":\"tbVmEoNQwN9K92bsITQNUtWY\",\"timeCreated\":1455048957000,\"timeLastModified\":1455048957461,\"organization\":\"yAM*fDkztrT3gUcz9mNDgNOL\",\"email\":\"st@onfleet.com\",\"type\":\"super\",\"name\":\"Sergey Tupolev\",\"isActive\":true,\"isReadOnly\":false,\"phone\":\"+16505557710\",\"teams\":[\"Q4Ay4XaJ77rvYtrxQ2WJmqh~\"],\"metadata\":[]},{\"id\":\"jBOlwJTkN0JzfE9Rj5vEL~UT\",\"timeCreated\":1455048990000,\"timeLastModified\":1455048990578,\"organization\":\"yAM*fDkztrT3gUcz9mNDgNOL\",\"email\":\"is@onfleet.com\",\"type\":\"standard\",\"name\":\"Iñaki Smith\",\"isActive\":false,\"isReadOnly\":false,\"phone\":\"+14155556327\",\"teams\":[],\"metadata\":[]},{\"id\":\"8AxaiKwMd~np7I*YP2NfukBE\",\"timeCreated\":1455156651000,\"timeLastModified\":1455156651779,\"organization\":\"yAM*fDkztrT3gUcz9mNDgNOL\",\"email\":\"cm@onf.lt\",\"type\":\"standard\",\"name\":\"Chelsea M\",\"isActive\":false,\"isReadOnly\":true,\"teams\":[\"Q4Ay4XaJ77rvYtrxQ2WJmqh~\",\"JDu~fDkztr*6fUcz9mNDgNOL\"],\"metadata\":[]}]";
		MockResponse mockResponse = new MockResponse()
				.setResponseCode(HttpURLConnection.HTTP_OK)
				.setBody(mockResponseJson);
		mockWebServer.enqueue(mockResponse);

		List<Administrator> admins = administratorApi.listAdministrators();
		RecordedRequest recordedRequest = mockWebServer.takeRequest();

		assertEquals(HttpMethodType.GET.name(), recordedRequest.getMethod());
		assertEquals("/admins", recordedRequest.getPath());
		Assertions.assertThat(admins)
				.usingRecursiveComparison()
				.isEqualTo(GsonSingleton.getInstance().fromJson(mockResponseJson, new TypeToken<List<Administrator>>() {
				}.getType()));
	}

	@Test
	void testUpdateAdmin() throws Exception {
		String mockResponseJson = "{\"id\":\"8AxaiKwMd~np7I*YP2NfukBE\",\"timeCreated\":1455156651000,\"timeLastModified\":1455156652494,\"organization\":\"yAM*fDkztrT3gUcz9mNDgNOL\",\"email\":\"cm@onf.lt\",\"type\":\"standard\",\"name\":\"C Manning\",\"isActive\":false,\"metadata\":[]}";
		MockResponse mockResponse = new MockResponse()
				.setResponseCode(HttpURLConnection.HTTP_OK)
				.setBody(mockResponseJson);
		mockWebServer.enqueue(mockResponse);

		Administrator admin = new AdministratorBuilder().setName("New Admin super").setType(Administrator.Type.STANDARD).build();
		admin = administratorApi.updateAdministrator("8AxaiKwMd~np7I*YP2NfukBE", admin);
		RecordedRequest recordedRequest = mockWebServer.takeRequest();

		assertEquals(HttpMethodType.PUT.name(), recordedRequest.getMethod());
		assertEquals("/admins/8AxaiKwMd~np7I*YP2NfukBE", recordedRequest.getPath());
		Assertions.assertThat(admin)
				.usingRecursiveComparison()
				.isEqualTo(GsonSingleton.getInstance().fromJson(mockResponseJson, Administrator.class));
	}

	@Test
	void testUpdateInvalidAdminId() throws Exception {
		String mockResponseJson = "{\"code\":\"InvalidContent\",\"message\":{\"error\":1005,\"message\":\"The data types of one or more parameters are invalid.\",\"cause\":\"PuLjIsI8nF1xGU3vRWn2XA~Ta must be of type ObjectId\",\"request\":\"aa7d0676-5f2d-4baa-aa38-4285c2bdb438\"}}";
		MockResponse mockResponse = new MockResponse()
				.setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST)
				.setBody(mockResponseJson);
		mockWebServer.enqueue(mockResponse);

		Administrator admin = new AdministratorBuilder().setName("New Admin super").setType(Administrator.Type.STANDARD).build();
		ApiException exception = assertThrows(ApiException.class, () -> administratorApi.updateAdministrator("8AxaiKwMd~np7I*YP2NfukBE", admin));
		RecordedRequest recordedRequest = mockWebServer.takeRequest();

		assertEquals(HttpMethodType.PUT.name(), recordedRequest.getMethod());
		assertEquals("/admins/8AxaiKwMd~np7I*YP2NfukBE", recordedRequest.getPath());
		Assertions.assertThat(exception.getErrorResponse())
				.usingRecursiveComparison()
				.isEqualTo(GsonSingleton.getInstance().fromJson(mockResponseJson, ErrorResponse.class));
	}

	@Test
	void testUpdateAdminIdNonExistent() throws Exception {
		String mockResponseJson = "{\"code\":\"ResourceNotFound\",\"message\":{\"error\":1402,\"message\":\"The requested resource does not exist.\",\"request\":\"a9212a19-73f9-461a-ae76-cfa1da474d4e\"}}";
		MockResponse mockResponse = new MockResponse()
				.setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST)
				.setBody(mockResponseJson);
		mockWebServer.enqueue(mockResponse);

		Administrator admin = new AdministratorBuilder().setName("New Admin super").setType(Administrator.Type.STANDARD).build();
		ApiException exception = assertThrows(ApiException.class, () -> administratorApi.updateAdministrator("8AxaiKwMd~np7I*YP2NfukBE", admin));
		RecordedRequest recordedRequest = mockWebServer.takeRequest();

		assertEquals(HttpMethodType.PUT.name(), recordedRequest.getMethod());
		assertEquals("/admins/8AxaiKwMd~np7I*YP2NfukBE", recordedRequest.getPath());
		Assertions.assertThat(exception.getErrorResponse())
				.usingRecursiveComparison()
				.isEqualTo(GsonSingleton.getInstance().fromJson(mockResponseJson, ErrorResponse.class));
	}

	@Test
	void testDeleteAdmin() throws Exception {
		MockResponse mockResponse = new MockResponse()
				.setResponseCode(HttpURLConnection.HTTP_OK);
		mockWebServer.enqueue(mockResponse);

		administratorApi.deleteAdministrator("8AxaiKwMd~np7I*YP2NfukBE");
		RecordedRequest recordedRequest = mockWebServer.takeRequest();

		assertEquals(HttpMethodType.DELETE.name(), recordedRequest.getMethod());
		assertEquals("/admins/8AxaiKwMd~np7I*YP2NfukBE", recordedRequest.getPath());
	}

	@Test
	void testInvalidApiKey() throws Exception {
		String mockResponseJson = "{\"code\":\"InvalidCredentials\",\"message\":{\"error\":1102,\"message\":\"The API key provided is invalid.\",\"cause\":\"The API key provided is not valid. Please contact support@onfleet.com for further assistance.\",\"request\":\"be5bd42f-6c60-4d0b-85aa-97a611f945f2\"}}";
		MockResponse mockResponse = new MockResponse()
				.setResponseCode(HttpURLConnection.HTTP_UNAUTHORIZED)
				.setBody(mockResponseJson);
		mockWebServer.enqueue(mockResponse);

		ApiException apiException = assertThrows(ApiException.class, () -> administratorApi.listAdministrators());
		RecordedRequest recordedRequest = mockWebServer.takeRequest();

		assertEquals(HttpMethodType.GET.name(), recordedRequest.getMethod());
		assertEquals("/admins", recordedRequest.getPath());
		Assertions.assertThat(apiException.getErrorResponse())
				.usingRecursiveComparison()
				.isEqualTo(GsonSingleton.getInstance().fromJson(mockResponseJson, ErrorResponse.class));
	}

}