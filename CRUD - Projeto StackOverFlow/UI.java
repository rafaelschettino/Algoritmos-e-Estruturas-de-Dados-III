import java.util.Date;
import java.util.Scanner;

public class UI {
	public static final Scanner sc = new Scanner(System.in);

	// https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println
	  public static final String ANSI_RESET = "\u001B[0m";
	  public static final String ANSI_BLACK = "\u001B[30m";
	  public static final String ANSI_RED = "\u001B[31m";
	  public static final String ANSI_GREEN = "\u001B[32m";
	  public static final String ANSI_YELLOW = "\u001B[33m";
	  public static final String ANSI_BLUE = "\u001B[34m";
	  public static final String ANSI_PURPLE = "\u001B[35m";
	  public static final String ANSI_CYAN = "\u001B[36m";
	  public static final String ANSI_WHITE = "\u001B[37m";
	
	// https://stackoverflow.com/questions/2979383/java-clear-the-console
	public static void clearScreen() {
		try{
			System.out.print("\033[H\033[2J");
		    System.out.flush();
			if (System.getProperty("os.name").contains("Windows"))
				new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
		 else
			Runtime.getRuntime().exec("clear");
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public static void printMenu() {
		//clearScreen();
		System.out.print(ANSI_CYAN +"****************************************\n");
		System.out.println(ANSI_CYAN +"*  " +ANSI_WHITE +"ACESSO" +ANSI_CYAN +"                              *");
		System.out.println(ANSI_CYAN +"*                                      *");
		System.out.println(ANSI_CYAN +"*                                      *");
		System.out.println(ANSI_CYAN +"*  " +ANSI_WHITE +"1) Acesso ao sistema" +ANSI_CYAN +"                *");
		System.out.println(ANSI_CYAN +"*  " +ANSI_WHITE +"2) Novo usuário (primeiro acesso)" +ANSI_CYAN +"   *");
		System.out.println(ANSI_CYAN +"*                                      *");
		System.out.println(ANSI_CYAN +"*  " +ANSI_WHITE +"0) Sair" +ANSI_CYAN +"                             *");
		System.out.print(ANSI_CYAN +"****************************************\n");
		System.out.print("Opção: ");
	}

	public static void printMenuPerguntas() {
		//clearScreen();
		System.out.print(ANSI_CYAN +"****************************************\n");
		System.out.println(ANSI_CYAN +"*  " +ANSI_WHITE +"PERGUNTAS 1.0" +ANSI_CYAN +"                       *");
		System.out.println(ANSI_CYAN +"*                                      *");
		System.out.println(ANSI_CYAN +"*  " +ANSI_WHITE +"INICIO" +ANSI_CYAN +"                  	  		   *");
		System.out.println(ANSI_CYAN +"*                                      *");
		System.out.println(ANSI_CYAN +"*  " +ANSI_WHITE +"1) Criação de perguntas" +ANSI_CYAN +"             *");
		System.out.println(ANSI_CYAN +"*  " +ANSI_WHITE +"2) Consultar/responder perguntas" +ANSI_CYAN +"    *");
		System.out.println(ANSI_CYAN +"*  " +ANSI_WHITE +"3) Notificações" +ANSI_CYAN +"                     *");
		System.out.println(ANSI_CYAN +"*                                      *");
		System.out.println(ANSI_CYAN +"*  " +ANSI_WHITE +"0) Sair" +ANSI_CYAN +"                             *");
		System.out.print(ANSI_CYAN +"****************************************\n");
		System.out.print("Opção: ");
	}

	public static void printMenuCriacaoPerguntas() {
		//clearScreen();
		System.out.print(ANSI_CYAN +"****************************************\n");
		System.out.println(ANSI_CYAN +"*  " +ANSI_WHITE +"PERGUNTAS 1.0" +ANSI_CYAN +"                       *");
		System.out.println(ANSI_CYAN +"*                                      *");
		System.out.println(ANSI_CYAN +"*  " +ANSI_WHITE +"INICIO > CRIAÇÃO DE PERGUNTAS" +ANSI_CYAN +"       *");
		System.out.println(ANSI_CYAN +"*                                      *");
		System.out.println(ANSI_CYAN +"*  " +ANSI_WHITE +"1) Listar" +ANSI_CYAN +"                           *");
		System.out.println(ANSI_CYAN +"*  " +ANSI_WHITE +"2) Incluir" +ANSI_CYAN +"                          *");
		System.out.println(ANSI_CYAN +"*  " +ANSI_WHITE +"3) Alterar" +ANSI_CYAN +"                          *");
		System.out.println(ANSI_CYAN +"*  " +ANSI_WHITE +"4) Arquivar" +ANSI_CYAN +"                         *");
		System.out.println(ANSI_CYAN +"*                                      *");
		System.out.println(ANSI_CYAN +"*  " +ANSI_WHITE +"0) Sair" +ANSI_CYAN +"                             *");
		System.out.print(ANSI_CYAN +"****************************************\n");
		System.out.print("Opção: ");
	}


public static void printMenuConsulta() {
		//clearScreen();
		System.out.println(ANSI_CYAN +ANSI_CYAN +"PERGUNTAS 1.0" +ANSI_WHITE);
		System.out.println(ANSI_CYAN +ANSI_CYAN +"=============" +ANSI_WHITE + "\n");
		System.out.println(ANSI_CYAN +ANSI_CYAN +"INICIO > PERGUNTAS" +ANSI_WHITE +"\n");
		System.out.println(ANSI_CYAN +ANSI_CYAN +"Busque as perguntas por palavra chave separadas por ponto e vírgula" +ANSI_WHITE);
		System.out.println(ANSI_CYAN +ANSI_CYAN + "Ex: política;Brasil;eleições\n");
		//System.out.println(ANSI_CYAN +ANSI_CYAN +"Digite 0 se deseja sair" +ANSI_WHITE +"\n");
		System.out.print(ANSI_CYAN +ANSI_CYAN +"Palavras chave: " +ANSI_WHITE );		

		//System.out.print("Opção: ");
	}

	public static void printPergunta(Pergunta pergunta, int posicao) {
		Date data = new Date(pergunta.criacao);

		if(posicao > 0)
			System.out.println("Posição :" + (posicao));
			
		System.out.println(ANSI_CYAN +"" +ANSI_WHITE + pergunta.getID()+"." +ANSI_CYAN +"                          ");
		System.out.println(ANSI_CYAN +"" +ANSI_WHITE + data.toString()+ANSI_CYAN +"                          ");
		System.out.println(ANSI_CYAN +"" +ANSI_WHITE + pergunta.pergunta+ANSI_CYAN +"                         ");
    	System.out.println(ANSI_CYAN +"" +ANSI_WHITE + pergunta.palavrasChave +ANSI_CYAN +"                         ");
		System.out.println("");
	}


	public static void printTelaDePergunta(){
		System.out.println("Insira a pergunta!\n");
	}

  public static void printTelaPalavraChave(){
		System.out.println("Digite as palavras chave da pergunta separando por ponto e vírgula:\n");
	}

	public static void printTelaNovoUsuario(){
		System.out.println("NOVO USUARIO\n");
		System.out.print("E-mail: ");
		System.out.flush();
	}

	public static void acesso(){
		System.out.println("\nACESSO AO SISTEMA\n" +ANSI_CYAN);
		System.out.print(ANSI_GREEN+ "E-mail: " +ANSI_WHITE);
	}

	public static void qualquerTeclaParaContinuar(){
		System.out.println("Pressione qualquer tecla para continuar\n");
		sc.nextLine();
	}

}