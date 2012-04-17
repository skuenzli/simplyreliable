// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.qualimente.reliability;

import com.qualimente.reliability.Example;
import com.qualimente.reliability.ExampleDataOnDemand;
import com.qualimente.reliability.ExampleIntegrationTest;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect ExampleIntegrationTest_Roo_IntegrationTest {
    
    declare @type: ExampleIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: ExampleIntegrationTest: @ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml");
    
    declare @type: ExampleIntegrationTest: @Transactional;
    
    @Autowired
    private ExampleDataOnDemand ExampleIntegrationTest.dod;
    
    @Test
    public void ExampleIntegrationTest.testCountExamples() {
        Assert.assertNotNull("Data on demand for 'Example' failed to initialize correctly", dod.getRandomExample());
        long count = Example.countExamples();
        Assert.assertTrue("Counter for 'Example' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void ExampleIntegrationTest.testFindExample() {
        Example obj = dod.getRandomExample();
        Assert.assertNotNull("Data on demand for 'Example' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Example' failed to provide an identifier", id);
        obj = Example.findExample(id);
        Assert.assertNotNull("Find method for 'Example' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'Example' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void ExampleIntegrationTest.testFindAllExamples() {
        Assert.assertNotNull("Data on demand for 'Example' failed to initialize correctly", dod.getRandomExample());
        long count = Example.countExamples();
        Assert.assertTrue("Too expensive to perform a find all test for 'Example', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<Example> result = Example.findAllExamples();
        Assert.assertNotNull("Find all method for 'Example' illegally returned null", result);
        Assert.assertTrue("Find all method for 'Example' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void ExampleIntegrationTest.testFindExampleEntries() {
        Assert.assertNotNull("Data on demand for 'Example' failed to initialize correctly", dod.getRandomExample());
        long count = Example.countExamples();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<Example> result = Example.findExampleEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'Example' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'Example' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void ExampleIntegrationTest.testFlush() {
        Example obj = dod.getRandomExample();
        Assert.assertNotNull("Data on demand for 'Example' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Example' failed to provide an identifier", id);
        obj = Example.findExample(id);
        Assert.assertNotNull("Find method for 'Example' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyExample(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'Example' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void ExampleIntegrationTest.testMergeUpdate() {
        Example obj = dod.getRandomExample();
        Assert.assertNotNull("Data on demand for 'Example' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Example' failed to provide an identifier", id);
        obj = Example.findExample(id);
        boolean modified =  dod.modifyExample(obj);
        Integer currentVersion = obj.getVersion();
        Example merged = obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'Example' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void ExampleIntegrationTest.testPersist() {
        Assert.assertNotNull("Data on demand for 'Example' failed to initialize correctly", dod.getRandomExample());
        Example obj = dod.getNewTransientExample(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'Example' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'Example' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        Assert.assertNotNull("Expected 'Example' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void ExampleIntegrationTest.testRemove() {
        Example obj = dod.getRandomExample();
        Assert.assertNotNull("Data on demand for 'Example' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Example' failed to provide an identifier", id);
        obj = Example.findExample(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'Example' with identifier '" + id + "'", Example.findExample(id));
    }
    
}