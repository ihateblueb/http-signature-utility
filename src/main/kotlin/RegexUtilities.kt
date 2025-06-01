class RegexUtilities {
    fun buildHeaderRegex(key: String) = Regex("$key=\"(.*?)\"")
}