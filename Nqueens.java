package intro_choco;

import org.chocosolver.samples.AbstractProblem;
import org.chocosolver.solver.constraints.IntConstraintFactory;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.VariableFactory;
import org.chocosolver.solver.Solver;

public class Nqueens extends AbstractProblem {
	// Nombre de reines à trouver -> Taille de la grille.
	int n = 10;
	// Nombre de reines placées par le générateur.
	int k = 9;
	// Les variables du problème sont des instances de la classe IntVar.
	IntVar[] vars;

	public static void main(String[] args) {
		// Cette méthode héritée de AbstractProblem appelle les autres méthodes
		// dans l’ordre approprié et affiche le résultat.
		new Nqueens().execute(args);
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
			vars[i] = VariableFactory.enumerated("Q_" + i, 1, n, solver);
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
	}
}
