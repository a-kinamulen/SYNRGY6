package com.kinamulen.binarfoodgateway.filter;

import com.google.common.net.HttpHeaders;
import com.kinamulen.binarfoodgateway.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Component
@Slf4j
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator validator;

    //    @Autowired
//    private RestTemplate template;
    @Autowired
    private JwtUtil jwtUtil;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {
                org.springframework.http.HttpHeaders headers = exchange.getRequest().getHeaders();
                //header contains token or not
                if (!headers.containsKey(HttpHeaders.AUTHORIZATION)) {
                    log.info("missing authorization header");
                    throw new ResponseStatusException(HttpStatusCode.valueOf(401));
                }

                String authHeader = headers.get(HttpHeaders.AUTHORIZATION).get(0);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }

                Map<String, Map<String, String>> decodedJwt = jwtUtil.decodeJwt(authHeader);
                if (validator.isUserSecured.test(exchange.getRequest()) && (!decodedJwt.get("payload").get("type").equals("USER"))) {
                    log.info("role mismatch, role needs to be: USER");
                    throw new ResponseStatusException(HttpStatusCode.valueOf(401));
                }
                if (decodedJwt.get("payload").get("type").equals("USER")){
                    exchange.getRequest().mutate().header("userId", decodedJwt.get("payload").get("binarfoodId"));
                }

                if (validator.isMerchantSecured.test(exchange.getRequest()) && (!decodedJwt.get("payload").get("type").equals("MERCHANT"))) {
                    log.info("role mismatch, role needs to be: MERCHANT");
                    throw new ResponseStatusException(HttpStatusCode.valueOf(401));
                }

                if (decodedJwt.get("payload").get("type").equals("MERCHANT")){
                    exchange.getRequest().mutate().header("merchantId", decodedJwt.get("payload").get("binarfoodId"));
                }

                try {
//                    //REST call to AUTH service
//                    template.getForObject("http://SECURITY-SERVICE//validate?token" + authHeader, String.class);
                    jwtUtil.validateToken(authHeader);

                } catch (Exception e) {
                    log.info("un authorized access to application");
                    throw new ResponseStatusException(HttpStatusCode.valueOf(401));
                }
            }
            return chain.filter(exchange);
        });
    }

    public static class Config {

    }
}
