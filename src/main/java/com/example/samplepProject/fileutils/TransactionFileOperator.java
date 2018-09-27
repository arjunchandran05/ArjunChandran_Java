package com.example.samplepProject.fileutils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.example.sampleProject.pojo.PositionInfo;
import com.example.sampleProject.pojo.Transaction;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;

public class TransactionFileOperator {

	private static String inputFilePath = "C:\\Arjun_Chandran\\Personal\\Projects\\transactionProcessor\\src\\main\\resources";
	
	public static String getInputFilePath() {
		return inputFilePath;
	}
	public TransactionFileOperator(String inputFilePath) {
		TransactionFileOperator.inputFilePath = inputFilePath;
	}
	
	private static Map<String, List<Transaction>> transactionsMap = new HashMap<String, List<Transaction>>();
	private static List<PositionInfo> listOfInputs;

	public static void main(String args[]) throws IOException {
		TransactionFileOperator.load(inputFilePath);
	}
	public static void load(String filePath) throws IOException {
		File folder = new File(filePath);

		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isFile() && fileEntry.getName().endsWith(".csv")) {
				// load input
				CSVReader reader = null;
				try {
					reader = new CSVReader(new FileReader(fileEntry));
					String [] nextLine;
					listOfInputs = new ArrayList<PositionInfo>();
					nextLine = reader.readNext();
					while ((nextLine = reader.readNext()) != null) {
					    // nextLine[] is an array of values from the line
						listOfInputs.add(new PositionInfo(nextLine[0], Long.parseLong(nextLine[1]), nextLine[2], Long.parseLong(nextLine[3])));
					    System.out.println(nextLine[0] + nextLine[1] + "etc...");
					}
				}
				catch(IOException ex) {
					System.out.println("IO exception occured while reading csv file at :: " + fileEntry.getAbsolutePath());
				}
				finally {
					if (reader != null) {
						reader.close();
					}
				}

			}
			if (fileEntry.isFile() && fileEntry.getName().endsWith(".json")) {
				// load transactions
				try {
					InputStream is = new FileInputStream(fileEntry);
		            String jsonTxt = IOUtils.toString(is, "UTF-8");
					ObjectMapper mapper = new ObjectMapper();
					// mapper.configure(MapperFeature.AUTO_DETECT_FIELDS, true);
					List<Transaction> listOfTransactions = mapper.readValue(jsonTxt, new TypeReference<List<Transaction>>() {});
					listOfTransactions.forEach(transaction -> {
						if (transactionsMap.get(transaction.getInstrument()) != null) {
							List <Transaction> t = transactionsMap.get(transaction.getInstrument());
							t.add(transaction);
							transactionsMap.put(transaction.getInstrument(), t);
						}
						else {
							List<Transaction> t = new ArrayList<Transaction>();
							t.add(transaction);
							transactionsMap.put(transaction.getInstrument(), t);
						}
					});
				}
				catch(IOException ex) {
					System.out.println("IO exception occured while reading json file at :: " + fileEntry.getAbsolutePath());
				}
				
			}
		}
	}
	
	public static void writeObjectAsJSON(List<PositionInfo> list, String filePath) throws IOException {
		File folder = new File(filePath);

		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isFile() && fileEntry.getName().endsWith(".csv")) {
				// load input
				try {
					final ByteArrayOutputStream out = new ByteArrayOutputStream();
				    final ObjectMapper mapper = new ObjectMapper();
				    mapper.writeValue(out, list);

				    final byte[] data = out.toByteArray();
				    FileOutputStream stream = new FileOutputStream(filePath);
				    try {
				        stream.write(data);
				    } finally {
				        stream.close();
				    }
				    System.out.println(new String(data));
				}
				catch(IOException ex) {
					System.out.println("IO exception occured while writing json file ");
				}
			}
		}
	}
	public static Map<String, List<Transaction>> getTransactionsMap() {
		return transactionsMap;
	}
	public static List<PositionInfo> getListOfInputs() {
		return listOfInputs;
	}

}