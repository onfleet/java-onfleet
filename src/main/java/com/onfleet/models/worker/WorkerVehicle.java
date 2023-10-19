package com.onfleet.models.worker;

import com.onfleet.models.VehicleType;

public class WorkerVehicle {
	private VehicleType type;
	private String description;
	private String licensePlate;
	private String color;

	public WorkerVehicle(VehicleType type, String description, String licensePlate, String color) {
		this.type = type;
		this.description = description;
		this.licensePlate = licensePlate;
		this.color = color;
	}

	public VehicleType getType() {
		return type;
	}

	public void setType(VehicleType type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLicensePlate() {
		return licensePlate;
	}

	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
}