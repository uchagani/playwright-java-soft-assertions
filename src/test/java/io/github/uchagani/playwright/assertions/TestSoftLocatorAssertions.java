package io.github.uchagani.playwright.assertions;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.LocatorAssertions;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import io.github.uchagani.jp.UseBrowserConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import java.util.regex.Pattern;

import static io.github.uchagani.playwright.assertions.Utils.assertFailureCount;
import static io.github.uchagani.playwright.assertions.Utils.mapOf;
import static org.junit.jupiter.api.Assertions.*;

@UseBrowserConfig(DefaultBrowserConfig.class)
public class TestSoftLocatorAssertions {
    private SoftAssertions softly;
    private Page page;

    @BeforeEach
    void beforeEach(Page page) {
        softly = SoftAssertions.create();
        this.page = page;
    }

    @Test
    void containsTextWRegexPass() {
        page.setContent("<div id=node>Text   content</div>");
        Locator locator = page.locator("#node");
        softly.assertThat(locator).containsText(Pattern.compile("ex"));
        softly.assertThat(locator).containsText(Pattern.compile("ext   cont"));
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void containsTextWRegexCaseInsensitivePass() {
        page.setContent("<div id=node>Text   content</div>");
        Locator locator = page.locator("#node");
        softly.assertThat(locator).containsText(Pattern.compile("text", Pattern.CASE_INSENSITIVE));
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void containsTextWRegexMultilinePass() {
        page.setContent("<div id=node>Text \nContent</div>");
        Locator locator = page.locator("#node");
        softly.assertThat(locator).containsText(Pattern.compile("^Content", Pattern.MULTILINE));
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void containsTextWRegexDotAllPass() {
        page.setContent("<div id=node>foo\nbar</div>");
        Locator locator = page.locator("#node");
        softly.assertThat(locator).containsText(Pattern.compile("foo.bar", Pattern.DOTALL));
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void containsTextWRegexFail() {
        page.setContent("<div id=node>Text   content</div>");
        Locator locator = page.locator("#node");
        softly.assertThat(locator).containsText(Pattern.compile("ex2"), new LocatorAssertions.ContainsTextOptions().setTimeout(1000));
        AssertionFailedError e = assertThrows(AssertionFailedError.class, () -> softly.assertAll());
        assertTrue(e.getMessage().contains("ex2"));
        assertTrue(e.getMessage().contains("Text   content"));
        assertTrue(e.getMessage().contains("Locator expected to contain regex"));
        assertFailureCount(softly, 1);
    }

    @Test
    void containsTextWTextPass() {
        page.setContent("<div id=node>Text content</div>");
        Locator locator = page.locator("#node");
        softly.assertThat(locator).containsText("Text");
        softly.assertThat(locator).containsText("   ext        cont\n  ");
        softly.assertThat(locator).containsText("EXT", new LocatorAssertions.ContainsTextOptions().setIgnoreCase(true));
        softly.assertThat(locator).not().containsText("TEXT", new LocatorAssertions.ContainsTextOptions().setIgnoreCase(false));
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void containsTextWTextArrayPass() {
        page.setContent("<div>Text \n1</div><div>Text2</div><div>Text3</div>");
        Locator locator = page.locator("div");
        softly.assertThat(locator).containsText(new String[]{"ext     1", "ext3"});
        softly.assertThat(locator).containsText(new String[]{"EXT 1",
                                                             "eXt3"}, new LocatorAssertions.ContainsTextOptions().setIgnoreCase(true));
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void hasTextWRegexPass() {
        page.setContent("<div id=node>Text   content</div>");
        Locator locator = page.locator("#node");
        softly.assertThat(locator).hasText(Pattern.compile("Te.t"));
        softly.assertThat(locator).hasText(Pattern.compile("Text.+content"));
        softly.assertThat(locator).hasText(Pattern.compile("text   content"), new LocatorAssertions.HasTextOptions().setIgnoreCase(true));
        softly.assertThat(locator).not().hasText(Pattern.compile("text   content", Pattern.CASE_INSENSITIVE), new LocatorAssertions.HasTextOptions().setIgnoreCase(false));
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void hasTextWRegexFail() {
        page.setContent("<div id=node>Text   content</div>");
        Locator locator = page.locator("#node");
        softly.assertThat(locator).hasText(Pattern.compile("Text 2"), new LocatorAssertions.HasTextOptions().setTimeout(1000));
        AssertionFailedError e = assertThrows(AssertionFailedError.class, () -> softly.assertAll());
        assertTrue(e.getMessage().contains("Text 2"));
        assertTrue(e.getMessage().contains("Text   content"));
        assertTrue(e.getMessage().contains("Locator expected to have text matching regex"), e.getMessage());
        assertFailureCount(softly, 1);
    }

    @Test
    void hasTextWTextPass() {
        page.setContent("<div id=node><span></span>Text \ncontent&nbsp;    </div>");
        Locator locator = page.locator("#node");
        softly.assertThat(locator).hasText("Text                        content");
        softly.assertThat(locator).hasText("text CONTENT", new LocatorAssertions.HasTextOptions().setIgnoreCase(true));
        softly.assertThat(locator).not().hasText("TEXT", new LocatorAssertions.HasTextOptions().setIgnoreCase(false));
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void hasTextWTextFail() {
        page.setContent("<div id=node>Text content</div>");
        Locator locator = page.locator("#node");
        softly.assertThat(locator).hasText("Text", new LocatorAssertions.HasTextOptions().setTimeout(1000));
        AssertionFailedError e = assertThrows(AssertionFailedError.class, () -> softly.assertAll());
        assertTrue(e.getMessage().contains("Text"));
        assertTrue(e.getMessage().contains("Text content"));
        assertTrue(e.getMessage().contains("Locator expected to have text"), e.getMessage());
        assertFailureCount(softly, 1);
    }

    @Test
    void hasTextWTextInnerTextPass() {
        page.setContent("<div id=node>Text <span hidden>garbage</span> content</div>");
        Locator locator = page.locator("#node");
        softly.assertThat(locator).hasText("Text content", new LocatorAssertions.HasTextOptions().setUseInnerText(true));
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void hasTextWTextArrayPass() {
        page.setContent("<div>Text    \n1</div><div>Text   2a</div>");
        Locator locator = page.locator("div");
        softly.assertThat(locator).hasText(new String[]{"Text  1", "Text   2a"});
        softly.assertThat(locator).hasText(new String[]{"tEXT 1",
                                                        "TExt 2A"}, new LocatorAssertions.HasTextOptions().setIgnoreCase(true));
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void hasTextWTextArrayPassEmpty() {
        page.setContent("<div></div>");
        Locator locator = page.locator("p");
        softly.assertThat(locator).hasText(new String[]{});
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void hasTextWTextArrayPassNotEmpty() {
        page.setContent("<div><p>Test</p></div>");
        Locator locator = page.locator("div");
        softly.assertThat(locator).not().hasText(new String[]{});
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void hasTextWTextArrayPassOnEmpty() {
        page.setContent("<div></div>");
        Locator locator = page.locator("p");
        softly.assertThat(locator).not().hasText(new String[]{"Test"});
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void hasTextWTextArrayFailOnNotEmpty() {
        page.setContent("<div></div>");
        Locator locator = page.locator("p");
        softly.assertThat(locator).not().hasText(new String[]{}, new LocatorAssertions.HasTextOptions().setTimeout(1000));
        AssertionFailedError e = assertThrows(AssertionFailedError.class, () -> softly.assertAll());
        assertTrue(e.getMessage().contains("[]"));
        assertTrue(e.getMessage().contains("[]"));
        assertTrue(e.getMessage().contains("Locator expected not to have text"), e.getMessage());
        assertFailureCount(softly, 1);
    }

    @Test
    void hasTextWTextArrayPassLazyPass() {
        page.setContent("<div id=div></div>");
        Locator locator = page.locator("p");
        page.evaluate("setTimeout(() => {\n" +
                "  div.innerHTML = \"<p>Text 1</p><p>Text 2</p>\";\n" +
                "}, 100);");
        softly.assertThat(locator).hasText(new String[]{"Text  1",
                                                        "Text   2"}, new LocatorAssertions.HasTextOptions().setTimeout(1000));
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void hasTextWTextArrayFail() {
        page.setContent("<div>Text 1</div><div>Text 3</div>");
        Locator locator = page.locator("div");
        page.evaluate("setTimeout(() => {\n" +
                "  div.innerHTML = \"<p>Text 1</p><p>Text 2</p>\";\n" +
                "}, 100);");
        softly.assertThat(locator).hasText(new String[]{"Text 1", "Text 3",
                                                        "Extra"}, new LocatorAssertions.HasTextOptions().setTimeout(1000));
        AssertionFailedError e = assertThrows(AssertionFailedError.class, () -> softly.assertAll());
        assertTrue(e.getMessage().contains("[Text 1, Text 3, Extra]"));
        assertTrue(e.getMessage().contains("[Text 1, Text 3]"));
        assertTrue(e.getMessage().contains("Locator expected to have text: [Text 1, Text 3, Extra]"), e.getMessage());
        assertTrue(e.getMessage().contains("Received: [Text 1, Text 3]"), e.getMessage());
        assertFailureCount(softly, 1);
    }

    @Test
    void hasTextWRegExArrayPass() {
        page.setContent("<div>Text    \n1</div><div>Text   2a</div>");
        Locator locator = page.locator("div");
        softly.assertThat(locator).hasText(new Pattern[]{Pattern.compile("Text    \n1"),
                                                         Pattern.compile("Text   \\d+a")});
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void hasTextWRegExArrayFail() {
        page.setContent("<div>Text 1</div><div>Text 3</div>");
        Locator locator = page.locator("div");
        softly.assertThat(locator).hasText(new Pattern[]{Pattern.compile("Text 1"), Pattern.compile("Text   \\d"),
                                                         Pattern.compile("Extra")}, new LocatorAssertions.HasTextOptions().setTimeout(1000));
        AssertionFailedError e = assertThrows(AssertionFailedError.class, () -> softly.assertAll());
        assertTrue(e.getMessage().contains("[Text 1, Text   \\d, Extra]"));
        assertTrue(e.getMessage().contains("[Text 1, Text 3]"));
        assertTrue(e.getMessage().contains("Locator expected to have text"), e.getMessage());
        assertFailureCount(softly, 1);
    }

    @Test
    void hasAttributeTextPass() {
        page.setContent("<div id=node>Text content</div>");
        Locator locator = page.locator("#node");
        softly.assertThat(locator).hasAttribute("id", "node");
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void hasAttributeTextFail() {
        page.setContent("<div id=node>Text content</div>");
        Locator locator = page.locator("#node");
        softly.assertThat(locator).hasAttribute("id", "foo", new LocatorAssertions.HasAttributeOptions().setTimeout(1000));
        AssertionFailedError e = assertThrows(AssertionFailedError.class, () -> softly.assertAll());
        assertTrue(e.getMessage().contains("foo"));
        assertTrue(e.getMessage().contains("node"));
        assertTrue(e.getMessage().contains("Locator expected to have attribute 'id': foo\nReceived: node"), e.getMessage());
        assertFailureCount(softly, 1);
    }

    @Test
    void hasAttributeRegExpPass() {
        page.setContent("<div id=node>Text content</div>");
        Locator locator = page.locator("#node");
        softly.assertThat(locator).hasAttribute("id", Pattern.compile("n..e"));
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void hasAttributeRegExpFail() {
        page.setContent("<div id=node>Text content</div>");
        Locator locator = page.locator("#node");
        softly.assertThat(locator).hasAttribute("id", Pattern.compile(".Nod.."), new LocatorAssertions.HasAttributeOptions().setTimeout(1000));
        AssertionFailedError e = assertThrows(AssertionFailedError.class, () -> softly.assertAll());
        assertTrue(e.getMessage().contains(".Nod.."));
        assertTrue(e.getMessage().contains("node"));
        assertTrue(e.getMessage().contains("Locator expected to have attribute 'id' matching regex: .Nod..\nReceived: node"), e.getMessage());
        assertFailureCount(softly, 1);
    }

    @Test
    void hasClassTextPass() {
        page.setContent("<div class=\"foo bar baz\"></div>");
        Locator locator = page.locator("div");
        softly.assertThat(locator).hasClass("foo bar baz");
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void hasClassTextFail() {
        page.setContent("<div class=\"bar baz\"></div>");
        Locator locator = page.locator("div");
        softly.assertThat(locator).hasClass("foo bar baz", new LocatorAssertions.HasClassOptions().setTimeout(1000));
        AssertionFailedError e = assertThrows(AssertionFailedError.class, () -> softly.assertAll());
        assertTrue(e.getMessage().contains("foo bar baz"));
        assertTrue(e.getMessage().contains("bar baz"));
        assertTrue(e.getMessage().contains("Locator expected to have class"), e.getMessage());
        assertFailureCount(softly, 1);
    }

    @Test
    void hasClassRegExpPass() {
        page.setContent("<div class=\"foo bar baz\"></div>");
        Locator locator = page.locator("div");
        softly.assertThat(locator).hasClass(Pattern.compile("foo.* baz"));
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void hasClassRegExpFail() {
        page.setContent("<div class=\"bar baz\"></div>");
        Locator locator = page.locator("div");
        softly.assertThat(locator).hasClass(Pattern.compile("foo Z.*"), new LocatorAssertions.HasClassOptions().setTimeout(1000));
        AssertionFailedError e = assertThrows(AssertionFailedError.class, () -> softly.assertAll());
        assertTrue(e.getMessage().contains("foo Z.*"));
        assertTrue(e.getMessage().contains("bar baz"));
        assertTrue(e.getMessage().contains("Locator expected to have class matching regex"), e.getMessage());
        assertFailureCount(softly, 1);
    }

    @Test
    void hasClassTextArrayPass() {
        page.setContent("<div class=\"foo\"></div><div class=\"bar\"></div><div class=\"baz\"></div>");
        Locator locator = page.locator("div");
        softly.assertThat(locator).hasClass(new String[]{"foo", "bar", "baz"});
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void hasClassTextArrayFail() {
        page.setContent("<div class=\"foo\"></div><div class=\"bar\"></div><div class=\"baz\"></div>");
        Locator locator = page.locator("div");
        softly.assertThat(locator).hasClass(new String[]{"foo", "bar",
                                                         "missing"}, new LocatorAssertions.HasClassOptions().setTimeout(1000));
        AssertionFailedError e = assertThrows(AssertionFailedError.class, () -> softly.assertAll());
        assertTrue(e.getMessage().contains("[foo, bar, missing]"));
        assertTrue(e.getMessage().contains("[foo, bar, baz]"));
        assertTrue(e.getMessage().contains("Locator expected to have class"), e.getMessage());
        assertFailureCount(softly, 1);
    }

    @Test
    void hasClassRegExpArrayPass() {
        page.setContent("<div class=\"foo\"></div><div class=\"bar\"></div><div class=\"baz\"></div>");
        Locator locator = page.locator("div");
        softly.assertThat(locator).hasClass(new Pattern[]{Pattern.compile("fo.*"), Pattern.compile(".ar"),
                                                          Pattern.compile("baz")});
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void hasClassRegExpArrayFail() {
        page.setContent("<div class=\"foo\"></div><div class=\"bar\"></div><div class=\"baz\"></div>");
        Locator locator = page.locator("div");
        softly.assertThat(locator).hasClass(new Pattern[]{Pattern.compile("fo.*"), Pattern.compile(".ar"),
                                                          Pattern.compile("baz"),
                                                          Pattern.compile("extra")}, new LocatorAssertions.HasClassOptions().setTimeout(1000));
        AssertionFailedError e = assertThrows(AssertionFailedError.class, () -> softly.assertAll());
        assertTrue(e.getMessage().contains("[fo.*, .ar, baz, extra]"));
        assertTrue(e.getMessage().contains("[foo, bar, baz]"));
        assertTrue(e.getMessage().contains("Locator expected to have class matching regex"), e.getMessage());
        assertFailureCount(softly, 1);
    }

    @Test
    void hasCountPass() {
        page.setContent("<select><option>One</option><option>Two</option></select>");
        Locator locator = page.locator("option");
        softly.assertThat(locator).hasCount(2);
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void hasCountFail() {
        page.setContent("<select><option>One</option><option>Two</option></select>");
        Locator locator = page.locator("option");
        softly.assertThat(locator).hasCount(1, new LocatorAssertions.HasCountOptions().setTimeout(1000));
        AssertionFailedError e = assertThrows(AssertionFailedError.class, () -> softly.assertAll());
        assertTrue(e.getMessage().contains("1"));
        assertTrue(e.getMessage().contains("2"));
        assertTrue(e.getMessage().contains("Locator expected to have count"), e.getMessage());
        assertFailureCount(softly, 1);
    }

    @Test
    void hasCountPassZero() {
        page.setContent("<div></div>");
        Locator locator = page.locator("span");
        softly.assertThat(locator).hasCount(0);
        softly.assertThat(locator).not().hasCount(1);
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void hasCSSPass() {
        page.setContent("<div id=node style='color: rgb(255, 0, 0)'>Text content</div>");
        Locator locator = page.locator("#node");
        softly.assertThat(locator).hasCSS("color", "rgb(255, 0, 0)");
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void hasCSSFail() {
        page.setContent("<div id=node style='color: rgb(255, 0, 0)'>Text content</div>");
        Locator locator = page.locator("#node");
        softly.assertThat(locator).hasCSS("color", "red", new LocatorAssertions.HasCSSOptions().setTimeout(1000));
        AssertionFailedError e = assertThrows(AssertionFailedError.class, () -> softly.assertAll());
        assertTrue(e.getMessage().contains("red"));
        assertTrue(e.getMessage().contains("rgb(255, 0, 0)"));
        assertTrue(e.getMessage().contains("Locator expected to have CSS property 'color'"), e.getMessage());
        assertFailureCount(softly, 1);
    }

    @Test
    void hasCSSRegExPass() {
        page.setContent("<div id=node style='color: rgb(255, 0, 0)'>Text content</div>");
        Locator locator = page.locator("#node");
        softly.assertThat(locator).hasCSS("color", Pattern.compile("rgb.*"));
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void hasCSSRegExFail() {
        page.setContent("<div id=node style='color: rgb(255, 0, 0)'>Text content</div>");
        Locator locator = page.locator("#node");
        softly.assertThat(locator).hasCSS("color", Pattern.compile("red"), new LocatorAssertions.HasCSSOptions().setTimeout(1000));
        AssertionFailedError e = assertThrows(AssertionFailedError.class, () -> softly.assertAll());
        assertTrue(e.getMessage().contains("red"));
        assertTrue(e.getMessage().contains("rgb(255, 0, 0)"));
        assertTrue(e.getMessage().contains("Locator expected to have CSS property 'color' matching regex"), e.getMessage());
        assertFailureCount(softly, 1);
    }

    @Test
    void hasIdPass() {
        page.setContent("<div id=node>Text content</div>");
        Locator locator = page.locator("#node");
        softly.assertThat(locator).hasId("node");
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void hasIdFail() {
        page.setContent("<div id=node>Text content</div>");
        Locator locator = page.locator("#node");
        softly.assertThat(locator).hasId("foo", new LocatorAssertions.HasIdOptions().setTimeout(1000));
        AssertionFailedError e = assertThrows(AssertionFailedError.class, () -> softly.assertAll());
        assertTrue(e.getMessage().contains("foo"));
        assertTrue(e.getMessage().contains("node"));
        assertTrue(e.getMessage().contains("Locator expected to have ID"), e.getMessage());
        assertFailureCount(softly, 1);
    }

    @Test
    void hasJSPropertyPass() {
        page.setContent("<div></div>");
        page.evalOnSelector("div", "e => e.foo = { a: 1, b: 'string' }");
        Locator locator = page.locator("div");
        softly.assertThat(locator).hasJSProperty("foo", mapOf("a", 1, "b", "string"));
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void hasJSPropertyNumberFail() {
        page.setContent("<div id=node>Text content</div>");
        Locator locator = page.locator("#node");
        page.evalOnSelector("div", "e => e.foo = 2021");
        softly.assertThat(locator).hasJSProperty("foo", 1, new LocatorAssertions.HasJSPropertyOptions().setTimeout(1000));
        AssertionFailedError e = assertThrows(AssertionFailedError.class, () -> softly.assertAll());
        assertTrue(e.getMessage().contains("1"));
        assertTrue(e.getMessage().contains("2021"));
        assertTrue(e.getMessage().contains("Locator expected to have JavaScript property 'foo'"), e.getMessage());
        assertFailureCount(softly, 1);
    }

    @Test
    void hasJSPropertyObjectFail() {
        page.setContent("<div id=node>Text content</div>");
        Locator locator = page.locator("#node");
        page.evalOnSelector("div", "e => e.foo = { a: 1, b: 'string' }");
        softly.assertThat(locator).hasJSProperty("foo", mapOf("a", 2), new LocatorAssertions.HasJSPropertyOptions().setTimeout(1000));
        AssertionFailedError e = assertThrows(AssertionFailedError.class, () -> softly.assertAll());
        assertTrue(e.getMessage().contains("{a=2}"));
        assertTrue(e.getMessage().contains("{a=1, b=string}"));
        assertTrue(e.getMessage().contains("Locator expected to have JavaScript property 'foo'"), e.getMessage());
        assertFailureCount(softly, 1);
    }

    @Test
    void hasJSPropertyStringFail() {
        page.setContent("<div id=node>Text content</div>");
        Locator locator = page.locator("#node");
        softly.assertThat(locator).hasJSProperty("id", "foo", new LocatorAssertions.HasJSPropertyOptions().setTimeout(1000));
        AssertionFailedError e = assertThrows(AssertionFailedError.class, () -> softly.assertAll());
        assertTrue(e.getMessage().contains("foo"));
        assertTrue(e.getMessage().contains("node"));
        assertTrue(e.getMessage().contains("Locator expected to have JavaScript property 'id'"), e.getMessage());
        assertFailureCount(softly, 1);
    }

    @Test
    void hasValueTextPass() {
        page.setContent("<input id=node></input>");
        Locator locator = page.locator("#node");
        locator.fill("Text content");
        softly.assertThat(locator).hasValue("Text content");
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void hasValueTextFail() {
        page.setContent("<input id=node></input>");
        Locator locator = page.locator("#node");
        locator.fill("Text content");
        softly.assertThat(locator).hasValue("Text2", new LocatorAssertions.HasValueOptions().setTimeout(1000));
        AssertionFailedError e = assertThrows(AssertionFailedError.class, () -> softly.assertAll());
        assertTrue(e.getMessage().contains("Text2"));
        assertTrue(e.getMessage().contains("Text content"));
        assertTrue(e.getMessage().contains("Locator expected to have value"), e.getMessage());
        assertFailureCount(softly, 1);
    }

    @Test
    void hasValueRegExpPass() {
        page.setContent("<input id=node></input>");
        Locator locator = page.locator("#node");
        locator.fill("Text content");
        softly.assertThat(locator).hasValue(Pattern.compile("Text"));
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void hasValueRegExpPassWithNot() {
        page.setContent("<input id=node></input>");
        Locator locator = page.locator("#node");
        locator.fill("Text content");
        softly.assertThat(locator).not().hasValue(Pattern.compile("Text2"));
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void hasValueRegExpFail() {
        page.setContent("<input id=node></input>");
        Locator locator = page.locator("#node");
        locator.fill("Text content");
        softly.assertThat(locator).hasValue(Pattern.compile("Text2"), new LocatorAssertions.HasValueOptions().setTimeout(1000));
        AssertionFailedError e = assertThrows(AssertionFailedError.class, () -> softly.assertAll());
        assertTrue(e.getMessage().contains("Text2"));
        assertTrue(e.getMessage().contains("Text content"));
        assertTrue(e.getMessage().contains("Locator expected to have value matching regex"), e.getMessage());
        assertFailureCount(softly, 1);
    }

    @Test
    void hasValuesWorksWithText() {
        page.setContent("<select multiple>\n" +
                "              <option value=\"R\">Red</option>\n" +
                "              <option value=\"G\">Green</option>\n" +
                "              <option value=\"B\">Blue</option>\n" +
                "            </select>");
        Locator locator = page.locator("select");
        locator.selectOption(new String[]{"R", "G"});
        softly.assertThat(locator).hasValues(new String[]{"R", "G"});
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void hasValuesFollowsLabels() {
        page.setContent("<label for=\"colors\">Pick a Color</label>\n" +
                "            <select id=\"colors\" multiple>\n" +
                "              <option value=\"R\">Red</option>\n" +
                "              <option value=\"G\">Green</option>\n" +
                "              <option value=\"B\">Blue</option>\n" +
                "            </select>");
        Locator locator = page.locator("text=Pick a Color");
        locator.selectOption(new String[]{"R", "G"});
        softly.assertThat(locator).hasValues(new String[]{"R", "G"});
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void hasValuesExactMatchWithText() {
        page.setContent("<select multiple>\n" +
                "              <option value=\"RR\">Red</option>\n" +
                "              <option value=\"GG\">Green</option>\n" +
                "            </select>");
        Locator locator = page.locator("select");
        locator.selectOption(new String[]{"RR", "GG"});
        softly.assertThat(locator).hasValues(new String[]{"R",
                                                          "G"}, new LocatorAssertions.HasValuesOptions().setTimeout(1000));
        AssertionFailedError e = assertThrows(AssertionFailedError.class, () -> softly.assertAll());
        assertTrue(e.getMessage().contains("[R, G]"));
        assertTrue(e.getMessage().contains("[RR, GG]"));
        assertTrue(e.getMessage().contains("Locator expected to have values"), e.getMessage());
        assertFailureCount(softly, 1);
    }

    @Test
    void hasValuesWorksWithRegex() {
        page.setContent("<select multiple>\n" +
                "              <option value=\"R\">Red</option>\n" +
                "              <option value=\"G\">Green</option>\n" +
                "              <option value=\"B\">Blue</option>\n" +
                "            </select>");
        Locator locator = page.locator("select");
        locator.selectOption(new String[]{"R", "G"});
        softly.assertThat(locator).hasValues(new Pattern[]{Pattern.compile("R"), Pattern.compile("G")});
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void hasValuesFailsWhenItemsNotSelected() {
        page.setContent("<select multiple>\n" +
                "              <option value=\"R\">Red</option>\n" +
                "              <option value=\"G\">Green</option>\n" +
                "              <option value=\"B\">Blue</option>\n" +
                "            </select>");
        Locator locator = page.locator("select");
        locator.selectOption(new String[]{"B"}, new Locator.SelectOptionOptions().setTimeout(1000));
        softly.assertThat(locator).hasValues(new Pattern[]{Pattern.compile("R"), Pattern.compile("G")});
        AssertionFailedError e = assertThrows(AssertionFailedError.class, () -> softly.assertAll());
        assertTrue(e.getMessage().contains("[R, G]"));
        assertTrue(e.getMessage().contains("[B]"));
        assertTrue(e.getMessage().contains("Locator expected to have values matching regex"), e.getMessage());
        assertFailureCount(softly, 1);
    }

    @Test
    void hasValuesFailsWhenMultipleNotSpecified() {
        page.setContent("<select>\n" +
                "              <option value=\"R\">Red</option>\n" +
                "              <option value=\"G\">Green</option>\n" +
                "              <option value=\"B\">Blue</option>\n" +
                "            </select>");
        Locator locator = page.locator("select");
        locator.selectOption(new String[]{"B"});
        softly.assertThat(locator).hasValues(new Pattern[]{Pattern.compile("R"), Pattern.compile("G")});
        AssertionFailedError e = assertThrows(AssertionFailedError.class, () -> softly.assertAll());
        assertTrue(e.getMessage().contains("Not a select element with a multiple attribute"), e.getMessage());
        assertFailureCount(softly, 1);
    }

    @Test
    void hasValuesFailsWhenNotASelectElement() {
        page.setContent("<input value=\"foo\" />");
        Locator locator = page.locator("input");
        softly.assertThat(locator).hasValues(new Pattern[]{Pattern.compile("R"),
                                                           Pattern.compile("G")}, new LocatorAssertions.HasValuesOptions().setTimeout(1000));
        AssertionFailedError e = assertThrows(AssertionFailedError.class, () -> softly.assertAll());
        assertTrue(e.getMessage().contains("Not a select element with a multiple attribute"), e.getMessage());
        assertFailureCount(softly, 1);
    }

    @Test
    void isCheckedPass() {
        page.setContent("<input type=checkbox checked></input>");
        Locator locator = page.locator("input");
        softly.assertThat(locator).isChecked();
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void isCheckedFail() {
        page.setContent("<input type=checkbox></input>");
        Locator locator = page.locator("input");
        softly.assertThat(locator).isChecked(new LocatorAssertions.IsCheckedOptions().setTimeout(1000));
        AssertionFailedError e = assertThrows(AssertionFailedError.class, () -> softly.assertAll());
        assertTrue(e.getMessage().contains("Locator expected to be checked"), e.getMessage());
        assertFailureCount(softly, 1);
    }

    @Test
    void notIsCheckedFail() {
        page.setContent("<input type=checkbox checked></input>");
        Locator locator = page.locator("input");
        softly.assertThat(locator).not().isChecked(new LocatorAssertions.IsCheckedOptions().setTimeout(1000));
        AssertionFailedError e = assertThrows(AssertionFailedError.class, () -> softly.assertAll());
        assertTrue(e.getMessage().contains("Locator expected not to be checked"), e.getMessage());
        assertFailureCount(softly, 1);
    }

    @Test
    void isCheckedFalsePass() {
        page.setContent("<input type=checkbox></input>");
        Locator locator = page.locator("input");
        softly.assertThat(locator).isChecked(new LocatorAssertions.IsCheckedOptions().setChecked(false));
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void isCheckedFalseFail() {
        page.setContent("<input checked type=checkbox></input>");
        Locator locator = page.locator("input");
        softly.assertThat(locator).isChecked(new LocatorAssertions.IsCheckedOptions().setChecked(false).setTimeout(1000));
        AssertionFailedError error = assertThrows(AssertionFailedError.class, () -> softly.assertAll());
        assertTrue(error.getMessage().contains("Locator expected to be unchecked"), error.getMessage());
        assertFailureCount(softly, 1);
    }

    @Test
    void isDisabledPass() {
        page.setContent("<button disabled>Text</button>");
        Locator locator = page.locator("button");
        softly.assertThat(locator).isDisabled();
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void isDisabledFail() {
        page.setContent("<button>Text</button>");
        Locator locator = page.locator("button");
        softly.assertThat(locator).isDisabled(new LocatorAssertions.IsDisabledOptions().setTimeout(1000));
        AssertionFailedError e = assertThrows(AssertionFailedError.class, () -> softly.assertAll());
        assertTrue(e.getMessage().contains("Locator expected to be disabled"), e.getMessage());
        assertFailureCount(softly, 1);
    }

    @Test
    void notIsDisabledFail() {
        page.setContent("<button disabled>Text</button>");
        Locator locator = page.locator("button");
        softly.assertThat(locator).not().isDisabled(new LocatorAssertions.IsDisabledOptions().setTimeout(1000));
        AssertionFailedError e = assertThrows(AssertionFailedError.class, () -> softly.assertAll());
        assertTrue(e.getMessage().contains("Locator expected not to be disabled"), e.getMessage());
        assertFailureCount(softly, 1);
    }

    @Test
    void isEditablePass() {
        page.setContent("<input></input>");
        Locator locator = page.locator("input");
        softly.assertThat(locator).isEditable();
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void isEditableFail() {
        page.setContent("<input disabled></input>");
        Locator locator = page.locator("input");
        softly.assertThat(locator).isEditable(new LocatorAssertions.IsEditableOptions().setTimeout(1000));
        AssertionFailedError e = assertThrows(AssertionFailedError.class, () -> softly.assertAll());
        assertTrue(e.getMessage().contains("Locator expected to be editable"), e.getMessage());
        assertFailureCount(softly, 1);
    }

    @Test
    void isEditableFalseFail() {
        page.setContent("<input></input>");
        Locator locator = page.locator("input");
        softly.assertThat(locator).isEditable(new LocatorAssertions.IsEditableOptions().setEditable(false).setTimeout(1000));
        AssertionFailedError error = assertThrows(AssertionFailedError.class, () -> softly.assertAll());
        assertTrue(error.getMessage().contains("Locator expected to be readonly"), error.getMessage());
        assertFailureCount(softly, 1);
    }

    @Test
    void notIsEditableFail() {
        page.setContent("<input></input>");
        Locator locator = page.locator("input");
        softly.assertThat(locator).not().isEditable(new LocatorAssertions.IsEditableOptions().setTimeout(1000));
        AssertionFailedError e = assertThrows(AssertionFailedError.class, () -> softly.assertAll());
        assertTrue(e.getMessage().contains("Locator expected not to be editable"), e.getMessage());
        assertFailureCount(softly, 1);
    }

    @Test
    void isEditableWithNot() {
        page.setContent("<input readonly></input>");
        Locator locator = page.locator("input");
        softly.assertThat(locator).not().isEditable();
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void isEditableWithEditableTrue() {
        page.setContent("<input></input>");
        Locator locator = page.locator("input");
        softly.assertThat(locator).isEditable(new LocatorAssertions.IsEditableOptions().setEditable(true));
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void isEditableWithEditableFalse() {
        page.setContent("<input readonly></input>");
        Locator locator = page.locator("input");
        softly.assertThat(locator).isEditable(new LocatorAssertions.IsEditableOptions().setEditable(false));
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void isEditableWithNotAndEditableFalse() {
        page.setContent("<input></input>");
        Locator locator = page.locator("input");
        softly.assertThat(locator).not().isEditable(new LocatorAssertions.IsEditableOptions().setEditable(false));
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void isEmptyPass() {
        page.setContent("<input></input>");
        Locator locator = page.locator("input");
        softly.assertThat(locator).isEmpty();
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void isEmptyFail() {
        page.setContent("<input value=text></input>");
        Locator locator = page.locator("input");
        softly.assertThat(locator).isEmpty(new LocatorAssertions.IsEmptyOptions().setTimeout(1000));
        AssertionFailedError e = assertThrows(AssertionFailedError.class, () -> softly.assertAll());
        assertTrue(e.getMessage().contains("Locator expected to be empty"), e.getMessage());
        assertFailureCount(softly, 1);
    }

    @Test
    void notIsEmptyFail() {
        page.setContent("<input></input>");
        Locator locator = page.locator("input");
        softly.assertThat(locator).not().isEmpty(new LocatorAssertions.IsEmptyOptions().setTimeout(1000));
        AssertionFailedError e = assertThrows(AssertionFailedError.class, () -> softly.assertAll());
        assertTrue(e.getMessage().contains("Locator expected not to be empty"), e.getMessage());
        assertFailureCount(softly, 1);
    }

    @Test
    void isEnabledPass() {
        page.setContent("<button>Text</button>");
        Locator locator = page.locator("button");
        softly.assertThat(locator).isEnabled();
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void isEnabledFail() {
        page.setContent("<button disabled>Text</button>");
        Locator locator = page.locator("button");
        softly.assertThat(locator).isEnabled(new LocatorAssertions.IsEnabledOptions().setTimeout(1000));
        AssertionFailedError e = assertThrows(AssertionFailedError.class, () -> softly.assertAll());
        assertTrue(e.getMessage().contains("Locator expected to be enabled"), e.getMessage());
        assertFailureCount(softly, 1);
    }

    @Test
    void notIsEnabledFail() {
        page.setContent("<button>Text</button>");
        Locator locator = page.locator("button");
        softly.assertThat(locator).not().isEnabled(new LocatorAssertions.IsEnabledOptions().setTimeout(1000));
        AssertionFailedError e = assertThrows(AssertionFailedError.class, () -> softly.assertAll());
        assertTrue(e.getMessage().contains("Locator expected not to be enabled"), e.getMessage());
        assertFailureCount(softly, 1);
    }

    @Test
    void isEnabledTrue() {
        page.setContent("<button>Text</button>");
        Locator locator = page.locator("button");
        softly.assertThat(locator).isEnabled(new LocatorAssertions.IsEnabledOptions().setEnabled(true));
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void isEnabledFalse() {
        page.setContent("<button disabled>Text</button>");
        Locator locator = page.locator("button");
        softly.assertThat(locator).isEnabled(new LocatorAssertions.IsEnabledOptions().setEnabled(false));
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void isEnabledFalseFail() {
        page.setContent("<button>Text</button>");
        Locator locator = page.locator("button");
        softly.assertThat(locator).isEnabled(new LocatorAssertions.IsEnabledOptions().setEnabled(false).setTimeout(1000));
        AssertionFailedError error = assertThrows(AssertionFailedError.class, () -> softly.assertAll());
        assertTrue(error.getMessage().contains("Locator expected to be disabled"), error.getMessage());
        assertFailureCount(softly, 1);
    }

    @Test
    void isEnabledEventually() {
        page.setContent("<button disabled>Text</button>");
        Locator locator = page.locator("button");
        locator.evaluate("e => setTimeout(() => {\n" +
                "  e.removeAttribute('disabled');\n" +
                "}, 500);\n");
        softly.assertThat(locator).isEnabled();
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void isEnabledEventuallyWithNot() {
        page.setContent("<button>Text</button>");
        Locator locator = page.locator("button");
        locator.evaluate("e => setTimeout(() => {\n" +
                "  e.setAttribute('disabled', '');\n" +
                "}, 500);\n");
        softly.assertThat(locator).not().isEnabled();
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void isEnabledWithNotAndEnabledFalse() {
        page.setContent("<button>Text</button>");
        Locator locator = page.locator("button");
        softly.assertThat(locator).not().isEnabled(new LocatorAssertions.IsEnabledOptions().setEnabled(false));
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void isFocusedPass() {
        page.setContent("<input></input>");
        Locator locator = page.locator("input");
        locator.focus();
        softly.assertThat(locator).isFocused();
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void isFocusedFail() {
        page.setContent("<input></input>");
        Locator locator = page.locator("input");
        softly.assertThat(locator).isFocused(new LocatorAssertions.IsFocusedOptions().setTimeout(1000));
        AssertionFailedError e = assertThrows(AssertionFailedError.class, () -> softly.assertAll());
        assertTrue(e.getMessage().contains("Locator expected to be focused"), e.getMessage());
        assertFailureCount(softly, 1);
    }

    @Test
    void notIsFocusedFail() {
        page.setContent("<input></input>");
        Locator locator = page.locator("input");
        locator.focus();
        softly.assertThat(locator).not().isFocused(new LocatorAssertions.IsFocusedOptions().setTimeout(1000));
        AssertionFailedError e = assertThrows(AssertionFailedError.class, () -> softly.assertAll());
        assertNull(e.getExpected());
        assertNull(e.getActual());
        assertTrue(e.getMessage().contains("Locator expected not to be focused"), e.getMessage());
        assertFailureCount(softly, 1);
    }

    @Test
    void isHiddenPass() {
        page.setContent("<button style='display: none'></button>");
        Locator locator = page.locator("button");
        softly.assertThat(locator).isHidden();
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void isHiddenFail() {
        page.setContent("<button></button>");
        Locator locator = page.locator("button");
        softly.assertThat(locator).isHidden(new LocatorAssertions.IsHiddenOptions().setTimeout(1000));
        AssertionFailedError e = assertThrows(AssertionFailedError.class, () -> softly.assertAll());
        assertTrue(e.getMessage().contains("Locator expected to be hidden"), e.getMessage());
        assertFailureCount(softly, 1);
    }

    @Test
    void notIsHiddenFail() {
        page.setContent("<button style='display: none'></button>");
        Locator locator = page.locator("button");
        softly.assertThat(locator).not().isHidden(new LocatorAssertions.IsHiddenOptions().setTimeout(1000));
        AssertionFailedError e = assertThrows(AssertionFailedError.class, () -> softly.assertAll());
        assertTrue(e.getMessage().contains("Locator expected not to be hidden"), e.getMessage());
        assertFailureCount(softly, 1);
    }

    @Test
    void isVisiblePass() {
        page.setContent("<input></input>");
        Locator locator = page.locator("input");
        softly.assertThat(locator).isVisible();
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void isVisibleFail() {
        page.setContent("<input style='display: none'></input>");
        Locator locator = page.locator("input");
        softly.assertThat(locator).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(1000));
        AssertionFailedError e = assertThrows(AssertionFailedError.class, () -> softly.assertAll());
        assertTrue(e.getMessage().contains("Locator expected to be visible"), e.getMessage());
        assertFailureCount(softly, 1);
    }

    @Test
    void isVisibleFalseFail() {
        page.setContent("<input></input>");
        Locator locator = page.locator("input");
        softly.assertThat(locator).isVisible(new LocatorAssertions.IsVisibleOptions().setVisible(false).setTimeout(1000));
        AssertionFailedError error = assertThrows(AssertionFailedError.class, () -> softly.assertAll());
        assertTrue(error.getMessage().contains("Locator expected to be hidden"), error.getMessage());
        assertFailureCount(softly, 1);
    }

    @Test
    void notIsVisibleFail() {
        page.setContent("<input></input>");
        Locator locator = page.locator("input");
        softly.assertThat(locator).not().isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(1000));
        AssertionFailedError e = assertThrows(AssertionFailedError.class, () -> softly.assertAll());
        assertTrue(e.getMessage().contains("Locator expected not to be visible"), e.getMessage());
        assertFailureCount(softly, 1);
    }

    @Test
    void isVisibleWithTrue() {
        page.setContent("<button>hello</button>");
        Locator locator = page.locator("button");
        softly.assertThat(locator).isVisible(new LocatorAssertions.IsVisibleOptions().setVisible(true));
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void isVisibleWithFalse() {
        page.setContent("<button hidden>hello</button>");
        Locator locator = page.locator("button");
        softly.assertThat(locator).isVisible(new LocatorAssertions.IsVisibleOptions().setVisible(false));
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void isVisibleWithNotAndFalse() {
        page.setContent("<button>hello</button>");
        Locator locator = page.locator("button");
        softly.assertThat(locator).not().isVisible(new LocatorAssertions.IsVisibleOptions().setVisible(false));
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void isVisibleEventually() {
        page.setContent("<div></div>");
        Locator locator = page.locator("span");
        page.evalOnSelector("div", "div => setTimeout(() => {\n" +
                "      div.innerHTML = '<span>Hello</span>';\n" +
                "    }, 10);");
        softly.assertThat(locator).isVisible();
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void isVisibleEventuallyWithNot() {
        page.setContent("<div><span>Hello</span></div>");
        Locator locator = page.locator("span");
        page.evalOnSelector("span", "span => setTimeout(() => {\n" +
                "      span.textContent = '';\n" +
                "    }, 10);");
        softly.assertThat(locator).not().isVisible();
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void locatorCountShouldWorkWithDeletedMapInMainWorld() {
        page.evaluate("Map = 1");
        page.locator("#searchResultTableDiv .x-grid3-row").count();
        softly.assertThat(page.locator("#searchResultTableDiv .x-grid3-row")).hasCount(0);
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void defaultTimeoutHasTextFail() {
        page.setContent("<div></div>");
        Locator locator = page.locator("div");
        PlaywrightAssertions.setDefaultAssertionTimeout(1000);
        softly.assertThat(locator).hasText("foo");
        AssertionFailedError exception = assertThrows(AssertionFailedError.class, () -> softly.assertAll());
        assertTrue(exception.getMessage().contains("Locator.expect with timeout 1000ms"), exception.getMessage());
        // Restore default.
        PlaywrightAssertions.setDefaultAssertionTimeout(5_000);
        assertFailureCount(softly, 1);
    }

    @Test
    void defaultTimeoutHasTextPass() {
        page.setContent("<div>foo</div>");
        Locator locator = page.locator("div");
        PlaywrightAssertions.setDefaultAssertionTimeout(1000);
        softly.assertThat(locator).hasText("foo");
        softly.assertAll();
        // Restore default.
        PlaywrightAssertions.setDefaultAssertionTimeout(5_000);
        assertFailureCount(softly, 0);
    }

    @Test
    void defaultTimeoutZeroHasTextPass() {
        page.setContent("<div>foo</div>");
        Locator locator = page.locator("div");
        PlaywrightAssertions.setDefaultAssertionTimeout(0);
        softly.assertThat(locator).hasText("foo");
        softly.assertAll();
        // Restore default.
        PlaywrightAssertions.setDefaultAssertionTimeout(5_000);
        assertFailureCount(softly, 0);
    }

    @Test
    void isAttachedDefault() {
        page.setContent("<input></input>");
        Locator locator = page.locator("input");
        softly.assertThat(locator).isAttached();
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void isAttachedWithHiddenElement() {
        page.setContent("<button style='display:none'>hello</button>");
        Locator locator = page.locator("button");
        softly.assertThat(locator).isAttached();
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void isAttachedWithNot() {
        page.setContent("<button>hello</button>");
        Locator locator = page.locator("input");
        softly.assertThat(locator).not().isAttached();
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void isAttachedWithAttachedTrue() {
        page.setContent("<button>hello</button>");
        Locator locator = page.locator("button");
        softly.assertThat(locator).isAttached(new LocatorAssertions.IsAttachedOptions().setAttached(true));
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void isAttachedWithAttachedFalse() {
        page.setContent("<button>hello</button>");
        Locator locator = page.locator("input");
        softly.assertThat(locator).isAttached(new LocatorAssertions.IsAttachedOptions().setAttached(false));
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void isAttachedWithNotAndAttachedFalse() {
        page.setContent("<button>hello</button>");
        Locator locator = page.locator("button");
        softly.assertThat(locator).not().isAttached(new LocatorAssertions.IsAttachedOptions().setAttached(false));
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void isAttachedEventually() {
        page.setContent("<div></div>");
        Locator locator = page.locator("span");
        page.evalOnSelector("div", "div => setTimeout(() => {\n" +
                "      div.innerHTML = '<span>Hello</span>'\n" +
                "    }, 100)");
        softly.assertThat(locator).isAttached();
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void isAttachedEventuallyWithNot() {
        page.setContent("<div><span>Hello</span></div>");
        Locator locator = page.locator("span");
        page.evalOnSelector("div", "div => setTimeout(() => {\n" +
                "      div.textContent = '';\n" +
                "    }, 0)");
        softly.assertThat(locator).not().isAttached();
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void isAttachedFail() {
        page.setContent("<button>Hello</button>");
        Locator locator = page.locator("input");
        softly.assertThat(locator).isAttached(new LocatorAssertions.IsAttachedOptions().setTimeout(1000));
        AssertionFailedError error = assertThrows(AssertionFailedError.class, () -> softly.assertAll());
        assertFalse(error.getMessage().contains("locator resolved to"), error.getMessage());
        assertFailureCount(softly, 1);
    }

    @Test
    void isAttachedFailWithNot() {
        page.setContent("<input></input>");
        Locator locator = page.locator("input");
        softly.assertThat(locator).not().isAttached(new LocatorAssertions.IsAttachedOptions().setTimeout(1000));
        AssertionFailedError error = assertThrows(AssertionFailedError.class, () -> softly.assertAll());
        assertTrue(error.getMessage().contains("locator resolved to <input/>"), error.getMessage());
        assertFailureCount(softly, 1);
    }

    @Test
    void isAttachedWithImpossibleTimeout() {
        page.setContent("<div id=node>Text content</div>");
        softly.assertThat(page.locator("#node")).isAttached(new LocatorAssertions.IsAttachedOptions().setTimeout(1));
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void isAttachedWithImpossibleTimeoutNot() {
        page.setContent("<div id=node>Text content</div>");
        softly.assertThat(page.locator("no-such-thing")).not().isAttached(new LocatorAssertions.IsAttachedOptions().setTimeout(1));
        softly.assertAll();
        assertFailureCount(softly, 0);
    }
}
