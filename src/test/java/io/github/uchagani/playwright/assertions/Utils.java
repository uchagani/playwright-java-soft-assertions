package io.github.uchagani.playwright.assertions;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Utils {
    static void assertFailureCount(SoftAssertions softAssertions, int expectedCount) {
        List<Throwable> results = ((SoftAssertionsImpl) softAssertions).results;
        assertEquals(results.size(), expectedCount);
    }

    static <K, V> Map<K, V> mapOf(Object... keyValuePairs) {
        if (keyValuePairs.length % 2 != 0) {
            throw new IllegalArgumentException("Number of arguments must be even.");
        }

        Map<K, V> map = new HashMap<>();
        for (int i = 0; i < keyValuePairs.length; i += 2) {
            K key = (K) keyValuePairs[i];
            V value = (V) keyValuePairs[i + 1];
            map.put(key, value);
        }
        return Collections.unmodifiableMap(map);
    }
}
