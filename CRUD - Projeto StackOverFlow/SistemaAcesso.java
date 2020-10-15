import java.util.*;

public class SistemaAcesso {

    public static final Scanner sc = new Scanner(System.in);
    public static int menuEscolha;
    public static int idUsuarioLogado;

    public static boolean registro(CRUD<Usuario> arqUsuarios) throws Exception{

        boolean sucesso = false;
        String email = sc.nextLine();

        if(email.trim().equals("")){
            // E-mail vazio
            sucesso = false;
            System.out.println("E-mail inválido!");
        } else {
            // Verifica se E-mail já existe
            Usuario usuario = arqUsuarios.read(email);
            if(usuario == null) {
                // E-mail inexistente
                System.out.print("Nome: ");
                String nome = sc.nextLine();
                System.out.print("Senha: ");
                String senha = sc.nextLine();

                System.out.println("\n" +
                    "Email: " + email     + "\n" + 
                    "Nome de usuário: " + nome   + "\n" +
                    "Senha: " + senha     + "\n");

                System.out.print("Dados corretos? (S/N): ");
                String resposta = sc.nextLine().toUpperCase();
                if(resposta.equals("S")){
                    System.out.print("Confirma (S/N): ");
                    resposta = sc.nextLine().toUpperCase();
                    if(resposta.equals("S")){
                        Usuario user = new Usuario(-1, nome, email, senha);
                        arqUsuarios.create(user);
                        sucesso = true;
                    } else {
                        System.out.println("Cancelando cadastro...");
                        sucesso = false;
                    }
                } else {
                    System.out.println("Tente novamente!");
                    sucesso = false;
                }

            } else {
                // E-mail já existe
                System.out.println("Este e-mail já foi cadastrado!");
                System.out.println("Tente novamente!");
                sucesso = false;
            }
        }

        return sucesso;

    }

    public static int acessoSistema(CRUD<Usuario> arqUsuarios) throws Exception{

        //boolean sucesso = false;
        String email = sc.nextLine();
        idUsuarioLogado = -1;
        Usuario user = arqUsuarios.read(email);

        if(user == null) {
            // E-mail não cadastrado
            System.out.println("Este e-mail não foi cadastrado!");
            System.out.println("Tente novamente!");
            //sucesso = false;
        } else {
            System.out.print("Senha: ");
            String senha = sc.nextLine();
            if(senha.equals(user.getSenha())){
                //Vai para tela principal
				idUsuarioLogado = user.getID();
                System.out.print("Acesso permitido!");
                //sucesso = true;
            } else {
                System.out.println("Senha incorreta!");
                System.out.println("Tente novamente!");
                //sucesso = false;
            }
        }
    
        //return sucesso;
        return idUsuarioLogado;
    }

    public static void MenuUsuarioLogado(int idUsuarioLogado){
		try {
			do {
				UI.printMenuPerguntas();
				menuEscolha = Integer.parseInt(String.valueOf(sc.nextLine()));
				switch (menuEscolha) { 
					case 0:
						UI.clearScreen();
						break;

					case 1:
						SistemaPerguntas.MenuCriacaoPerguntas(idUsuarioLogado);
						break;

                    case 2:
						SistemaPerguntas.MenuConsultaPerguntas(idUsuarioLogado);
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