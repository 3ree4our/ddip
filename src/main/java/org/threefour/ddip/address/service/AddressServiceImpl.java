package org.threefour.ddip.address.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.threefour.ddip.address.Repository.AddressRepository;
import org.threefour.ddip.address.domain.Address;

@Service
@Transactional
public class AddressServiceImpl implements AddressService {

  @Value("${naver.api.url}")
  private String apiUrl;

  @Value("${naver.api.key.id}")
  private String apiKeyId;

  @Value("${naver.api.key.secret}")
  private String apikeySecret;

  @Autowired
  private AddressRepository addressRepository;


  @Override
  public Address saveAddress(Address address) {
    System.out.println("#도로명 주소: "+ address.getDetailedAddress()+", 상세주소: "+address.getDetailedAddress());

    return addressRepository.save(address);
  };

  @Override
  public Address getCoordinates(String address) {
    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.set("X-NCP-APIGW-API-KEY-ID", apiKeyId);
    headers.set("X-NCP-APIGW-API-KEY", apikeySecret);

    //String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8);
    String url = apiUrl + "?query=" + address;
    //System.out.println("@url: "+url); //여기까진 됨
    ResponseEntity<String> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            new HttpEntity<>(headers),
            String.class
    );
    //System.out.println("@response " + response); 뽑았당~!

    ObjectMapper objectMapper = new ObjectMapper();
    Address addressEntity = new Address();

    try {
      JsonNode root = objectMapper.readTree(response.getBody());
      JsonNode addresses = root.path("addresses");
      if (addresses.isArray() && addresses.size() > 0) {
        JsonNode addressNode = addresses.get(0);

        addressEntity.setRoadAddress(addressNode.path("roadAddress").asText());
        addressEntity.setDetailedAddress(addressNode.path("jibunAddress").asText());

        addressEntity.setLnt(addressNode.path("x").asDouble());
        addressEntity.setLat(addressNode.path("y").asDouble());
        System.out.println("@@@addressEntity: " + addressEntity);
      }
    }catch (JsonProcessingException e) {
      System.out.println("json처리 오류"+e.getMessage());
    }catch(Exception e){
      e.printStackTrace();
    }
    return addressEntity;
  }

  @Override
  public String[] getGeocode(String address) throws JSONException {
    String url = apiUrl + "?query=" + address;
    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.set("X-NCP-APIGW-API-KEY", apiKeyId);
    headers.set("X-NCP-API-KEY", apikeySecret);
    System.out.println("@@url: "+url);
    HttpEntity<String> entity = new HttpEntity<>(headers);
    System.out.println("@@entity: " + entity);

    ResponseEntity<String> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            entity,
            String.class
    );

    if (response.getStatusCode() == HttpStatus.OK) {
      JSONObject jsonObject = new JSONObject(response.getBody());
      if (jsonObject.getJSONArray("addresses").length() > 0) {
        JSONObject addressObject = jsonObject.getJSONArray("addresses").getJSONObject(0);
        String lnt = addressObject.getString("x");
        String lat = addressObject.getString("y");
        return new String[]{lnt, lat};
      }
    }
    return null;
  }


}
