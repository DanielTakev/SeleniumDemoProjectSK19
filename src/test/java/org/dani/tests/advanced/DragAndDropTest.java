package org.dani.tests.advanced;

import org.dani.tests.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Demonstrates <b>Drag and Drop</b> operations using Selenium's {@link Actions} class.
 * <p>
 * The {@link Actions} class provides a fluent API for complex user gestures:
 * mouse movements, drag-and-drop, double-click, right-click, key combos, etc.
 * </p>
 *
 * <h3>Key Actions methods for Drag &amp; Drop:</h3>
 * <ul>
 *   <li>{@code dragAndDrop(source, target)} — drags the source element and drops it on the target</li>
 *   <li>{@code dragAndDropBy(source, xOffset, yOffset)} — drags by pixel offset</li>
 *   <li>{@code clickAndHold(element).moveToElement(target).release()} — manual drag sequence</li>
 * </ul>
 *
 * <h3>Note:</h3>
 * <p>
 * This test uses a public demo page specifically designed for drag-and-drop testing.
 * </p>
 */
public class DragAndDropTest extends BaseTest {

    /**
     * Public demo page with drag-and-drop columns.
     */
    private static final String DRAG_DROP_URL = "https://the-internet.herokuapp.com/drag_and_drop";

    @BeforeClass
    @Override
    public void setUp() {
        super.setUp();
        driver.get(DRAG_DROP_URL);
    }

    // -----------------------------------------------------------------------
    // 1. Drag and Drop using Actions.dragAndDrop()
    // -----------------------------------------------------------------------

    /**
     * Drags column A onto column B using the built-in {@code dragAndDrop()} method.
     * <p>
     * This is the simplest and most readable approach. Under the hood it performs:
     * click-and-hold on source → move to target → release.
     * </p>
     *
     * <b>Note:</b> Some web apps use HTML5 drag-and-drop which may not work with
     * the native {@code Actions.dragAndDrop()}. In those cases, a JavaScript-based
     * workaround is needed (see test #3).
     */
    @Test(priority = 1, description = "Drag and drop using Actions.dragAndDrop()")
    public void testDragAndDropWithActions() throws InterruptedException {
        WebElement columnA = driver.findElement(By.cssSelector("#column-a"));
        WebElement columnB = driver.findElement(By.cssSelector("#column-b"));

        String headerBefore = columnA.findElement(By.tagName("header")).getText();
        System.out.println("Column A header before drag: " + headerBefore);

        // Perform drag and drop
        Actions actions = new Actions(driver);
        actions.dragAndDrop(columnA, columnB).perform();

        // After the drop, column A's header should have changed
        String headerAfter = driver.findElement(By.cssSelector("#column-a header")).getText();
        System.out.println("Column A header after drag: " + headerAfter);
        // The columns should have swapped
        Assert.assertNotEquals(headerBefore, headerAfter,
                "Column headers should swap after drag and drop");
    }

    // -----------------------------------------------------------------------
    // 2. Drag and Drop using click-hold-move-release (manual sequence)
    // -----------------------------------------------------------------------

    /**
     * Performs drag and drop as a series of discrete steps.
     * <p>
     * This approach gives more control and can be helpful for debugging
     * or for adding pauses between steps.
     * </p>
     *
     * <pre>
     * clickAndHold(source)  → picks up the element
     * moveToElement(target) → moves it over the target
     * release()             → drops it
     * perform()             → executes the whole chain
     * </pre>
     */
    @Test(priority = 2, description = "Drag and drop with click-hold-move-release sequence")
    public void testDragAndDropManualSequence() throws InterruptedException {
        // Refresh the page to reset positions
        driver.navigate().refresh();

        WebElement source = driver.findElement(By.cssSelector("#column-a"));
        WebElement target = driver.findElement(By.cssSelector("#column-b"));

        String headerBefore = source.findElement(By.tagName("header")).getText();

        // Manual drag-and-drop sequence
        Actions actions = new Actions(driver);
        actions.clickAndHold(source).moveToElement(target).pause(2000).release().perform();

        String headerAfter = driver.findElement(By.cssSelector("#column-a header")).getText();
        System.out.println("Before: " + headerBefore + " | After: " + headerAfter);
    }

