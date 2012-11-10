package com.qualimente.reliability;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class ExampleTest {

  @Test
  public void should_roundtrip_basic_json(){
    String basicExampleJson = "{\"id\":1,\"version\":2}";
    Example example = Example.fromJsonToExample(basicExampleJson);

    assertNotNull(example);
    assertEquals(Long.valueOf(1), example.getId());
    assertEquals(Integer.valueOf(2), example.getVersion());

    assertEquals(basicExampleJson, example.toJson());
  }

}
