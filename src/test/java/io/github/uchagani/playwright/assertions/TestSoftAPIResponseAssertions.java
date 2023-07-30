package io.github.uchagani.playwright.assertions;

import com.github.tomakehurst.wiremock.http.Body;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import io.github.uchagani.jp.UseBrowserConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.opentest4j.AssertionFailedError;

import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static io.github.uchagani.playwright.assertions.Utils.assertFailureCount;
import static org.junit.jupiter.api.Assertions.*;

@UseBrowserConfig(DefaultBrowserConfig.class)
public class TestSoftAPIResponseAssertions {
    private APIRequestContext request;
    private SoftAssertions softly;
    private String path;

    @RegisterExtension
    WireMockExtension wireMock = WireMockExtension.newInstance().options(wireMockConfig().dynamicPort()).build();

    @BeforeEach
    void beforeEach(Browser browser) {
        Browser.NewContextOptions options = new Browser.NewContextOptions()
                .setBaseURL(wireMock.getRuntimeInfo().getHttpBaseUrl());
        BrowserContext browserContext = browser.newContext(options);
        this.request = browserContext.newPage().request();
        this.softly = SoftAssertions.create();
        this.path = "/" + UUID.randomUUID();
    }

    @Test
    void passWithResponse() {
        wireMock.stubFor(get(path).willReturn(ok()));
        APIResponse res = request.get(path);
        softly.assertThat(res).isOK();
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void passWithNot() {
        wireMock.stubFor(get(path).willReturn(notFound()));
        APIResponse res = request.get(path);
        softly.assertThat(res).not().isOK();
        softly.assertAll();
        assertFailureCount(softly, 0);
    }

    @Test
    void failWithNot() {
        wireMock.stubFor(get(path).willReturn(ok()));
        APIResponse res = request.get(path);
        softly.assertThat(res).not().isOK();
        AssertionFailedError e = assertThrows(AssertionFailedError.class, () -> softly.assertAll());
        assertTrue(e.getMessage().contains("Response status expected not to be within [200..299] range, was 200"));
        assertFailureCount(softly, 1);
    }

    @Test
    void fail() {
        wireMock.stubFor(get(path).willReturn(notFound()));
        APIResponse res = request.get(path);
        softly.assertThat(res).isOK();
        AssertionFailedError e = assertThrows(AssertionFailedError.class, () -> softly.assertAll());
        assertTrue(e.getMessage().contains("→ GET " + wireMock.getRuntimeInfo().getHttpBaseUrl() + path), "Actual error: " + e);
        assertTrue(e.getMessage().contains("← 404 Not Found"), "Actual error: " + e);
        assertFailureCount(softly, 1);
    }

    @Test
    void shouldPrintResponseTextIfIsOkFails() {
        wireMock.stubFor(get(path).willReturn(notFound()
                .withHeader("Content-Type", "text/plain")
                .withResponseBody(Body.fromOneOf(null, "File not found", null, null))));
        APIResponse res = request.get(path);
        softly.assertThat(res).isOK();
        AssertionFailedError e = assertThrows(AssertionFailedError.class, () -> softly.assertAll());
        assertTrue(e.getMessage().contains("File not found"), "Actual error: " + e);
        assertFailureCount(softly, 1);
    }

    @Test
    void shouldOnlyPrintResponseWithTextContentTypeIfIsOkFails() {
        StubMapping stub = wireMock.stubFor(get(path).willReturn(notFound()
                .withResponseBody(Body.fromOneOf(null, "Text error", null, null))
                .withHeader("Content-type", "text/plain")));
        softly.assertThat(request.get(path)).isOK();
        wireMock.removeStub(stub);

        stub = wireMock.stubFor(get(path).willReturn(notFound()
                .withStatusMessage("Json error")
                .withHeader("Content-type", "image/svg+xml")));
        softly.assertThat(request.get(path)).isOK();
        wireMock.removeStub(stub);

        stub = wireMock.stubFor(get(path).willReturn(notFound()
                .withResponseBody(Body.fromOneOf(null, "No content type error", null, null))));
        softly.assertThat(request.get(path)).isOK();
        wireMock.removeStub(stub);

        wireMock.stubFor(get(path).willReturn(notFound().withHeader("Content-type", "image/bmp")
                .withResponseBody(Body.fromOneOf(null, "Image type error", null, null))));
        softly.assertThat(request.get(path)).isOK();

        AssertionFailedError e = assertThrows(AssertionFailedError.class, () -> softly.assertAll());
        assertTrue(e.getMessage().contains("Text error"));
        assertTrue(e.getMessage().contains("Json error"));
        assertFalse(e.getMessage().contains("No content type error"));
        assertFalse(e.getMessage().contains("Image type error"));
        assertFailureCount(softly, 4);
    }
}
