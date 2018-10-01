package com.example.transactionprocessor;

import static org.assertj.core.api.Assertions.assertThat;
import java.io.IOException;
import java.util.List;

import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.transactionprocessor.main.TransactionProcessorApplication;
import com.transactionprocessor.pojo.PositionInfo;
import com.transactionprocessor.pojo.Transaction;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration
public class TransactionProcessorApplicationTests {
	
	private TransactionProcessorApplication transactionProcessorApplication = new TransactionProcessorApplication();
	private JSONObject theInputTransaction;
	private String theInputPosition;

	/*
	 * When the transaction type is B
When the account type is E
When the input transaction is { "TransactionId": 1,"Instrument": "IBM","TransactionType": "B","TransactionQuantity": 1000 }
When the start of input position is IBM,101,E,100000 and IBM,201,I,-100000
Then the output transaction is IBM,101,E,101000,1000 and IBM,201,I,-101000,-1000*/
    
    @When("the input transaction is $inputTransaction")
    public void whenTheInputTransactionIs(JSONObject inputTransaction) {
    	theInputTransaction = inputTransaction;
    }
    
    @When("the start of input position is $inputPosition")
    public void whenTheInputPositionsAre(String inputPosition) {
    	theInputPosition = inputPosition;
    }
    
    public List<Transaction> prepareInputTransactions() throws JsonParseException, JsonMappingException, IOException {
    	String jsonTxt = theInputTransaction.toString();
		ObjectMapper mapper = new ObjectMapper();
		// mapper.configure(MapperFeature.AUTO_DETECT_FIELDS, true);
		List<Transaction> listOfTransactions = mapper.readValue(jsonTxt,
				new TypeReference<List<Transaction>>() {
				});
		return listOfTransactions;
    	
    }
    
    public PositionInfo preparePositionInfo() {
    	String[] inputPositions1 = theInputPosition.split(" ") ;
    	return new PositionInfo(inputPositions1[0], Long.parseLong(inputPositions1[1]), inputPositions1[2], Long.parseLong(inputPositions1[3]));
    }
    
    public Long getExpectedDelta(String theExpectedOutput) {
    	String[] output = theExpectedOutput.split(",");
    	return Long.parseLong(output[4]);
    }
    @Then("Then the output transaction is $theExpectedOutput")
    public void thenTheOutputTransactionDeltaShouldBe(String theExpectedOutput) throws JsonParseException, JsonMappingException, IOException {
        assertThat(transactionProcessorApplication.calculateDelta(this.preparePositionInfo(), this.prepareInputTransactions().get(0))).isEqualTo(this.getExpectedDelta(theExpectedOutput));
    }
    
    @Test
    public void mockTest() {
    	assertThat(true).isEqualTo(true);
    }

}
