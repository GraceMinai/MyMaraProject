package www.mara.android.com.ActivitiesPackage;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import www.mara.android.com.R;


public class GrantPermission extends AppCompatActivity
{

    private Button btnGrantPermission;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grant_permission);

        btnGrantPermission = (Button) findViewById(R.id.btn_grantPermission);

        /**
         * Setting validations to check if the permission has already been granted.
         * If the permission is granted prior to the start of this activity
         * then user will be directed to the map(Main Activity)
         * if not, the user will be required to grant the permission first
         */

        if (ContextCompat.checkSelfPermission(GrantPermission.this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)
        {
            //if the permission is already granted the MapsActivity will start
            startActivity(new Intent(GrantPermission.this, MainActivity.class));
            //Finish the activity
            finish();
            //Calling return
            return;
        }
        else
        {
            btnGrantPermission.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    //requesting for runtime permission using dexter
                    Dexter.withActivity(GrantPermission.this)
                            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                            .withListener(new PermissionListener()
                            {
                                @Override
                                public void onPermissionGranted(PermissionGrantedResponse response)
                                {
                                    //If the permission is granted then send the uer to MapsActivity
                                    startActivity(new Intent(GrantPermission.this, MainActivity.class));
                                    finish();


                                }

                                @Override
                                public void onPermissionDenied(PermissionDeniedResponse response)
                                {
                                    /**
                                     * If the permission is permanently denied th uer needs to go to the settings to allow the permission
                                     * We'll send the user a message using an alertdialogue
                                     * Also enable a button in the dialoge that will send the user too the settings
                                     */

                                    if (response.isPermanentlyDenied())
                                    {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(GrantPermission.this);
                                        builder.setTitle("Permission Denied");
                                        builder.setMessage("Permission to access device location has been permanently denied. You need to go to Settings and allow the permission")
                                                .setNegativeButton("Cancel", null)
                                                .setPositiveButton("Ok", new DialogInterface.OnClickListener()
                                                {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which)
                                                    {
                                                        //sending the user to the settings
                                                        Intent settingsIntent = new Intent();
                                                        settingsIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                        settingsIntent.setData(Uri.fromParts("package", getPackageName(), null));
                                                        startActivity(settingsIntent);
                                                    }
                                                })
                                                .show();
                                    }
                                    //If the permission is not permanently denied we'll show the user a Toast. Its upto then to allow the permission
                                    else
                                    {
                                        Toast.makeText(GrantPermission.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                                    }

                                }

                                @Override
                                public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token)
                                {
                                         token.continuePermissionRequest();
                                }
                            })
                            .check();

                    
                }
            });
        }


    }
}
