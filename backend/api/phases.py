from flask import Flask, Blueprint, request, jsonify
from dateutil.relativedelta import relativedelta
from werkzeug.utils import secure_filename
import sqlite3
import datetime
import random
import os

phases_bp = Blueprint('phases', __name__)

# CONNECTING TO DATABASE FOR STORING AND READING PURPOSES
con = sqlite3.connect('bizBangkit.db', check_same_thread=False)
cur = con.cursor()
con.commit()

# FOR UPLOADING FILE (IMAGE) INTO THE SYSTEM
app = Flask(__name__)
UPLOAD_FOLDER = './pictures/'
ALLOWED_EXTENSIONS = {'txt', 'pdf', 'png', 'jpg', 'jpeg', 'gif'}
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER

currentDateTime = datetime.datetime.now()
currentDate = datetime.date.today()


def allowed_file(filename):
    return '.' in filename and filename.rsplit('.', 1)[1].lower() in ALLOWED_EXTENSIONS


def upload_image():
    file = request.files['file']
    path = ''

    if file.filename == '':  # If no file is selected, default picture will be used
        path = './pictures/default.png'

    if file and allowed_file(file.filename):
        # stores the name of the file uploaded
        filename = str(random.randint(0, 99999)) + secure_filename(file.filename)
        path = (os.path.join(app.config['UPLOAD_FOLDER'], filename))  # save uploaded image under the filename
        file.save(path)

    return path


# Get wallet balance
@phases_bp.route('/wallet/balance/<user_id>', methods=['GET'])
def get_balance(user_id):
    cur.execute("SELECT wallet_available_cash FROM USER_WALLET_T WHERE user_id = ?", (user_id, ))
    balance = cur.fetchone()

    return jsonify({'response': 200, 'balance': balance})


# Wallet Process for transaction purposes
@phases_bp.route('/wallet/<user_authkey>', methods=['POST'])
def wallet(user_authkey):
    wal = request.form
    ''' request needs 
    'user_id' : user id 
    'amount'  : withdrawal or deposit amount
    'action'  : 'withdraw' or 'reload'
    '''

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

            changeToWallet = int(wal['amount'])

            cur.execute("SELECT wallet_available_cash FROM USER_WALLET_T "
                        "WHERE user_id = ?", (check1,))
            check2 = cur.fetchone()
            currentBalance = check2[0]  # wallet_available_cash

            # P2: Reload to USER_WALLET_T from bank account
            if wal['action'] == 'reload':
                wal_avail_cash = currentBalance + changeToWallet

                cur.execute("UPDATE	USER_WALLET_T SET wallet_available_cash = ?, "
                            "wallet_total_deposit = wallet_total_deposit + ? WHERE user_id = ?",
                            (wal_avail_cash, changeToWallet, check1,))
                con.commit()

                return jsonify({'response': 200, 'balance': wal_avail_cash})

            # P3: withdraw from USER_WALLET_T and into bank account
            elif wal['action'] == 'withdraw':
                if currentBalance >= changeToWallet:  # should have more than withdrawal amount in eWallet
                    wal_avail_cash = currentBalance - changeToWallet  # will be 0
                    cur.execute("UPDATE	USER_WALLET_T SET wallet_available_cash = ?, "
                                "wallet_total_withdraw = wallet_total_withdraw + ? WHERE user_id = ?",
                                (wal_avail_cash, changeToWallet, check1,))
                    con.commit()

                    return jsonify({'response': 200, 'balance': wal_avail_cash})
                else:
                    return jsonify({'response': 401, 'Error': 'User has insufficient funds in wallet to withdraw to bank account.'})

            else:
                return jsonify({'response': 401, 'Error': 'Invalid action request to eWallet'})

        else:
            return jsonify({'response': 401, 'Error': 'User has not registered a bank account number yet'})

    else:
        return jsonify({'response': 401, 'Error': 'Invalid user_id from the database!!'})


