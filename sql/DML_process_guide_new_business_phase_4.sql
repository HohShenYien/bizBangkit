//-- START OF PHASE 4

//-- **START OF PROCESS 1**

//-- SQL & Python code for process 1:

//-- 1. Set the spend amount (Python)
int spendAmt = 1000;
string descriptionOfTransaction = "E.g. Spent RM 1000 to pay rent";

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

//-- 1. GET the end date from BUSINESS_T
SELECT B.bus_end_date as "endOfIncubation"
FROM BUSINESS_T
WHERE (B.bus_id = '<BUSINESS ID>')

//-- 2. Check if business has reached end date. ()
If (endOfIncubation > date.today) { //"Business has completed funding on platform"}
ELSE { //"Error: Business has not yet finished incubation." }

//-- 4. UPDATE BUSINESS SHARE PHASE in  BUS_SHARE_T
UPDATE	BUS_SHARE_T
SET		bus_share_phase = 5
WHERE	( BUS_SHARE_T.bus_id = '<BUSINESS ID>' )


//-- **END OF PROCESS 3**

//-- END OF PHASE 4

//-- NOTE: BUSINESS that have finished 1 year reporting on the platform will be given completion token and transactions will no longer be tracked on the platform.