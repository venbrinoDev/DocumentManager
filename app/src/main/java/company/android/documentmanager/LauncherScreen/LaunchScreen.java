package company.android.documentmanager.LauncherScreen;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.rd.PageIndicatorView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import company.android.documentmanager.Ads.FaceBooKAds;
import company.android.documentmanager.R;
import company.android.documentmanager.Utility.AppPrefrences;
import company.android.documentmanager.Utility.Constant;

public class LaunchScreen extends AppCompatActivity {
    @BindView(R.id.view_pager)
    ViewPager pager;
    @BindView(R.id.next)
    Button nextBtn;
    @BindView(R.id.prev)Button prevBtn;

    FaceBooKAds faceBooKAds;
    ArrayList<Slide> slides;
    AppPrefrences appPrefrences;

    @BindView(R.id.pageIndicatorView) PageIndicatorView pageIndicatorView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lauch_sceen);
        ButterKnife.bind(this);


        faceBooKAds = new FaceBooKAds(this);
        faceBooKAds.loadAdsWithoutShowing();

        appPrefrences = new AppPrefrences(this, Constant.SPLASH_SCREEN);

        boolean getStatedClicked = appPrefrences.getBoolean(Constant.GET_STARTED,false);
        if (getStatedClicked){

            pager.clearOnPageChangeListeners();
            Visible(prevBtn,false);
            Visible(nextBtn,false);
            Visible(pageIndicatorView,false);

            slides = getGetStartedSlide();
            preView();

        }else {

            Visible(prevBtn,false);
            slides = getSlidesList();
            preView();
            SwipePage();
            pageIndicatorView.setCount(4);
        }






    }

    private void SwipePage(){
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position ==2){
                    Visible(prevBtn,true);
                    Visible(nextBtn,true);
                    Visible(pageIndicatorView,true);
                    return;
                }


                if (position==1){
                    Visible(prevBtn,true);
                    return;
                }

                if (position==0){
                    Visible(prevBtn,false);
                    return;
                }

                if (position ==3){
                    Visible(prevBtn,false);
                    Visible(nextBtn,false);
                    Visible(pageIndicatorView,false);
                    return;
                }

            }

            @Override
            public void onPageSelected(int position) {


                pageIndicatorView.setSelection(position);


            }

            @Override
            public void onPageScrollStateChanged(int state) {


            }
        });
    }



    @OnClick({R.id.next,R.id.prev}) void clickButton(View view){
        int id = view.getId();
        if (id == R.id.next){

            faceBooKAds.loadAdsAgain();

            int position = getItem(+1);
            pager.setCurrentItem(position,true);

        }
        if (id == R.id.prev){

            int position =getItem(-1);
            pager.setCurrentItem(position,true);
        }
    }


    private int getItem(int i) {
        return pager.getCurrentItem() + i;
    }

    private  void preView(){
        CustomAdapter customAdapter = new CustomAdapter(
                slides,this,getWindowManager(),pager);
        pager.setAdapter(customAdapter);
    }

    private ArrayList<Slide> getGetStartedSlide(){
        ArrayList<Slide> slides= new ArrayList<>();
        int Images [] = {R.drawable.getstarted};
        String [] text = {"Let get Started"};

        for (int i = 0;i<Images.length;i++){

            slides.add(new Slide(Images[i],text[i]));
        }
        return slides;
    }
    private ArrayList<Slide> getSlidesList(){
        ArrayList<Slide> slides= new ArrayList<>();
        int Images [] = {R.drawable.trying_one,R.drawable.trying_two,R.drawable.trying_three,R.drawable.just_started};
        String [] text = {"List PDF,XLS,PPT,DOCS,TXT Files","Search File,Rename,Delete","Smart filter by file type","Let get Started"};

        for (int i = 0;i<Images.length;i++){

            slides.add(new Slide(Images[i],text[i]));
        }
      return slides;
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

    public class ParallaxPageTransformer implements ViewPager.PageTransformer {

        private ImageView imageView;

        public ParallaxPageTransformer(ImageView imageView) {
            this.imageView = imageView;
        }

        public void transformPage(View view, float position) {

            int pageWidth = view.getWidth();


            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0.5f);

            } else if (position <= 1) { // [-1,1]

                imageView.setTranslationX(-position * (pageWidth / 2)); //Half the normal speed

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0.5f);
            }


        }
    }
}