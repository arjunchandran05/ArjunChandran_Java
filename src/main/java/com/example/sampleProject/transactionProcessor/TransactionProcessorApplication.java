package com.example.sampleProject.transactionProcessor;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

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
		System.out.println("Please select on of the below options : " + "\n 1.Process position files \n 2. Process default file in src\\resources \n 3.Exit");
		Scanner scan = new Scanner(System.in);
		int input = Integer.parseInt(scan.nextLine());
		String inputPath = null;
		String outputPath = null;
		while(input != 3) {
			switch (input) {
			case 1:
				System.out.println("Please Enter the path for transaction input :::");
				inputPath = scan.nextLine();
				System.out.println("Please Enter the output path for transaction output :::");
				outputPath = scan.nextLine();
				break;
			case 2:
				System.out.println("Processing the input transactions in src\\main\\resources..");
				System.out.println("Output will also be placed in src\\main\\resources..");
				inputPath = "src\\main\\resources";
				outputPath = "src\\main\\resources";
				break;

			default: System.out.println("Invalid input...");
				break;
			}
			if (inputPath != null ) {
				TransactionFileOperator.setInputFilePath(inputPath);
				TransactionFileOperator.setOutputFilePath(outputPath);
				TransactionFileOperator.load();
				List<PositionInfo> inputPositions = processTransactions(TransactionFileOperator.getListOfInputs(), TransactionFileOperator.getTransactionsMap());
				TransactionFileOperator.writeObjectAsJSON(inputPositions);
				break;
			}
		}
		scan.close();
	}
	
	public static List<PositionInfo> processTransactions(List<PositionInfo> inputPositions, Map<String, List<Transaction>> transactionsMap) throws IOException {
		System.out.println("Printing input positions ::" + inputPositions);
		inputPositions.forEach(inputPos -> {
			calculateDelta(inputPos, transactionsMap.get(inputPos.getInstrument()));
		});	
		return inputPositions;
	}

	private static void calculateDelta(PositionInfo inputPos, List<Transaction> list) {
		if (list != null && list.size() > 0) {
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
		else {
			System.out.println(" List is null for input instrument :::" + inputPos.getInstrument());
		}
	}
}
