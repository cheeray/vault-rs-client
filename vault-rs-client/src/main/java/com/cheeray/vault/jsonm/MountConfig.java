package com.cheeray.vault.jsonm;

import com.google.gson.annotations.SerializedName;

public class MountConfig {

	@SerializedName("default_lease_ttl")
	private Long defaultLeaseTtl;
	@SerializedName("max_lease_ttl")
	private Long maxLeaseTtl;

	public Long getDefaultLeaseTtl() {
		return defaultLeaseTtl;
	}

	public void setDefaultLeaseTtl(Long defaultLeaseTtl) {
		this.defaultLeaseTtl = defaultLeaseTtl;
	}

	public Long getMaxLeaseTtl() {
		return maxLeaseTtl;
	}

	public void setMaxLeaseTtl(Long maxLeaseTtl) {
		this.maxLeaseTtl = maxLeaseTtl;
	}
}
