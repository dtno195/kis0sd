package com.sd.gateway.config;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Set;

/**
 * @author D
 */

@Component
public class LoggingFilter implements GlobalFilter {
  Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    Set<URI> originalUris = exchange.getAttribute(GATEWAY_ORIGINAL_REQUEST_URL_ATTR);
    if (originalUris != null) {
      URI originalUri = originalUris.iterator().next();

      Route route = exchange.getAttribute(GATEWAY_ROUTE_ATTR);

      URI routeUri = exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR);
      logger.info("Incoming request " + originalUri.toString() + " is routed to id: " + route.getId()
              + ", uri:" + routeUri);
    }
    return chain.filter(exchange);
  }
}
