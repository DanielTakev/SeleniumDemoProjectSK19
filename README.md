# Selenium Demo Project – SK19

A hands-on **Java + Maven + Selenium + TestNG** demo project that demonstrates two approaches
to structuring UI test automation: the **Page Object Model (POM)** and the **Page Factory** pattern,
plus **Data-Driven Testing** with TestNG's `@DataProvider`.

---

## Table of Contents

1. [Prerequisites](#prerequisites)
2. [Project Structure](#project-structure)
3. [Patterns Explained](#patterns-explained)
    - [Page Object Model (POM)](#page-object-model-pom)
    - [Page Factory](#page-factory)
    - [Data-Driven Testing](#data-driven-testing)
4. [Key Annotations & APIs](#key-annotations--apis)
5. [How to Run](#how-to-run)
6. [testng.xml Explained](#testngxml-explained)

---

## Prerequisites

| Tool   | Version |
|--------|---------|
| Java   | 17+     |
| Maven  | 3.8+    |
| Chrome | latest  |

> ChromeDriver is managed automatically by Selenium 4's built-in driver manager — no manual download needed.

---

## Project Structure

```
src/
├── main/java/org/dani/
│   ├── pages/                          # Classic POM pages (By locators)
│   │   ├── BasePage.java
│   │   ├── LoginPage.java
│   │   └── HomePage.java
│   └── pagefactory/                    # Page Factory pages (@FindBy annotations)
│       ├── BasePageFactory.java
│       ├── LoginPageFactory.java
│       └── HomePageFactory.java
├── test/java/org/dani/tests/
│   ├── BaseTest.java                   # Shared setup / teardown
│   ├── LoginTest.java                  # Basic POM test
│   ├── LoginDataDrivenTest.java        # Data-driven POM test (@DataProvider)
│   └── pagefactory/
│       └── LoginPageFactoryTest.java   # Page Factory test
testng.xml                              # TestNG suite configuration
pom.xml
```

---

## Patterns Explained

### Page Object Model (POM)

The **classic POM** stores locators as `By` objects and wraps browser interactions in methods:

```java
// Locator stored as By
private final By usernameField = By.cssSelector("#defaultLoginFormUsername");

// Manual interaction with explicit wait
public void enterUsername(String username) {
    typeText(usernameField, username); // typeText uses WebDriverWait + sendKeys
}
```

**Pros:** Full control over when and how elements are located.  
**Cons:** More boilerplate — you write wait/find logic manually.

> See the `org.dani.pages` package.

---

### Page Factory

**Page Factory** uses annotations to declare elements and `PageFactory.initElements()` to create
lazy-loading proxies:

```java
@FindBy(css = "#defaultLoginFormUsername")
@CacheLookup
private WebElement usernameField;

// Constructor — one-time initialization
public LoginPageFactory(WebDriver driver) {
    PageFactory.initElements(new AjaxElementLocatorFactory(driver, 10), this);
}
```

**Pros:** Less boilerplate; annotations are declarative and readable.  
**Cons:** `@CacheLookup` can cause `StaleElementReferenceException` on dynamic/AJAX elements.

> See the `org.dani.pagefactory` package.

---

### Data-Driven Testing

TestNG's `@DataProvider` feeds multiple data rows into a single `@Test` method:

```java
@DataProvider(name = "loginCredentials")
public Object[][] loginData() {
    return new Object[][]{
        {"validUser",  "validPass",  true,  "Valid credentials"},
        {"wrongUser",  "validPass",  false, "Invalid username"},
    };
}

@Test(dataProvider = "loginCredentials")
public void testLogin(String user, String pass, boolean shouldPass, String desc) {
    // Same test logic, different data each run
}
```

TestNG invokes the test method **once per row**, injecting column values as parameters.
The report shows each invocation as a separate test result.

> See `LoginDataDrivenTest.java`.

---

## Key Annotations & APIs

| Annotation / API               | Description                                                                                                 |
|---------------------------------|-------------------------------------------------------------------------------------------------------------|
| `@FindBy`                       | Locates a single element (or `List<WebElement>`) by one strategy (id, css, xpath, className …).             |
| `@FindAll`                      | **OR logic** – collects elements matching *any* of the listed `@FindBy` locators.                           |
| `@FindBys`                      | **AND logic (chained)** – finds elements matching locator A, then within those, locator B.                  |
| `@CacheLookup`                  | Caches the element after first lookup. Use **only** for static elements that are never re-rendered.         |
| `PageFactory.initElements()`    | Scans a class for `@FindBy` fields and creates proxy objects that resolve elements on first access.         |
| `AjaxElementLocatorFactory`     | A locator factory that adds **polling** to each proxy — waits N seconds for the element to appear in the DOM. Ideal for AJAX pages. |
| `@DataProvider`                 | TestNG annotation that supplies test data rows to a `@Test` method.                                        |

---

## How to Run

### Run the full suite via Maven (uses testng.xml)

```bash
mvn clean test
```

### Run a specific test class

```bash
mvn clean test -Dtest=LoginTest
```

### Run from your IDE

- **IntelliJ IDEA:** Right-click `testng.xml` → *Run*.
- **VS Code:** Use the *Test Runner for Java* extension.

---

## testng.xml Explained

```xml
<suite name="Selenium Demo Suite" verbose="1">

    <!-- Each <test> block gets its own @BeforeClass / @AfterClass lifecycle -->
    <test name="POM - Login Tests">
        <classes>
            <class name="org.dani.tests.LoginTest"/>
        </classes>
    </test>

    <test name="POM - Data Driven Login Tests">
        <classes>
            <class name="org.dani.tests.LoginDataDrivenTest"/>
        </classes>
    </test>

    <test name="Page Factory - Login Tests">
        <classes>
            <class name="org.dani.tests.pagefactory.LoginPageFactoryTest"/>
        </classes>
    </test>

</suite>
```

| Element     | Purpose                                                                                         |
|-------------|-------------------------------------------------------------------------------------------------|
| `<suite>`   | Top-level container. `verbose="1"` controls console output detail (0 = silent, 10 = maximum).   |
| `<test>`    | A logical group of test classes. Each group triggers its own `@BeforeClass` / `@AfterClass`.    |
| `<classes>` | Lists the fully-qualified test classes to include in the group.                                  |

The `maven-surefire-plugin` in `pom.xml` is configured to pick up `testng.xml`, so
`mvn clean test` automatically runs this suite.

---

## License

This project is for **educational purposes only**.
