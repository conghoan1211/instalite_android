package vn.edu.fpt.instalite.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import vn.edu.fpt.instalite.fragments.AddPostFragment;
import vn.edu.fpt.instalite.fragments.HomeFragment;
import vn.edu.fpt.instalite.fragments.NotificationFragment;
import vn.edu.fpt.instalite.fragments.ProfileFragment;
import vn.edu.fpt.instalite.fragments.SearchFragment;

public class MainAdapter extends FragmentStatePagerAdapter {
    public MainAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 1:
                return new SearchFragment();
            case 2:
                return new AddPostFragment();
            case 3:
                return new NotificationFragment();
            case 4:
                return new ProfileFragment();
            default:
                return new HomeFragment();
        }
    }

    @Override
    public int getCount() {
        return 5;
    }
}
