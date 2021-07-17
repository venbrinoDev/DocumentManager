package company.android.documentmanager.Fragments;


import android.Manifest;
import android.animation.Animator;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import company.android.documentmanager.Adapter.DropAdapter;
import company.android.documentmanager.Adapter.FileViewAdapter;
import company.android.documentmanager.Ads.FaceBooKAds;
import company.android.documentmanager.DialogFragment.OpenActionDialog;
import company.android.documentmanager.Executor.AppExecutor;
import company.android.documentmanager.HandleFile.FileWorker;
import company.android.documentmanager.Model.FileViewModel;
import company.android.documentmanager.Model.SpinnerModel;
import company.android.documentmanager.R;
import company.android.documentmanager.RoomDatabase.Database;
import company.android.documentmanager.RoomDatabase.RecentFile;
import company.android.documentmanager.Utility.GotoReadears;
import company.android.documentmanager.inteface.CallHome;
import company.android.documentmanager.inteface.NotifyFragment;
import company.android.documentmanager.inteface.RecyclerViewClick;
import company.android.documentmanager.inteface.SearchInterface;

public class FileViewFragment extends Fragment implements SearchInterface,AdapterView.OnItemSelectedListener, RecyclerViewClick, NotifyFragment {


   @BindView(R.id.spinner) Spinner dropDown;
   @BindView(R.id.RecyclerBottomSheet) RecyclerView recyclerBottomSheet;
    @BindView(R.id.emptyFolder) LinearLayout emptyFolder;
    @BindView(R.id.flipper) ViewFlipper filpView;
    @BindView(R.id.mainLayout) CoordinatorLayout mainLayout;
    @BindView(R.id.searchBody) EditText searchBody;
    @BindView(R.id.search_button) Button searchButton;
    @BindView(R.id.backButton)Button searchBackButton;
    @BindView(R.id.progress)
    ProgressBar progressBar;

    @BindView(R.id.sizedesc)LinearLayout sizedesc;
    @BindView(R.id.fabSizeWiseDesc) FloatingActionButton fabSizedesc;
    @BindView(R.id.sizeWiseAsec)LinearLayout SizeAsc;
    @BindView(R.id.facSizeWiseAsce) FloatingActionButton fabSizeAse;

    @BindView(R.id.nameWiseAsce)LinearLayout nameWiseAsc;
    @BindView(R.id.fabNameWiseAsec) FloatingActionButton fabNameAsc;
    @BindView(R.id.nameWisedesc)LinearLayout nameWiseDesc;
    @BindView(R.id.fabNameWisedesc) FloatingActionButton fabNameDesc;

    @BindView(R.id.DateWiseDesc)LinearLayout dateWiseDesc;
    @BindView(R.id.fabDateWiseDesc) FloatingActionButton fabDateWiseDesc;
    @BindView(R.id.DateWiseAce)LinearLayout dateWiseAsc;
    @BindView(R.id.fabDateWiseAsce) FloatingActionButton fabDateWiseAsc;


    @BindView(R.id.centeralFab) FloatingActionButton centralFab;

    @BindView(R.id.shadowView)View view;

    @BindView(R.id.banner_container)LinearLayout bannerView;


    boolean isOpen = false;

  ArrayList<SpinnerModel> spinnerModels;
   DropAdapter dropDownAdapter;
    Context globalContext;

    ArrayList<FileViewModel> documents;

    ArrayList<String> homePath;

    FileViewAdapter fileViewAdapter;
    AtomicBoolean atomicBoolean;
     private CallHome callHome;
    Database database;

    GotoReadears gotoReadears;
    String itemSelected;
    FaceBooKAds faceBooKAds;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View root = inflater.inflate(R.layout.fragment_file_view, container, false);
        setHasOptionsMenu(true);


        requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (filpView.getDisplayedChild()==1){
                    ShowViews(filpView);
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

       database = Database.getDatabaseInstance(getContext());
       itemSelected = "all";



    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    globalContext = requireContext();
    if (globalContext==null){
        globalContext = getContext();
    }

        spinnerModels = new ArrayList<>();
        initView();

        gotoReadears = new GotoReadears(getActivity());
        dropDownAdapter = new DropAdapter(globalContext,spinnerModels);
        dropDown.setAdapter(dropDownAdapter);
       dropDown.setOnItemSelectedListener(this);

       homePath = new ArrayList<>();
       documents  = new ArrayList<>();
       Visible(emptyFolder,false);

       atomicBoolean = new AtomicBoolean(true);


        checkFile();
        EditTextSearch();
    }


    @OnClick({R.id.shadowView,R.id.centeralFab,R.id.sizedesc,R.id.sizeWiseAsec,R.id.DateWiseAce,R.id.DateWiseDesc,R.id.nameWisedesc,R.id.nameWiseAsce}) void fabOnClick(View view){
        int id = view.getId();

        if (id==R.id.shadowView){
            ControlViews();
        }
        if (id==R.id.centeralFab){
            ControlViews();
            return;
        }

        if (id==R.id.sizedesc){
            Collections.sort(documents,FileViewModel.compareFileSize);
            Collections.reverse(documents);

            ControlViews();
            if (fileViewAdapter !=null){
                fileViewAdapter.notifyDataSetChanged();
            }
            return;
        }

        if (id==R.id.sizeWiseAsec){
            Collections.sort(documents,FileViewModel.compareFileSize);


            ControlViews();
            if (fileViewAdapter !=null){
                fileViewAdapter.notifyDataSetChanged();
            }

            return;
        }
        if (id==R.id.DateWiseDesc){
            Collections.sort(documents,FileViewModel.compareDate);
            Collections.reverse(documents);

            ControlViews();
            if (fileViewAdapter !=null){
                fileViewAdapter.notifyDataSetChanged();
            }
            return;
        }
        if (id==R.id.DateWiseAce){
            Collections.sort(documents,FileViewModel.compareDate);


            ControlViews();
            if (fileViewAdapter !=null){
                fileViewAdapter.notifyDataSetChanged();
            }
            return;
        }

        if (id==R.id.nameWisedesc){
            Collections.sort(documents,FileViewModel.compareName);
            Collections.reverse(documents);

            ControlViews();
            if (fileViewAdapter !=null){
                fileViewAdapter.notifyDataSetChanged();
            }
            return;
        }

        if (id==R.id.nameWiseAsce){
            Collections.sort(documents,FileViewModel.compareName);

            ControlViews();
            if (fileViewAdapter !=null){
                fileViewAdapter.notifyDataSetChanged();
            }
        }
    }

    public  void ControlViews(){
        if (!isOpen) {
            ShowMenu();
        } else {
            CloseFAB();
        }
    }

    private void CloseFAB(){


        sizedesc.animate().translationY(0);
        SizeAsc.animate().translationY(0);

        nameWiseDesc.animate().translationY(0);
        nameWiseAsc.animate().translationY(0);

        dateWiseDesc.animate().translationY(0);
        dateWiseAsc.animate().translationY(0);

        dateWiseAsc.animate().translationY(0).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (!isOpen) {
                    nameWiseAsc.setVisibility(View.GONE);
                    nameWiseDesc.setVisibility(View.GONE);
                    SizeAsc.setVisibility(View.GONE);
                    sizedesc.setVisibility(View.GONE);
                    dateWiseAsc.setVisibility(View.GONE);
                    dateWiseDesc.setVisibility(View.GONE);
                }

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        view.setVisibility(View.GONE);
        isOpen=false;
        centralFab.animate().rotation(0);
    }

