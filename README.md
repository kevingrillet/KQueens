# Queens completion
Une instance de ce problème est un échiquier de N par N sur lequel K reines sont déjà placées et ne sont pas en conflit (il doit y en avoir au plus une sur chaque ligne, colonne et diagonale). Résoudre une instance consiste à déterminer s’il existe un moyen de placer N-K reines supplémentaires sans créer de conflits.

La question adressée est celle de la difficulté, en fonction de N et K, de la résolution d’instances produites aléatoirement. Il faudra donc produire des instances aléatoires et résoudre, pour chaque valeur de N et de K testées, au moins 30 instances pour (1) déterminer la valeur moyenne de la difficulté exprimée en termes de nombre de branchements du solveur et (2) déterminer la proportion d’instances ayant au moins une solution.

## Pourcentage ayant une solution  ##
![](https://raw.githubusercontent.com/kevingrillet/KQueens/master/Analyse/result.png)

## Moyenne du nombre de backtrack  ##
![](https://raw.githubusercontent.com/kevingrillet/KQueens/master/Analyse/backtrack.png)

## Moyenne de K reines placées par le générateur  ##
![](https://raw.githubusercontent.com/kevingrillet/KQueens/master/Analyse/realK.png)
