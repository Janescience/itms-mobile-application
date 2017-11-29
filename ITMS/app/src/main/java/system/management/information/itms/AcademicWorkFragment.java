package system.management.information.itms;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class AcademicWorkFragment extends Fragment {

    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    public AcademicWorkFragment() {

    }

//  ====== add all user to listener ======
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
//  === remove all user in listener to return resource to application ===
    public void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_academic_work, container, false);

        final RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.rv_recycler_view);
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

//      ========== get history and academic data from firebase database by user id ===========
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!= null){

//                  ===== reference object --> User =====
                    mDatabase= FirebaseDatabase.getInstance().getReference().child("User");
//                  ===== get data by current user id in object [User] ====
                    mDatabase.child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener(){
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

//                          ===== send data array to MyAcademicWorkAdapter.java =====
                            MyAcademicWorkAdapter adapter = new MyAcademicWorkAdapter(new String[]{
                                    dataSnapshot.child("academic_work").child("academic").getValue().toString(),
                                    dataSnapshot.child("academic_work").child("research").getValue().toString()

                            }, new String[]{
                                    getActivity().getString(R.string.academic_work),
                                    getActivity().getString(R.string.research)
                            });
//                          ==== set data to recyclerview =====
                            rv.setAdapter(adapter);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.e("", databaseError.getMessage());
                        }
                    });
                }
            }
        };
        return rootView;
    }
}
