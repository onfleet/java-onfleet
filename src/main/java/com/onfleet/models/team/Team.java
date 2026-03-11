package com.onfleet.models.team;

import com.onfleet.models.Metadata;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Team {
	private String id;
	private String name;
	private List<String> workers;
	private List<String> managers;
	private List<Metadata> metadata;
	@SerializedName("hub")
	private String hubId;
	private Boolean enableSelfAssignment;
	private List<String> tasks;
	private Long timeCreated;
	private Long timeLastModified;

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List<String> getWorkers() {
		return workers;
	}

	public List<String> getManagers() {
		return managers;
	}

	public String getHubId() {
		return hubId;
	}

	public Boolean getEnableSelfAssignment() {
		return enableSelfAssignment;
	}

	public List<String> getTasks() {
		return tasks;
	}

	public Long getTimeCreated() {
		return timeCreated;
	}

	public Long getTimeLastModified() {
		return timeLastModified;
	}

	public List<Metadata> getMetadata() {
		return metadata;
	}
}
