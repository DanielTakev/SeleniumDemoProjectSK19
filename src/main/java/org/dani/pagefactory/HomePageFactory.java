package org.dani.pagefactory;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;

/**
 * Home page represented with the <b>Page Factory</b> pattern.
 * <p>
 * Demonstrates {@link FindAll} which applies <b>OR logic</b> across multiple
 * {@link FindBy} locators — the resulting list contains elements matching
 * <em>any</em> of the given strategies.
 * </p>
 *
 * <h3>{@code @FindAll} vs {@code @FindBys} — quick reference:</h3>
 * <table border="1">
 *   <tr><th>Annotation</th><th>Logic</th><th>Description</th></tr>
 *   <tr>
 *     <td>{@code @FindAll}</td>
 *     <td><b>OR</b></td>
 *     <td>Collects elements matching locator A <b>or</b> locator B (union).</td>
 *   </tr>
 *   <tr>
 *     <td>{@code @FindBys}</td>
 *     <td><b>AND (chained)</b></td>
 *     <td>Finds elements matching locator A, <b>then</b> within those, locator B.</td>
 *   </tr>
 * </table>
 *
 * @see org.dani.pages.HomePage for the classic POM equivalent
 */
public class HomePageFactory extends BasePageFactory {

    private static final String HOME_URL = "/posts/all";

    /**
     * Post feed containers found by a single CSS selector.
     */
    @FindBy(css = ".post-feed-container")
    private List<WebElement> postContainers;

    /**
     * Navigation links collected with {@code @FindAll} (OR logic).
     * <p>
     * The resulting list contains elements matching <b>either</b> of the two
     * {@code @FindBy} locators. This is useful when the same logical component
     * may appear under different selectors (e.g., responsive layouts, A/B tests,
     * or legacy markup).
     * </p>
     *
     * <pre>{@code
     * // Pseudo-equivalent without Page Factory:
     * List<WebElement> links = new ArrayList<>();
     * links.addAll(driver.findElements(By.cssSelector("a.nav-link")));
     * links.addAll(driver.findElements(By.cssSelector(".navbar a")));
     * }</pre>
     */
    @FindAll({
            @FindBy(css = "a.nav-link"), // Top priority if both are visible: the elements with a.nav-link 
            @FindBy(css = ".navbar a")
    })
    private List<WebElement> navigationLinks;

    /**
     * Creates a HomePageFactory and initializes all annotated elements.
     *
     * @param driver the WebDriver instance
     */
    public HomePageFactory(WebDriver driver) {
        super(driver);
    }

    /**
     * Navigates the browser to the home page.
     */
    public void navigateToPage() {
        navigateTo(HOME_URL);
    }

    /**
     * Waits until the home page URL is fully loaded.
     */
    public void verifyPageLoaded() {
        waitForUrl(BASE_URL + HOME_URL);
    }

    /**
     * Checks whether the browser is currently on the home page.
     *
     * @return {@code true} if current URL matches the expected home page URL
     */
    public boolean isUrlLoaded() {
        verifyPageLoaded();
        return getCurrentUrl().equals(BASE_URL + HOME_URL);
    }

    /**
     * Returns the number of post containers on the page.
     *
     * @return post count
     */
    public int getPostsCount() {
        return postContainers.size();
    }

    /**
     * Returns the number of navigation links found via {@code @FindAll}.
     * <p>
     * Demonstrates that OR-based multi-locator collection works across
     * different selectors.
     * </p>
     *
     * @return navigation link count
     */
    public int getNavigationLinksCount() {
        return navigationLinks.size();
    }

    public boolean hasPostByAuthorName(String authorName) {
        return isElementVisible(By.xpath("//app-post-detail//a[text()=\"" + authorName + "\"]"));
    }
}
