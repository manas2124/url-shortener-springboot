package com.manas.urlshortener.controller;

import com.manas.urlshortener.service.UrlService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class RedirectController {

    private final UrlService urlService;

    public RedirectController(UrlService urlService) {
        this.urlService = urlService;
    }

    @GetMapping("/r/{slug}")
    public RedirectView redirect(@PathVariable String slug) {

        var optionalUrl = urlService.resolveUrl(slug);

        if (optionalUrl.isEmpty()) {
            throw new RuntimeException("Link Expired or Not Found");
        }

        String longUrl = optionalUrl.get().getLongUrl();

        if (!longUrl.startsWith("http://") && !longUrl.startsWith("https://")) {
            longUrl = "https://" + longUrl;
        }

        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(longUrl);
        redirectView.setStatusCode(HttpStatus.FOUND); // 302

        return redirectView;
    }
}