@phases_bp.route('/phase1/buy/<bus_id>', methods=['POST'])
def phase_one(bus_id):
    p1 = request.json

    # Process 1
    # Phase 1 trigger 1: "First investor buys shares at 8 % valuation."

    # 1. GET value to be paid and share quantity amount.
    cur.execute("SELECT bus_valuation FROM BUSINESS_T WHERE bus_id = ?", (bus_id,))
    check = cur.fetchone()
    currValue = check[0]

    # 2. CALCULATE 8% of valuation
    paymentAmt = 0.08 * currValue

    # 3. GET user wallet balance.
    cur.execute("SELECT W.wallet_available_cash FROM USER_WALLET_T W JOIN USER_T U ON W.user_id = U.user_id "
                "WHERE W.user_id = ?", (p1['user_id'],))
    check2 = cur.fetchone()
    currentBalance = check2[0]

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
    # Phase 1 trigger 2: Business SSM & small business licence registered

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


@phases_bp.route('/phase2/buy/<bus_id>', methods=['POST'])
def phase_two(bus_id):
    p2 = request.json

    # Process 1
    # Phase 2 trigger 1: investors buys shares at 5%

    # 1. GET value to be paid and share quantity amount.
    cur.execute("SELECT bus_valuation FROM BUSINESS_T WHERE bus_id = ?", (bus_id,))
    check = cur.fetchone()
    currValue = check[0]

    # 2. CALCULATE 5% of valuation
    paymentAmt = 0.05 * currValue

    # 3. GET user wallet balance.
    cur.execute("SELECT W.wallet_available_cash FROM USER_WALLET_T W JOIN USER_T U ON W.user_id = U.user_id "
                "WHERE W.user_id = ?", (p2['user_id'],))
    check2 = cur.fetchone()
    currentBalance = check2[0]

    # 4. Check IF paymentAmt >= currentBalance ELSE Return error.
    if currentBalance >= paymentAmt:
        balance = currentBalance - int(paymentAmt)

        # 5. MINUS payment from user waller amt and ADD to spent cash
        cur.execute("UPDATE	USER_WALLET_T SET wallet_available_cash = ?, wallet_spent_cash = wallet_spent_cash + ? "
                    "WHERE user_id = ?", (balance, int(paymentAmt), p2['user_id'],))

        # 6. SET transaction history
        cur.execute("""INSERT INTO USER_TRANSACT_T (user_id, bus_id, utransact_no, utransact_datetime, utransact_amt, 
                utransact_type, utransact_status) VALUES 
                (?, ?, (SELECT max(utransact_no) FROM USER_TRANSACT_T)+1, ?, ?, 'BUY', 'CF')""",
                    (p2['user_id'], bus_id, currentDateTime, int(paymentAmt),))

        # 7. SET SHARE for USER_SHARE_T
        cur.execute("""INSERT INTO USER_SHARE_T (bus_id, user_id, share_percent, share_buy_value, share_buy_datetime) 
                VALUES (?, ?, 5, ?, ?)""", (bus_id, p2['user_id'], int(paymentAmt), currentDateTime,))

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
    # Phase 2 trigger 2: Total of seven successfully investors reached

    # 1. Check that the share limit is reached (Phase 2 is max investor limit (7 for new business proposals))
    cur.execute("SELECT bus_share_current_investor, bus_share_approve_limit FROM BUS_SHARE_T WHERE bus_id = ?",
                (bus_id,))
    check3 = cur.fetchone()
    curInvestor = check3[0]
    appLimit = check3[1]

    if curInvestor == appLimit:
        # 2. UPDATE BUSINESS_T with start date (phase 3) and end date (phase 4)
        # 3. UPDATE BUS_SHARE_T with values to proceed to Phase 3.
        six_months = currentDateTime + relativedelta(months=+6)
        print(six_months)
        cur.execute("UPDATE	BUS_SHARE_T SET	bus_share_phase = ?, "
                    "bus_share_maturity_date = ? WHERE bus_id = ?", (3, six_months, bus_id,))
        con.commit()

        one_year = currentDateTime + relativedelta(months=+12)
        print(one_year)
        cur.execute("UPDATE BUSINESS_T SET bus_start_date = ?, bus_end_date = ?, bus_share_phase = ? WHERE bus_id = ?",
                    (currentDateTime, one_year, 3, bus_id,))
        con.commit()

        return jsonify({'Status': 'Success! End of phase 2'})
    else:
        return jsonify({'Status': 'Number of current investor is still within the limit of Phase 2'})


