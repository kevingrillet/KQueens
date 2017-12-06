package intro_choco;

import java.util.Random;
import org.chocosolver.samples.AbstractProblem;
import org.chocosolver.solver.constraints.IntConstraintFactory;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.VariableFactory;
import org.chocosolver.util.ESat;
import org.chocosolver.solver.Solver;

public class Nqueens extends AbstractProblem {
	// Nombre de reines à trouver -> Taille de la grille.
	int n = 10;
	// Nombre de reines placées par le générateur.
	int k = 2;
	// Stockage de sortie du générateur
	int[] init;
	// Tableau pour les diagonales
	int[][] diag = { { -1, -1 }, { -1, 1 }, { 1, -1 }, { 1, 1 } };
	// Les variables du problème sont des instances de la classe IntVar.
	IntVar[] vars;

	public static void main(String[] args) {
		Nqueens sol = new Nqueens();
		sol.generate();
		// Cette méthode héritée de AbstractProblem appelle les autres méthodes
		// dans l’ordre approprié et affiche le résultat.
		sol.execute();
	}

	private void generate() {
		init = new int[n];
		for (int i = 0; i < n; i++)
			init[i] = 0;
		Random rnd = new Random();
		boolean chk, isValid;
		int x, y, dx, dy, cpt, nb = 0;
		while (nb < k) {
			isValid = false;
			x = rnd.nextInt(n);
			//horizontal
			if (init[x] == 0) {
				isValid = true;
				y = rnd.nextInt(n) + 1;
				//vertical
				for (int i = 0; i < n; i++) {
					if (init[i] == y) {
						isValid = false;
						break;
					}
				}
				chk = true;
				cpt = 1;
				//diag
				while (chk && isValid) {
					chk = false;
					for (int i = 0; i < 4; i++) {
						dx = diag[i][0] * cpt;
						dy = diag[i][1] * cpt;
						if (x + dx >= 0 && x + dx < n && y + dy > 0 && y + dy < n + 1) {
							chk = true;
							if (init[x + dx] == y + dy) {
								isValid = false;
								break;
							}
						}
					}
					cpt++;
				}
				if (isValid) {
					init[x] = y;
					System.out.println("===== OK ===== X:" + x + " Y:" + y);
					nb++;
				} else {
					System.out.println("----- KO ----- X:" + x + " Y:" + y);
				}
			}
		}
		for (int i = 0; i < init.length; i++) {
			for (int j = 0; j < init.length; j++)
				System.out.print(init[j] - 1 == i ? "|Q" : "| ");
			System.out.println("|");
		}
		System.out.println();
	}

	/**
	 * Cette méthode définit la stratégie de recherche. Ici, on conserve la
	 * stratégie par défaut.
	 */
	@Override
	public void configureSearch() {
	}

	/**
	 * Cette méthode crée une instance de Solver et place sa référence dans la
	 * variable solver héritée de la classe AbstractProblem.
	 */
	@Override
	public void createSolver() {
		solver = new Solver("NQueen");
	}

	/**
	 * Cette méthode indique au solveur ce qu’il doit faire, par exemple trouver
	 * une solution ou toutes les solutions.
	 */
	@Override
	public void solve() {
		// solver.findAllSolutions();
		solver.findSolution();
	}

	@Override
	public void buildModel() {
		// Création du tableau contenant les références des variables du
		// problème.
		vars = new IntVar[n];
		// Création des variables ayant toutes pour domaine 1..n.
		for (int i = 0; i < vars.length; i++) {
			if (init[i] == 0)
				vars[i] = VariableFactory.enumerated("Q_" + i, 1, n, solver);
			else
				vars[i] = VariableFactory.fixed("Q_" + i, init[i], solver);
		}
		// Ajout d’une contrainte imposant que les variables aient toutes des
		// valeurs différentes.
		solver.post(IntConstraintFactory.alldifferent(vars, "AC"));
		// Technique de filtrage utilisée (Arc Consistency).
		for (int i = 0; i < n - 1; i++) {
			for (int j = i + 1; j < n; j++) {
				int k = j - i;
				// Ajout des contraintes imposant qu’une paire de reine ne doit
				// pas se trouver sur une même diagonale.
				solver.post(IntConstraintFactory.arithm(vars[i], "!=", vars[j], "+", -k));
				solver.post(IntConstraintFactory.arithm(vars[i], "!=", vars[j], "+", k));
			}
		}
	}

	@Override
	public void prettyOut() {
		if (solver.isFeasible().equals(ESat.TRUE)) {
			for (int i = 0; i < vars.length; i++) {
				for (int j = 0; j < vars.length; j++)
					System.out.print(
							solver.getSolutionRecorder().getLastSolution().getIntVal(vars[j]) - 1 == i ? "|Q" : "| ");
				System.out.println("|");
			}
		} else {
			System.out.println("Pas de solutions !");
		}
	}
}
