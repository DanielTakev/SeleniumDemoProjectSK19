package org.dani.tests.listeners;

import org.dani.listeners.HookableExample;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Demonstrates the {@code IHookable} interface by extending {@link HookableExample}.
 * <p>
 * When a test class implements {@code IHookable}, TestNG calls the {@code run()} method
 * instead of directly invoking each {@code @Test} method. The {@code run()} method decides
 * whether to proceed with the actual test.
 * </p>
 *
 * <h3>In this demo:</h3>
 * <ul>
 *   <li>Tests with "skip" in their name are intercepted and skipped by {@code IHookable}</li>
 *   <li>All other tests are executed normally with timing information</li>
 * </ul>
 *
 * @see HookableExample for the IHookable implementation
 */
public class HookableDemoTest extends HookableExample {

    /**
     * This test runs normally — no "skip" in the method name.
     */
    @Test(priority = 1, description = "Normal test that runs through IHookable")
    public void testNormalExecution() {
        System.out.println("  [Test] Normal test executing...");
        Assert.assertTrue(true);
    }

    /**
     * This test has "skip" in its name, so {@code IHookable} will intercept it
     * and prevent execution.
     */
    @Test(priority = 2, description = "This test contains 'skip' — IHookable will skip it")
    public void testThatShouldBeSkippedByHookable() {
        // This line should never execute because IHookable intercepts it
        System.out.println("  [Test] This should NOT appear in console!");
        Assert.fail("IHookable should have prevented this from running");
    }

    /**
     * Another normal test to show mixed execution.
     */
    @Test(priority = 3, description = "Another normal test that runs through IHookable")
    public void testAnotherNormalExecution() {
        System.out.println("  [Test] Another normal test executing...");
        Assert.assertEquals(1 + 1, 2);
    }
}
