package com.mandalorian.chatapp

import com.mandalorian.chatapp.utils.ValidationUtil
import junit.framework.TestCase.assertEquals
import org.junit.Test

class ValidationUtilTest {
    @Test
    fun `should return false if email is empty`() {
        assertEquals(ValidationUtil.validateEmail(""), false)
    }

    @Test
    fun `should return false if @ is not included`() {
        assertEquals(ValidationUtil.validateEmail("abc.com"), false)
    }

    @Test
    fun `should return false if email starts with special character`() {
        assertEquals(ValidationUtil.validateEmail(".abc@abc.com"), false)
    }

    @Test
    fun `email should not contain any special character other than @ and dot`() {
        assertEquals(ValidationUtil.validateEmail("abc#$%aa@abc.com"), false)
    }

    @Test
    fun `valid email should pass the test`() {
        assertEquals(ValidationUtil.validateEmail("a@a.com"), true)
    }

    @Test
    fun `username containing only alphanumeric characters should pass the test`() {
        assertEquals(ValidationUtil.validateUsername("khayrul123"), true)
    }

    @Test
    fun `username should contain only alphanumeric characters`() {
        assertEquals(ValidationUtil.validateUsername("%#khayrul123"), false)
    }
}