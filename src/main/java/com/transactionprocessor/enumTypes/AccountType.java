package com.transactionprocessor.enumTypes;

public enum AccountType {
	EXTERNAL("E"), INTERNAL("E");

	public String getValue() {
        return asChar;
    }

    private final String asChar;

    private AccountType(String asChar) {
        this.asChar = asChar;
    }

}
