package com.gde.university.data.database

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ConvertersTest {

    private lateinit var converters: Converters

    @Before
    fun setUp() {
        converters = Converters()
    }

    @Test
    fun `fromList should convert list to JSON string`() {
        val list = listOf("one", "two", "three")
        val result = converters.fromList(list)
        assertEquals("[\"one\",\"two\",\"three\"]", result)
    }

    @Test
    fun `fromString should convert JSON string to list`() {
        val json = "[\"one\",\"two\",\"three\"]"
        val result = converters.fromString(json)
        assertEquals(listOf("one", "two", "three"), result)
    }
}
