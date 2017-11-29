package system.management.information.itms;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class SplashScreenActivity extends Activity {
    Typeface Fonts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Fonts = Typeface.createFromAsset(this.getAssets(),"fonts/Kanit-Light.ttf");
        TextView system = (TextView) findViewById(R.id.system);
        TextView systemSub = (TextView) findViewById(R.id.system_sub);
        system.setTypeface(Fonts);
        systemSub.setTypeface(Fonts);
        Thread myThread = new Thread(){
            public void run(){
                try{
                    sleep(5500);

                        Intent intent = new Intent(getApplicationContext(), CheckLoginActivity.class);
                        startActivity(intent);

                    finish();
                }catch (InterruptedException e){
                    e.printStackTrace();

                }
            }
        };
        myThread.start();
    }
}
