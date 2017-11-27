package system.management.information.itms;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.Trace;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.math.BigInteger;
import java.security.SecureRandom;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends Fragment implements View.OnClickListener {

    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private int CAMERA_REQUEST_CODE = 0;
    private ProgressDialog progressDialog;
    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private CircleImageView imageProfile;
    private EditText  textName,textPhone;
    private TextView  textEmail,Person,txtPageToolBar;
    private Button btEditImage,btProfile,btEdit,btSave;
    private ProgressBar spinner;

    Typeface Fonts;

    public ProfileFragment() {

    }

    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        textName = (EditText) rootView.findViewById(R.id.txtNameShow);
        textPhone = (EditText) rootView.findViewById(R.id.txtPhoneShow);
        textEmail = (TextView) rootView.findViewById(R.id.txtEmailShow);
        txtPageToolBar = (TextView) rootView.findViewById(R.id.txtPageToolBar) ;
        btEditImage = (Button) rootView.findViewById(R.id.btEditImage);
        btProfile = (Button) rootView.findViewById(R.id.btProfile);
        btEdit = (Button) rootView.findViewById(R.id.btEdit);
        btSave = (Button) rootView.findViewById(R.id.btSave);
        spinner = (ProgressBar) rootView.findViewById(R.id.progressBar);
        spinner.setVisibility(rootView.GONE);

        imageProfile =( CircleImageView) rootView.findViewById(R.id.imageUser);
        Fonts = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Kanit-Light.ttf");

        textName.setTypeface(Fonts);
        textPhone.setTypeface(Fonts);
        textEmail.setTypeface(Fonts);
        btProfile.setTypeface(Fonts);
        btEditImage.setTypeface(Fonts);
        btEdit.setTypeface(Fonts);
        btSave.setTypeface(Fonts);
        txtPageToolBar.setTypeface(Fonts);

        btProfile.setOnClickListener(this);

        btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(textName.isEnabled()== false && textPhone.isEnabled()== false ) {
                    textName.setEnabled(true);
                    textPhone.setEnabled(true);
                    btSave.setEnabled(true);
                    btEdit.setText("ยกเลิก");
                    btEdit.setTextColor(Color.RED);
                }else{
                    textName.setEnabled(false);
                    textPhone.setEnabled(false);
                    btSave.setEnabled(false);
                    btEdit.setText("แก้ไข");
                    btEdit.setTextColor(Color.BLACK);
                }
            }
        });

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(textName.isEnabled()== false && textPhone.isEnabled()== false && textEmail.isEnabled() == false) {
                    Toast.makeText(getActivity(),"ไม่สามารถบันทึกได้",Toast.LENGTH_SHORT).show();
                }else{

                }
            }
        });

        btEditImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                if(intent.resolveActivity(getActivity().getPackageManager()) != null){
                    startActivityForResult(Intent.createChooser(intent,"กรุณาเลือกรูปประจำตัว"),CAMERA_REQUEST_CODE);
                }
            }
        });

        spinner.setVisibility(rootView.VISIBLE);
        progressDialog = new ProgressDialog(getActivity());
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {
               if(firebaseAuth.getCurrentUser()!= null){
                   mStorage = FirebaseStorage.getInstance().getReference();
                   mDatabase= FirebaseDatabase.getInstance().getReference().child("User");
                   final FirebaseUser user = firebaseAuth.getCurrentUser();
                   mDatabase.child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener(){
                       @Override
                       public void onDataChange(DataSnapshot dataSnapshot) {
                            textName.setText(dataSnapshot.child("name").getValue().toString());
                            textPhone.setText(dataSnapshot.child("telephone").getValue().toString());
                            String imageUrl = dataSnapshot.child("image").getValue().toString();
                            textEmail.setText(user.getEmail());
                            if(!imageUrl.equals("ยังไม่มีรูป") || TextUtils.isEmpty(imageUrl)){
                                Picasso.with(getActivity()).load(Uri.parse(dataSnapshot.child("image").getValue().toString())).into(imageProfile);
                            }
                            spinner.setVisibility(rootView.GONE);
                       }

                       @Override
                       public void onCancelled(DatabaseError databaseError) {
                           Log.e("", databaseError.getMessage());
                       }

                   });


               }

                btSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String EditName = textName.getText().toString();
                        String EditPhone = textPhone.getText().toString();
                        mDatabase.child(firebaseAuth.getCurrentUser().getUid()).child("name").setValue(EditName);
                        mDatabase.child(firebaseAuth.getCurrentUser().getUid()).child("telephone").setValue(EditPhone);
                        Toast.makeText(getActivity(),"แก้ไขข้อมูลสำเร็จ",Toast.LENGTH_SHORT).show();

                        textName.setEnabled(false);
                        textPhone.setEnabled(false);
                    }
                });
            }
        };
        return rootView;
    }

    public String getRandomString(){
        SecureRandom random = new SecureRandom();
        return new BigInteger(130,random).toString(32);
    }

    public void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK){

            if (mAuth.getCurrentUser() == null)
                return;
            progressDialog.setMessage("กำลังอัพโหลดรูปภาพ...");
            progressDialog.show();

            final Uri uri = data.getData();

            if (uri == null){
                progressDialog.dismiss();
                return;
            }

            if (mAuth.getCurrentUser() == null)
                return;

            if(mStorage == null)
                mStorage = FirebaseStorage.getInstance().getReference();
            if (mDatabase == null)
                mDatabase = FirebaseDatabase.getInstance().getReference().child("User");

            final  StorageReference filepath = mStorage.child("Photos").child(getRandomString());
            final DatabaseReference currentUser = mDatabase.child(mAuth.getCurrentUser().getUid());

            currentUser.child("image").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String image = dataSnapshot.getValue().toString();

                    if(!image.equals("ยังไม่มีรูป") && !image.isEmpty()){
                        Task<Void> task = FirebaseStorage.getInstance().getReferenceFromUrl(image).delete();
                        task.addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                    Toast.makeText(getActivity(),"ลบรูปภาพสำเร็จ",Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(getActivity(),"ไม่สามารถลบรูปภาพได้",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    currentUser.child("image").removeEventListener(this);

                    filepath.putFile(uri).addOnSuccessListener(getActivity(), new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Uri dowloadUri = taskSnapshot.getDownloadUrl();
                            Toast.makeText(getActivity(),"เปลี่ยนรูปภาพสำเร็จ",Toast.LENGTH_SHORT).show();
                            Picasso.with(getActivity()).load(uri).into(imageProfile);
                            DatabaseReference currentUser = mDatabase.child(mAuth.getCurrentUser().getUid());
                            currentUser.child("image").setValue(dowloadUri.toString());
                        }
                    }).addOnFailureListener(getActivity(), new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }
    }

    @Override
    public void onClick(View view) {
        Fragment fragment = null;
        switch (view.getId()) {
            case R.id.btProfile:
                fragment = new PortfolioFragment();
                replaceFragment(fragment);
                break;
        }
    }

    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_bottombar, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
