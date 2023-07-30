package io.github.uchagani.playwright.assertions;

import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.assertions.APIResponseAssertions;
import com.microsoft.playwright.impl.APIResponseAssertionsImpl;

import java.util.List;

class APIResponseAssertionsImplProxy extends SoftAssertionsBase implements APIResponseAssertions {
    private final APIResponseAssertionsImpl assertions;

    APIResponseAssertionsImplProxy(APIResponse response, List<Throwable> results) {
        this(results, new APIResponseAssertionsImpl(response));
    }

    private APIResponseAssertionsImplProxy(List<Throwable> results, APIResponseAssertionsImpl assertions) {
        super(results);
        this.assertions = assertions;
    }

    @Override
    public APIResponseAssertions not() {
        return new APIResponseAssertionsImplProxy(super.results, (APIResponseAssertionsImpl) assertions.not());
    }

    @Override
    public void isOK() {
        assertAndCaptureResult(assertions::isOK);
    }
}
