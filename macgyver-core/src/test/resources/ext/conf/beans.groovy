import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class MyConfig {
    
    @Bean
    def testObject() {
        println "creating!!!!"
        return "test"
    
    }

}


return MyConfig.class