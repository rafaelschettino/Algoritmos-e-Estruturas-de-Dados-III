import java.io.FileInputStream;
import java.io.DataInputStream;
import java.io.ByteArrayOutputStream;
import java.io.RandomAccessFile;
import java.io.IOException;
import java.lang.reflect.Constructor;

/**
 * Class CRUD
 * @author Rafael Schettino
 * @version agosto/2020
 */
public class CRUD<T extends Registro>{

    private Constructor<T> construtor;
    private String nomeArquivo;
    private RandomAccessFile arquivo;

    //Construtor da classe CRUD
    public CRUD(String nomeArquivo, Constructor<T> construtor) throws Exception{
        this.construtor = construtor;
        this.nomeArquivo = nomeArquivo;
                
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
    public int create(T objeto){
        int idReal = 0;

        try{
            //Abro no inicio do arquivo
            arquivo.seek(0);
            idReal = arquivo.readInt(); //Guardo o ultimo ID utilizado
            objeto.setID(idReal);

            //Abro no inicio do arquivo
            arquivo.seek(0);
            arquivo.writeInt(idReal+1); //Atualizo o cabeçalho do arquivo com o novo ID

            //Abro no fim do arquivo
            arquivo.seek(arquivo.length());
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
     * Metodo de leitura.
     * @param int id para a realização da busca no arquivo.
     * @return T objeto localizado e lido.
     */
    public T read(int id) throws Exception{        
        T objeto = null;    //Objeto de retorno        
        boolean encontrado = false;
        boolean indisponivel = false;
        int tam_registro = 0;        

        try{
            //Abre o arquivo
            arquivo.seek(4);

            //Enquanto o id que esta sendo procurado nao for encontrado e enquanto nao atingirmos o fim do arquivo.
            while(encontrado == false && arquivo.getFilePointer() < arquivo.length() ) {

                //Verifica pelos bytes de lapide se o registro esta valido.
                if(arquivo.readChar() != '*'){
                    indisponivel = false;
                } else {
                    indisponivel = true;
                }//fim if

                tam_registro = arquivo.readInt(); //Numero inteiro que indica o tamanho do vetor de bytes  
                
                if(indisponivel == true) {
                    arquivo.seek(arquivo.getFilePointer() + tam_registro);  //Se o registro estiver indisponivel, pulo para o proximo
                } else {
                    byte[] ByteArray = new byte[tam_registro];
                    arquivo.read(ByteArray);

                    //Crio o objeto de retorno com os dados do registro lido.
                    objeto = this.construtor.newInstance();
                    objeto.fromByteArray(ByteArray);    

                    //Verifica se o id do registro correponde ao id que esta sendo procurado.
                    if(objeto.getID() == id){
                        encontrado = true;
                    }//fim if
                }//fim if

            }//fim while
        } catch(Exception e){
            e.printStackTrace();
        }

        /*
        *Caso o objeto que estamos procurando nao tenha sido encontrado,
        *retornamos um objeto vazio.
        */
        if(encontrado == false){
            objeto = null;
        }//fim if

        return objeto;
    }

    /**
     * Metodo de alteração de um registro.
     * @param T objeto 
     * @return boolean se a operação foi bem sucedida ou não.
     */
    public boolean update(T objeto) throws Exception{
        boolean sucesso = false;
        T registro = null;  //Registro auxiliar para armazenas os registros.
        boolean encontrado = false;
        boolean indisponivel = false; //Se os bytes de lapide indicam um registro invalido.
        int tam_registro = -1;
        long tamanho_arq = arquivo.length();
        long endereco_lapide = -1;

        try{
            arquivo.seek(4);    //Abrindo o arquivo.

            //Enquanto o id que esta sendo procurado nao for encontrado e enquanto nao atingirmos o fim do arquivo.
            while(encontrado == false && arquivo.getFilePointer() < tamanho_arq){

                //Guardo o endereco da lapide
                endereco_lapide = arquivo.getFilePointer();
                                
                //Verifica pelos bytes de lapide se o registro esta valido. 
                if(arquivo.readChar() == ' '){
                    indisponivel = false;
                }else if(arquivo.readChar() == '*'){
                    indisponivel = true; //Byte de lapide indica que o registro esta indisponivel.
                }//fim if

                tam_registro = arquivo.readInt();   //Le o indicador de tamanho do registro
                
                byte[] ByteArray = new byte[tam_registro];  //Array de bytes para o registro
                arquivo.read(ByteArray);

                //Criacao do objeto com os dados do registro.
                registro = this.construtor.newInstance();              
                registro.fromByteArray(ByteArray);               

                //Verifica se o ID do objeto corresponde ao ID procurado.
                if(objeto.getID() == registro.getID()) {
                    //Primeiro passo: excluo o registro a ser alterado.
                    encontrado = true;
                    arquivo.seek(endereco_lapide);  //Abro o arquivo no endereco da lapide.
                    arquivo.writeChar('*');     //Atualizo os bytes de lapide indicando a remocao do registro.
                    sucesso = true;

                    //Segundo passo: Crio um novo registro ao final do arquivo, com as alteracoes desejadas
                    arquivo.seek(arquivo.length());
                    byte[] array_objeto = objeto.toByteArray();
                    arquivo.writeChar(' '); //Lapide do registro.
                    arquivo.writeInt(array_objeto.length); //Numero inteiro que indica o tamanho do vetor de bytes
                    arquivo.write(array_objeto); 
                }else{
                    registro= null;
                }//fim if
                
            }//fim while
        }catch(Exception e){
            e.printStackTrace();
            registro = null;
        }        

        return sucesso;
    }

    /**
     * Metodo de exclusão.
     * @param int id do registro a ser excluído.
     * @return boolean se a operação de exclusão foi bem sucedida ou não.
     */    
    public boolean delete(int id) throws Exception{
        T objeto = null;   
        boolean encontrado = false;
        boolean indisponivel = false; //Se os bytes de lapide indicam um registro invalido.
        int tam_registro = -1;
        long tamanho_arq = arquivo.length();
        long endereco_lapide = -1;
        boolean sucesso = false; //Retorna true se a operacao de exclusao foi um sucesso.

        try{
            
            arquivo.seek(4);    //Abrindo o arquivo.

            //Enquanto o id que esta sendo procurado nao for encontrado e enquanto nao atingirmos o fim do arquivo.
            while(encontrado == false && arquivo.getFilePointer() < tamanho_arq){

                //Guardo o endereco da lapide
                endereco_lapide = arquivo.getFilePointer();
                                
                //Verifica pelos bytes de lapide se o registro esta valido. 
                if(arquivo.readChar() == ' '){
                    indisponivel = false;
                }else if(arquivo.readChar() == '*'){
                    indisponivel = true; //Byte de lapide indica que o registro esta indisponivel.
                }//fim if

                tam_registro = arquivo.readInt();   //Le o indicador de tamanho do registro
                
                byte[] ByteArray = new byte[tam_registro];  //Array de bytes para o registro
                arquivo.read(ByteArray);

                //Criacao do objeto com os dados do registro.
                objeto = this.construtor.newInstance();
                objeto.fromByteArray(ByteArray);
                

                //Verifica se o ID do objeto corresponde ao ID procurado.
                if(objeto.getID() == id) {
                    encontrado = true;
                    arquivo.seek(endereco_lapide);  //Abro o arquivo no endereco da lapide.
                    arquivo.writeChar('*');     //Atualizo os bytes de lapide indicando a remocao do registro.
                    sucesso = true;
                }else{
                    objeto = null;
                }//fim if
                
            }//fim while
        }catch(IOException e){
            e.printStackTrace();
            objeto = null;
        }

        return sucesso;
    }
    
}