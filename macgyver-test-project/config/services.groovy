
/*
testds {
	serviceType="dataSource"
	jdbcUrl="jdbc:h2:mem:testdb"
	driverClass="org.h2.Driver"
	username="sa"
	password=""
}


graphite {
	serviceType="graphite"
    host="mygraphitehost"
    port=2003
    prefix="sandbox"
}*/


macgyverGitConfigRepo {
	serviceType="GitRepository"
	url="https://github.com/if6was9/macgyver-resource-test"
	username=""
	password=""
	//resourceProvider=true
}