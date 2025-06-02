package site.remlit.blueb.httpSignatures

import kotlinx.datetime.Clock
import java.security.PublicKey
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.time.Duration.Companion.seconds

/**
 * Representation of a Signature on a HttpSignature
 * */
data class Signature(
    val value: String
) {
    override fun toString(): String {
        return value
    }

    fun toByteArray(): ByteArray {
        return value.toByteArray()
    }

    /**
     * Verify if this signature is valid.
     *
     * @param publicKey Public key of the signer.
     * @param date Date and time the signature was created at.
     * @param data Byte array of signed data.
     * @param maxTimeMargin Number of seconds a signature's date can be off by (either direction). Changing this is not recommended.
     * */
    fun verify(
        publicKey: PublicKey,
        date: LocalDateTime,
        data: ByteArray,
        maxTimeMargin: Long = 150,
    ): Boolean = verify(
        signature = this,
        publicKey = publicKey,
        date = date,
        data = data,
        maxTimeMargin = maxTimeMargin
    )

    companion object {
        /**
         * Verify if a signature is valid.
         *
         * @param signature Signature either from a String or from a [HttpSignature].
         * @param publicKey Public key of the signer.
         * @param date Local date and time the signature was created at.
         * @param data Byte array of signed data.
         * @param maxTimeMargin Number of seconds a signature's date can be off by (either direction). Changing this is not recommended.
         * */
        fun verify(
            signature: Signature,
            publicKey: PublicKey,
            date: LocalDateTime,
            data: ByteArray,
            maxTimeMargin: Long = 150,
        ): Boolean {
            /*
            * 150ms is 2.5m, total of 5m window.
            * */

            val nowPlusMargin = Clock.System.now().plus(maxTimeMargin.seconds)
            val nowMinusMargin = Clock.System.now().minus(maxTimeMargin.seconds)

            val dateInstant = date.toInstant(TimeZone.currentSystemDefault())

            if (dateInstant > nowPlusMargin)
                throw IllegalSignatureException("Date is more than $maxTimeMargin seconds past now.")

            if (dateInstant < nowMinusMargin)
                throw IllegalSignatureException("Date is more than $maxTimeMargin seconds from now.")

            val javaSignature = java.security.Signature.getInstance("RSA")
                .apply {
                    initVerify(publicKey)
                    update(data)
                }

            return javaSignature.verify(signature.toByteArray())
        }

        fun create(): Nothing = TODO()
    }
}