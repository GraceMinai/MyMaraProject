package www.mara.android.com;

public class UserInformation
{

    private String  mUserFirstName;
  private String  mUserSecondName;
  private String  mUserSurname;
  private String  mUserEmailAdress;
  private String mUserPhoneNumber;
  private String  mUserPlaceOfResidence;
  private String mUserIssue;
  private String  mUserNeed;
  private String mUserAge;


    public UserInformation()
    {
        //empty constructor
    }

    public UserInformation(String mUserFirstName, String mUserSecondName, String mUserSurname, String mUserEmailAdress,
                           String mUserPhoneNumber, String mUserPlaceOfResidence, String mUserIssue, String mUserNeed, String mUserAge)
    {
        this.mUserFirstName = mUserFirstName;
        this.mUserSecondName = mUserSecondName;
        this.mUserSurname = mUserSurname;
        this.mUserEmailAdress = mUserEmailAdress;
        this.mUserPhoneNumber = mUserPhoneNumber;
        this.mUserPlaceOfResidence = mUserPlaceOfResidence;
        this.mUserIssue = mUserIssue;
        this.mUserNeed = mUserNeed;
        this.mUserAge = mUserAge;
    }

    public String getmUserFirstName() {
        return mUserFirstName;
    }

    public String getmUserSecondName() {
        return mUserSecondName;
    }

    public String getmUserSurname() {
        return mUserSurname;
    }

    public String getmUserEmailAdress() {
        return mUserEmailAdress;
    }

    public String getmUserPhoneNumber() {
        return mUserPhoneNumber;
    }

    public String getmUserPlaceOfResidence() {
        return mUserPlaceOfResidence;
    }

    public String getmUserIssue() {
        return mUserIssue;
    }

    public String getmUserNeed() {
        return mUserNeed;
    }

    public String getmUserAge() {
        return mUserAge;
    }

    public void setmUserFirstName(String mUserFirstName) {
        this.mUserFirstName = mUserFirstName;
    }

    public void setmUserSecondName(String mUserSecondName) {
        this.mUserSecondName = mUserSecondName;
    }

    public void setmUserSurname(String mUserSurname) {
        this.mUserSurname = mUserSurname;
    }

    public void setmUserEmailAdress(String mUserEmailAdress) {
        this.mUserEmailAdress = mUserEmailAdress;
    }

    public void setmUserPhoneNumber(String mUserPhoneNumber) {
        this.mUserPhoneNumber = mUserPhoneNumber;
    }

    public void setmUserPlaceOfResidence(String mUserPlaceOfResidence) {
        this.mUserPlaceOfResidence = mUserPlaceOfResidence;
    }

    public void setmUserIssue(String mUserIssue) {
        this.mUserIssue = mUserIssue;
    }

    public void setmUserNeed(String mUserNeed) {
        this.mUserNeed = mUserNeed;
    }

    public void setmUserAge(String mUserAge) {
        this.mUserAge = mUserAge;
    }


}
