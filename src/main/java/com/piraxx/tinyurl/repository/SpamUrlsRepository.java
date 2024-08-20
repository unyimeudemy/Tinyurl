package com.piraxx.tinyurl.repository;

import com.piraxx.tinyurl.models.SpamUrls;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface SpamUrlsRepository extends MongoRepository<SpamUrls, Integer> {
    Optional<SpamUrls> findByUrl(String url);
}
