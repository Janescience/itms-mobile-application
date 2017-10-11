package system.management.information.itms;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by janescience on 3/10/2560.
 */

public class MyPortfolioAdapter extends RecyclerView.Adapter<MyPortfolioAdapter.MyViewHolder> {

    public  static String[] mDataset;
    private String[] mTopicset;
    private int[] mImageset;


    public MyPortfolioAdapter(String[] myDataset,String[] myTopicset,int[] myImageset){
        mDataset = myDataset;
        mTopicset = myTopicset;
        mImageset = myImageset;

    }


    @Override
    public MyPortfolioAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.education_row, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.editText.setText(mDataset[position]);
        holder.topicText.setText(mTopicset[position]);
        holder.icImage.setImageResource(mImageset[position]);

    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }



    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public CardView cardView;
        public EditText editText;
        public Button   btSave,btEdit;
        public TextView topicText;
        public ImageView icImage;
        public  FirebaseAuth.AuthStateListener mAuthListener;
        public  FirebaseAuth mAuth;
        public  DatabaseReference mDatabase;
        Typeface Fonts;




        public MyViewHolder(final View itemView) {
            super(itemView);


            Fonts = Typeface.createFromAsset(itemView.getContext().getAssets(),"fonts/Kanit-Light.ttf");

            cardView = (CardView) itemView.findViewById(R.id.card_view);
            editText = (EditText) itemView.findViewById(R.id.tv_text);
            btSave = (Button) itemView.findViewById(R.id.btSaveEducation);
            btEdit = (Button) itemView.findViewById(R.id.btEditEducation);
            topicText = (TextView) itemView.findViewById(R.id.topic_text);
            icImage = (ImageView) itemView.findViewById(R.id.ic);



            editText.setTypeface(Fonts);
            topicText.setTypeface(Fonts);
            btSave.setTypeface(Fonts);
            btEdit.setTypeface(Fonts);

            if(editText.equals("") || editText.equals(null)){
                btSave.setEnabled(false);
                btEdit.setEnabled(false);
            }else if(editText.isEnabled()==false){
                btSave.setEnabled(false);
            }

            btEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(editText.isEnabled()==false) {
                        editText.setEnabled(true);
                        btSave.setEnabled(true);
                        btEdit.setText("ยกเลิก");
                        btEdit.setTextColor(Color.RED);
                    }else{
                        editText.setEnabled(false);
                        btSave.setEnabled(false);
                        btEdit.setText("แก้ไข");
                        btEdit.setTextColor(Color.BLACK);
                    }
                }
            });

          editText.addTextChangedListener(new TextWatcher() {
              @Override
              public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

              }

              @Override
              public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

              }

              @Override
              public void afterTextChanged(Editable editable) {
                  Log.d("DATA" + getAdapterPosition() + "0", editable.toString());
              }
          });



            mAuth = FirebaseAuth.getInstance();
            mAuthListener = new FirebaseAuth.AuthStateListener(){
                @Override
                public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {

                    mDatabase= FirebaseDatabase.getInstance().getReference().child("User");

                    btSave.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {



                                mDatabase.child(firebaseAuth.getCurrentUser().getUid()).child("education").child("his_education").setValue(editText.getText().toString());
                                Toast.makeText(itemView.getContext(), "แก้ไขข้อมูลประวัติการศึกษาสำเร็จ", Toast.LENGTH_SHORT).show();

                                mDatabase.child(firebaseAuth.getCurrentUser().getUid()).child("education").child("expertise").setValue(editText.getText().toString());
                                Toast.makeText(itemView.getContext(), "แก้ไขข้อมูลความถนัดเฉพาะด้านสำเร็จ", Toast.LENGTH_SHORT).show();

                            editText.setEnabled(false);
                            btSave.setEnabled(false);
                            btEdit.setText("แก้ไข");
                            btEdit.setTextColor(Color.BLACK);
                        }
                    });
                }
            };

            mAuth.addAuthStateListener(mAuthListener);



        }
    }
}
