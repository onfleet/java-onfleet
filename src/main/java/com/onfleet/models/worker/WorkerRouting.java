package com.onfleet.models.worker;

import com.onfleet.models.destination.Address;

import java.util.List;

public class WorkerRouting {
	private final Address address;
	private final Boolean createdByLocation;
	private final String googlePlaceId;
	private final String id;
	private final List<Double> location;
	private final String notes;
	private final String organization;
	private final Long timeCreated;
	private final Long timeLastModified;
	private final Boolean wasGeocoded;

	private WorkerRouting(Builder builder) {
		address = builder.address;
		createdByLocation = builder.createdByLocation;
		googlePlaceId = builder.googlePlaceId;
		id = builder.id;
		location = builder.location;
		notes = builder.notes;
		organization = builder.organization;
		timeCreated = builder.timeCreated;
		timeLastModified = builder.timeLastModified;
		wasGeocoded = builder.wasGeocoded;
	}

	public Address getAddress() {
		return address;
	}

	public Boolean getCreatedByLocation() {
		return createdByLocation;
	}

	public String getGooglePlaceId() {
		return googlePlaceId;
	}

	public String getId() {
		return id;
	}

	public List<Double> getLocation() {
		return location;
	}

	public String getNotes() {
		return notes;
	}

	public String getOrganization() {
		return organization;
	}

	public Long getTimeCreated() {
		return timeCreated;
	}

	public Long getTimeLastModified() {
		return timeLastModified;
	}

	public Boolean getWasGeocoded() {
		return wasGeocoded;
	}

	public static class Builder {
		private Address address;
		private Boolean createdByLocation;
		private String googlePlaceId;
		private String id;
		private List<Double> location;
		private String notes;
		private String organization;
		private Long timeCreated;
		private Long timeLastModified;
		private Boolean wasGeocoded;

		public Builder setAddress(Address address) {
			this.address = address;
			return this;
		}

		public Builder setCreatedByLocation(Boolean createdByLocation) {
			this.createdByLocation = createdByLocation;
			return this;
		}

		public Builder setGooglePlaceId(String googlePlaceId) {
			this.googlePlaceId = googlePlaceId;
			return this;
		}

		public Builder setId(String id) {
			this.id = id;
			return this;
		}

		public Builder setLocation(List<Double> location) {
			this.location = location;
			return this;
		}

		public Builder setNotes(String notes) {
			this.notes = notes;
			return this;
		}

		public Builder setOrganization(String organization) {
			this.organization = organization;
			return this;
		}

		public Builder setTimeCreated(Long timeCreated) {
			this.timeCreated = timeCreated;
			return this;
		}

		public Builder setTimeLastModified(Long timeLastModified) {
			this.timeLastModified = timeLastModified;
			return this;
		}

		public Builder setWasGeocoded(Boolean wasGeocoded) {
			this.wasGeocoded = wasGeocoded;
			return this;
		}

		public WorkerRouting build() {
			return new WorkerRouting(this);
		}
	}

}
