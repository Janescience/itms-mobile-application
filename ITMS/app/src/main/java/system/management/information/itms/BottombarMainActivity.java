package system.management.information.itms;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarBadge;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnMenuTabClickListener;

import system.management.information.itms.Core.Logout.LogoutPresenter;
import system.management.information.itms.Ui.Activity.SplashActivity;
import system.management.information.itms.Ui.Activity.UserListingActivity;
import system.management.information.itms.Ui.Fragment.UserFragment;

/**
 * Created by janescience on 28/8/2560.
 */

public class BottombarMainActivity extends ActionBarActivity{

    BottomBar mBottombar;
    FirebaseAuth firebaseAuth;
    private ChatFragment chatFragment;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, UserListingActivity.class);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, int flags) {
        Intent intent = new Intent(context, UserListingActivity.class);
        intent.setFlags(flags);
        context.startActivity(intent);
    }

    protected  void  onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_bottombar_main);


        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        mBottombar = BottomBar.attach(this,saveInstanceState);
        mBottombar.useDarkTheme();
        mBottombar.useFixedMode();
        mBottombar.setItemsFromMenu(R.menu.bottombar_menu, new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {

                if(menuItemId == R.id.Home ){
                    IndexFragment fragment = new IndexFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_bottombar, fragment).commit();
                }else if(menuItemId == R.id.Profile){
                    ProfileFragment fragment = new ProfileFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_bottombar, fragment).commit();
                }else if(menuItemId == R.id.History){
                    HistoryFragment fragment = new HistoryFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_bottombar, fragment).commit();
                }else if(menuItemId == R.id.Chat){
//                    UserFragment fragment = new UserFragment();
//                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_bottombar, fragment).commit();
                    UserListingActivity.startActivity(BottombarMainActivity.this);
                }else if(menuItemId == R.id.Website) {
                    WebViewFragment fragment = new WebViewFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_bottombar, fragment).commit();
                }else if(menuItemId == R.id.Menu){
                    MenuFragment fragment = new MenuFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_bottombar, fragment).commit();

                }
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {

            }

        });


        chatFragment = new ChatFragment();
        int amount = chatFragment.getItemCount();


        BottomBarBadge unread;
        unread = mBottombar.makeBadgeForTabAt(3,"#0174DF",amount);
        unread.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem itemSearch=menu.findItem(R.id.action_search);
        itemSearch.setVisible(false);

        MenuItem itemBack=menu.findItem(R.id.action_back);
        itemBack.setVisible(false);

        MenuItem itemManual=menu.findItem(R.id.action_manual);
        itemManual.setVisible(false);

        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id == R.id.action_manual){

            ManualFragment fragment = new ManualFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_bottombar, fragment).commit();
            return true;

        }else if(id == R.id.action_back){
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
