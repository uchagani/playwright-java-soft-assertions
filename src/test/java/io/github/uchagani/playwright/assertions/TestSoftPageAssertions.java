package io.github.uchagani.playwright.assertions;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.PageAssertions;
import io.github.uchagani.jp.UseBrowserConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import java.util.regex.Pattern;

import static io.github.uchagani.playwright.assertions.Utils.assertFailureCount;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@UseBrowserConfig(DefaultBrowserConfig.class)
public class TestSoftPageAssertions {
    private SoftAssertions softly;
    private Page page;
    private final static String PAGE_TITLE_HTML = "<!DOCTYPE html><html><head><title>My Page Title</title></head></html>";
    private final static String PAGE_TITLE = "My Page Title";
    @BeforeEach
    void beforeEach(Page page) {
        softly = SoftAssertions.create();
        this.page = page;
    }

    @Test
    void hasURLTextPass() {
        page.navigate("data:text/html,<div>A</div>");
        softly.assertThat(page).hasURL("data:text/html,<div>A</div>");
    }

    @Test
    void hasURLTextFail() {
        page.navigate("data:text/html,<div>B</div>");
        softly.assertThat(page).hasURL("foo", new PageAssertions.HasURLOptions().setTimeout(1_000));
        AssertionFailedError e = assertThrows(AssertionFailedError.class, () -> softly.assertAll());
        assertTrue(e.getMessage().contains("foo"));
        assertTrue(e.getMessage().contains("data:text/html,<div>B</div>"));
        assertTrue(e.getMessage().contains("Page URL expected to be"), e.getMessage());
        assertFailureCount(softly, 1);
    }

    @Test
    void notHasUrlTextPass() {
        page.navigate("data:text/html,<div>B</div>");
        softly.assertThat(page).not().hasURL("about:blank", new PageAssertions.HasURLOptions().setTimeout(1000));
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void notHasUrlTextFail() {
        page.navigate("data:text/html,<div>B</div>");
        softly.assertThat(page).not().hasURL("data:text/html,<div>B</div>", new PageAssertions.HasURLOptions().setTimeout(1_000));
        AssertionFailedError e = assertThrows(AssertionFailedError.class, () -> softly.assertAll());
        assertTrue(e.getMessage().contains("data:text/html,<div>B</div>"));
        assertTrue(e.getMessage().contains("Page URL expected not to be"), e.getMessage());
        assertFailureCount(softly, 1);
    }

    @Test
    void hasURLRegexPass() {
        page.navigate("data:text/html,<div>A</div>");
        softly.assertThat(page).hasURL(Pattern.compile("text"));
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void hasURLRegexFail() {
        page.setContent(PAGE_TITLE_HTML);
        softly.assertThat(page).hasURL(Pattern.compile(".*foo.*"), new PageAssertions.HasURLOptions().setTimeout(1_000));
        AssertionFailedError e = assertThrows(AssertionFailedError.class, () -> softly.assertAll());
        assertTrue(e.getMessage().contains(".*foo.*"));
        assertTrue(e.getMessage().contains("Page URL expected to match regex"), e.getMessage());
        assertFailureCount(softly, 1);
    }

    @Test
    void notHasUrlRegEx() {
        page.navigate("data:text/html,<div>B</div>");
        softly.assertThat(page).not().hasURL(Pattern.compile("about"), new PageAssertions.HasURLOptions().setTimeout(1000));
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void hasTitleTextPass() {
        page.setContent(PAGE_TITLE_HTML);
        softly.assertThat(page).hasTitle(PAGE_TITLE, new PageAssertions.HasTitleOptions().setTimeout(1_000));
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void hasTitleTextNormalizeWhitespaces() {
        page.setContent("<title>     Foo     Bar    </title>");
        softly.assertThat(page).hasTitle("  Foo  Bar", new PageAssertions.HasTitleOptions().setTimeout(1_000));
    }

    @Test
    void hasTitleTextFail() {
        page.setContent(PAGE_TITLE_HTML);
        softly.assertThat(page).hasTitle("foo", new PageAssertions.HasTitleOptions().setTimeout(1_000));
        AssertionFailedError e = assertThrows(AssertionFailedError.class, () -> softly.assertAll());
        assertTrue(e.getMessage().contains("foo"));
        assertTrue(e.getMessage().contains(PAGE_TITLE));
        assertTrue(e.getMessage().contains("Page title expected to be: foo\nReceived: " + PAGE_TITLE), e.getMessage());
        assertFailureCount(softly, 1);
    }

    @Test
    void hasTitleRegexPass() {
        page.setContent(PAGE_TITLE_HTML);
        softly.assertThat(page).hasTitle(Pattern.compile("^" + PAGE_TITLE + "$"));
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void hasTitleRegexFail() {
        page.setContent(PAGE_TITLE_HTML);
        softly.assertThat(page).hasTitle(Pattern.compile("^foo[AB]"), new PageAssertions.HasTitleOptions().setTimeout(1_000));
        AssertionFailedError e = assertThrows(AssertionFailedError.class, () -> softly.assertAll());
        assertTrue(e.getMessage().contains("^foo[AB]"));
        assertTrue(e.getMessage().contains(PAGE_TITLE));
        assertTrue(e.getMessage().contains("Page title expected to match regex: ^foo[AB]\nReceived: " + PAGE_TITLE), e.getMessage());
        assertFailureCount(softly, 1);
    }

    @Test
    void notHasTitleRegEx() {
        page.setContent(PAGE_TITLE_HTML);
        softly.assertThat(page).not().hasTitle(Pattern.compile("ab.ut"));
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void hasTitleRegExCaseInsensitivePass() {
        page.setContent(PAGE_TITLE_HTML);
        softly.assertThat(page).hasTitle(Pattern.compile(PAGE_TITLE, Pattern.CASE_INSENSITIVE));
        softly.assertAll();
        assertFailureCount(softly, 0);
    }
}
