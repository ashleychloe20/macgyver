package io.macgyver.ssh;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import com.google.common.io.CharStreams;

import net.schmizz.sshj.connection.channel.direct.Session.Command;

public class StringResponseCallback extends ResponseCallback<String> {

	@Override
	public String handle(Command cmd) throws IOException {
		 InputStream inputStream = cmd.getInputStream();
         
         String string = CharStreams.toString( new InputStreamReader( inputStream, "UTF-8" ) );
         
         cmd.join(5, TimeUnit.SECONDS);
     
         if (cmd.getExitStatus()!=0) {
        	 String x = CharStreams.toString( new InputStreamReader( cmd.getErrorStream(), "UTF-8" ) );
        	 throw new SshException(cmd.getExitStatus(), x);
         }
         return string;

	}

}
