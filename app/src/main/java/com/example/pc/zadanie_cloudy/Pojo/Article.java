package com.example.pc.zadanie_cloudy.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Article {
@SerializedName("id")
@Expose
private Integer id;
@SerializedName("title")
@Expose
private String title;
@SerializedName("content")
@Expose
private String content;
@SerializedName("link")
@Expose
private String link;
@SerializedName("publicationDate")
@Expose
private String publicationDate;
@SerializedName("createdAt")
@Expose
private String createdAt;
@SerializedName("updatedAt")
@Expose
private String updatedAt;
@SerializedName("WebsiteId")
@Expose
private Integer websiteId;

public Integer getId() {
return id;
}

public void setId(Integer id) {
this.id = id;
}

public String getTitle() {
return title;
}

public void setTitle(String title) {
this.title = title;
}

public String getContent() {
return content;
}

public void setContent(String content) {
this.content = content;
}

public String getLink() {
return link;
}

public void setLink(String link) {
this.link = link;
}

public String getPublicationDate() {
return publicationDate;
}

public void setPublicationDate(String publicationDate) {
this.publicationDate = publicationDate;
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

public Integer getWebsiteId() {
return websiteId;
}

public void setWebsiteId(Integer websiteId) {
this.websiteId = websiteId;
}

}