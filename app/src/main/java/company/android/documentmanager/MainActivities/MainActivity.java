package company.android.documentmanager.MainActivities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.Arrays;

import company.android.documentmanager.Adapter.MenuAdapter;
import company.android.documentmanager.Ads.FaceBooKAds;
import company.android.documentmanager.BuildConfig;
import company.android.documentmanager.Fragments.BookmarkFragment;
import company.android.documentmanager.Fragments.FileManagerFragment;
import company.android.documentmanager.Fragments.FileViewFragment;
import company.android.documentmanager.Fragments.FolderViewFragment;
import company.android.documentmanager.Fragments.HomeFragment;
import company.android.documentmanager.Fragments.RecentFilesFragment;
import company.android.documentmanager.Fragments.SettingsFragment;
import company.android.documentmanager.R;
import company.android.documentmanager.Utility.AppPrefrences;
import company.android.documentmanager.inteface.CallHome;
import company.android.documentmanager.inteface.SearchInterface;
import nl.psdcompany.duonavigationdrawer.views.DuoDrawerLayout;
import nl.psdcompany.duonavigationdrawer.views.DuoMenuView;
import nl.psdcompany.duonavigationdrawer.widgets.DuoDrawerToggle;


public class MainActivity extends
//        TutorialActivity,
        AppCompatActivity implements DuoMenuView.OnMenuClickListener, CallHome {

    private AppPrefrences prefrences;
    private MenuAdapter mMenuAdapter;
    private ViewHolder mViewHolder;
    private ArrayList<String> mTitles = new ArrayList<>();
    private SearchInterface searchInterface;
    AppPrefrences sharedPreferences;
    String KEY="savePop";
    boolean saveState=false;
    AppPrefrences trackAppBegining;

    FaceBooKAds faceBooKAds;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

;
//        addFragment(
//                new Step.Builder()
//                        .setTitle("Title one")
//                        .setContent("Content 1")
//                        .setBackgroundColor(Color.parseColor("#FF0957"))
//                        .setDrawable(R.drawable.getstarted)
//                        .build());
//        addFragment(
//                new Step.Builder()
//                        .setTitle("Title two")
//                        .setContent("Content 1")
//                        .setBackgroundColor(Color.parseColor("#FF0957"))
//                        .setDrawable(R.drawable.onboarding1)
//                        .build());
//        addFragment(
//                new Step.Builder()
//                        .setTitle("Title three")
//                        .setContent("Content 1")
//                        .setBackgroundColor(Color.parseColor("#00D4BA"))
//                        .setDrawable(R.drawable.onboarding2)
//                        .build());
//        addFragment(
//                new Step.Builder()
//                        .setTitle("Title four")
//                        .setContent("Content 1")
//                        .setBackgroundColor(Color.parseColor("#1098FE"))
//                        .setDrawable(R.drawable.onboarding3)
//                        .setSummary("This is a summary")
//                        .build());

//        MobileAds.initialize(this, new OnInitializationCompleteListener() {
//            @Override
//            public void onInitializationComplete(InitializationStatus initializationStatus) {
//
//            }
//        });

        faceBooKAds = new FaceBooKAds(this);
        faceBooKAds.LoadInterstitial();



        String SHARED_KEY = "mainKey";

        sharedPreferences = new AppPrefrences(this, SHARED_KEY);
        trackAppBegining = new AppPrefrences(this, SHARED_KEY);


        mTitles = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.menuOptions)));


        // Initialize the views
        mViewHolder = new ViewHolder();

        // Handle toolbar actions
        handleToolbar();

        // Handle menu actions
        handleMenu();

        // Handle drawer actions
        handleDrawer();

        // Show main fragment in container
        goToFragment(new HomeFragment(), false);
        mMenuAdapter.setViewSelected(0, true);
        setTitle(mTitles.get(0));
        TracKAppBegining();




    }

    private void TracKAppBegining() {
        String BEGIN_KEY="beginKey";
        if (trackAppBegining.getBoolean(BEGIN_KEY,true)){
            trackAppBegining.putBoolean(BEGIN_KEY,false);
        }else {
            shouldShowPopUp();
        }
    }

    private  void shouldShowPopUp(){

        boolean showPop = sharedPreferences.getBoolean(KEY,true);
        if (showPop){
            ShowInitialRating();
        }

    }

    private void handleToolbar() {
        setSupportActionBar(mViewHolder.mToolbar);


    }

    private void  setSearch (SearchInterface search){
        this.searchInterface=search;
    }

    private void handleDrawer() {
        DuoDrawerToggle duoDrawerToggle = new DuoDrawerToggle(this,
                mViewHolder.mDuoDrawerLayout,
                mViewHolder.mToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

//        duoDrawerToggle.setDrawerIndicatorEnabled(false);
        mViewHolder.mDuoDrawerLayout.setDrawerListener(duoDrawerToggle);
        duoDrawerToggle.syncState();

    }

    private void handleMenu() {
        mMenuAdapter = new MenuAdapter(mTitles,MainActivity.this);

        mViewHolder.mDuoMenuView.setOnMenuClickListener(this);
        mViewHolder.mDuoMenuView.setAdapter(mMenuAdapter);
    }

    @Override
    public void onFooterClicked() {
mViewHolder.mDuoDrawerLayout.closeDrawer();
    }



    @Override
    public void onHeaderClicked() {


    }

    private void goToFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (addToBackStack) {
            transaction.addToBackStack(null);
        }

        transaction.add(R.id.container, fragment).commit();
        setSearch((SearchInterface)fragment);
    }

    @Override
    public void onOptionClicked(int position, Object objectClicked) {
        // Set the toolbar title
//        setTitle(mTitles.get(position));

        // Set the right options selected
        mMenuAdapter.setViewSelected(position, true);

        // Navigate to the right fragment
        switch (position) {
            case 0:
                setTitle("Home");

                goToFragment(new HomeFragment(), false);
                break;
            case 1:
                setTitle("All Folder");
                goToFragment(new FolderViewFragment(), false);
                break;
            case 2:
                setTitle("All File");
                goToFragment(new FileViewFragment(), false);
                break;
            case 3:
                setTitle("File Manager");
                goToFragment(new FileManagerFragment(), false);
                break;
            case 4:
                setTitle("Recent Files");
                goToFragment(new RecentFilesFragment(), false);
                break;
            case 5:
                setTitle("Bookmark Files");
                goToFragment(new BookmarkFragment(), false);
                break;
            case 6:
                setTitle("Settings");
                goToFragment(new SettingsFragment(), false);
                break;
            case 7:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey, I Recommend this App for you. It's a cool looking Document Manager for your Android Device, you would definitely like it. Check it out on Play Store: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;
            case 8:
                ShowRating();
                break;
            default:
                goToFragment(new HomeFragment(), false);
                break;
        }

        // Close the drawer
        mViewHolder.mDuoDrawerLayout.closeDrawer();
    }

