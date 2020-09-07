import java.io.File;

public class Teste{
    //public static CRUD<Livro> arqLivros;

    public static void main(String[] args){

         // Livros de exemplo
        Livro l1 = new Livro(-1, "Eu, Robô", "Isaac Asimov", 14.9F);
        Livro l2 = new Livro(-1, "Eu Sou A Lenda", "Richard Matheson", 21.99F);
        Livro l3 = new Livro(-1, "Número Zero", "Umberto Eco", 34.11F);
        int id1, id2, id3;

        try{
            //Verifica se existe algum arquivo anterior, para poder deleta-lo
            if(new File("livros.db").exists()){
                new File("livros.db").delete();
            }//fim if

            // Abre (cria) o arquivo de livros
            new File("livros.db");
            CRUD<Livro> arqLivros = new CRUD<>("livros.db", Livro.class.getConstructor());

            // Insere os três livros
            id1 = arqLivros.create(l1); 
            l1.setID(id1);
            id2 = arqLivros.create(l2);
            l2.setID(id2);
            id3 = arqLivros.create(l3);
            l3.setID(id3);
            
            //Busca por dois livros utilizando o id como chave.
            System.out.println(arqLivros.read(id1));
            System.out.println(arqLivros.read(id2));

            //Busca por um livro utilizando o nome como chave.
            System.out.println(arqLivros.read(l3.getNome()));

            //Excluir um livro e mostra que não existe mais
            arqLivros.delete(id3);
            Livro l = arqLivros.read(id3);
            if(l == null){
                System.out.println("\nTentativa de leitura do Livro3: ");
                System.out.println("Livro excluido");
            }else{
                System.out.println(l);
            }//fim if

            //Altera um livro para um tamanho maior e exibe o resultado
            //l2.autor = "Richard Burton Matheson";
            //arqLivros.update(l2);
            //System.out.println(arqLivros.read(id2));

            //Altera um livro para um tamanho menor e exibe o resultado
            //l1.autor = "I. Asimov";
            //arqLivros.update(l1);
            //System.out.println(arqLivros.read(id1));
            

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}