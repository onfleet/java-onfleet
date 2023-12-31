package com.onfleet.models.webhook;

public class WebhookCreateParams {
	private String name;
	private String url;
	private int trigger;
	private Integer threshold;

	private WebhookCreateParams(Builder builder) {
		name = builder.name;
		url = builder.url;
		trigger = builder.trigger;
		threshold = builder.threshold;
	}

	public WebhookCreateParams(String name, String url, int trigger, Integer threshold) {
		this.name = name;
		this.url = url;
		this.trigger = trigger;
		this.threshold = threshold;
	}

	public static class Builder {
		private String name;
		private String url;
		private int trigger;
		private Integer threshold;

		public Builder setName(String name) {
			this.name = name;
			return this;
		}

		public Builder setUrl(String url) {
			this.url = url;
			return this;
		}

		public Builder setTrigger(int trigger) {
			this.trigger = trigger;
			return this;
		}

		public Builder setThreshold(Integer threshold) {
			this.threshold = threshold;
			return this;
		}

		public WebhookCreateParams build() {
			return new WebhookCreateParams(this);
		}
	}

}
