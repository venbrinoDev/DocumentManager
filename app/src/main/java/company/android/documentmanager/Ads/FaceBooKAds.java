package company.android.documentmanager.Ads;

import android.content.Context;
import android.widget.LinearLayout;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;

public class FaceBooKAds {

    private AdView adView;
    private InterstitialAd interstitialAd;
    private Context context;
    private     InterstitialAdListener interstitialAdListener;


    public FaceBooKAds(Context context) {
        this.context=context;

    }

    public void LoadBannerAds(LinearLayout container){
        AudienceNetworkAds.initialize(context);
        adView = new AdView(context, "IMG_16_9_APP_INSTALL#2029572424039676_2029575434039375", AdSize.BANNER_HEIGHT_50);
        container.addView(adView);
        adView.loadAd();
    }



    public void loadAdsWithoutShowing(){
        interstitialAd = new InterstitialAd(context, "IMG_16_9_APP_INSTALL#2029572424039676_2029575330706052");

        interstitialAdListener= new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {

            }

            @Override
            public void onInterstitialDismissed(Ad ad) {

            }

            @Override
            public void onError(Ad ad, AdError adError) {

            }


            @Override
            public void onAdLoaded(Ad ad) {
                interstitialAd.show();
            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        };

    }
    public void LoadInterstitial() {

      interstitialAd = new InterstitialAd(context, "IMG_16_9_APP_INSTALL#2029572424039676_2029575330706052");

        interstitialAdListener= new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {

            }

            @Override
            public void onInterstitialDismissed(Ad ad) {

            }

            @Override
            public void onError(Ad ad, AdError adError) {

            }


            @Override
            public void onAdLoaded(Ad ad) {
                interstitialAd.show();
            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        };

        interstitialAd.loadAd(interstitialAd.buildLoadAdConfig().withAdListener(interstitialAdListener).build());

    }


    public void  loadAdsAgain(){
        interstitialAd.loadAd(interstitialAd.buildLoadAdConfig().withAdListener(interstitialAdListener).build());
    }

    public void DestroyAds(){
        if (adView!=null){
            adView.destroy();
        }
        if (interstitialAd !=null){
            interstitialAd.destroy();
        }

        if (context !=null){
            context = null;
        }
    }
}
