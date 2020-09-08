# Algoritmo-e-Esrutura-de-Dados-III
# CRUD Genérico

## Classe Livro
**Atributos**: ID, Nome, Autor e Preço   
           
**Métodos**:Construtores, Sets e Gets, toByteArray e fromByteArray

## Classe CRUD
Construtor da classe CRUD. Criação do arquivo de tipo RandomAccessFile e escrita do número 0 no cabeçalho do arquivo, representando o primeiro ID. 
```
public CRUD(String nomeArquivo, Constructor<T> construtor){}
```
Método que armazena o objeto recebido no arquivo. A primeira coisa a ser feita foi abrir o arquivo no início, guardar o valor do último ID utilizado e atribuir esse número ao objeto recebido como parâmetro. O segundo passo foi atualizar o cabeçalho do arquivo com o novo ID (ID+1). E a última coisa feita foi abrir o arquivo no final e criar um registro para o objeto recebido, com os bytes de lápide, indicador de tamanho e os campos para os atributos.
```
public int create(T objeto){}
```
Método de leitura de um registro. Abro o arquivo depois do cabeçalho e farei uma busca sequencial de um registro utilizando como chave o seu ID. Essa busca vai acontecer enquanto eu não encontrar o registro e enquanto eu não chegar ao fim do arquivo. Para cada registro, realizo a leitura dos seus bytes de lápide para verificar se ele se encontra disponível. Para cada registro indisponível encontrado eu vou para o próximo. Para cada registro disponível encontrado comparo seu ID com o ID recebido como parâmetro na função. Caso os IDs sejam iguais, encerro a procura e retorno o objeto encontrado.
```
public T read(int id){}
```
Método de exclusão de um registro. Processo parecido com o de leitura. A condição para o loop é a mesma que a do método de leitura. Para cada execução da estrutura de repetição eu guardo o endereço da lápide de um registro e crio um novo objeto correspondente a esse registro. Caso o ID dese objeto seja igual ao ID recebido como parâmetro da função, tenho que excluí-lo. Abro o arquivo na posição da lápide (que guardei ao início do loop), altero o char vazio para um asterisco, representando que o objeto não estará mais disponível, encerro o loop e retorno true, indicando que a operação foi um sucesso.
```
public boolean delete(int id){}
```
Método de alteração de um registro. Nesse método não consegui utilizar as condições estabelecidas para  tamanho do registro atual e tamanho do registro após ser alterado. Assim, o que foi feito foi a exclusão do registro desatualizado e a inclusão do mesmo, já com as alterações, ao final do arquivo. Com isso, essa função ficou uma mistura entre os métodos delete() e create(). Fazendo o mesmo processo do método de exclusão e ainda, ao final, a inclusão do novo registro ao final do arquivo.
```
public boolean update(T objeto){}
```

## Classe de Teste
Criação dos objetos do tipo livro para serem utilizados pela classe CRUD.
```
Livro l1 = new Livro(-1, "Eu, Robô", "Isaac Asimov", 14.9F);
Livro l2 = new Livro(-1, "Eu Sou A Lenda", "Richard Matheson", 21.99F);
Livro l3 = new Livro(-1, "Número Zero", "Umberto Eco", 34.11F);
```
Criação do CRUD de tipo livros
```
CRUD<Livro> arqLivros = new CRUD<>("livros.db", Livro.class.getConstructor());
```
Insiro o livro no arquivo. Repito esse processo para os três Livros criados.
```
id1 = arqLivros.create(l1); 
l1.setID(id1);
```
Leio o livro no arquivo e mostro o que foi lido. Repito esse processo para os três Livros criados.
```
System.out.println(arqLivros.read(id1));
```
Excluo o livro do arquivo e tento ler seu registro. Método read() retornará "null", já que o registro foi apagado.
```
arqLivros.delete(id3);
Livro l = arqLivros.read(id3);
```
Realizo a alteração de um registro e printo seus atributos demonstrando o resultado da alteração.
```
l2.autor = "Richard Burton Matheson";
arqLivros.update(l2);
System.out.println(arqLivros.read(id2));
```
