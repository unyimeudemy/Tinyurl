package com.piraxx.tinyurl.repository;

import com.piraxx.tinyurl.models.LegitUrls;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface LegitUrlsRepository extends MongoRepository<LegitUrls, String> {
    Optional<LegitUrls> findByValue(String url);
}
