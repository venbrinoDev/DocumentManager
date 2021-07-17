package company.android.documentmanager.Fragments;


import android.Manifest;
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
import company.android.documentmanager.Executor.AppExecutor;
import company.android.documentmanager.HandleFile.FileWorker;
import company.android.documentmanager.Model.HomeDocument;
import company.android.documentmanager.R;
import company.android.documentmanager.RoomDatabase.Database;
import company.android.documentmanager.RoomDatabase.RecentFile;
import company.android.documentmanager.Utility.AppPrefrences;
import company.android.documentmanager.Utility.Constant;
import company.android.documentmanager.Utility.GotoReadears;
import company.android.documentmanager.inteface.CallHome;
import company.android.documentmanager.inteface.RecyclerViewClick;
import company.android.documentmanager.inteface.SearchInterface;


public class FolderViewFragment extends Fragment implements SearchInterface, RecyclerViewClick {

private GotoReadears gotoReadears;
    @BindView(R.id.RecyclerBottomSheet) RecyclerView recyclerBottomSheet;
    @BindView(R.id.gridView) ImageView gridView;
    @BindView(R.id.linearView)ImageView linearView;
    @BindView(R.id.backStack) Button backStack;
    @BindView(R.id.emptyFolder)LinearLayout emptyFolder;
    @BindView(R.id.headingName) TextView headingnName;
    @BindView(R.id.heading) ViewFlipper filpView;
    @BindView(R.id.searchBody) EditText searchBody;
    @BindView(R.id.search_button)Button searchButton;
    @BindView(R.id.backButton)Button searchBackButton;
    @BindView(R.id.mainLayout)
    CoordinatorLayout mainLayout;
    @BindView(R.id.progress)
    ProgressBar progressBar;
    @BindView(R.id.banner_container)LinearLayout bannerView;


    RecyclerViewAdapter recyclerViewAdapter;

    private Context globalContext;

    private Stack<String> path;

    AppPrefrences prefrences;

    ArrayList<HomeDocument> documents;

    ArrayList<String> homePath;

    private CallHome callHome;
    private Database database;
    boolean loaded = false;
    FaceBooKAds faceBooKAds;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View root = inflater.inflate(R.layout.fragment_file_manager, container, false);

        setHasOptionsMenu(true);

        requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if ( path==null||path.isEmpty()){
                    if (filpView.getDisplayedChild()==1){
                        ShowViews(filpView);
                    }else {
                        callHome.CallHome();
                    }

                }else{
//                    String  pop =path.pop();
//                    File file = new File(pop);
//                    String filePath=file.getParent();

                    loaded = false;
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

        database = Database.getDatabaseInstance(getContext());
        gotoReadears = new GotoReadears(getActivity());


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

       init();
       EditTextSearch();
       checkFile();


    }
    //init views
    private void init(){
        globalContext = requireContext();
        if (globalContext==null){
            globalContext = getContext();
        }

        prefrences = new AppPrefrences(globalContext, Constant.SHARED_PREF_USED_FIRST);
        homePath = new ArrayList<>();

        documents = new ArrayList<>();


        Visible(backStack,false);
        Visible(emptyFolder,false);
        Visible(progressBar,false);
        path = new Stack<>();

    }

    //Check for Internal and External Storage Device
    private void checkFile(){
        if (!homePath.isEmpty()){
            homePath.clear();
        }
        if (checkwhetherPermission()){
            File[] dir =globalContext.getExternalFilesDirs(null);
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

    private void loadHome(ArrayList<String> path,String [] pattern,String Which){
        Visible(progressBar,true);
        documents.clear();
        if (recyclerViewAdapter !=null){
            recyclerViewAdapter.notifyDataSetChanged();
        }
        if (!path.isEmpty()){
            for (String homepaths:path){

                new Thread(() -> {
                   documents= loadArrayList(homepaths,Which,pattern,documents);

               getActivity().runOnUiThread(() -> {
                 Visible(progressBar,false);
                SetUpViews(documents,Constant.getAllfile(),new File(homepaths));
               });

                }).start();


            }
        }
    }


      private ArrayList<HomeDocument> loadArrayList(String dir,String which,String [] pattern,ArrayList<HomeDocument> documents){
    ArrayList<HomeDocument> fileDocuments = new ArrayList<>();
        if (checkwhetherPermission()){
            if (loaded){
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

    //Set Home the recycler View
    private void settingUpTheView(ArrayList<HomeDocument> documents){
        RecyclerView.LayoutManager layoutManager = null;
        int layout_Constant = Constant.LINEAR_LAYOUT;
        int layout = prefrences.getInt(Constant.LAYOUT_VIEW_FOLDERVIEW,Constant.LINEAR_LAYOUT);
        if (layout==Constant.LINEAR_LAYOUT){

            layoutManager =new LinearLayoutManager(globalContext);
            layout_Constant = Constant.LINEAR_LAYOUT;
        }else{

            layoutManager =new GridLayoutManager(globalContext,3);
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


    //Show Permission Granting Process
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


    //Main control Click Event
    @OnClick({R.id.linearView,R.id.gridView,R.id.backStack})  void  onClick(View view){
        int id = view.getId();
        if (id==R.id.linearView){
            prefrences.putInt(Constant.LAYOUT_VIEW_FOLDERVIEW,Constant.LINEAR_LAYOUT);

            settingUpTheView(documents);

        }else if (id ==R.id.gridView){
            prefrences.putInt(Constant.LAYOUT_VIEW_FOLDERVIEW,Constant.GRID_LAYOUT);

            settingUpTheView(documents);
        }else if(id==R.id.backStack){
            if (!path.isEmpty()){
                String  pop =path.pop();
                File file = new File(pop);
                String filePath=file.getParent();

                loaded = false;
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
//
//                }

            }else {
//                path.clear();
//                loadHome(homePath,Constant.getAllfile(),Constant.HOME);
                loaded = false;
                checkFile();
            }
        }

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



    private void ChangeView(){
        if (filpView.getDisplayedChild()==1){

            searchBody.setText("");
            filter(searchBody.getText());

            filpView.setInAnimation(AnimationUtils.loadAnimation(globalContext,R.anim.in_from_left));
            filpView.setOutAnimation(AnimationUtils.loadAnimation(globalContext,R.anim.out_from_left));
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
            loaded = true;
            loadHome(path,null,Constant.Others);
        }else{

            String fileType = filePath.substring(filePath.length()-3);

            RecentFile recentFile = new RecentFile(file.getAbsolutePath(),file.getAbsolutePath());

            AppExecutor.getInstance().getDiskIO().execute(() -> {
                database.Dao().insertFile(recentFile);
            });

            gotoReadears.gotToActivity(file.getAbsolutePath());
        }

    }
    @Override
    public void Search() {
        InputMethodManager inputMethodManager =(InputMethodManager)globalContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(mainLayout.getWindowToken(),0);
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
        callHome=(CallHome)context;
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
