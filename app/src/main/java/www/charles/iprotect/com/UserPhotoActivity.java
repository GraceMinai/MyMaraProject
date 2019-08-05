package www.charles.iprotect.com;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserPhotoActivity extends AppCompatActivity {

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
