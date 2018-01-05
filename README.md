# Queens completion
Une instance de ce problème est un échiquier de N par N sur lequel K reines sont déjà placées et ne sont pas en conflit (il doit y en avoir au plus une sur chaque ligne, colonne et diagonale). Résoudre une instance consiste à déterminer s’il existe un moyen de placer N-K reines supplémentaires sans créer de conflits.

La question adressée est celle de la difficulté, en fonction de N et K, de la résolution d’instances produites aléatoirement. Il faudra donc produire des instances aléatoires et résoudre, pour chaque valeur de N et de K testées, au moins 30 instances pour (1) déterminer la valeur moyenne de la difficulté exprimée en termes de nombre de branchements du solveur et (2) déterminer la proportion d’instances ayant au moins une solution.

## Pourcentage ayant une solution  ##
![](https://raw.githubusercontent.com/kevingrillet/KQueens/master/Analyse/result.png)

## Moyenne du nombre de backtrack  ##
![](https://raw.githubusercontent.com/kevingrillet/KQueens/master/Analyse/backtrack.png)

## Moyenne de K reines placées par le générateur  ##
![](https://raw.githubusercontent.com/kevingrillet/KQueens/master/Analyse/realK.png)

## Algorithme ##
```
ALGORITHME generate
// Générateur maison qui place k reines sur les n

VARIABLES
    chk, isValid : BOOLEAN;
    iTry, x, y, dx, dy, cpt, nb : INTEGER;
    init : INTEGER[];
    pX, pY : ARRAYLIST<INTEGER>;

DEBUT
    pX <- gpX; //Liste des X (0..n-1)
    pY <- gpY; //Liste des X (1..n)
    init <- new INTEGER[n];
    iTry <- 0;
    nb <- 0;
    TANT QUE (nb < k) FAIRE
        x <- Tirage random de pX;
        y <- Tirage random de pY;
        chk <- VRAI;
        cpt <- 1;
        // Vérification des diagonales, temps qu'on a pas d'erreur
        TANT QUE (chk ET isValid ET nb > 0) FAIRE
            chk <- FAUX;
            // On boucle sur les 4 directions
            POUR i ALLANT DE 0 A 4 FAIRE
                dX <- diag[i][0] * cpt;
                dY <- diag[i][1] * cpt;
                // Vérification globale pour savoir si on est dans la grille
                SI (x + dx >= 0 ET x + dx < n ET y + dy > 0 ET y + dy < n + 1) ALORS
                    chk <- VRAI;
                    SI () ALORS
                        isValid <- FAUX;
                        STOP;
                    FIN SI
                FIN SI
            FIN POUR
            ctp <- cpt + 1;
        FIN TANT QUE
        SI isValid ALORS
            init[x] <- y;
            // On retire le X/Y utilisé
            pX.REMOVE(x);
            pY.REMOVE(y);
        SINON
            SI (iTry > n * 1000) ALORS
                ARRET;
            SINON
                iTry <- iTry + 1;
            FIN SI
        FIN SI
    FIN TANT QUE
FIN
```
