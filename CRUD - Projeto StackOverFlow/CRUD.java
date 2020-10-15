import java.io.RandomAccessFile;
import java.lang.reflect.Constructor;

public class CRUD<T extends Registro> {

    private      RandomAccessFile       arquivo;
    public       HashExtensivel         indice_direto;
    public       ArvoreBMais_String_Int indice_indireto;
    public       ArvoreBMais_Int_Int    indice_indireto_relacao;
    private      Constructor<T>         construtor;
    public final String                 pasta = "Dados";
    public ListaInvertida lista;
    
    // Construtor do CRUD
    public CRUD(Constructor<T> construtor) throws Exception {
        
        this.construtor = construtor;
        String nomeArquivo = construtor.newInstance().getClass().getSimpleName();

        arquivo = new RandomAccessFile(pasta + "/" + nomeArquivo + ".db", "rw");

        // Verifica se o arquivo está vazio
        if(arquivo.length() < 4)
            arquivo.writeInt(0);

        indice_direto = new HashExtensivel(10, this.pasta + "/" + nomeArquivo + ".diretorio.idx", this.pasta + "/" + nomeArquivo + ".cesto.idx");
        indice_indireto = new ArvoreBMais_String_Int(10, this.pasta + "/" + nomeArquivo + ".arvore.idx");
        indice_indireto_relacao = new ArvoreBMais_Int_Int(10, this.pasta + "/" + nomeArquivo + "arvoreRelacao.idx");
        lista = new ListaInvertida(10, "Dados/listainv.db", "Dados/blocos.listainv.db");
    }
    
    /*
     * Função para adicionar dados ao arquivo
     * @param objeto do tipo T para ser incluido no arquivo
     * @return id do novo objeto adicionado ao arquivo
     */
    public int create(T objeto) throws Exception {
        
        // Indo para o início do arquivo para ver o último id utilizado 
        arquivo.seek(0);
        int ID = arquivo.readInt() + 1;
        //Atualiza o metadado com o último ID utilizado
        arquivo.seek(0);
        arquivo.writeInt(ID);

        arquivo.seek(arquivo.length());
        long endereco = arquivo.getFilePointer();
        // Seta o ID do obejeto
        objeto.setID(ID);
        // Lápide
        arquivo.writeByte(' ');
        //Tamanho do Registro
        arquivo.writeInt(objeto.toByteArray().length);
        // Dados do Registro
        arquivo.write(objeto.toByteArray());

        indice_direto.create(ID, endereco);
        if(objeto.chaveSecundaria() != null)
            indice_indireto.create(objeto.chaveSecundaria(), objeto.getID()); 

        return ID;
    }


    /*
     * Função para a leitura de um objeto de acordo com sua chave primária
     * @param int id, identificador do objeto 
     * @return objeto do tipo T encontrado ou não no arquivo
     */
    public T read(int ID) throws Exception {
        
        T objeto = null;
        byte lapide;
        byte[] byteObjeto;
        int tamanho;
        long endereco;
        
        if((endereco = indice_direto.read(ID)) >= 0) {
            // Vai para o endereço do objeto
            arquivo.seek(endereco);
            // Verifica o lápide
            lapide = arquivo.readByte();
            // Tamanho do arquivo
            tamanho = arquivo.readInt();
            // Leitura dos bytes
            byteObjeto = new byte[tamanho];
            arquivo.read(byteObjeto);
            // Cria objeto do tipo T com os bytes lidos
            objeto = this.construtor.newInstance();
            objeto.fromByteArray(byteObjeto);
            arquivo.seek(endereco);
            // Retorna o objeto criado
            return objeto;
        } else {
            // ID inexistente, objeto não existe
            return null;
        }//fim if

    }
    
