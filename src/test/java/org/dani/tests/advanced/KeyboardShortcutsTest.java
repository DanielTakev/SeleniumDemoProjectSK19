package org.dani.tests.advanced;

import org.dani.pages.LoginPage;
import org.dani.tests.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Demonstrates <b>keyboard shortcuts and key interactions</b> using Selenium.
 * <p>
 * There are several ways to send keyboard input in Selenium:
 * </p>
 *
 * <h3>1. {@code WebElement.sendKeys()}</h3>
 * <p>
 * Sends keys directly to a specific element. Supports special keys via
 * the {@link Keys} enum (ENTER, TAB, ESCAPE, CONTROL, SHIFT, etc.).
 * </p>
 *
 * <h3>2. {@link Actions} class</h3>
 * <p>
 * The {@link Actions} builder provides fine-grained keyboard control:
 * </p>
 * <ul>
 *   <li>{@code keyDown(Keys.CONTROL)} / {@code keyUp(Keys.CONTROL)} — hold/release a modifier</li>
 *   <li>{@code sendKeys(Keys.ENTER)} — press a key (without targeting a specific element)</li>
 *   <li>Combine with mouse actions for complex gestures (e.g., Ctrl+Click)</li>
 * </ul>
 *
 * <h3>Common keyboard shortcuts:</h3>
 * <table border="1">
 *   <tr><th>Action</th><th>Windows/Linux</th><th>macOS</th><th>Selenium Code</th></tr>
 *   <tr><td>Select All</td><td>Ctrl+A</td><td>Cmd+A</td><td>{@code Keys.chord(Keys.CONTROL, "a")}</td></tr>
 *   <tr><td>Copy</td><td>Ctrl+C</td><td>Cmd+C</td><td>{@code Keys.chord(Keys.CONTROL, "c")}</td></tr>
 *   <tr><td>Paste</td><td>Ctrl+V</td><td>Cmd+V</td><td>{@code Keys.chord(Keys.CONTROL, "v")}</td></tr>
 *   <tr><td>Tab</td><td colspan="2">Tab</td><td>{@code Keys.TAB}</td></tr>
 *   <tr><td>Enter</td><td colspan="2">Enter</td><td>{@code Keys.ENTER}</td></tr>
 *   <tr><td>Escape</td><td colspan="2">Escape</td><td>{@code Keys.ESCAPE}</td></tr>
 * </table>
 *
 * <h3>Important note for macOS:</h3>
 * <p>
 * On macOS, keyboard shortcuts typically use the Command key ({@code Keys.COMMAND})
 * instead of {@code Keys.CONTROL}. However, Selenium WebDriver with ChromeDriver
 * generally maps {@code Keys.CONTROL} correctly on macOS for most shortcuts.
 * </p>
 */
public class KeyboardShortcutsTest extends BaseTest {

    @BeforeClass
    @Override
    public void setUp() {
        super.setUp();
        // Navigate to login page where we have input fields to work with
        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateToPage();
    }

    // -----------------------------------------------------------------------
    // 1. Send basic keys to an input field
    // -----------------------------------------------------------------------

    /**
     * Types text into a field and presses ENTER using {@code sendKeys()}.
     * <p>
     * {@link Keys#ENTER} and {@link Keys#RETURN} both simulate pressing
     * the Enter key. They are interchangeable in most cases.
     * </p>
     */
    @Test(priority = 1, description = "Type text and press ENTER via sendKeys()")
    public void testSendKeysToInputField() throws InterruptedException {
        WebElement usernameField = driver.findElement(By.cssSelector("#defaultLoginFormUsername"));
        usernameField.clear();

        // Type text followed by ENTER
        usernameField.sendKeys("testUser");
        usernameField.sendKeys(Keys.TAB); // Move focus to the next field

        System.out.println("Typed 'testUser' and pressed TAB");

        WebElement passwordField = driver.findElement(By.cssSelector("#defaultLoginFormPassword"));
        boolean isFocused = passwordField.equals(driver.switchTo().activeElement());
        System.out.println("Password field is focused: " + isFocused);
        boolean isFocused2 = usernameField.equals(driver.switchTo().activeElement());
        System.out.println("Input field is focused: " + isFocused2);
    }

    // -----------------------------------------------------------------------
    // 2. Select All + Delete using key chord
    // -----------------------------------------------------------------------

    /**
     * Uses {@link Keys#chord(CharSequence...)} to perform Ctrl+A (Select All),
     * then deletes the selection.
     * <p>
     * {@code Keys.chord()} creates a single string with all specified keys pressed
     * simultaneously, then released. Think of it as pressing a keyboard shortcut.
     * </p>
     */
    @Test(priority = 2, description = "Select all text with Ctrl+A and delete it")
    public void testSelectAllAndDelete() {
        WebElement usernameField = driver.findElement(By.cssSelector("#defaultLoginFormUsername"));
        usernameField.clear();
        usernameField.sendKeys("TextToBeDeleted");

        // Select all text with Ctrl+A (or Cmd+A on Mac — Selenium handles this)
        usernameField.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        // Delete the selected text
        usernameField.sendKeys(Keys.DELETE);

        String remainingText = usernameField.getAttribute("value");
        System.out.println("Remaining text after select-all + delete: '" + remainingText + "'");
    }

