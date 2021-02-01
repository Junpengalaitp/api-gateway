package com.alaitp.api.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;

/**
 * redirect default https request for downstream services to http
 */
@Slf4j
@Component
public class SchemeFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Object uriObj = exchange.getAttributes().get(GATEWAY_REQUEST_URL_ATTR);
        if (uriObj != null) {
            URI uri = (URI) uriObj;
            log.info("filter get uri: {}", uri.toString());
            uri = this.upgradeConnection(uri);
            log.info("filter upgraded uri to: {}", uri.toString());
            exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, uri);
        }
        return chain.filter(exchange);
    }

    private URI upgradeConnection(URI uri) {
        String rawQuery = uri.getRawQuery();
        if (rawQuery == null) {
            return uri;
        }
        String scheme = "http";
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUri(uri).scheme(scheme);
        // When building the URI, UriComponentsBuilder verify the allowed characters and does not
        // support the '+' so we replace it for its equivalent '%20'.
        // See issue https://jira.spring.io/browse/SPR-10172
        uriComponentsBuilder.replaceQuery(uri.getRawQuery().replace("+", "%20"));

        return uriComponentsBuilder.build(true).toUri();
    }

    @Override
    public int getOrder() {
        return 10101;
    }
}
