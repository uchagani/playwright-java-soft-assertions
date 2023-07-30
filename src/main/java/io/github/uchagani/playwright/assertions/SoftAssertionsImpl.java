package io.github.uchagani.playwright.assertions;

import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.APIResponseAssertions;
import com.microsoft.playwright.assertions.LocatorAssertions;
import com.microsoft.playwright.assertions.PageAssertions;
import org.opentest4j.AssertionFailedError;

import java.util.ArrayList;
import java.util.List;

class SoftAssertionsImpl implements SoftAssertions {
    final List<Throwable> results;

    SoftAssertionsImpl() {
        this.results = new ArrayList<>();
    }

    @Override
    public PageAssertions assertThat(Page page) {
        return new PageAssertionsImplProxy(page, results);
    }

    @Override
    public LocatorAssertions assertThat(Locator locator) {
        return new LocatorAssertionsImplProxy(locator, results);
    }

    @Override
    public APIResponseAssertions assertThat(APIResponse response) {
        return new APIResponseAssertionsImplProxy(response, results);
    }

    @Override
    public void assertAll() {
        if (!results.isEmpty()) {
            throw new AssertionFailedError(getFormattedErrorMessage());
        }
    }

    private String getFormattedErrorMessage() {
        StringBuilder message = new StringBuilder();
        message
                .append(results.size())
                .append(" assertion(s) failed:");

        for (Throwable t : results) {
            message.append("\n");
            message.append("----------------------------------------\n");
            message.append(t.getMessage());
        }

        return message.toString();
    }
}
