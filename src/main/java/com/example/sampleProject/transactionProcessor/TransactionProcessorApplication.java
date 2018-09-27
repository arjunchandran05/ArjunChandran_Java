package com.example.sampleProject.transactionProcessor;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.sampleProject.enumTypes.AccountType;
import com.example.sampleProject.enumTypes.TransactionTypeEnum;
import com.example.sampleProject.pojo.PositionInfo;
import com.example.sampleProject.pojo.Transaction;
import com.example.samplepProject.fileutils.TransactionFileOperator;

@SpringBootApplication
public class TransactionProcessorApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(TransactionProcessorApplication.class, args);
		System.out.println("Executing the start class");
		TransactionFileOperator.load(TransactionFileOperator.getInputFilePath());
		processTransactions(TransactionFileOperator.getListOfInputs(), TransactionFileOperator.getTransactionsMap());
	}
	
	public static void processTransactions(List<PositionInfo> inputPositions, Map<String, List<Transaction>> transactionsMap) throws IOException {
		inputPositions.forEach(inputPos -> {
			calculateDelta(inputPos, transactionsMap.get(inputPos.getInstrument()));
		});
		TransactionFileOperator.writeObjectAsJSON(inputPositions, TransactionFileOperator.getInputFilePath());
		
	}

	private static void calculateDelta(PositionInfo inputPos, List<Transaction> list) {
        list.forEach(transaction -> {
        	Long newQuantity = 0L;
        	Long quantity = inputPos.getQuantity();
        	Long transactionQuantity = transaction.getTransactionQuantity();
    		if((TransactionTypeEnum.BUY.getValue().equals(transaction.getTransactionType()) && AccountType.EXTERNAL.getValue().equals(inputPos.getAccountType())) ||
    				(TransactionTypeEnum.SELL.getValue().equals(transaction.getTransactionType()) && AccountType.INTERNAL.getValue().equals(inputPos.getAccountType()))
    				) {
    			newQuantity = quantity + transactionQuantity;
 
    		}
    		else if((TransactionTypeEnum.BUY.getValue().equals(transaction.getTransactionType()) && AccountType.EXTERNAL.getValue().equals(inputPos.getAccountType())) ||
    				(TransactionTypeEnum.SELL.getValue().equals(transaction.getTransactionType()) && AccountType.INTERNAL.getValue().equals(inputPos.getAccountType()))) {
    			newQuantity = quantity - transactionQuantity;
    		}
    		inputPos.setDelta(inputPos.getQuantity() - newQuantity);
        	
        });
	}
}
