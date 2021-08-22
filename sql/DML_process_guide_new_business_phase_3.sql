//-- START OF PHASE 3

//-- **START OF PROCESS 1**

//-- IF ( bus_share_maturity_date LESS THAN  <INPUT: date.today> ) {// "Error: Shares cannot be sold!"; }

//-- BUS_REPORT_T.rep_total_spent + <INPUT: 1000> //example of spending RM1000
//-- Phase 3 trigger 1: Business spends from account.

//-- Results:
//-- set the amount to spend //e.g. RM 1000
//-- check current funds in BUS_REPORT_T.rep_current_fund
//-- minus value(int) from BUS_REPORT_T
//-- update value(int) in BUS_REPORT_T.rep_total_spent
//-- set value(int) in transaction history

//-- SQL & Python code for process 1:

//-- 1. Set the spend amount (Python)
int spendAmt = 1000;
string descriptionOfTransaction = "E.g. Spent RM 1000 to buy Nasi Lemak Stand";

//-- 2. GET balance of funds (SQL)
SELECT 	BR.rep_current_fund as "currentBalance"
FROM	BUS_REPORT_T BR
WHERE ( BR.bus_id = '<BUSINESS ID>' )

//-- 3. MINUS spend amount from BUS_REPORT_T (SQL)
UPDATE	BUS_REPORT_T
SET		rep_current_fund = ( rep_current_fund - '<spendAmt>' ),
		rep_total_spent = ( rep_total_spent + '<spendAmt>' )
		
WHERE	( bus_id = '<BUSINESS ID>' )

//-- 4. CREATE TRANSACTION RECORD (SQL)
INSERT INTO BUS_TRANSACT_T as BST (BST.bus_id, BST.rep_transact_datetime, BST.rep_transact_decription, BST.rep_transact_tos_flag, BST.rep_transact_fpath_updatepic, BST.rep_transact_amt, BST.rep_transact_type)
VALUES ('<BUSINESS ID>', 'YYYY-MM-DD HH:MM:SS.120174 UTC', '<descriptionOfTransaction>', 'A', './tmp/business_update/fake_spend.jpg', '1000', 'SP')
//-- SPECIAL NOTE: The evidence pictures can be a fake picture stored on the app for demo, LIVE build will need to integrate camera to upload to DB


//-- **END OF PROCESS 1**

//-- **START OF PROCESS 2**

//-- BUS_REPORT_T.rep_total_income + <INPUT: 1000> //example of depositing RM1000 at end of business day
//-- Phase 3 trigger 2: Business deposits to account.

//-- Results:
//-- set the amount to earned //e.g. RM 1000
//-- add value(int) from BUS_REPORT_T
//-- set value(int) in transaction history
//-- update value(int) in BUS_REPORT_T.rep_total_income

//-- SQL & Python code for process 2:

//-- 1. SET the deposited amount. (Python)
int depositAmt = 1000;
string descriptionOfTransaction = "Successful Deposited!";

//-- 2. ADD income amount to BUS_REPORT_T (SQL)
UPDATE	BUS_REPORT_T
SET		rep_current_fund = ( rep_current_fund + '<depositAmt>' ),
		rep_total_income = ( rep_total_income + '<depositAmt>' )

WHERE	( bus_id = '<BUSINESS ID>' )
 
//-- 3. CREATE TRANSACTION RECORD (SQL)
INSERT INTO BUS_TRANSACT_T as BST (BST.bus_id, BST.rep_transact_datetime, BST.rep_transact_decription, BST.rep_transact_tos_flag, BST.rep_transact_fpath_updatepic, BST.rep_transact_amt, BST.rep_transact_type)
VALUES ('<BUSINESS ID>', 'YYYY-MM-DD HH:MM:SS.120174 UTC', '<descriptionOfTransaction>', 'A', './tmp/business_update/deposit_success.jpg', '1000', 'EA')


//-- **END OF PROCESS 2**

//-- **START OF PROCESS 3**

//-- IF (bus_share_maturity_date == <INPUT: date.today> )
//-- Phase 3 trigger 3: six months after start date

