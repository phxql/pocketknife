package de.mkammerer.pocketknife.pocketknife.json

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import de.mkammerer.pocketknife.pocketknife.BaseController
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView

@Controller
@RequestMapping("/json/prettifier")
class JsonPrettifier : BaseController<JsonPrettifier.Model>("json/prettifier") {
    @GetMapping
    fun index(): ModelAndView = view(Model.EMPTY)

    @PostMapping("/prettify")
    fun decode(@ModelAttribute("form") form: PrettifyForm): ModelAndView {
        val model = try {
            Model(form.ugly, prettifyJson(form.ugly), null)
        } catch (e: JsonProcessingException) {
            Model("", "", e.message)
        }

        return view(model)
    }

    @PostMapping("/minify")
    fun decode(@ModelAttribute("form") form: MinifyForm): ModelAndView {
        val model = try {
            Model(minifyJson(form.pretty), form.pretty, null)
        } catch (e: JsonProcessingException) {
            Model("", "", e.message)
        }

        return view(model)
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
    data class Model(val ugly: String, val pretty: String, val error: String?) {
        companion object {
            val EMPTY = Model("", "", null)
        }
    }
}