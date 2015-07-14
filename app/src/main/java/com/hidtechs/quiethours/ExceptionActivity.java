package com.hidtechs.quiethours;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class ExceptionActivity extends AppCompatActivity implements View.OnClickListener {

    private  String name;
    private String number;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    TextView addcontacts,exceptions,arrow1,arrow2;
    TextView allcontacts;
    SwitchCompat s1;
    ImageView addButton;
    LinearLayout l4,l5;
    Intent intent;
    final int PICK_CONTACT=1;
    List<Information> data;
    int icon;
    boolean allContactsClicked,exceptionsClicked=false;
    SharedPreferences preferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exception);
        data=new ArrayList<>();
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        allcontacts= (TextView) findViewById(R.id.allContacts);
        exceptions= (TextView) findViewById(R.id.exceptions);
        s1= (SwitchCompat) findViewById(R.id.calls_switch);
        arrow1= (TextView) findViewById(R.id.arrow1);
        arrow2= (TextView) findViewById(R.id.arrow2);
        addButton= (ImageView) findViewById(R.id.add_button);
        l4= (LinearLayout) findViewById(R.id.linearLayout4);
        l5= (LinearLayout) findViewById(R.id.linearLayout5);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setVisibility(mRecyclerView.GONE);
        l4.setVisibility(l4.GONE);
        l5.setVisibility(l5.GONE);
        preferences = getSharedPreferences("MyFiles",MODE_PRIVATE);
        boolean cbpref = preferences.getBoolean("callsButton", false);
        allContactsClicked =preferences.getBoolean("allcontacts",false);
        exceptionsClicked =preferences.getBoolean("exceptions",false);
        s1.setChecked(cbpref);
        addcontacts= (TextView) findViewById(R.id.add_contact);
        addButton.setOnClickListener(this);
        s1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("callsButton", s1.isChecked());
                editor.commit();
            }
        });
        allcontacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allContactsClicked=true;
                exceptionsClicked=false;
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("allcontacts", allContactsClicked);
                editor.putBoolean("exceptions", exceptionsClicked);
                editor.commit();
                arrow1.setVisibility(arrow1.VISIBLE);
                arrow2.setVisibility(arrow2.GONE);
                exceptions.setTextColor(getResources().getColor(R.color.primary_dark_material_dark));
                arrow2.setTextColor(getResources().getColor(R.color.primary_dark_material_dark));
                allcontacts.setTextColor(getResources().getColor(R.color.accentColor));
                arrow1.setTextColor(getResources().getColor(R.color.accentColor));
                mRecyclerView.setVisibility(mRecyclerView.GONE);
                l4.setVisibility(l4.GONE);
                l5.setVisibility(l5.GONE);

            }
        });
        exceptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allContactsClicked=false;
                exceptionsClicked=true;
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("exceptions", exceptionsClicked);
                editor.putBoolean("allcontacts",allContactsClicked);
                editor.commit();
                arrow1.setVisibility(arrow1.GONE);
                arrow2.setVisibility(arrow2.VISIBLE);
                allcontacts.setTextColor(getResources().getColor(R.color.primary_dark_material_dark));
                arrow1.setTextColor(getResources().getColor(R.color.primary_dark_material_dark));
                exceptions.setTextColor(getResources().getColor(R.color.accentColor));
                arrow2.setTextColor(getResources().getColor(R.color.accentColor));
                mRecyclerView.setVisibility(mRecyclerView.VISIBLE);
                l4.setVisibility(l4.VISIBLE);
                l5.setVisibility(l5.VISIBLE);

            }
        });

        mRecyclerView.setLayoutManager(mLayoutManager);
        if (allContactsClicked)
        {
            allcontacts.performClick();
        }
        else if(exceptionsClicked)
        {
            exceptions.performClick();
        }
    }


    public  List<Information> getData()
    {
        Information current=new Information();
        current.name = name;
        current.iconId = icon;
        current.number=number;
        data.add(current);
        return data;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==addButton.getId())
        {
            intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent, PICK_CONTACT);

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // If the request went well (OK) and the request was PICK_CONTACT_REQUEST
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_CONTACT) {
            // Perform a query to the contact's content provider for the contact's name
            Cursor cursor = managedQuery(data.getData(), null, null, null, null);
            while (cursor.moveToNext()) {
                name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                Log.d("HELLO : ", " "+contactId);
                String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                Log.d("HELLO : ", " "+hasPhone);
                if ((Integer.parseInt(hasPhone)==1 )){
                    Log.d("HELLO : ", " IN if");
                    Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId,
                            null, null);
                    while (phones.moveToNext()) {
                        number = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    }
                    phones.close();
                }
                Log.d("HELLO : ", " "+name);
                Log.d("HELLO : ", " "+number);
                icon=R.drawable.remove;
                mAdapter= new MyAdapter(this,getData());
                mRecyclerView.setAdapter(mAdapter);
            }
        }

    }}
