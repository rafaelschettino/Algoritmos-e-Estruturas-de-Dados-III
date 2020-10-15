public class ComandosDeTeclado {

	public static boolean Confirma(String resposta){
		return resposta.compareTo("S") == 0
			|| resposta.compareTo("s") == 0;
	}

	public static boolean Recusa(String resposta){
		return resposta.compareTo("N") == 0
			|| resposta.compareTo("n") == 0;
	}

	public static boolean Vazio(String resposta){
		return resposta.compareTo("") == 0;
	}
}