    private void ShowMenu(){

        nameWiseAsc.setVisibility(View.VISIBLE);
        nameWiseDesc.setVisibility(View.VISIBLE);
        SizeAsc.setVisibility(View.VISIBLE);
        sizedesc.setVisibility(View.VISIBLE);
        dateWiseDesc.setVisibility(View.VISIBLE);
        dateWiseAsc.setVisibility(View.VISIBLE);



        centralFab.animate().rotation(180);

        nameWiseAsc.animate().translationY(-30);
        nameWiseDesc.animate().translationY(-50);
        dateWiseAsc.animate().translationY(-70);
        dateWiseDesc.animate().translationY(-90);
        SizeAsc.animate().translationY(-110);
        sizedesc.animate().translationY(-130);

        view.setVisibility(View.VISIBLE);
        isOpen=true;

    }

    private void checkFile(){
        if (checkwhetherPermission()){

            File[] dir =globalContext.getExternalFilesDirs(null);
            for(File file:dir){
                String path = file.getAbsolutePath();
                int index = path.indexOf("/Android/data/");
                String intenalStorage = path.substring(0,index);
                homePath.add(intenalStorage);
            }

            String [] pattern = {"pdf","ppt","doc","docx","xls","xlsx","txt","zip","rar","pptx"};
            loadHome(homePath,pattern,false);

        }else{
            CheckPermission();
        }
    }

