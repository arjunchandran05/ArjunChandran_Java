Story: The Transaction Processor

Meta:
@author Arjun Chandran

Narrative:
    ABC Investment Bank is a prominent global bank, as part of the General Ledger System, I want to develop a position calculation process
    Position Calculation process takes start of day positions and transaction files as input,
    apply transactions on positions based on transaction type (B/S) and account type (I/E), and computes end of day positions.
    Depending on the direction of the transaction (Buy/Sell) each transaction is recorded as debit and credit
    into external and internal accounts in the “Positions” file.

Scenario: Midnight
When the time is 00:00:00
Then the clock should look like
Y
OOOO
OOOO
OOOOOOOOOOO
OOOO

Scenario: Middle of the afternoon
When the time is 13:17:01
Then the clock should look like
O
RROO
RRRO
YYROOOOOOOO
YYOO

Scenario: Just before midnight
When the time is 23:59:59
Then the clock should look like
O
RRRR
RRRO
YYRYYRYYRYY
YYYY

Scenario: Midnight
When the time is 24:00:00
Then the clock should look like
Y
RRRR
RRRR
OOOOOOOOOOO
OOOO