    // -----------------------------------------------------------------------
    // 3. Copy and Paste using keyboard shortcuts
    // -----------------------------------------------------------------------

    /**
     * Demonstrates Copy (Ctrl+C) and Paste (Ctrl+V) using sendKeys().
     * <p>
     * Copies text from the username field and pastes it into the password field.
     * </p>
     */
    @Test(priority = 3, description = "Copy text from one field and paste into another")
    public void testCopyAndPaste() throws InterruptedException {
        Thread.sleep(2444);
        WebElement usernameField = driver.findElement(By.cssSelector("#defaultLoginFormUsername"));
        WebElement passwordField = driver.findElement(By.cssSelector("#defaultLoginFormPassword"));

        usernameField.clear();
        passwordField.clear();

        // Type text in username field
        usernameField.sendKeys("CopyThisText");
        Thread.sleep(4444);

        // Select all and copy
        usernameField.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        Thread.sleep(444);

        usernameField.sendKeys(Keys.chord(Keys.CONTROL, "c"));
        Thread.sleep(444);

        usernameField.clear();
        Thread.sleep(4444);
        // Click on username field and paste
        usernameField.click();
        usernameField.sendKeys(Keys.chord(Keys.CONTROL, "v"));
        Thread.sleep(4444);

        String pastedText = usernameField.getAttribute("value");
        System.out.println("Pasted text: " + pastedText);
        Assert.assertEquals(pastedText, "CopyThisText",
                "Password field should contain the pasted text");
    }

    // -----------------------------------------------------------------------
    // 4. Using Actions class for keyboard interactions
    // -----------------------------------------------------------------------

    /**
     * Uses the {@link Actions} class for keyboard shortcuts.
     * <p>
     * The Actions class is more flexible than {@code sendKeys()} because it
     * allows you to:
     * <ul>
     *   <li>Hold down modifier keys across multiple actions</li>
     *   <li>Combine keyboard and mouse actions</li>
     *   <li>Send keys without targeting a specific element</li>
     * </ul>
     * </p>
     */
    @Test(priority = 4, description = "Keyboard shortcuts using Actions class")
    public void testActionsKeyboard() {
        WebElement usernameField = driver.findElement(By.cssSelector("#defaultLoginFormUsername"));
        usernameField.clear();
        usernameField.sendKeys("ActionsExample");

        Actions actions = new Actions(driver);

        // Using keyDown/keyUp for Ctrl+A (select all)
        actions.click(usernameField)
               .keyDown(Keys.CONTROL)
               .sendKeys("a")
               .keyUp(Keys.CONTROL)
               .perform();

        System.out.println("Selected all text using Actions keyDown/keyUp");

        // Type replacement text
        actions.sendKeys("ReplacedText").perform();

        String newText = usernameField.getAttribute("value");
        System.out.println("Text after replacement: " + newText);
    }

    // -----------------------------------------------------------------------
    // 5. Press ESCAPE key (global action)
    // -----------------------------------------------------------------------

    /**
     * Sends the ESCAPE key to dismiss dialogs, close dropdowns, etc.
     * <p>
     * When sent via {@code Actions.sendKeys()} without a target element,
     * the key event goes to the currently focused element or the page body.
     * </p>
     */
    @Test(priority = 5, description = "Press ESCAPE key using Actions")
    public void testPressEscapeKey() {
        Actions actions = new Actions(driver);
        actions.sendKeys(Keys.ESCAPE).perform();
        System.out.println("ESCAPE key pressed — useful for closing modals/dialogs");
    }

    // -----------------------------------------------------------------------
    // 6. Arrow keys navigation
    // -----------------------------------------------------------------------

    /**
     * Demonstrates arrow key navigation in form fields.
     * <p>
     * Arrow keys can be used to navigate within text fields, move between
     * list items, navigate dropdown menus, etc.
     * </p>
     */
    @Test(priority = 6, description = "Navigate with arrow keys")
    public void testArrowKeysNavigation() {
        WebElement usernameField = driver.findElement(By.cssSelector("#defaultLoginFormUsername"));
        usernameField.clear();
        usernameField.sendKeys("Hello");

        // Move cursor to the beginning using HOME key
        usernameField.sendKeys(Keys.HOME);

        // Type text at the beginning
        usernameField.sendKeys(">> ");

        String text = usernameField.getAttribute("value");
        System.out.println("Text after HOME + type: " + text);
        Assert.assertTrue(text.startsWith(">> "),
                "Text should start with '>> ' after HOME key navigation");
    }

    // -----------------------------------------------------------------------
    // 7. Key combinations with multiple modifiers
    // -----------------------------------------------------------------------

    /**
     * Demonstrates pressing multiple modifier keys simultaneously.
     * <p>
     * Example: Shift+Tab to move focus backwards through form fields.
     * </p>
     */
    @Test(priority = 7, description = "Shift+Tab to move focus backwards")
    public void testShiftTabNavigation() {
        WebElement passwordField = driver.findElement(By.cssSelector("#defaultLoginFormPassword"));
        passwordField.click();

        // Shift+Tab should move focus back to the username field
        passwordField.sendKeys(Keys.chord(Keys.SHIFT, Keys.TAB));

        WebElement activeElement = driver.switchTo().activeElement();
        System.out.println("Active element after Shift+Tab: " + activeElement.getAttribute("id"));
    }
}
