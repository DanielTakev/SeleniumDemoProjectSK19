package org.dani.listeners;

import org.testng.IHookCallBack;
import org.testng.IHookable;
import org.testng.ITestResult;

/**
 * Demonstrates the {@link IHookable} interface.
 * <p>
 * {@code IHookable} is a powerful interface that allows you to <b>intercept</b>
 * test method invocations. When your test class implements {@code IHookable},
 * TestNG calls {@code run()} instead of directly calling the test method —
 * giving you full control over whether and how the test executes.
 * </p>
 *
 * <h3>How it works:</h3>
 * <pre>
 *   Normal:   TestNG → @Test method
 *   IHookable: TestNG → IHookable.run() → you decide → callBack.runTestMethod()
 * </pre>
 *
 * <h3>Typical use cases:</h3>
 * <ul>
 *   <li>Adding authentication/authorization checks before test execution</li>
 *   <li>Skipping tests based on runtime conditions</li>
 *   <li>Wrapping test execution with additional logic (timing, transactions, etc.)</li>
 *   <li>Implementing custom retry logic per test method</li>
 * </ul>
 *
 * <h3>Important notes:</h3>
 * <ul>
 *   <li>{@code IHookable} is implemented by the <b>test class itself</b> (not as an external listener)</li>
 *   <li>If you don't call {@code callBack.runTestMethod(result)}, the test method will NOT execute</li>
 *   <li>Similar to a servlet filter or AOP "around" advice</li>
 * </ul>
 *
 * <h3>Usage example:</h3>
 * <pre>{@code
 * public class MyTest implements IHookable {
 *     @Override
 *     public void run(IHookCallBack callBack, ITestResult testResult) {
 *         // Pre-test logic
 *         System.out.println("Before test: " + testResult.getMethod().getMethodName());
 *
 *         // Actually run the test (MUST call this, or test won't execute!)
 *         callBack.runTestMethod(testResult);
 *
 *         // Post-test logic
 *         System.out.println("After test: " + testResult.getMethod().getMethodName());
 *     }
 *
 *     @Test
 *     public void myTestMethod() {
 *         // This runs inside callBack.runTestMethod()
 *     }
 * }
 * }</pre>
 *
 * @see IHookCallBack
 */
public class HookableExample implements IHookable {

    /**
     * Intercepts every {@code @Test} method invocation.
     * <p>
     * You MUST call {@code callBack.runTestMethod(testResult)} to actually execute
     * the test. If you skip the callback, the test will be marked as passed
     * without running.
     * </p>
     *
     * @param callBack   used to invoke the actual test method
     * @param testResult the result object for the current test
     */
    @Override
    public void run(IHookCallBack callBack, ITestResult testResult) {
        String methodName = testResult.getMethod().getMethodName();

        System.out.println("[IHookable] Intercepting test: " + methodName);

        // Example: Skip tests that have "skip" in their name
        if (methodName.toLowerCase().contains("skip")) {
            System.out.println("[IHookable] Skipping test: " + methodName);
            testResult.setStatus(ITestResult.SKIP);
            return; // Do NOT call runTestMethod — the test won't execute
        }

        // Run the actual test method
        long start = System.currentTimeMillis();
        callBack.runTestMethod(testResult);
        long duration = System.currentTimeMillis() - start;

        System.out.println("[IHookable] Test " + methodName + " completed in " + duration + " ms");
    }
}
