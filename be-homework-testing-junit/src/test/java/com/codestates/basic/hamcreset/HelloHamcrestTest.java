package com.codestates.basic.hamcreset;

import com.codestates.CryptoCurrency;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class HelloHamcrestTest {

    @DisplayName("Hello Junit Test using hamcrest")
    @Test
    public void assertionTest1(){
        String expected = "Hello";
        String actual = "Hello";
//        String actual = "Hello Junit";

        assertThat(actual, is(equalTo(expected)));
    }

    @DisplayName("AssertionNull() Test")
    @Test
    public void assertNotNullTest() {
        String currencyName = getCryptoCurrency("ETH");

        assertThat(currencyName, is(notNullValue()));   // (1)
//        assertThat(currencyName, is(nullValue()));    // (2)
    }

    @DisplayName("throws NullPointerException when map.get()")
    @Test
    public void assertionThrowExceptionTest() {
        Throwable actualException = assertThrows(NullPointerException.class,
                () -> getCryptoCurrencyUpper("XRP"));   // (1)

        assertThat(actualException.getClass(), is(NullPointerException.class));  // (2)
    }

    private String getCryptoCurrencyUpper(String unit) {
        return CryptoCurrency.map.get(unit).toUpperCase();
    }

    private String getCryptoCurrency(String unit) {
        return CryptoCurrency.map.get(unit);
    }
}
