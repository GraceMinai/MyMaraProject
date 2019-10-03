package www.mara.android.com.AdaptersPackage;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

import www.mara.android.com.R;
import www.mara.android.com.UploadClassesPackage.UserInformation;

public class RetriveUserInfoAdapter extends ArrayAdapter<UserInformation>
{
    private Activity context;
    private List<UserInformation> userInformationList;

    public RetriveUserInfoAdapter(Activity context, List<UserInformation> userInformationList)
    {
        super(context, R.layout.sample_userinfo_layout, userInformationList);
        this.context = context;
        this.userInformationList = userInformationList;
    }



    @NonNull
    @Override
    public View getView(int position, View convertView,  ViewGroup parent)
    {

        LayoutInflater layoutInflater = context.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.sample_userinfo_layout, null , true);

        UserInformation userInformation = userInformationList.get(position);

        //Finding the views by id
        TextView retriveUserFirstName = view.findViewById(R.id.ret_userFirstName);
        TextView retriveUserSecondName = view.findViewById(R.id.ret_userSecondName);
        TextView retriveUserSurname = view.findViewById(R.id.ret_userSurname);
        TextView retriveUserEmailAddress = view.findViewById(R.id.ret_userEmailAddress);
        TextView retriveUserPhoneNumber = view.findViewById(R.id.ret_userPhonenumber);
        TextView retriveUserAge = view.findViewById(R.id.ret_userAge);
        TextView retriveUserPlaceOfRecidence = view.findViewById(R.id.ret_userPlaceOfRecidence);
        TextView retriveUserIssue = view.findViewById(R.id.ret_userIssue);
        TextView retriveUserNeed = view.findViewById(R.id.ret_userNeed);

        retriveUserFirstName.setText("First name : " + userInformation.getmUserFirstName());
        retriveUserSecondName.setText("Second name : " + userInformation.getmUserSecondName());
        retriveUserSurname.setText("Surname : " + userInformation.getmUserSurname());
        retriveUserEmailAddress.setText("Email address : "+ userInformation.getmUserEmailAdress());
        retriveUserPhoneNumber.setText("Phone number : " + userInformation.getmUserPhoneNumber());
        retriveUserAge.setText("Age :" + userInformation.getmUserAge());
        retriveUserPlaceOfRecidence.setText("Place of residence : " + userInformation.getmUserPlaceOfResidence());
        retriveUserIssue.setText("Issue of concern : " + userInformation.getmUserIssue());
        retriveUserNeed.setText("Need  : " + userInformation.getmUserNeed());


        return view;
    }
}
