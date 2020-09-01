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
Método que armazena o objeto recebido no arquivo. A primeira coisa a ser feita foi abrir o arquivo no início, guardar o valor do último ID utilizado e atribuir esse número ao objeto recebido como parâmetro. O segundo passo foi atualizar o cabeçalho do arquivo com o novo ID (id+1). E a última coisa feita foi abrir o arquivo no final e criar um registro para o objeto recebido, com os bytes de lápide, indicador de tamanho e os campos para os atributos.
```
public int create(T objeto){}
```
Método de leitura de um registro. Abro o arquivo depois do cabeçalho e farei uma busca sequencial de um registro utilizando como chave o seu id. Para cada registro, realizo a leitura dos seus bytes de lápide para verificar se ele se encontra disponível. Para cada registro indisponível encontrado eu vou para o próximo. Para cada registro disponível encontrado comparo seu ID com o ID recebido como parâmetro na função. Caso os IDs sejam iguais, encerro a procura e retorno o objeto encontrado.
```
public T read(int id){}
```

```
public boolean delete(int id){}
```

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
