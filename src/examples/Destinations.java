public class Destinations {
	private static final Onfleet onfleet = new Onfleet("ApiKey");

	public void createDestination() {
		// https://docs.onfleet.com/reference/create-destination
		Address address = new Address.Builder()
				.setName("543")
				.setStreet("Howard St")
				.setApartment("5th Floor")
				.setCity("San Francisco")
				.setState("CA")
				.setCountry("USA")
				.build();
		DestinationCreateParams params = new DestinationCreateParams.Builder(address)
				.setNotes("Don't forget to check out the epic rooftop.")
				.build();
		onfleet.getDestinationApi().createDestination(params);
	}

	public void getDestination() {
		// https://docs.onfleet.com/reference/get-single-destination
		onfleet.getDestinationApi().getDestination("0i~RR0SUIculbRFsIse6MENg");
	}

}
