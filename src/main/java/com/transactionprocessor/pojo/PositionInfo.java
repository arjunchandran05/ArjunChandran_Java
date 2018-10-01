package com.transactionprocessor.pojo;

public class PositionInfo {
	private String instrument;
	private Long accountNo;
	private String accountType;
	private Long quantity;
	private Long delta;
	
	public PositionInfo(String instrument, Long accountNo, String accountType, Long quantity) {
		this.instrument = instrument;
		this.accountNo = accountNo;
		this.accountType = accountType;
		this.quantity = quantity;
		
	}

	public String getInstrument() {
		return instrument;
	}
	public void setInstrument(String instrument) {
		this.instrument = instrument;
	}
	public Long getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(Long accountNo) {
		this.accountNo = accountNo;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public Long getQuantity() {
		return quantity;
	}
	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}
	public Long getDelta() {
		return delta;
	}
	public void setDelta(Long delta) {
		this.delta = delta;
	}
	
	@Override
	public String toString() {
		return this.instrument + " " + this.accountNo + " " + this.accountType + " " + this.quantity + " " + this.delta ;
	}
}
