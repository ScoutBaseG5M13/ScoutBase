package com.empresa.scoutbase

import com.empresa.scoutbase.viewmodel.login.LoginViewModel
import org.junit.Assert.assertNull
import org.junit.Test

/**
 * Prova unitària que comprova que logout()
 * esborra el token i el rol de l'usuari.
 */
class LoginViewModelLogoutTest {

    @Test
    fun logout_neteja_token_i_rol() {
        val vm = LoginViewModel()

        // Simulem que l'usuari està autenticat utilitzant la funció de test
        vm.setFakeAuth("fakeToken", "ROLE_USER")

        vm.logout()

        assertNull(vm.token.value)
        assertNull(vm.role.value)
    }
}



