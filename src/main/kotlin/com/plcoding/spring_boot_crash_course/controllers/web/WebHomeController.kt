package com.plcoding.spring_boot_crash_course.controllers.web

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class WebHomeController {

    @GetMapping("/web/home")
    fun home(): String {
        return "home"
    }
}
