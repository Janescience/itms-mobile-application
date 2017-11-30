package system.management.information.itms;


import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import android.support.v7.widget.Toolbar;




public class RegisterUserFragment extends Fragment implements View.OnClickListener {

    Typeface Fonts;

    private EditText mName,mEmail,mPassword,mPhone;
    private Button mRegister;
    private TextView txtPageToolBar;
    private ProgressDialog progressDialog;

    FirebaseAuth firebaseAuth;


    public RegisterUserFragment() {
        // Required empty public constructor
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem backItem = menu.findItem(R.id.action_back);
        backItem.setVisible(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_register_user, container, false);
        Fonts = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Kanit-Light.ttf");

        txtPageToolBar = (TextView) rootView.findViewById(R.id.txtPageToolBar) ;

        firebaseAuth = FirebaseAuth.getInstance();
        mEmail = (EditText) rootView.findViewById(R.id.txtEmail);
        mPassword = (EditText) rootView.findViewById(R.id.password);
        mName = (EditText) rootView.findViewById(R.id.txtName);
        mPhone = (EditText) rootView.findViewById(R.id.txtPhone);
        mRegister = (Button) rootView.findViewById(R.id.button_register);
        progressDialog = new ProgressDialog(getActivity());

        //Set Fonts
        mEmail.setTypeface(Fonts);
        mPassword.setTypeface(Fonts);
        mName.setTypeface(Fonts);
        mPhone.setTypeface(Fonts);
        mRegister.setTypeface(Fonts);
        txtPageToolBar.setTypeface(Fonts);


        mRegister.setOnClickListener(this);

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });



        return rootView;
    }

    private void registerUser() {
        final String name = mName.getText().toString().trim();
        final String phone = mPhone.getText().toString().trim();
        final String email = mEmail.getText().toString().trim();
        final String password = mPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getActivity(), "กรุณากรอกชื่อและนามสกุล", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(getActivity(), "กรุณากรอกเบอร์โทรศัพท์", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getActivity(), "กรุณากรอกอีเมลล์", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getActivity(), "กรุณากรอกรหัสผ่าน", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("กำลังบันทึกข้อมูล...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            firebaseAuth.signInWithEmailAndPassword(email, password);

                            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("User");
                            DatabaseReference currentUser = mDatabase.child(firebaseAuth.getCurrentUser().getUid());
                            currentUser.child("academic_work").child("academic").setValue("-");
                            currentUser.child("academic_work").child("research").setValue("-");
                            currentUser.child("education").child("expertise").setValue("-");
                            currentUser.child("education").child("his_education").setValue("-");
                            currentUser.child("work").child("his_work").setValue("-");
                            currentUser.child("work").child("more_info").setValue("-");
                            currentUser.child("name").setValue(name);
                            currentUser.child("telephone").setValue(phone);
                            currentUser.child("image").setValue("ยังไม่มีรูป");
                            currentUser.child("uid").setValue(firebaseAuth.getCurrentUser().getUid());
                            currentUser.child("status").setValue("user");

                            Toast.makeText(getActivity(), "ลงทะเบียนสำเร็จ", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "ไม่สามารถลงทะเบียนได้ กรุณาลองอีกครั้ง", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    @Override
    public void onClick(View v) {
        registerUser();
    }
}
