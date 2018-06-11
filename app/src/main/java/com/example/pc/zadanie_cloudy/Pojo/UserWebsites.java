package com.example.pc.zadanie_cloudy.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserWebsites {

@SerializedName("createdAt")
@Expose
private String createdAt;
@SerializedName("updatedAt")
@Expose
private String updatedAt;
@SerializedName("UserId")
@Expose
private Integer userId;
@SerializedName("WebsiteId")
@Expose
private Integer websiteId;

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

public Integer getUserId() {
return userId;
}

public void setUserId(Integer userId) {
this.userId = userId;
}

public Integer getWebsiteId() {
return websiteId;
}

public void setWebsiteId(Integer websiteId) {
this.websiteId = websiteId;
}

}