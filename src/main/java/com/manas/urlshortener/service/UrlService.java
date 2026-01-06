package com.manas.urlshortener.service;

import com.manas.urlshortener.model.UrlEntity;
import com.manas.urlshortener.repository.UrlRepository;
import com.manas.urlshortener.util.SlugGenerator;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class UrlService {

    // 1 minute expiry (you can change later)
    private static final long EXPIRY_DURATION_SECONDS = 60;

    private final UrlRepository urlRepository;

    public UrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    // ================= SHORTEN URL =================
    public UrlEntity shortenUrl(String longUrl) {

        // 🔹 Idempotency check
        Optional<UrlEntity> existing = urlRepository.findByLongUrl(longUrl);
        if (existing.isPresent()) {
            return existing.get();
        }

        // 🔹 Generate slug
        String slug = SlugGenerator.generateSlug(longUrl);

        // 🔹 Set expiry time
        Instant expiryTime = Instant.now().plusSeconds(EXPIRY_DURATION_SECONDS);

        UrlEntity urlEntity = new UrlEntity(slug, longUrl, expiryTime);

        return urlRepository.save(urlEntity);
    }

    // ================= RESOLVE URL =================
    public Optional<UrlEntity> resolveUrl(String slug) {

        Optional<UrlEntity> optionalUrl = urlRepository.findBySlug(slug);

        if (optionalUrl.isEmpty()) {
            return Optional.empty();
        }

        UrlEntity urlEntity = optionalUrl.get();

        // ⛔ Expiry check
        if (Instant.now().isAfter(urlEntity.getExpiryTime())) {
            return Optional.empty();
        }

        // ✅ Increment access count
        urlEntity.setAccessCount(urlEntity.getAccessCount() + 1);
        urlRepository.save(urlEntity);

        return Optional.of(urlEntity);
    }

    // ================= ANALYTICS =================
    public Optional<Long> getAccessCount(String slug) {
        return urlRepository.findBySlug(slug)
                .map(UrlEntity::getAccessCount);
    }
}
