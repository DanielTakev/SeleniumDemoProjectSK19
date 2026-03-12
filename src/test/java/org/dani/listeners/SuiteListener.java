package org.dani.listeners;

import org.testng.ISuite;
import org.testng.ISuiteListener;

/**
 * Demonstrates the {@link ISuiteListener} interface.
 * <p>
 * {@code ISuiteListener} is called at the <b>suite level</b> — before and after
 * the entire {@code <suite>} tag in testng.xml runs. Each suite can contain
 * multiple {@code <test>} tags.
 * </p>
 *
 * <h3>Difference from ITestListener:</h3>
 * <ul>
 *   <li>{@code ISuiteListener} → executes once per {@code <suite>}</li>
 *   <li>{@code ITestListener}  → executes once per {@code <test>} tag within a suite</li>
 * </ul>
 *
 * <h3>Typical use cases:</h3>
 * <ul>
 *   <li>Setting up shared resources (DB connections, test data) before the suite</li>
 *   <li>Generating custom reports after the suite</li>
 *   <li>Sending summary emails/notifications</li>
 *   <li>Cleaning up resources after all tests complete</li>
 * </ul>
 */
public class SuiteListener implements ISuiteListener {

    /**
     * Called before any test in the suite runs.
     *
     * @param suite provides access to suite parameters, tests, and configuration
     */
    @Override
    public void onStart(ISuite suite) {
        System.out.println("╔═══════════════════════════════════════════════════════╗");
        System.out.println("║ [ISuiteListener] SUITE STARTED: " + suite.getName());
        System.out.println("║ Suite XML File: " + suite.getXmlSuite().getFileName());
        System.out.println("║ Parallel mode: " + suite.getXmlSuite().getParallel());
        System.out.println("╚═══════════════════════════════════════════════════════╝");
    }

    /**
     * Called after all tests in the suite have run.
     *
     * @param suite provides the results of the entire suite
     */
    @Override
    public void onFinish(ISuite suite) {
        System.out.println("╔═══════════════════════════════════════════════════════╗");
        System.out.println("║ [ISuiteListener] SUITE FINISHED: " + suite.getName());
        System.out.println("║ Results: " + suite.getResults().size() + " test(s) executed");
        System.out.println("╚═══════════════════════════════════════════════════════╝");
    }
}
