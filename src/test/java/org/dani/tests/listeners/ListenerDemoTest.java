package org.dani.tests.listeners;

import org.dani.listeners.InvokedMethodListener;
import org.dani.listeners.TestListener;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

/**
 * Test class that demonstrates <b>TestNG Listeners</b> in action.
 * <p>
 * This class uses the {@code @Listeners} annotation to attach listeners.
 * You will see console output from each listener at different lifecycle stages.
 * </p>
 *
 * <h3>Listeners registered on this class:</h3>
 * <ul>
 *   <li>{@link TestListener} — logs test start/pass/fail/skip events ({@code ITestListener})</li>
 *   <li>{@link InvokedMethodListener} — logs before/after EVERY method ({@code IInvokedMethodListener})</li>
 * </ul>
 *
 * <h3>Listeners registered via testng.xml (global):</h3>
 * <ul>
 *   <li>{@code SuiteListener} — suite start/finish ({@code ISuiteListener})</li>
 *   <li>{@code CustomReporter} — custom report after all suites ({@code IReporter})</li>
 *   <li>{@code AnnotationTransformerListener} — modifies annotations at runtime ({@code IAnnotationTransformer})</li>
 *   <li>{@code MethodInterceptorListener} — re-orders test methods ({@code IMethodInterceptor})</li>
 * </ul>
 *
 * <h3>Two ways to register listeners:</h3>
 * <ol>
 *   <li><b>{@code @Listeners} annotation</b> on the test class (as done here)</li>
 *   <li><b>{@code <listeners>} tag</b> in testng.xml (preferred for global listeners)</li>
 * </ol>
 *
 * <p>
 * <b>Note:</b> {@code IAnnotationTransformer} and {@code IMethodInterceptor} should be
 * registered in testng.xml rather than via {@code @Listeners}, because they need to
 * execute before the test class is instantiated.
 * </p>
 */
@Listeners({TestListener.class, InvokedMethodListener.class})
public class ListenerDemoTest {

    /**
     * A simple passing test to observe listener output for a successful test.
     */
    @Test(priority = 1, description = "A test that passes — observe ITestListener.onTestSuccess()")
    public void testThatPasses() {
        System.out.println("      [Test Body] This test will PASS");
        Assert.assertTrue(true, "This should always pass");
    }

    /**
     * A test that intentionally fails to observe listener output for failures.
     * <p>
     * Watch the console — you'll see {@code ITestListener.onTestFailure()} fire,
     * which is where you'd typically take a screenshot.
     * </p>
     */
    @Test(priority = 2, description = "A test that fails — observe ITestListener.onTestFailure()",
          enabled = false) // Disabled by default so the build doesn't fail
    public void testThatFails() {
        System.out.println("      [Test Body] This test will FAIL");
        Assert.fail("Intentional failure to demonstrate ITestListener.onTestFailure()");
    }

    /**
     * A test that depends on a non-existent method, causing it to be skipped.
     * <p>
     * TestNG skips this test because the dependency cannot be satisfied.
     * Watch the console for {@code ITestListener.onTestSkipped()}.
     * </p>
     */
    @Test(priority = 3, description = "A skipped test — observe ITestListener.onTestSkipped()",
          dependsOnMethods = "nonExistentMethod", // This method doesn't exist
          enabled = false) // Disabled so it doesn't cause errors
    public void testThatIsSkipped() {
        System.out.println("      [Test Body] This should be SKIPPED");
    }

    /**
     * Another passing test to observe the execution order after
     * {@code IMethodInterceptor} potentially re-orders methods.
     */
    @Test(priority = 4, description = "Second passing test")
    public void anotherPassingTest() {
        System.out.println("      [Test Body] Another passing test");
        Assert.assertEquals(2 + 2, 4);
    }

    /**
     * Tests basic math — a third passing test to demonstrate listener output
     * across multiple test methods.
     */
    @Test(priority = 5, description = "Third passing test for listener demo")
    public void testBasicMath() {
        System.out.println("      [Test Body] Testing basic math");
        Assert.assertEquals(10 / 2, 5);
    }
}
