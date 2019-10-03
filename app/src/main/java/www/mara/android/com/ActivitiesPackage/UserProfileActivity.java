package www.mara.android.com.ActivitiesPackage;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;

import de.hdodenhof.circleimageview.CircleImageView;
import www.mara.android.com.R;

public class UserProfileActivity extends AppCompatActivity {

    CircleImageView userProfileImage;
    Button btn_camera;
    Button btn_Gallery;
    private static final int TAKE_PIC_REQUEST = 100;
    private static final int CHOOSE_PIC_REQUEST = 101;
    Uri mUri;



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_photo);



    }


}
