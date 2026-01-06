package com.manas.urlshortener.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "urls")
public class UrlEntity {

    @Id
    private String id; // MongoDB ObjectId

    private String slug;
    private String longUrl;
    private Instant expiryTime;
    private long accessCount;

    public UrlEntity() {
    }

    public UrlEntity(String slug, String longUrl, Instant expiryTime) {
        this.slug = slug;
        this.longUrl = longUrl;
        this.expiryTime = expiryTime;
        this.accessCount = 0;
    }

    // ---------- Getters & Setters ----------

    public String getId() {
        return id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    public Instant getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(Instant expiryTime) {
        this.expiryTime = expiryTime;
    }

    public long getAccessCount() {
        return accessCount;
    }

    public void setAccessCount(long accessCount) {
        this.accessCount = accessCount;
    }
}
