package system.management.information.itms;


import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class WorkHistoryFragment extends Fragment {

    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private DatabaseReference mDatabase;
    Typeface Fonts;


    public WorkHistoryFragment() {
        // Required empty public constructor
    }

    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_work_history, container, false);

        Fonts = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Kanit-Light.ttf");

        TextView topicWork = (TextView) rootView.findViewById(R.id.textTopicWork);
        TextView topicInformation= (TextView) rootView.findViewById(R.id.textTopicMoreInformation);
        EditText editWork = (EditText) rootView.findViewById(R.id.editTextWork);
        EditText editInformation = (EditText) rootView.findViewById(R.id.editTextMoreInformation);
        Button btSaveWork = (Button)   rootView.findViewById(R.id.btSaveWork);
        Button btEditWork = (Button)   rootView.findViewById(R.id.btEditWork);

        topicWork.setTypeface(Fonts);
        topicInformation.setTypeface(Fonts);
        editWork.setTypeface(Fonts);
        editInformation.setTypeface(Fonts);
        btSaveWork.setTypeface(Fonts);
        btEditWork.setTypeface(Fonts);

        progressDialog = new ProgressDialog(getActivity());
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!= null){

                    mDatabase= FirebaseDatabase.getInstance().getReference().child("User");

                    mDatabase.child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener(){
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        };

        // Inflate the layout for this fragment
        return rootView;
    }

}
