package de.mkammerer.pocketknife.pocketknife.json

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView

@Controller
@RequestMapping("/json/prettifier")
class JsonPrettifier {
    @GetMapping
    fun index(): ModelAndView {
        return ModelAndView("json/prettifier", mapOf(
                "model" to Model("", "", null)
        ))
    }

    @PostMapping("/prettify")
    fun decode(@ModelAttribute("form") form: PrettifyForm): ModelAndView {
        val (pretty, error) = try {
            val mapper = ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT)
            val pretty = mapper.writeValueAsString(mapper.readTree(form.ugly))
            Pair(pretty, null)
        } catch (e: JsonProcessingException) {
            Pair("", e.message)
        }

        return ModelAndView("json/prettifier", mapOf(
                "model" to Model(form.ugly, pretty, error)
        ))
    }

    @PostMapping("/minify")
    fun decode(@ModelAttribute("form") form: MinifyForm): ModelAndView {
        val (ugly, error) = try {
            val mapper = ObjectMapper().disable(SerializationFeature.INDENT_OUTPUT)
            val ugly = mapper.writeValueAsString(mapper.readTree(form.pretty))
            Pair(ugly, null)
        } catch (e: JsonProcessingException) {
            Pair("", e.message)
        }

        return ModelAndView("json/prettifier", mapOf(
                "model" to Model(ugly, form.pretty, error)
        ))
    }

    data class PrettifyForm(val ugly: String)
    data class MinifyForm(val pretty: String)
    data class Model(val ugly: String, val pretty: String, val error: String?)
}