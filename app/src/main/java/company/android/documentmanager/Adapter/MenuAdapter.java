package company.android.documentmanager.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import company.android.documentmanager.R;
import nl.psdcompany.duonavigationdrawer.views.DuoOptionView;

public class MenuAdapter extends BaseAdapter {
    private ArrayList<String> mOptions = new ArrayList<>();
    private ArrayList<DuoOptionView> mOptionViews = new ArrayList<>();
    ArrayList<Integer> mdrawable = new ArrayList<>();

    int [] images ={R.drawable.ic_baseline_home, R.drawable.ic_baseline_folder_,
            R.drawable.ic_baseline_file,R.drawable.ic_baseline_folder_,R.drawable.ic_baseline_recent,
    R.drawable.ic_baseline_bookmark_24,R.drawable.ic_baseline_settings_24,R.drawable.ic_baseline_share_24,
    R.drawable.ic_baseline_star_24};

    private void AddAllImages(){

        mdrawable.add(R.drawable.ic_baseline_home);
        mdrawable.add(R.drawable.ic_baseline_folder_);
        mdrawable.add(R.drawable.ic_baseline_file);
        mdrawable.add(R.drawable.ic_baseline_filemager);
        mdrawable.add(R.drawable.ic_baseline_recent);
        mdrawable.add(R.drawable.ic_baseline_bookmark_24);
        mdrawable.add(R.drawable.ic_baseline_settings_24);
        mdrawable.add(R.drawable.ic_baseline_share_24);
        mdrawable.add(R.drawable.ic_baseline_star_24);

    }
    private Context globalContext;

    public  MenuAdapter(ArrayList<String> options, Context context) {
        mOptions = options;
     AddAllImages();
     globalContext =context;
    }

    @Override
    public int getCount() {
        return mOptions.size();
    }

    @Override
    public Object getItem(int position) {
        return mOptions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final String option = mOptions.get(position);
         Drawable images = globalContext.getDrawable(mdrawable.get(position));
        // Using the DuoOptionView to easily recreate the demo
        final DuoOptionView optionView;
        if (convertView == null) {
            optionView = new DuoOptionView(parent.getContext());
        } else {
            optionView = (DuoOptionView) convertView;
        }




        // Using the DuoOptionView's default selectors
        optionView.bind( option,images, null);

        // Adding the views to an array list to handle view selection
        mOptionViews.add(optionView);


        return optionView;
    }

    public void setViewSelected(int position, boolean b) {

        // Looping through the options in the menu
        // Selecting the chosen option
        for (int i = 0; i < mOptionViews.size(); i++) {
            if (i == position) {
                mOptionViews.get(i).setSelected(b);
            } else {
                mOptionViews.get(i).setSelected(!b);
            }
        }
    }
}
