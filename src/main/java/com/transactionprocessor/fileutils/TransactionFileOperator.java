package com.transactionprocessor.fileutils;

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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import com.transactionprocessor.pojo.PositionInfo;
import com.transactionprocessor.pojo.Transaction;

public class TransactionFileOperator {

	private static String inputFilePath;
	private static String outputFilePath;

	private static Map<String, List<Transaction>> transactionsMap = new HashMap<String, List<Transaction>>();
	private static List<PositionInfo> listOfInputs;

	public static void main(String args[]) throws IOException {
		TransactionFileOperator.load();
	}

	public static void load() throws IOException {
		File folder = new File(TransactionFileOperator.getInputFilePath());

		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isFile() && fileEntry.getName().endsWith(".csv")) {
				// load input
				CSVReader reader = null;
				try {
					reader = new CSVReader(new FileReader(fileEntry));
					String[] nextLine;
					listOfInputs = new ArrayList<PositionInfo>();
					String [] headerLine = reader.readNext();
					while ((nextLine = reader.readNext()) != null) {
						// nextLine[] is an array of values from the line
						listOfInputs.add(new PositionInfo(nextLine[0], Long.parseLong(nextLine[1]), nextLine[2],
								Long.parseLong(nextLine[3])));
						System.out.println(nextLine[0] + nextLine[1] + "etc...");
					}
				} catch (IOException ex) {
					System.out.println(
							"IO exception occured while reading csv file at :: " + fileEntry.getAbsolutePath());
				} finally {
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
					List<Transaction> listOfTransactions = mapper.readValue(jsonTxt,
							new TypeReference<List<Transaction>>() {
							});
					listOfTransactions.forEach(transaction -> {
						if (transactionsMap.get(transaction.getInstrument()) != null) {
							List<Transaction> t = transactionsMap.get(transaction.getInstrument());
							t.add(transaction);
							transactionsMap.put(transaction.getInstrument(), t);
						} else {
							List<Transaction> t = new ArrayList<Transaction>();
							t.add(transaction);
							transactionsMap.put(transaction.getInstrument(), t);
						}
					});
				} catch (IOException ex) {
					System.out.println(
							"IO exception occured while reading json file at :: " + fileEntry.getAbsolutePath());
				}

			}
		}
	}

	public static void writeObjectAsJSON(List<PositionInfo> list) throws IOException {
		try {
			final ByteArrayOutputStream out = new ByteArrayOutputStream();
			final ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(out, list);
			String headers = "Instrument,Account,AccountType,Quantity,Delta \r \n";
			File jsonOuput = new File(TransactionFileOperator.getOutputFilePath() + "\\Expected_EndOfDay_Positions.json");
			mapper.writeValue(jsonOuput, headers);
			mapper.writeValue(jsonOuput, list );
			// mapper.writeValue(new File(TransactionFileOperator.getOutputFilePath() + "\\Expected_EndOfDay_Positions.json"), list );

			final byte[] data = out.toByteArray();
			FileOutputStream stream = new FileOutputStream(TransactionFileOperator.getOutputFilePath());
			try {
				stream.write(data);
			} finally {
				stream.close();
			}
			System.out.println(new String(data));
		} catch (IOException ex) {
			System.out.println("IO exception occured while writing json file ");
		}
	}

	public static Map<String, List<Transaction>> getTransactionsMap() {
		return transactionsMap;
	}

	public static List<PositionInfo> getListOfInputs() {
		return listOfInputs;
	}

	public static void setInputFilePath(String inputFilePath) {
		TransactionFileOperator.inputFilePath = inputFilePath;
	}

	public static String getInputFilePath() {
		return inputFilePath;
	}

	public TransactionFileOperator(String inputFilePath) {
		TransactionFileOperator.inputFilePath = inputFilePath;
	}

	public static String getOutputFilePath() {
		return outputFilePath;
	}

	public static void setOutputFilePath(String outputFilePath) {
		TransactionFileOperator.outputFilePath = outputFilePath;
	}

}
