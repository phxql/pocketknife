package de.mkammerer.pocketknife.pocketknife.json

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import de.mkammerer.pocketknife.pocketknife.BaseController
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import java.time.Instant
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

        val decodedPayload = Base64.getDecoder().decode(parts[1])
        val parsedPayload = ObjectMapper().readTree(decodedPayload)

        val exp = if (parsedPayload.has("exp")) formatTimestamp(parsedPayload.get("exp")) else null
        val nbf = if (parsedPayload.has("nbf")) formatTimestamp(parsedPayload.get("nbf")) else null
        val iat = if (parsedPayload.has("iat")) formatTimestamp(parsedPayload.get("iat")) else null

        val payload = prettifyJson(decodedPayload)
        return Token(header, payload, exp, nbf, iat)
    }

    private fun formatTimestamp(node: JsonNode): String {
        val timestamp = node.asLong()
        return Instant.ofEpochSecond(timestamp).toString()
    }

    private fun prettifyJson(input: ByteArray): String {
        try {
            val mapper = ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT)
            return mapper.writeValueAsString(mapper.readTree(input))
        } catch (e: JsonProcessingException) {
            throw IllegalArgumentException(e.message, e)
        }
    }

    data class Token(
            val header: String,
            val payload: String,
            val exp: String?,
            val nbf: String?,
            val iat: String?
    )

    data class DecodeForm(val jwt: String)
    data class Model(val jwt: String, val header: String, val payload: String, val exp: String?, val nbf: String?, val iat: String?, val error: String?) {
        companion object {
            val EMPTY = Model("", "", "", null, null, null, null)

            fun fromToken(jwt: String, token: Token) = Model(jwt, token.header, token.payload, token.exp, token.nbf, token.iat, null)

            fun fromException(jwt: String, exception: Exception) = Model(jwt, "", "", null, null, null, exception.message)
        }
    }
}