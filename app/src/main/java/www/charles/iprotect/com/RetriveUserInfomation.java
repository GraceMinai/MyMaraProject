package www.charles.iprotect.com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RetriveUserInfomation extends AppCompatActivity
{
    private ListView mListView;
    DatabaseReference mDatabaseRef;

    private  List<UserInformation> userInformationList;
    private RetriveUserInfoAdapter retriveUserInfoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrive_user_infomation);

        mListView = findViewById(R.id.userInfoListView);

        //Initializing the database reference
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Posted_issues");

        userInformationList = new ArrayList<>();

        retriveUserInfoAdapter = new RetriveUserInfoAdapter(RetriveUserInfomation.this, userInformationList);





    }

    @Override
    protected void onStart()
    {
        //Setting a listener the the database ref for it to retrive the data

        mDatabaseRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                //Getting the data from firebase
                for (DataSnapshot mDatasnapshot : dataSnapshot.getChildren())
                {
                    UserInformation userInformation = mDatasnapshot.getValue(UserInformation.class);
                    //adding the information to the listview
                    userInformationList.add(userInformation);
                }
                //setting an adapter to the listview
                mListView.setAdapter(retriveUserInfoAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
        super.onStart();
    }
}
