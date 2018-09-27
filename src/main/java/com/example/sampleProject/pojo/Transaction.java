package com.example.sampleProject.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Transaction {
	@JsonProperty("TransactionId")
	private Long transactionId;
	@JsonProperty("Instrument")
	private String instrument;
	@JsonProperty("TransactionType")
	private String transactionType;
	@JsonProperty("TransactionQuantity")
	private Long transactionQuantity;

	public Long getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}
	public String getInstrument() {
		return instrument;
	}
	public void setInstrument(String instrument) {
		this.instrument = instrument;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public Long getTransactionQuantity() {
		return transactionQuantity;
	}
	public void setTransactionQuantity(Long transactionQuantity) {
		this.transactionQuantity = transactionQuantity;
	}

}
