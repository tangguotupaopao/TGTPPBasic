package com.tangguotupaopao.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.tangguotupaopao.greendao.dao.Contacts;
import com.tangguotupaopao.greendao.dao.ContactsDao;
import com.tangguotupaopao.greendao.dao.DaoMaster;
import com.tangguotupaopao.greendao.dao.DaoSession;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private ContactsDao contactsDao;
    private Cursor cursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "notes-db", null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        contactsDao = daoSession.getContactsDao();

        String order = ContactsDao.Properties.Id.columnName+" COLLATE LOCALIZED ASC";
        cursor = db.query(contactsDao.getTablename(), contactsDao.getAllColumns(),
                null,null,null,null, order);


        final EditText edName = (EditText)findViewById(R.id.ed_name);
        final EditText edPhone = (EditText)findViewById(R.id.ed_phone);
        Button ok = (Button)findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edName.getText().toString();
                edName.setText("");
                String phone = edPhone.getText().toString();
                edPhone.setText("");
                addContact(name, phone);
            }
        });

        ListView listView = (ListView)findViewById(R.id.listView);
        listView.setAdapter(new SimpleCursorAdapter(this,android.R.layout.simple_list_item_2,
                cursor, new String[]{ContactsDao.Properties.Name.columnName,
                                     ContactsDao.Properties.Phone.columnName},
                        new int[]{android.R.id.text1, android.R.id.text2}
                ));

    }

    private void addContact(String name, String phone){
        if(name.equals("")||phone.equals(""))
            return;
        Contacts c = new Contacts(null, name, phone);
        contactsDao.insert(c);
        cursor.requery();
    }
}
