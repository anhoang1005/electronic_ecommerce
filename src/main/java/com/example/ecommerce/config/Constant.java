package com.example.ecommerce.config;

import java.util.concurrent.atomic.AtomicInteger;

public class Constant {
    private static final String APPLICATION_NAME = "electronic-ecommerce";
    private static final String SERVER_PORT = "8080";
    private static final boolean DOCKER_COMPOSE_ENABLE = false;
    private static final String LOGGING_FILE_NAME = "logs/application.log";
    private static final String LOGGING_LEVEL_ROOT = "WARN";
    private static final String JWT_SECRET_KEY = "CiLq3KRj2+Rx8Gjs4rSi/Cp+7vD+JtkspLFaVs8M9mE=";
    private static final Long JWT_EXPIRED_TIME = 604800L;

}
