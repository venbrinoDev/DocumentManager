package company.android.documentmanager.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import company.android.documentmanager.FileReaders.FilOperationns;
import company.android.documentmanager.Model.FileViewModel;
import company.android.documentmanager.inteface.NotifyFragment;
import company.android.documentmanager.R;
import company.android.documentmanager.inteface.RecyclerViewClick;
import company.android.documentmanager.Utility.AppPrefrences;
import company.android.documentmanager.Utility.CalculateClass;
import company.android.documentmanager.Utility.Constant;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.ViewHolder>{

    private ArrayList<FileViewModel> documents;
    private Context globalContext;
    private RecyclerViewClick recyclerViewClick;
    AppPrefrences appPrefrences;
    private NotifyFragment notifyFragment;

    public BookmarkAdapter(ArrayList<FileViewModel> documents, Context globalContext, RecyclerViewClick recyclerViewClick, NotifyFragment notifyFragment) {
        this.documents = documents;
        this.globalContext = globalContext;
        this.recyclerViewClick=recyclerViewClick;
        this.notifyFragment =notifyFragment;
        appPrefrences = new AppPrefrences(globalContext,Constant.SHARED_PREF_USED_FIRST);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(globalContext).inflate(R.layout.bookmark_row,parent,false);
        return  new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        FileViewModel model = documents.get(position);


        TextView name = holder.name;
        TextView numberOfFile = holder.numbeer;
        ImageView display = holder.showFileImage;
        TextView path = holder.fullPath;
        setImage(display,model.getName());

        name.setText(model.getName());


       float fileSize = CalculateClass.calculateSize(model.getSize());

String size = "kb";

        if (fileSize<=1){
            fileSize = model.getFile().length();
            size = "byte";
        }else if (fileSize >= 1024){
       fileSize = CalculateClass.calculateSize(fileSize);
            size = "mb";
        }

        numberOfFile.setText(fileSize+" "+size);

        boolean show=appPrefrences.getBoolean(Constant.DISPLAY_FULL_PATH_KEY,false);
        if (show){
            path.setText(model.getFile().getPath());
        }else{
            path.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(view -> {
            recyclerViewClick.onClickItem(position,model.getFile(),model.getFile().getAbsolutePath());
        });


        holder.showButtonClick.setOnClickListener(view -> {

            if (notifyFragment!=null){
                notifyFragment.ChangeValue(model.getFile());
            }


        });


    }

    private void setImage(ImageView view, String name){

//        String lastThree ="fil";
//        if (name.length()>3){
//            lastThree = name.substring(name.length()-3);
//        }

        String lastThree = FilOperationns.fileExt(name);

        Log.d("lastThree" ,"setImage: "+lastThree);

        switch (lastThree){
            case "ppt" :
            case "pptx":
                view.setImageResource(R.drawable.ppt_file);
                break;
            case "pdf" :
                view.setImageResource(R.drawable.pdf_file);
                break;
            case "txt" :
                view.setImageResource(R.drawable.text_file);
                break;
            case "xlsx":
            case "xls" :
                view.setImageResource(R.drawable.excel_file);
                break;
            case "docx":
            case "doc" :
                view.setImageResource(R.drawable.word_file);
                break;
            case "zip" :
                view.setImageResource(R.drawable.zip_file);
                break;
            case "rar" :
                view.setImageResource(R.drawable.rar_file);
                break;

            default:
                view.setImageResource(R.drawable.all_doc);
                break;
        }

    }


    @Override
    public int getItemCount() {
        return documents.size();
    }

    public  void setDocuments(ArrayList<FileViewModel> documents){
        this.documents=documents;
        notifyDataSetChanged();
    }


    public ArrayList<FileViewModel> getDocuments(){
        return documents;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView showFileImage;
        Button showButtonClick;

        TextView name, numbeer,fullPath;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            showFileImage=itemView.findViewById(R.id.folderImage);
            name = itemView.findViewById(R.id.name);
            numbeer = itemView.findViewById(R.id.numbersOfFile);
            fullPath = itemView.findViewById(R.id.displayFullPath);
            showButtonClick = itemView.findViewById(R.id.action);



        }

    }
}
