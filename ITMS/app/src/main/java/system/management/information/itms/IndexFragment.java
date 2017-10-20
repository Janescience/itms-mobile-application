package system.management.information.itms;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
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
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Scanner;


public class IndexFragment extends Fragment{

    private FirebaseAuth.AuthStateListener mAuthListener,mAuthListenerUser;
    private FirebaseAuth mAuth, mAuthUser,firebaseAuth;
    private DatabaseReference mDatabase, mDatabaseHistory,mDatabaseUser;
    private EditText editTextTopic_1;
    private Button  buttonedit,buttonSave;
    private TextView txtPageToolBar;
    private ProgressDialog progressDialog;
    private String currentDateTimeString,nameEdit,nameUser;
    public  Spinner spinnerPage;
    public Object spinnerTopicPosition,spinnerPagePosition;
    public String txt_spinnerTopic,txt_spinnerPage;

    private String currentDate,currentMonth,currentYear,currentTime;
    private String date,time;
    public  Spinner spinner;
    private Integer indexSpinnerPage,indexSpinnerTopic;

    private static final String AUTH_KEY="key=AAAALVRxPuo:APA91bEVQA6g8xJLelUeh6Cr5G-cDh2ZwA7qtayoNeax7Q3A__I_t5ICpvp5cU9mX72UQKAQrWmNtmTgm74RILQZAeJ8TpGqcnWrh-qKml_jfSDkoicY95dgwFbL1Z6grn0kaP35IpbZ";


    private static final String TAG = "IndexFragment";

    Typeface Fonts;

    public IndexFragment() {
        // Required empty public constructor
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




        editTextTopic_1 = (EditText) rootView.findViewById(R.id.Topic_1);
        txtPageToolBar = (TextView) rootView.findViewById(R.id.txtPageToolBar) ;

        buttonedit=(Button) rootView.findViewById(R.id.edit);
        buttonSave=(Button) rootView.findViewById(R.id.save);


        mDatabaseHistory = FirebaseDatabase.getInstance().getReference().child("History");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        date = simpleDateFormat.format(new Date());

        currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());


        Fonts = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Kanit-Light.ttf");

        editTextTopic_1.setTypeface(Fonts);
        buttonSave.setTypeface(Fonts);
        buttonedit.setTypeface(Fonts);
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

                                    Object txt = spinner.getSelectedItemPosition();
                                    if (txt.equals(1)) {
                                        buttonedit.setEnabled(true);
                                        editTextTopic_1.setText(dataSnapshot.child("txtTopic_First").getValue().toString());
                                        ((ImageView) rootView.findViewById(R.id.imageSpin)).setImageResource(0);
                                        ((ImageView) rootView.findViewById(R.id.imageSpin)).setImageResource(R.drawable.header_first);
                                    } else if (txt.equals(2)) {
                                        buttonedit.setEnabled(true);
                                        editTextTopic_1.setText(dataSnapshot.child("txtDetails_First").getValue().toString());
                                        ((ImageView) rootView.findViewById(R.id.imageSpin)).setImageResource(0);
                                        ((ImageView) rootView.findViewById(R.id.imageSpin)).setImageResource(R.drawable.header_first);
                                    } else if (txt.equals(3)) {
                                        buttonedit.setEnabled(true);
                                        editTextTopic_1.setText(dataSnapshot.child("txtTopic_Second").getValue().toString());
                                        ((ImageView) rootView.findViewById(R.id.imageSpin)).setImageResource(0);
                                        ((ImageView) rootView.findViewById(R.id.imageSpin)).setImageResource(R.drawable.header_first);
                                    } else if (txt.equals(4)) {
                                        buttonedit.setEnabled(true);
                                        editTextTopic_1.setText(dataSnapshot.child("txtDetails_Second").getValue().toString());
                                        ((ImageView) rootView.findViewById(R.id.imageSpin)).setImageResource(0);
                                        ((ImageView) rootView.findViewById(R.id.imageSpin)).setImageResource(R.drawable.header_first);
                                    }else{
                                        buttonedit.setEnabled(false);
                                        buttonSave.setEnabled(false);
                                        editTextTopic_1.setText("ข้อมูลจากเว็บไซต์");
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
                            mDatabaseEdit.child("Website").child("Index").child("Header").child("txtDetails_First").setValue(EditHeader);
                            editTextTopic_1.setEnabled(false);
                            Toast.makeText(getActivity(), "แก้ไขข้อมูลสำเร็จ", Toast.LENGTH_SHORT).show();

                            sendWithOtherThread("topic");

                        }else if(spinnerTopicPosition.equals(3)){

                            FirebaseMessaging.getInstance().unsubscribeFromTopic("news");

                            String EditHeader = editTextTopic_1.getText().toString();
                            mDatabaseEdit.child("Website").child("Index").child("Header").child("txtTopic_Second").setValue(EditHeader);
                            editTextTopic_1.setEnabled(false);
                            Toast.makeText(getActivity(), "แก้ไขข้อมูลสำเร็จ", Toast.LENGTH_SHORT).show();

                            sendWithOtherThread("topic");

                        }else if(spinnerTopicPosition.equals(4)){

                            FirebaseMessaging.getInstance().unsubscribeFromTopic("news");

                            String EditHeader = editTextTopic_1.getText().toString();
                            mDatabaseEdit.child("Website").child("Index").child("Header").child("txtDetails_Second").setValue(EditHeader);
                            editTextTopic_1.setEnabled(false);
                            Toast.makeText(getActivity(), "แก้ไขข้อมูลสำเร็จ", Toast.LENGTH_SHORT).show();

                            sendWithOtherThread("topic");

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

