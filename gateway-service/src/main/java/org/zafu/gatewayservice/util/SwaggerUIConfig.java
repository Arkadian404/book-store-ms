package org.zafu.gatewayservice.util;


import lombok.RequiredArgsConstructor;
import org.springdoc.core.AbstractSwaggerUiConfigProperties.SwaggerUrl;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class SwaggerUIConfig {
    private final DiscoveryClient discoveryClient;

    @GetMapping("/swagger-config.json")
    public Map<String, Object> swaggerConfig(){
        List<SwaggerUrl> urls = new LinkedList<>();
        discoveryClient.getServices()
                .forEach(serviceName -> discoveryClient.getInstances(serviceName)
                        .forEach(instance ->{
                            String prefix  = serviceName.substring(0, serviceName.indexOf("-")).concat("s");
                            String url = instance.getUri() +"/" + prefix + "/v3/api-docs/swagger-ui";
                            urls.add(new SwaggerUrl(serviceName, url, serviceName));
                        })
                );
        return Map.of("urls", urls);
    }
}
