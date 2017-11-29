package system.management.information.itms;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarBadge;
import com.roughike.bottombar.OnMenuTabClickListener;

import system.management.information.itms.Ui.Activity.UserListingActivity;
import system.management.information.itms.Ui.Fragment.UserFragment;

public class BottombarUserMainActivity extends ActionBarActivity {

    private BottomBar mBottombar;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottombar_user_main);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        mBottombar = BottomBar.attach(this,savedInstanceState);
        mBottombar.useDarkTheme();
        mBottombar.useFixedMode();
        mBottombar.setItemsFromMenu(R.menu.bottombar_user_menu, new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {

                if(menuItemId == R.id.Home ){
                    IndexFragment fragment = new IndexFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_user_bottombar, fragment).commit();
                }else if(menuItemId == R.id.Profile){
                    ProfileFragment fragment = new ProfileFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_user_bottombar, fragment).commit();
                }else if(menuItemId == R.id.Chat){
                    UserFragment fragment = new UserFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_user_bottombar, fragment).commit();
                }else if(menuItemId == R.id.Menu){
                    MenuFragment fragment = new MenuFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_user_bottombar, fragment).commit();

                }
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {

            }

        });



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
