package com.cheeray.vault.jsonm;

import com.google.gson.annotations.SerializedName;

public class SecretLease {

	@SerializedName("lease_id")
	private String leaseId;
	private Boolean renewable;
	@SerializedName("lease_duration")
	private Long leaseDuration;
	private Secret data;
	private String warnings;
	private String auth;

	public String getLeaseId() {
		return leaseId;
	}

	public void setLeaseId(String leaseId) {
		this.leaseId = leaseId;
	}

	public Boolean getRenewable() {
		return renewable;
	}

	public void setRenewable(Boolean renewable) {
		this.renewable = renewable;
	}

	public Long getLeaseDuration() {
		return leaseDuration;
	}

	public void setLeaseDuration(Long leaseDuration) {
		this.leaseDuration = leaseDuration;
	}

	public Secret getData() {
		return data;
	}

	public void setData(Secret data) {
		this.data = data;
	}

	public String getWarnings() {
		return warnings;
	}

	public void setWarnings(String warnings) {
		this.warnings = warnings;
	}

	public String getAuth() {
		return auth;
	}

	public void setAuth(String auth) {
		this.auth = auth;
	}
}
