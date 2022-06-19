package com.sd.config;//package com.sd.config;
//
//import okhttp3.OkHttpClient;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.concurrent.TimeUnit;
//
///**
// * @author D
// */
//@Configuration
//public class RedisTemplateConfig {
//
//  @Value("${internal.timeout}")
//  private int timeout;
//
//  @Bean
//  public RestTemplate restTemplate() {
//    OkHttpClient client = new OkHttpClient.Builder()
//            .readTimeout(timeout, TimeUnit.SECONDS)
//            .connectTimeout(timeout, TimeUnit.SECONDS)
//            .build();
//    OkHttp3ClientHttpRequestFactory okHttp3ClientHttpRequestFactory = new OkHttp3ClientHttpRequestFactory(client);
//
//    return  new RestTemplate(okHttp3ClientHttpRequestFactory);
//  }
//
//}