    private void loadHome(ArrayList<String> path,String [] pattern,boolean Which){
        Visible(progressBar,true);

        documents.clear();

        if (fileViewAdapter !=null){
            fileViewAdapter.notifyDataSetChanged();
        }
        if (!path.isEmpty()){
            for (String homepaths:path){

                new Thread(() -> {
                    ArrayList<FileViewModel> presentDocument= FileWorker.searchDirectory(homepaths,pattern,Which,documents);
                    documents.addAll(presentDocument);
                    getActivity().runOnUiThread(() -> {

                        Visible(progressBar,false);
                        settingUpTheView(documents);
                    });

                }).start();


            }
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



    @OnClick({R.id.searchBody,R.id.search_button,R.id.backButton}) void Search(View view){
        int id = view.getId();
        if (id == R.id.searchBody){



        }else if (id == R.id.search_button){

            InputMethodManager inputMethodManager =(InputMethodManager)globalContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(mainLayout.getWindowToken(),0);
            filter(searchBody.getText().toString());
        }else if(id == R.id.backButton){
            InputMethodManager inputMethodManager =(InputMethodManager)globalContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(mainLayout.getWindowToken(),0);

            searchBody.setText("");
            filter(searchBody.getText());
            ShowViews(filpView);
        }

    }


    private synchronized void settingUpTheView(ArrayList<FileViewModel> documents){

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(globalContext);



        if (documents.isEmpty()){
            Visible(emptyFolder,true);
        }else{
            Visible(emptyFolder,false);
        }
        fileViewAdapter = new FileViewAdapter(documents,getActivity(),this, this);
        recyclerBottomSheet.setLayoutManager(layoutManager);
        recyclerBottomSheet.setHasFixedSize(true);
        recyclerBottomSheet.setHapticFeedbackEnabled(true);
        recyclerBottomSheet.setAdapter(fileViewAdapter);

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

    public void CheckPermission(){
        final PermissionListener permissionListener= new PermissionListener() {
            @Override
            public void onPermissionGranted(){
                checkFile();
            }
            @Override
            public void onPermissionDenied(final List<String> deniedPermissions){

                Toast.makeText(globalContext,"permission denied",Toast.LENGTH_SHORT).show();

            }
        };

        TedPermission.with(globalContext)
                .setPermissionListener(permissionListener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }
    //Check if permission Was Granted Using Ted Permission Library
    public boolean checkwhetherPermission(){
        boolean result = ContextCompat.checkSelfPermission(globalContext,Manifest.permission.READ_EXTERNAL_STORAGE) ==(PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(globalContext,Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private void initView() {
        spinnerModels.add(new SpinnerModel("All Documents",R.drawable.all_doc,"all"));
        spinnerModels.add(new SpinnerModel("PDF Files",R.drawable.pdf_file,"pdf"));
        spinnerModels.add(new SpinnerModel("MS WORD Files",R.drawable.word_file,"doc"));
        spinnerModels.add(new SpinnerModel("Text Files",R.drawable.text_file,"txt"));
        spinnerModels.add(new SpinnerModel("Ms Excel Files",R.drawable.excel_file,"xls"));
        spinnerModels.add(new SpinnerModel("Ms Power Point Files",R.drawable.ppt_file,"ppt"));
        spinnerModels.add(new SpinnerModel("Zip Files",R.drawable.zip_file,"zip"));
        spinnerModels.add(new SpinnerModel("Rar Files",R.drawable.rar_file,"rar"));


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

    //Flip the search View a main control View
    private void ShowViews(ViewFlipper filpView){

        if (filpView.getDisplayedChild()==1){

            searchBody.setText("");
            filter(searchBody.getText());

            filpView.setInAnimation(AnimationUtils.loadAnimation(globalContext,R.anim.in_from_left));
            filpView.setOutAnimation(AnimationUtils.loadAnimation(globalContext,R.anim.out_from_left));
            filpView.showNext();
        }else  if (filpView.getDisplayedChild()==0){
            filpView.setInAnimation(AnimationUtils.loadAnimation(globalContext,R.anim.in_from_right));
            filpView.setOutAnimation(AnimationUtils.loadAnimation(globalContext,R.anim.out_from_right));
            filpView.showPrevious();
        }
    }

    @Override
    public void Search() {
        InputMethodManager inputMethodManager =(InputMethodManager)globalContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(mainLayout.getWindowToken(),0);
        ShowViews(filpView);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (atomicBoolean.get()){
            atomicBoolean.set(false);
        }else{
            SpinnerModel values =(SpinnerModel)adapterView.getItemAtPosition(i);
            String [] pattern = null;
            if (values.getFileType().equals("all")){
                pattern = new String[]{"pdf", "ppt", "doc","docx","xls","xlsx", "txt", "zip", "rar","pptx"};
                itemSelected = "all";
            }else {
                itemSelected =values.getFileType();
                pattern = new String[]{values.getFileType()};
               for (String value:pattern){
                   if(value.equalsIgnoreCase("xls")){
                       pattern = new String[]{"xls","xlsx"};
                   }

                   if (value.equalsIgnoreCase("doc")){
                       pattern = new String[]{"doc","docx"};
                   }
                   if (value.equalsIgnoreCase("ppt")){
                       pattern = new String[]{"ppt","pptx"};
                   }
               }
            }

                   String[] finalPattern = pattern;
                   loadHome(homePath,finalPattern,false);


        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClickItem(int position, File file, String filePath) {

        RecentFile recentFile = new RecentFile(file.getAbsolutePath(),file.getAbsolutePath());
        AppExecutor.getInstance().getDiskIO().execute(() -> {
            database.Dao().insertFile(recentFile);
        });


        gotoReadears.gotToActivity(file.getAbsolutePath());
    }

    @Override
    public void onDestroy() {
        if (gotoReadears.CheckActivity()!=null){
            gotoReadears.Destroy();
        }
        if (gotoReadears!=null){
            gotoReadears=null;
        }

        if (faceBooKAds != null){
            faceBooKAds.DestroyAds();
        }


        super.onDestroy();
    }

    public void callHomeMethod(String [] pattern){
    loadHome(homePath,pattern,false);
    }

    @Override
    public void ChangeValue(File file) {
        OpenActionDialog openActionDialog = new OpenActionDialog();
        openActionDialog.setDialogValue(file,this,itemSelected);
        openActionDialog.show(getChildFragmentManager(),"fragment");
    }
    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);

        MenuItem search = menu.findItem(R.id.action_radio);

        if (search !=null){
            search.setVisible(true);
        }

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        callHome =(CallHome)context;
    }


}

