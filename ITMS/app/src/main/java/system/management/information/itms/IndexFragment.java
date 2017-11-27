package system.management.information.itms;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import static android.app.Activity.RESULT_OK;


public class IndexFragment extends Fragment{

    private int CAMERA_REQUEST_CODE = 0;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private StorageReference mStorage;
    private DatabaseReference mDatabase, mDatabaseHistory,mDatabaseUser,mDatabaseInfo;
    private EditText editTextTopic_1,editTextDetail;
    private Button  buttonedit,buttonSave,buttonImage,buttoneditDetail,buttonSaveDetail;
    private TextView txtPageToolBar;
    private ProgressDialog progressDialog;
    private String currentDateTimeString,nameEdit;
    public  Spinner spinnerPage;
    private ImageView imageInfo;
    public Object spinnerTopicPosition,spinnerPagePosition;
    public String txt_spinnerTopic,txt_spinnerPage;

    private ProgressBar progressbar;
    private String currentDate,currentMonth,currentYear,currentTime;
    private String date,time;
    public  Spinner spinner;
    private Integer indexSpinnerPage,indexSpinnerTopic;

    private static final String AUTH_KEY="key=AAAALVRxPuo:APA91bEVQA6g8xJLelUeh6Cr5G-cDh2ZwA7qtayoNeax7Q3A__I_t5ICpvp5cU9mX72UQKAQrWmNtmTgm74RILQZAeJ8TpGqcnWrh-qKml_jfSDkoicY95dgwFbL1Z6grn0kaP35IpbZ";


    private static final String TAG = "IndexFragment";

    Typeface Fonts;

    public IndexFragment() {

    }

    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem manualItem = menu.findItem(R.id.action_manual);
        manualItem.setVisible(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_index, container, false);
        spinner = (Spinner) rootView.findViewById(R.id.spinnerShowEdittext);
        spinnerPage = (Spinner) rootView.findViewById(R.id.spinnerShowSpinner);
        imageInfo= (ImageView) rootView.findViewById(R.id.imageSpin);


        progressbar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        progressbar.setVisibility(rootView.GONE);


        editTextTopic_1 = (EditText) rootView.findViewById(R.id.Topic_1);
        editTextDetail = (EditText) rootView.findViewById(R.id.DetailEdit);
        txtPageToolBar = (TextView) rootView.findViewById(R.id.txtPageToolBar) ;

        buttonedit=(Button) rootView.findViewById(R.id.edit);
        buttonSave=(Button) rootView.findViewById(R.id.save);
        buttoneditDetail=(Button) rootView.findViewById(R.id.editDetail);
        buttonSaveDetail=(Button) rootView.findViewById(R.id.saveDetail);
        buttonImage=(Button) rootView.findViewById(R.id.imageEdit);


        mDatabaseHistory = FirebaseDatabase.getInstance().getReference().child("History");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        date = simpleDateFormat.format(new Date());

        currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());


        Fonts = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Kanit-Light.ttf");

        editTextTopic_1.setTypeface(Fonts);
        editTextDetail.setTypeface(Fonts);
        buttonSave.setTypeface(Fonts);
        buttonedit.setTypeface(Fonts);
        buttonSaveDetail.setTypeface(Fonts);
        buttoneditDetail.setTypeface(Fonts);
        buttonImage.setTypeface(Fonts);
        txtPageToolBar.setTypeface(Fonts);


        final Bundle bundle = this.getArguments();

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_book);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ManualFragment fragment = new ManualFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_bottombar, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });


        if (getActivity().getIntent().getExtras() != null) {
            for (String key : getActivity().getIntent().getExtras().keySet()) {
                Object value = getActivity().getIntent().getExtras().get(key);
                Log.d(TAG, "Key: " + key + " Value: " + value);
            }
        }

        buttonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                if(intent.resolveActivity(getActivity().getPackageManager()) != null){
                    startActivityForResult(Intent.createChooser(intent,"กรุณาเลือกรูป"),CAMERA_REQUEST_CODE);
                }
            }
        });


        buttonedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextTopic_1.isEnabled() == false) {
                    editTextTopic_1.setEnabled(true);
                    buttonSave.setEnabled(true);
                    buttonedit.setText("ยกเลิก");
                    buttonedit.setTextColor(Color.RED);

                } else {
                    editTextTopic_1.setEnabled(false);
                    buttonSave.setEnabled(false);
                    buttonedit.setText("แก้ไข");
                    buttonedit.setTextColor(Color.BLACK);

                }
            }
        });

        buttoneditDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextDetail.isEnabled() == false) {
                    editTextDetail.setEnabled(true);
                    buttonSaveDetail.setEnabled(true);
                    buttoneditDetail.setText("ยกเลิก");
                    buttoneditDetail.setTextColor(Color.RED);

                } else {
                    editTextDetail.setEnabled(false);
                    buttonSaveDetail.setEnabled(false);
                    buttoneditDetail.setText("แก้ไข");
                    buttoneditDetail.setTextColor(Color.BLACK);

                }
            }
        });

        MySpinnerAdapter adapterPage = new MySpinnerAdapter(
                getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                Arrays.asList(getResources().getStringArray(R.array.page_array))
        );
        spinnerPage.setAdapter(adapterPage);

        if (bundle != null) {
            indexSpinnerPage  = bundle.getInt("spinnerPage");
            indexSpinnerTopic = bundle.getInt("spinnerTopic");
            spinnerPage.setSelection(indexSpinnerPage);
        }else{
            ((ImageView) rootView.findViewById(R.id.imageSpin)).setImageResource(R.drawable.image_icon);
        }

        MySpinnerAdapter adapterTopic = new MySpinnerAdapter(
                getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                Arrays.asList(getResources().getStringArray(R.array.null_array))
        );
        spinner.setAdapter(adapterTopic);

        spinnerPage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinnerPagePosition = spinnerPage.getSelectedItemPosition();
                txt_spinnerPage = spinnerPage.getSelectedItem().toString();
                if (spinnerPagePosition.equals(1)) {
                    MySpinnerAdapter adapterTopic = new MySpinnerAdapter(
                            getContext(),
                            android.R.layout.simple_spinner_dropdown_item,
                            Arrays.asList(getResources().getStringArray(R.array.topic_array))
                    );
                    spinner.setAdapter(adapterTopic);

                    if (bundle != null) {
                        spinner.setSelection(indexSpinnerTopic);
                        indexSpinnerTopic=0;

                    }
                } else {
                    MySpinnerAdapter adapterTopic = new MySpinnerAdapter(
                            getContext(),
                            android.R.layout.simple_spinner_dropdown_item,
                            Arrays.asList(getResources().getStringArray(R.array.null_array))
                    );
                    spinner.setAdapter(adapterTopic);
                    buttonedit.setEnabled(false);
                    buttoneditDetail.setEnabled(false);
                    buttonImage.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        progressDialog = new ProgressDialog(getActivity());
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {

                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Website").child("Index");
                    mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("User");
                    mDatabaseInfo = FirebaseDatabase.getInstance().getReference().child("Website").child("Index");
                    mDatabase.child("Header").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(final DataSnapshot dataSnapshot) {
                            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                                    Object txt = spinner.getSelectedItemPosition();
                                    if (txt.equals(1)) {
                                        progressbar.setVisibility(View.VISIBLE);
                                        editTextDetail.setVisibility(View.VISIBLE);
                                        buttonSaveDetail.setVisibility(View.VISIBLE);
                                        buttoneditDetail.setVisibility(View.VISIBLE);
                                        buttonedit.setEnabled(true);
                                        buttoneditDetail.setEnabled(true);
                                        buttonImage.setEnabled(true);
                                        editTextTopic_1.setText(dataSnapshot.child("txtTopic_First").getValue().toString());
                                        editTextDetail.setText(dataSnapshot.child("txtDetails_First").getValue().toString());
                                        Picasso.with(getActivity()).load(Uri.parse(dataSnapshot.child("imageSlide_First").getValue().toString())).into(imageInfo);
                                        if(imageInfo!=null)
                                        progressbar.setVisibility(View.GONE);
                                    } else if (txt.equals(2)) {
                                        progressbar.setVisibility(View.VISIBLE);
                                        editTextDetail.setVisibility(View.VISIBLE);
                                        buttonSaveDetail.setVisibility(View.VISIBLE);
                                        buttoneditDetail.setVisibility(View.VISIBLE);
                                        buttonedit.setEnabled(true);
                                        buttonImage.setEnabled(true);
                                        editTextTopic_1.setText(dataSnapshot.child("txtTopic_Second").getValue().toString());
                                        editTextDetail.setText(dataSnapshot.child("txtDetails_Second").getValue().toString());
                                        Picasso.with(getActivity()).load(Uri.parse(dataSnapshot.child("imageSlide_Second").getValue().toString())).into(imageInfo);
                                        if(imageInfo!=null)
                                        progressbar.setVisibility(View.GONE);
                                    }else if(txt.equals(3)) {
                                        progressbar.setVisibility(View.VISIBLE);
                                        editTextDetail.setText("ข้อมูลจากเว็บไซต์");
                                        editTextDetail.setVisibility(View.GONE);
                                        buttonSaveDetail.setVisibility(View.GONE);
                                        buttoneditDetail.setVisibility(View.GONE);
                                        mDatabaseInfo.child("Body").child("Info").child("Bachelor-Info").addValueEventListener(new ValueEventListener() {

                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                buttonedit.setEnabled(true);
                                                buttonImage.setEnabled(true);
                                                editTextTopic_1.setText(dataSnapshot.child("Topic-1").getValue().toString());
                                                Picasso.with(getActivity()).load(Uri.parse(dataSnapshot.child("Image-1").getValue().toString())).into(imageInfo);
                                                if(imageInfo!=null)
                                                    progressbar.setVisibility(View.GONE);
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                        }else if(txt.equals(4)){
                                        progressbar.setVisibility(View.VISIBLE);
                                        editTextDetail.setText("ข้อมูลจากเว็บไซต์");
                                        editTextDetail.setVisibility(View.GONE);
                                        buttonSaveDetail.setVisibility(View.GONE);
                                        buttoneditDetail.setVisibility(View.GONE);
                                        mDatabaseInfo.child("Body").child("Info").child("Bachelor-Info").addValueEventListener(new ValueEventListener() {

                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                buttonedit.setEnabled(true);
                                                buttonImage.setEnabled(true);
                                                editTextTopic_1.setText(dataSnapshot.child("Topic-2").getValue().toString());
                                                Picasso.with(getActivity()).load(Uri.parse(dataSnapshot.child("Image-2").getValue().toString())).into(imageInfo);
                                                if(imageInfo!=null)
                                                    progressbar.setVisibility(View.GONE);
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                    }else if(txt.equals(5)){
                                        progressbar.setVisibility(View.VISIBLE);
                                        editTextDetail.setText("ข้อมูลจากเว็บไซต์");
                                        editTextDetail.setVisibility(View.GONE);
                                        buttonSaveDetail.setVisibility(View.GONE);
                                        buttoneditDetail.setVisibility(View.GONE);
                                        mDatabaseInfo.child("Body").child("Info").child("Bachelor-Info").addValueEventListener(new ValueEventListener() {

                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                buttonedit.setEnabled(true);
                                                buttonImage.setEnabled(true);
                                                editTextTopic_1.setText(dataSnapshot.child("Topic-3").getValue().toString());
                                                Picasso.with(getActivity()).load(Uri.parse(dataSnapshot.child("Image-3").getValue().toString())).into(imageInfo);
                                                if(imageInfo!=null)
                                                    progressbar.setVisibility(View.GONE);
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                    }else if(txt.equals(6)){
                                        progressbar.setVisibility(View.VISIBLE);
                                        editTextDetail.setText("ข้อมูลจากเว็บไซต์");
                                        editTextDetail.setVisibility(View.GONE);
                                        buttonSaveDetail.setVisibility(View.GONE);
                                        buttoneditDetail.setVisibility(View.GONE);
                                        mDatabaseInfo.child("Body").child("Info").child("Graduate Studies").addValueEventListener(new ValueEventListener() {

                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                buttonedit.setEnabled(true);
                                                buttonImage.setEnabled(true);
                                                editTextTopic_1.setText(dataSnapshot.child("Topic-1").getValue().toString());
                                                Picasso.with(getActivity()).load(Uri.parse(dataSnapshot.child("Image-1").getValue().toString())).into(imageInfo);
                                                if(imageInfo!=null)
                                                    progressbar.setVisibility(View.GONE);
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                    }else if(txt.equals(7)){
                                        progressbar.setVisibility(View.VISIBLE);
                                        editTextDetail.setText("ข้อมูลจากเว็บไซต์");
                                        editTextDetail.setVisibility(View.GONE);
                                        buttonSaveDetail.setVisibility(View.GONE);
                                        buttoneditDetail.setVisibility(View.GONE);
                                        mDatabaseInfo.child("Body").child("Info").child("Graduate Studies").addValueEventListener(new ValueEventListener() {

                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                buttonedit.setEnabled(true);
                                                buttonImage.setEnabled(true);
                                                editTextTopic_1.setText(dataSnapshot.child("Topic-2").getValue().toString());
                                                Picasso.with(getActivity()).load(Uri.parse(dataSnapshot.child("Image-2").getValue().toString())).into(imageInfo);
                                                if(imageInfo!=null)
                                                    progressbar.setVisibility(View.GONE);
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });


                                    }else{
                                            editTextDetail.setVisibility(View.VISIBLE);
                                            buttonSaveDetail.setVisibility(View.VISIBLE);
                                            buttoneditDetail.setVisibility(View.VISIBLE);
                                            buttonedit.setEnabled(false);
                                            buttoneditDetail.setEnabled(false);
                                            buttonSaveDetail.setEnabled(false);
                                            buttonSave.setEnabled(false);
                                            buttonImage.setEnabled(false);
                                            editTextTopic_1.setText("ข้อมูลจากเว็บไซต์");
                                            editTextDetail.setText("ข้อมูลจากเว็บไซต์");
                                            ((ImageView) rootView.findViewById(R.id.imageSpin)).setImageResource(0);
                                            ((ImageView) rootView.findViewById(R.id.imageSpin)).setImageResource(R.drawable.image_icon);
                                        }
                                    }


                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.e("", databaseError.getMessage());
                        }
                    });


                    mDatabaseUser.child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            nameEdit = dataSnapshot.child("name").getValue().toString();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.e("", databaseError.getMessage());
                        }
                    });
                }

                final DatabaseReference mDatabaseEdit = FirebaseDatabase.getInstance().getReference();


                buttonSaveDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        spinnerTopicPosition = spinner.getSelectedItemPosition();
                        txt_spinnerTopic = spinner.getSelectedItem().toString();
                        if (spinnerTopicPosition.equals(1)) {

                            FirebaseMessaging.getInstance().unsubscribeFromTopic("news");

                            String EditHeaderDetail = editTextDetail.getText().toString();
                            mDatabaseEdit.child("Website").child("Index").child("Header").child("txtDetails_First").setValue(EditHeaderDetail);
                            editTextDetail.setEnabled(false);
                            Toast.makeText(getActivity(), "แก้ไขข้อมูลสำเร็จ", Toast.LENGTH_SHORT).show();

                            sendWithOtherThread("topic");


                        }else if(spinnerTopicPosition.equals(2)){

                            FirebaseMessaging.getInstance().unsubscribeFromTopic("news");

                            String EditHeader = editTextDetail.getText().toString();
                            mDatabaseEdit.child("Website").child("Index").child("Header").child("txtDetails_Second").setValue(EditHeader);
                            editTextDetail.setEnabled(false);
                            Toast.makeText(getActivity(), "แก้ไขข้อมูลสำเร็จ", Toast.LENGTH_SHORT).show();

                            sendWithOtherThread("topic");

                        }

                        final String detail_val = editTextDetail.getText().toString().trim();
                        startPosting(detail_val);
                    }
                });


                buttonSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        spinnerTopicPosition = spinner.getSelectedItemPosition();
                        txt_spinnerTopic = spinner.getSelectedItem().toString();
                        if (spinnerTopicPosition.equals(1)) {

                            FirebaseMessaging.getInstance().unsubscribeFromTopic("news");

                            String EditHeader = editTextTopic_1.getText().toString();
                            mDatabaseEdit.child("Website").child("Index").child("Header").child("txtTopic_First").setValue(EditHeader);
                            editTextTopic_1.setEnabled(false);
                            Toast.makeText(getActivity(), "แก้ไขข้อมูลสำเร็จ", Toast.LENGTH_SHORT).show();

                            sendWithOtherThread("topic");


                        }else if(spinnerTopicPosition.equals(2)){

                            FirebaseMessaging.getInstance().unsubscribeFromTopic("news");

                            String EditHeader = editTextTopic_1.getText().toString();
                            mDatabaseEdit.child("Website").child("Index").child("Header").child("txtTopic_Second").setValue(EditHeader);
                            editTextTopic_1.setEnabled(false);
                            Toast.makeText(getActivity(), "แก้ไขข้อมูลสำเร็จ", Toast.LENGTH_SHORT).show();

                            sendWithOtherThread("topic");

                        }else if(spinnerTopicPosition.equals(3)){

                            FirebaseMessaging.getInstance().unsubscribeFromTopic("news");

                            String EditHeader = editTextTopic_1.getText().toString();
                            mDatabaseEdit.child("Website").child("Index").child("Body").child("Info").child("Bachelor-Info").child("Topic-1").setValue(EditHeader);
                            editTextTopic_1.setEnabled(false);
                            Toast.makeText(getActivity(), "แก้ไขข้อมูลสำเร็จ", Toast.LENGTH_SHORT).show();

                            sendWithOtherThread("topic");
                        }else if(spinnerTopicPosition.equals(4)){

                            FirebaseMessaging.getInstance().unsubscribeFromTopic("news");

                            String EditHeader = editTextTopic_1.getText().toString();
                            mDatabaseEdit.child("Website").child("Index").child("Body").child("Info").child("Bachelor-Info").child("Topic-2").setValue(EditHeader);
                            editTextTopic_1.setEnabled(false);
                            Toast.makeText(getActivity(), "แก้ไขข้อมูลสำเร็จ", Toast.LENGTH_SHORT).show();

                            sendWithOtherThread("topic");
                        }else if(spinnerTopicPosition.equals(5)){

                            FirebaseMessaging.getInstance().unsubscribeFromTopic("news");

                            String EditHeader = editTextTopic_1.getText().toString();
                            mDatabaseEdit.child("Website").child("Index").child("Body").child("Info").child("Bachelor-Info").child("Topic-3").setValue(EditHeader);
                            editTextTopic_1.setEnabled(false);
                            Toast.makeText(getActivity(), "แก้ไขข้อมูลสำเร็จ", Toast.LENGTH_SHORT).show();

                            sendWithOtherThread("topic");
                        }else if(spinnerTopicPosition.equals(6)){

                            FirebaseMessaging.getInstance().unsubscribeFromTopic("news");

                            String EditHeader = editTextTopic_1.getText().toString();
                            mDatabaseEdit.child("Website").child("Index").child("Body").child("Info").child("Graduate Studies").child("Topic-1").setValue(EditHeader);
                            editTextTopic_1.setEnabled(false);
                            Toast.makeText(getActivity(), "แก้ไขข้อมูลสำเร็จ", Toast.LENGTH_SHORT).show();

                            sendWithOtherThread("topic");
                        }else if(spinnerTopicPosition.equals(7)){

                            FirebaseMessaging.getInstance().unsubscribeFromTopic("news");

                            String EditHeader = editTextTopic_1.getText().toString();
                            mDatabaseEdit.child("Website").child("Index").child("Body").child("Info").child("Graduate Studies").child("Topic-2").setValue(EditHeader);
                            editTextTopic_1.setEnabled(false);
                            Toast.makeText(getActivity(), "แก้ไขข้อมูลสำเร็จ", Toast.LENGTH_SHORT).show();

                            sendWithOtherThread("topic");
                        }


                        final String detail_val = editTextTopic_1.getText().toString().trim();
                        startPosting(detail_val);
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

        if(requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {

            if (mAuth.getCurrentUser() == null)
                return;
            progressDialog.setMessage("กำลังอัพโหลดรูปภาพ...");
            progressDialog.show();

            final Uri uri = data.getData();

            if (uri == null) {
                progressDialog.dismiss();
                return;
            }

            if (mAuth.getCurrentUser() == null)
                return;

            if (mStorage == null)
                mStorage = FirebaseStorage.getInstance().getReference();


            Object positionSpin = spinner.getSelectedItemPosition();

            DatabaseReference database = null;
            if (positionSpin.equals(1)) {
                database = mDatabase.child("Header").child("imageSlide_First");
            }else if(positionSpin.equals(2)){
                database = mDatabase.child("Header").child("imageSlide_Second");
            }else if(positionSpin.equals(3)){
                database = mDatabaseInfo.child("Body").child("Info").child("Bachelor-Info").child("Image-1");
            }else if(positionSpin.equals(4)){
                database = mDatabaseInfo.child("Body").child("Info").child("Bachelor-Info").child("Image-2");
            }else if(positionSpin.equals(5)){
                database = mDatabaseInfo.child("Body").child("Info").child("Bachelor-Info").child("Image-3");
            }else if(positionSpin.equals(6)){
                database = mDatabaseInfo.child("Body").child("Info").child("Graduate Studies").child("Image-1");
            }else if(positionSpin.equals(7)){
                database = mDatabaseInfo.child("Body").child("Info").child("Graduate Studies").child("Image-2");
            }



            final StorageReference filepath = mStorage.child("Photos").child(getRandomString());


            final DatabaseReference finalDatabase = database;
            database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String image = dataSnapshot.getValue().toString();

                    if (!image.equals("ยังไม่มีรูป") && !image.isEmpty()) {
                        Task<Void> task = FirebaseStorage.getInstance().getReferenceFromUrl(image).delete();
                        task.addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful())
                                    Toast.makeText(getActivity(), "ลบรูปภาพสำเร็จ", Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(getActivity(), "ไม่สามารถลบรูปภาพได้", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    finalDatabase.removeEventListener(this);

                    filepath.putFile(uri).addOnSuccessListener(getActivity(), new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Uri dowloadUri = taskSnapshot.getDownloadUrl();
                            Toast.makeText(getActivity(), "เปลี่ยนรูปภาพสำเร็จ", Toast.LENGTH_SHORT).show();
                            Picasso.with(getActivity()).load(uri).into(imageInfo);
                            finalDatabase.setValue(dowloadUri.toString());
                        }
                    }).addOnFailureListener(getActivity(), new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }
    }


    private void startPosting(String detail) {

        final String topic_val = txt_spinnerTopic;
        final String page_val = txt_spinnerPage;
        final String date_time_val = currentDateTimeString;

        if (!TextUtils.isEmpty(detail) && !TextUtils.isEmpty(nameEdit)) {
            progressDialog.setMessage("Posting to Blog...");
            progressDialog.show();
            DatabaseReference newPost = mDatabaseHistory.push();
            newPost.child("Name").setValue(nameEdit);
            newPost.child("Topic").setValue(topic_val);
            newPost.child("Page").setValue(page_val);
            newPost.child("Detail").setValue(detail);
            newPost.child("Date").setValue(date_time_val);
            progressDialog.dismiss();
            buttonSave.setEnabled(false);
        }

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
            jNotification.put("title",nameEdit);
            jNotification.put("body",txt_spinnerPage+" | "+txt_spinnerTopic+"       \n"+date +" "+currentMonth+currentYear+" "+time);
            jNotification.put("sound", "default");
            jNotification.put("badge", "1");
            jNotification.put("click_action", "OPEN_ACTIVITY_1");
            jNotification.put("icon", "ic_launcher");


            switch(type) {
                case "tokens":
                    JSONArray ja = new JSONArray();
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
                    Toast.makeText(getActivity(),resp, Toast.LENGTH_SHORT).show();
                }
            });

            FirebaseMessaging.getInstance().subscribeToTopic("news");

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

    }

    private String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next().replace(",", ",\n") : "";
    }




    public static class MySpinnerAdapter extends ArrayAdapter<String> {
        // Initialise custom font, for example:
        Typeface font = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/Kanit-Light.ttf");

        public MySpinnerAdapter(Context context, int resource, List<String> items) {
            super(context, resource, items);
        }

        // Affects default (closed) state of the spinner
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getView(position, convertView, parent);
            view.setTypeface(font);
            return view;
        }

        // Affects opened state of the spinner
        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getDropDownView(position, convertView, parent);
            view.setTypeface(font);
            return view;
        }
    }
}

