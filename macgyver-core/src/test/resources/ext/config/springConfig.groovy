import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import io.macgyver.core.TestBean

beans {

    //  This is used by a test case
    testGroovyBean(TestBean) {
        // Do not change the value...it is used by a test case
        foo="Jerry Garcia"
    }
}