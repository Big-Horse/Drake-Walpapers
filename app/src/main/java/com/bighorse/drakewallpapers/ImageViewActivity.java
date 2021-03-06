package com.bighorse.drakewallpapers;

import android.app.DownloadManager;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.io.IOException;
import java.util.ArrayList;



public class ImageViewActivity extends AppCompatActivity implements ImageOverlayView.onImageOverlayClickedListener{

    private ImageOverlayView mOverlayView;
    private int REQUEST_WRITE_PERMISSION_CODE = 1;
    private InterstitialAd mInterstitialImageFullAd;
    private int mImageCount;
    private String uri;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uri = "https://firebasestorage.googleapis.com/v0/b/drake-wallpapers.appspot.com/o/wallpapers%2Fbanner_ads.jpg?alt=media&token=3e94d30e-a23d-4950-a089-ebddbbdad85d";
        setContentView(R.layout.activity_image_viewer);
        mImageCount = 1;
        Intent intent = getIntent();

        int position = intent.getIntExtra("position",0);
        final ArrayList<ImageModel> list = intent.getParcelableArrayListExtra("list");
        mInterstitialImageFullAd = new InterstitialAd(this);
        mInterstitialImageFullAd.setAdUnitId("ca-app-pub-5005687032079051/2059315261");
        mInterstitialImageFullAd.loadAd(new AdRequest.Builder().build());


        mOverlayView = new ImageOverlayView(this, this, this, uri);
        new ImageViewer.Builder<>(this, list)
                .setFormatter(new ImageViewer.Formatter<ImageModel>() {
                    @Override
                    public String format(ImageModel customImage) {
                        return customImage.getUriWallpaperDownload();
                    }
                }).hideStatusBar(true).setOnDismissListener(new ImageViewer.OnDismissListener() {
            @Override
            public void onDismiss() {
                MainActivity.addVideoAttempt();
                ImageViewActivity.this.finish();
            }
        }).setStartPosition(position).setOverlayView(mOverlayView).hideStatusBar(false).setImageChangeListener(new ImageViewer.OnImageChangeListener() {
            @Override
            public void onImageChange(int position) {
                ImageModel image = (ImageModel) list.get(position);
                mOverlayView.setImage(image.getUriWallpaperDownload());
                mOverlayView.setFilename(image.getName());
                mImageCount++;
                if((mImageCount % 2) == 0){
                    mInterstitialImageFullAd.show();
                } else {
                    mInterstitialImageFullAd.loadAd(new AdRequest.Builder().build());
                }
            }
        }).build().show();

    }

    @Override
    public void onSetClicked(final String mUriImage, int type) {
        if(mUriImage.equals(uri)){
            openPlaystore();
        } else {
            final WallpaperManager myWallpaperManager
                    = WallpaperManager.getInstance(getApplicationContext());

            AsyncTask.execute(() -> {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        // On Android N and above use the new API to set both the general system wallpaper and
                        // the lock-screen-specific wallpaper
                        myWallpaperManager.setBitmap(Utility.getBitmap(mUriImage, getApplicationContext()), null, true, WallpaperManager.FLAG_SYSTEM | WallpaperManager.FLAG_LOCK);
                    } else {
                        myWallpaperManager.setBitmap(Utility.getBitmap(mUriImage, getApplicationContext()));
                    }

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    ImageViewActivity.this.runOnUiThread(() -> {
                        Toast.makeText(ImageViewActivity.this, ImageViewActivity.this.getResources().getString(R.string.wallpaper_set), Toast.LENGTH_SHORT).show();
                    });

                }
            });
        }
    }

    @Override
    public void onImageClicked(String mUriImage) {
        if(mUriImage.equals(uri)){
            openPlaystore();
        }
    }

    private void openPlaystore() {
        Intent i = new Intent(android.content.Intent.ACTION_VIEW);
        i.setData(Uri.parse("https://play.google.com/store/apps/details?id=game.drake.flappydrake"));
        startActivity(i);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_WRITE_PERMISSION_CODE)
        {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mOverlayView.clickDownload();
            } else {
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
            }
        }
    }

    @Override
    public void onDownloadClicked(String mUriImage, String filename) {
        if(mUriImage.equals(uri)){
            openPlaystore();
        } else {
            Uri uri = Uri.parse(mUriImage);
            DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                    DownloadManager.Request.NETWORK_MOBILE);

            // set title and description
            request.setTitle(filename + ".jgp");

            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

            //set the local destination for download file to a path within the application's external files directory
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename + ".jpg");
            request.setMimeType("image/*");
            downloadManager.enqueue(request);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
