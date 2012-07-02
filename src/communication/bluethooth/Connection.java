package communication.bluethooth;

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
 
import java.util.LinkedList;
import java.util.List;

import android.bluetooth.BluetoothSocket;

import communication.Message;

public class Connection  extends communication.Connection{
   
    private boolean connected = true;
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
	connected = false;
    }

    protected BluetoothSocket s; 

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
 

    public Connection(BluetoothSocket cli) throws Exception {
	s = cli;

	init();

    }

    public boolean isConnected() {
	return connected;
    }

   
  
    public void init() {
	t = new Thread() {
	    public void run() {
		while (connected) {
		    try {
			System.out.println("waiting");
			Message m = read();
			System.out.println("Received:" + m);
			msgs.add(m);

			synchronized (msgs) {
			    msgs.notifyAll();
			}
		    } catch (EOFException e) {
			try {
			    s.close();
			} catch (IOException e1) {
			    // TODO Auto-generated catch block
			  //  e1.printStackTrace();
			}
			//e.printStackTrace();
		    } catch (Exception e) {

			//e.printStackTrace();
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
//	    System.out.println("Removed MSG:" + ret);

	}

	return ret;
    }

    private Message read() throws Exception {
	return (Message) getIn().readObject();
    }

    public synchronized void write(Message m) throws Exception {
	System.out.println("Writing.. " + m);
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
//	System.out.println("Removed MSG:" + msgs.get(0));
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
	System.out.println("read.. " + cod);
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
