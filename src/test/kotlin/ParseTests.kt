import kotlin.test.Test
import kotlin.test.expect

class ParseTests {
    val sampleKeyId = "https://activitypub.academy/users/bedacia_tukaban#main-key"
    val sampleAlgorithm = "rsa-sha256"
    val sampleHeaders = "(request-target) host date digest content-type"
    val sampleSignature = "BAuQOg3NI6bf2cnx1PJaT+wvdh2qZcS5ZWi/Lbf15JJeBOzoypC0ZdNiE2BXKPPeuK2mbZF7ofscpbRqS7A9mdag/qvNl8z/CkSCyEbFOCm6wd1hlKsh8hXQQBCM2pb3yKhonEviqBbWt2S+bClS9JY5SDgexpdWGOFM6CUy0G7rvvt16mXAvpwh94NycnnssENxqGJCAIDsvo/b9ETjr6U7Vc9sCvoy7vMjmH9j3IdsAq55Xp3UbxggHHLHhtmHRH9VmI02FL9bc3QOBWHGlDNs0nx31IzTAJqGOLcnjkvjGTRx+k3jd4YDrwF0pmX5JM7w5vYJdCLsRoyuqHrXcA=="
    val sampleHttpSignature = "keyId=\"$sampleKeyId\",algorithm=\"$sampleAlgorithm\",headers=\"$sampleHeaders\",signature=\"$sampleSignature\""

    var signature: HttpSignature = HttpSignature.parseHeaderString(sampleHttpSignature)

    @Test
    fun keyIdMatches() {
        expect(signature.keyId) { sampleKeyId }
    }

    @Test
    fun algorithmMatches() {
        expect(signature.algorithm) { sampleAlgorithm }
    }

    @Test
    fun headersMatches() {
        expect(signature.headers) { sampleHeaders.split(" ") }
    }

    @Test
    fun signatureMatches() {
        expect(signature.signature) { sampleSignature }
    }

    @Test
    fun builtSignatureMatchesReconstruction() {
        val reconstruction = "keyId=\"${signature.keyId}\",algorithm=\"${signature.algorithm}\",headers=\"${signature.headers.joinToString(" ")}\",signature=\"${signature.signature}\""
        expect(reconstruction) { sampleHttpSignature }
    }
}