import java.io.IOException;

public interface Registro {

    public int getID();
    public void setID(int id);
    public void fromByteArray(byte[] ba) throws IOException;
    public byte[] toByteArray() throws IOException;
    public String chaveSecundaria();
    
}