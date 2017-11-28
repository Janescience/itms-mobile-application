package system.management.information.itms.Ui.Fragment;

/**
 * Created by Janescience on 11/24/2017.
 */

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import system.management.information.itms.Core.Users.GetAll.GetUserContract;
import system.management.information.itms.Core.Users.GetAll.GetUserPresenter;
import system.management.information.itms.Models.User;
import system.management.information.itms.R;
import system.management.information.itms.Ui.Activity.ChatActivity;
import system.management.information.itms.Ui.Adapters.UserListingPagerAdapter;
import system.management.information.itms.Ui.Adapters.UserListingRecyclerAdapter;
import system.management.information.itms.Utils.ItemClickSupport;

public class UserFragment extends Fragment implements GetUserContract.View, ItemClickSupport.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    public static final String ARG_TYPE = "type";
    public static final String TYPE_CHATS = "type_chats";
    public static final String TYPE_ALL = "type_all";

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerViewAllUserListing;
    private TabLayout mTabLayoutUserListing;
    private ViewPager mViewPagerUserListing;

    Typeface Fonts;

    private TextView txtPageToolBar;

    private UserListingRecyclerAdapter mUserListingRecyclerAdapter;

    private GetUserPresenter mGetUsersPresenter;

    public static UserFragment newInstance(String type) {
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, type);
        UserFragment fragment = new UserFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_users, container, false);
        bindViews(fragmentView);
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });

        ItemClickSupport.addTo(mRecyclerViewAllUserListing)
                .setOnItemClickListener(this);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        // set the view pager adapter
        UserListingPagerAdapter userListingPagerAdapter = new UserListingPagerAdapter(getChildFragmentManager());
        mViewPagerUserListing.setAdapter(userListingPagerAdapter);

        // attach tab layout with view pager
        mTabLayoutUserListing.setupWithViewPager(mViewPagerUserListing);

        mGetUsersPresenter = new GetUserPresenter(this);
        mGetUsersPresenter.getAllUsers();
        return fragmentView;
    }

    private void bindViews(View view) {
        Fonts = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Kanit-Light.ttf");

        mTabLayoutUserListing = (TabLayout) view.findViewById(R.id.tab_layout_user_listing);
        mViewPagerUserListing = (ViewPager) view.findViewById(R.id.view_pager_user_listing);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mRecyclerViewAllUserListing = (RecyclerView) view.findViewById(R.id.recycler_view_all_user_listing);
        txtPageToolBar = (TextView) view.findViewById(R.id.txtPageToolBar) ;

        txtPageToolBar.setTypeface(Fonts);
    }



    @Override
    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
        ChatActivity.startActivity(getActivity(),
                mUserListingRecyclerAdapter.getUser(position).name,
                mUserListingRecyclerAdapter.getUser(position).uid,
                mUserListingRecyclerAdapter.getUser(position).firebaseToken);
    }

    @Override
    public void onGetAllUsersSuccess(List<User> users) {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        mUserListingRecyclerAdapter = new UserListingRecyclerAdapter(users);
        mRecyclerViewAllUserListing.setAdapter(mUserListingRecyclerAdapter);
        mUserListingRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetAllUsersFailure(String message) {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        Toast.makeText(getActivity(), "Error: " + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGetChatUsersSuccess(List<User> users) {

    }

    @Override
    public void onGetChatUsersFailure(String message) {

    }

    @Override
    public void onRefresh() {
        mGetUsersPresenter.getAllUsers();
    }
}
