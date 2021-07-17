package company.android.documentmanager.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

import company.android.documentmanager.FileReaders.FilOperationns;
import company.android.documentmanager.Model.HomeDocument;
import company.android.documentmanager.R;
import company.android.documentmanager.inteface.RecyclerViewClick;
import company.android.documentmanager.Utility.AppPrefrences;
import company.android.documentmanager.Utility.Constant;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

private ArrayList<HomeDocument> documents;
private Context globalContext;
public int layout;


private RecyclerViewClick recyclerViewClick;
AppPrefrences appPrefrences;


    public RecyclerViewAdapter(ArrayList<HomeDocument> documents, Context globalContext,int layout,RecyclerViewClick recyclerViewClick) {
        this.documents = documents;
        this.globalContext = globalContext;
        this.layout= layout;
        this.recyclerViewClick=recyclerViewClick;
        appPrefrences = new AppPrefrences(globalContext, Constant.SHARED_PREF_USED_FIRST);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType ==Constant.LINEAR_LAYOUT){
            View view = LayoutInflater.from(globalContext).inflate(R.layout.row_linear,parent,false);
            return new ViewHolder(view);
        }else{
            View  secondView = LayoutInflater.from(globalContext).inflate(R.layout.row_gridlayout,parent,false);
            return new ViewHolder(secondView);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {



        String name=documents.get(position).getFileText();
        int image = documents.get(position).getFileImageView();
        String numberOffile = documents.get(position).getFileNumber();
        String filePath = documents.get(position).getFilePath();
        File file = documents.get(position).getFile();
        boolean show=appPrefrences.getBoolean(Constant.DISPLAY_FULL_PATH_KEY,false);



        TextView fileSize = holder.numbeer;
        ImageView doocumentImage = holder.showFileImage;
        TextView fileName= holder.name;
        TextView displayFullPath = holder.fullPath;
        fileName.setText(name);

        if (!file.isDirectory()){
            fileSize.setVisibility(View.GONE);
            if (show){
                displayFullPath.setText(file.getPath());
            }else{
                displayFullPath.setVisibility(View.GONE);
            }

            setImage(doocumentImage,name);
        }else {
            fileSize.setVisibility(View.VISIBLE);
            displayFullPath.setVisibility(View.GONE);
           doocumentImage.setImageResource(image);
           if (Long.parseLong(numberOffile)>1){
               fileSize.setText(numberOffile+" files");
           }else{
               fileSize.setText(numberOffile+" file");
           }

        }




holder.itemView.setOnClickListener(view->{
    recyclerViewClick.onClickItem(position,file,filePath);


});
    }

    private void setImage(ImageView view, String name){

//        String lastThree ="fil";
//if (name.length()>3){
//    lastThree = name.substring(name.length()-3);
//}

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

            default:
                view.setImageResource(R.drawable.all_doc);
                break;
        }

    }

    @Override
    public int getItemViewType(int position) {
      switch (layout){
          case 2:
              return Constant.GRID_LAYOUT;

          default:
              return Constant.LINEAR_LAYOUT;
      }

    }

    @Override
    public int getItemCount() {
        return documents.size();
    }




  class ViewHolder extends RecyclerView.ViewHolder{
      ImageView showFileImage,showButtonClick;

      TextView name, numbeer,fullPath;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            showFileImage=itemView.findViewById(R.id.folderImage);
            name = itemView.findViewById(R.id.name);
            numbeer = itemView.findViewById(R.id.numbersOfFile);
            fullPath = itemView.findViewById(R.id.displayFullPath);

        }

    }
}
