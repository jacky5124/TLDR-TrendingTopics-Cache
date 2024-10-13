package com.jackist.tldr.trendingtopics.summarycache.controller;

import com.jackist.tldr.trendingtopics.summarycache.pojo.Result;
import com.jackist.tldr.trendingtopics.summarycache.service.SummaryCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
public class SummaryCacheController {
    private static final Logger logger = LoggerFactory.getLogger(SummaryCacheController.class);

    private final SummaryCacheService summaryCacheService;

    public SummaryCacheController(SummaryCacheService summaryCacheService) {
        this.summaryCacheService = summaryCacheService;
    }

    @GetMapping("/trending-topics")
    public ResponseEntity<Result> get(@RequestParam("mkt") String mkt) {
        logger.info("Getting summary cache for {}", mkt);
        Result result;
        try {
            result = summaryCacheService.get(mkt);
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
            summaryCacheService.put(mkt, result);
        } catch (Exception e) {
            String error = "Error updating summary cache for %s".formatted(mkt);
            logger.error(error, e);
            return ResponseEntity.internalServerError().body("Error");
        }
        return ResponseEntity.ok("Success");
    }
}
