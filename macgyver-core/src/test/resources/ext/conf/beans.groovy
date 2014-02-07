import io.macgyver.core.TestBean
import org.springframework.beans.factory.annotation.Value





beans {

    myTestBean(TestBean) {
      
        foo = prop.SOME_TEST_PROPERTY
    }
    
    yadda(Yadda) {
        
    }

}


class Yadda {

}