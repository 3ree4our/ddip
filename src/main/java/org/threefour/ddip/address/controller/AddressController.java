/*
package org.threefour.ddip.address.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.json.JSONException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.threefour.ddip.address.service.AddressService;

@Controller
@RequestMapping("/geo")
public class AddressController {

  @Autowired
  private AddressService addressService;

  @GetMapping("/geocode")
  public String getGeocode(@RequestParam("address") String address, Model model) throws JSONException {
    String[] coordinates = addressService.getGeocode(address);
    //System.out.println("@@coordinate: " + coordinates[0]);
    if(coordinates != null){
      model.addAttribute("latitude", coordinates[0]);
      model.addAttribute("longitude", coordinates[1]);
    }else{
      model.addAttribute("error", "Geocoding failed");
    }
    return "geocode";
  }



}
*/
