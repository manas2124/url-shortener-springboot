package com.manas.urlshortener.controller;

import com.manas.urlshortener.model.UrlEntity;
import com.manas.urlshortener.service.UrlService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UrlController {

    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    // ================= SHORTEN URL =================
    // POST /api/shorten
    @PostMapping("/shorten")
    public ResponseEntity<?> shortenUrl(@RequestBody Map<String, String> request) {

        String longUrl = request.get("longUrl");
        if (longUrl == null || longUrl.isBlank()) {
            return ResponseEntity.badRequest()
                    .body("Long URL is required");
        }

        UrlEntity urlEntity = urlService.shortenUrl(longUrl);

        return ResponseEntity.ok(
                Map.of(
                        "shortUrl", "http://localhost:8080/" + urlEntity.getSlug(),
                        "slug", urlEntity.getSlug()));
    }

    // ================= ANALYTICS =================
    // GET /api/analytics/{slug}
    @GetMapping("/analytics/{slug}")
    public ResponseEntity<?> getAnalytics(@PathVariable String slug) {

        Optional<Long> accessCount = urlService.getAccessCount(slug);

        if (accessCount.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Slug not found");
        }

        return ResponseEntity.ok(
                Map.of(
                        "slug", slug,
                        "accessCount", accessCount.get()));
    }
}
