import com.jolbox.bonecp.BoneCPDataSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.beans.factory.annotation.Value



@Configuration
class MyConfig {

    @Value('${dataSource.testds.jdbcUrl}')
    def testDsUrl
    
    @Bean
    def testds() {
       
        
        def cp = new BoneCPDataSource();
    	cp.jdbcUrl=testDsUrl
		cp.driverClass="org.h2.Driver"
		cp.username="sa"
		cp.password=""
       
        return cp
    }

}


return MyConfig.class

