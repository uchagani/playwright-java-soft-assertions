package io.github.uchagani.playwright.assertions;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.assertions.LocatorAssertions;
import com.microsoft.playwright.impl.LocatorAssertionsImpl;

import java.util.List;
import java.util.regex.Pattern;

public class LocatorAssertionsImplProxy extends SoftAssertionsBase implements LocatorAssertions {
    private final LocatorAssertionsImpl locatorAssertions;

    LocatorAssertionsImplProxy(Locator locator, List<Throwable> results) {
        this(results, new LocatorAssertionsImpl(locator));
    }

    private LocatorAssertionsImplProxy(List<Throwable> results, LocatorAssertionsImpl locatorAssertions) {
        super(results);
        this.locatorAssertions = locatorAssertions;
    }

    @Override
    public LocatorAssertions not() {
        return new LocatorAssertionsImplProxy(super.results, (LocatorAssertionsImpl) locatorAssertions.not());
    }

    @Override
    public void isAttached(IsAttachedOptions isAttachedOptions) {
        assertAndCaptureResult(() -> locatorAssertions.isAttached(isAttachedOptions));
    }

    @Override
    public void isChecked(IsCheckedOptions isCheckedOptions) {
        assertAndCaptureResult(() -> locatorAssertions.isChecked(isCheckedOptions));
    }

    @Override
    public void isDisabled(IsDisabledOptions isDisabledOptions) {
        assertAndCaptureResult(() -> locatorAssertions.isDisabled(isDisabledOptions));
    }

    @Override
    public void isEditable(IsEditableOptions isEditableOptions) {
        assertAndCaptureResult(() -> locatorAssertions.isEditable(isEditableOptions));
    }

    @Override
    public void isEmpty(IsEmptyOptions isEmptyOptions) {
        assertAndCaptureResult(() -> locatorAssertions.isEmpty(isEmptyOptions));
    }

    @Override
    public void isEnabled(IsEnabledOptions isEnabledOptions) {
        assertAndCaptureResult(() -> locatorAssertions.isEnabled(isEnabledOptions));
    }

    @Override
    public void isFocused(IsFocusedOptions isFocusedOptions) {
        assertAndCaptureResult(() -> locatorAssertions.isFocused(isFocusedOptions));
    }

    @Override
    public void isHidden(IsHiddenOptions isHiddenOptions) {
        assertAndCaptureResult(() -> locatorAssertions.isHidden(isHiddenOptions));
    }

    @Override
    public void isInViewport(IsInViewportOptions isInViewportOptions) {
        assertAndCaptureResult(() -> locatorAssertions.isInViewport(isInViewportOptions));
    }

    @Override
    public void isVisible(IsVisibleOptions isVisibleOptions) {
        assertAndCaptureResult(() -> locatorAssertions.isVisible(isVisibleOptions));
    }

    @Override
    public void containsText(String s, ContainsTextOptions containsTextOptions) {
        assertAndCaptureResult(() -> locatorAssertions.containsText(s, containsTextOptions));
    }

    @Override
    public void containsText(Pattern pattern, ContainsTextOptions containsTextOptions) {
        assertAndCaptureResult(() -> locatorAssertions.containsText(pattern, containsTextOptions));
    }

    @Override
    public void containsText(String[] strings, ContainsTextOptions containsTextOptions) {
        assertAndCaptureResult(() -> locatorAssertions.containsText(strings, containsTextOptions));
    }

    @Override
    public void containsText(Pattern[] patterns, ContainsTextOptions containsTextOptions) {
        assertAndCaptureResult(() -> locatorAssertions.containsText(patterns, containsTextOptions));
    }

    @Override
    public void hasAttribute(String s, String s1, HasAttributeOptions hasAttributeOptions) {
        assertAndCaptureResult(() -> locatorAssertions.hasAttribute(s, s1, hasAttributeOptions));
    }

