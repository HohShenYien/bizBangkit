//-- START OF PHASE 2

//-- **START OF PROCESS 1** 

//-- Phase 2 trigger 1: investors buys shares at 5%
//-- IF USER_WALLET_T.utransact_amt == (0.05 * BUSINESS_T.bus_valuation) 

//-- Results:
//-- minus value(int) from USER_WALLET_T [wallet_available_cash - <INPUT: 2000>] (CALC:  0.05 * 40000 = 2000)
//-- plus value(int) to USER_WALLET_T [wallet_spent_cash + <INPUT: 2000>]
//-- set value(int) to USER_TRANSACT_T [utransact_amt = <INPUT: 2000>]
//-- plus value(int) to USER_SHARE_T [share_percent = <INPUT: 5> && share_buy_value + <INPUT: 2000>]
//-- plus value(int) to BUS_SHARE_T [bus_share_current_investor + <INPUT: 1>]
//-- plus value(int) to BUS_TRANSACT_T [rep_transact_datetime = <INPUT: "2021-08-12 23:57:12.120174 UTC">] *GET datetime.datetime.now().timestamp()
//-- plus value(int) to BUS_REPORT_T [rep_total_fund + <INPUT: 2000>]

//-- SQL & Python code for process 1:

//-- 1. GET value to be paid and share quantity amount. (SQL)
SELECT B.bus_valuation as "currValue"
FROM BUSINESS_T B
WHERE (B.bus_id = '<BUSINESS ID>')

//-- 2. CALCULATE 5% of valuation (Python)
int paymentAmt = 0.05 * currValue;	// IF THE VALUATION set by business owner/s is RM 40,000 THEN COST of share is RM 2,000

//-- 3. GET user wallet balance. (Python)
SELECT 	W.wallet_available_cash as "currentBalance"
FROM 	USER_T U, USER_WALLET_T W
WHERE ( U.user_id = W.user_id )

//-- 4. CHECK IF paymentAmt >= currentBalance ELSE Return error. (Python)
if	("currentBalance" >= paymentAmt) 
		{	//proceed	}
else 	{ 	//Error: Insufficient wallet balance }


//-- SUB-SEQUENCE:  RUN EACH time an investor buys shares until investor limit reached. 
//-- REMOVE payment amount from users wallet --> ADD to spent cash --> SET transaction history [USER_TRANSACT_T] --> SET Shares in [USER_SHARE_T & BUS_SHARE_T] --> ADD to Total Funds in BUS_REPORT_T

//-- 5. MINUS payment from user waller amt and ADD to spent cash (SQL)
 
UPDATE	USER_WALLET_T
SET		wallet_available_cash = ( "currentBalance" - paymentAmt ),
		wallet_spent_cash = ( wallet_spent_cash + paymentAmt )
	 
WHERE	( USER_WALLET_T.user_id = '<USER ID>' )

//-- 6. SET transaction history (SQL)

INSERT INTO USER_TRANSACT_T as T (T.user_id, T.bus_id, T.utransact_datetime, T.utransact_amt, T.utransact_type, T.utransact_status)
VALUES ('<USER ID>', '<BUSINESS ID>', 'YYYY-MM-DD HH:MM:SS', 'paymentAmt', 'BUY', 'PAID')

//-- 7. SET SHARE for USER_SHARE_T (SQL)

INSERT INTO USER_SHARE_T as USR (USR.bus_id, USR.user_id, USR.share_percent, USR.share_buy_value, USR.share_buy_datetime)
VALUES ('<BUSINESS ID>', '<USER ID>', '5', 'paymentAmt', 'YYYY-MM-DD HH:MM:SS.120174 UTC')

//-- 8. UPDATE SHARE for BUS_SHARE_T (SQL)	

UPDATE	BUS_SHARE_T
SET		bus_share_current_investor = ( bus_share_current_investor + 1 )
WHERE	( USER_SHARE_T.bus_id = '<BUSINESS ID>' )

//-- 9. ADD funds collected to business report (SQL)

UPDATE	BUS_REPORT_T
SET		rep_total_fund = ( rep_total_fund + 'paymentAmt' ),
		rep_current_fund = ( rep_current_fund + 'paymentAmt' )
		
WHERE	( BUS_REPORT_T.bus_id = '<BUSINESS ID>' )

//-- **END OF PROCESS 1** 

//-- **END OF PROCESS 1**

//-- **START OF PROCESS 2**

//-- IF ( BUS_SHARE_T.bus_share_current_investor == bus_share_approve_limit )
//-- Phase 2 trigger 2: " total of seven sucessfully investors reached. "

//-- Results:
//-- set value(int) to BUS_SHARE_T [bus_share_phase = <INPUT: 3>]
//-- set value(datetime) to BUS_SHARE_T [bus_share_maturity_date = <INPUT: "2022-02-12 23:57:12">] ( CALC: six_months = date.today("2021-08-12 23:57:12") + relativedelta(months=+6) ) **FORMAT: YYYY-MM-DD HH:MM:SS
//-- set value(datetime) to BUSINESS_T [bus_start_date = <INPUT: "2022-08-12 23:57:12">] ( CALC: startDateOfBusiness = date.today("2021-08-12 23:57:12") ) **FORMAT: YYYY-MM-DD HH:MM:SS
//-- set value(datetime) to BUSINESS_T [bus_end_date = <INPUT: "2022-08-12 23:57:12">]   ( CALC: one_year = date.today("2021-08-12 23:57:12") + relativedelta(months=+12) ) **FORMAT: YYYY-MM-DD HH:MM:SS

//-- SQL & Python code for process 2:

//-- 1. Check that the share limit is reached (Phase 2 is max investor limit (7 for new business proposals)) (Python)

//-- IF ( 	BUS_SHARE_T.bus_share_current_investor == BUS_SHARE_T.bus_share_approve_limit )

//-- 2. UPDATE BUSINESS_T with start date (phase 3) and end date (phase 4) (SQL)

UPDATE	BUS_SHARE_T
SET	bus_start_date = <'2021-08-12 23:57:12'>,
	bus_share_maturity_date = <'2022-02-12 23:57:12'>,
	bus_end_date = <'2022-08-12 23:57:12'>
	
WHERE	( BUS_SHARE_T.bus_id = '<BUSINESS ID>' )	

//-- 3.  UPDATE BUS_SHARE_T with values to proceed to Phase 3. (SQL) 

UPDATE	BUSINESS_T
SET	bus_share_phase = 3

//--  END OF PHASE 2
