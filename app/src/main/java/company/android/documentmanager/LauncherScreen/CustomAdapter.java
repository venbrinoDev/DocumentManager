package company.android.documentmanager.LauncherScreen;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import company.android.documentmanager.MainActivities.MainActivity;
import company.android.documentmanager.R;
import company.android.documentmanager.Utility.AppPrefrences;
import company.android.documentmanager.Utility.Constant;


public class CustomAdapter extends PagerAdapter {

    private Context ctx;
    private LayoutInflater layoutInflater;
    private ArrayList<Slide> slides;
    private AppPrefrences appPrefrences;
    public ViewPager viewPager;



    public CustomAdapter(ArrayList<Slide> slides, Context ctx, WindowManager windowManager, ViewPager viewPager){

        this.slides = slides;
        this.ctx = ctx;
        appPrefrences = new AppPrefrences(ctx,Constant.SPLASH_SCREEN);

        this.viewPager = viewPager;

    }

    @Override
    public int getCount() {
        return slides.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==(RelativeLayout)object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater =(LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = layoutInflater.inflate(R.layout.show_app_usabilty_layout,container,false);
        ImageView imageView = itemView.findViewById(R.id.image);


        Picasso.get()
                .load(slides.get(position).getImage())
                .fit()
                .into(imageView);

        Button getStarted = itemView.findViewById(R.id.getStarted);
        TextView textView = itemView.findViewById(R.id.textView);



        boolean getStartedValue = appPrefrences.getBoolean(Constant.GET_STARTED,false);

        if (position ==3){
            getStarted.setVisibility(View.VISIBLE);

        }else {
            if (getStartedValue){
                getStarted.setVisibility(View.VISIBLE);
            }else{
                if (getStarted.getVisibility()==View.VISIBLE){
                    getStarted.setVisibility(View.GONE);
                }
            }

        }






        textView.setText(slides.get(position).getText());

        getStarted.setOnClickListener(v -> {
            if (!getStartedValue){
                appPrefrences.putBoolean(Constant.GET_STARTED,true);
            }


            Intent  startActivity= new Intent(ctx, MainActivity.class);
            ctx.startActivity(startActivity);
        });

        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((RelativeLayout)object);
    }


}
