# Algoritmo-e-Esrutura-de-Dados-III
# CRUD Genérico

## Classe Livro
**Atributos**: ID
               Nome
               Autor
               Preço   
           
**Métodos**:Construtores          
            Sets e Gets
            toByteArray e fromByteArray
           
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
Localizo e leio o livro no arquivo. Repito esse processo para os três Livros criados.
```
System.out.println(arqLivros.read(id1));
```
