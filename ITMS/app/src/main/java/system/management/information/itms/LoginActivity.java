package system.management.information.itms;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by janescience on 8/8/2560.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mUsername;
    private EditText mPassword;

    private Button login;
    private TextView mRegister,mLogin;
    Typeface Fonts;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    protected void onCreate(Bundle savedIntanceState) {

        super.onCreate(savedIntanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        Fonts = Typeface.createFromAsset(this.getAssets(),"fonts/Kanit-Light.ttf");


         login = (Button) findViewById(R.id.button_login);
         mLogin= (TextView) findViewById(R.id.text_login);
         mUsername = (EditText) findViewById(R.id.username);
         mPassword = (EditText) findViewById(R.id.password);
         mRegister = (TextView) findViewById(R.id.register);

        login.setTypeface(Fonts);
        mUsername.setTypeface(Fonts);
        mPassword.setTypeface(Fonts);
        mRegister.setTypeface(Fonts);
        mLogin.setTypeface(Fonts);

        progressDialog = new ProgressDialog(this);

        login.setOnClickListener(this);


        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        if(firebaseAuth.getCurrentUser() != null){
            //close this activity
            finish();
            //opening profile activity
            startActivity(new Intent(getApplicationContext(), BottombarMainActivity.class));
        }

    }

    private void loginUser() {
        String username = mUsername.getText().toString().trim();
        String password = mPassword.getText().toString().trim();


        if (TextUtils.isEmpty(username)) {
            Toast.makeText(LoginActivity.this, "กรุณากรอกอีเมลล์", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this, "กรุณากรอกรหัสผ่าน", Toast.LENGTH_SHORT).show();
            return;
        }


        progressDialog.setMessage("กำลังเข้าสู่ระบบ...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(username,password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(LoginActivity.this,"เข้าสู่ระบบสำเร็จ",Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(getApplicationContext(), BottombarMainActivity.class));
                        }else{
                            Toast.makeText(LoginActivity.this,"ไม่สามารถเข้าสู่ระบบได้ กรุณาลองอีกครั้ง",Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });

    }



    @Override
    public void onClick(View view) {

        loginUser();


    }

}