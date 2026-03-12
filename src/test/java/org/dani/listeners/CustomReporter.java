package org.dani.listeners;

import java.util.List;
import java.util.Map;

import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.xml.XmlSuite;

/**
 * Demonstrates the {@link IReporter} interface.
 * <p>
 * {@code IReporter} is called <b>after all suites have completed</b>.
 * It receives the complete results and is designed for generating custom reports.
 * </p>
 *
 * <h3>When it runs:</h3>
 * <pre>
 *   All suites finish → IReporter.generateReport() called once
 * </pre>
 *
 * <h3>Typical use cases:</h3>
 * <ul>
 *   <li>Generating custom HTML/JSON/CSV reports</li>
 *   <li>Sending test summary emails</li>
 *   <li>Publishing results to a dashboard (Grafana, Kibana, etc.)</li>
 *   <li>Updating JIRA/TestRail with test results</li>
 * </ul>
 *
 * <h3>Note:</h3>
 * <p>
 * Unlike {@code ITestListener} and {@code ISuiteListener}, the {@code IReporter}
 * receives results from <b>all suites at once</b> — making it suitable for
 * aggregate reporting.
 * </p>
 */
public class CustomReporter implements IReporter {

    /**
     * Called after all suites have finished running.
     *
     * @param xmlSuites  the XML suite definitions
     * @param suites     the suite results
     * @param outputDirectory the TestNG output directory (e.g., {@code test-output/})
     */
    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
        System.out.println();
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║            [IReporter] CUSTOM TEST REPORT                 ║");
        System.out.println("╠═══════════════════════════════════════════════════════════╣");

        int totalPassed = 0;
        int totalFailed = 0;
        int totalSkipped = 0;

        for (ISuite suite : suites) {
            System.out.println("║ Suite: " + suite.getName());

            Map<String, ISuiteResult> results = suite.getResults();
            for (ISuiteResult suiteResult : results.values()) {
                ITestContext context = suiteResult.getTestContext();

                int passed = context.getPassedTests().size();
                int failed = context.getFailedTests().size();
                int skipped = context.getSkippedTests().size();

                totalPassed += passed;
                totalFailed += failed;
                totalSkipped += skipped;

                System.out.println("║   Test: " + context.getName());
                System.out.println("║     Passed:  " + passed);
                System.out.println("║     Failed:  " + failed);
                System.out.println("║     Skipped: " + skipped);
            }
        }

        System.out.println("╠═══════════════════════════════════════════════════════════╣");
        System.out.println("║ TOTAL: " + totalPassed + " passed | "
                + totalFailed + " failed | " + totalSkipped + " skipped");
        System.out.println("║ Output directory: " + outputDirectory);
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
        System.out.println();
    }
}
