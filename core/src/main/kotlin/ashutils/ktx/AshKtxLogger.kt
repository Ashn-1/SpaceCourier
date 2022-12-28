package ashutils.ktx

import ktx.log.Logger
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * Extends the libKTX Logger class to have custom tags.
 *
 * @since 0.1
 * @author Cpt-Ash (Ahmad Haidari)
 */
class AshKtxLogger(val tag: String) : Logger(tag.padEnd(MAX_TAG_CHARACTERS)) {
    override val debugTag: String
        get() = "${timestampFormatter.format(Instant.now())} [DEBUG] [$tag]"

    override val infoTag: String
        get() = "${timestampFormatter.format(Instant.now())} [INFO ] [$tag]"

    override val errorTag: String
        get() = "${timestampFormatter.format(Instant.now())} [ERROR] [$tag]"

    companion object {
        private val timestampFormatter: DateTimeFormatter = DateTimeFormatter
            .ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
            .withZone(ZoneId.systemDefault())

        private const val MAX_TAG_CHARACTERS = 16
    }
}

fun ashLogger(tag: String): Logger = AshKtxLogger(tag)
