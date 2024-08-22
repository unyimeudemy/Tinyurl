package com.piraxx.tinyurl.unitTests;

import com.piraxx.tinyurl.exceptions.InternalServerErrorException;
import com.piraxx.tinyurl.exceptions.NotFoundException;
import com.piraxx.tinyurl.models.LegitUrls;
import com.piraxx.tinyurl.models.SpamUrls;
import com.piraxx.tinyurl.repository.LegitUrlsRepository;
import com.piraxx.tinyurl.repository.SpamUrlsRepository;
import com.piraxx.tinyurl.services.ServicesImpl.BloomFilter;
import com.piraxx.tinyurl.services.ServicesImpl.UrlServiceImpl;
import com.piraxx.tinyurl.utils.Encoder;
import com.piraxx.tinyurl.utils.SnowflakeIdGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UrlServiceImplUnitTest {

    @Mock
    private LegitUrlsRepository legitUrlsRepository;

    @Mock
    private SpamUrlsRepository spamUrlsRepository;

    @Mock
    private BloomFilter bloomFilter;

    @Mock
    private SnowflakeIdGenerator snowflakeIdGenerator;

    @Mock
    private Encoder encoder;

    @InjectMocks
    private UrlServiceImpl underTest;

    @Test
    public void test_that_generateShort_generates_and_return_shortUrl(){
        LegitUrls legitUrl = LegitUrls.builder()
                .key("https://tiny.com/27qMi57J")
                .value("https://example.com/long-url")
                .build();
        long id = 12345678L;


        when(legitUrlsRepository.findByValue(legitUrl.getValue()))
                .thenReturn(Optional.empty());
        when(bloomFilter.mightContain(legitUrl.getValue())).thenReturn(false);
        when(snowflakeIdGenerator.nextId()).thenReturn(id);
        when(encoder.mapToBase58Value(id)).thenReturn("27qMi57J");

        String shortUrl = underTest.generateShort(legitUrl.getValue());
        assertThat(shortUrl).isEqualTo(legitUrl.getKey());

    }

    @Test
    public void test_that_generateShort_returns_url_if_exist(){
        LegitUrls legitUrl = LegitUrls.builder()
                .key("https://tiny.com/27qMi57J")
                .value("https://example.com/long-url")
                .build();

        when(legitUrlsRepository.findByValue(legitUrl.getValue()))
                .thenReturn(Optional.of(legitUrl));

        String shortUrl = underTest.generateShort(legitUrl.getValue());
        assertThat(shortUrl).isEqualTo(legitUrl.getKey());
    }

    @Test
    public void test_that_generateShort_catches_spam_url(){
        SpamUrls spamUrl = SpamUrls.builder()
                .url("https://example.com/spam-url")
                .build();

        when(bloomFilter.mightContain(spamUrl.getUrl())).thenReturn(true);
        when(spamUrlsRepository.findByUrl(spamUrl.getUrl()))
                .thenReturn(Optional.of(spamUrl));

        String shortUrl = underTest.generateShort(spamUrl.getUrl());
        assertThat(shortUrl).isEqualTo("Potential spam detected: URL flagged by the system.");
    }

    @Test
    public void test_that_generateShort_throws_exception_for_other_errors(){
        SpamUrls spamUrl = SpamUrls.builder()
                .url("https://example.com/spam-url")
                .build();
        when(legitUrlsRepository.findByValue(spamUrl.getUrl())).thenThrow(new RuntimeException("Unexpected error"));

        assertThatThrownBy(() -> underTest.generateShort(spamUrl.getUrl()))
                .isInstanceOf(InternalServerErrorException.class)
                .hasMessage("Unexpected error");
    }

    @Test
    public void test_that_addSpamUrl_saves_and_returns_url(){
        SpamUrls spamUrl = SpamUrls.builder()
                .url("https://example.com/spam-url")
                .build();

        when(spamUrlsRepository.findByUrl(spamUrl.getUrl())).thenReturn(Optional.empty());
        when(spamUrlsRepository.save(spamUrl)).thenReturn(spamUrl);
        String url = underTest.addSpamUrl(spamUrl.getUrl());
        assertThat(url).isEqualTo(spamUrl.getUrl());
    }

    @Test
    public void test_that_addSpamUrl_returns_url_when_it_already_exist(){
        SpamUrls spamUrl = SpamUrls.builder()
                .url("https://example.com/spam-url")
                .build();

        when(spamUrlsRepository.findByUrl(spamUrl.getUrl()))
                .thenReturn(Optional.of(spamUrl));
        String url = underTest.addSpamUrl(spamUrl.getUrl());
        assertThat(url).isEqualTo(spamUrl.getUrl());
    }

    @Test
    public void test_that_addSpamUrl_throws_exception_for_other_errors(){
        SpamUrls spamUrl = SpamUrls.builder()
                .url("https://example.com/spam-url")
                .build();
        when(spamUrlsRepository.findByUrl(spamUrl.getUrl())).thenThrow(new RuntimeException("Unexpected error"));
        assertThatThrownBy(() -> underTest.addSpamUrl(spamUrl.getUrl()))
                .isInstanceOf(InternalServerErrorException.class)
                .hasMessage("Unexpected error");
    }

    @Test
    public void test_that_findByKey_finds_return_url(){
        LegitUrls legitUrl = LegitUrls.builder()
                .key("https://tiny.com/27qMi57J")
                .value("https://example.com/long-url")
                .build();

        when(legitUrlsRepository.findById(legitUrl.getKey()))
                .thenReturn(Optional.of(legitUrl));
        String url = underTest.findByKey(legitUrl.getKey());
        assertThat(url).isEqualTo(legitUrl.getValue());
    }

    @Test
    public void test_that_findByKey_throws_not_found_exception_for_none_existing_url(){
        LegitUrls legitUrl = LegitUrls.builder()
                .key("https://tiny.com/27qMi57J")
                .value("https://example.com/long-url")
                .build();

        when(legitUrlsRepository.findById(legitUrl.getKey()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.findByKey(legitUrl.getKey()))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("URL not found");
    }

    @Test
    public void test_that_findByKey_throws_internal_server_error_for_unexpected_cases(){
        LegitUrls legitUrl = LegitUrls.builder()
                .key("https://tiny.com/27qMi57J")
                .value("https://example.com/long-url")
                .build();

        when(legitUrlsRepository.findById(legitUrl.getKey()))
                .thenThrow(new RuntimeException("Something expected happened"));

        assertThatThrownBy(() -> underTest.findByKey(legitUrl.getKey()))
                .isInstanceOf(InternalServerErrorException.class)
                .hasMessage("Something expected happened");
    }
}
