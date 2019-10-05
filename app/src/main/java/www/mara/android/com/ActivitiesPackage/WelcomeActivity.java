package www.mara.android.com.ActivitiesPackage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import www.mara.android.com.R;

public class WelcomeActivity extends AppCompatActivity
{


    public void onAttachedToWindow()
    {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        StartAnimations();


    }

    private void StartAnimations()
    {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        LinearLayout l = (LinearLayout) findViewById(R.id.lin_lay);
        l.clearAnimation();
        l.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        TextView iv = (TextView) findViewById(R.id.splash);
        iv.clearAnimation();
        iv.startAnimation(anim);

        Thread welcomeThread = new Thread()
        {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    // Splash screen pause time
                    while (waited < 3500)
                    {
                        sleep(100);
                        waited += 100;
                    }
                    Intent intent = new Intent(WelcomeActivity.this,
                            GrantPermission.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    WelcomeActivity.this.finish();
                }
                catch (InterruptedException e)
                {
                    //Run splash
                } finally
                {
                    WelcomeActivity.this.finish();
                }

            }
        };
        welcomeThread.start();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        finish();
    }
}
