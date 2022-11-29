package com.drissamri.favorites;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.drissamri.favorites.config.AppConfig;
import com.drissamri.favorites.model.Favorite;
import com.drissamri.favorites.model.dto.AddFavoriteRequest;
import com.drissamri.favorites.service.FavoriteService;
import com.fasterxml.jackson.jr.ob.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.http.HttpStatusCode;

public class AddFavoriteLambda {
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

    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        APIGatewayProxyResponseEvent response;

        try {
            AddFavoriteRequest addFavoriteRequest = jsonMapper.beanFrom(AddFavoriteRequest.class, input.getBody());
            Favorite savedFavorite = favoriteService.add(addFavoriteRequest);
            LOG.info("Favorite created: {}", savedFavorite);

            response = new APIGatewayProxyResponseEvent()
                    .withStatusCode(HttpStatusCode.OK)
                    .withBody(jsonMapper.asString(savedFavorite));
        } catch (Exception ex) {
            LOG.error("Exception: {}", ex.getMessage());
            response = createErrorResponse();
        }
        return response;
    }

    private APIGatewayProxyResponseEvent createErrorResponse() {
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(HttpStatusCode.INTERNAL_SERVER_ERROR);
    }
}