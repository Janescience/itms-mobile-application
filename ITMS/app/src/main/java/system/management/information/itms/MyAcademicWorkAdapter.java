package system.management.information.itms;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Janescience on 10/17/2017.
 */

public class MyAcademicWorkAdapter extends RecyclerView.Adapter<MyAcademicWorkAdapter.MyViewHolder> {

    public  static String[] mDataset;
    private String[] mTopicset;

    public MyAcademicWorkAdapter(String[] myDataset,String[] myTopicset){
        mDataset = myDataset;
        mTopicset = myTopicset;
    }

    @Override
    public MyAcademicWorkAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.academic_work_row, parent, false);
        MyAcademicWorkAdapter.MyViewHolder vh = new MyAcademicWorkAdapter.MyViewHolder(v);
        return vh;
    }



    @Override
    public void onBindViewHolder(MyAcademicWorkAdapter.MyViewHolder holder, int position) {
        holder.editAcademicText.setText(mDataset[0]);
        holder.editResearchText.setText(mDataset[1]);
        holder.topicAcademicText.setText(mTopicset[0]);
        holder.topicResearchText.setText(mTopicset[1]);


    }

    @Override
    public int getItemCount() {
        return 1;
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public CardView cardView;
        public EditText editAcademicText,editResearchText;
        public Button btAcademicSave,btAcademicEdit,btResearchSave,btResearchEdit;
        public TextView topicAcademicText,topicResearchText;
        public  FirebaseAuth.AuthStateListener mAuthListener;
        public  FirebaseAuth mAuth;
        public DatabaseReference mDatabase;
        Typeface Fonts;

        public MyViewHolder(final View itemView) {
            super(itemView);

            Fonts = Typeface.createFromAsset(itemView.getContext().getAssets(),"fonts/Kanit-Light.ttf");

            cardView = (CardView) itemView.findViewById(R.id.card_view);
            editAcademicText = (EditText) itemView.findViewById(R.id.academic_text);
            btAcademicSave = (Button) itemView.findViewById(R.id.btSaveAcademic);
            btAcademicEdit = (Button) itemView.findViewById(R.id.btEditAcademic);
            topicAcademicText = (TextView) itemView.findViewById(R.id.topic_academic);

            editResearchText = (EditText) itemView.findViewById(R.id.research_text);
            btResearchSave = (Button) itemView.findViewById(R.id.btSaveResearch);
            btResearchEdit = (Button) itemView.findViewById(R.id.btEditResearch);
            topicResearchText = (TextView) itemView.findViewById(R.id.topic_research);


            editAcademicText.setTypeface(Fonts);
            topicAcademicText.setTypeface(Fonts);
            btAcademicSave.setTypeface(Fonts);
            btAcademicEdit.setTypeface(Fonts);

            editResearchText.setTypeface(Fonts);
            btResearchSave.setTypeface(Fonts);
            btResearchEdit.setTypeface(Fonts);
            topicResearchText.setTypeface(Fonts);

            if(editAcademicText.equals("") || editAcademicText.equals(null)){
                btAcademicSave.setEnabled(false);
                btAcademicEdit.setEnabled(false);
            }else if(editAcademicText.isEnabled()==false){
                btAcademicSave.setEnabled(false);
            }

            if(editResearchText.equals("") || editResearchText.equals(null)){
                btResearchSave.setEnabled(false);
                btResearchEdit.setEnabled(false);
            }else if(editResearchText.isEnabled()==false){
                btResearchSave.setEnabled(false);
            }

            btResearchEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(editResearchText.isEnabled()==false) {
                        editResearchText.setEnabled(true);
                        btResearchSave.setEnabled(true);
                        btResearchEdit.setText(R.string.cancel);
                        btResearchEdit.setTextColor(Color.RED);
                    }else{
                        editResearchText.setEnabled(false);
                        btResearchSave.setEnabled(false);
                        btResearchEdit.setText(R.string.edit);
                        btResearchEdit.setTextColor(Color.BLACK);
                    }
                }
            });

            btAcademicEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(editAcademicText.isEnabled()==false) {
                        editAcademicText.setEnabled(true);
                        btAcademicSave.setEnabled(true);
                        btAcademicEdit.setText(R.string.cancel);
                        btAcademicEdit.setTextColor(Color.RED);
                    }else{
                        editAcademicText.setEnabled(false);
                        btAcademicSave.setEnabled(false);
                        btAcademicEdit.setText(R.string.edit);
                        btAcademicEdit.setTextColor(Color.BLACK);
                    }
                }
            });

//          ====================== save data edited to firebase database by user id ===========================
            mAuth = FirebaseAuth.getInstance();
            mAuthListener = new FirebaseAuth.AuthStateListener(){
                @Override
                public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {

                    mDatabase= FirebaseDatabase.getInstance().getReference().child("User");

                    btAcademicSave.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            mDatabase.child(firebaseAuth.getCurrentUser().getUid()).child("academic_work").child("acdemic").setValue(editAcademicText.getText().toString());
                            Toast.makeText(itemView.getContext(),R.string.history_education_edited, Toast.LENGTH_SHORT).show();

                            editAcademicText.setEnabled(false);
                            btAcademicSave.setEnabled(false);
                            btAcademicEdit.setText(R.string.cancel);
                            btAcademicEdit.setTextColor(Color.BLACK);
                        }
                    });

                    btResearchSave.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDatabase.child(firebaseAuth.getCurrentUser().getUid()).child("academic_work").child("research").setValue(editResearchText.getText().toString());
                            Toast.makeText(itemView.getContext(), R.string.expertise_edited, Toast.LENGTH_SHORT).show();

                            editResearchText.setEnabled(false);
                            btResearchSave.setEnabled(false);
                            btResearchEdit.setText(R.string.edit);
                            btResearchEdit.setTextColor(Color.BLACK);
                        }
                    });
                }
            };

            mAuth.addAuthStateListener(mAuthListener);

            if (mAuthListener != null) {
                mAuth.removeAuthStateListener(mAuthListener);
            }
        }
    }
}
