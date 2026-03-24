package com.empresa.scoutbase

import com.empresa.scoutbase.viewmodel.login.LoginViewModel
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Prova unitària que comprova que setError() actualitza correctament
 * el missatge d'error del ViewModel.
 */
class LoginViewModelSetErrorTest {

    @Test
    fun setError_actualitza_missatge_error() {
        val vm = LoginViewModel()

        vm.setError("Error de prova")

        assertEquals("Error de prova", vm.error.value)
    }
}
