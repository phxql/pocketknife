package de.mkammerer.pocketknife.pocketknife.encoding

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import java.util.Base64

@Controller
@RequestMapping("/encoding/base64")
class Base64 {
    @GetMapping
    fun index(): ModelAndView {
        return ModelAndView("encoding/base64", mapOf(
                "model" to Model("", "", null)
        ))
    }

    @PostMapping("/decode")
    fun decode(@ModelAttribute("form") form: DecodeForm): ModelAndView {
        val (plaintext, error) = try {
            Pair(String(Base64.getDecoder().decode(form.base64), charset = Charsets.UTF_8), null)
        } catch (e: IllegalArgumentException) {
            Pair("", e.message)
        }

        return ModelAndView("encoding/base64", mapOf(
                "model" to Model(form.base64, plaintext, error)
        ))
    }

    @PostMapping("/encode")
    fun decode(@ModelAttribute("form") form: EncodeForm): ModelAndView {
        val base64 = Base64.getEncoder().encodeToString(form.plaintext.toByteArray())

        return ModelAndView("encoding/base64", mapOf(
                "model" to Model(base64, form.plaintext, null)
        ))
    }

    data class DecodeForm(val base64: String)
    data class EncodeForm(val plaintext: String)
    data class Model(val base64: String, val plaintext: String, val error: String?)
}