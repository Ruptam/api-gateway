package com.ruptam.apigateway;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Configuration
public class CustomFilter implements GlobalFilter {

    Logger log = LoggerFactory.getLogger(CustomFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String authorizationHeader = request.getHeaders().getFirst("Authorization");

        //if there is any code need to be executed for any particular service then use below if with url pattern
        // if (request.getURI().toString().contains("/api/student")) {

        // }
        log.info("Authorization Header ==> " + authorizationHeader);
        if (authorizationHeader.isEmpty()) {
            throw new RuntimeException("No Authorization header found");
        }
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            ServerHttpResponse response = exchange.getResponse();
            log.info("Post filter after executing the request and before sending the response to gateway => "
                 + response.getStatusCode());
        }));
    }
    
}
