package com.bighorse.drakewallpapers;

import android.content.Intent;
import android.net.Uri;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.common.util.UriUtil;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MainActivity extends AppCompatActivity implements Adapter.onImageClickedListener, FirebaseController.FirebaseListener {

    private RecyclerView mRecyclerView;
    public static Adapter mAdapter;

    private ProgressBar mProgressBar;




    private AdView mAdMainBannerView;
    private InterstitialAd mInterstitialMainAd;
    private static InterstitialAd mInterstitialVideoAd;

    static int videoAttempts = 0;
    private FirebaseAuth mAuth;
    private int counter = 1;

    public static void addVideoAttempt() {
        videoAttempts++;

        if ((videoAttempts !=0) && (videoAttempts %2 == 0)) {
            mInterstitialVideoAd.show();
        }else{
            mInterstitialVideoAd.loadAd(new AdRequest.Builder().build());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        mRecyclerView = findViewById(R.id.recyclerview);
        mProgressBar = findViewById(R.id.mainProgressbar);

        mAdapter = new Adapter();
        mAdapter.setListener(this, getApplicationContext());
        mRecyclerView.setAdapter(mAdapter);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(this,3);
        mRecyclerView.setLayoutManager(mGridLayoutManager);

        FirebaseController.getInstance().setListener(this);
        FirebaseController.getInstance().attachDatabaseListener();

        MobileAds.initialize(this, "ca-app-pub-9750227298627262~4730480796");

        mAdMainBannerView = findViewById(R.id.adView);
        mAdMainBannerView.loadAd(new AdRequest.Builder().build());

        mInterstitialMainAd = new InterstitialAd(this);
        //mInterstitialMainAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialMainAd.setAdUnitId("ca-app-pub-9750227298627262/2663694421");
        mInterstitialMainAd.loadAd(new AdRequest.Builder().build());

        mInterstitialMainAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                mInterstitialMainAd.show();
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the interstitial ad is closed.
            }
        });

        mInterstitialVideoAd = new InterstitialAd(this);
        mInterstitialVideoAd.setAdUnitId("ca-app-pub-9750227298627262/7999781587");

    }

    @Override
    public void onClick(ImageModel image, int position) {
        Intent imageViewActivity = new Intent(this, ImageViewActivity.class);
        imageViewActivity.putParcelableArrayListExtra("list", mAdapter.getList());
        imageViewActivity.putExtra("position", position);
        startActivity(imageViewActivity);
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_share) {
            shareApp();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Intent shareApp() {
        Intent intent= new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");

        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_app));
        startActivity(Intent.createChooser(intent, "Spread the message!!"));
        return intent;
    }

    @Override
    public void onChildAdded(final ImageModel image) {
        new Thread() {
            @Override
            public void run() {
                final StorageReference[] storageReference = {FirebaseStorage.getInstance().getReference().child("thumbs/" + image.getUriThumb())};
                storageReference[0].getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        image.setUriThumbDownload(uri.toString());

                        storageReference[0] = FirebaseStorage.getInstance().getReference().child("wallpapers/" + image.getUriWallpaper());
                        storageReference[0].getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                image.setUriWallpaperDownload(uri.toString());

                                mAdapter.addImage(image);

                                mProgressBar.setVisibility(View.GONE);
                            }
                        });
                    }
                });

            }
        }.start();

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // do your stuff
        } else {
            signInAnonymously();
        }
    }

    private void signInAnonymously() {
        mAuth.signInAnonymously();
    }


}
