Story: The Transaction Processor

Meta:
@author Arjun Chandran

Narrative:
    ABC Investment Bank is a prominent global bank, as part of the General Ledger System, I want to develop a position calculation process
    Position Calculation process takes start of day positions and transaction files as input,
    apply transactions on positions based on transaction type (B/S) and account type (I/E), and computes end of day positions.
    Depending on the direction of the transaction (Buy/Sell) each transaction is recorded as debit and credit
    into external and internal accounts in the “Positions” file.

Scenario: Transaction type is B and Account type is E
When the input transaction is { "TransactionId": 1,"Instrument": "IBM","TransactionType": "B","TransactionQuantity": 1000 }
When the start of input position is IBM,101,E,100000
Then the output transaction is IBM,101,E,101000,1000

