package site.remlit.blueb.httpSignatures

import kotlinx.datetime.Clock
import java.security.PublicKey
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.io.encoding.Base64
import kotlin.time.Duration.Companion.seconds

/**
 * Representation of a Signature
 * */
data class Signature(
    val value: String
) {
    /**
     * Returns Base64 encoded string
     * */
    override fun toString(): String {
        return value
    }

    /**
     * Returns Base64 decoded string of Signature value
     * */
    fun toDecodedString(): String {
        return Base64.decode(value).toString()
    }

    /**
     * Returns Base64 decoded ByteArray of Signature value
     * */
    fun toByteArray(): ByteArray {
        return Base64.decode(value)
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
            * 150s is 2.5m, total of 5m window.
            * Iceshrimp.NET also does 5m.
            * */

            val nowPlusMargin = Clock.System.now().plus(maxTimeMargin.seconds)
            val nowMinusMargin = Clock.System.now().minus(maxTimeMargin.seconds)

            val dateInstant = date.toInstant(TimeZone.currentSystemDefault())

            if (dateInstant > nowPlusMargin)
                throw SignatureException("Date is more than $maxTimeMargin seconds past now.")

            if (dateInstant < nowMinusMargin)
                throw SignatureException("Date is more than $maxTimeMargin seconds from now.")

            val javaSignature = java.security.Signature.getInstance("SHA256withRSA")
            javaSignature.initVerify(publicKey)
            javaSignature.update(data)

            return javaSignature.verify(signature.toByteArray())
        }

        fun create(): Nothing = TODO()
    }
}