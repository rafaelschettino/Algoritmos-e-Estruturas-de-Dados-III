import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class Livro implements Registro{
    protected int id;
    protected String nome;
    protected String autor;
    protected float preco;

    public Livro(){
        setID(-1);
        setNome("");
        setAutor("");
        setPreco(0F);
    }

    public Livro(int id, String nome, String autor, float preco){
        setID(id);
        setNome(nome);
        setAutor(autor);
        setPreco(preco);
    }

    public void setID(int id){
        this.id = id;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public void setAutor(String autor){
        this.autor = autor;
    }

    public void setPreco(float preco){
        this.preco = preco;
    }

    public int getID(){
        return this.id;
    }

    public String getNome(){
        return this.nome;
    }

    public String getAutor(){
        return this.autor;
    }

    public float getPreco(){
        return this.preco;
    }

    public String chaveSecundaria() {
        return this.nome;
    }

    public byte[] toByteArray() throws IOException{
        ByteArrayOutputStream dados = new ByteArrayOutputStream();
        DataOutputStream saida = new DataOutputStream(dados);

        saida.writeInt(id);
        saida.writeUTF(nome);
        saida.writeUTF(autor);
        saida.writeFloat(preco);

        return dados.toByteArray();
    }
    public void fromByteArray(byte[] ba) throws IOException{
        ByteArrayInputStream dados = new ByteArrayInputStream(ba);
        DataInputStream entrada = new DataInputStream(dados);

        id = entrada.readInt();
        nome = entrada.readUTF();
        autor = entrada.readUTF();
        preco = entrada.readFloat();
    }

    public String toString() {
        return "\nID: " + id + "\nTítulo: " + nome + "\nAutor: " + autor + "\nPreço: R$ " + preco;
    }
}