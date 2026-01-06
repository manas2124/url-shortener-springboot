package com.manas.urlshortener.repository;

import com.manas.urlshortener.model.UrlEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UrlRepository extends MongoRepository<UrlEntity, String> {

    // Find by short slug
    Optional<UrlEntity> findBySlug(String slug);

    // For idempotency (same long URL -> same slug)
    Optional<UrlEntity> findByLongUrl(String longUrl);
}
