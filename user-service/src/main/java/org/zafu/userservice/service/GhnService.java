package org.zafu.userservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.zafu.userservice.dto.response.ghn.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GhnService {

    @Value("${app.ghn.token-key}")
    private String tokenKey;
    @Value("${app.ghn.shop-id}")
    private String shopId;
    @Value("${app.ghn.api-url}")
    private String apiUrl;
    @Value("${app.ghn.fee-url}")
    private String feeUrl;

    private final ObjectMapper mapper;
    private final RestTemplate restTemplate;


    public List<Province> getProvinces(){
        String url = apiUrl+"/province";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Token", tokenKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        try{
            String response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();
            JavaType type = mapper.getTypeFactory().constructParametricType(GhnApiResponse.class,
                    mapper.getTypeFactory().constructCollectionType(List.class, Province.class));
            GhnApiResponse<List<Province>> apiResponse = mapper.readValue(response, type);
            return apiResponse.getData();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public List<District> getDistricts(Integer provinceId){
        String url = apiUrl+"/district";
        ProvinceRequest request = ProvinceRequest.builder().province_id(provinceId).build();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Token", tokenKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ProvinceRequest> entity = new HttpEntity<>(request, headers);
        try {
            String response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class).getBody();
            JavaType type = mapper.getTypeFactory().constructParametricType(GhnApiResponse.class,
                    mapper.getTypeFactory().constructCollectionType(List.class, District.class));
            GhnApiResponse<List<District>> apiResponse = mapper.readValue(response, type);
            return apiResponse.getData();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Ward> getWards(Integer districtId){
        String url = apiUrl+"/ward?district_id";
        DistrictRequest request = DistrictRequest.builder().district_id(districtId).build();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Token", tokenKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<DistrictRequest> entity = new HttpEntity<>(request, headers);
        try {
            String response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class).getBody();
            JavaType type = mapper.getTypeFactory().constructParametricType(GhnApiResponse.class,
                    mapper.getTypeFactory().constructCollectionType(List.class, Ward.class));
            GhnApiResponse<List<Ward>> apiResponse = mapper.readValue(response, type);
            return apiResponse.getData();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