//    @Override
//    public void currentFragmentPosition(int position) {
//        Toast.makeText(this, "Position : " + position, Toast.LENGTH_SHORT).show();
//    }

    public void ShowRating(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.rate_dialog, viewGroup, false);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();

        dialogView.findViewById(R.id.closedialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        RatingBar ratingBar =dialogView.findViewById(R.id.rating);
       ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
           @Override
           public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
               if (rating ==5){
                   if (fromUser){
                       final String appPackageName = getPackageName();
                       try {
                           startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                       }catch (android.content.ActivityNotFoundException anfe){
                           startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));

                       }
                   }
               }else{
                   alertDialog.cancel();
               }


           }
       });


    }
    public void ShowInitialRating(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.rate_dialog, viewGroup, false);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();

        dialogView.findViewById(R.id.closedialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        RatingBar ratingBar =dialogView.findViewById(R.id.rating);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (rating ==5 || rating==4){
                    if (fromUser){
                        sharedPreferences.putBoolean(KEY,false);
                        alertDialog.cancel();
                        saveState=true;

                        final String appPackageName = getPackageName();
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        }catch (android.content.ActivityNotFoundException anfe){
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));

                        }
                    }
                }else{
                    alertDialog.cancel();
                }


            }
        });


    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }



    //    @Override
//    public void finishTutorial() {
//        super.finishTutorial();
//    }



    @Override
    public void exits() {
        boolean showPop = sharedPreferences.getBoolean(KEY,true);
        if (saveState){
          finishActivity();
          return;
        }
        if (showPop){
            saveState=true;
            ShowInitialRating();

        }else {
           finishActivity();
        }

    }

    private void finishActivity(){
        finish();
        finishAffinity();
        System.exit(0);
    }

    @Override
    public void CallHome() {
        setTitle("Home");
    mMenuAdapter.setViewSelected(0,true);
goToFragment(new HomeFragment(),false);
    }

    private class ViewHolder {
        private DuoDrawerLayout mDuoDrawerLayout;
        private DuoMenuView mDuoMenuView;
        private Toolbar mToolbar;

        ViewHolder() {
            mDuoDrawerLayout = (DuoDrawerLayout) findViewById(R.id.drawer);
            mDuoMenuView = (DuoMenuView) mDuoDrawerLayout.getMenuView();
            mToolbar = (Toolbar) findViewById(R.id.toolbar);

            mToolbar.setNavigationIcon(R.drawable.menu);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if(id ==R.id.action_radio){
            searchInterface.Search();
        }
        return super.onOptionsItemSelected(item);
    }

    //    public void setTitle(String title){
////        getSupportActionBar().setHomeButtonEnabled(true);
////        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        TextView textView = new TextView(this);
//        textView.setText(title);
//        textView.setTextSize(20);
//        textView.setTypeface(null, Typeface.BOLD);
//        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
//        textView.setGravity(Gravity.CENTER);
//        textView.setTextColor(getResources().getColor(R.color.colorAccent));
//        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        getSupportActionBar().setCustomView(textView);
//
//    };


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}