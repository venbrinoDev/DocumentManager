package company.android.documentmanager.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import company.android.documentmanager.Adapter.FileViewAdapter;
import company.android.documentmanager.Ads.FaceBooKAds;
import company.android.documentmanager.inteface.CallHome;
import company.android.documentmanager.DialogFragment.RecentDialog;
import company.android.documentmanager.Executor.AppExecutor;
import company.android.documentmanager.Model.FileViewModel;
import company.android.documentmanager.inteface.NotifyFragment;
import company.android.documentmanager.R;
import company.android.documentmanager.inteface.RecyclerViewClick;
import company.android.documentmanager.RoomDatabase.Database;
import company.android.documentmanager.RoomDatabase.RecentFile;
import company.android.documentmanager.inteface.SearchInterface;
import company.android.documentmanager.Utility.GotoReadears;

public class RecentFilesFragment extends Fragment implements SearchInterface, RecyclerViewClick, NotifyFragment {


    private GotoReadears gotoReadears;
    @BindView(R.id.RecyclerBottomSheet)
    RecyclerView recyclerBottomSheet;
    @BindView(R.id.emptyFolder)
    LinearLayout emptyFolder;

    @BindView(R.id.searchBody)
    EditText searchBody;
    @BindView(R.id.search_button)
    Button searchButton;

    @BindView(R.id.progress)
    ProgressBar progressBar;
    @BindView(R.id.mainLayout)
    CoordinatorLayout mainLayout;
    @BindView(R.id.serchHeading)
    RelativeLayout fullSearchBody;
    ArrayList<FileViewModel> documents;

    @BindView(R.id.banner_container)LinearLayout bannerView;


    Database recentDatabase;
    private CallHome callHome;
    FileViewAdapter fileViewAdapter;
    FaceBooKAds faceBooKAds;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View root = inflater.inflate(R.layout.fragment_recent_files, container, false);

        setHasOptionsMenu(true);

        requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed()
            {
                if (fullSearchBody.getVisibility()==View.VISIBLE){
                    Visible(fullSearchBody,false);
                }else {
                    callHome.CallHome();
                }

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

        Visible(fullSearchBody,false);

        documents = new ArrayList<>();

        recentDatabase = Database.getDatabaseInstance(getContext());
        gotoReadears = new GotoReadears(getActivity());
        retrieveFromDataBase();
        EditTextSearch();
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);

        MenuItem search = menu.findItem(R.id.action_radio);

        if (search !=null){
            search.setVisible(true);
        }

    }


    //Implement Search Interface
    private void EditTextSearch(){
        searchBody.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filter(charSequence);


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    //Filter Search Interface
    private void filter(CharSequence charSequence){
        if (charSequence.length()!=0){
            ArrayList<FileViewModel> temp = new ArrayList<>();

            for (FileViewModel homeDocument:documents){

                if (homeDocument.getName().trim().toLowerCase().contains(charSequence.toString().trim().toLowerCase())){
                    temp.add(new FileViewModel(homeDocument.getFile(),homeDocument.getFile().length(),homeDocument.getName(),homeDocument.getFile().lastModified()));
                }
            }
            settingUpTheView(temp);

        }else{
            settingUpTheView(documents);
        }
    }

    @Override
    public void Search() {
        rotateVisibility(fullSearchBody);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        callHome =(CallHome)context;
    }

    public void rotateVisibility(View view){

        if (view.getVisibility()==View.VISIBLE){
            view.setVisibility(View.GONE);

        }else{
            view.setVisibility(View.VISIBLE);
        }

    }

    private void retrieveFromDataBase(){
        Handler handler = new Handler();

        AppExecutor.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {

                List<RecentFile> recentFiles = recentDatabase.Dao().getPersonList();

                for (RecentFile file:recentFiles){
                    File fileFromPath = new File(file.getAbsolutePath());

                    if (fileFromPath.exists()){
                        documents.add(new FileViewModel(fileFromPath,fileFromPath.length(),fileFromPath.getName(),fileFromPath.lastModified()));
                    }else{
                        AppExecutor.getInstance().getDiskIO().execute(() -> recentDatabase.Dao().deleteFile(file));
                    }
                }

                handler.post(
                        new Runnable() {
                            @Override
                            public void run() {
                                settingUpTheView(documents);
                            }
                        }
                );

            }
        });


    }

    @OnClick({R.id.searchBody,R.id.search_button}) void Search(View view){
        int id = view.getId();
        if (id == R.id.searchBody){



        }else if (id == R.id.search_button){

            InputMethodManager inputMethodManager =(InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(mainLayout.getWindowToken(),0);

        }

    }


    private void Visible(View view ,boolean which){
        if (which){

            if (view.getVisibility() == View.INVISIBLE ||view.getVisibility()== View.GONE) {
                view.setVisibility(View.VISIBLE);
            }


        }else{
            if (view.getVisibility() == View.VISIBLE) {
                view.setVisibility(View.GONE);
            }
        }

    }

    private synchronized void settingUpTheView(ArrayList<FileViewModel> documents){

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());



        if (documents.isEmpty()){
            Visible(emptyFolder,true);
        }else{
            Collections.reverse(documents);
            Visible(emptyFolder,false);
        }
        fileViewAdapter = new FileViewAdapter(documents,getActivity(),this, this);
        recyclerBottomSheet.setLayoutManager(layoutManager);
        recyclerBottomSheet.setHasFixedSize(true);
        recyclerBottomSheet.setHapticFeedbackEnabled(true);
        recyclerBottomSheet.setAdapter(fileViewAdapter);

    }

    public void callHomeMethod(){
        documents.clear();
        retrieveFromDataBase();

    }
    @Override
    public void ChangeValue(File file) {
        RecentDialog openActionDialog = new RecentDialog();
        openActionDialog.setDialogValue(file,this);
        openActionDialog.show(getChildFragmentManager(),"recentFile");
    }

    @Override
    public void onClickItem(int position, File file, String filePath) {
gotoReadears.gotToActivity(file.getAbsolutePath());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (gotoReadears.CheckActivity()!=null){
            gotoReadears.Destroy();
        }
        if (gotoReadears!=null){
            gotoReadears=null;
        }

        if (faceBooKAds !=null){
            faceBooKAds.DestroyAds();
        }
    }
}

