package com.cheeray.vault.jsonm;

import com.google.gson.annotations.SerializedName;

public class SealStatus {

	private boolean sealed;
	@SerializedName("t")
	private int threshold;
	@SerializedName("n")
	private int shares;
	private int progress;
	
	public boolean isSealed() {
		return sealed;
	}
	public void setSealed(boolean sealed) {
		this.sealed = sealed;
	}
	public int getThreshold() {
		return threshold;
	}
	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}
	public int getShares() {
		return shares;
	}
	public void setShares(int shares) {
		this.shares = shares;
	}
	public int getProgress() {
		return progress;
	}
	public void setProgress(int progress) {
		this.progress = progress;
	}

}