    // -----------------------------------------------------------------------
    // 3. Drag and Drop via JavaScript (HTML5 workaround)
    // -----------------------------------------------------------------------

    /**
     * Uses JavaScript to simulate drag-and-drop for HTML5 {@code draggable} elements.
     * <p>
     * Selenium's native {@code Actions.dragAndDrop()} sometimes fails with HTML5
     * drag-and-drop API because the browser requires specific {@code DataTransfer}
     * events that Selenium doesn't generate.
     * </p>
     * <p>
     * This JS-based approach creates and dispatches the correct {@code dragstart},
     * {@code dragover}, and {@code drop} events manually.
     * </p>
     */
    @Test(priority = 3, description = "Drag and drop via JavaScript (HTML5 workaround)")
    public void testDragAndDropViaJavaScript() {
        // Refresh to reset state
        driver.navigate().refresh();

        // JavaScript that dispatches HTML5 drag events
        String jsScript =
                "function simulateDragDrop(sourceNode, destinationNode) {" +
                "    var EVENT_TYPES = { DRAG_END: 'dragend', DRAG_START: 'dragstart'," +
                "        DROP: 'drop', DRAG_OVER: 'dragover' };" +
                "    function createCustomEvent(type) {" +
                "        var event = new CustomEvent('Event');  " +
                "        event.initEvent(type, true, true);     " +
                "        event.dataTransfer = { data: {}, setData: function(type, val) {" +
                "            this.data[type] = val; }, getData: function(type) {" +
                "            return this.data[type]; } };" +
                "        return event;" +
                "    }" +
                "    var dragStartEvent = createCustomEvent(EVENT_TYPES.DRAG_START);" +
                "    sourceNode.dispatchEvent(dragStartEvent);" +
                "    var dropEvent = createCustomEvent(EVENT_TYPES.DROP);" +
                "    dropEvent.dataTransfer = dragStartEvent.dataTransfer;" +
                "    destinationNode.dispatchEvent(dropEvent);" +
                "    var dragEndEvent = createCustomEvent(EVENT_TYPES.DRAG_END);" +
                "    dragEndEvent.dataTransfer = dragStartEvent.dataTransfer;" +
                "    sourceNode.dispatchEvent(dragEndEvent);" +
                "}" +
                "simulateDragDrop(arguments[0], arguments[1]);";

        WebElement source = driver.findElement(By.cssSelector("#column-a"));
        WebElement target = driver.findElement(By.cssSelector("#column-b"));

        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(jsScript, source, target);

        System.out.println("HTML5 drag and drop executed via JavaScript");
    }

    // -----------------------------------------------------------------------
    // 4. Drag and Drop by offset
    // -----------------------------------------------------------------------

    /**
     * Drags an element by a specific pixel offset rather than to a target element.
     * <p>
     * Useful when you need to drag sliders, resize handles, or move elements
     * to specific positions on the page.
     * </p>
     */
    @Test(priority = 4, description = "Drag and drop by pixel offset")
    public void testDragAndDropByOffset() {
        driver.navigate().refresh();

        WebElement source = driver.findElement(By.cssSelector("#column-a"));

        // Get the position difference between columns
        WebElement target = driver.findElement(By.cssSelector("#column-b"));
        int xOffset = target.getLocation().getX() - source.getLocation().getX();
        int yOffset = target.getLocation().getY() - source.getLocation().getY();

        System.out.println("Dragging by offset: x=" + xOffset + ", y=" + yOffset);

        Actions actions = new Actions(driver);
        actions.dragAndDropBy(source, xOffset, yOffset).perform();

        System.out.println("Drag by offset completed");
    }
}
