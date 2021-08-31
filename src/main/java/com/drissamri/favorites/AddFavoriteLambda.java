package com.drissamri.favorites;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import com.drissamri.favorites.config.AppConfig;
import com.drissamri.favorites.model.Favorite;
import com.drissamri.favorites.model.dto.AddFavoriteRequest;
import com.drissamri.favorites.service.FavoriteService;
import com.fasterxml.jackson.jr.ob.JSON;
import com.splunk.support.lambda.TracingRequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.http.HttpStatusCode;

public class AddFavoriteLambda extends TracingRequestWrapper {
    private static final Logger LOG = LoggerFactory.getLogger(AddFavoriteLambda.class);
    private FavoriteService favoriteService;
    private JSON jsonMapper;

    public AddFavoriteLambda() {
        this(AppConfig.favoriteService(), JSON.std);
    }

    public AddFavoriteLambda(FavoriteService favoriteService, JSON jsonMapper) {
        this.favoriteService = favoriteService;
        this.jsonMapper = jsonMapper;
    }
    
    public APIGatewayV2HTTPResponse handleRequest(APIGatewayV2HTTPEvent input, Context context) {
        APIGatewayV2HTTPResponse response;
        try {
            AddFavoriteRequest addFavoriteRequest = jsonMapper.beanFrom(AddFavoriteRequest.class, input.getBody());
            Favorite savedFavorite = favoriteService.add(addFavoriteRequest);
            LOG.info("Favorite created: {}", savedFavorite);

            response = APIGatewayV2HTTPResponse.builder()
                    .withStatusCode(HttpStatusCode.OK)
                    .withBody(jsonMapper.asString(savedFavorite))
                    .build();

        } catch (Exception ex) {
            LOG.error("Exception: {}", ex.getMessage());
            response = createErrorResponse();
        }
        return response;
    }

    private APIGatewayV2HTTPResponse createErrorResponse() {
        return APIGatewayV2HTTPResponse.builder()
                .withStatusCode(HttpStatusCode.INTERNAL_SERVER_ERROR)
                .build();
    }
}