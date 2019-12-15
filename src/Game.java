import java.io.*;
import java.util.ArrayList;

public class Game {

	private static Map map;
	private static Player player;

	public void initialize(File inputFile) throws Exception{
		BufferedReader br = new BufferedReader(new FileReader(inputFile));
		
		// Read the first of the input file
		String line = br.readLine();
		int M = Integer.parseInt(line.split(" ")[0]);
		int N = Integer.parseInt(line.split(" ")[1]);

		this.map = new Map(M,N);

		for (int i = 0; i < M; i++) {
			line = br.readLine();
			for(int j = 0; j < N; j++){
				map.setPath(i, j, line.charAt(j));
			}
		}

		line = br.readLine();
		while (line != null) {
			String content[] = line.split(",");
			for (int i = 0; i < content.length; i++) {
				content[i].trim();
			}

			if(content.length > 3) {
				Pokemon pokemon = new Pokemon(Integer.parseInt(content[0].substring(1)),
						Integer.parseInt(content[1].substring(0, content[1].length() - 1)),
						content[2], content[3],
						Integer.parseInt(content[4].trim()), Integer.parseInt(content[5].trim()));
				map.addPokemon(pokemon);
			} else  {
				Station station = new Station(Integer.parseInt(content[0].substring(1)),
						Integer.parseInt(content[1].substring(0, content[1].length() - 1)),
						Integer.parseInt(content[2].trim()));
				map.addStation(station);
			}

			line = br.readLine();
		}

		br.close();

		this.player = new Player(map.getStart().getRow(), map.getStart().getCol());
		map.getVisitList().add(0, map.getStart());
		map.getVisitList().add(map.getDestination());
		map.setWholeList();
		map.findShortestPath();
		map.getVisitList().remove(map.getStart());
		map.getVisitList().remove(map.getDestination());
		player.addPath(map.getStart());
	}

	public int getScore(Cell cell) {
		if (cell instanceof Station) {
			return ((Station) cell).getNumOfBalls();
		} else if (cell instanceof Pokemon) {
			return 5 + player.newType(((Pokemon) cell)) * 10 + player.cpChange(cell) - ((Pokemon) cell).getNumOfRequiredBalls();
		}
		return 0;
	}

	public int getBenefit(Cell current, Cell next) {
		return getScore(next) - map.getDistance(current, next);
	}

	public Pair findPath(ArrayList<Cell> visitList, Player currentPlayer, int k) {
		int score = -map.getDistance(currentPlayer.getPath().get(currentPlayer.getPath().size() - 1), map.getDestination());
		Player bestPlayer = new Player(currentPlayer);
//		System.out.println("vist size:" + visitList.size());
		for (int i = 0; i < visitList.size(); i++) {
			if (!(visitList.get(i) instanceof Pokemon
					&& currentPlayer.getBallInBag() < ((Pokemon) visitList.get(i)).getNumOfRequiredBalls())) {

				Player newPlayer = new Player(currentPlayer);
				player = newPlayer;

				Cell current = newPlayer.getPath().get(currentPlayer.getPath().size() - 1);
				Cell next = visitList.get(i);
				int benefit = getBenefit(current, next);
				newPlayer.addPath(visitList.get(i));
				ArrayList<Cell> newList = new ArrayList<Cell>(visitList);
				newList.remove(visitList.get(i));

				Pair newPair = findPath(newList, newPlayer, k+1);
				newPair.setScore(benefit + newPair.getScore());
				int newScore = Math.max(score, newPair.getScore());
				if (newScore > score) {
					score = newScore;
					bestPlayer = new Player(newPair.getPlayer());
				}
			}
		}

		Pair bestPair = new Pair(score, bestPlayer);
//		System.out.println("Path's size " + bestPair.getPlayer().getPath().size());
//		System.out.println("Best score: " + score);
		return bestPair;
	}

	public void printPath(Pair pair) {
		System.out.println(player.getBallInBag() + ":" + player.getPokemonCaught().size() + ":" + player.getCaughtTypes().size()
		+ ":" + player.getMaxCP());
		System.out.print("<" + map.getStart().getRow() + "," + map.getStart().getCol() + ">");
		for (int i = 0; i < player.getPath().size() - 1; i++) {
			map.printPath(player.getPath().get(i), player.getPath().get(i+1));
		}
	}

	private void writeSolutionToFile(File outputFile, Pair pair) throws Exception {
		BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));
		bw.write(String.valueOf(pair.getScore()));
		bw.newLine();
		bw.write(player.getBallInBag() + ":" + player.getPokemonCaught().size() + ":" + player.getCaughtTypes().size()
				+ ":" + player.getMaxCP());
		bw.newLine();
		bw.write("<" + map.getStart().getRow() + "," + map.getStart().getCol() + ">");
		for (int i = 0; i < player.getPath().size() - 1; i++) {
			bw.write("->" + map.writePath(player.getPath().get(i), player.getPath().get(i+1)));

		}
		bw.close();
	}

	public static void main(String[] args) throws Exception{
		File inputFile = new File("./sampleIn.txt");
		File outputFile = new File("./sampleOutput.txt");

		if (args.length > 0) {
			inputFile = new File(args[0]);
		}

		if (args.length > 1) {
			outputFile = new File(args[1]);
		}
		long startTime, stopTime;

		startTime = System.nanoTime();

		Game game = new Game();
		game.initialize(inputFile);
		Pair bestPair = game.findPath(map.getVisitList(), player, 0);
		player = bestPair.getPlayer();
		player.addPath(map.getDestination());
		game.printPath(bestPair);
		game.writeSolutionToFile(outputFile, bestPair);

		stopTime = System.nanoTime();
		System.out.println();
		System.out.println("FindPath Time: " + (stopTime - startTime) / 1000000000.0 + " second(s)");

		// TODO:
		// Read the configures of the map and pokemons from the file inputFile
		// and output the results to the file outputFile
	}
}

class Pair {
	private int score;
	private Player player;

	Pair(int score, Player player) {
		this.score = score;
		this.player = new Player(player);
	}

	public Player getPlayer() {
		return player;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
}
