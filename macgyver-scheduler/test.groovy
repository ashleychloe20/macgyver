


schedule {
	description "description"
	
	invoke { }

}



class TestDSL {


	
	def static schedule(closure) {
		def x = new TestDSL()
		closure.delegate = x
		closure()
		
	}
	
	def test(String x) {
		print "Test "+x
	}
	
	def methodMissing(String name, args) {
		println "MISSING!!!"
	}

}






TestDSL.schedule { test "test" 
a "1"
}
