# **Aplicació d'exemple amb Hibernate: Helpdapp**

## Finalitat de l'aplicació

L'aplicació gestiona dues entitats: Usuari i Petició. La idea és que un usuari pot treballar en diferents peticions i a cada petició poden intervenir diferents tècnics.
Per aquesta raó és un ManyToMany.

Com a peculiaritat, Petició utilitza anotacions mentre que Usuari utilitza fitxer de mapatge.

## Organització del projecte

És un projecte amb maven on s'utilitza Hibernate 6 i com a base de dades MariaDB.

Amb la intenció de separar i delimitar responsabilitats l'estructura de projecte seria la següent:
* Carpeta **Model**: Incorpora els POJOs que tenen el paper d'entitats: Usuari i Peticio.
* Carpeta **DAO**: Incorpora els DAO de cada entitat on hi ha els mètodes que fan:
    * el persist
    * el find
    * el merge
    * el remove 
    * També incorporen un query HQL per llistar tots els objectes de cada entitat.
* Carpetes Views i Controller: Amb l'ànim de fer servir Model Vista Controlador, veureu:
    * Una classe anomenada **InputView** on hi ha el menú i les opcions.
    * Dues classes anomenades **PeticioController** i **UsuariController**, on trobaríem els mètodes cridats per la vista per tal de poder fer accions de CRUD amb les entitats.

El Main és una classe encarregada de cridar el mètode del menu d'InputView i ja InputView interacciona amb els controladors per fer les accions necessàries sobre les entitats.

## Justificació d'algunes propietats emprades

A Petició emprem CASCADE de tipus PERSIST ja que permet que petició pugui persistir nous usuaris, modificar la petició i fer que quan aquesta s'esborri no esborri els usuaris de la taula d'usuaris. El que si fa és esborrar els registres de la taula intermitja Usuari_peticio que es crea com a conseqüència del manytomany.

Una altra possibilitat passaria per fer aparèixer una tercera entitat usuari_petició i fer que totes les relacions deixin de ser manytomany. 

(Quan són relacions onetomany existeix una propietat anomenada orphanRemoval la qual en el cas de manytomany no es pot emprar).

Per altra banda el FETCH es EAGER a Petició per tal d'afavorir en una mateixa sessió el mostrar peticions i usuaris d'una sola vegada. En aquest sentit LAZY és més limitant i ens obliga a fer crides diferides per recuperar cada entitat per separat, però depenent del volum d'objectes persistits ens convindrà més una opció o una altra.

## Explicació de les opcions que té l'aplicació

### Opció de Crear Petició i Usuari

Aquesta opció demana les dades per inserir una nova petició amb unes particularitats: 
* Pot inserir usuaris nous fent la persistència amb el mètode PersistirPeticio del DAO de petició.
* Pot afegir usuaris existents a una petició.

El mecanisme és el següent:
* Demana el DNI de l'usuari, ja que aquest és índex únic.
* Amb aquest DNI verifica si l'usuari ja existeix fent servir getUsuariByDNI.
* Si no existeix procedeix a afegir-lo a la petició amb el mètode addUsuari. Recordeu la importància d'aquest mètode per assegurar la bidireccionalitat entre usuaris i peticions.
* Si existeix l'afegeix en un Set anomenat users. 
* A partir d'aquí persisteix la petició. En aquest moment arrossega els usuaris nous.
* Un cop persistida pregunta si el Set d'usuaris està ple (amb usuaris existents). Si és el cas:
    * Fa un **getUsuaris** de la petició que acabem de persistir i els emmagatzema temporalment en un Set d'usuaris anomenat uset.
    * Itera el Set us#ers i per cada usuari l'afegeix al set uset.
    * En aquest moment el Set uset conté tots els usuaris, tant els que ja s'havien persistit amb la petició com els que ja s'havien persistit amb anterioritat.
    * Fem un setUsuaris de la petició amb el contingut d'uset i finalitzem fent un merge de la petició mitjançant **ModificarPeticio**.

### Opció de crear usuari

Permet crear un nou usuari sense petició. Valida si l'usuari existeix i en aquest cas no el crea.

### Opció de Llistar Peticions amb els seus Usuaris

Permet llistar les peticions persistides i els usuaris relacionats amb cada petició.

### Opció d'Esmenar Petició

Es poden esmenar:
* L'estat de la petició.
* Afegir usuaris a la petició.

En aquest darrer cas la idea és molt semblant a quan creem la petició. Com a diferència, si l'usuari no existeix el persisteix per separat de la petició i l'afegeix a un Set temporal d'emmagatzematge. I si existeix l'afegeix també al Set.

A partir d'aquí modifica el Set d'usuaris de la petició i procedeix amb el merge de la petició fent servir ModificarPeticio.

### Opció d'Esborrar Petició

El que es fa es esborrar la petició sense esborrar els usuaris. Es demana la id de la petició i es valida si existeix. En cas que si es mostra la descripció de la petició i es demana confirmació per esborrar.

### Opció de Llistar usuaris

Llista els usuaris tinguin o no petició. Tots els usuaris.
