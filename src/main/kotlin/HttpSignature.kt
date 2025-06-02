package site.remlit.blueb.httpSignatures

/**
 * Representation of Signature HTTP header
 * */
data class HttpSignature(
    val keyId: String,
    val algorithm: String,
    val headers: List<String>,
    val signature: Signature
) {
    companion object {
        private val regexUtilities = RegexUtilities()

        /**
         * Parses Signature header to HttpSignature
         *
         * @param header String of Signature header from HTTP request
         * @return [HttpSignature]
         * */
        fun parseHeaderString(header: String): HttpSignature {
            val keyIdRegex = regexUtilities.buildHeaderRegex("keyId")
            val algorithmRegex = regexUtilities.buildHeaderRegex("algorithm")
            val headersRegex = regexUtilities.buildHeaderRegex("headers")
            val signatureRegex = regexUtilities.buildHeaderRegex("signature")

            val keyId = keyIdRegex.find(header)?.groups?.get(1)?.value
            val algorithm = algorithmRegex.find(header)?.groups?.get(1)?.value
            val headers = headersRegex.find(header)?.groups?.get(1)?.value?.split(" ")
            val signature = signatureRegex.find(header)?.groups?.get(1)?.value

            require(keyId != null) { "keyId was null" }
            require(algorithm != null) { "algorithm was null" }
            require(headers != null) { "headers was null" }
            require(signature != null) { "signature was null" }

            return HttpSignature(
                keyId = keyId,
                algorithm = algorithm,
                headers = headers,
                signature = Signature(signature)
            )
        }

        fun create(): Nothing = TODO()
    }
}