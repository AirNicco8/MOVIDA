# MOVIDA
Unibo ASD project 19/20

Autori:
* ** Lorenzo Niccolai 0000838300 **
* ** Raffaello Balica matricola **

GitHub: https://github.com/DatNigazz/MOVIDA

Trello: https://trello.com/b/hQa8sNSs/movida


## Compilazione

```bash
./compile.sh
```
## Esecuzione

```bash
./run.sh
```

## Introduzione:

Algoritmi di ordinamento:
* SelectionSort
* MergeSort

Implementazioni dizionario:
* AVL
* HashConcatenamento

Le librerie usate sono per lo più standard di java, per la parte riguardante i grafi abbiamo usato una parte della libreria del corso Asdlab.

## MovidaCore

La classe principale ed entry point consiste di 3 strutture principali:
* Dizionario Movies: che contiene tutti i record dei films
* Dizionario People: che contiene tutti i record di attori e registi
* Grafo Collabs: che ha come nodi tutti gli attori presenti in People e come archi le collaborazioni tra di essi

Si usa un istanza della classe utils per caricare i dati da file di testo, nei metodi della suddetta classe viene controllata la correttezza del formato e vengono creati i record.

Per quanto riguarda l'interfaccia IMovidaConfig il metodo setMap cambia l'implementazione del dizionario trasferendo i dati già caricati se presenti.

Mentre l'implementazione di IMovidaSearch usa un metodo di ordinamento che in base all'algoritmo selezionato effettua un ordinamento con un comparator specifico per ogni ricerca specifica (```searchMostVotedMovies```, ```searchMostActiveActors```, etc)

## Movida grafi

```getDirectCollaboratorsOf``` restituisce gli attori corrispondenti ai nodi adiacenti a quello dell'attore passato come parametro.

Il metodo ```getTeamOf``` utilizza la BFS per visitare il grafo sconnesso di cui fa parte l'attore passato come parametro.

Mentre per ```maximizeCollaborationsInTheTeamOf``` abbiamo usato una sorta di algoritmo di Prim col peso degli archi dato da getScore.
Questo viene applicato per trovare il Maximum Spanning Tree del sottografo dato da ```getTeamOf``` dell'attore passato, che restituirà i nodi e gli archi che comporranno il suddetto. Per la ```PriorityQueue``` abbiamo usato la Collection di Java.
