import org.gradle.api.provider.Provider

fun Iterable<String>.toJavaString(): String {
    return "{\"" + joinToString("\", \"") + "\"}"
}

fun Provider<String>.toInt(): Int = get().toInt()
