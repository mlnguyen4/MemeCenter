package teamsylvanmatthew.memecenter.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import teamsylvanmatthew.memecenter.R;


public class SearchFragment extends Fragment {
    private Activity mActivity;
    private View mView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_search, container, false);
        mActivity = getActivity();

        /* Remove Loading when Layout is ready */
        FragmentManager fragmentManager = getFragmentManager();
        Fragment loadingFragment = fragmentManager.findFragmentByTag("TAG_LOADING");

        if (loadingFragment != null) {
            fragmentManager.beginTransaction().remove(loadingFragment).commit();
        }

        return mView;
    }
}
