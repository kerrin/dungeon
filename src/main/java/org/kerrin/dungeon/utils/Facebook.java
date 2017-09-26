package org.kerrin.dungeon.utils;

public class Facebook {
	private String hostUrl;
	private String apiId;
	private String apiSecret;
	
	public Facebook(String hostUrl, String apiId, String apiSecret) {
		super();
		this.hostUrl = hostUrl;
		this.apiId = apiId;
		this.apiSecret = apiSecret;
	}
	
	public String getHostUrl() {
		return hostUrl;
	}
	public void setHostUrl(String hostUrl) {
		this.hostUrl = hostUrl;
	}
	public String getApiId() {
		return apiId;
	}
	public void setApiId(String apiId) {
		this.apiId = apiId;
	}
	public String getApiSecret() {
		return apiSecret;
	}
	public void setApiSecret(String apiSecret) {
		this.apiSecret = apiSecret;
	}
}
