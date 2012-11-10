package com.qualimente.reliability.web;

import com.qualimente.reliability.Example;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("/examples")
@Controller
@RooWebScaffold(path = "examples", formBackingObject = Example.class)
@RooWebJson(jsonObject = Example.class)
public class ExampleController {

  @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
  public ResponseEntity<String> createFromJson(@RequestBody String json) {

    Example example = Example.fromJsonToExample(json);
    example.persist();
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json");
    return new ResponseEntity<String>(headers, HttpStatus.CREATED);
  }

}
