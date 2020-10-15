import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

class Usuario implements Registro{

    protected int idUsuario;
    protected String nome;
    protected String email;
    protected String senha;

    //Construtores
    public Usuario() {
        this(-1,"","","");
    }

    public Usuario(int idUsuario, String nome, String email, String senha){
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    //Getters e Setters
    public void setID(int idUsuario){
        this.idUsuario = idUsuario;
    }

    public int getID(){
        return this.idUsuario;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public String getNome(){
        return this.nome;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getEmail(){
        return this.email;
    }

    public void setSenha(String senha){
        this.senha = senha;
    }

    public String getSenha(){
        return this.senha;
    }

    public String toString() {
        return "\nID: " + this.idUsuario + 
               "\nNome: " + this.nome + 
               "\nE-mail: " + this.email + 
               "\nSenha: " + this.senha;
    }

    //Chave Secundária
    public String chaveSecundaria() {
        return this.email;
    }

    // Escrita em memória
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(idUsuario);
        dos.writeUTF(nome);
        dos.writeUTF(email);
        dos.writeUTF(senha);
        return baos.toByteArray();
    }

    // Leitura
    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        idUsuario = dis.readInt();
        nome = dis.readUTF();
        email = dis.readUTF();
        senha = dis.readUTF();
    }

}