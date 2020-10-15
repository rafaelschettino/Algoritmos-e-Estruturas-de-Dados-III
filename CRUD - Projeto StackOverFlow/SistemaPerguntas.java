import java.text.Normalizer;
import java.util.*;

public class SistemaPerguntas {

   public static final Scanner sc = new Scanner(System.in);
   public static int menuEscolha;
   public static int idUsuarioLogado;
   public static CRUD<Pergunta> arqPergunta;
   public static ArrayList<Integer> itensAtivos;

   public static void MenuCriacaoPerguntas(int idUsuario) {

      try {
         arqPergunta = new CRUD<>(Pergunta.class.getConstructor());
         idUsuarioLogado = idUsuario;

         do {
            UI.printMenuCriacaoPerguntas();
            menuEscolha = Integer.parseInt(String.valueOf(sc.nextLine()));
            switch (menuEscolha) {

               case 1:
                  listaPerguntas(idUsuarioLogado);
                  UI.qualquerTeclaParaContinuar();
                  break;

               case 2:
                  incluiPergunta();
                  UI.qualquerTeclaParaContinuar();
                  break;

               case 3:
                  alterarPerguntas();
                  UI.qualquerTeclaParaContinuar();
                  break;

               case 4:
                  arquivarPerguntas();
                  UI.qualquerTeclaParaContinuar();
                  break;

               default:
                  break;

            }
         } while (menuEscolha != 0);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public static void MenuConsultaPerguntas(int idUsuario) throws Exception {

      UI.printMenuConsulta();
      idUsuarioLogado = idUsuario;
      String palavras = sc.nextLine();
      String[] chaves = palavras.split(";");

      for (int i = 0; i < chaves.length; i++) {
         chaves[i] = limpaString(chaves[i]);
      }

      Pergunta[] resp = interseçãoIDs(chaves, idUsuario);

      for (int i = 0; i < resp.length; i++) {
         UI.printPergunta(resp[i], 0);
      }

   }

   public static Pergunta[] interseçãoIDs(String[] chaves, int idUsuario) throws Exception {

      ArrayList<String> array = new ArrayList<>();
      ArrayList<Pergunta> arrayRespostas = new ArrayList<>();
      Pergunta[] resposta = null;
      int[] idArray = null;

      for (int i = 0; i < chaves.length; i++) {

         try {
            idArray = arqPergunta.lista.read(chaves[i]);

            for(int j = 0; j < idArray.length; j++) {
               if(i == 0) {
                  array.add(arqPergunta.read(idArray[j]).getPergunta());
               } else {
                     Pergunta tmp = arqPergunta.read(idArray[j]);
                     if (array.indexOf(tmp.getPergunta()) != -1) {
                        //System.out.println("Não pertence index " +tmp);
                        arrayRespostas.add(tmp);
                     }
               }
            }
         } catch (Exception e) {
            e.getMessage();
         }
      }

      if(chaves.length == 1) {
        //arrayRespostas.add(arqPergunta.read(idArray[0]));
        idArray = arqPergunta.lista.read(chaves[0]);

        for(int j = 0; j < idArray.length; j++) {
            arrayRespostas.add(arqPergunta.read(idArray[j]));
        }
      }  

      resposta = new Pergunta[arrayRespostas.size()];

      for(int i = 0; i < arrayRespostas.size(); i++) {
         resposta[i] = arrayRespostas.get(i);
      }

      return resposta;

   }

   public static String limpaString(String str) {

	   str = str.toLowerCase();
	   str = Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");

	   return str;
	}

	public static void inserirPalavrasChaves(int idPergunta, String palavrasChave) throws Exception {

		String [] arrayPalavrasChave;

		arrayPalavrasChave = palavrasChave.split(";");

		// tirar acentos e maiúsculas e insere na lista
		for(int i = 0; i < arrayPalavrasChave.length; i++) {
			arqPergunta.lista.create(limpaString(arrayPalavrasChave[i]), idPergunta);
		}

	}
	
   public static int incluiPergunta() throws Exception{

      String pergunta;
      String palavrasChave;
      int idPergunta = -1;
   
      UI.printTelaDePergunta();
      pergunta = sc.nextLine();
   
      UI.printTelaPalavraChave();
      palavrasChave = sc.nextLine();
   
      if(ComandosDeTeclado.Vazio(pergunta))
         idPergunta = perguntaInvalida();
      else {
		 idPergunta = perguntaValida(pergunta, palavrasChave,idPergunta);
		 inserirPalavrasChaves(idPergunta, palavrasChave);
	  }
		 
		 return idPergunta;
   }

   public static void listaPerguntas(int idUsuarioLogado) throws Exception {
   	
      int[] arrayIDperguntas = arqPergunta.indice_indireto_relacao.read(idUsuarioLogado);
   
      for(int id : arrayIDperguntas){
         Pergunta pergunta = arqPergunta.read(id);
      	
         if(pergunta.ativa)
            UI.printPergunta(pergunta, 0);
      }	
   }

   public static int perguntaInvalida(){

      System.out.println("Não foi possível incluir esta pergunta!");
      System.out.println("Por favor tente novamente");
      return -1;
   }

   public static int perguntaValida(String pergunta,String palavrachave, int idPergunta) throws Exception{

      String resposta;
      System.out.print("Confirma a inclusão desta pergunta? (S/N): ");

      do{
         resposta = sc.nextLine();
         if(ComandosDeTeclado.Confirma(resposta)){
            Pergunta objetoPergunta = new Pergunta(-1, idUsuarioLogado, new Date().getTime(), (short)0, pergunta,palavrachave, true); 
            idPergunta = arqPergunta.create(objetoPergunta); 
            arqPergunta.indice_indireto_relacao.create(idUsuarioLogado, idPergunta);
            System.out.println("Pergunta criada com sucesso!");
         
            return idPergunta;
         } else if(ComandosDeTeclado.Recusa(resposta)) {
            System.out.println("Cancelando inclusão da pergunta...");
            return -1;
         } else {
            System.out.println("Tente novamente!");
            System.out.print("Confirma a inclusão desta pergunta? (S/N): ");
         }
      } while(true);
   }

   public static void alterarPerguntas() throws Exception {
      itensAtivos = new ArrayList<Integer>();
   	
      int[] arrayIDperguntas = arqPergunta.indice_indireto_relacao.read(idUsuarioLogado);
      int i = 0;
   
      for(int id : arrayIDperguntas){
         Pergunta pergunta = arqPergunta.read(id);
      
         if(pergunta.ativa){
            UI.printPergunta(pergunta, i+1);
            itensAtivos.add(id);
            i++;
         }
      }
   
      alteraPergunta(arrayIDperguntas);
   }

   public static void arquivarPerguntas() throws Exception {
      itensAtivos = new ArrayList<Integer>();
   
      int[] arrayIDperguntas = arqPergunta.indice_indireto_relacao.read(idUsuarioLogado);
      int i = 0;
   
      for(int id : arrayIDperguntas){
         Pergunta pergunta = arqPergunta.read(id);
      	
         if(pergunta.ativa){
            UI.printPergunta(pergunta, i+1);
            itensAtivos.add(id);
            i++;
         }
      }
   
      arquivarPergunta(arrayIDperguntas);
   }

   public static String pegaNovaPergunta(){

      System.out.println("Digite a nova pergunta");
      String novaPergunta = sc.nextLine();
      return novaPergunta;
   }

   public static String pegaConfirmacao(String message){

      System.out.print(message);
      return sc.nextLine();
   }

   public static void alteraPergunta(int[] arrayIDperguntas) throws Exception {

      System.out.println("Digite a posição do item para alterar:");
      System.out.println("Digite 0 para retornar!");
   	
      var resposta = Integer.parseInt(sc.nextLine());
      if(resposta == 0)
         return;
   
      Pergunta pergunta = arqPergunta.read(itensAtivos.get(resposta - 1));
      UI.printPergunta(pergunta, 0);
   
      String novaPergunta = pegaNovaPergunta();
      System.out.println("Digite as novas palavras chave separadas por ponto e virgula");
      String novaPalavra = sc.nextLine();
     // String testando = alteraPalavrasChave(pergunta,novaPalavra);
   
   
      if(ComandosDeTeclado.Vazio(novaPergunta))
         return;
   
      String confirmacao = pegaConfirmacao("Confirma a alteração da pergunta? (S/N): ");
      if(ComandosDeTeclado.Recusa(confirmacao))
         return;
   
      pergunta.pergunta = novaPergunta;
    //  pergunta.palavrasChave = testando;
      arqPergunta.update(pergunta);
      System.out.print("Pergunta alterada!");
   }

   public static void arquivarPergunta(int[] arrayIDperguntas) throws Exception {
      System.out.println("Digite a posição do item para arquivar:");
      System.out.println("Digite 0 para retornar!");
   	
      var resposta = Integer.parseInt(sc.nextLine());
      if(resposta == 0)
         return;
   
      Pergunta pergunta = arqPergunta.read(itensAtivos.get(resposta - 1));
   
      UI.printPergunta(pergunta, 0);
   
      String confirmacao = pegaConfirmacao("Confirma a o arquivamento da pergunta? (S/N): ");
      if(ComandosDeTeclado.Recusa(confirmacao))
         return;
   
      String deleta= pergunta.palavrasChave;
      pergunta.ativa = false;
      arqPergunta.lista.delete(deleta,pergunta.getID());

      arqPergunta.update(pergunta);
      System.out.print("Pergunta arquivada!");
   }

}