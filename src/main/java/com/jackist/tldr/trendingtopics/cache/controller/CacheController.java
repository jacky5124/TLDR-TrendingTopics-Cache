package com.jackist.tldr.trendingtopics.cache.controller;

import com.jackist.tldr.trendingtopics.cache.pojo.cache.Result;
import com.jackist.tldr.trendingtopics.cache.service.CacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
public class CacheController {
    private static final Logger logger = LoggerFactory.getLogger(CacheController.class);

    private final CacheService cacheService;

    public CacheController(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @GetMapping("/trending-topics")
    public ResponseEntity<Result> get(@RequestParam("mkt") String mkt) {
        logger.info("Getting summary cache for {}", mkt);
        Result result;
        try {
            result = cacheService.get(mkt);
        } catch (Exception e) {
            String error = "Error getting summary cache for %s".formatted(mkt);
            logger.error(error, e);
            return ResponseEntity.internalServerError().body(null);
        }
        return ResponseEntity.ok(result);
    }

    @PutMapping("/trending-topics")
    public ResponseEntity<String> put(@RequestParam("mkt") String mkt, @RequestBody Result result) {
        logger.info("Updating summary cache for {}", mkt);
        try {
            cacheService.put(mkt, result);
        } catch (Exception e) {
            String error = "Error updating summary cache for %s".formatted(mkt);
            logger.error(error, e);
            return ResponseEntity.internalServerError().body("Error");
        }
        return ResponseEntity.ok("Success");
    }
}
