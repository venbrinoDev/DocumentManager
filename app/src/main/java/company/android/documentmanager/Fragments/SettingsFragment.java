package company.android.documentmanager.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import company.android.documentmanager.Ads.FaceBooKAds;
import company.android.documentmanager.inteface.CallHome;
import company.android.documentmanager.R;
import company.android.documentmanager.inteface.SearchInterface;
import company.android.documentmanager.Utility.AppPrefrences;
import company.android.documentmanager.Utility.Constant;

public class SettingsFragment extends Fragment implements SearchInterface {

    @BindView(R.id.checkbox) CheckBox displayFullPath;
@BindView(R.id.fullLayout)
    LinearLayout layout;

    @BindView(R.id.banner_container)LinearLayout bannerView;

    AppPrefrences appPrefrences;
    private CallHome callHome;

    boolean checkedValue = false;
    private final String SHAREKEY="setting_key";
    FaceBooKAds faceBooKAds;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View root = inflater.inflate(R.layout.fragment_settings, container, false);

        setHasOptionsMenu(true);

        requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                callHome.CallHome();
            }
        });


        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this,view);

         faceBooKAds = new FaceBooKAds(getContext());

        faceBooKAds.LoadBannerAds(bannerView);
        faceBooKAds.LoadInterstitial();

        appPrefrences = new AppPrefrences(getContext(),Constant.SHARED_PREF_USED_FIRST);

        checkedValue = appPrefrences.getBoolean(Constant.DISPLAY_FULL_PATH_KEY,false);
        displayFullPath.setChecked(checkedValue);


        displayFullPath.setClickable(false);

        layout.setOnClickListener(v -> {
            checkedValue = !checkedValue;
            appPrefrences.putBoolean(Constant.DISPLAY_FULL_PATH_KEY,checkedValue);
            displayFullPath.setChecked(checkedValue);
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);





    }


    private void checkChange(){






    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);

        MenuItem search = menu.findItem(R.id.action_radio);

        if (search !=null){
            search.setVisible(false);
        }

    }

    @Override
    public void Search() {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        callHome =(CallHome)context;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (faceBooKAds !=null){
            faceBooKAds.DestroyAds();
        }
    }
}
