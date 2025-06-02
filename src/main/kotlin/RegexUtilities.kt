package site.remlit.blueb.httpSignatures

class RegexUtilities {
    fun buildHeaderRegex(key: String) = Regex("$key=\"(.*?)\"")
}