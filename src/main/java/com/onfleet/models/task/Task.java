package com.onfleet.models.task;
import com.onfleet.models.destination.Destination;
import com.onfleet.models.Metadata;
import com.onfleet.models.recipient.Recipient;

import java.util.List;

public class Task {
	private TaskAdditionalQuantities additionalQuantities;
	private TaskAppearance appearance;
	private TaskBarcodeContainer barcodes;
	private Long completeAfter;
	private Long completeBefore;
	private TaskCompletionDetails completionDetails;
	private TaskContainer container;
	private String creator;
	private Double delayTime;
	private List<String> dependencies;
	private Destination destination;
	private Long estimatedArrivalTime;
	private Long estimatedCompletionTime;
	private Long eta;
	private String executor;
	private Object feedback;
	private String id;
	private TaskIdentity identity;
	private String merchant;
	private List<Metadata> metadata;
	private String notes;
	private String organization;
	private TaskOverrides overrides;
	private Boolean pickupTask;
	private Double quantity;
	private List<Recipient> recipients;
	private Boolean scanOnlyRequiredBarcodes;
	private Double serviceTime;
	private String shortId;
	private String sourceTaskId;
	private TaskState state;
	private Long timeCreated;
	private Long timeLastModified;
	private String trackingUrl;
	private Boolean trackingViewed;
	private String worker;

	public TaskAdditionalQuantities getAdditionalQuantities() {
		return additionalQuantities;
	}

	public TaskAppearance getAppearance() {
		return appearance;
	}

	public TaskBarcodeContainer getBarcodes() {
		return barcodes;
	}

	public Long getCompleteAfter() {
		return completeAfter;
	}

	public Long getCompleteBefore() {
		return completeBefore;
	}

	public TaskCompletionDetails getCompletionDetails() {
		return completionDetails;
	}

	public TaskContainer getContainer() {
		return container;
	}

	public String getCreator() {
		return creator;
	}

	public Double getDelayTime() {
		return delayTime;
	}

	public List<String> getDependencies() {
		return dependencies;
	}

	public Destination getDestination() {
		return destination;
	}

	public Long getEstimatedArrivalTime() {
		return estimatedArrivalTime;
	}

	public Long getEstimatedCompletionTime() {
		return estimatedCompletionTime;
	}

	public Long getEta() {
		return eta;
	}

	public String getExecutor() {
		return executor;
	}

	public String getId() {
		return id;
	}

	public TaskIdentity getIdentity() {
		return identity;
	}

	public String getMerchant() {
		return merchant;
	}

	public List<Metadata> getMetadata() {
		return metadata;
	}

	public String getNotes() {
		return notes;
	}

	public String getOrganization() {
		return organization;
	}

	public TaskOverrides getOverrides() {
		return overrides;
	}

	public Boolean getPickupTask() {
		return pickupTask;
	}

	public Double getQuantity() {
		return quantity;
	}

	public List<Recipient> getRecipients() {
		return recipients;
	}

	public Boolean getScanOnlyRequiredBarcodes() {
		return scanOnlyRequiredBarcodes;
	}

	public Double getServiceTime() {
		return serviceTime;
	}

	public String getShortId() {
		return shortId;
	}

	public String getSourceTaskId() {
		return sourceTaskId;
	}

	public TaskState getState() {
		return state;
	}

	public Long getTimeCreated() {
		return timeCreated;
	}

	public Long getTimeLastModified() {
		return timeLastModified;
	}

	public String getTrackingUrl() {
		return trackingUrl;
	}

	public Boolean getTrackingViewed() {
		return trackingViewed;
	}

	public String getWorker() {
		return worker;
	}

	public Object getFeedback() {
		return feedback;
	}

}