//-- Results:
//-- 1: BUS_REPORT_T.rep_total_income - BUS_REPORT_T.rep_total_spent = <OUTPUT: REVENUE> //E.g. assume NET revenue = RM 10000 after 6 months of business
//-- 2: capitalAtStart = 0.40 * BUSINESS_T.bus_valuation; // = 16000
//-- 3: capitalNow = 16000 + <REVENUE>; // = 16000 + 10000 = 26000
//-- 4: updatedValuation = ( (26000/4) * 10 ); // RESULT AFTER PHASE 3: valuation = RM 65000  
//-- 5: BUSINESS_T.bus_valuation = 65000

//-- SQL & Python code for process 3:

//-- 1. GET revenue from BUS_REPORT_T (SQL)
SELECT 	BR.rep_total_income as 'totalIncome',
		BR.rep_total_spent as 'totalExpenses'
FROM	BUS_REPORT_T BR
WHERE ( BR.bus_id = '<BUSINESS ID>' )

//-- 2. Calculate the revenue (Python)
int revenue = ( '<totalIncome>' - '<totalExpenses>' );

//-- 3. GET starting capital from BUSINESS_T (SQL)
SELECT	B.bus_valuation as 'valueAtStart'
FROM	BUSINESS_T B
WHERE (	B.bus_id = '<BUSINESS ID>' )

//-- 4. Calculate the new valuation (Python)
int capitalAtStart = ( 0.04 * '<valueAtStart>' );
int capitalNow = ( capitalAtStart + revenue );
int updatedValuation = ( ( capitalNow / 4 ) * 10 );

//-- 5. UPDATE to current business valuation. (SQL)
UPDATE	BUSINESS_T
SET		bus_valuation = ( '<updatedValuation>' )

WHERE	( bus_id = '<BUSINESS ID>' )


//-- **END OF PROCESS 3**

//-- **START OF PROCESS 4**

//-- IF ( ( USER_SHARE_T.share_percent == 8 ) && ( BUS_SHARE_T.bus_share_maturity_date == <INPUT: date.today> ) && ( USER_TRANSACT_T.bus_id == BUSINESS_T.bus_id ) )
//-- Phase 3 trigger 4: investors sell shares 8% during 24 hours at end of phase 3.

//-- Results:
//-- plus value(int) to USER_WALLET_T [wallet_available_cash + <INPUT: 5200>] (CALC:  0.08 * 65000 = 5200)
//-- plus value(int) to USER_WALLET_T [wallet_earned_cash + <INPUT: 5200>] 
//-- set value(int) to USER_TRANSACT_T [utransact_amt = <INPUT: 5200>]
//-- set value(int) to USER_SHARE_T [share_sell_value + <INPUT: 5200>]
//-- plus value(int) to BUS_SHARE_T [bus_share_total_divestor + <INPUT: 1>]
//-- plus value(int) to BUS_TRANSACT_T [rep_transact_datetime = <INPUT: "2022-02-12 23:57:12.120174 UTC">] *GET datetime.datetime.now().timestamp()
//-- minus value(int) to BUS_REPORT_T [rep_current_fund - <INPUT: 5200>]
//-- plus value(int) to BUSINESS_OWNER_T[share_alloc_qty + 8]

//-- IF ( ( USER_SHARE_T.share_percent == 5 ) && ( BUS_SHARE_T.bus_share_maturity_date == <INPUT: date.today> ) && ( USER_TRANSACT_T.bus_id == BUSINESS_T.bus_id ) )
//-- Phase 3 trigger 5: investors sell shares 5% during 24 hours.

//-- Results:
//-- plus value(int) to USER_WALLET_T [wallet_available_cash + <INPUT: 3250>] (CALC:  0.05 * 65000 = 3250)
//-- plus value(int) to USER_WALLET_T [wallet_earned_cash + <INPUT: 3250>] 
//-- set value(int) to USER_TRANSACT_T [utransact_amt = <INPUT: 3250>]
//-- set value(int) to USER_SHARE_T [share_sell_value + <INPUT: 3250>]
//-- plus value(int) to BUS_SHARE_T [bus_share_total_divestor + <INPUT: 1>]
//-- minus value(int) to BUS_REPORT_T [rep_current_fund - <INPUT: 3250>]
//-- plus value(int) to BUSINESS_OWNER_T[share_alloc_qty + 5]

