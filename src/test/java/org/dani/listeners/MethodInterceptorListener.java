package org.dani.listeners;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.testng.IMethodInstance;
import org.testng.IMethodInterceptor;
import org.testng.ITestContext;

/**
 * Demonstrates the {@link IMethodInterceptor} interface.
 * <p>
 * {@code IMethodInterceptor} is called <b>after</b> TestNG determines which test methods
 * to run, but <b>before</b> actually running them. It allows you to:
 * </p>
 * <ul>
 *   <li><b>Reorder</b> test methods (e.g., alphabetically, by priority, by group)</li>
 *   <li><b>Filter out</b> test methods (e.g., skip slow tests in a quick build)</li>
 *   <li><b>Add</b> or <b>remove</b> test methods dynamically</li>
 * </ul>
 *
 * <h3>When it runs:</h3>
 * <pre>
 *   TestNG XML parsed → methods determined → IMethodInterceptor.intercept() → methods executed
 * </pre>
 *
 * <h3>This implementation:</h3>
 * <p>
 * Sorts test methods alphabetically by method name. This demonstrates the concept,
 * though in practice you'd more likely filter by custom annotations or system properties.
 * </p>
 */
public class MethodInterceptorListener implements IMethodInterceptor {

    /**
     * Intercepts the list of test methods and returns a (possibly modified) list.
     * <p>
     * TestNG will execute the methods in the order they appear in the returned list.
     * </p>
     *
     * @param methods the list of test methods about to be run
     * @param context the test context
     * @return the (re-ordered or filtered) list of test methods
     */
    @Override
    public List<IMethodInstance> intercept(List<IMethodInstance> methods, ITestContext context) {
        System.out.println("[IMethodInterceptor] Intercepting " + methods.size() + " test methods");

        // Create a mutable copy (the original list may be unmodifiable)
        List<IMethodInstance> sortedMethods = new ArrayList<>(methods);

        // Sort alphabetically by method name
        sortedMethods.sort(Comparator.comparing(
                m -> m.getMethod().getMethodName()
        ));

        System.out.println("[IMethodInterceptor] Execution order after sorting:");
        for (int i = 0; i < sortedMethods.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + sortedMethods.get(i).getMethod().getMethodName());
        }

        return sortedMethods;
    }
}
