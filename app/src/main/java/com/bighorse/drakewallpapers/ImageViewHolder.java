package com.bighorse.drakewallpapers;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import com.facebook.drawee.view.SimpleDraweeView;


class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final SimpleDraweeView mImageView;

    private onImageClickedListener mListener;

    public void setListener(onImageClickedListener mListener){
        this.mListener = mListener;
    }

    public interface onImageClickedListener{
        void onClick(int adapterPosition);
    }

    public ImageViewHolder(@NonNull View itemView) {
        super(itemView);
        mImageView = itemView.findViewById(R.id.image_view);
        mImageView.setOnClickListener(this);
    }

    public void setImage(final ImageModel image, final Context context) {
        mImageView.setImageURI(image.getUriThumbDownload());
    }

    @Override
    public void onClick(View v) {
        if(mListener != null){
            mListener.onClick(getAdapterPosition());
        }
    }



}
