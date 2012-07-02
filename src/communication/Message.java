package communication;
import java.io.Serializable;


public class Message implements Serializable{
    @Override
    public String toString() {
	return "Message [data=" + data + ", cod=" + cod + ", id=" + id
		+ ", replyTo=" + replyTo + "]";
    }
    public Serializable data;
    public int cod;
    public int id;
    public int replyTo;
    private static Integer id_atual = 0;
    private Message(){
	synchronized (id_atual) {
	    id = id_atual++;
	}
	
    }
    public static Message createMessage(){
	return new Message();
    }
}
