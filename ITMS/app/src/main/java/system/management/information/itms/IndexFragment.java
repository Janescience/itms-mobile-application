package system.management.information.itms;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class IndexFragment extends Fragment {

    private FirebaseAuth.AuthStateListener mAuthListener,mAuthListenerUser;
    private FirebaseAuth mAuth, mAuthUser,firebaseAuth;
    private DatabaseReference mDatabase, mDatabaseHistory,mDatabaseUser;
    private EditText editTextTopic_1,name;
    private Button  buttonedit,buttonSave;
    private TextView Topic;
    private ProgressDialog progressDialog;
    private String date, currentDateTimeString,nameEdit;
    private Spinner spinnerPage;
    private String txt_spinnerTopic,txt_spinnerPage;


    Typeface Fonts;

    public IndexFragment() {
        // Required empty public constructor
    }

    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_index, container, false);
        final Spinner spinner = (Spinner) rootView.findViewById(R.id.spinnerShowEdittext);
        spinnerPage = (Spinner) rootView.findViewById(R.id.spinnerShowSpinner);


        editTextTopic_1 = (EditText) rootView.findViewById(R.id.Topic_1);


        buttonedit = (Button) rootView.findViewById(R.id.edit);
        buttonSave = (Button) rootView.findViewById(R.id.save);


        mDatabaseHistory = FirebaseDatabase.getInstance().getReference().child("History");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        date = simpleDateFormat.format(new Date());

        currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());


        Fonts = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Kanit-Light.ttf");

        editTextTopic_1.setTypeface(Fonts);
        buttonSave.setTypeface(Fonts);
        buttonedit.setTypeface(Fonts);


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

        MySpinnerAdapter adapterPage = new MySpinnerAdapter(
                getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                Arrays.asList(getResources().getStringArray(R.array.page_array))
        );
        spinnerPage.setAdapter(adapterPage);

        MySpinnerAdapter adapterTopic = new MySpinnerAdapter(
                getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                Arrays.asList(getResources().getStringArray(R.array.null_array))
        );
        spinner.setAdapter(adapterTopic);


//        ArrayAdapter<CharSequence> adapterPage = ArrayAdapter.createFromResource(getActivity(), R.array.page_array, android.R.layout.simple_spinner_item);
//        adapterPage.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerPage.setAdapter(adapterPage);

//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.null_array, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);

        spinnerPage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                txt_spinnerPage = spinnerPage.getSelectedItem().toString();
                if (txt_spinnerPage.equals("หน้าแรก")) {
                    MySpinnerAdapter adapterTopic = new MySpinnerAdapter(
                            getContext(),
                            android.R.layout.simple_spinner_dropdown_item,
                            Arrays.asList(getResources().getStringArray(R.array.topic_array))
                    );
                    spinner.setAdapter(adapterTopic);
                } else {
                    MySpinnerAdapter adapterTopic = new MySpinnerAdapter(
                            getContext(),
                            android.R.layout.simple_spinner_dropdown_item,
                            Arrays.asList(getResources().getStringArray(R.array.null_array))
                    );
                    spinner.setAdapter(adapterTopic);
                    buttonedit.setEnabled(false);
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
                    final FirebaseUser user = firebaseAuth.getCurrentUser();
                    mDatabase.child("Header").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(final DataSnapshot dataSnapshot) {
                            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                                    String txt = spinner.getSelectedItem().toString();
                                    if (txt.equals("ข้อความส่วนหัวรูปภาพสไลด์")) {
                                        buttonedit.setEnabled(true);
                                        editTextTopic_1.setText(dataSnapshot.child("txtTopic_First").getValue().toString());
                                        ((ImageView) rootView.findViewById(R.id.imageSpin)).setImageResource(0);
                                        ((ImageView) rootView.findViewById(R.id.imageSpin)).setImageResource(R.drawable.header_first);
                                    } else if (txt.equals("รายละเอียดส่วนหัวรูปภาพสไลด์")) {
                                        buttonedit.setEnabled(true);
                                        editTextTopic_1.setText(dataSnapshot.child("txtDetails_First").getValue().toString());
                                        ((ImageView) rootView.findViewById(R.id.imageSpin)).setImageResource(0);
                                        ((ImageView) rootView.findViewById(R.id.imageSpin)).setImageResource(R.drawable.header_first);
                                    } else {
                                        buttonedit.setEnabled(false);
                                        buttonSave.setEnabled(false);
                                        editTextTopic_1.setText("ข้อมูลจากเว็บไซต์");
                                        ((ImageView) rootView.findViewById(R.id.imageSpin)).setImageResource(0);
                                    }
                                }

                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    mDatabaseUser.child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            nameEdit = dataSnapshot.child("name").getValue().toString();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

                final DatabaseReference mDatabaseEdit = FirebaseDatabase.getInstance().getReference();



                buttonSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        txt_spinnerTopic = spinner.getSelectedItem().toString();
                        if (txt_spinnerTopic.equals("ข้อความส่วนหัวรูปภาพสไลด์")) {

                            String EditHeader = editTextTopic_1.getText().toString();
                            mDatabaseEdit.child("Website").child("Index").child("Header").child("txtTopic_First").setValue(EditHeader);
                            editTextTopic_1.setEnabled(false);
                            Toast.makeText(getActivity(), "แก้ไขข้อมูลสำเร็จ", Toast.LENGTH_SHORT).show();


                        }

                        startPosting();

                    }
                });


            }
        };


        return rootView;
    }

    private void startPosting() {

        final String detail_val = editTextTopic_1.getText().toString().trim();
        final String topic_val = txt_spinnerTopic;
        final String page_val = txt_spinnerPage;
        final String date_time_val = currentDateTimeString;

        if (!TextUtils.isEmpty(detail_val) && !TextUtils.isEmpty(nameEdit)) {
            progressDialog.setMessage("Posting to Blog...");
            progressDialog.show();
            DatabaseReference newPost = mDatabaseHistory.push();
            newPost.child("Name").setValue(nameEdit);
            newPost.child("Topic").setValue(topic_val);
            newPost.child("Page").setValue(page_val);
            newPost.child("Detail").setValue(detail_val);
            newPost.child("Date").setValue(date_time_val);
            progressDialog.dismiss();
            Toast.makeText(getActivity(), "Save data complete.", Toast.LENGTH_SHORT).show();
            buttonSave.setEnabled(false);
        }

    }


    private static class MySpinnerAdapter extends ArrayAdapter<String> {
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

