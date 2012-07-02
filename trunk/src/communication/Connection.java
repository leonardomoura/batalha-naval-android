package communication;

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class Connection {
    

    public void close() throws Exception {
	if (in != null) {
	    in.close();
	}
	if (out != null) {
	    out.close();
	}
	if (s != null) {
	    s.close();
	}
    }

    protected Socket s;
    protected ObjectInputStream in;
    protected ObjectOutputStream out;

    public ObjectInputStream getIn() throws IOException {
	if (in == null)
	    in = new ObjectInputStream(new BufferedInputStream(
		    s.getInputStream()));
	return in;
    }

    public ObjectOutputStream getOut() throws IOException {
	if (out == null) {
	    out = new ObjectOutputStream(s.getOutputStream());
	}
	return out;
    }

    protected List<Message> msgs = new LinkedList<Message>();
    protected Thread t;

    public Connection(Socket cli) throws Exception {
	s = cli;

	init();

    }
    public Connection() throws Exception {

      }

    public boolean isConnected() {
	return s.isConnected() && !s.isClosed();
    }

    public Connection(String ip, int port) throws Exception {
	s = new Socket(ip, port);
	init();

    }
  
    public void init() {
	t = new Thread() {
	    public void run() {
		while (s.isConnected() && !s.isClosed()) {
		    try {
			Message m = read();
			msgs.add(m);

			synchronized (msgs) {
			    msgs.notifyAll();
			}
		    } catch (EOFException e) {
			try {
			    s.close();
			} catch (IOException e1) {
			}
		    } catch (Exception e) {

		    }
		}

	    };
	};
	t.start();
    }

    private Message getMessage(int cod) {
	Message ret = null;
	synchronized (msgs) {
	    for (Message m : msgs) {
		if (m.cod == cod) {
		    ret = m;
		}
	    }
	    if (ret != null) {
		msgs.remove(ret);
	    }

	}

	return ret;
    }

    private Message read() throws Exception {
	return (Message) getIn().readObject();
    }

    public synchronized void write(Message m) throws Exception {
	getOut().writeObject(m);
    }

    public Message getFirstMessage() throws Exception {
	while (msgs.size() == 0) {
	    if(!isConnected()){
		throw new Exception("Closed");
	    }
	    synchronized (msgs) {
		msgs.wait(60000);
	    }
	}
	return msgs.remove(0);
    }

    public Message read(int cod) throws Exception {
	return read(cod, 0);
    }

    public void cancelOneWait(int cod) {
	cancelCod = cod;
	synchronized (msgs) {
	    msgs.notifyAll();
	}
    }

    public int cancelCod = -1;

    public Message read(int cod, long timeout) throws Exception {
	Message m = null;
	long t = System.currentTimeMillis();
	m = getMessage(cod);
	while (m == null) {
	    if (cancelCod == cod) {
		cancelCod = -1;
		throw new Exception("Canceled");
	    }
	    if (timeout > 0 && System.currentTimeMillis() - t > timeout) {
		throw new Exception("Timeout " + timeout);
	    }

	    synchronized (msgs) {
		if (timeout == 0)
		    msgs.wait();
		else
		    msgs.wait(timeout / 10);
	    }
	    m = getMessage(cod);
	}
	return m;
    }

    public Message writeAndWait(Message m, int codWait, long timeout)
	    throws Exception {
	write(m); 
	return read(codWait, timeout);
    }
}