    @Override
    public void hasAttribute(String s, Pattern pattern, HasAttributeOptions hasAttributeOptions) {
        assertAndCaptureResult(() -> locatorAssertions.hasAttribute(s, pattern, hasAttributeOptions));
    }

    @Override
    public void hasClass(String s, HasClassOptions hasClassOptions) {
        assertAndCaptureResult(() -> locatorAssertions.hasClass(s, hasClassOptions));
    }

    @Override
    public void hasClass(Pattern pattern, HasClassOptions hasClassOptions) {
        assertAndCaptureResult(() -> locatorAssertions.hasClass(pattern, hasClassOptions));
    }

    @Override
    public void hasClass(String[] strings, HasClassOptions hasClassOptions) {
        assertAndCaptureResult(() -> locatorAssertions.hasClass(strings, hasClassOptions));
    }

    @Override
    public void hasClass(Pattern[] patterns, HasClassOptions hasClassOptions) {
        assertAndCaptureResult(() -> locatorAssertions.hasClass(patterns, hasClassOptions));
    }

    @Override
    public void hasCount(int i, HasCountOptions hasCountOptions) {
        assertAndCaptureResult(() -> locatorAssertions.hasCount(i, hasCountOptions));
    }

    @Override
    public void hasCSS(String s, String s1, HasCSSOptions hasCSSOptions) {
        assertAndCaptureResult(() -> locatorAssertions.hasCSS(s, s1, hasCSSOptions));
    }

    @Override
    public void hasCSS(String s, Pattern pattern, HasCSSOptions hasCSSOptions) {
        assertAndCaptureResult(() -> locatorAssertions.hasCSS(s, pattern, hasCSSOptions));
    }

    @Override
    public void hasId(String s, HasIdOptions hasIdOptions) {
        assertAndCaptureResult(() -> locatorAssertions.hasId(s, hasIdOptions));
    }

    @Override
    public void hasId(Pattern pattern, HasIdOptions hasIdOptions) {
        assertAndCaptureResult(() -> locatorAssertions.hasId(pattern, hasIdOptions));
    }

    @Override
    public void hasJSProperty(String s, Object o, HasJSPropertyOptions hasJSPropertyOptions) {
        assertAndCaptureResult(() -> locatorAssertions.hasJSProperty(s, o, hasJSPropertyOptions));
    }

    @Override
    public void hasText(String s, HasTextOptions hasTextOptions) {
        assertAndCaptureResult(() -> locatorAssertions.hasText(s, hasTextOptions));
    }

    @Override
    public void hasText(Pattern pattern, HasTextOptions hasTextOptions) {
        assertAndCaptureResult(() -> locatorAssertions.hasText(pattern, hasTextOptions));
    }

    @Override
    public void hasText(String[] strings, HasTextOptions hasTextOptions) {
        assertAndCaptureResult(() -> locatorAssertions.hasText(strings, hasTextOptions));
    }

    @Override
    public void hasText(Pattern[] patterns, HasTextOptions hasTextOptions) {
        assertAndCaptureResult(() -> locatorAssertions.hasText(patterns, hasTextOptions));
    }

    @Override
    public void hasValue(String s, HasValueOptions hasValueOptions) {
        assertAndCaptureResult(() -> locatorAssertions.hasValue(s, hasValueOptions));
    }

    @Override
    public void hasValue(Pattern pattern, HasValueOptions hasValueOptions) {
        assertAndCaptureResult(() -> locatorAssertions.hasValue(pattern, hasValueOptions));
    }

    @Override
    public void hasValues(String[] strings, HasValuesOptions hasValuesOptions) {
        assertAndCaptureResult(() -> locatorAssertions.hasValues(strings, hasValuesOptions));
    }

    @Override
    public void hasValues(Pattern[] patterns, HasValuesOptions hasValuesOptions) {
        assertAndCaptureResult(() -> locatorAssertions.hasValues(patterns, hasValuesOptions));
    }
}
