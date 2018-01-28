package christian.ispyai.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import christian.ispyai.Fragments.Camera.MainFragment;
import christian.ispyai.Fragments.LeaderBoardFragment;
import christian.ispyai.Fragments.MissionFragment;

/**
 * Created by christianmaschka on 27/01/2018.
 */

public class MainPagerAdapter extends FragmentPagerAdapter {
    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    private Fragment mCurrentFragment;

    public Fragment getCurrentFragment() {
        return mCurrentFragment;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return LeaderBoardFragment.create();

            case 1:
                return MainFragment.create();

            case 2:
                return MissionFragment.create();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (getCurrentFragment() != object) {
            mCurrentFragment = ((Fragment) object);
        }
        super.setPrimaryItem(container, position, object);
    }
}