    /*
     * Função para a leitura de um objeto de acordo com suva chave Secundária
     * @param String que é a chave secundária da pesquisa
     * @return objeto do tipo T encontrado ou não no arquivo
     */
    public T read(String chaveSecundaria) throws Exception {
        
        T objeto = null;
        byte lapide;
        byte[] byteObjeto;
        int tamanho;
        long endereco;
        int codigo;
        
      if((codigo = indice_indireto.read(chaveSecundaria))>= 0) {
          endereco = indice_direto.read(codigo);
          // Vai para o endereço do objeto
          arquivo.seek(endereco);
          // Verifica o lápide
          lapide = arquivo.readByte();
          // Tamanho do arquivo
          tamanho = arquivo.readInt();
          // Leitura dos bytes
          byteObjeto = new byte[tamanho];
          arquivo.read(byteObjeto);
          // Cria objeto do tipo T com os bytes lidos
          objeto = this.construtor.newInstance();
          objeto.fromByteArray(byteObjeto);
          // Retorna o objeto criado
          return objeto;
      }else {
          // ID inexistente, objeto não existe
          return null;
      }//fim if
      
    }
    
    /**
     * Metodo de exclusão.
     * @param int id do registro a ser excluído.
     * @return boolean se a operação de exclusão foi bem sucedida ou não.
     */
    public boolean delete(int id) throws Exception {
        boolean sucesso = true;
        byte[] ByteArray;
        int tam_registro;
        T aux = this.construtor.newInstance();  //Objeto auxiliar.
        long endereco = indice_direto.read(id);   //Endereço do objeto a ser removido.

        try{
            arquivo.seek(endereco);   //Abro o arquivo no endereco do registro a ser removido.
            arquivo.write('*');      //Atualizo os bytes de lápide indicando a remoção.
            tam_registro = arquivo.readInt();   //Leio o indicador de tamanho do registro.

            //Cria um objeto a partir do registro que será excluido.
            ByteArray = new byte[tam_registro];
            arquivo.read(ByteArray);
            aux = this.construtor.newInstance();
            aux.fromByteArray(ByteArray);

            indice_direto.delete(id);   //Remove o registro do indice direto.
            indice_indireto.delete(aux.chaveSecundaria());  //Remove o registro do indice indireto.

        }catch(Exception e){
            e.printStackTrace();
            sucesso = false;
        }
        
        return sucesso;
    }
    

    /**
     * Metodo de alteração de um registro.
     * @param T objeto com dados atualizados. 
     * @return boolean se a operação foi bem sucedida ou não.
     */
    public boolean update(T objeto) throws Exception {

        boolean sucesso = false;
        byte[] ByteArray;
        int tam_registro;   
        T aux = null;   //Objeto auxiliar.
        long endereco = indice_direto.read(objeto.getID());   //Endereco do registro a ser alterado.

        arquivo.seek(endereco);
        arquivo.write('*');   //Excluo o registro desatualizado para colocar o atualizado ao final do arquivo.
        tam_registro = arquivo.readInt();   //Armazeno o tamanho do registro a ser alterado.
        ByteArray = new byte[tam_registro];
        arquivo.read(ByteArray);
        aux = this.construtor.newInstance();
        aux.fromByteArray(ByteArray);

        arquivo.seek(arquivo.length()); 
        endereco = arquivo.getFilePointer();  //Novo endereço do registro.

        //Insiro o novo registro ao final do arquivo.
        arquivo.writeByte(' ');   //Bytes de lápide.
        ByteArray = objeto.toByteArray();
        arquivo.writeInt(ByteArray.length);
        arquivo.write(ByteArray);

        //Realizo as alterações também nos índices.
        indice_direto.update(objeto.getID(), endereco);

        if(objeto.chaveSecundaria() != null && objeto.chaveSecundaria().compareTo(aux.chaveSecundaria()) != 0) {
            indice_indireto.delete(aux.chaveSecundaria());
            indice_indireto.create(objeto.chaveSecundaria(), objeto.getID());
        }//fim if

        return sucesso;
    }
    
    

}
 