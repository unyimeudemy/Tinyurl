package com.piraxx.tinyurl.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "legit_urls")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LegitUrls {
    @Id
    private String key;

    @Indexed(unique = true)
    private String value;
}
