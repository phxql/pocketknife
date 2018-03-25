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
    private val viewName = "encoding/base64"

    @GetMapping
    fun index(): ModelAndView {
        return ModelAndView(viewName, mapOf(
                "model" to Model("", "", null)
        ))
    }

    @PostMapping("/decode")
    fun decode(@ModelAttribute("form") form: DecodeForm): ModelAndView {
        val (plaintext, error) = try {
            Pair(decodeBase64(form.base64), null)
        } catch (e: IllegalArgumentException) {
            Pair("", e.message)
        }

        return ModelAndView(viewName, mapOf(
                "model" to Model(form.base64, plaintext, error)
        ))
    }

    @PostMapping("/encode")
    fun decode(@ModelAttribute("form") form: EncodeForm): ModelAndView {
        val base64 = encodeBase64(form.plaintext)

        return ModelAndView(viewName, mapOf(
                "model" to Model(base64, form.plaintext, null)
        ))
    }

    private fun encodeBase64(plaintext: String): String {
        return Base64.getEncoder().encodeToString(plaintext.toByteArray())
    }

    private fun decodeBase64(base64: String): String {
        return String(Base64.getDecoder().decode(base64), charset = Charsets.UTF_8)
    }

    data class DecodeForm(val base64: String)
    data class EncodeForm(val plaintext: String)
    data class Model(val base64: String, val plaintext: String, val error: String?)
}