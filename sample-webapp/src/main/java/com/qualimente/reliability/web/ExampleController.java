package com.qualimente.reliability.web;

import com.qualimente.reliability.Example;
import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/examples")
@Controller
@RooWebScaffold(path = "examples", formBackingObject = Example.class)
@RooWebJson(jsonObject = Example.class)
public class ExampleController {
  private static final Logger LOGGER = Logger.getLogger(ExampleController.class);

  @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
  public ResponseEntity<String> createFromJson(@RequestBody String json) {
    LOGGER.info("creating Example from: " + json);
    Example example = Example.fromJsonToExample(json);

    exampleService.saveExample(example);

    LOGGER.info("persisted Example: " + example.toJson());
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json");
    return new ResponseEntity<String>(headers, HttpStatus.CREATED);
  }


  @RequestMapping(value = "/{id}", headers = "Accept=application/json")
  @ResponseBody
  public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
    LOGGER.info("requested Example: " + id);

    Example example = exampleService.findExample(id);

    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json; charset=utf-8");
    if (example == null) {
      return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
    } else {
      LOGGER.info("returning Example: " + example.toJson());
      return new ResponseEntity<String>(example.toJson(), headers, HttpStatus.OK);
    }
  }

  @RequestMapping(headers = "Accept=application/json")
  @ResponseBody
  public ResponseEntity<String> listJson() {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json; charset=utf-8");

    List<Example> result = exampleService.findAllExamples();

    return new ResponseEntity<String>(Example.toJsonArray(result), headers, HttpStatus.OK);
  }

}

