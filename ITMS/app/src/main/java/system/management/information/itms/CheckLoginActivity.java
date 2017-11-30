package system.management.information.itms;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * Created by Janescience on 11/28/2017.
 */

public class CheckLoginActivity extends AppCompatActivity {
    private static final String TAG = "Login";
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ProgressDialog progressDialog;


    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    protected void onCreate(Bundle savedIntanceState) {
        super.onCreate(savedIntanceState);

        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("กำลังเข้าสู่ระบบ...");
        progressDialog.show();

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!= null){
                    mDatabase= FirebaseDatabase.getInstance().getReference().child("User");
                    mDatabase.child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener(){
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String status = dataSnapshot.child("status").getValue().toString();
                            if(status.equals("admin")) {
                                startActivity(new Intent(getApplicationContext(), BottombarMainActivity.class));
                                FirebaseMessaging.getInstance().subscribeToTopic("news");
                                Log.d(TAG, "FollowToITMS");
                                progressDialog.dismiss();
                            }else if(status.equals("user")){
                                startActivity(new Intent(getApplicationContext(), BottombarUserMainActivity.class));
                                progressDialog.dismiss();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.e("", databaseError.getMessage());
                        }
                    });
                }
            }
        };


    }

}
