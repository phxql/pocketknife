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
    private val viewName = "json/prettifier"
    private val modelName = "model"

    @GetMapping
    fun index(): ModelAndView {
        return ModelAndView(viewName, mapOf(
                modelName to Model("", "", null)
        ))
    }

    @PostMapping("/prettify")
    fun decode(@ModelAttribute("form") form: PrettifyForm): ModelAndView {
        val (pretty, error) = try {
            Pair(prettifyJson(form.ugly), null)
        } catch (e: JsonProcessingException) {
            Pair("", e.message)
        }

        return ModelAndView(viewName, mapOf(
                modelName to Model(form.ugly, pretty, error)
        ))
    }

    @PostMapping("/minify")
    fun decode(@ModelAttribute("form") form: MinifyForm): ModelAndView {
        val (ugly, error) = try {
            Pair(minifyJson(form.pretty), null)
        } catch (e: JsonProcessingException) {
            Pair("", e.message)
        }

        return ModelAndView(viewName, mapOf(
                modelName to Model(ugly, form.pretty, error)
        ))
    }

    private fun minifyJson(json: String): String {
        val mapper = ObjectMapper().disable(SerializationFeature.INDENT_OUTPUT)
        return mapper.writeValueAsString(mapper.readTree(json))
    }

    private fun prettifyJson(json: String): String {
        val mapper = ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT)
        return mapper.writeValueAsString(mapper.readTree(json))
    }

    data class PrettifyForm(val ugly: String)
    data class MinifyForm(val pretty: String)
    data class Model(val ugly: String, val pretty: String, val error: String?)
}