package com.kinamulen.binarfoodgateway.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    public static final List<String> openApiEndpoints = List.of(
//            "/binarfood/user/register",
//            "/binarfood/merchant/register",
//            "/binarfood/merchant",
//            "/binarfood/merchant/open",
//            "/binarfood/product",
//            "/binarfood/product/merchant/%s",
//            "/binarfood/product/%s",
            "/_public",
            "/auth/**",
            "/eureka"
    );

//    public static final List<String> userApiEndpoints = List.of(
//            "/binarfood/wallet/%s",
//            "/binarfood/user/%s",
//            "/binarfood/order/%s/invoice",
//            "/binarfood/order",
//            "/binarfood/order/%s",
//            "/binarfood/order/%s/pay"
//    );


//    public static final List<String> merchantApiEndpoints = List.of(
//            "/binarfood/wallet/%s",
//            "/binarfood/product/_create",
//            "/binarfood/product/%s/_update",
//            "/binarfood/product/%s/_delete",
//            "/binarfood/merchant/%s/_update",
//            "/binarfood/merchant/%s/_delete"
//    );


    public Predicate<ServerHttpRequest> isSecured =
            //kalo GAADA di openApiEndpoints -> Harus ngecek TOKEN
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));

    public static final String USER_API_ENDPOINT_SUFFIX = "/_user-secured";
    public Predicate<ServerHttpRequest> isUserSecured =
            //kalo punya suffix /_user-secured -> Harus ngecek TOKEN -> Role harus User
            request -> request.getURI().getPath().contains(USER_API_ENDPOINT_SUFFIX);

    public static final String MERCHANT_API_ENDPOINT_SUFFIX = "/_merchant-secured";
    public Predicate<ServerHttpRequest> isMerchantSecured =
            //kalo punya suffix /_merchant-secured -> Harus ngecek TOKEN -> Role harus Merchant
            request -> request.getURI().getPath().contains(MERCHANT_API_ENDPOINT_SUFFIX);

}
