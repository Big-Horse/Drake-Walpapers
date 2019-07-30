package com.bighorse.drakewallpapers;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class Adapter extends RecyclerView.Adapter<ImageViewHolder> implements ImageViewHolder.onImageClickedListener {
    private ArrayList<ImageModel> imagesList = new ArrayList<>();
    private onImageClickedListener mListener;
    int counter = 0;

    private Context mContext;

    public void setListener(onImageClickedListener mListener, Context context){
        this.mListener = mListener;
        this.mContext = context;
    }

    public ArrayList<ImageModel> getList() {
        return imagesList;
    }

    public interface onImageClickedListener{
        void onClick(ImageModel adapterPosition, int position);
    }

    public void addImage(ImageModel image){
        imagesList.add(image);
        //notifyItemInserted(imagesList.size() - 1);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.image_item, viewGroup, false);
        ImageViewHolder holder = new ImageViewHolder(view);
        holder.setListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder imageViewHolder, int position) {
        if(counter == 3 || counter == 14){
            ImageModel imageModel = new ImageModel();
            imageModel.setUriThumbDownload("https://firebasestorage.googleapis.com/v0/b/drake-wallpapers.appspot.com/o/wallpapers%2Fbanner_ads.jpg?alt=media&token=3e94d30e-a23d-4950-a089-ebddbbdad85d");
            imageModel.setUriWallpaperDownload("https://firebasestorage.googleapis.com/v0/b/drake-wallpapers.appspot.com/o/wallpapers%2Fbanner_ads.jpg?alt=media&token=3e94d30e-a23d-4950-a089-ebddbbdad85d");
            imagesList.add(imageModel);
        }
        ImageModel image = imagesList.get(position);
        imageViewHolder.setImage(image, mContext);
        counter ++;
    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }


    @Override
    public void onClick(int adapterPosition) {
        if(mListener != null){
            mListener.onClick(imagesList.get(adapterPosition), adapterPosition);
        }
    }
}
