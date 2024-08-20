package com.piraxx.tinyurl.services.ServicesImpl;

import com.piraxx.tinyurl.exceptions.InternalServerErrorException;
import com.piraxx.tinyurl.exceptions.NotFoundException;
import com.piraxx.tinyurl.models.LegitUrls;
import com.piraxx.tinyurl.models.SpamUrls;
import com.piraxx.tinyurl.repository.LegitUrlsRepository;
import com.piraxx.tinyurl.repository.SpamUrlsRepository;
import com.piraxx.tinyurl.services.UrlService;
import com.piraxx.tinyurl.utils.Base58Map;
import com.piraxx.tinyurl.utils.BaseUrl;
import com.piraxx.tinyurl.utils.Encoder;
import com.piraxx.tinyurl.utils.SnowflakeIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UrlServiceImpl implements UrlService {

    @Autowired
    private SnowflakeIdGenerator snowflakeIdGenerator;

    @Autowired
    private Encoder encoder;

    @Autowired
    private BloomFilter bloomFilter;

    @Autowired
    private SpamUrlsRepository spamUrlsRepository;

    @Autowired
    private LegitUrlsRepository legitUrlsRepository;


    @Override
    public String getUrlKey(String url) {
        long id = snowflakeIdGenerator.nextId();
        return encoder.mapToBase58Value(id);
    }

    @Override
    public String generateShort(String url) {

        try{
            // check if url already exist
            Optional<LegitUrls> existingUrl = legitUrlsRepository.findByValue(url);
            if (existingUrl.isPresent()) {
                return existingUrl.get().getKey();
            }

            // check if it is a spam
            if(bloomFilter.mightContain(url)){
                Optional<SpamUrls> spamUrl = spamUrlsRepository.findByUrl(url);
                if(spamUrl.isPresent()){
                    return "Potential spam detected: URL flagged by the system.";
                }
            }

            long id = snowflakeIdGenerator.nextId();
            String shortenUrl = encoder.mapToBase58Value(id);
            StringBuilder baseUrl = BaseUrl.getBaseUrl();
            String key = baseUrl.append(shortenUrl).toString();

            LegitUrls newUrl = LegitUrls.builder()
                    .key(key)
                    .value(url)
                    .build();
            legitUrlsRepository.save(newUrl);
            return key;
        }catch (Exception e){
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Override
    public String addSpamUrl(String url) {
        try{
            if(spamUrlsRepository.findByUrl(url).isPresent()){
                return url;
            }
            bloomFilter.add(url);
            SpamUrls spamUrl = spamUrlsRepository.save(SpamUrls.builder().url(url).build());
            return spamUrl.getUrl();
        }catch (Exception e){
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Override
    public String findByKey(String urlKey) {
        try{
            Optional<LegitUrls> legitUrls = legitUrlsRepository.findById(urlKey);
            if(legitUrls.isPresent()) {
                return legitUrls.get().getValue();
            }else {
                throw new NotFoundException("URL not found");
            }
        }catch (Exception e){
            throw new InternalServerErrorException(e.getMessage());
        }
    }
}
