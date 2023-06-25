package com.uhuru.auth;

public record AuthenticationRequest(
        String username,
        String password
) {
}
