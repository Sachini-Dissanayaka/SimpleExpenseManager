package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DatabaseHandler;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistentTransactionDAO implements TransactionDAO {

    private DatabaseHandler dh;
    public PersistentTransactionDAO(DatabaseHandler dh) {
        this.dh = dh;

    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SQLiteDatabase db = dh.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("Account_Number", accountNo);
        values.put("ExpenseType",expenseType.name());
        values.put("Amount",amount);
        values.put("Date",new SimpleDateFormat("dd/MM/yyyy").format(date));

        db.insert("Transaction_Table", null, values);
        db.close();
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        List<Transaction> transactionList = new ArrayList<Transaction>();

        String selectQuery = "SELECT  * FROM  Transaction_Table";

        SQLiteDatabase db = dh.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Date date1= null;
                try {
                    date1 = new SimpleDateFormat("dd/MM/yyyy").parse(cursor.getString(4));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(cursor.getString(2)=="EXPENSE") {
                    Transaction trans = new Transaction(date1, cursor.getString(1), ExpenseType.EXPENSE, cursor.getDouble(3));
                    transactionList.add(trans);
                }
                else {
                    Transaction trans = new Transaction(date1, cursor.getString(1), ExpenseType.INCOME, cursor.getDouble(3));
                    transactionList.add(trans);
                }

            } while (cursor.moveToNext());
        }

        return transactionList;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List<Transaction> transactionList = new ArrayList<Transaction>();

        String selectQuery = "SELECT  * FROM  Transaction_Table LIMIT "+limit;

        SQLiteDatabase db = dh.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Date date1= null;
                try {
                    date1 = new SimpleDateFormat("dd/MM/yyyy").parse(cursor.getString(4));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(cursor.getString(2)=="EXPENSE") {
                    Transaction trans = new Transaction(date1, cursor.getString(1), ExpenseType.EXPENSE, cursor.getDouble(3));
                    transactionList.add(trans);
                }
                else {
                    Transaction trans = new Transaction(date1, cursor.getString(1), ExpenseType.INCOME, cursor.getDouble(3));
                    transactionList.add(trans);
                }

            } while (cursor.moveToNext());
        }

        return transactionList;
    }
}
