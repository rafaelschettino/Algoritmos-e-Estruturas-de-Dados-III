import java.io.File;
import java.util.*;

public class Main {

	public static Scanner sc = new Scanner(System.in);
	public static int menuEscolha;
	public static boolean sucesso = false;
	public static int idUsuario;

	public static void main(String[] args) throws Exception {

		try {
			CRUD<Usuario> arqUsuarios = new CRUD<>(Usuario.class.getConstructor());

			do {
				UI.printMenu();
				menuEscolha = Integer.parseInt(String.valueOf(sc.nextLine()));
				switch (menuEscolha) {
				case 0:
					UI.clearScreen();
					break;

				case 1:
					UI.acesso();
					idUsuario = SistemaAcesso.acessoSistema(arqUsuarios);

					if(idUsuario != -1){
						SistemaAcesso.MenuUsuarioLogado(idUsuario);
					}//fim if

					break;

				case 2:
					UI.printTelaNovoUsuario();
					sucesso = SistemaAcesso.registro(arqUsuarios);
					if (sucesso){
						System.out.println("Cadastro efetuado com sucesso!");
					}//fim if

					break;

				default:
					break;

				}

			} while (menuEscolha != 0);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
}