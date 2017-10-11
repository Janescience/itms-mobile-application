package system.management.information.itms;


import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.icu.text.DateFormat;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {

    private RecyclerView mChatList;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase,mDatabaseChat;
    private String chatName;
    private EditText message;
    private Button send;

    private String date, currentDateTimeString;

    private ProgressDialog progressDialog;

    static Typeface Fonts;

    public ChatFragment() {
        // Required empty public constructor
    }

    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Chat, ChatFragment.ChatViewHolder> firebaseRecyclerAdapter = new  FirebaseRecyclerAdapter<Chat, ChatFragment.ChatViewHolder>(

                Chat.class,
                R.layout.chat_row,
                ChatFragment.ChatViewHolder.class,
                mDatabaseChat

        ) {
            protected void populateViewHolder(ChatFragment.ChatViewHolder viewHolder, Chat model, final int position) {
                viewHolder.setName(model.getName());
                viewHolder.setMessage(model.getMessage());
                viewHolder.setDate(model.getDate());
                mChatList.smoothScrollToPosition(position);

            }

        };
        mChatList.setAdapter(firebaseRecyclerAdapter);


        mAuth.addAuthStateListener(mAuthListener);

    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public ChatViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

        }

        public void setName(String name) {
            TextView chat_name = (TextView) mView.findViewById(R.id.chat_name);
            chat_name.setText(name);
            chat_name.setTypeface(Fonts);
        }


        public void setMessage(String message) {
            TextView chat_message = (TextView) mView.findViewById(R.id.chat_text);
            chat_message.setText(message);
            chat_message.setTypeface(Fonts);
        }

        public void setDate(String date){
            TextView chat_date = (TextView) mView.findViewById(R.id.chat_date);
            chat_date.setText(date);
            chat_date.setTypeface(Fonts);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);

        message = (EditText) rootView.findViewById(R.id.textInputChat);
        send = (Button) rootView.findViewById(R.id.btSendChat);

        Fonts = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Kanit-Light.ttf");


        currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

        mDatabaseChat = FirebaseDatabase.getInstance().getReference().child("Chat");

        mChatList = (RecyclerView) rootView.findViewById(R.id.chat_list);
        mChatList.setLayoutManager(new LinearLayoutManager(getActivity()));

        progressDialog = new ProgressDialog(getActivity());
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    mDatabase = FirebaseDatabase.getInstance().getReference().child("User");
                    mDatabase.child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            chatName = dataSnapshot.child("name").getValue().toString();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        };


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
                message.setText("");
            }
        });
            // Inflate the layout for this fragment
        return rootView;
    }

    private void sendMessage(){
        final String message_val = message.getText().toString().trim();
        final String date_current = currentDateTimeString;

        if (!TextUtils.isEmpty(message_val) && !TextUtils.isEmpty(chatName)) {

            DatabaseReference newMessage = mDatabaseChat.push();
            newMessage.child("name").setValue(chatName);
            newMessage.child("message").setValue(message_val);
            newMessage.child("date").setValue(date_current);

        }
    }

}
