package io.github.uchagani.playwright.assertions;

import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.APIResponseAssertions;
import com.microsoft.playwright.assertions.LocatorAssertions;
import com.microsoft.playwright.assertions.PageAssertions;

public interface SoftAssertions {
    PageAssertions assertThat(Page page);
    LocatorAssertions assertThat(Locator locator);

    APIResponseAssertions assertThat(APIResponse response);

    void assertAll();

    static SoftAssertions create() {
        return new SoftAssertionsImpl();
    }
}
