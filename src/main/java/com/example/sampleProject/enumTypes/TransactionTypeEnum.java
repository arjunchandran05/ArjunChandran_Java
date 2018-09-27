package com.example.sampleProject.enumTypes;

public enum TransactionTypeEnum { 
	BUY("B"), SELL("S");

    public String getValue() {
        return value;
    }

    private final String value;

    private TransactionTypeEnum(String value) {
        this.value = value;
    }
};