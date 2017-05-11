package supercompany.robotcompanionapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private FirebaseDatabase mDatabase;
    private Movement movement;
    private String direction;

    private DatabaseReference dbReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabase = mDatabase.getInstance();

        //listen to button touch events for going forward
        Button forwardButton = (Button) findViewById(R.id.button);
        forwardButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                goForward(v);
            }
        });

        //listen to button touch events for going backward
        Button backwardButton = (Button) findViewById(R.id.button2);
        backwardButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                goBackward(v);
            }
        });

        //listen to button click events for going left
        Button leftButton = (Button) findViewById(R.id.button3);
        leftButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                goLeft(v);
            }
        });

        //listen to button click events for going right
        Button rightButton = (Button) findViewById(R.id.button4);
        rightButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                goRight(v);
            }
        });

        //listen to button touch events for going backward
        Button teaBagButton = (Button) findViewById(R.id.button5);
        teaBagButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                goTeaBagging(v);
            }
        });

        //listening to confirmation messages
        dbReference = FirebaseDatabase.getInstance().getReference();
        ValueEventListener directionListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot directionSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot whichWaySnapshot : directionSnapshot.getChildren()) {
                        HashMap hm = (HashMap) whichWaySnapshot.getValue();
                        Log.d(TAG, hm.get("direction").toString());

                        String confirmation = hm.get("direction").toString();

                        if(confirmation.equals("Direction: a")) {

                            Log.d(TAG, "confirmed a");
                            dbReference.removeValue();

                        } else if(confirmation.equals("Direction: b")) {

                            Log.d(TAG, "confirmed b");
                            dbReference.removeValue();

                        } else if(confirmation.equals("Direction: l")) {

                            Log.d(TAG, "confirmed l");
                            dbReference.removeValue();

                        } else if(confirmation.equals("Direction: r")) {

                            Log.d(TAG, "confirmed r");
                            dbReference.removeValue();

                        } else {
                            Log.d(TAG, "something else");
                        }
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled called");
            }
        };
        dbReference.addValueEventListener(directionListener);
    }

    public void onMovement(String direction) {
        final DatabaseReference dbReference = mDatabase.getReference("directions").push();
        dbReference.child("direction").setValue(direction);
        Log.d(TAG, "onMovement called and processed");
    }

    public void goForward(View view) {
        Log.d(TAG, "Forward!!");
        movement = new Movement("a");
        onMovement(movement.getDirection());
    }

    public void goBackward(View view) {
        Log.d(TAG, "Backing up!!");
        movement = new Movement("b");
        onMovement(movement.getDirection());
    }

    public void goLeft(View v) {
        Log.d(TAG, "Going left!");
        movement = new Movement("l");
        onMovement(movement.getDirection());
    }

    public void goRight(View v) {
        Log.d(TAG, "Going right!");
        movement = new Movement("r");
        onMovement(movement.getDirection());
    }

    public void goTeaBagging(View v) {
        Log.d(TAG, "Going up and down!");
        movement = new Movement("e");
        onMovement(movement.getDirection());
    }
}
