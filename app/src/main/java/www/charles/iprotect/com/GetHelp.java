package www.charles.iprotect.com;

import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class GetHelp extends AppCompatActivity
{

    private EditText userFirstName, userSecondName, userSurname, userAge,
            userEmailAdress, userPhoneNumber,userPlaceOfResidence,
            userIssue,usersNeed;

    private Button btnSummit;
    private CheckBox mCheckBox;

    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseRef;

    private Class<UserInformation> UserIssuesUploadClass;




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_help);



        userEmailAdress = findViewById(R.id.userEmailAddress);
        userFirstName = findViewById(R.id.userFirstName);
        userSecondName = findViewById(R.id.userSecondName);
        userSurname = findViewById(R.id.userSurname);
        userPhoneNumber = findViewById(R.id.userPhoneNumber);
        userPlaceOfResidence = findViewById(R.id.usersPlaceOfRecidence);
        userIssue = findViewById(R.id.userIssue);
        usersNeed = findViewById(R.id.usersNeed);
        userAge = findViewById(R.id.userAge);

        mCheckBox = findViewById(R.id.user_checkbox);

        //Creating an instance to the databaseRef

        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Posted_issues");

        btnSummit = (Button)findViewById(R.id.btn_submmit);

        //Checking if the user has accepted terms of use
        btnSummit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (mCheckBox.isChecked())
                {
                    btnSummit.setBackgroundResource(R.color.colorPrimaryDark);
                    sendInfoToProffessional();
                }
                else if (!mCheckBox.isChecked())
                {
                    btnSummit.setBackgroundResource(R.color.colorAccent);
                    Toast.makeText(GetHelp.this, "Please accept terms of use before proceeding", Toast.LENGTH_SHORT).show();
                }
            }
        });



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
        String mUserAge = userAge.getText().toString().trim();


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
            && !TextUtils.isEmpty(mUserAge))
        {
            /**
             * If all spaces are filled correctly
             * The information will be sent to the database
             */


            //Generating a special id for each user
            String userKey = mDatabaseRef.push().getKey();

            //Uploading the information to the server
            UserInformation userInformation = new UserInformation(mUserFirstName, mUserSecondName, mUserSurname, mUserEmailAdress,
                    mUserPhoneNumber, mUserPlaceOfResidence, mUserIssue, mUserNeed, mUserAge);

            mDatabaseRef.child(userKey).setValue(userInformation);
            userFirstName.setText("");
            userSecondName.setText("");
            userSurname.setText("");
            userEmailAdress.setText("");
            userPhoneNumber.setText("");
            userPlaceOfResidence.setText("");
            userAge.setText("");
            userIssue.setText("");
            usersNeed.setText("");

            //Informing the user that the information has been uploaded
            Toast.makeText(this, "Sent successfully", Toast.LENGTH_SHORT).show();


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

}
