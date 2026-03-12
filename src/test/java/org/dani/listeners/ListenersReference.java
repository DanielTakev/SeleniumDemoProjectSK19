package org.dani.listeners;

/**
 * <h2>TestNG Listeners — Complete Reference</h2>
 * <p>
 * TestNG provides several listener interfaces that let you hook into the test lifecycle.
 * This package contains working examples of the most commonly used listeners.
 * </p>
 *
 * <h3>Listener Summary Table</h3>
 * <table border="1" cellpadding="8">
 *   <tr>
 *     <th>Interface</th>
 *     <th>Scope</th>
 *     <th>When it fires</th>
 *     <th>Example Class</th>
 *   </tr>
 *   <tr>
 *     <td><b>ITestListener</b></td>
 *     <td>Per test method</td>
 *     <td>Before/after each @Test, on pass/fail/skip</td>
 *     <td>{@link TestListener}</td>
 *   </tr>
 *   <tr>
 *     <td><b>ISuiteListener</b></td>
 *     <td>Per suite</td>
 *     <td>Before/after the entire &lt;suite&gt; tag</td>
 *     <td>{@link SuiteListener}</td>
 *   </tr>
 *   <tr>
 *     <td><b>IInvokedMethodListener</b></td>
 *     <td>Per method (ALL)</td>
 *     <td>Before/after every method (@Test + @Before/@After)</td>
 *     <td>{@link InvokedMethodListener}</td>
 *   </tr>
 *   <tr>
 *     <td><b>IMethodInterceptor</b></td>
 *     <td>Per test tag</td>
 *     <td>After method selection, before execution (reorder/filter)</td>
 *     <td>{@link MethodInterceptorListener}</td>
 *   </tr>
 *   <tr>
 *     <td><b>IReporter</b></td>
 *     <td>Global</td>
 *     <td>After ALL suites finish (for custom reporting)</td>
 *     <td>{@link CustomReporter}</td>
 *   </tr>
 *   <tr>
 *     <td><b>IAnnotationTransformer</b></td>
 *     <td>Per annotation</td>
 *     <td>Before tests run — modifies @Test annotations at runtime</td>
 *     <td>{@link AnnotationTransformerListener}</td>
 *   </tr>
 *   <tr>
 *     <td><b>IHookable</b></td>
 *     <td>Per test method</td>
 *     <td>Wraps each @Test invocation (must be implemented by test class)</td>
 *     <td>{@link HookableExample}</td>
 *   </tr>
 * </table>
 *
 * <h3>Registration Methods</h3>
 * <table border="1" cellpadding="8">
 *   <tr>
 *     <th>Method</th>
 *     <th>Works for</th>
 *     <th>Example</th>
 *   </tr>
 *   <tr>
 *     <td>testng.xml {@code <listeners>}</td>
 *     <td>ALL listeners</td>
 *     <td>{@code <listener class-name="org.dani.listeners.TestListener"/>}</td>
 *   </tr>
 *   <tr>
 *     <td>{@code @Listeners} annotation</td>
 *     <td>All except IAnnotationTransformer</td>
 *     <td>{@code @Listeners(TestListener.class)}</td>
 *   </tr>
 *   <tr>
 *     <td>{@code ServiceLoader} (SPI)</td>
 *     <td>ALL listeners</td>
 *     <td>META-INF/services/org.testng.ITestNGListener</td>
 *   </tr>
 *   <tr>
 *     <td>Programmatic (TestNG API)</td>
 *     <td>ALL listeners</td>
 *     <td>{@code testNG.addListener(new TestListener())}</td>
 *   </tr>
 * </table>
 *
 * <h3>Lifecycle order (simplified)</h3>
 * <pre>
 * 1. IAnnotationTransformer.transform()    — modify annotations
 * 2. ISuiteListener.onStart()               — suite begins
 * 3. IMethodInterceptor.intercept()         — reorder/filter methods
 * 4. ITestListener.onStart()                — test tag begins
 * 5.   IInvokedMethodListener.beforeInvocation()  — before @BeforeClass
 * 6.   @BeforeClass runs
 * 7.   IInvokedMethodListener.afterInvocation()   — after @BeforeClass
 * 8.     IInvokedMethodListener.beforeInvocation() — before @Test
 * 9.     IHookable.run() → @Test method runs
 * 10.    IInvokedMethodListener.afterInvocation()  — after @Test
 * 11.    ITestListener.onTestSuccess/Failure/Skip()
 * 12.  IInvokedMethodListener.beforeInvocation()  — before @AfterClass
 * 13.  @AfterClass runs
 * 14.  IInvokedMethodListener.afterInvocation()   — after @AfterClass
 * 15. ITestListener.onFinish()              — test tag ends
 * 16. ISuiteListener.onFinish()             — suite ends
 * 17. IReporter.generateReport()            — generate custom reports
 * </pre>
 *
 * <h3>IAnnotationTransformer vs IAnnotationTransformer2</h3>
 * <p>
 * In TestNG 6.x, there were separate interfaces:
 * </p>
 * <ul>
 *   <li>{@code IAnnotationTransformer} — transformed only {@code @Test} annotations</li>
 *   <li>{@code IAnnotationTransformer2} — also transformed {@code @DataProvider},
 *       {@code @Factory}, and configuration annotations</li>
 * </ul>
 * <p>
 * In <b>TestNG 7+</b>, {@code IAnnotationTransformer2} has been <b>merged</b> into
 * {@code IAnnotationTransformer}. The single {@code IAnnotationTransformer} interface
 * now has default methods for all annotation types. You only need to implement
 * the one interface.
 * </p>
 */
public final class ListenersReference {
    private ListenersReference() {
        // Utility class — not instantiable
    }
}
