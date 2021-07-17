package company.android.documentmanager.DialogFragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ShareCompat;
import androidx.fragment.app.DialogFragment;

import java.io.File;
import java.net.URLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import company.android.documentmanager.Executor.AppExecutor;
import company.android.documentmanager.FileReaders.FilOperationns;
import company.android.documentmanager.Fragments.FileViewFragment;
import company.android.documentmanager.inteface.NotifyFragment;
import company.android.documentmanager.R;
import company.android.documentmanager.RoomDatabase.Database;
import company.android.documentmanager.RoomDatabase.RecentFile;
import company.android.documentmanager.Utility.CalculateClass;
import company.android.documentmanager.Utility.Constant;
import company.android.documentmanager.Utility.GotoReadears;

public class OpenActionDialog  extends DialogFragment {
    private GotoReadears gotoReadears;

    File file;
    FileViewFragment fileViewFragment;
    String itemSelected;

    public OpenActionDialog(){

    }

    public void setDialogValue(File file, FileViewFragment fragment,String itemSelected){
        this.file =file;
        fileViewFragment = fragment;
        this.itemSelected = itemSelected;
    }

    @BindView(R.id.folderImage)
    ImageView folderImage;

    @BindView(R.id.name)
    TextView Filename;

    @BindView(R.id.cancel_action)ImageView cancleAction;
    @BindView(R.id.read) RelativeLayout read;
    @BindView(R.id.delete) RelativeLayout delete;
    @BindView(R.id.rename) RelativeLayout rename;
    @BindView(R.id.share) RelativeLayout share;
    @BindView(R.id.infoView) RelativeLayout info;


    private Context globalContext;
    private NotifyFragment notifyFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.action_dialog,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);

