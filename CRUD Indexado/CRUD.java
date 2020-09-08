import java.io.FileInputStream;
import java.io.DataInputStream;
import java.io.ByteArrayOutputStream;
import java.io.RandomAccessFile;
import java.io.IOException;
import java.lang.reflect.Constructor;

/**
 * Class CRUD
 * @author Rafael Schettino
 * @version setembro/2020
 */
public class CRUD<T extends Registro>{
    private Constructor<T> construtor;
    private String nomeArquivo;
    private RandomAccessFile arquivo;
    private HashExtensivel indiceDireto;
    private ArvoreBMais_String_Int indiceIndireto;

    //Construtor da classe CRUD
    public CRUD(String nomeArquivo, Constructor<T> construtor) throws Exception{
        this.construtor = construtor;
        this.nomeArquivo = nomeArquivo;
        indiceDireto = new HashExtensivel(10, "livros.diretorio.idx", "livros.cestos.idx");
        indiceIndireto = new ArvoreBMais_String_Int(10, "livros.arvore.idx");
                
        try{            
            this.arquivo = new RandomAccessFile(nomeArquivo, "rw");
 
            if(arquivo.length() < 4){
                //arquivo.seek(0);
                arquivo.writeInt(0);    //Primeiro ID
            }//fim if
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }

    /**
     * Metodo que armazena o objeto recebido no arquivo.
     * @param T objeto (de tipo qualquer) a ser armazenado no arquivo.
     * @return int id real do objeto, que será gerado pelo próprio método
     * a partir do ultimo ID usado.
     */
    public int create(T objeto) throws Exception{
        int idReal = 0;
        long endereco = -1; //Armazena o endereço do novo registro

        try{
            //Abro no inicio do arquivo
            arquivo.seek(0);
            idReal = arquivo.readInt(); //Guardo o ultimo ID utilizado
            objeto.setID(idReal);

            //Abro no inicio do arquivo
            arquivo.seek(0);
            arquivo.writeInt(idReal+1); //Atualizo o cabeçalho do arquivo com o novo ID

            endereco = arquivo.length();   //Guardo o endereco do registro que está sendo inserido no arquivo. 
            indiceDireto.create(idReal, endereco);  //Incluir no indice direto.
            indiceIndireto.create(objeto.chaveSecundaria(), idReal); //Incluir no indice indireto.
            arquivo.seek(endereco);     //Abro o arquivo no endereço do novo registro.
            byte[] ByteArray = objeto.toByteArray();
            arquivo.writeChar(' '); //Campo de lapide do registro
            arquivo.writeInt(ByteArray.length); //Numero inteiro que indica o tamanho do vetor de bytes
            arquivo.write(ByteArray);  
                        
        }catch(IOException e){
            e.printStackTrace();
        }

        return idReal;
    }

    /**
     * Metodo de leitura que utiliza como chave de busca o ID.
     * @param int id para a realização da busca no indice direto.
     * @return T objeto localizado e lido.
     */
    public T read(int ID) throws Exception{
        T objeto = this.construtor.newInstance();   //Objeto de retorno.
        long endereco = -1;     //Armazena o endereco do registro procurado.
        int tam_registro = 0;
        char lapide;    //Armazena o campo de lapide do registro.

        endereco = indiceDireto.read(ID);   //Localizo o ID desejado no indice direto.        

        try{
            //Se o endereço retornado for -1, quer dizer que não existe o registro procurado.
            if(endereco != -1){
                //Abro o arquivo no endereço do registro procurado.
                arquivo.seek(endereco);
                lapide = arquivo.readChar(); //Leio os bytes de lapide do registro.

                //Verifico se o registro é válido.
                if(lapide != '*'){
                    tam_registro = arquivo.readInt();  //Leio o indicador de tamanho.                
                    byte[] ByteArray = new byte[tam_registro];
                    arquivo.read(ByteArray);
                    objeto.fromByteArray(ByteArray);    //Preencho o objeto de retorno com os dados do registro.
                }else{
                    objeto = null;  //Retorno objeto vazio, pois o registro não é válido.
                }//fim if
            }else{
                objeto = null;
            }//fim if
        }catch(IOException e){
            e.printStackTrace();
            objeto = null;
        }

        return objeto;
        
    }

    /**
     * Método de leitur que utiliza como chave de busca o nome do objeto.
     * @param String chaveSecundaria para a realizacao da busca no indice indireto.
     * @return T objeto localizado e lido.
     */
    public T read(String chaveSecundaria) throws Exception{
        T objeto = this.construtor.newInstance();   //Objeto de retorno.
        int id = 0;

        id = indiceIndireto.read(chaveSecundaria);   //Consulta ao indice indireto

        objeto = this.read(id);     //Consulta ao indice direto.
        
        return objeto;
    }

    /**
     * Metodo de alteração de um registro.
     * @param T objeto com dados atualizados. 
     * @return boolean se a operação foi bem sucedida ou não.
     */
    public boolean update(T objeto) throws Exception{
        long endereco = -1;     //Endereco do registro a ser alterado.
        int tam_registro = 0;   //Armazeno o tamanho do registro a ser alterado.
        int tam_objeto = 0;     //Tamanho do objeto alterado recebido.
        boolean sucesso = false;
        
        endereco = indiceDireto.read(objeto.getID());
        arquivo.seek(endereco);
        arquivo.readChar();
        tam_registro = arquivo.readInt();      
        //System.out.println(tam_registro);

        tam_objeto = objeto.toByteArray().length;    
        //System.out.println(tam_objeto);

        //Se o objeto alterado é menor ou tem o mesmo tamanho 
        if(tam_registro >= tam_objeto){
            arquivo.seek(endereco);
            arquivo.writeChar(' ');     //Bytes de lápide.
            arquivo.writeInt(tam_objeto);   //Indicador de tamanho do registro.
            arquivo.write(objeto.toByteArray());
        }else{
            arquivo.seek(endereco);
            arquivo.writeChar('*');     //Excluo o registro desatualizado para colocar o atualizado ao final do arquivo.            
            arquivo.seek(arquivo.length());     //Abro ao final do arquivo.
            endereco = arquivo.length();    //Novo endereço do registro.

            //Insiro o novo registro ao final do arquivo.
            arquivo.writeChar(' ');     //Bytes de lápide.
            arquivo.writeInt(tam_objeto);   //Indicador de tamanho do registro.
            arquivo.write(objeto.toByteArray());

            //Realizo as alterações também nos indices.
            indiceDireto.update(objeto.getID(), endereco);
            indiceIndireto.update(objeto.chaveSecundaria(), objeto.getID());

            sucesso = true;
        }//fim if

        return sucesso;
    }

    /**
     * Metodo de exclusão.
     * @param int id do registro a ser excluído.
     * @return boolean se a operação de exclusão foi bem sucedida ou não.
     */    
    public boolean delete(int id) throws Exception{
        T objeto = this.construtor.newInstance();
        boolean sucesso = true;    //Retorna true se a operação de exclusão foi um sucesso.
        long endereco = -1;     //Endereço do objeto a ser removido.
        int tam_registro = 0;

        try{

            endereco = indiceDireto.read(id);   
            arquivo.seek(endereco);     //Abro o arquivo no endereco do registro a ser removido.
            arquivo.writeChar('*');     //Atualizo os bytes de lápide indicando a remoção.
            tam_registro = arquivo.readInt();      //Leio o indicador de tamanho do registro.

            //Cria um objeto a partir do registro que será excluido.
            byte[] ByteArray = new byte[tam_registro];
            arquivo.read(ByteArray);
            objeto.fromByteArray(ByteArray);

            indiceDireto.delete(objeto.getID());    //Remove o registro do indice direto.
            indiceIndireto.delete(objeto.chaveSecundaria());    //Remove o registro do indice indireto.

        }catch(Exception e){
            e.printStackTrace();
            sucesso = false;
        }

        return sucesso;
    }

    
}