@phases_bp.route('/phase3/buy/<bus_id>', methods=['POST'])
def phase_three(bus_id):
    p3 = request.form  # because need to upload proof of transaction

    cur.execute("SELECT bus_share_maturity_date FROM BUS_SHARE_T WHERE bus_id = ?", (bus_id,))
    check = cur.fetchone()
    newDate = check[0]
    matureDate = newDate[0:10]
    print(matureDate)

    if matureDate < str(currentDate):
        return jsonify({'Error': 'Shares cannot be sold!'})  # Phase 3 has ended before the currentDateTime
    else:
        # Process 1
        # Phase 3 trigger 1: Business spends from account

        # 1. Set the spend amount
        spendAmt = p3['spend_amt']
        # string descriptionOfTransaction = p3['transact_desc']

        # 2. GET balance of funds
        cur.execute("SELECT rep_current_fund FROM BUS_REPORT_T WHERE bus_id = ?", (bus_id,))
        check1 = cur.fetchone()
        currentBalance = check1[0]

        # 3. MINUS spend amount from BUS_REPORT_T
        cur.execute("UPDATE	BUS_REPORT_T SET rep_current_fund = rep_current_fund - ?, "
                    "rep_total_spent = rep_total_spent + ? WHERE bus_id = ?", (spendAmt, spendAmt, bus_id,))
        con.commit()

        #  4. CREATE TRANSACTION RECORD
        proof = upload_image()  # proof of transaction
        cur.execute("""INSERT INTO BUS_TRANSACT_T (rep_transact_id, bus_id, rep_transact_datetime, 
        rep_transact_decription, rep_transact_tos_flag, rep_transact_fpath_updatepic, rep_transact_amt, 
        rep_transact_type) VALUES ((SELECT max(rep_transact_id) FROM BUS_TRANSACT_T)+1, ?, ?, ?, 'A', ?, ?, 'SP')""",
                    (bus_id, currentDateTime, p3['transact_desc'], proof, spendAmt,))
        con.commit()

    # Process 2
    # Phase 3 trigger 2: Business deposits to account.

    # 1. SET the deposited amount
    depositAmt = p3['deposit_amt']
    descriptionOfTransaction = 'Successful Deposit!'

    # 2. ADD income amount to BUS_REPORT_T
    cur.execute("UPDATE	BUS_REPORT_T SET rep_current_fund = rep_current_fund + ?, "
                "rep_total_income = rep_total_income + ? WHERE bus_id = ?", (depositAmt, depositAmt, bus_id,))
    con.commit()

    # 3. CREATE TRANSACTION RECORD
    proof = upload_image()  # proof of transaction
    cur.execute("""INSERT INTO BUS_TRANSACT_T (rep_transact_id, bus_id, rep_transact_datetime, 
            rep_transact_decription, rep_transact_tos_flag, rep_transact_fpath_updatepic, rep_transact_amt, 
            rep_transact_type) VALUES ((SELECT max(rep_transact_id) FROM BUS_TRANSACT_T)+1, ?, ?, ?, 'A', ?, ?, 'EA')""",
                (bus_id, currentDateTime, descriptionOfTransaction, proof, depositAmt,))
    con.commit()

    # Process 3
    # IF (bus_share_maturity_date == <INPUT: date.today> )
    # Phase 3 trigger 3: six months after start date

    if matureDate == str(currentDate):
        # 1. GET revenue from BUS_REPORT_T
        cur.execute("SELECT rep_total_income, rep_total_spent FROM BUS_REPORT_T WHERE bus_id = ? ", (bus_id,))
        check2 = cur.fetchone()
        totalIncome = check2[0]
        totalExpenses = check2[1]

        # 2. Calculate the revenue (Python)
        revenue = totalIncome - totalExpenses

        # 3. GET starting capital from BUSINESS_T
        cur.execute("SELECT	bus_valuation FROM BUSINESS_T B WHERE bus_id = ?", (bus_id,))
        check3 = cur.fetchone()
        valueAtStart = check3[0]

        # 4. Calculate the new valuation
        capitalAtStart = 0.04 * valueAtStart
        capitalNow = capitalAtStart + revenue
        updatedValuation = (capitalNow / 4) * 10

        # 5. UPDATE to current business valuation
        cur.execute("UPDATE	BUSINESS_T SET bus_valuation = ? WHERE bus_id = ?", (updatedValuation, bus_id))
        con.commit()

    # Process 4 (trigger 4 and trigger 5)
    # IF ( ( USER_SHARE_T.share_percent == 8 ) && ( BUS_SHARE_T.bus_share_maturity_date == <INPUT: date.today> )
    # && ( USER_TRANSACT_T.bus_id == BUSINESS_T.bus_id ) )
    # Phase 3 trigger 4: investors sell shares 8% during 24 hours at end of phase 3
    # Phase 3 trigger 5: investors sell shares 5% during 24 hours.

    # 1. GET user share for this business
    cur.execute("SELECT	share_percent FROM USER_SHARE_T WHERE user_id = ?", (p3['user_id'],))
    check4 = cur.fetchone()
    sharePercent = check4[0]  # 8% or 5%

    # 2. GET Share maturity date.
    # Reuse the code from Phase 3 Trigger 3

    # 3. CHECK IF Share can be sold right now?
    if matureDate == str(currentDate):
        # 4. GET updated valuation in BUSINESS_T
        cur.execute("SELECT bus_valuation FROM BUSINESS_T WHERE bus_id = ?", (bus_id,))
        check5 = cur.fetchone()
        currentValue = check5[0]

        # 5. Calculate the selling value.
        sellValue = sharePercent * currentValue
        timeOfSale = datetime.datetime.now()

        # 6. ADD share sale amount to USER_WALLET_T
        cur.execute("UPDATE	USER_WALLET_T SET wallet_available_cash = wallet_available_cash + ?, "
                    "wallet_earned_cash = wallet_earned_cash + ? WHERE user_id = ?",
                    (sellValue, sellValue, p3['user_id'],))
        con.commit()

        # 7. CREATE transaction record in USER_TRANSACT_T
        cur.execute("""INSERT INTO USER_TRANSACT_T (utransact_no, user_id, bus_id, utransact_datetime, utransact_amt, 
        utransact_type, utransact_status) VALUES ((SELECT max(utransact_no) FROM USER_TRANSACT_T)+1, ?, ?, ?, ?, 
        'SEL', 'CF')""", (p3['user_id'], bus_id, timeOfSale, sellValue,))
        con.commit()

        # 8. UPDATE USER_SHARE_T
        cur.execute("UPDATE	USER_SHARE_T SET share_sell_value = ?, share_sell_datetime = ? "
                    "WHERE user_id = ? AND bus_id = ?", (sellValue, timeOfSale, p3['user_id'], bus_id,))
        con.commit()

        # 9. UPDATE BUS_SHARE_T
        cur.execute("UPDATE	BUS_SHARE_T SET bus_share_total_divestor = bus_share_total_divestor + 1 WHERE bus_id = ?",
                    (bus_id,))
        con.commit()

        # 10. UPDATE BUS_REPORT_T to reflect sale of capital
        cur.execute("UPDATE	BUS_REPORT_T SET rep_current_fund = rep_current_fund - ? WHERE bus_id = ?",
                    (sellValue, bus_id,))
        con.commit()

        # 11. UPDATE the BUSINESS_OWNER_T
        cur.execute("UPDATE	BUSINESS_OWNER_T SET share_alloc_qty = share_alloc_qty + ? WHERE bus_id = ?",
                    (sharePercent, bus_id,))
        con.commit()

        return jsonify({'Status': 'Selling is allowed and new share data have been added!'})

    # Process 5
    # IF ( bus_share_maturity_date MORE THAN <INPUT: date.today> )
    # Phase 3 trigger 6: six months + 1 day passed.
    elif matureDate > str(currentDate):
        # 1. UPDATE BUSINESS SHARE PHASE in  BUS_SHARE_T
        cur.execute("UPDATE	BUS_SHARE_T SET bus_share_phase = ? WHERE bus_id = ?", (4, bus_id,))
        con.commit()

        return jsonify({'Status': 'Selling is allowed and business is in share phase 4'})

    else:
        return jsonify({'Error': 'Share has not yet mature or Share Selling Period has not yet ended'})


