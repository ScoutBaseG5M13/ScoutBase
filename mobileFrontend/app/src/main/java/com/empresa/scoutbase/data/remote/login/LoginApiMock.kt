package com.empresa.scoutbase.data.remote.mock

import com.empresa.scoutbase.model.login.ApiResponse

object LoginApiMock {

    fun getUserRoleMock(): ApiResponse<String> {
        return ApiResponse(
            success = true,
            message = "Mock role",
            data = "ROLE_ADMIN", // o ROLE_USER, el que necessitis
            sessionId = "mock-session",
            timestamp = "mock-timestamp"
        )
    }
}
