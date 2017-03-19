package bipin.me.dailymotivation.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import bipin.me.dailymotivation.fragments.intro.Fragment1;
import bipin.me.dailymotivation.fragments.intro.Fragment2;
import bipin.me.dailymotivation.fragments.intro.Fragment3;
import bipin.me.dailymotivation.fragments.intro.Fragment4;

/**
 * Created by BipinSutar on 15/03/17.
 */

public class SplashScreenPagerAdapter extends FragmentStatePagerAdapter {

    public SplashScreenPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new Fragment1();
            case 1:
                return new Fragment2();
            case 2:
                return new Fragment3();
            case 3:
                return new Fragment4();
        }
        return new Fragment4();
    }

    @Override
    public int getCount() {
        return 3;
    }
}
