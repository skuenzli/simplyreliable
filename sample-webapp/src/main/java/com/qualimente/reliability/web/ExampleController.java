package com.qualimente.reliability.web;

import com.qualimente.reliability.Example;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/examples")
@Controller
@RooWebScaffold(path = "examples", formBackingObject = Example.class)
public class ExampleController {
}
