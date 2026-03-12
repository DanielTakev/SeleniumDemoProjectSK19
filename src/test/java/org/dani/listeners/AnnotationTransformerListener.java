package org.dani.listeners;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

/**
 * Demonstrates the {@link IAnnotationTransformer} interface.
 * <p>
 * {@code IAnnotationTransformer} allows you to <b>modify TestNG annotations at runtime</b>,
 * before tests are executed. This is incredibly powerful — you can change test behavior
 * without modifying the source code.
 * </p>
 *
 * <h3>What you can modify:</h3>
 * <ul>
 *   <li>Enable/disable tests dynamically</li>
 *   <li>Change {@code invocationCount} (run a test multiple times)</li>
 *   <li>Add/change {@code dependsOnMethods} or {@code dependsOnGroups}</li>
 *   <li>Modify {@code timeOut}, {@code priority}, {@code description}</li>
 *   <li>Add a {@code retryAnalyzer} to flaky tests</li>
 *   <li>Change {@code dataProvider} name or class</li>
 * </ul>
 *
 * <h3>Registration:</h3>
 * <p>
 * Unlike other listeners, {@code IAnnotationTransformer} can ONLY be registered in
 * {@code testng.xml} or programmatically — NOT via {@code @Listeners} annotation
 * (because it needs to run before annotation processing).
 * </p>
 *
 * <h3>This implementation:</h3>
 * <p>
 * Adds a retry analyzer to all test methods and sets a default timeout.
 * </p>
 */
public class AnnotationTransformerListener implements IAnnotationTransformer {

    /**
     * Called for every {@code @Test} annotation before the test class is instantiated.
     *
     * @param annotation  the {@code @Test} annotation that can be modified
     * @param testClass   the test class (if annotated at class level)
     * @param testConstructor the test constructor (if applicable)
     * @param testMethod  the test method (if annotated at method level)
     */
    @Override
    public void transform(ITestAnnotation annotation, Class testClass,
                          Constructor testConstructor, Method testMethod) {
        if (testMethod != null) {
            System.out.println("[IAnnotationTransformer] Transforming: "
                    + testMethod.getDeclaringClass().getSimpleName() + "." + testMethod.getName());

            // Example 1: Set a default timeout of 30 seconds for all tests
            // (only if not already set — 0 means no timeout was specified)
            if (annotation.getTimeOut() == 0) {
                annotation.setTimeOut(30000); // 30 seconds
                System.out.println("  → Set timeout to 30s");
            }

            // Example 2: Add retry analyzer for all tests
            // (Uncomment the line below to enable retries)
            // annotation.setRetryAnalyzerClass(RetryAnalyzer.class);
        }
    }
}
