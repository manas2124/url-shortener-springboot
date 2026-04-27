package com.manas.urlshortener.controller;

import com.manas.urlshortener.model.UrlEntity;
import com.manas.urlshortener.service.UrlService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final UrlService urlService;

    public ApiController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/shorten")
    public ResponseEntity<?> shorten(@RequestBody Map<String, String> request) {

        String longUrl = request.get("longUrl");

        if (longUrl == null || longUrl.isBlank()) {
            return ResponseEntity.badRequest().body("Long URL required");
        }

        try {
            UrlEntity entity = urlService.shortenUrl(longUrl);

            return ResponseEntity.ok(Map.of(
                    "slug", entity.getSlug(),
                    "shortUrl", "http://localhost:8080/r/" + entity.getSlug(),
                    "expiryTime", entity.getExpiryTime().toString()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Enter a valid URL");
        }
    }

    @GetMapping("/analytics/{slug}")
    public ResponseEntity<?> analytics(@PathVariable String slug) {

        return urlService.getAccessCount(slug)
                .map(c -> ResponseEntity.ok(Map.of(
                        "slug", slug,
                        "accessCount", c)))
                .orElse(ResponseEntity.notFound().build());
    }
}
