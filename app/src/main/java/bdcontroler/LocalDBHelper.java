package bdcontroler;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LocalDBHelper extends SQLiteOpenHelper {

    private static final String NOME_BANCO = "nosso_bd";
    private static final int VERSAO = 1;

    public LocalDBHelper(Context context) {
        super(context, NOME_BANCO, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUsuarios = "CREATE TABLE usuarios " +
                "(email VARCHAR(255) PRIMARY KEY," +
                "senha VARCHAR(255), " +
                "sync INTEGER)";
        db.execSQL(createUsuarios);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Implementar como as atualizações no banco de dados deverão ser feitas no BD do usuário
    }

    public String joinText(String[] values, String delimiter, boolean putMarks) {
        String finalValue = "";
        for (int i = 0; i < values.length; i++) {
            String value;
            if (putMarks) {
                value = '"' + values[i] + '"';
            } else {
                value = values[i];
            }
            finalValue += value;
            if (i != values.length - 1) {
                finalValue += ",";
            }
        }
        return finalValue;
    }

    public void insert(String tableName, String[] columns, String[] values, int sync) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "INSERT INTO " + tableName + " (" + joinText(columns, ",", false)
                + ") VALUES (" + joinText(values, ",", true) + "," + sync + ");";
        db.execSQL(sql);
    }

    public Cursor select(String tableName, String whereClause, String[] args) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + tableName + " " + whereClause, args);
        return cursor;
    }

    public Cursor selectAll(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + tableName, null);
        return cursor;
    }

    public void update(String tableName, String setClause, String whereClause, String[] args) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "UPDATE " + tableName + " SET " + setClause + " WHERE " + whereClause + ";";
        db.execSQL(sql, args);
    }

    public void delete(String tableName, String whereClause, String[] args) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "DELETE FROM " + tableName + "WHERE " + whereClause + ";";
        db.execSQL(sql, args);
    }
}
