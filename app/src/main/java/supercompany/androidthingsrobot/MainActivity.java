package supercompany.androidthingsrobot;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private DatabaseReference dbReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbReference = FirebaseDatabase.getInstance().getReference();

        ValueEventListener directionListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot directionSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot whichWaySnapshot : directionSnapshot.getChildren()) {
                        HashMap hm = (HashMap) whichWaySnapshot.getValue();
                        Log.d(TAG, hm.get("direction").toString());
                        dbReference.removeValue();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "hello, onCancelled called");
            }
        };
        dbReference.addValueEventListener(directionListener);




    }
}
