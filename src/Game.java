import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
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

		Map map = new Map(M,N);

		for (int i = 0; i < M; i++) {
			line = br.readLine();
			for(int j = 0; j < N; j++){
				map.setPath(i, j, line.charAt(j));
			}
		}

		line = br.readLine();
		while (line != null) {
			String content[] = br.readLine().split(",");
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

		player.setRow(map.getStart().getRow());
		player.setCol(map.getStart().getCol());
		map.getVisitList().add(0, map.getStart());
		map.getVisitList().add(map.getDestination());
		map.setWholeList();
		map.findShortestPath();
		map.getVisitList().remove(map.getDestination());
		player.addPath(map.getStart());
	}

	public int getScore(Cell cell) {
		if (cell instanceof Station) {
			return ((Station) cell).getNumOfBalls();
		} else if (cell instanceof Pokemon) {
			return 5 + player.newType(((Pokemon) cell)) * 10 + player.cpChange(cell);
		}
		return 0;
	}

	public int getBenefit(Cell current, Cell next) {
		return getScore(next) - map.getDistance(current, next) - map.getDistance(next, map.getDestination());
	}

	public int findPath(int[][] distance, ArrayList<Cell> visitList, ArrayList<Cell> path) {
		int score = getBenefit(map.getStart(), map.getDestination());
		for (int i = 0; i < visitList.size(); i++) {
			path.add(visitList.get(i));
			Cell current = path.get(path.size() - 2);
			Cell next = path.get(path.size() - 1);
			ArrayList<Cell> newList = new ArrayList<Cell>(visitList);
			newList.remove(visitList.get(i));
			int newScore = Math.max(score, getBenefit(current, next) + findPath(distance, newList, path));
			if (newScore == score) {
				path.remove(next);
			}
		}
		return score;
	}

	public void printPath() {

	}

	public static void main(String[] args) throws Exception{
		File inputFile = new File("./sampleInput.txt");
		File outputFile = new File("./sampleOut.txt");

		if (args.length > 0) {
			inputFile = new File(args[0]);
		}

		if (args.length > 1) {
			outputFile = new File(args[1]);
		}

		Game game = new Game();
		game.initialize(inputFile);
		int score = game.findPath(map.getDistance(), map.getVisitList(), player.getPath());

		System.out.println(score);
		
		// TODO:
		// Read the configures of the map and pokemons from the file inputFile
		// and output the results to the file outputFile
	}
}
