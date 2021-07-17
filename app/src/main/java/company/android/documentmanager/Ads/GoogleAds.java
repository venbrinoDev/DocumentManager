package company.android.documentmanager.Ads;

import android.content.Context;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;



public class GoogleAds {

    private InterstitialAd mInterstitialAd;
    private  Context context;
public GoogleAds(Context context){
    this.context=context;

}

public void instantAds(){
    mInterstitialAd = new InterstitialAd(context);
    mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
    mInterstitialAd.loadAd(new AdRequest.Builder().build());
    mInterstitialAd.setAdListener(new AdListener() {
        @Override
        public void onAdLoaded() {
            if (mInterstitialAd.isLoaded()){
                mInterstitialAd.show();
            }else {
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        }

        @Override
        public void onAdFailedToLoad(LoadAdError adError) {
        }

        @Override
        public void onAdOpened() {

        }

        @Override
        public void onAdClicked() {
        }

        @Override
        public void onAdLeftApplication() {
        }

        @Override
        public void onAdClosed() {
        }
    });
}

public  void destroyAds(){
    if (context !=null){
        context = null;
    }

    if (mInterstitialAd !=null){
        mInterstitialAd=null;
    }
}
}
