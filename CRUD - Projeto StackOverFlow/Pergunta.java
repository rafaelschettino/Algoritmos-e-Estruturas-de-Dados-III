import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

class Pergunta implements Registro {
	int idPergunta;
	int idUsuario;
	long criacao;
	short nota;
	String pergunta;
  	String palavrasChave;
	boolean ativa;


   //Construtores
    public Pergunta() {
        this(-1, 0, 0, (short)0, "", "", true);
    }

	public Pergunta(int idUsuario, int idPergunta, long criacao, short nota, String pergunta, String palavrasChave, boolean ativa){
		this.idUsuario = idUsuario;
		this.idPergunta = idPergunta;
		this.criacao = criacao;
		this.nota = nota;
		this.pergunta = pergunta;
    	this.palavrasChave = palavrasChave;
		this.ativa = ativa;
	}

	public int getID(){
		return idPergunta;
	}

	public void setID(int id){
		idPergunta = id;
	}

	public String getPergunta(){
		return this.pergunta;
	}
	
	public String chaveSecundaria(){
		return null;
	}
	
	public byte[] toByteArray() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		dos.writeInt(idPergunta);
		dos.writeInt(idUsuario);
		dos.writeLong(criacao);
		dos.writeShort(nota);
		dos.writeUTF(pergunta);
    	dos.writeUTF(palavrasChave);
		dos.writeBoolean(ativa);
		return baos.toByteArray();
	}

	public void fromByteArray(byte[] ba) throws IOException {
		ByteArrayInputStream bais = new ByteArrayInputStream(ba);
		DataInputStream dis = new DataInputStream(bais);
		idPergunta = dis.readInt();
		idUsuario = dis.readInt();
		criacao = dis.readLong();
		nota = dis.readShort();
		pergunta = dis.readUTF();
    	palavrasChave = dis.readUTF();
		ativa = dis.readBoolean();
	}


}