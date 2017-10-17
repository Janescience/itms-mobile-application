package system.management.information.itms;

import android.icu.text.DateFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String AUTH_KEY="key=AAAALVRxPuo:APA91bEVQA6g8xJLelUeh6Cr5G-cDh2ZwA7qtayoNeax7Q3A__I_t5ICpvp5cU9mX72UQKAQrWmNtmTgm74RILQZAeJ8TpGqcnWrh-qKml_jfSDkoicY95dgwFbL1Z6grn0kaP35IpbZ";
    private static final String TAG = "MainActivity";
    private TextView mTextView;
    private String name,currentDateTimeString;
    private String currentDate,currentMonth,currentYear,currentTime;
    private String date,time;


    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.txt);



        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d(TAG, "Key: " + key + " Value: " + value);
            }
        }

        findViewById(R.id.subscribeButton).setOnClickListener(this);
        findViewById(R.id.unsubscribeButton).setOnClickListener(this);
        findViewById(R.id.logTokenButton).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!= null){

                    mDatabase= FirebaseDatabase.getInstance().getReference().child("User");
                    final FirebaseUser user = firebaseAuth.getCurrentUser();
                    mDatabase.child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener(){
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            name = dataSnapshot.child("name").getValue().toString();

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }

                    });


                }



            }
        };

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.subscribeButton:
                FirebaseMessaging.getInstance().subscribeToTopic("news");
                Log.d(TAG, "SubscribeToTopic");
                Toast.makeText(MainActivity.this, "SubscribeToTopic", Toast.LENGTH_SHORT).show();
                break;
            case R.id.unsubscribeButton:
                FirebaseMessaging.getInstance().unsubscribeFromTopic("news");
                Log.d(TAG, "UnsubscribeFromTopic");
                Toast.makeText(MainActivity.this, "UnsubscribeFromTopic", Toast.LENGTH_SHORT).show();
                break;
            case R.id.logTokenButton:
                String token = FirebaseInstanceId.getInstance().getToken();
                Log.d(TAG, "Token : " + token);
                Toast.makeText(MainActivity.this, "Token : " + token, Toast.LENGTH_SHORT).show();
                break;
        }

    }


    public void sendToken(View view) {
        sendWithOtherThread("condtion");
    }

    public void sendTokens(View view) {
        sendWithOtherThread("tokens");
    }

    public void sendTopic(View view) {
        sendWithOtherThread("topic");
    }

    private void sendWithOtherThread(final String type) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                currentDate = currentDateTimeString.split(" ")[1];
                currentMonth =  currentDateTimeString.split(" ")[0];
                currentYear = currentDateTimeString.split(",")[1];
                currentTime = currentDateTimeString.split(",")[2];

                date = currentDate.split(",")[0];
                time = currentTime.split(":")[0]+":"+currentTime.split(":")[1]+" "+currentTime.split(" ")[2];

                pushNotification(type);

            }
        }).start();
    }

    private void pushNotification(String type) {


        JSONObject jPayload = new JSONObject();
        JSONObject jNotification = new JSONObject();
        JSONObject jData = new JSONObject();
        try {
            jNotification.put("title",name);
            jNotification.put("body","Edited Website             "+date +" "+currentMonth+currentYear+" "+time);
            jNotification.put("sound", "default");
            jNotification.put("badge", "1");
            jNotification.put("click_action", "OPEN_ACTIVITY_1");
            jNotification.put("icon", "ic_launcher");

            jData.put("picture_url", date +" "+currentMonth+currentYear+" "+time);

            switch(type) {
                case "tokens":
                    JSONArray ja = new JSONArray();
                    ja.put("eMBADIzz2D4:APA91bG1Sjo3VAnt01amT2MFgV2zt1QiIoIm43Jgioinf3P-wcdLVtRW-Om7qT5RASSqjJOHCnNevAVQcgbqlib3rz4rV3dnjmbipSa8vGYht1u2HKG1Hn6N12CPRrfrUyWmTN-Bp_6N");
                    ja.put(FirebaseInstanceId.getInstance().getToken());
                    jPayload.put("registration_ids", ja);
                    break;
                case "topic":
                    jPayload.put("to", "/topics/news");
                    break;
                case "condition":
                    jPayload.put("condition", "'sport' in topics || 'news' in topics");
                    break;
                default:
                    jPayload.put("to", FirebaseInstanceId.getInstance().getToken());
            }

            jPayload.put("priority", "high");
            jPayload.put("notification", jNotification);
            jPayload.put("data", jData);

            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", AUTH_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Send FCM message content.
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(jPayload.toString().getBytes());

            // Read FCM response.
            InputStream inputStream = conn.getInputStream();
            final String resp = convertStreamToString(inputStream);

            Handler h = new Handler(Looper.getMainLooper());
            h.post(new Runnable() {
                @Override
                public void run() {
                    mTextView.setText(resp);
                }
            });
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    private String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next().replace(",", ",\n") : "";
    }
}
