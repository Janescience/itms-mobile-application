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

public class MyWorkHistoryAdapter extends RecyclerView.Adapter<MyWorkHistoryAdapter.MyViewHolder> {

public  static String[] mDataset;
private String[] mTopicset;



public MyWorkHistoryAdapter(String[] myDataset,String[] myTopicset){
        mDataset = myDataset;
        mTopicset = myTopicset;


        }


@Override
public MyWorkHistoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.work_row, parent, false);
    MyWorkHistoryAdapter.MyViewHolder vh = new MyWorkHistoryAdapter.MyViewHolder(v);
        return vh;
        }

@Override
public void onBindViewHolder(MyWorkHistoryAdapter.MyViewHolder holder, int position) {
        holder.editWorkText.setText(mDataset[0]);
        holder.editInfoText.setText(mDataset[1]);
        holder.topicWorkText.setText(mTopicset[0]);
        holder.topicInfoText.setText(mTopicset[1]);


        }

@Override
public int getItemCount() {
        return 1;
        }



public static class MyViewHolder extends RecyclerView.ViewHolder{
    public CardView cardView;
    public EditText editWorkText,editInfoText;
    public Button btWorkSave,btWorkEdit,btInfoSave,btInfoEdit;
    public TextView topicWorkText,topicInfoText;
    public  FirebaseAuth.AuthStateListener mAuthListener;
    public  FirebaseAuth mAuth;
    public DatabaseReference mDatabase;
    Typeface Fonts;




    public MyViewHolder(final View itemView) {
        super(itemView);


        Fonts = Typeface.createFromAsset(itemView.getContext().getAssets(),"fonts/Kanit-Light.ttf");

        cardView = (CardView) itemView.findViewById(R.id.card_view);
        editWorkText = (EditText) itemView.findViewById(R.id.work_text);
        btWorkSave = (Button) itemView.findViewById(R.id.btSaveWork);
        btWorkEdit = (Button) itemView.findViewById(R.id.btEditWork);
        topicWorkText = (TextView) itemView.findViewById(R.id.topic_work);

        editInfoText = (EditText) itemView.findViewById(R.id.info_text);
        btInfoSave = (Button) itemView.findViewById(R.id.btSaveInfo);
        btInfoEdit = (Button) itemView.findViewById(R.id.btEditInfo);
        topicInfoText = (TextView) itemView.findViewById(R.id.topic_info);

        editWorkText.setTypeface(Fonts);
        topicWorkText.setTypeface(Fonts);
        btWorkSave.setTypeface(Fonts);
        btWorkEdit.setTypeface(Fonts);

        editInfoText.setTypeface(Fonts);
        btInfoSave.setTypeface(Fonts);
        btInfoEdit.setTypeface(Fonts);
        topicInfoText.setTypeface(Fonts);

        if(editWorkText.equals("") || editWorkText.equals(null)){
            btWorkSave.setEnabled(false);
            btWorkEdit.setEnabled(false);
        }else if(editWorkText.isEnabled()==false){
            btWorkSave.setEnabled(false);
        }

        if(editInfoText.equals("") || editInfoText.equals(null)){
            btInfoSave.setEnabled(false);
            btInfoEdit.setEnabled(false);
        }else if(editInfoText.isEnabled()==false){
            btInfoSave.setEnabled(false);
        }

        btInfoEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editInfoText.isEnabled()==false) {
                    editInfoText.setEnabled(true);
                    btInfoSave.setEnabled(true);
                    btInfoEdit.setText("ยกเลิก");
                    btInfoEdit.setTextColor(Color.RED);
                }else{
                    editInfoText.setEnabled(false);
                    btInfoSave.setEnabled(false);
                    btInfoEdit.setText("แก้ไข");
                    btInfoEdit.setTextColor(Color.BLACK);
                }
            }
        });

        btWorkEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editWorkText.isEnabled()==false) {
                    editWorkText.setEnabled(true);
                    btWorkSave.setEnabled(true);
                    btWorkEdit.setText("ยกเลิก");
                    btWorkEdit.setTextColor(Color.RED);
                }else{
                    editWorkText.setEnabled(false);
                    btWorkSave.setEnabled(false);
                    btWorkEdit.setText("แก้ไข");
                    btWorkEdit.setTextColor(Color.BLACK);
                }
            }
        });


        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {

                mDatabase= FirebaseDatabase.getInstance().getReference().child("User");

                btWorkSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {



                        mDatabase.child(firebaseAuth.getCurrentUser().getUid()).child("work").child("his_work").setValue(editWorkText.getText().toString());
                        Toast.makeText(itemView.getContext(), "แก้ไขข้อมูลประวัติการศึกษาสำเร็จ", Toast.LENGTH_SHORT).show();

                        editWorkText.setEnabled(false);
                        btWorkSave.setEnabled(false);
                        btWorkEdit.setText("แก้ไข");
                        btWorkEdit.setTextColor(Color.BLACK);
                    }
                });

                btInfoSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDatabase.child(firebaseAuth.getCurrentUser().getUid()).child("work").child("more_info").setValue(editInfoText.getText().toString());
                        Toast.makeText(itemView.getContext(), "แก้ไขข้อมูลความถนัดเฉพาะด้านสำเร็จ", Toast.LENGTH_SHORT).show();

                        editInfoText.setEnabled(false);
                        btInfoSave.setEnabled(false);
                        btInfoEdit.setText("แก้ไข");
                        btInfoEdit.setTextColor(Color.BLACK);
                    }
                });
            }
        };

        mAuth.addAuthStateListener(mAuthListener);



    }
}
}
