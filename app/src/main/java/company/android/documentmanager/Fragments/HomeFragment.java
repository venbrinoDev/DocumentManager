package company.android.documentmanager.Fragments;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import company.android.documentmanager.Adapter.RecyclerViewAdapter;
import company.android.documentmanager.Ads.FaceBooKAds;
import company.android.documentmanager.inteface.CallHome;
import company.android.documentmanager.Executor.AppExecutor;
import company.android.documentmanager.HandleFile.FileWorker;
import company.android.documentmanager.Model.HomeDocument;
import company.android.documentmanager.R;
import company.android.documentmanager.inteface.RecyclerViewClick;
import company.android.documentmanager.RoomDatabase.Database;
import company.android.documentmanager.RoomDatabase.RecentFile;
import company.android.documentmanager.inteface.SearchInterface;
import company.android.documentmanager.Utility.AppPrefrences;
import company.android.documentmanager.Utility.Constant;
import company.android.documentmanager.Utility.GotoReadears;

public class HomeFragment extends Fragment  implements RecyclerViewClick, SearchInterface {
@BindView(R.id.docs) ImageView docsFile;
@BindView(R.id.pdf)ImageView pfdFile;
@BindView(R.id.xls)ImageView xlsFile;
@BindView(R.id.txt)ImageView txtFile;
@BindView(R.id.ppT)ImageView ppTFile;
@BindView(R.id.others)ImageView others;
@BindView(R.id.bottom_sheet)LinearLayout bottomSheetLayout;
@BindView(R.id.headLayout)LinearLayout headLayout;
@BindView(R.id.RecyclerBottomSheet)RecyclerView recyclerBottomSheet;
@BindView(R.id.gridView)ImageView gridView;
@BindView(R.id.linearView)ImageView linearView;
@BindView(R.id.backStack)Button backStack;
@BindView(R.id.emptyFolder)LinearLayout emptyFolder;
@BindView(R.id.headingName) TextView headingnName;
@BindView(R.id.heading) ViewFlipper filpView;
@BindView(R.id.searchBody) EditText searchBody;
@BindView(R.id.search_button)Button searchButton;
@BindView(R.id.backButton)Button searchBackButton;
@BindView(R.id.mainLayout) CoordinatorLayout mainLayout;
@BindView(R.id.progress) ProgressBar progressBar;
    @BindView(R.id.banner_container)LinearLayout bannerView;


BottomSheetBehavior<View> sheetBehavior;
RecyclerViewAdapter recyclerViewAdapter;

private Stack<String>  path;

AppPrefrences prefrences;

ArrayList<HomeDocument> documents;

ArrayList<String> homePath;

Activity activity;
private CallHome callHome;
private Boolean pressed;
private Database database;
private GotoReadears gotoReadears;
private  boolean LOAD_FILE = false;

FaceBooKAds faceBooKAds;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         View root = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);

        requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if ( path==null||path.isEmpty()){
                    if (pressed){
                        pressed=false;
                        loadHome(homePath,Constant.getAllfile(),Constant.HOME);
                    }else {
                        if (filpView.getDisplayedChild()==1){
                            ShowViews(filpView);
                        }else {
                            if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
                                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                            }else {
                                callHome.exits();
                            }

                        }

                    }

                }else{
                    String  pop =path.pop();
                    File file = new File(pop);
                    String filePath=file.getParent();

                    LOAD_FILE = false;
                    path.clear();
                    checkFile();
//                    if (isHomeFile(filePath)){
//                        path.clear();
//                        loadHome(homePath,Constant.getAllfile(),Constant.HOME);
//
//                    }else {
//                        ArrayList<String> path = new ArrayList<>();
//                        path.add(file.getParent());
//                        loadHome(path,null,Constant.Others);
//                    }
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


        bottomSheetLayout.setVisibility(View.INVISIBLE);

        sheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);

        prefrences = new AppPrefrences(getContext(),Constant.SHARED_PREF_USED_FIRST);
        homePath = new ArrayList<>();

        documents = new ArrayList<>();
        gotoReadears = new GotoReadears(getActivity());
        database = Database.getDatabaseInstance(getContext());

       pressed=false;
        Visible(backStack,false);
        Visible(emptyFolder,false);
        Visible(progressBar,false);

        path = new Stack<>();

        EditTextSearch();

        checkFile();
        ListenToTocuch();



        headLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                mainLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        sheetBehavior.setPeekHeight((mainLayout.getMeasuredHeight()-headLayout.getMeasuredHeight())-30);
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                bottomSheetLayout.setVisibility(View.VISIBLE);
                            }
                        },20);


                        mainLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this::onGlobalLayout);
                    }
                });

                headLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this::onGlobalLayout);
            }
        });



    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

   private boolean isHomeFile(String path){
       for (String home:homePath){

           if (path.equals(home)){

               return true;
           }
       }

        return false;
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

    private void ListenToTocuch(){
        searchBody.setOnTouchListener((view, motionEvent) -> {

            if (motionEvent.getAction()==MotionEvent.ACTION_DOWN){

                SwitchBottomState(sheetBehavior,true);
            }

            return false;
        });
    }






    //Check for Internal and External Storage Device
    private void checkFile(){

        if (checkwhetherPermission()){
            if (!homePath.isEmpty()){
                homePath.clear();
            }
            File [] dir =getContext().getExternalFilesDirs(null);
            for(File file:dir){
                String path = file.getAbsolutePath();
                int index = path.indexOf("/Android/data/");
                String intenalStorage = path.substring(0,index);
                homePath.add(intenalStorage);

            }

            loadHome(homePath,Constant.getAllfile(),Constant.HOME);

        }else{
            CheckPermission();
        }

    }


    //This Central is the Central Loading Method
    private void loadHome(ArrayList<String> path,String [] pattern,String Which){
        Visible(progressBar,true);
        documents.clear();
        if (recyclerViewAdapter !=null){
            recyclerViewAdapter.notifyDataSetChanged();
        }

        if (!path.isEmpty()){
            for (String homepaths:path){

                new Thread(() -> {
                    ArrayList<HomeDocument> presentDocument= loadArrayList(homepaths,Which,pattern,documents);
                    documents=presentDocument;
                    getActivity().runOnUiThread(() -> {

                        Visible(progressBar,false);
                        SetUpViews(presentDocument,Constant.getAllfile(),new File(homepaths));
                    });

                }).start();


            }
        }
    }

    private ArrayList<HomeDocument> loadArrayList(String dir,String which,String [] pattern,ArrayList<HomeDocument> documents){
        ArrayList<HomeDocument> fileDocuments = new ArrayList<>();
        if (checkwhetherPermission()){
            if (LOAD_FILE){
                fileDocuments = FileWorker.folderFiles(dir,which,pattern,documents);
            }else{

                fileDocuments = FileWorker.collectAllFile(dir,which,pattern,documents);
            }

        }else{
            CheckPermission();
        }
        return fileDocuments;
    }

    private synchronized void SetUpViews(ArrayList<HomeDocument>  documents,String [] pattern,File root){
        if (path.isEmpty()){
            if (pattern.length<2){
                headingnName.setText("All" +pattern[0]);
                Visible(backStack,true);
            }else{
                headingnName.setText("All Categories");
                Visible(backStack,false);
            }

        }else{
            headingnName.setText(root.getName());
            Visible(backStack,true);
        }
        settingUpTheView(documents);
    }


    //Main control Click Event
    @OnClick({R.id.linearView,R.id.gridView,R.id.backStack})  void  onClick(View view){
        int id = view.getId();
        if (id==R.id.linearView){
            prefrences.putInt(Constant.LAYOUT_VIEW,Constant.LINEAR_LAYOUT);

            settingUpTheView(documents);

        }else if (id ==R.id.gridView){
            prefrences.putInt(Constant.LAYOUT_VIEW,Constant.GRID_LAYOUT);

            settingUpTheView(documents);
        }else if(id==R.id.backStack){
            if (!path.isEmpty()){
                String  pop =path.pop();
                File file = new File(pop);
                String filePath=file.getParent();
                LOAD_FILE = false;
                path.clear();
                checkFile();

//                if (isHomeFile(filePath)){
//                    path.clear();
//                    loadHome(homePath,Constant.getAllfile(),Constant.HOME);
//
//                }else {
//                    ArrayList<String> path = new ArrayList<>();
//                    path.add(file.getParent());
//                    loadHome(path,null,Constant.Others);
//                }

            }else {
                LOAD_FILE = false;
                path.clear();
                checkFile();
//                path.clear();
//                loadHome(homePath,Constant.getAllfile(),Constant.HOME);
            }
        }

    }

    //Main Board Click Event
  @OnClick({R.id.ppT,R.id.xls,R.id.docs,R.id.pdf,R.id.txt,R.id.others})
    void FileType(View view  ){
  int id = view.getId();

if (id ==R.id.pdf){
pressed=true;
          SingleFileSearch(new String[]{"pdf"});
          return;
  }
if (id ==R.id.txt){
    pressed=true;
    SingleFileSearch(new String[]{"txt"});
    return;
  }
if (id==R.id.xls){
    pressed=true;
    SingleFileSearch(new String[]{"xls","xlsx"});
    return;
  }
if (id==R.id.docs){
    pressed=true;
    SingleFileSearch(new String[]{"doc","docx"});
    return;
  }
if (id ==R.id.ppT){
    pressed=true;
    SingleFileSearch(new String[]{"ppt","pptx"});
    return;
  }
if (id ==R.id.others){
    pressed=false;
    path.clear();
    loadHome(homePath,Constant.getAllfile(),Constant.HOME);
}

    }

    //Search Board Click Event
    @OnClick({R.id.searchBody,R.id.search_button,R.id.backButton}) void Search(View view){
        int id = view.getId();
        if (id == R.id.searchBody){



        }else if (id == R.id.search_button){

            InputMethodManager inputMethodManager =(InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(mainLayout.getWindowToken(),0);
            filter(searchBody.getText().toString());
        }else if(id == R.id.backButton){
            InputMethodManager inputMethodManager =(InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(mainLayout.getWindowToken(),0);

            searchBody.setText("");
            filter(searchBody.getText());
            ShowViews(filpView);
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
            ArrayList<HomeDocument> temp = new ArrayList<>();

            for (HomeDocument homeDocument:documents){

              if (homeDocument.getFileText().trim().toLowerCase().contains(charSequence.toString().trim().toLowerCase())){
                  temp.add(new HomeDocument(R.drawable.folder,homeDocument.getFileText(),String.valueOf(homeDocument.getFileNumber()),homeDocument.getFilePath(),homeDocument.getFile()));
              }
            }
            settingUpTheView(temp);

        }else{
            settingUpTheView(documents);
        }
    }



    //Search for a file With Single Argument Example
    private void SingleFileSearch(String [] pattern){


        path.clear();
        Visible(progressBar,true);
        documents.clear();
        if (recyclerViewAdapter !=null){
            recyclerViewAdapter.notifyDataSetChanged();
        }

        for (String path:homePath){
            new Thread(new Runnable() {
                @Override
                public void run() {
                   ArrayList<HomeDocument> presentDocument = FileWorker.searchAllFile(path,pattern,documents);
                   documents = presentDocument;

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                              Visible(progressBar,false);
                            SetUpViews(presentDocument,pattern,new File(path));
                        }
                    });
                }
            }).start();

        }

    }



    //Set Home the recycler View
    private void settingUpTheView(ArrayList<HomeDocument> documents){
        RecyclerView.LayoutManager layoutManager = null;
        int layout_Constant = Constant.LINEAR_LAYOUT;
        int layout = prefrences.getInt(Constant.LAYOUT_VIEW,Constant.LINEAR_LAYOUT);
        if (layout==Constant.LINEAR_LAYOUT){

            layoutManager =new LinearLayoutManager(getContext());
            layout_Constant = Constant.LINEAR_LAYOUT;
        }else{

            layoutManager =new GridLayoutManager(getContext(),3);
            layout_Constant = Constant.GRID_LAYOUT;
        }


        if (documents.isEmpty()){
            Visible(emptyFolder,true);
        }else{
            Visible(emptyFolder,false);
        }
        recyclerViewAdapter = new RecyclerViewAdapter(documents,getContext(),layout_Constant,this);
        recyclerBottomSheet.setLayoutManager(layoutManager);
        recyclerBottomSheet.setHasFixedSize(true);
        recyclerBottomSheet.setHapticFeedbackEnabled(true);
        recyclerBottomSheet.setAdapter(recyclerViewAdapter);

    }



    //Show Permission Granting Process
    public void CheckPermission(){
        final PermissionListener permissionListener= new PermissionListener() {
            @Override
            public void onPermissionGranted(){
              checkFile();
            }
            @Override
            public void onPermissionDenied(final List<String> deniedPermissions){

                Toast.makeText(getContext(),"permission denied",Toast.LENGTH_SHORT).show();

            }
        };

        TedPermission.with(getContext())
                .setPermissionListener(permissionListener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }


    //Check if permission Was Granted Using Ted Permission Library
    public boolean checkwhetherPermission(){
        boolean result = ContextCompat.checkSelfPermission(getContext(),Manifest.permission.READ_EXTERNAL_STORAGE) ==(PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(getContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }


    //Switch  BottomSheetView to either Up or down Depending on expanding Value
    private  void SwitchBottomState(BottomSheetBehavior<View> sheetBehavior,Boolean expanand){

        if (expanand){
            if (sheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        }else {

            if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        }

    }


    //Switch ButtomSheetView to opposite direction
    private  void SwitchBottomState(BottomSheetBehavior<View> sheetBehavior){


            if (sheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        }else {


                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        }

    }

    @Override
    public void onResume() {
        super.onResume();

    }


    private void ChangeView(){
        if (filpView.getDisplayedChild()==1){

            searchBody.setText("");
            filter(searchBody.getText());

            filpView.setInAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.in_from_left));
            filpView.setOutAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.out_from_left));
            filpView.showNext();
        }
    }
    //Interface that handles recycler item click
    @Override
    public void onClickItem(int position, File file,String filePath) {
        ChangeView();

        if (file.isDirectory()){
            path.push(file.getAbsolutePath());
            ArrayList<String> path = new ArrayList<>();
            path.add(filePath);
            LOAD_FILE=true;
            loadHome(path,null,Constant.Others);
        }else{
            RecentFile recentFile = new RecentFile(file.getAbsolutePath(),file.getAbsolutePath());
            AppExecutor.getInstance().getDiskIO().execute(() -> {
                database.Dao().insertFile(recentFile);
            });
            gotoReadears.gotToActivity(file.getAbsolutePath());
        }

    }

    //Flip the search View a main control View
    private void ShowViews(ViewFlipper filpView){

        if (filpView.getDisplayedChild()==1){

            searchBody.setText("");
            filter(searchBody.getText());

            filpView.setInAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.in_from_left));
            filpView.setOutAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.out_from_left));
            filpView.showNext();
        }else  if (filpView.getDisplayedChild()==0){
            filpView.setInAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.in_from_right));
            filpView.setOutAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.out_from_right));
            filpView.showPrevious();
        }
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

    //Interface that Handle Search Clicked
    @Override
    public void Search() {
        InputMethodManager inputMethodManager =(InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(mainLayout.getWindowToken(),0);
SwitchBottomState(sheetBehavior,true);
ShowViews(filpView);

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
        callHome = (CallHome)context;
    }
}
