# playwright-java-soft-assertions

`playwright-java-soft-assertions` gives you the ability to use Playwright's built in web-first assertions with soft assertions.

## Installation

```xml
<dependency>
    <groupId>io.github.uchagani</groupId>
    <artifactId>playwright-java-soft-assertions</artifactId>
    <version>1.0.1</version>
</dependency>
```

## Usage

Using `playwright-java-soft-assertions` is similar to other soft assertion implementations:

1.  Create an instance of the `SoftAssertions` object
2.  Use the soft assertions object to make calls to Playwright's assertions
3.  Call the `assertAll()` method when you're done.

```java
import io.github.uchagani.playwright.assertions.SoftAssertions;

public class FooTest {
    
    @Test
    public void test() {
        Locator someButton = page.locator("#some-id");
        Locator someLabel = page.locator(".my-label");

        SoftAssertions softly = SoftAssertions.create();
        softly.assertThat(someButton).isEnabled();
        softly.assertThat(someLabel).hasText("foo");
        
        softly.assertAll();
    }
}
```

All Playwright assertions are supported by `playwright-java-soft-assertions` including Page, Locator, and APIResponse.


