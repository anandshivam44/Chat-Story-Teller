package com.example.recyclerview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements ProgrammingAdapter.CityAdapterEvents,Communicator {//, View.OnClickListener {
    private static final String TAG = "MyTag";
    ArrayList<String> newArray = new ArrayList<>();
    HashMap<Integer, String> hashMap;
    HashMap<Integer, String> appendingHashMap;
    ProgrammingAdapter mAdapter;
    LinearLayout linearLayout;
    RecyclerView recyclerView;
    int count = 1;
    int i = 1;
    DatabaseReference myRef;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        linearLayout = findViewById(R.id.listView);
        hashMap = new HashMap<Integer, String>();
        appendingHashMap = new HashMap<Integer, String>();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Story");


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        appendingHashMap.put(0, "FALSE#Hi");


        Log.d(TAG, "Before Setting Adapter");
        mAdapter = new ProgrammingAdapter(this, appendingHashMap);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (isOnline()) {
                    addItemtoView(event);
                } else {
                    //fragment part
                    recyclerView.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, "You are Offline", Toast.LENGTH_SHORT).show();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    statusFragment frag=new statusFragment();
                    fragmentTransaction.add(R.id.listView,frag,"VivzFragment");
                    fragmentTransaction.commit();

                }

                return false;
            }
        });

    }

    private void addItemtoView(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (count<=32){
                myRef.child(String.valueOf(count)).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String value = dataSnapshot.getValue(String.class);
                        appendingHashMap.put(count, value);
                        mAdapter.notifyItemInserted(count);
                        count++;
                        recyclerView.smoothScrollToPosition(count);
                        Log.d(TAG, "Value is: " + value);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w(TAG, "Failed to read value.", error.toException());
                    }
                });
            }
        }
    }

    @Override
    public void onCityClicked(int position) {
        //Toast.makeText(this, "position"+" "+daysOfWeek.get(position), Toast.LENGTH_SHORT).show();
    }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    @Override
    public void buttonClickedFromFragment(boolean b) {
        if (isOnline()){
        recyclerView.setVisibility(View.VISIBLE);
        }
        else{
            Toast.makeText(MainActivity.this, "You are still Offline", Toast.LENGTH_SHORT).show();
        }

    }
}
