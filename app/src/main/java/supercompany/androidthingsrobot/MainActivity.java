package supercompany.androidthingsrobot;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.things.pio.PeripheralManagerService;
import com.google.android.things.pio.UartDevice;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private DatabaseReference dbReference;

    private Handler mHandler = new Handler();
    //private Gpio mButtonGpio;
    //private Gpio mLedGpio;
    private static final String UART_DEVICE_NAME = "UART0"; // UART Device Name
    private UartDevice mDevice;
    //Button bton,bton2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "oncreate called");

        //not in the layout atm
        //bton=(Button)findViewById(R.id.button2);
        //bton2=(Button)findViewById(R.id.button3);

        // Attempt to access the UART device
        try {
            PeripheralManagerService manager = new PeripheralManagerService();

            mDevice = manager.openUartDevice(UART_DEVICE_NAME);
        } catch (IOException e) {
            Log.w(TAG, "Unable to access UART device", e);
        }

        // Configure the UART port
        try {
            mDevice.setBaudrate(9600);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mDevice.setDataSize(8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mDevice.setParity(UartDevice.PARITY_NONE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mDevice.setStopBits(1);
        } catch (IOException e) {
            e.printStackTrace();
        }


        //PeripheralManagerService service = new PeripheralManagerService();
        //needs configurations first
        //try {
            //String pinName = BoardDefaults.getGPIOForButton();
            //mButtonGpio = service.openGpio(pinName);
            //mButtonGpio.setDirection(Gpio.DIRECTION_IN);
            //mButtonGpio.setEdgeTriggerType(Gpio.EDGE_FALLING);
            //String pinName2 = "BCM6";
            //mLedGpio = service.openGpio(pinName2);
            //mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            //Log.i(TAG, "Start blinking LED GPIO pin");

            /*bton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    mHandler.post(mUART1Runnable);*/
                    /*try {
                        mLedGpio.setValue(!mLedGpio.getValue());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/
                //}
            /*});

            bton2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    mHandler.post(mUART2Runnable);*/
                    /*try {
                        mLedGpio.setValue(!mLedGpio.getValue());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/
                /*}
            });*/
            //} catch (IOException e) {
            //    Log.e(TAG, "Error on PeripheralIO API", e);
            //}



        //Connecting to Firebase
        dbReference = FirebaseDatabase.getInstance().getReference();
        ValueEventListener directionListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot directionSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot whichWaySnapshot : directionSnapshot.getChildren()) {
                        HashMap hm = (HashMap) whichWaySnapshot.getValue();
                        Log.d(TAG, hm.get("direction").toString());

                        String direction = hm.get("direction").toString();
                        if(direction.equals("a")) {
                            mHandler.post(mUART1Runnable);
                        }


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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*if (mButtonGpio != null) {
            // Close the Gpio pin
            Log.i(TAG, "Closing Button GPIO pin");
            try {
                mButtonGpio.close();
            } catch (IOException e) {
                Log.e(TAG, "Error on PeripheralIO API", e);
            } finally {
                mButtonGpio = null;
            }
        }*/

        if (mDevice != null) {
            try {
                mDevice.close();
                mDevice = null;
            } catch (IOException e) {
                Log.w(TAG, "Unable to close UART device", e);
            }
        }

    }

    /*private Runnable mBlinkRunnable = new Runnable() {
        @Override
        public void run() {
            // Exit Runnable if the GPIO is already closed
            if (mLedGpio == null) {
                return;
            }
            try {
                // Toggle the GPIO state
                //mLedGpio.setValue(!mLedGpio.getValue());
                Log.d(TAG, "State set to " + mLedGpio.getValue());

                // Reschedule the same runnable in {#INTERVAL_BETWEEN_BLINKS_MS} milliseconds
                //mHandler.postDelayed(mBlinkRunnable, 1000);
            } catch (IOException e) {
                Log.e(TAG, "Error on PeripheralIO API", e);
            }
        }
    };*/

    private Runnable mUART1Runnable = new Runnable() {

        @Override
        public void run() {
            // Exit Runnable if the GPIO is already closed
            if (mDevice == null) {
                return;
            }
            try {
                byte[] buffer = {'a'};
                int count = mDevice.write(buffer, buffer.length);

                Log.d(TAG, "Wrote " + count + " bytes to peripheral 1");
                buffer = null;
                //mHandler.postDelayed(mUARTRunnable, 1000);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    /*private Runnable mUART2Runnable = new Runnable() {

        @Override
        public void run() {*/
            // Exit Runnable if the GPIO is already closed
            /*if (mDevice == null) {
                return;
            }
            try {
                byte[] buffer = {'b'};
                int count = mDevice.write(buffer, buffer.length);

                Log.d(TAG, "Wrote " + count + " bytes to peripheral 2");
                buffer = null;*/
                //mHandler.postDelayed(mUARTRunnable, 1000);
            /*} catch (IOException e) {
                e.printStackTrace();
            }
        }
    };*/

    public void writeUartData(UartDevice uart) throws IOException {
        byte[] buffer = {1};
        int count = uart.write(buffer, buffer.length);
        Log.d(TAG, "Wrote " + count + " bytes to peripheral");
    }
}
