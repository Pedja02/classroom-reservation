package com.NJT.WebApi.config;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Binds the "app.*" block of application-{profile}.yaml.
 *
 * Both lists are declared per profile, so local and production differ
 * without any origin or path being hardcoded in Java.
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private Frontend frontend = new Frontend();
    private Cors cors = new Cors();
    private Security security = new Security();

    @Getter
    @Setter
    public static class Frontend {

        private String url;
    }

    @Getter
    @Setter
    public static class Cors {

        private List<String> allowedOrigins = new ArrayList<>();

        public String[] originsArray() {
            return allowedOrigins.toArray(new String[0]);
        }
    }

    @Getter
    @Setter
    public static class Security {

        private List<String> publicEndpoints = new ArrayList<>();

        public String[] publicEndpointsArray() {
            return publicEndpoints.toArray(new String[0]);
        }
    }
}
