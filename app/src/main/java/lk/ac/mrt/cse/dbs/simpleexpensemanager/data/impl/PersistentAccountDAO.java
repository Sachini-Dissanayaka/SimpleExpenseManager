package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import java.util.List;
import java.util.ArrayList;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DatabaseHandler;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class PersistentAccountDAO implements AccountDAO {

    private DatabaseHandler dh;
    public PersistentAccountDAO(DatabaseHandler dh) {
        this.dh = dh;
    }

    @Override
    public List<String> getAccountNumbersList() {
        List<String> accountNumberList = new ArrayList<String>();
        String selectQuery = "SELECT  Account_number FROM  Account_Table";

        SQLiteDatabase db = dh.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                accountNumberList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        return accountNumberList;
    }

    @Override
    public List<Account> getAccountsList() {
        List<Account> accountList = new ArrayList<Account>();

        String selectQuery = "SELECT  * FROM  Account_Table";

        SQLiteDatabase db = dh.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Account account = new Account(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getDouble(3));
                accountList.add(account);
            } while (cursor.moveToNext());
        }

        return accountList;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = dh.getReadableDatabase();

        Cursor cursor = db.query("Account_Table", new String[] { "Account_Number",
                        "Bank_Name", "Holder_Name","Balance" }, "Account_Number" + "=?",
                new String[] { String.valueOf(accountNo) }, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();

            Account account = new Account(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getDouble(3));
            return account;
        }
        String msg = "Account " + accountNo + " is invalid.";
        throw new InvalidAccountException(msg);

    }

    @Override
    public void addAccount(Account account) {

        SQLiteDatabase db = dh.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("Account_Number", account.getAccountNo());
        values.put("Bank_Name",account.getBankName());
        values.put("Holder_Name",account.getAccountHolderName());
        values.put("Balance",account.getBalance());

        db.insert("Account_Table", null, values);
        db.close();

    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {

        SQLiteDatabase db = dh.getWritableDatabase();
        db.delete("Account_Table",  "Account_Number"+ " = ?",
                new String[] { accountNo });
        db.close();
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        Account account = this.getAccount(accountNo);
        SQLiteDatabase db = dh.getWritableDatabase();

        ContentValues values = new ContentValues();

        switch (expenseType) {
            case EXPENSE:
                values.put("Balance" ,account.getBalance() - amount);
                break;
            case INCOME:
                values.put("Balance",account.getBalance() + amount);
                break;
        }

        db.update("Account_Table", values,  "Account_Number"+ " = ?",
                new String[] { accountNo});
    }
}
