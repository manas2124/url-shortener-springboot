package com.manas.urlshortener.controller;

import com.manas.urlshortener.service.UrlService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class RedirectController {

    private final UrlService urlService;

    public RedirectController(UrlService urlService) {
        this.urlService = urlService;
    }

    @GetMapping("/r/{slug}")
    public RedirectView redirect(@PathVariable String slug) {

        var optional = urlService.resolveUrl(slug);

        if (optional.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Link expired or not found");
        }

        String longUrl = optional.get().getLongUrl();

        if (!longUrl.startsWith("http")) {
            longUrl = "https://" + longUrl;
        }

        RedirectView rv = new RedirectView(longUrl);
        rv.setStatusCode(HttpStatus.FOUND);
        return rv;
    }
}
