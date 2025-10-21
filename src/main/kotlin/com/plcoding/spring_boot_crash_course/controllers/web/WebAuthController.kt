package com.plcoding.spring_boot_crash_course.controllers.web

import com.plcoding.spring_boot_crash_course.security.AuthService
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Pattern
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.server.ResponseStatusException

@Controller
@RequestMapping("/web")
class WebAuthController(
    private val authService: AuthService
) {

    data class LoginForm(
        @field:Email(message = "Invalid email format.")
        val email: String = "",
        val password: String = ""
    )

    data class RegisterForm(
        @field:Email(message = "Invalid email format.")
        val email: String = "",
        @field:Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{9,}\$",
            message = "Password must be at least 9 characters long and contain at least one digit, uppercase and lowercase character."
        )
        val password: String = ""
    )

    @GetMapping("/login")
    fun loginPage(model: Model): String {
        model.addAttribute("loginForm", LoginForm())
        return "login"
    }

    @PostMapping("/login")
    fun login(
        @Valid @ModelAttribute loginForm: LoginForm,
        bindingResult: BindingResult,
        model: Model,
        response: HttpServletResponse
    ): String {
        if (bindingResult.hasErrors()) {
            return "login"
        }

        return try {
            val tokenPair = authService.login(loginForm.email, loginForm.password)
            
            // Set access token cookie (15 minutes)
            val accessCookie = Cookie("access_token", tokenPair.accessToken)
            accessCookie.isHttpOnly = true
            accessCookie.path = "/"
            accessCookie.maxAge = 15 * 60 // 15 minutes
            accessCookie.secure = false // Set to true in production with HTTPS
            response.addCookie(accessCookie)
            
            // Set refresh token cookie (30 days)
            val refreshCookie = Cookie("refresh_token", tokenPair.refreshToken)
            refreshCookie.isHttpOnly = true
            refreshCookie.path = "/"
            refreshCookie.maxAge = 30 * 24 * 60 * 60 // 30 days
            refreshCookie.secure = false // Set to true in production with HTTPS
            response.addCookie(refreshCookie)
            
            "redirect:/web/notes"
        } catch (e: BadCredentialsException) {
            model.addAttribute("error", "Invalid credentials.")
            "login"
        } catch (e: Exception) {
            model.addAttribute("error", "An error occurred during login.")
            "login"
        }
    }

    @GetMapping("/register")
    fun registerPage(model: Model): String {
        model.addAttribute("registerForm", RegisterForm())
        return "register"
    }

    @PostMapping("/register")
    fun register(
        @Valid @ModelAttribute registerForm: RegisterForm,
        bindingResult: BindingResult,
        model: Model
    ): String {
        if (bindingResult.hasErrors()) {
            return "register"
        }

        return try {
            authService.register(registerForm.email, registerForm.password)
            model.addAttribute("success", "Registration successful! Please log in.")
            "redirect:/web/login?registered"
        } catch (e: ResponseStatusException) {
            model.addAttribute("error", e.reason ?: "Registration failed.")
            "register"
        } catch (e: Exception) {
            model.addAttribute("error", "An error occurred during registration.")
            "register"
        }
    }
}
