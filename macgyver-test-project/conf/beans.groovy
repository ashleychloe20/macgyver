import com.jolbox.bonecp.BoneCPDataSource


beans {

    testds(BoneCPDataSource) {
    	jdbcUrl="jdbc:h2:mem:testdb"
		driverClass="org.h2.Driver"
		username="sa"
		password=""
    }

}