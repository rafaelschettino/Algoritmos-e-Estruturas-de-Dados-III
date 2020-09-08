# CRUD Indexado

## Classe Livro
**Atributos**: ID, Nome, Autor e Preço   
           
**Métodos**:Construtores, Sets e Gets, toByteArray e fromByteArray, chaveSecundaria

## Classe CRUD
Construtor da classe CRUD. Criação do arquivo de tipo RandomAccessFile e escrita do número 0 no cabeçalho do arquivo, representando o primeiro ID. Também há a criação dos arquivos de índice. 
```
public CRUD(String nomeArquivo, Constructor<T> construtor){}
```
Método que armazena o objeto recebido no arquivo. A primeira coisa a ser feita foi abrir o arquivo no início, guardar o valor do último ID utilizado e atribuir esse número ao objeto recebido como parâmetro. O segundo passo foi atualizar o cabeçalho do arquivo com o novo ID (ID+1). E a última coisa feita foi abrir o arquivo no final e criar um registro para o objeto recebido, com os bytes de lápide, indicador de tamanho e os campos para os atributos. Além disso, tenho que incluir o novo registro criado nos índices direto e indireto.
```
public int create(T objeto){}
```
Primeiro método de leitura de um registro. Recebo o ID do objeto a ser lido como parâmetro. A partir desse ID, farei a busca no índice direto que me retorna o endereço do registro no arquivo. Abro o arquivo nesse endereço, se o registro estiver disponível, realizo sua leitura. Caso o registro não seja encontrado no índice ou ele esteja indisponpivel, retorno um objeto vazio.
```
public T read(int id){}
```
Segundo método de leitura de um registro. Recebo a chave secundária do objeto a ser lido, que no caso é o título do livro. A partir dessa chave, farei a busca no índice indireto que me retorna o ID do registro. Com esse ID, farei exatamente o mesmo processo do primeiro read(). Caso o registro não seja encontrado no índice ou ele esteja indisponpivel, retorno um objeto vazio.
```
public T read(String chaveSecundaria){}
```
Método de exclusão de um registro. Recebo o ID do objeto a ser excluído, como parâmetro. A partir desse ID, utilizo o método read() para obter o endereço do registro que será removido. Abro o arquivo nesse endereço, altero o char de lápide vazio para um asterisco, representando que o objeto não estará mais disponível e retorno true, indicando que a operação foi um sucesso. Além disso, tenho que deletar os dados desse registro em ambos os índices.
```
public boolean delete(int id){}
```
Método de alteração de um registro. Nesse método eu comparo os tamanhos dos registros sem e com alteração. Caso o tamanho do registro alterado seja menor ou igual, apenas sobrescrevo o "novo" objeto no endereço original. Se o tamanho do registro alterado for maior, excluo o registro "desatualizado" e realizo a inclusão do "novo" registro ao fim do arquivo. É necessária a atualização dos dois índices com o novo endereço e com a possível nova chave secundária.
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
Leio o livro no arquivo e mostro o que foi lido. Realizo a busca passando o ID como chave.
```
System.out.println(arqLivros.read(id1));
```
Leio o livro no arquivo e mostro o que foi lido. Realizo a busca passando a chave secundária, que no caso é o título do livro, como chave.
```
System.out.println(arqLivros.read(l3.getNome()));
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