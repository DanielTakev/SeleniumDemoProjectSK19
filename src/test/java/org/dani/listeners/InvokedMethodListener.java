package org.dani.listeners;

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;

/**
 * Demonstrates the {@link IInvokedMethodListener} interface.
 * <p>
 * This listener is invoked <b>before and after every method</b> — including
 * {@code @BeforeClass}, {@code @BeforeMethod}, {@code @Test}, {@code @AfterMethod},
 * and {@code @AfterClass} methods. This gives much finer-grained control than
 * {@code ITestListener}.
 * </p>
 *
 * <h3>Difference from ITestListener:</h3>
 * <ul>
 *   <li>{@code ITestListener} — only fires for {@code @Test} methods</li>
 *   <li>{@code IInvokedMethodListener} — fires for <b>ALL</b> methods
 *       (config methods + test methods)</li>
 * </ul>
 *
 * <h3>Typical use cases:</h3>
 * <ul>
 *   <li>Logging every method invocation for detailed debugging</li>
 *   <li>Measuring setup/teardown time separately from test time</li>
 *   <li>Adding retry logic for flaky configuration methods</li>
 *   <li>Custom dependency injection before each method</li>
 * </ul>
 */
public class InvokedMethodListener implements IInvokedMethodListener {

    /**
     * Called <b>before</b> each method (both config and test methods).
     *
     * @param method     the method about to be invoked
     * @param testResult the partial test result
     */
    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        String methodType = method.isTestMethod() ? "@Test" : "@Config";
        String methodName = method.getTestMethod().getMethodName();
        String className = testResult.getTestClass().getRealClass().getSimpleName();

        System.out.println("    → [IInvokedMethodListener] BEFORE " + methodType
                + ": " + className + "." + methodName);
    }

    /**
     * Called <b>after</b> each method (both config and test methods).
     *
     * @param method     the method that was just invoked
     * @param testResult the result of the method invocation
     */
    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        String methodType = method.isTestMethod() ? "@Test" : "@Config";
        String methodName = method.getTestMethod().getMethodName();
        String status = getStatusString(testResult.getStatus());

        System.out.println("    ← [IInvokedMethodListener] AFTER " + methodType
                + ": " + methodName + " [" + status + "]");
    }

    private String getStatusString(int status) {
        return switch (status) {
            case ITestResult.SUCCESS -> "PASSED";
            case ITestResult.FAILURE -> "FAILED";
            case ITestResult.SKIP -> "SKIPPED";
            default -> "UNKNOWN";
        };
    }
}
