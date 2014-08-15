package io.macgyver.ssh;

import java.io.IOException;
import java.io.InputStream;

import net.schmizz.sshj.connection.channel.direct.Session.Command;

public abstract class ResponseCallback<T> {

	public abstract T handle(Command cmd) throws IOException;
}
