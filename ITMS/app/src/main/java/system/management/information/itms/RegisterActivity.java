package system.management.information.itms;

import android.app.ProgressDialog;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by janescience on 9/8/2560.
 */

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mName,mEmail,mPassword,mPhone;
    private Button mRegister;
    private TextView txtRegister;
    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    Typeface Fonts;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();

        mEmail = (EditText) findViewById(R.id.txtEmail);
        mPassword = (EditText) findViewById(R.id.password);
        mName = (EditText) findViewById(R.id.txtName);
        mPhone = (EditText) findViewById(R.id.txtPhone);
        txtRegister = (TextView) findViewById(R.id.register);

        mRegister = (Button) findViewById(R.id.button_register);

        progressDialog = new ProgressDialog(this);

        Fonts = Typeface.createFromAsset(getAssets(),"fonts/Kanit-Light.ttf");

        //Set Fonts
        mEmail.setTypeface(Fonts);
        mPassword.setTypeface(Fonts);
        mName.setTypeface(Fonts);
        mPhone.setTypeface(Fonts);
        mRegister.setTypeface(Fonts);
        txtRegister.setTypeface(Fonts);

        mRegister.setOnClickListener(this);

    }

    private void registerUser(){
        final String name = mName.getText().toString().trim();
        final String phone = mPhone.getText().toString().trim();
        final String email = mEmail.getText().toString().trim();
        final String password = mPassword.getText().toString().trim();

        if(TextUtils.isEmpty(name)){
            Toast.makeText(RegisterActivity.this,"กรุณากรอกชื่อและนามสกุล",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(phone)){
            Toast.makeText(RegisterActivity.this,"กรุณากรอกเบอร์โทรศัพท์",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(email)){
            Toast.makeText(RegisterActivity.this,"กรุณากรอกอีเมลล์",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(RegisterActivity.this,"กรุณากรอกรหัสผ่าน",Toast.LENGTH_SHORT).show();
            return;
        }

            progressDialog.setMessage("กำลังบันทึกข้อมูล...");
            progressDialog.show();

            firebaseAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if(task.isSuccessful()){
                                firebaseAuth.signInWithEmailAndPassword(email,password);

                                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("User");
                                DatabaseReference currentUser = mDatabase.child(firebaseAuth.getCurrentUser().getUid());
                                currentUser.child("name").setValue(name);
                                currentUser.child("telephone").setValue(phone);
                                currentUser.child("image").setValue("ยังไม่มีรูป");
                                Toast.makeText(RegisterActivity.this,"ลงทะเบียนสำเร็จ",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(RegisterActivity.this,"ไม่สามารถลงทะเบียนได้ กรุณาลองอีกครั้ง",Toast.LENGTH_SHORT).show();
                            }

                        }
                    });


     

    }

    @Override
    public void onClick(View view) {

        registerUser();


    }
}
