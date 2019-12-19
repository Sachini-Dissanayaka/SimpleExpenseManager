package lk.ac.mrt.cse.dbs.simpleexpensemanager.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 5;
    private static final String DATABASE_NAME = "170145M";

    private static final String Account_Table = "Account_Table";
    private static final String Account_number = "Account_number";
    private static final String Bank_Name = "Bank_Name";
    private static final String Holder_Name = "Holder_Name";
    private static final String Balance = "Balance";

    private static final String Transaction_Table = "Transaction_Table";
    private static final String Trans_ID = "Trans_ID";
    private static final String ExpenseType = "ExpenseType";
    private static final String  Amount= "Amount";
    private static final String Date= "Date";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_ACCOUNT_TABLE = "CREATE TABLE " + Account_Table + "("
                + Account_number + " TEXT PRIMARY KEY," + Bank_Name + " TEXT ,"
                + Holder_Name + " TEXT ," + Balance + " REAL " + ")";
        db.execSQL(CREATE_ACCOUNT_TABLE);

        String CREATE_TRANSACTION_TABLE = "CREATE TABLE " + Transaction_Table + "("
                + Trans_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Account_number + " TEXT ,"
                + ExpenseType + " TEXT,"
                + Amount + " REAL ," + Date + " TEXT ,FOREIGN KEY ("
                + Account_number +") REFERENCES "+ Account_Table +" ("+ Account_number +"))";
        db.execSQL(CREATE_TRANSACTION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + Account_Table);
        db.execSQL("DROP TABLE IF EXISTS " + Transaction_Table);
        onCreate(db);
    }


}
