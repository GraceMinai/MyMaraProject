package www.charles.iprotect.com;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter
{
    /**
     * Generating a constructor for the pager adapter
     */

    public PagerAdapter(FragmentManager fragmentManager)
    {
        super(fragmentManager);
    }
    /**
     * getting the position of the fragment that the user has selected.
     */
    @Override
    public Fragment getItem(int position)
    {
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
    public int getCount()
    {
        return 3;
    }

    /**
     * Setting the title of the tab on the view pager
     */
    @Nullable
    @Override
    public CharSequence getPageTitle(int position)
    {
      switch (position)
      {
          case 0:
              return "REPORTS";
          case 1:
              return "Help";
          case 2:
              return "Chats";
              default:

                  return null;
      }
    }
}
