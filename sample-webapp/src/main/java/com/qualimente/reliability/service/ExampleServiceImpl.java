package com.qualimente.reliability.service;


import com.qualimente.reliability.Example;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExampleServiceImpl implements ExampleService {

  private static final long STANDARD_RPC_LATENCY = 50;

  private final ExecutorService executorService = Executors.newFixedThreadPool(10);

  public Example findExample(Long id) {
    simulateRpcToSharedResource();
    return Example.findExample(id);
  }

  public List<Example> findAllExamples() {
    simulateRpcToSharedResource();
    return Example.findAllExamples();
  }

  public void saveExample(Example example) {
    simulateRpcToSharedResource();
    example.persist();
  }

  public Example updateExample(Example example) {
    simulateRpcToSharedResource();
    return example.merge();
  }

  private void simulateRpcToSharedResource() {
    try {
      Future<Object> rpcFuture = executorService.submit(new ReliableRPCSimulator(STANDARD_RPC_LATENCY));
      rpcFuture.get();
    } catch (Exception e){
      throw new RuntimeException(e);
    }
  }

  private static final class ReliableRPCSimulator implements Callable<Object> {

    private final long simulatedLatency;

    ReliableRPCSimulator(long simulatedLatency){
      this.simulatedLatency = simulatedLatency;
    }

    public Object call() throws Exception {
      Thread.sleep(simulatedLatency);
      return null;
    }

  }

}
