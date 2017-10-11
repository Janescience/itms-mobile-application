package system.management.information.itms;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.ActionBarActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

/**
 * Created by janescience on 28/8/2560.
 */

public class BottombarMainActivity extends ActionBarActivity{

    BottomBar mBottombar;
    FirebaseAuth firebaseAuth;
    private Typeface Fonts;

    protected  void  onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_bottombar_main);

        Fonts = Typeface.createFromAsset(getAssets(), "fonts/Kanit-Light.ttf");

        firebaseAuth = FirebaseAuth.getInstance();

        //if the user is not logged in
        //that means current user will return null
        if(firebaseAuth.getCurrentUser() == null){
            //closing this activity
            finish();
            //starting login activity
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
                    ChatFragment fragment = new ChatFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_bottombar, fragment).commit();
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

    }

}
