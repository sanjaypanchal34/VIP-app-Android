package com.vip.marrakech.admin.models;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PromotionGallery implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("promotions_id")
    @Expose
    private String promotionsId;
    @SerializedName("image_name")
    @Expose
    private String imageName;
    @SerializedName("image_type")
    @Expose
    private String imageType;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("encrypted_id")
    @Expose
    private String encryptedId;

    private boolean isFromLocal = false;

    public PromotionGallery() {
    }

    public PromotionGallery(boolean b, String path) {
        imageName = path;
        isFromLocal = b;
    }

    public PromotionGallery(String id) {
        encryptedId = id;
    }

    public boolean isFromLocal() {
        return isFromLocal;
    }

    public void setFromLocal(boolean fromLocal) {
        isFromLocal = fromLocal;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPromotionsId() {
        return promotionsId;
    }

    public void setPromotionsId(String promotionsId) {
        this.promotionsId = promotionsId;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getEncryptedId() {
        return encryptedId;
    }

    public void setEncryptedId(String encryptedId) {
        this.encryptedId = encryptedId;
    }


    @Override
    public boolean equals(@Nullable Object obj) {
        try {
            PromotionGallery gallery = (PromotionGallery) obj;

            if (gallery != null) {
                return gallery.encryptedId.equals(encryptedId);
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
}
