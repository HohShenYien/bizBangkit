//-- DML for Process phases START(PHASE 1) to END (PHASE 4) after creation of new business proposal.

//-- START OF PHASE 1

//-- **START OF PROCESS 1** 

//-- Phase 1 trigger 1: " First investor buys shares at 8 % valuation. "
//-- IF USER_WALLET_T.utransact_amt == (0.08 * BUSINESS_T.bus_valuation) 

//-- 	Results to process: 
//--	minus value(int) from USER_WALLET_T [wallet_available_cash - <INPUT: 3200>] (CALC:  0.08 * 40000 = 3200)
//--	plus value(int) to USER_WALLET_T [wallet_spent_cash + <INPUT: 3200>]
//--	set value(int) to USER_TRANSACT_T [utransact_amt = <INPUT: 3200>]
//--	plus value(int) to USER_SHARE_T [share_percent = <INPUT: 8> && share_buy_value + <INPUT: 3200>]
//--	plus value(int) to BUS_SHARE_T [bus_share_current_investor + <INPUT: 1>]
//--	plus value(int) to BUS_TRANSACT_T [rep_transact_datetime = <INPUT: "2021-08-12 23:57:12.120174 UTC">] *GET datetime.datetime.now().timestamp()
//--	plus value(int) to BUS_REPORT_T [rep_total_fund + <INPUT: 3200>]


//-- SQL & Python code for process 1:

//-- 1. GET value to be paid and share quantity amount. (SQL)
SELECT B.bus_valuation as "currValue"
FROM BUSINESS_T B
WHERE (B.bus_id = '<BUSINESS ID>')

//-- 2. CALCULATE 8% of valuation (Python)
int paymentAmt = 0.08 * currValue;	// IF THE VALUATION set by business owner/s is RM 40,000 THEN COST of share is RM 3,200

//-- 3. GET user wallet balance. (Python)
SELECT 	W.wallet_available_cash as "currentBalance"
FROM USER_T U, USER_WALLET_T W
WHERE ( U.user_id = W.user_id )

//-- 4. CHECK IF paymentAmt >= currentBalance ELSE Return error. (Python)
if ("currentBalance" >= paymentAmt) 
		{	//proceed	}
		
else 	{ 	//Error: Insufficient wallet balance }

//-- SUB-SEQUENCE: 
//-- REMOVE payment amount from users wallet --> ADD to spent cash --> SET transaction history [USER_TRANSACT_T] --> SET Shares in [USER_SHARE_T & BUS_SHARE_T] --> ADD to Total Funds in BUS_REPORT_T

//-- 5. MINUS payment from user waller amt and ADD to spent cash (SQL)
 
UPDATE	USER_WALLET_T
SET	wallet_available_cash = ( "currentBalance" - 'paymentAmt' ),
	wallet_spent_cash = ( wallet_spent_cash + 'paymentAmt' )
	 
WHERE	( USER_WALLET_T.user_id = '<USER ID>' )

//-- 6. SET transaction history (SQL) 
//-- utransact_status ( P is Paid, S is Sold ) 

INSERT INTO USER_TRANSACT_T as T (T.user_id, T.bus_id, T.utransact_datetime, T.utransact_amt, T.utransact_type, T.utransact_status)
VALUES ('<USER ID>', '<BUSINESS ID>', 'YYYY-MM-DD HH:MM:SS', 'paymentAmt', 'BUY', 'CF')	//CF = confirmed

//-- 7. SET SHARE for USER_SHARE_T (SQL)

INSERT INTO USER_SHARE_T as USR (USR.bus_id, USR.user_id, USR.share_percent, USR.share_buy_value, USR.share_buy_datetime)
VALUES ('<BUSINESS ID>', '<USER ID>', '8', '<paymentAmt>', 'YYYY-MM-DD HH:MM:SS.120174 UTC')

//-- 8. UPDATE SHARE for BUS_SHARE_T (SQL)	

UPDATE	BUS_SHARE_T
SET		bus_share_current_investor = ( bus_share_current_investor + 1 )
WHERE	( USER_SHARE_T.bus_id = '<BUSINESS ID>' )

//-- 9. ADD funds collected to business report (SQL)

UPDATE	BUS_REPORT_T
SET		rep_total_fund = 'paymentAmt',
		rep_current_fund = 'paymentAmt'

WHERE	( BUS_REPORT_T.bus_id = '<BUSINESS ID>' )

//-- **END OF PROCESS 1** 

//-- **START OF PROCESS 2**

//-- Phase 1 trigger 2: " Business SSM & small business licence registered. "
//-- IF (  BUS_SHARE_T.bus_share_current_investor == bus_share_approve_limit && 
//--     ( BUSINESS_T.bus_ssm_id != 'pending1F' && BUSINESS_T.bus_lic_no != 'pending1F' ) )

//-- Results:
//-- set value(int) to BUS_SHARE_T [bus_share_approve_limit =  bus_share_total_investor]
//-- set value(int) to BUS_SHARE_T [bus_share_phase = <INPUT: 2>]

//-- SQL & Python code for process 2:

//-- 1. Check that the share limit is reached (Phase 1 is 1 investor) & SSM and BUSINESS LICENCE Registration is COMPLETE.(Python)
//-- SPECIAL NOTE: The cost of SSM and Bus Licence is not being factored in this phase for the prototype. It should be added here for the LIVE build.

//-- IF ( 	BUS_SHARE_T.bus_share_current_investor == bus_share_approve_limit && 
//--      ( BUSINESS_T.bus_ssm_id != 'pendingFI' &&
//--        BUSINESS_T.bus_lic_no != 'pendingFI' ) )

//-- 2. UPDATE BUS_SHARE_T with values to proceed to Phase 2. (SQL)

UPDATE	BUS_SHARE_T
SET		bus_share_approve_limit = bus_share_total_investor,
		bus_share_phase = 2
	
WHERE	( BUS_SHARE_T.bus_id = '<BUSINESS ID>' )


//-- END OF PHASE 1