//-- SQL & Python code for process 4:

//-- 1. GET user share for this business. (SQL)
SELECT	USR.share_percent as 'sharePercent'		// 8% or 5%
FROM	USER_SHARE_T USR
WHERE ( USR.user_id = "<USER ID>" )

//-- 2. GET Share maturity date. (SQL)
SELECT	BSR.bus_share_maturity_date as "matureDate"
FROM	BUS_SHARE_T BSR
WHERE ( BSR.bus_id = "<BUSINESS ID>" )

//-- 3. CHECK IF Share can be sold right now? (Python)
If (matureDate == date.today) { //"Selling is allowed. Proceed to step 4"}
ELSE {//"Error: Share has not matured yet."} 

//-- 4. GET updated valuation in BUSINESS_T (SQL)
SELECT	B.bus_valuation as 'currentValue'
FROM	BUSINESS_T B
WHERE (	B.bus_id = '<BUSINESS ID>' )

//-- 5. Calculate the selling value.
int sellValue = ( sharePercent * currentValue );
string timeOfSale = datetime.datetime.now().timestamp();

//-- 6. ADD share sale amount to USER_WALLET_T (SQL)
UPDATE	USER_WALLET_T
SET		wallet_available_cash = wallet_available_cash + sellValue,
		wallet_earned_cash = wallet_earned_cash + sellValue
WHERE	( user_id = '<USER ID>' )

//-- 7. CREATE transaction record in USER_TRANSACT_T. (SQL)
INSERT INTO USER_TRANSACT_T as T (T.user_id, T.bus_id, T.utransact_datetime, T.utransact_amt, T.utransact_type, T.utransact_status)
VALUES ('<USER ID>', '<BUSINESS ID>', 'timeOfSale', 'sellValue', 'SEL', 'CF')	//CF = confirmed

//-- 8. UPDATE USER_SHARE_T (SQL)
UPDATE	USER_SHARE_T
SET		share_sell_value = 'sellValue',
		share_sell_datetime = 'timeOfSale'
WHERE	( user_id = '<USER ID>' AND bus_id = '<BUSINESS ID>')

//-- 9. UPDATE BUS_SHARE_T (SQL)
UPDATE	BUS_SHARE_T
SET		bus_share_total_divestor = bus_share_total_divestor + 1
WHERE	( bus_id = '<BUSINESS ID>' )

//-- 10. UPDATE BUS_REPORT_T to reflect sale of capital (SQL)
UPDATE	BUS_REPORT_T
SET		rep_current_fund = rep_current_fund - 'sellValue'
WHERE	( bus_id = '<BUSINESS ID>' )

//--11. UPDATE the BUSINESS_OWNER_T
UPDATE	BUSINESS_OWNER_T
SET		share_alloc_qty = share_alloc_qty + 'sharePercent'
WHERE	( bus_id = '<BUSINESS ID>' )

//-- **END OF PROCESS 4**


//-- **START OF PROCESS 5**

//-- IF ( bus_share_maturity_date MORE THAN <INPUT: date.today> )
//-- Phase 3 trigger 6: six months + 1 day passed.

//-- Results:
//-- set value(int) to BUS_SHARE_T [bus_share_phase = <INPUT: 4>]
//-- NOTE: BUSINESS_T.bus_valuation stays constant.

//-- SQL & Python code for process 5:

//-- 1. GET the share maturity date (SQL)
SELECT	BSR.bus_share_maturity_date as "matureDate"
FROM	BUS_SHARE_T BSR
WHERE ( BSR.bus_id = "<BUSINESS ID>" )


//-- 2. CHECK IF Share selling 24 hours has expired? (Python)
If (matureDate > date.today) { //"Selling is allowed. Proceed to step 2"}
ELSE { //"Error: Share selling period has not ended yet." }


//-- 3. UPDATE BUSINESS SHARE PHASE in  BUS_SHARE_T
UPDATE	BUS_SHARE_T
SET		bus_share_phase = 4
WHERE	( BUS_SHARE_T.bus_id = '<BUSINESS ID>' )

//-- PHASE 4

//-- **END OF PROCESS 5**

//-- END OF PHASE 3