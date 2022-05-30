package com.vip.marrakech.vendor.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ConciergeModel implements Serializable {

@SerializedName("name")
@Expose
private String name;
@SerializedName("email")
@Expose
private String email;
@SerializedName("comission_percentage")
@Expose
private String comissionPercentage;
@SerializedName("concierge_id")
@Expose
private String conciergeId;
@SerializedName("encrypted_concierge_id")
@Expose
private String encryptedConciergeId;
@SerializedName("created_date")
@Expose
private String createdDate;

public String getName() {
return name;
}

public void setName(String name) {
this.name = name;
}

public String getEmail() {
return email;
}

public void setEmail(String email) {
this.email = email;
}

public String getComissionPercentage() {
return comissionPercentage;
}

public void setComissionPercentage(String comissionPercentage) {
this.comissionPercentage = comissionPercentage;
}

public String getConciergeId() {
return conciergeId;
}

public void setConciergeId(String conciergeId) {
this.conciergeId = conciergeId;
}

public String getEncryptedConciergeId() {
return encryptedConciergeId;
}

public void setEncryptedConciergeId(String encryptedConciergeId) {
this.encryptedConciergeId = encryptedConciergeId;
}

public String getCreatedDate() {
return createdDate;
}

public void setCreatedDate(String createdDate) {
this.createdDate = createdDate;
}

}