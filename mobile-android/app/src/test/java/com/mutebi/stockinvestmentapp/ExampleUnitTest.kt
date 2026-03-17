package com.mutebi.stockinvestmentapp

import com.mutebi.stockinvestmentapp.core.utils.Validator
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun emailValidator_works() {
        assertTrue(Validator.isValidEmail("user@example.com"))
    }
}