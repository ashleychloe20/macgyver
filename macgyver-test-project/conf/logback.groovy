
import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.ConsoleAppender

import static ch.qos.logback.classic.Level.DEBUG
import static ch.qos.logback.classic.Level.INFO
import static ch.qos.logback.classic.Level.NONE

scan("30 seconds")
appender("CONSOLE", ConsoleAppender) {
  encoder(PatternLayoutEncoder) {
    pattern = "%d [%thread] [%level] %logger{36} - %msg%n"
  }
}
appender("FILE", FileAppender) {
  file = "./logs/server.log"
  append = true
  encoder(PatternLayoutEncoder) {
    pattern = "%d [%thread] [%level] %logger{36} - %msg%n"
  }
}

logger("io.macgyver", DEBUG)

root(INFO, ["CONSOLE","FILE"])