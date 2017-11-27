package system.management.information.itms;


import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class HistoryFragment extends Fragment {

    private RecyclerView mBlogList;
    private DatabaseReference mDatabase;
    static Typeface Fonts;
    private TextView txtPageToolBar;
    private SwipeRefreshLayout swipeRefreshLayout;



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchItem.setVisible(true);
        search(searchView);
    }

    private void search(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return true;
            }
        });
    }


    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootview = inflater.inflate(R.layout.fragment_history, container, false);

        Fonts = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Kanit-Light.ttf");

        txtPageToolBar = (TextView) rootview.findViewById(R.id.txtPageToolBar) ;

        txtPageToolBar.setTypeface(Fonts);
        mBlogList = (RecyclerView) rootview.findViewById(R.id.blog_list);
        mBlogList.setLayoutManager(new LinearLayoutManager(getActivity()));

        mDatabase = FirebaseDatabase.getInstance().getReference().child("History");

        Toolbar toolbar = (Toolbar) rootview.findViewById(R.id.toolbar);

        swipeRefreshLayout = (SwipeRefreshLayout) rootview.findViewById(R.id.refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onStart();
            }
        });
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
            protected void populateViewHolder(HistoryViewHolder viewHolder, History model,final int position) {
                viewHolder.setName(model.getName());
                viewHolder.setPage(model.getPage());
                viewHolder.setTopic(model.getTopic());
                viewHolder.setDetail(model.getDetail());
                viewHolder.setDate(model.getDate());
                mBlogList.smoothScrollToPosition(position);
            }

        };
        mBlogList.setAdapter(firebaseRecyclerAdapter);
        onRefreshCompleted();
    }

    private void onRefreshCompleted() {
        swipeRefreshLayout.setRefreshing(false);
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder  {

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
            post_date.setText(String.valueOf(date));
            post_date.setTypeface(Fonts);
        }
    }
}


