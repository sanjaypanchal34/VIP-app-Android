package com.vip.marrakech.admin.models;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Image implements Serializable {

    private boolean isLocal = false;
    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("thumb_name")
    @Expose
    private String thumb_name;

    @SerializedName("recommandation_id")
    @Expose
    private Integer recommandationId;
    @SerializedName("image_name")
    @Expose
    private String imageName;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("encrypted_id")
    @Expose
    private String encryptedId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRecommandationId() {
        return recommandationId;
    }

    public void setRecommandationId(Integer recommandationId) {
        this.recommandationId = recommandationId;
    }

    public boolean isLocal() {
        return isLocal;
    }

    public void setLocal(boolean local) {
        isLocal = local;
    }

    public String getImageName() {
        return imageName;
    }


    public void setImageName(String imageName) {
        this.imageName = imageName;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getThumb_name() {
        return thumb_name;
    }

    public void setThumb_name(String thumb_name) {
        this.thumb_name = thumb_name;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        try {
            AdminTopModel model = (AdminTopModel) obj;
            return model.getEncrypted_id().equals(encryptedId);
        } catch (Exception e) {
            return false;
        }
    }

    public Image(String encryptedId) {
        this.encryptedId = encryptedId;
    }

    public Image(boolean isPath, String path) {
        this.imageName = path;
        this.isLocal = isPath;
    }
}