package com.sd.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

/**
 * @author D
 */
@Configuration
public class RouteConfig {
  //
//  @Bean
//  public RouteLocator routeLocator(RouteLocatorBuilder builder) {
//    return builder.routes()
//            .route(r -> r.path("/user/**")
//                    .uri("http://localhost:16002/"))
//            .route(r -> r.path("/broker/**")
//                    .uri("http://localhost:160032/"))
//            .build();
//  }
  @Bean
  public KeyResolver userKeyResolver() {
    return exchange -> Mono.just("1");
  }
}


