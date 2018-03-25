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
import java.util.*

@Controller
@RequestMapping("/jwt/decode")
class JwtDecode : BaseController<JwtDecode.Model>("jwt/decode") {

    @GetMapping
    fun index(): ModelAndView = view(Model.EMPTY)

    @PostMapping("/decode")
    fun decode(@ModelAttribute("form") form: DecodeForm): ModelAndView {
        val model = try {
            Model.fromToken(form.jwt, decodeToken(form.jwt))
        } catch (e: IllegalArgumentException) {
            Model.fromException(form.jwt, e)
        }
        return view(model)
    }

    private fun decodeToken(jwt: String): Token {
        val parts = jwt.split('.')
        if (parts.size < 2) throw IllegalArgumentException("Expected at least 2 parts, found only ${parts.size}")

        val header = prettifyJson(Base64.getDecoder().decode(parts[0]))
        val payload = prettifyJson(Base64.getDecoder().decode(parts[1]))

        return Token(header, payload)
    }

    private fun prettifyJson(input: ByteArray): String {
        try {
            val mapper = ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT)
            return mapper.writeValueAsString(mapper.readTree(input))
        } catch (e: JsonProcessingException) {
            throw IllegalArgumentException(e.message, e)
        }
    }

    data class Token(val header: String, val payload: String)

    data class DecodeForm(val jwt: String)
    data class Model(val jwt: String, val header: String, val payload: String, val error: String?) {
        companion object {
            val EMPTY = Model("", "", "", null)

            fun fromToken(jwt: String, token: Token) = Model(jwt, token.header, token.payload, null)

            fun fromException(jwt: String, exception: Exception) = Model(jwt, "", "", exception.message)
        }
    }
}