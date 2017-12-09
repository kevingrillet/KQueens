package intro_choco;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import org.chocosolver.samples.AbstractProblem;
import org.chocosolver.solver.constraints.IntConstraintFactory;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.VariableFactory;
import org.chocosolver.util.ESat;
import org.chocosolver.solver.Solver;

public class Nqueens extends AbstractProblem {
	// -1 -> Silencieux, 0 -> Sortie fichier,
	// 1 -> Affichage des grilles, 2 -> Affichage de tout
	static final int DEBUG = 0;
	// Nombre de reines à trouver -> Taille de la grille.
	static int n;
	static int nMax = 100;
	// Nombre de reines placées par le générateur.
	static int k;
	// Nombre de test
	static int t;
	static int tMax = 100;
	// Stockage de sortie du générateur
	int[] init;
	// Tableau de prégénération des coord X/Y
	static ArrayList<Integer> gpX = new ArrayList<Integer>();
	static ArrayList<Integer> gpY = new ArrayList<Integer>();
	// Tableau pour les diagonales
	int[][] diag = { { -1, -1 }, { -1, 1 }, { 1, -1 }, { 1, 1 } };
	// Les variables du problème sont des instances de la classe IntVar.
	IntVar[] vars;
	// Fichier sauvegarde
	static FileWriter writer;
	// Time
	static long time = System.nanoTime();

	/**
	 * Point d'entrée
	 * 
	 * @param args
	 */
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		if (DEBUG >= 0) {
			try {
				writer = new FileWriter(System.getProperty("user.dir") + "/out.csv");
				writer.flush();
				writer.append("n, k, kPlace, ok, back, ns\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		for (n = 1; n <= nMax; n++) {
			for (int i = 0; i < n; i++) {
				gpX.add(i);
				gpY.add(i + 1);
			}
			for (k = 0; k <= n; k++) {
				for (t = 0; t < tMax; t++) {
					if (DEBUG >= 0) {
						time = System.nanoTime();
						if (DEBUG > 0)
							System.out.println("<== n: " + n + " k: " + k + "==>");
					}
					Nqueens sol = new Nqueens();
					sol.generate();
					// Cette méthode héritée de AbstractProblem appelle les
					// autres méthodes
					// dans l’ordre approprié et affiche le résultat.
					sol.execute();
				}
			}
			System.out.println("Fini \\o/");
		}
		if (DEBUG >= 0) {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Générateur maison qui place k reines sur les n.
	 * 
	 * @return TRUE si il trouve une solution, FALSE sinon
	 */
	@SuppressWarnings("unused")
	private boolean generate() {
		int iTry = 0;
		ArrayList<Integer> pX = new ArrayList<Integer>(gpX);
		ArrayList<Integer> pY = new ArrayList<Integer>(gpY);
		init = new int[n];
		Random rnd = new Random();
		boolean chk, isValid;
		int x, y, dx, dy, cpt, nb = 0;
		while (nb < k) {
			isValid = true;
			// On tire au sort un X/Y pas encore utilisé
			x = pX.get(rnd.nextInt(pX.size()));
			y = pY.get(rnd.nextInt(pY.size()));
			chk = true;
			cpt = 1;
			// check diag
			while (chk && isValid && nb > 0) {
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
				if (DEBUG > 1)
					System.out.println("===== OK ===== X:" + x + " Y:" + y);
				init[x] = y;
				// On retire le X/Y utilisé
				pX.remove(pX.indexOf(x));
				pY.remove(pY.indexOf(y));
				iTry = 0;
				nb++;
			} else {
				if (DEBUG > 1)
					System.out.println("----- KO ----- X:" + x + " Y:" + y);
				// Si ca marche pas, on s'arrête, des fois ca marche juste pas
				// TODO: Retour en arrière & regénération jusqu'à solution...
				if (iTry > n * 100) {
					if (DEBUG > 0)
						System.out.println("<-- Pas de solution n: " + n + " k: " + k + " nb: " + nb + " -->");
					return false;
				} else {
					iTry++;
				}
			}
		}
		if (DEBUG > 0) {
			for (int i = 0; i < init.length; i++) {
				for (int j = 0; j < init.length; j++)
					System.out.print(init[j] - 1 == i ? "|Q" : "| ");
				System.out.println("|");
			}
			System.out.println();
		}
		return true;
	}

	/**
	 * Ecrit le résultat dans un CSV
	 * 
	 * @param ok
	 *            Génération réussie?
	 * @throws IOException
	 */
	private void writeCSV(boolean ok, long back) throws IOException {
		if (DEBUG >= 0) {
			int kPlace = 0;
			for (int i = 0; i < n; i++) {
				if (init[i] != 0)
					kPlace++;
			}
			// writer.append("n, k, kPlace, ok, back, ns\n");
			writer.append(n + "," + k + "," + kPlace + "," + (ok ? "1" : "0") + "," + back + ","
					+ (System.nanoTime() - time) + "\n");
		}
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

	/**
	 * Construit le problème en complétant la prégénération.
	 */
	@Override
	public void buildModel() {
		// Création du tableau contenant les références des variables du
		// problème.
		vars = new IntVar[n];
		// Création des variables ayant toutes pour domaine 1..n.
		for (int i = 0; i < vars.length; i++) {
			if (init[i] == 0) {
				vars[i] = VariableFactory.enumerated("Q_" + i, 1, n, solver);
			} else {
				vars[i] = VariableFactory.fixed("Q_" + i, init[i], solver);
			}
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

	/**
	 * Sortie propre appelée après le solveur.
	 */
	@Override
	@SuppressWarnings("unused")
	public void prettyOut() {
		if (DEBUG >= 0) {
			try {
				writeCSV(solver.isFeasible().equals(ESat.TRUE), solver.getMeasures().getBackTrackCount());
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (DEBUG > 0) {
				if (solver.isFeasible().equals(ESat.TRUE)) {
					for (int i = 0; i < vars.length; i++) {
						for (int j = 0; j < vars.length; j++)
							System.out.print(solver.getSolutionRecorder().getLastSolution().getIntVal(vars[j]) - 1 == i
									? "|Q" : "| ");
						System.out.println("|");
					}
				} else {
					System.out.println("Pas de solutions !");
				}
			}
		}
	}
}
