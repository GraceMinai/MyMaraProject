package www.charles.iprotect.com;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {

    //creating a constructor for our adapter

    int numberOfTabs;

    public PagerAdapter(FragmentManager fragmentManager, int NumberOfTabs)
    {
        super(fragmentManager);
        //seting the global number of tabs to the local number of tabs

        this.numberOfTabs = NumberOfTabs;
    }


    @Override
    public Fragment getItem(int position) {

       //creating a switch for the position of the tabs

        switch (position)
        {
            case 0:
                TabNewsFeed tabNewsFeed = new TabNewsFeed();
                return tabNewsFeed;
            case 1:
                TabGetHelp tabGetHelp = new TabGetHelp();
                return tabGetHelp;

            case 2:
                TabChat tabChat = new TabChat();
                return tabChat;
            default:
                return null;


        }



    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
