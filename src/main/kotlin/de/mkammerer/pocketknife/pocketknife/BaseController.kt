package de.mkammerer.pocketknife.pocketknife

import org.springframework.web.servlet.ModelAndView

abstract class BaseController<T>(
        private val viewName: String
) {
    protected fun view(model: T): ModelAndView =
            ModelAndView(viewName, mapOf("model" to model))
}