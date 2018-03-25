package de.mkammerer.pocketknife.pocketknife

import org.springframework.stereotype.Component
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver
import java.io.PrintWriter
import java.io.StringWriter
import java.lang.Exception
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class ErrorHandler : DefaultHandlerExceptionResolver() {
    override fun doResolveException(request: HttpServletRequest, response: HttpServletResponse, handler: Any?, ex: Exception): ModelAndView? {
        val result = super.doResolveException(request, response, handler, ex)

        if (result != null) return result

        logger.error("Unhandled exception occurred", ex)
        return ModelAndView("error", mapOf("model" to Model(extractStacktrace(ex))))
    }

    data class Model(val stacktrace: String)

    private fun extractStacktrace(ex: Exception): String {
        return StringWriter().use {
            PrintWriter(it).use {
                ex.printStackTrace(it)
            }
            it.toString()
        }
    }
}