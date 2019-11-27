package www.mara.android.com.ActivitiesPackage;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import www.mara.android.com.R;
import www.mara.android.com.UploadClassesPackage.UserInformation;

public class ConnectWithExpert extends AppCompatActivity
{

    private EditText userFirstName, userSecondName, userSurname, userDateOfBirth,
            userEmailAdress, userPhoneNumber,userPlaceOfResidence,
            userIssue,usersNeed;
    private ProgressDialog mProgressDialog;

    private Button btnSummit;


    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseRef;

    private Class<UserInformation> UserIssuesUploadClass;

    private DatePickerDialog.OnDateSetListener userDatesetListener;




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_with_expert);
        Toolbar contactProfToolbar = findViewById(R.id.contactProf_toolbar);

        setSupportActionBar(contactProfToolbar);
        getSupportActionBar().setTitle("Connect With Expert");
        contactProfToolbar.setNavigationIcon(R.drawable.ic_toolbar_nav_back);
        contactProfToolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onBackPressed();
            }
        });

        //Creating an instance to the progress bar

        mProgressDialog = new ProgressDialog(this);



        userEmailAdress = findViewById(R.id.userEmailAddress);
        userFirstName = findViewById(R.id.userFirstName);
        userSecondName = findViewById(R.id.userSecondName);
        userSurname = findViewById(R.id.userSurname);
        userPhoneNumber = findViewById(R.id.userPhoneNumber);
        userPlaceOfResidence = findViewById(R.id.usersPlaceOfRecidence);
        userIssue = findViewById(R.id.userIssue);
        usersNeed = findViewById(R.id.usersNeed);
        userDateOfBirth = findViewById(R.id.userAge);


        //Creating an instance to the databaseRef

        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Posted_issues");

        btnSummit = (Button)findViewById(R.id.btn_submmit);

        //Checking if the user has accepted terms of use
       btnSummit.setOnClickListener(new View.OnClickListener()
       {
           @Override
           public void onClick(View v)
           {
               sendInfoToProffessional();
           }
       });

        //Showing the date picker
        userDateOfBirth.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Calendar calendar = Calendar.getInstance();
                //Getting the users year of Birth
                int year = calendar.get(Calendar.YEAR);
                //Getting the users Month of Birth
                int month = calendar.get(Calendar.MONTH);
                //Getting the users Day of birth
                int day = calendar.get(Calendar.DAY_OF_MONTH);


                //Creating a dialog for the date picker
                DatePickerDialog datePickerDialog = new DatePickerDialog(ConnectWithExpert.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth,userDatesetListener, year, month, day);

                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();

            }
        });

        //Initializing onDateSetListener to the object
        userDatesetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day)
            {
                final Calendar calendar = Calendar.getInstance();
                calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH, day);

                //format for displaying the user date of birth
                String dateFormat =  new SimpleDateFormat("dd.MMM.yyyy").format(calendar.getTime());

                //Showing the user's date of birth
                userDateOfBirth.setText(dateFormat);

            }
        };


    }



    private void sendInfoToProffessional()
    {
        //Converting the user input into String

        String mUserFirstName = userFirstName.getText().toString().trim();
        String mUserSecondName = userSecondName.getText().toString().trim();
        String mUserSurname = userSurname.getText().toString().trim();
        String mUserEmailAdress = userEmailAdress.getText().toString().trim();
        String mUserPhoneNumber = userPhoneNumber.getText().toString().trim();
        String mUserPlaceOfResidence = userPlaceOfResidence.getText().toString().trim();
        String mUserIssue = userIssue.getText().toString().trim();
        String mUserNeed = usersNeed.getText().toString().trim();
        String mUserDateOfBirth = userDateOfBirth.getText().toString().trim();


        /**
         *This validation will ensure that the user does not leave a blank space
         */

        if (!TextUtils.isEmpty(mUserFirstName)
            && !TextUtils.isEmpty(mUserSecondName)
            && !TextUtils.isEmpty(mUserSurname)
            && !TextUtils.isEmpty(mUserEmailAdress)
            && !TextUtils.isEmpty(mUserPhoneNumber)
            && !TextUtils.isEmpty(mUserPlaceOfResidence)
            && !TextUtils.isEmpty(mUserIssue)
            && !TextUtils.isEmpty(mUserNeed)
            && !TextUtils.isEmpty(mUserDateOfBirth)
        )
        {
            /**
             * If all spaces are filled correctly
             * The information will be sent to the database
             */






              mProgressDialog.setTitle("Sending information");
              mProgressDialog.setMessage("Please wait.Your information is being sent to health experts");


            //Generating a special id for each user
            String userKey = mDatabaseRef.push().getKey();

            //Uploading the information to the server
            UserInformation userInformation = new UserInformation(mUserFirstName, mUserSecondName, mUserSurname, mUserEmailAdress,
                    mUserPhoneNumber, mUserPlaceOfResidence, mUserIssue, mUserNeed, mUserDateOfBirth);

            mDatabaseRef.child(userKey).setValue(userInformation);


            userFirstName.setText("");
            userSecondName.setText("");
            userSurname.setText("");
            userEmailAdress.setText("");
            userPhoneNumber.setText("");
            userPlaceOfResidence.setText("");
            userDateOfBirth.setText("");
            userIssue.setText("");
            usersNeed.setText("");

            //Dismissing the progress dialog
            mProgressDialog.dismiss();



            //Informing the user that the information has been uploaded
            Toast.makeText(this, "Sent successfully. A health expert will contact you shortly", Toast.LENGTH_LONG).show();


        }
        else
        {
            /**
             * if any field is left bank this message will display to the user
             */

            Toast.makeText(this, "Ensure that all spaces are filled correctly then try again",
                    Toast.LENGTH_SHORT).show();
        }

    }

    //This method is for hiding the keyboard when the activity is launched





    //Checking for the connection

    public boolean isConnected(Context context)
    {

        ConnectivityManager  connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnectedOrConnecting())
        {
            android.net.NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobileData = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if ((mobileData != null && mobileData.isConnectedOrConnecting()) || wifi != null && wifi.isConnectedOrConnecting())
            {
                return true;
            }
            else {
                return false;
            }

        }
        else
        {
            return false;
        }
    }

    //Displaying a dialog box for internet connection

    public AlertDialog.Builder buildDialog(Context context)
    {
        final  AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("No internet connection");
        builder.setMessage("You need to have Mobile data or Wifi to connect with the health expert.");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                finish();
            }
        });

        return builder;
    }

}
