from flask import Blueprint, request, jsonify
import sqlite3
import datetime

phases_bp = Blueprint('phases', __name__)

# CONNECTING TO DATABASE FOR STORING AND READING PURPOSES
con = sqlite3.connect('bizBangkit.db', check_same_thread=False)
cur = con.cursor()
con.commit()

currentDateTime = datetime.datetime.now()


# Wallet Process for transaction purposes
@phases_bp.route('/wallet/<user_authkey>', methods=['POST'])
def wallet(user_authkey):
    wal = request.json

    cur.execute("SELECT user_id FROM USER_T WHERE user_authkey = ?", (user_authkey,))
    check = cur.fetchone()
    check1 = check[0]  # user_id from database

    data = wal['user_id']  # user_id from json input

    if data == check1:
        cur.execute("SELECT W.user_bank_acc_no FROM USER_T U JOIN USER_WALLET_T W ON U.user_id = W.user_id "
                    "WHERE W.user_id = ?", (check1,))
        val = cur.fetchone()
        bankAccount = val[0]  # W.user_bank_acc_no
        # print(bankAccount)  # print the bank account number

        if bankAccount != 0:  # it will show the bank account number

            # P2: Withdraw from Bank Account to USER_WALLET_T
            addToWallet = 5000

            cur.execute("SELECT W.wallet_available_cash FROM USER_T U JOIN USER_WALLET_T W ON U.user_id = W.user_id "
                        "WHERE w.user_id = ?", (check1,))
            check2 = cur.fetchone()
            currentBalance = check2[0]  # W.wallet_available_cash

            wal_avail_cash = currentBalance + addToWallet

            cur.execute("UPDATE	USER_WALLET_T SET wallet_available_cash = ?, "
                        "wallet_total_deposit = wallet_total_deposit + ? WHERE user_id = ?",
                        (wal_avail_cash, addToWallet, check1,))
            con.commit()

            # P3: Deposit to Bank Account from USER_WALLET_T
            addToBankAcc = 5000

            cur.execute("SELECT W.wallet_available_cash FROM USER_T U JOIN USER_WALLET_T W ON U.user_id = W.user_id "
                        "WHERE W.user_id = ?", (check1,))
            check21 = cur.fetchone()
            currentBalance1 = check21[0]  # W.wallet_available_cash

            if currentBalance1 >= addToBankAcc:  # should have MIN 5000 in bank account
                wal_cash_avail = currentBalance1 - addToBankAcc  # will be 0
                cur.execute("UPDATE	USER_WALLET_T SET wallet_available_cash = ?, "
                            "wallet_total_withdraw = wallet_total_withdraw + ? WHERE user_id = ?",
                            (wal_cash_avail, addToBankAcc, check1,))
                con.commit()

                return jsonify({'wal_avail_cash at P2': wal_avail_cash, 'wal_cash_avail at P3': wal_cash_avail})
            else:
                return jsonify({'Error': 'User has insufficient funds in wallet to withdraw to bank account.'})

        else:
            return jsonify({'Error': 'User has not registered a bank account number yet'})

    else:
        return jsonify({'Error': 'Invalid user_id with the database!!'})


# Phase 1 trigger 1: "First investor buys shares at 8 % valuation."
@phases_bp.route('/phase1/buy/<bus_id>', methods=['POST'])
def phase_one(bus_id):
    p1 = request.json

    # Process 1
    # 1. GET value to be paid and share quantity amount.
    cur.execute("SELECT bus_valuation FROM BUSINESS_T WHERE bus_id = ?", (bus_id,))
    check = cur.fetchone()
    currValue = check[0]
    print(currValue)

    # 2. CALCULATE 8% of valuation
    paymentAmt = 0.08 * currValue
    print(paymentAmt)

    # 3. GET user wallet balance.
    cur.execute("SELECT W.wallet_available_cash FROM USER_WALLET_T W JOIN USER_T U ON W.user_id = U.user_id "
                "WHERE W.user_id = ?", (p1['user_id'],))
    check2 = cur.fetchone()
    currentBalance = check2[0]
    print(currentBalance)

    # 4. Check IF paymentAmt >= currentBalance ELSE Return error.
    if currentBalance >= paymentAmt:
        balance = currentBalance - int(paymentAmt)

        # 5. MINUS payment from user waller amt and ADD to spent cash
        cur.execute("UPDATE	USER_WALLET_T SET wallet_available_cash = ?, wallet_spent_cash = wallet_spent_cash + ? "
                    "WHERE user_id = ?", (balance, int(paymentAmt), p1['user_id'],))

        # 6. SET transaction history
        cur.execute("""INSERT INTO USER_TRANSACT_T (user_id, bus_id, utransact_no, utransact_datetime, utransact_amt, 
        utransact_type, utransact_status) VALUES 
        (?, ?, (SELECT max(utransact_no) FROM USER_TRANSACT_T)+1, ?, ?, 'BUY', 'CF')""",
                    (p1['user_id'], bus_id, currentDateTime, int(paymentAmt),))

        # 7. SET SHARE for USER_SHARE_T
        cur.execute("""INSERT INTO USER_SHARE_T (bus_id, user_id, share_percent, share_buy_value, share_buy_datetime) 
        VALUES (?, ?, 8, ?, ?)""", (bus_id, p1['user_id'], int(paymentAmt), currentDateTime,))

        # 8. UPDATE SHARE for BUS_SHARE_T
        cur.execute("UPDATE	BUS_SHARE_T SET bus_share_current_investor = bus_share_current_investor + 1 "
                    "WHERE bus_id = ?", (bus_id,))

        # 9. ADD funds collected to business report
        cur.execute("UPDATE	BUS_REPORT_T SET rep_total_fund = ?, rep_current_fund = ? WHERE bus_id = ?",
                    (int(paymentAmt), int(paymentAmt), bus_id,))

        con.commit()
    else:
        return jsonify({'Error': 'Insufficient wallet balance'})

    # Process 2
    # 1. Check that the share limit is reached & SSM and BUSINESS LICENCE Registration is COMPLETE
    cur.execute("SELECT bus_share_current_investor, bus_share_approve_limit FROM BUS_SHARE_T WHERE bus_id = ?",
                (bus_id,))
    check3 = cur.fetchone()
    curInvestor = check3[0]
    appLimit = check3[1]

    cur.execute("SELECT bus_ssm_id, bus_lic_no FROM BUSINESS_T WHERE bus_id =?", (bus_id,))
    check4 = cur.fetchone()
    ssm = check4[0]
    lic = check4[1]

    if curInvestor == appLimit:
        if ssm != 'pendingFI' and lic != 'pendingFI':
            # 2. UPDATE BUS_SHARE_T with values to proceed to Phase 2
            cur.execute("UPDATE	BUS_SHARE_T SET bus_share_approve_limit = bus_share_total_investor, "
                        "bus_share_phase = ? WHERE bus_id = ?", (2, bus_id,))
            con.commit()

            return jsonify({'Status': 'Success! End of Phase 1'})
