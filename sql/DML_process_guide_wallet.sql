//-- Guide for all USER_WALLET_T functions

//-- Check that there is a Bank account registered in USER_WALLET_T.user_bank_acc_no

//-- **START OF PROCESS 1**

//-- 1. GET user_bank_acc_no (SQL)
SELECT 	W.user_bank_acc_no as "bankAccount"
FROM USER_T U, USER_WALLET_T W
WHERE ( U.user_id = W.user_id ) 

//-- 2. CHECK if bank account != NULL (Python)
If (bankAccount != NULL) { // "Proceed to next process" }
ELSE { //"Error: User has not yet registered a bank account number yet." }

//-- **END OF PROCESS 1**


//-- **START OF PROCESS 2**

//-- Withdraw from Bank Account to USER_WALLET_T

//-- 1. Set the amount withdrawn. (Python)
int addToWallet = 5000;		// adding RM 5,000 to the users wallet

//-- GET the current Balance. (SQL)
SELECT 	W.wallet_available_cash as 'currentBalance'
FROM 	USER_T U, USER_WALLET_T W
WHERE ( U.user_id = W.user_id )

//-- 2. UPDATE USER_WALLET_T. (SQL)
UPDATE	USER_WALLET_T
SET		wallet_available_cash = ( 'currentBalance' + addToWallet ),
		wallet_total_deposit = (  wallet_total_deposit + addToWallet )	 
WHERE	( USER_WALLET_T.user_id = '<USER ID>' )

//-- **END OF PROCESS 2**


//-- **START OF PROCESS 3**

//-- Deposit to Bank Account from USER_WALLET_T (SQL)

//-- 1. Set the amount deposit. (Python)
int addToBankAcc = 5000;		// removing RM 5000 from the users wallet

//-- 2. GET the current Balance. (SQL)
SELECT 	W.wallet_available_cash as 'currentBalance'
FROM 	USER_T U, USER_WALLET_T W
WHERE ( U.user_id = W.user_id )

//-- 3. CHECK IF current currentBalance >= addToWallet. (Python)
If (currentBalance >= addToBankAcc) { // "Proceed to next step" }
ELSE { //"Error: User has insufficient funds in wallet to withdraw to bank account." }

//-- 4. UPDATE USER_WALLET_T. (SQL)
UPDATE	USER_WALLET_T
SET		wallet_available_cash = ( 'currentBalance' - addToBankAcc ),
		 wallet_total_withdraw = (   wallet_total_withdraw + addToBankAcc )	 
WHERE	( USER_WALLET_T.user_id = '<USER ID>' )


//-- **END OF PROCESS 3**