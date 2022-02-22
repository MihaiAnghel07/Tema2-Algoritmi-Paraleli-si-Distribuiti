****************************
Nume: Anghel Mihai-Gabriel *
Grupa: 336CC               *
****************************


****************Clase folosite + detalierea acestora****************

Clasa Tema2: este considerata utilizatorul care cere procesarea unui set de 
	     date;
Clasa Coordinator: este coordonatorul care asigneaza thread-urilor fie task-uri
		   de Map, fie task-uri Reduce + realizeaza scrierea 
		   rezultatului;
Clasa Generator: aceasta clasa ajuta coordonatorul sa genereze task-uri 
		 Map/Reduce;
Clasa MapClass: este clasa carea realizeaza functia de Map asupra task-urilor
		map;
Clasa ReduceClass: aceasta clasa realizeaza etapa de reduce care are inclusa si
                   etapa de processing asupra task-urilor reduce;
Clasa MapTask: defineste structura unui task map care contine informatia 
	       necesara prelucrarii textului;
Clasa ReduceTask: defineste structura unui task reduce care contine informatia 
	          necesara combinarii rezultatelor de la etapa de map pentru
		  a reduce numarul task-urilor;
Clasa Output: defineste structura output-ului, pentru fiecare task reduce, se 
	      creeaza o instanta output cu care se face afisarea rezultatelor;



*****************************FLOW************************************

Utilizatorul cere coordonatorului sa proceseze un set de date, coordonatorul isi
incepe treaba si cu ajutorul generatorului creeaza task-uri de tip map(generatorul
deschide fisierul de input si adauga informatia necesara impartirii textului in
fragmente, insa nu adauga si textul propriu-zis). Coordonatorul creeaza un numar
de thread-uri (numar primit in linia de comanda) care vor aplica Map asupra acestor
task-uri. Acum, se citeste informatia din fisier, fiecare task respecta fragmentul 
asignat anterior (respectand si cele doua cazuri cand inceputul/sfarsitul unui 
fragment poate sa fie in mijlocul unui cuvant) si se extrag cuvintele din fragment
si se creaza dictionarul si lista de cuvinte maximale aferente fiecarui task.
Coordonatorul preia aceste task-uri prelucrate, pe baza lor creeaza task-urile Reduce
(tot cu ajutorul generatorului care reduce numarul task-urilor la nrFiles task-uri
Reduce) si astfel fiecare task reduce va contine o lista de dictionare si o lista
de liste de cuvinte maximale(partiale). Se asigneaza task-urile thread-urilor si
se aplica Reduce, deci se reduce lista de dictionare la un singur dictionar(acelasi
lucru pentru lista de liste de cuvinte maximale cu mentiunea ca se face o triere
a cuvintelor astfel incat se pastreaza doar cele de lungime maxima).Dupa reducere,
se aplica procesarea care calculeaza rangul fiecarui task pe baza dictionarului.
Se creeaza output-ul si coordonatorul face sortarea in functie de rang si afisarea
(scrierea pe disc).
   