gotoReadears = new GotoReadears(getActivity());

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        globalContext = requireContext();
        if (globalContext==null){
            globalContext=getContext();
        }


        Window window = getDialog().getWindow();

        if (window !=null){
            window.setBackgroundDrawableResource(android.R.color.transparent);
        }

        setCancelable(true);

        Filename.setText(file.getName());

        setImage(folderImage,file.getName());
    }

    @OnClick(R.id.cancel_action) void headersOnClick(){
        getDialog().cancel();
    }


    @OnClick({R.id.read,R.id.delete,R.id.rename,R.id.share,R.id.infoView}) void ChildrenClicked(View view){

        int id = view.getId();
        if (id == R.id.read){
            Database database = Database.getDatabaseInstance(getContext());
            RecentFile recentFile = new RecentFile(file.getAbsolutePath(),file.getAbsolutePath());

            AppExecutor.getInstance().getDiskIO().execute(() -> {
                database.Dao().insertFile(recentFile);
            });

            gotoReadears.gotToActivity(file.getAbsolutePath());
            return;
        }
        if (id==R.id.delete){
            DeleteDialog();
            return;
        }
        if (id==R.id.rename){
            renameDialog();
            return;
        }
        if (id==R.id.share){
            share();
            return;
        }
        if (id ==R.id.infoView){
            infoAlertDialog();

return;
        }
    }

    private void renameDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(globalContext);
        View view = View.inflate(globalContext,R.layout.rename,null);
        TextView name = view.findViewById(R.id.name);
        ImageView imageView = view.findViewById(R.id.folderImage);
        ImageView cancelIcon = view.findViewById(R.id.cancleIcon);
        Button cancelButton = view.findViewById(R.id.cancleButton);
        Button renameButton = view.findViewById(R.id.renameButton);
        EditText searchView = view.findViewById(R.id.searchBody);

        name.setText(file.getName());
        setImage(imageView,file.getName());

        searchView.setHint(file.getName());

        builder.setView(view);
        builder.setCancelable(true);



        AlertDialog dialog = builder.create();
        dialog.show();


        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        cancelButton.setOnClickListener(view1 -> dialog.cancel());
        cancelIcon.setOnClickListener(view1 -> dialog.cancel());
        renameButton.setOnClickListener(view1 -> {
            String renameValue = searchView.getText().toString().trim();
            if (renameValue.isEmpty()){
                searchView.setError("Must not be empty");
                searchView.requestFocus();
                return;
            }

            String extension ="."+ FilOperationns.fileExt(file.getName());

            processValue(renameValue,extension,dialog);


        });

    }

    private String removeExtension(String name){

        return name.substring(name.length()-4);
    }

    private void processValue(String value,String extension,AlertDialog dialog){

    File from = new File(file.getAbsolutePath());

    File to = new File(file.getParent(),value+extension);

    if (!to.exists()){

        boolean result = from.renameTo(to);


        if (result){
            successDialog(dialog,"This file has been Renamed successfully",R.drawable.ic_done,"Renamed");
        }else{
            successDialog(dialog,"error Renaming this file please try again",R.drawable.ic_baseline_error_24,"Error");
        }
    }else{
        Toast.makeText(getContext(), "Another file already use this name", Toast.LENGTH_LONG).show();
    }

    }

    private void share(){

String mimeType = CalculateClass.getMimeType(file.getName());
        Uri uri=Uri.parse(file.getPath());
//        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
//
//        sharingIntent.setType(mimeType);
//        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "APP NAME (Document Manager)");
//        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
//        startActivity(Intent.createChooser(sharingIntent, "Share file"));

        ShareCompat.IntentBuilder.from(getActivity()).setStream(uri).setType(URLConnection.guessContentTypeFromName(file.getName())).startChooser();

    }

    private void DeleteDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(globalContext);
        View view = View.inflate(globalContext,R.layout.delete,null);

        TextView name = view.findViewById(R.id.name);
        ImageView imageView = view.findViewById(R.id.folderImage);
        ImageView cancelIcon = view.findViewById(R.id.cancleIcon);
        Button cancelButton = view.findViewById(R.id.cancleButton);
        Button confirmButton = view.findViewById(R.id.confirmDelete);

        setImage(imageView,file.getName());

        name.setText(file.getName());

        builder.setView(view);
        builder.setCancelable(true);


        AlertDialog dialog = builder.create();
        dialog.show();

        cancelButton.setOnClickListener(view1 -> dialog.cancel());
        cancelIcon.setOnClickListener(view1 -> dialog.cancel());

        confirmButton.setOnClickListener(view12 -> {

            boolean deleted =file.delete();
            if (deleted){
                successDialog(dialog,"This file has been deleted successfully",R.drawable.ic_done,"Deleted");
            }else{
                successDialog(dialog,"error deleting this file please try again",R.drawable.ic_baseline_error_24,"Error");
            }

        });
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    private void successDialog(AlertDialog dialog,String message,int  diplayImage,String name){
        dialog.cancel();

        AlertDialog.Builder builder = new AlertDialog.Builder(globalContext);
        View view = View.inflate(globalContext,R.layout.done,null);

        TextView nameTop = view.findViewById(R.id.name);
        TextView messsage = view.findViewById(R.id.message);
        ImageView imageView = view.findViewById(R.id.folderImage);
        ImageView cancleIcon = view.findViewById(R.id.cancleIcon);
        Button ok = view.findViewById(R.id.okButtton);

        imageView.setImageResource(diplayImage);

        messsage.setText(message);
        nameTop.setText(name);
        builder.setView(view);
        builder.setCancelable(false);

        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        cancleIcon.setOnClickListener(view1 -> {

           if (itemSelected.equalsIgnoreCase("all")){
               fileViewFragment.callHomeMethod(Constant.getAllfile());
           }else {
               String [] extend;
               String ext = itemSelected;
               if (ext.equalsIgnoreCase("xls")){
                   extend = new String[]{"xls","xlsx"};
               }else if(ext.equalsIgnoreCase("doc")) {
                   extend = new String[]{"doc","docx"};

               }else if (ext.equalsIgnoreCase("ppt")){
                    extend = new String[]{"ppt", "pptx"};

               } else{
                   extend = new String[]{ext};
               }
               fileViewFragment.callHomeMethod(extend);

               alertDialog.dismiss();
               getDialog().dismiss();
           }


                }

        );

        ok.setOnClickListener(view12 -> {

           alertDialog.dismiss();
           getDialog().dismiss();
            if (itemSelected.equalsIgnoreCase("all")){
                fileViewFragment.callHomeMethod(Constant.getAllfile());
            }else {
                String [] extend;
                String ext = itemSelected;
                if (ext.equalsIgnoreCase("xls")){
                    extend = new String[]{"xls","xlsx"};
                }else if(ext.equalsIgnoreCase("doc")) {
                    extend = new String[]{"doc", "docx"};
                }else if (ext.equalsIgnoreCase("ppt")){
                    extend = new String[]{"ppt", "pptx"};
                } else{
                    extend = new String[]{ext};
                }
                fileViewFragment.callHomeMethod(extend);

            }

        });
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }


    private void infoAlertDialog(){


        AlertDialog.Builder builder = new AlertDialog.Builder(globalContext);
        View view = View.inflate(globalContext,R.layout.info,null);


        TextView name = view.findViewById(R.id.name);
        TextView fullName = view.findViewById(R.id.fullNmae);
        TextView fileType = view.findViewById(R.id.fileType);
        TextView fileSize = view.findViewById(R.id.fileSize);
        TextView filePath = view.findViewById(R.id.filePath);
        TextView modifiedDate = view.findViewById(R.id.modifiedFile);
        ImageView imageFileViw = view.findViewById(R.id.folderImage);
        ImageView cancleImage = view.findViewById(R.id.cancleIcon);
        Button cancleButton = view.findViewById(R.id.cancleButton);

        String date = CalculateClass.getDateOuOfTimeStanp(file.lastModified());
        modifiedDate.setText(date);

        setImage(imageFileViw,file.getName());
        name.setText(file.getName());
        filePath.setText(file.getPath());

        String fileTypeValue = TypeValue(file.getName());

        fileType.setText(fileTypeValue);
        fullName.setText(file.getName());

        setImage(imageFileViw,file.getName());

        float currentSize = CalculateClass.calculateSize(file.length());
        String size = "kb";

        if (currentSize<=1){
            currentSize =file.length();
            size = "byte";
        }else if (currentSize >= 1024){
            currentSize = CalculateClass.calculateSize(currentSize);
            size = "mb";
        }

        fileSize.setText(currentSize +" "+size);

        builder.setView(view);
        builder.setCancelable(true);


        AlertDialog dialog = builder.create();
        dialog.show();


        cancleImage.setOnClickListener(view1 -> dialog.cancel());
        cancleButton.setOnClickListener(view1 -> dialog.cancel());

        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    }

    private String TypeValue(String name){

        String lastThree = FilOperationns.fileExt(name);
        switch (lastThree) {
            case "ppt":
            case "pptx":
                return "PowerPoint File";
            case "pdf":
                return "pdf file";
            case "txt":
                return "text file";
            case "xlsx":
            case "xls":
                return "Excel file";
            case "doc":
            case "docx":
                return "Word document file";
            case "zip":
                return "zip file";
            case "rar":
                return "rar File";

            default:
                return "File";
        }
    }

    private void setImage(ImageView view, String name) {
        String lastThree = FilOperationns.fileExt(name);



        switch (lastThree) {
            case "ppt":
            case "pptx":
                view.setImageResource(R.drawable.ppt_file);
                break;
            case "pdf":
                view.setImageResource(R.drawable.pdf_file);
                break;
            case "txt":
                view.setImageResource(R.drawable.text_file);
                break;
            case "xlsx":
            case "xls":
                view.setImageResource(R.drawable.excel_file);
                break;
            case "doc":
            case "docx":
                view.setImageResource(R.drawable.word_file);
                break;
            case "zip":
                view.setImageResource(R.drawable.zip_file);
                break;
            case "rar":
                view.setImageResource(R.drawable.rar_file);
                break;

            default:
                view.setImageResource(R.drawable.all_doc);
                break;
        }
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        fileViewFragment=null;
    }
}
