package org.dani.listeners;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * Demonstrates the {@link ITestListener} interface — the most commonly used TestNG listener.
 * <p>
 * {@code ITestListener} provides callbacks at various points in the test lifecycle:
 * </p>
 *
 * <h3>Method call order for a single test:</h3>
 * <pre>
 *   onStart(context)          → suite/test tag starts
 *     onTestStart(result)     → individual test method starts
 *     onTestSuccess(result)   → test passed
 *     onTestFailure(result)   → test failed
 *     onTestSkipped(result)   → test was skipped
 *   onFinish(context)         → suite/test tag finished
 * </pre>
 *
 * <h3>Typical use cases:</h3>
 * <ul>
 *   <li>Logging test execution status</li>
 *   <li>Taking screenshots on failure (see also {@code ScreenshotOnFailureListener})</li>
 *   <li>Measuring test execution time</li>
 *   <li>Sending notifications (email, Slack) on failure</li>
 *   <li>Updating external test management tools</li>
 * </ul>
 *
 * <h3>How to register this listener:</h3>
 * <ol>
 *   <li>In {@code testng.xml}:
 *       <pre>{@code <listeners><listener class-name="org.dani.listeners.TestListener"/></listeners>}</pre></li>
 *   <li>Via annotation on the test class:
 *       <pre>{@code @Listeners(TestListener.class)}</pre></li>
 * </ol>
 */
public class TestListener implements ITestListener {

    /**
     * Called when the {@code <test>} tag starts (before any test methods).
     *
     * @param context provides access to all the information about the test run
     */
    @Override
    public void onStart(ITestContext context) {
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("  [ITestListener] TEST SUITE STARTED: " + context.getName());
        System.out.println("  Start time: " + context.getStartDate());
        System.out.println("═══════════════════════════════════════════════════════");
    }

    /**
     * Called when the {@code <test>} tag finishes (after all test methods).
     *
     * @param context provides the test results summary
     */
    @Override
    public void onFinish(ITestContext context) {
        int passed = context.getPassedTests().size();
        int failed = context.getFailedTests().size();
        int skipped = context.getSkippedTests().size();

        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("  [ITestListener] TEST SUITE FINISHED: " + context.getName());
        System.out.println("  Results: " + passed + " passed | " + failed + " failed | " + skipped + " skipped");
        System.out.println("  End time: " + context.getEndDate());
        System.out.println("═══════════════════════════════════════════════════════");
    }

    /**
     * Called each time a test method starts.
     *
     * @param result contains info about the test method being executed
     */
    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("  ▶ [ITestListener] TEST STARTED: " + getTestMethodName(result));
    }

    /**
     * Called when a test method passes.
     *
     * @param result contains the test result and timing information
     */
    @Override
    public void onTestSuccess(ITestResult result) {
        long durationMs = result.getEndMillis() - result.getStartMillis();
        System.out.println("  ✔ [ITestListener] TEST PASSED: " + getTestMethodName(result)
                + " (" + durationMs + " ms)");
    }

    /**
     * Called when a test method fails.
     * <p>
     * This is the ideal place to take screenshots, log failure details,
     * or trigger alerts.
     * </p>
     *
     * @param result contains the exception that caused the failure
     */
    @Override
    public void onTestFailure(ITestResult result) {
        long durationMs = result.getEndMillis() - result.getStartMillis();
        System.out.println("  ✘ [ITestListener] TEST FAILED: " + getTestMethodName(result)
                + " (" + durationMs + " ms)");
        System.out.println("    Reason: " + result.getThrowable().getMessage());
    }

    /**
     * Called when a test method is skipped (e.g., due to a dependency failure).
     *
     * @param result contains info about the skipped test
     */
    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("  ⊘ [ITestListener] TEST SKIPPED: " + getTestMethodName(result));
        if (result.getThrowable() != null) {
            System.out.println("    Reason: " + result.getThrowable().getMessage());
        }
    }

    /**
     * Called when a test method fails but is within the allowed failure percentage.
     *
     * @param result the test result
     */
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        System.out.println("  ~ [ITestListener] TEST FAILED (within success %): "
                + getTestMethodName(result));
    }

    /**
     * Helper to get a readable test method name including the class.
     */
    private String getTestMethodName(ITestResult result) {
        return result.getTestClass().getRealClass().getSimpleName()
                + "." + result.getMethod().getMethodName();
    }
}
