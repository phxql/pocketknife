package de.mkammerer.pocketknife.pocketknife

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView

@Controller
@RequestMapping("/")
class Index {
    @GetMapping
    fun index(): ModelAndView {
        return ModelAndView("index")
    }
}