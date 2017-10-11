package system.management.information.itms;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class HistoryFragment extends Fragment {

    private RecyclerView mBlogList;
    private DatabaseReference mDatabase;
    static Typeface Fonts;


    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.fragment_history, container, false);

        Fonts = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Kanit-Light.ttf");


        mBlogList = (RecyclerView) rootview.findViewById(R.id.blog_list);
        mBlogList.setLayoutManager(new LinearLayoutManager(getActivity()));

        mDatabase = FirebaseDatabase.getInstance().getReference().child("History");

        return rootview;
    }


    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<History, HistoryViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<History, HistoryViewHolder>(

                History.class,
                R.layout.history_row,
                HistoryViewHolder.class,
                mDatabase

        ) {
            protected void populateViewHolder(HistoryViewHolder viewHolder, History model, int position) {
                viewHolder.setName(model.getName());
                viewHolder.setPage(model.getPage());
                viewHolder.setTopic(model.getTopic());
                viewHolder.setDetail(model.getDetail());
                viewHolder.setDate(model.getDate());
                mBlogList.smoothScrollToPosition(position);
            }

        };
        mBlogList.setAdapter(firebaseRecyclerAdapter);

    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public HistoryViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

        }

        public void setName(String name) {
            TextView post_name = (TextView) mView.findViewById(R.id.post_name);
            post_name.setText(name);
            post_name.setTypeface(Fonts);
        }

        public void setPage(String page) {
            TextView post_page = (TextView) mView.findViewById(R.id.post_page);
            post_page.setText(page);
            post_page.setTypeface(Fonts);
        }

        public void setTopic(String topic) {
            TextView post_topic = (TextView) mView.findViewById(R.id.post_topic);
            post_topic.setText(topic);
            post_topic.setTypeface(Fonts);
        }

        public void setDetail(String detail) {
            TextView post_details = (TextView) mView.findViewById(R.id.post_details);
            post_details.setText(detail);
            post_details.setTypeface(Fonts);
        }

        public void setDate(String date){
            TextView post_date = (TextView) mView.findViewById(R.id.post_date);
            post_date.setText(date);
            post_date.setTypeface(Fonts);
        }
    }
}


