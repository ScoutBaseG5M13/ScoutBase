package com.empresa.scoutbase

import com.empresa.scoutbase.viewmodel.login.LoginViewModel
import org.junit.Assert.*
import org.junit.Test

/**
 * Prova unitària que comprova l'estat inicial del LoginViewModel.
 */
class LoginViewModelInitialStateTest {

    @Test
    fun estat_inicial_viewmodel() {
        val vm = LoginViewModel()

        assertFalse(vm.loading.value)
        assertNull(vm.error.value)
        assertNull(vm.token.value)
        assertNull(vm.role.value)
    }
}

