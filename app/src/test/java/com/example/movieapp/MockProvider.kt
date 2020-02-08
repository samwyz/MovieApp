package com.example.movieapp

import org.mockito.ArgumentCaptor
import org.mockito.Mockito


object MockProvider {

    /**
     * Helps mocking generic interfaces.
     */
    inline fun <reified T : Any> mock(): T = Mockito.mock(T::class.java)

    /**
     * Helps getting an argument captor for a given class
     */
    inline fun <reified T : Any> argumentCaptor(): ArgumentCaptor<T> = ArgumentCaptor.forClass(T::class.java)


}