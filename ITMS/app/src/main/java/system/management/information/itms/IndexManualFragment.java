package system.management.information.itms;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Janescience on 10/16/2017.
 */

public class IndexManualFragment extends Fragment {

    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private DatabaseReference mDatabase;

    public String education;
    Typeface Fonts;


    public IndexManualFragment() {
        // Required empty public constructor
    }

    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_index_manual, container, false);

        final RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.rv_recycler_view);
        rv.setHasFixedSize(true);



        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        Fonts = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Kanit-Light.ttf");


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

                            MyIndexManualAdapter adapter = new MyIndexManualAdapter(new int[]{
                                    R.drawable.index_manual
                            });
                            rv.setAdapter(adapter);
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
