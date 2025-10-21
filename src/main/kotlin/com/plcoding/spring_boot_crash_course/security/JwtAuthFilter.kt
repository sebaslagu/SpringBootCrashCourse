package com.plcoding.spring_boot_crash_course.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthFilter(
    private val jwtService: JwtService
): OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        var token: String? = null
        
        // First, try to get token from Authorization header
        val authHeader = request.getHeader("Authorization")
        if(authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader
        } else {
            // If no header, try to get token from cookie
            val cookies = request.cookies
            if (cookies != null) {
                val accessTokenCookie = cookies.find { it.name == "access_token" }
                if (accessTokenCookie != null) {
                    token = accessTokenCookie.value
                }
            }
        }
        
        // Validate token if found
        if(token != null && jwtService.validateAccessToken(token)) {
            val userId = jwtService.getUserIdFromToken(token)
            val auth = UsernamePasswordAuthenticationToken(userId, null, emptyList())
            SecurityContextHolder.getContext().authentication = auth
        }

        filterChain.doFilter(request, response)
    }
}