package fu.alfie.com.contentproviderexdemoa;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ContentResolver contentResolver;
    private EditText e_account_id;
    private EditText e_mobile;
    private EditText e_mail;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contentResolver = getContentResolver();

        e_account_id = (EditText)findViewById(R.id.account_id);
        e_mobile = (EditText)findViewById(R.id.mobile);
        e_mail = (EditText)findViewById(R.id.mail);
        textView = (TextView)findViewById(R.id.textView);
    }

    public void onInsertClick(View view){
        ContentValues contentValues = new ContentValues();
        contentValues.put("account_id", e_account_id.getText().toString());
        contentValues.put("mobile", e_mobile.getText().toString());
        contentValues.put("mail", e_mail.getText().toString());
        contentResolver.insert(MyContentProvider.CONTENT_URI, contentValues);
    }
    public void onUpdateClick(View view){
        String where = "account_id like ?";
        String [] selectionArgs = new String[] {e_account_id.getText().toString()};
        ContentValues contentValues = new ContentValues();
        contentValues.put("account_id", e_account_id.getText().toString());
        contentValues.put("mobile", e_mobile.getText().toString());
        contentValues.put("mail", e_mail.getText().toString());
        int update_num = contentResolver.update(MyContentProvider.CONTENT_URI, contentValues, where, selectionArgs);
        Toast.makeText(this,"已更新"+String.valueOf(update_num)+"筆資料",Toast.LENGTH_LONG).show();
    }
    public void onDeleteClick(View view){
        String where = "account_id like ?";
        String [] selectionArgs = new String[] {e_account_id.getText().toString()};
        int delete_num = contentResolver.delete(MyContentProvider.CONTENT_URI, where, selectionArgs);
        Toast.makeText(this,"已刪除"+String.valueOf(delete_num)+"筆資料",Toast.LENGTH_LONG).show();
    }
    public void onSearchClick(View view){
        Cursor cursor = null;

        String[] projection = new String[]{"account_id", "mobile", "mail"};

        if (!e_account_id.getText().toString().equals("")) {
            cursor = contentResolver.query(MyContentProvider.CONTENT_URI, projection,
                    "account_id=" + "\"" + e_account_id.getText().toString() + "\"",
                    null, null);
        } else if (!e_mobile.getText().toString().equals("")) {
            cursor = contentResolver.query(MyContentProvider.CONTENT_URI, projection,
                    "mobile=" + "\"" + e_mobile.getText().toString() + "\"",
                    null, null);

        } else if (!e_mail.getText().toString().equals("")) {
            cursor = contentResolver.query(MyContentProvider.CONTENT_URI, projection,
                    "mail=" + "\"" + e_mail.getText().toString() + "\"",
                    null, null);
        }

        if (cursor == null)
            return;

        if (cursor.getCount() == 0) {
            textView.setText("");
            Toast.makeText(MainActivity.this, "沒有這筆資料", Toast.LENGTH_LONG)
                    .show();
        } else {
            cursor.moveToFirst();
            textView.setText(cursor.getString(0) + cursor.getString(1)  + cursor.getString(2));

            while (cursor.moveToNext())
                textView.append("\n" + cursor.getString(0) + cursor.getString(1) +
                        cursor.getString(2));
        }
    }
    public void onListClick(View view){
        String[] projection = new String[]{"account_id", "mobile", "mail"};

        Cursor cursor = contentResolver.query(MyContentProvider.CONTENT_URI, projection,
                null, null, null);

        if (cursor == null)
            return;

        if (cursor.getCount() == 0) {
            textView.setText("");
            Toast.makeText(MainActivity.this, "沒有資料", Toast.LENGTH_LONG)
                    .show();
        }
        else {
            cursor.moveToFirst();
            textView.setText(cursor.getString(0) + cursor.getString(1)  + cursor.getString(2));

            while (cursor.moveToNext())
                textView.append("\n" + cursor.getString(0) + cursor.getString(1)  +
                        cursor.getString(2));
        }
    }
    public void onClearClick(View view){
        textView.setText("");
    }
}
