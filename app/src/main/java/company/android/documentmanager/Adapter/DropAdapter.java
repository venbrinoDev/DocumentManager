package company.android.documentmanager.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import company.android.documentmanager.Model.SpinnerModel;
import company.android.documentmanager.R;

public class DropAdapter extends ArrayAdapter{



    public DropAdapter(@NonNull Context context, ArrayList<SpinnerModel> models) {
        super(context,0, models);
    }


    public View initView (int Position,View convertView ,ViewGroup parent){
        if (convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_layout,parent,false);
        }
        ImageView imageView = convertView.findViewById(R.id.folderImage);
        TextView textView = convertView.findViewById(R.id.name);

        SpinnerModel spinnerModel =(SpinnerModel) getItem(Position);

        if (spinnerModel !=null){
            imageView.setImageResource(spinnerModel.getImage());
            textView.setText(spinnerModel.getText());
        }


        return convertView;

    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position,convertView,parent);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position,convertView,parent);
    }
}
