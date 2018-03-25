package de.mkammerer.pocketknife.pocketknife.encoding

import de.mkammerer.pocketknife.pocketknife.BaseController
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import java.util.*

@Controller
@RequestMapping("/encoding/base64")
class Base64Decoder : BaseController<Base64Decoder.Model>("encoding/base64") {
    @GetMapping
    fun index(): ModelAndView = view(Model.EMPTY)

    @PostMapping("/decode")
    fun decode(@ModelAttribute("form") form: DecodeForm): ModelAndView {
        val model = try {
            Model(form.base64, decodeBase64(form.base64), null)
        } catch (e: IllegalArgumentException) {
            Model(form.base64, "", e.message)
        }

        return view(model)
    }

    @PostMapping("/encode")
    fun decode(@ModelAttribute("form") form: EncodeForm): ModelAndView {
        val base64 = encodeBase64(form.plaintext)

        return view(Model(base64, form.plaintext, null))
    }

    private fun encodeBase64(plaintext: String): String {
        return Base64.getEncoder().encodeToString(plaintext.toByteArray())
    }

    private fun decodeBase64(base64: String): String {
        return String(Base64.getDecoder().decode(base64), charset = Charsets.UTF_8)
    }

    data class DecodeForm(val base64: String)
    data class EncodeForm(val plaintext: String)
    data class Model(val base64: String, val plaintext: String, val error: String?) {
        companion object {
            val EMPTY = Model("", "", null)
        }
    }
}