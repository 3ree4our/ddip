package org.threefour.ddip.address.service;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.threefour.ddip.address.domain.Address;

public interface AddressService {
  //Address saveAddress(Address address);

  Address saveAddress(Address address);

  Address getCoordinates(String address);

  String[] getGeocode(String address) throws JSONException;
}
