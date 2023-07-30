package io.github.uchagani.playwright.assertions;

import io.github.uchagani.jp.BrowserConfig;
import io.github.uchagani.jp.PlaywrightBrowserConfig;

public class DefaultBrowserConfig implements PlaywrightBrowserConfig {
    @Override
    public io.github.uchagani.jp.BrowserConfig getBrowserConfig() {
        return new BrowserConfig()
                .chromium()
                .launch();
    }
}
