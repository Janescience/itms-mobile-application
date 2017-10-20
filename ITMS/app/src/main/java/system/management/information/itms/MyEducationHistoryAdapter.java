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

public class MyEducationHistoryAdapter extends RecyclerView.Adapter<MyEducationHistoryAdapter.MyViewHolder> {

    public  static String[] mDataset;
    private String[] mTopicset;



    public MyEducationHistoryAdapter(String[] myDataset,String[] myTopicset){
        mDataset = myDataset;
        mTopicset = myTopicset;


    }


    @Override
    public MyEducationHistoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.education_row, parent, false);
        MyEducationHistoryAdapter.MyViewHolder vh = new MyEducationHistoryAdapter.MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyEducationHistoryAdapter.MyViewHolder holder, int position) {
        holder.editEducationText.setText(mDataset[0]);
        holder.editExpertiseText.setText(mDataset[1]);
        holder.topicEducationText.setText(mTopicset[0]);
        holder.topicExpertiseText.setText(mTopicset[1]);
    }

    @Override
    public int getItemCount() {
        return 1;
    }



    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public CardView cardView;
        public EditText editEducationText,editExpertiseText;
        public Button btEducationSave,btEducationEdit,btExpertiseSave,btExpertiseEdit;
        public TextView topicEducationText,topicExpertiseText;
        public  FirebaseAuth.AuthStateListener mAuthListener;
        public  FirebaseAuth mAuth;
        public DatabaseReference mDatabase;
        Typeface Fonts;




        public MyViewHolder(final View itemView) {
            super(itemView);


            Fonts = Typeface.createFromAsset(itemView.getContext().getAssets(),"fonts/Kanit-Light.ttf");

            cardView = (CardView) itemView.findViewById(R.id.card_view);
            editEducationText = (EditText) itemView.findViewById(R.id.education_text);
            btEducationSave = (Button) itemView.findViewById(R.id.btSaveEducation);
            btEducationEdit = (Button) itemView.findViewById(R.id.btEditEducation);
            topicEducationText = (TextView) itemView.findViewById(R.id.topic_education);

            editExpertiseText = (EditText) itemView.findViewById(R.id.expertise_text);
            btExpertiseSave = (Button) itemView.findViewById(R.id.btSaveExpertise);
            btExpertiseEdit = (Button) itemView.findViewById(R.id.btEditExpertise);
            topicExpertiseText = (TextView) itemView.findViewById(R.id.topic_expertise);

            editEducationText.setTypeface(Fonts);
            topicEducationText.setTypeface(Fonts);
            btEducationSave.setTypeface(Fonts);
            btEducationEdit.setTypeface(Fonts);

            editExpertiseText.setTypeface(Fonts);
            btExpertiseSave.setTypeface(Fonts);
            btExpertiseEdit.setTypeface(Fonts);
            topicExpertiseText.setTypeface(Fonts);

            if(editEducationText.equals("") || editEducationText.equals(null)){
                btEducationSave.setEnabled(false);
                btEducationEdit.setEnabled(false);
            }else if(editEducationText.isEnabled()==false){
                btEducationSave.setEnabled(false);
            }

            if(editExpertiseText.equals("") || editExpertiseText.equals(null)){
                btExpertiseSave.setEnabled(false);
                btExpertiseEdit.setEnabled(false);
            }else if(editExpertiseText.isEnabled()==false){
                btExpertiseSave.setEnabled(false);
            }

            btExpertiseEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(editExpertiseText.isEnabled()==false) {
                        editExpertiseText.setEnabled(true);
                        btExpertiseSave.setEnabled(true);
                        btExpertiseEdit.setText("ยกเลิก");
                        btExpertiseEdit.setTextColor(Color.RED);
                    }else{
                        editExpertiseText.setEnabled(false);
                        btExpertiseSave.setEnabled(false);
                        btExpertiseEdit.setText("แก้ไข");
                        btExpertiseEdit.setTextColor(Color.BLACK);
                    }
                }
            });

            btEducationEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(editEducationText.isEnabled()==false) {
                        editEducationText.setEnabled(true);
                        btEducationSave.setEnabled(true);
                        btEducationEdit.setText("ยกเลิก");
                        btEducationEdit.setTextColor(Color.RED);
                    }else{
                        editEducationText.setEnabled(false);
                        btEducationSave.setEnabled(false);
                        btEducationEdit.setText("แก้ไข");
                        btEducationEdit.setTextColor(Color.BLACK);
                    }
                }
            });


            mAuth = FirebaseAuth.getInstance();
            mAuthListener = new FirebaseAuth.AuthStateListener(){
                @Override
                public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {

                    mDatabase= FirebaseDatabase.getInstance().getReference().child("User");

                    btEducationSave.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {



                            mDatabase.child(firebaseAuth.getCurrentUser().getUid()).child("education").child("his_education").setValue(editEducationText.getText().toString());
                            Toast.makeText(itemView.getContext(), "แก้ไขข้อมูลประวัติการศึกษาสำเร็จ", Toast.LENGTH_SHORT).show();

                            editEducationText.setEnabled(false);
                            btEducationSave.setEnabled(false);
                            btEducationEdit.setText("แก้ไข");
                            btEducationEdit.setTextColor(Color.BLACK);
                        }
                    });

                    btExpertiseSave.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDatabase.child(firebaseAuth.getCurrentUser().getUid()).child("education").child("expertise").setValue(editExpertiseText.getText().toString());
                            Toast.makeText(itemView.getContext(), "แก้ไขข้อมูลความถนัดเฉพาะด้านสำเร็จ", Toast.LENGTH_SHORT).show();

                            editExpertiseText.setEnabled(false);
                            btExpertiseSave.setEnabled(false);
                            btExpertiseEdit.setText("แก้ไข");
                            btExpertiseEdit.setTextColor(Color.BLACK);
                        }
                    });
                }
            };

            mAuth.addAuthStateListener(mAuthListener);



        }
    }
}