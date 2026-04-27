package com.manas.urlshortener.service;

import com.manas.urlshortener.model.UrlEntity;
import com.manas.urlshortener.repository.UrlRepository;
import com.manas.urlshortener.util.SlugGenerator;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class UrlService {

    private static final long EXPIRY_DURATION_SECONDS = 5 * 60;

    private final UrlRepository urlRepository;

    public UrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    private boolean isValidUrl(String url) {
        try {
            new java.net.URI(url).toURL();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // ================= SHORTEN =================
    public UrlEntity shortenUrl(String longUrl) {

        if (!isValidUrl(longUrl)) {
            throw new IllegalArgumentException("Invalid URL");
        }

        Optional<UrlEntity> existing = urlRepository.findByLongUrl(longUrl);

        if (existing.isPresent()) {
            UrlEntity old = existing.get();

            // If still valid → return same slug (idempotency)
            if (Instant.now().isBefore(old.getExpiryTime())) {
                return old;
            }

            // If expired → delete immediately
            urlRepository.delete(old);
        }

        String slug;
        do {
            slug = SlugGenerator.generateSlug(longUrl + System.nanoTime());
        } while (urlRepository.findBySlug(slug).isPresent());

        Instant expiryTime = Instant.now().plusSeconds(EXPIRY_DURATION_SECONDS);
        UrlEntity entity = new UrlEntity(slug, longUrl, expiryTime);

        return urlRepository.save(entity);
    }

    // ================= RESOLVE =================
    public Optional<UrlEntity> resolveUrl(String slug) {

        Optional<UrlEntity> optional = urlRepository.findBySlug(slug);

        if (optional.isEmpty())
            return Optional.empty();

        UrlEntity entity = optional.get();

        if (Instant.now().isAfter(entity.getExpiryTime())) {
            urlRepository.delete(entity);
            return Optional.empty();
        }

        entity.setAccessCount(entity.getAccessCount() + 1);
        urlRepository.save(entity);

        return Optional.of(entity);
    }

    // ================= ANALYTICS =================
    public Optional<Long> getAccessCount(String slug) {
        return urlRepository.findBySlug(slug)
                .map(UrlEntity::getAccessCount);
    }
}