@phases_bp.route('/phase4/buy/<bus_id>', methods=['POST'])
def phase_four(bus_id):
    p4 = request.form

    # Process 1

    # 1. Set the spend amount
    spendAmt = p4['spend_amt']
    # string descriptionOfTransaction = p4['transact_desc']

    # 2. GET balance of funds
    cur.execute("SELECT rep_current_fund FROM BUS_REPORT_T WHERE bus_id = ?", (bus_id,))
    check = cur.fetchone()
    currentBalance = check[0]

    # 3. MINUS spend amount from BUS_REPORT_T
    cur.execute("UPDATE	BUS_REPORT_T SET rep_current_fund = rep_current_fund - ?, rep_total_spent = rep_total_spent + ?"
                " WHERE bus_id = ?", (spendAmt, spendAmt, bus_id,))
    con.commit()

    # 4. CREATE TRANSACTION RECORD
    proof = upload_image()  # proof of transaction
    cur.execute("""INSERT INTO BUS_TRANSACT_T (rep_transact_id, bus_id, rep_transact_datetime, 
    rep_transact_decription, rep_transact_tos_flag, rep_transact_fpath_updatepic, rep_transact_amt, 
    rep_transact_type) VALUES ((SELECT max(rep_transact_id) FROM BUS_TRANSACT_T)+1, ?, ?, ?, 'A', ?, ?, 'EA')""",
                (bus_id, currentDateTime, p4['transact_desc'], proof, spendAmt,))
    con.commit()

    # Process 2
    # Phase 3 trigger 2: Business deposits to account

    # 1. SET the deposited amount
    depositAmt = p4['deposit_amt']
    descriptionOfTransaction = 'Successful Deposit!'

    # 2. ADD income amount to BUS_REPORT_T
    cur.execute("UPDATE	BUS_REPORT_T SET rep_current_fund = rep_current_fund + ?, "
                "rep_total_income = rep_total_income + ? WHERE bus_id = ?", (depositAmt, depositAmt, bus_id,))
    con.commit()

    # 3. CREATE TRANSACTION RECORD
    cur.execute("""INSERT INTO BUS_TRANSACT_T (rep_transact_id, bus_id, rep_transact_datetime, 
        rep_transact_decription, rep_transact_tos_flag, rep_transact_fpath_updatepic, rep_transact_amt, 
        rep_transact_type) VALUES ((SELECT max(rep_transact_id) FROM BUS_TRANSACT_T)+1, ?, ?, ?, 'A', ?, ?, 'EA')""",
                (bus_id, currentDateTime, descriptionOfTransaction, proof, depositAmt,))
    con.commit()

    # Process 3

    # 1. GET the end date from BUSINESS_T
    cur.execute("SELECT bus_end_date FROM BUSINESS_T WHERE bus_id = ?", (bus_id,))
    check1 = cur.fetchone()
    newDate = check1[0]
    endOfIncubation = newDate[0:10]

    # 2. Check if business has reached end date
    if endOfIncubation > str(currentDate):
        # 4. UPDATE BUSINESS SHARE PHASE in  BUS_SHARE_T
        cur.execute("UPDATE	BUS_SHARE_T SET bus_share_phase = ? WHERE bus_id = ?", (5, bus_id,))
        con.commit()

        return jsonify({'Status': 'Business has completed funding on platform'})
    else:
        return jsonify({'Error': 'Business has not yet finished the incubation period'})
