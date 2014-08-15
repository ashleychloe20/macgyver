//#@Schedule {"cron":"*/5 * * * * ?"}
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
/*
def x = ctx.getBean(AuthenticationManager.class)

def token = new UsernamePasswordAuthenticationToken("user".toString(),
					"password");

                    println token

                    println x
                    
x.authenticate(token)
*/