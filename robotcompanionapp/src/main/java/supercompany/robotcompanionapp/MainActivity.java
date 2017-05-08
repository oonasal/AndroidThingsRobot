package supercompany.robotcompanionapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private FirebaseDatabase mDatabase;
    private Movement movement;
    private String direction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabase = mDatabase.getInstance();

        //listen to button touch events for going forward
        Button forwardButton = (Button) findViewById(R.id.button);
        forwardButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN ) {
                    //codes
                    goForward(v);
                    return true;
                }
                return false;
            }
        });

        //listen to button touch events for going backward
        Button backwardButton = (Button) findViewById(R.id.button2);
        backwardButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN ) {
                    //codes
                    goBackward(v);
                    return true;
                }
                return false;
            }
        });

        //listen to button click events for stopping movement
        Button stopButton = (Button) findViewById(R.id.button3);
        stopButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //codes
                stopRobot(v);
            }
        });

    }

    public void onMovement(String direction) {
        final DatabaseReference dbReference = mDatabase.getReference("directions").push();
        dbReference.child("direction").setValue(direction);
        Log.d(TAG, "onMovement called and processed");
    }

    public void goForward(View view) {
        Log.d(TAG, "Forward!!");
        movement = new Movement("A");
        onMovement(movement.getDirection());

    }

    public void goBackward(View view) {
        Log.d(TAG, "Backing up!!");
        movement = new Movement("B");
        onMovement(movement.getDirection());
    }

    public void stopRobot(View v) {
        Log.d(TAG, "Stopping!");
        movement = new Movement("S");
        onMovement(movement.getDirection());
    }
}
