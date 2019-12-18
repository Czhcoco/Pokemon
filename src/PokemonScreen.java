import java.io.File;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class PokemonScreen extends Application {

    private Player player;

    private static final int STEP_SIZE = 40;
    private ImageView avatar;

    private static final String avatarFront = new File("icons/front.png").toURI().toString();
    private static final String avatarBack = new File("icons/back.png").toURI().toString();
    private static final String avatarLeft = new File("icons/left.png").toURI().toString();
    private static final String avatarRight = new File("icons/right.png").toURI().toString();

    // other game assets
    private static final String tree = new File("icons/wall6.png").toURI().toString();
    private static final String exit = new File("icons/end2.png").toURI().toString();
    private static final String ball = new File("icons/ball.png").toURI().toString();
    private static final String start = new File("icons/start.png").toURI().toString();

    // gif animations
    private static final String caught = new File("icons/caught.gif").toURI().toString();

    private static final Label labelScore = new Label();
    private static final Label labelCaught = new Label();
    private static final Label labelBalls = new Label();

    private static SimpleIntegerProperty score = new SimpleIntegerProperty(0);
    private static SimpleIntegerProperty numCaught = new SimpleIntegerProperty(0);
    private static SimpleIntegerProperty numBalls = new SimpleIntegerProperty(0);

    private static Group mapGroup;
    private static Label labelStatus;

    private static AnimationTimer timer;

    private static ArrayList<Image> catchSuccessfulFrames;
    private ArrayList<Cell> wholePath;
    private ArrayList<Cell> visitList;

    {
        avatar = new ImageView(new Image(avatarFront));
        avatar.setFitWidth(STEP_SIZE);
        avatar.setFitHeight(STEP_SIZE);
        avatar.setPreserveRatio(true);

        catchSuccessfulFrames = new ArrayList<>();
        for (int i = 0; i <= 233; i++)
            catchSuccessfulFrames.add(new Image(new File("icons/catch/frame_" + i + "_delay-0.05s.gif").toURI().toString()));

        wholePath = new ArrayList<>();
        visitList = new ArrayList<>();
    }

    @Override
    public void start(final Stage stage) {
        BorderPane mainPane = new BorderPane();
        mainPane.setPadding(new Insets(10, 10, 10, 10));

        mapGroup = new Group();
        renderMap(mapGroup);

        mainPane.setCenter(mapGroup);

        // right-sided menu
        VBox bottom = new VBox(10);

        labelScore.textProperty().bind(score.asString());
        HBox scoreBox = new HBox(new Label("Current Score: "), labelScore);

        labelCaught.textProperty().bind(numCaught.asString());
        HBox pokeBox = new HBox(new Label("# of Pokemon Caught: "), labelCaught);

        labelBalls.textProperty().bind(numBalls.asString());
        HBox ballBox = new HBox(new Label("# of Pokemon Balls: "),labelBalls);

        labelStatus = new Label("");

        bottom.getChildren().addAll(scoreBox, pokeBox, ballBox, labelStatus);
        scoreBox.setAlignment(Pos.CENTER);
        pokeBox.setAlignment(Pos.CENTER);
        ballBox.setAlignment(Pos.CENTER);
        labelStatus.setAlignment(Pos.CENTER);
        bottom.setPadding(new Insets(40));

        mainPane.setBottom(bottom);
        bottom.setAlignment(Pos.CENTER);

        Scene scene = new Scene(mainPane);

        AudioManager audio = AudioManager.getInstance();
        audio.playSound(AudioManager.SoundRes.MAP);


        stage.setScene(scene);
        stage.show();

        timer = new AnimationTimer() {

            private long lastUpdate = LocalTime.now().getNano();

            @Override
            public void handle(long now) {
                if (now - lastUpdate >= 800_000_000) {
                    Platform.runLater(() -> {
                        Cell next = wholePath.remove(0);
                        Type d = getDirection(new Cell(player.getRow(), player.getCol()), next);
                        avatar.setImage(getImage(d, 0));
                        avatar.relocate(avatar.getLayoutX() + STEP_SIZE * (next.getCol() - player.getCol()),
                                avatar.getLayoutY() + STEP_SIZE * (next.getRow() - player.getRow()));
                        player.setPosition(next);
                        System.out.println("move");
                        score.set(score.get() - 1);
                        labelStatus.setText("");

                        //Player encounter station, pokemon or destination
                        if (player.equals(visitList.get(0))) {
                            System.out.println("visit");
                            Cell cell = visitList.remove(0);
                            if (visitList.size() == 0) {
                                timer.stop();
                                audio.stopSound();
                                audio.playSound(AudioManager.SoundRes.GAMEOVER);
                                labelStatus.setText("End Game");
                                labelStatus.setTextFill(Color.GREEN);
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Game Over");
                                alert.setHeaderText(null);
                                alert.setContentText("Your score is " + score.get());
                                alert.showAndWait();
                                Platform.exit();
                            }
                            score.set(score.get() + player.getScore(cell));
                            player.addPath(cell);
                            ImageView imgV = (ImageView) mapGroup.lookup("#" + cell.getRow() + "," + cell.getCol());
                            imgV.setVisible(false);
                            if (cell instanceof Pokemon) {
                                showCatchAnimation();
                                labelStatus.setText("Catch A Pokemon!");
                                labelStatus.setTextFill(Color.GREEN);
                                audio.playSound(AudioManager.SoundRes.POKEMON);
                            } else if (cell instanceof Station) {
                                labelStatus.setText("Gain " + ((Station) cell).getNumOfBalls() + " balls!");
                                labelStatus.setTextFill(Color.GREEN);
                                audio.playSound(AudioManager.SoundRes.STATION);
                            }
                            numBalls.set(player.getBallInBag());
                            numCaught.set(player.getPokemonCaught().size());
                        }
                    });
                    lastUpdate = now ;
                }
            }
        };

        timer.start();

    }

    /**
     * Render map according to given input file
     *
     * @param group group containing imageViews of each cell
     */
    private void renderMap(Group group) {
        System.out.println("Rendering map");
        Game game = new Game();

        File inputFile = new File("./sampleInput.txt");

        try {
            game.initialize(inputFile);
        } catch (Exception e) {
            System.out.println("ERROR: Cannot build map");
            e.printStackTrace();
        }

        Map map = game.getMap();
        player = game.getPlayer();
        Pair bestPair = game.findPath(map.getVisitList(), player);
        visitList.addAll(bestPair.getPlayer().getPath());
        visitList.add(map.getDestination());

        wholePath.add(map.getStart());
        for (int i = 0; i < visitList.size() - 1; i++) {
            wholePath.addAll(map.returnPath(visitList.get(i), visitList.get(i+1)));
        }

        visitList.remove(0);

        player.setPosition(wholePath.remove(0));

        //Get the image of each cell
        int numOfPoke = 0;
        for(int i = 0; i < map.getRow(); i++) {
            for(int j = 0; j < map.getCol(); j++) {
                ImageView img = new ImageView(getImage(map.getCellType(i, j), numOfPoke));
                img.setId(i + "," + j);

                if (map.getCellType(i, j) == Type.POKEMON) {
                    numOfPoke++;
                    int height = (int) Math.floor(img.getImage().getHeight() / Math.max(img.getImage().getHeight(), img.getImage().getWidth()) * 30);
                    int width = (int) Math.floor(img.getImage().getWidth() / Math.max(img.getImage().getHeight(), img.getImage().getWidth()) * 30);
                    img.setFitHeight(height);
                    img.setFitWidth(width);
                    img.relocate(j * STEP_SIZE + 8, i * STEP_SIZE + 8);
                } else if (map.getCellType(i, j) == Type.STATION) {
                    img.setFitHeight(STEP_SIZE - 10);
                    img.setFitWidth(STEP_SIZE - 10);
                    img.relocate(j * STEP_SIZE + 5, i * STEP_SIZE + 5);
                } else {
                    if (map.getCellType(i, j) == Type.START) {
                        avatar = new ImageView(getImage(Type.DOWN, 0));
                        avatar.setFitHeight(STEP_SIZE);
                        avatar.setFitWidth(STEP_SIZE);
                        avatar.relocate(j * STEP_SIZE, i * STEP_SIZE);
                        avatar.setPreserveRatio(true);
                        avatar.setVisible(true);
                        avatar.setCache(true);
                        group.getChildren().add(avatar);
                    }
                    img.setFitHeight(STEP_SIZE);
                    img.setFitWidth(STEP_SIZE);
                    img.relocate(j * STEP_SIZE, i * STEP_SIZE);
                }
                img.setPreserveRatio(true);
                img.setVisible(true);
                img.setCache(true);
                group.getChildren().add(img);
            }
        }
    }

    /**
     * Show catch animation when player successfully catch the pokemon
     */
    private synchronized static void showCatchAnimation() {
        ArrayList<Image> frames = catchSuccessfulFrames;
        Stage newStage = new Stage();
        newStage.setTitle("My New Stage Title");
        Image img = frames.get(0);
        ImageView imgView = new ImageView();
        imgView.setImage(img);
        imgView.setFitWidth(250);
        imgView.setPreserveRatio(true);
        StackPane pane = new StackPane();
        pane.getChildren().add(imgView);
        newStage.setScene(new Scene(pane));
        newStage.setAlwaysOnTop(true);
        newStage.initStyle(StageStyle.UNDECORATED);
        newStage.show();

        newStage.show();

        Thread catchAnimationThread = new Thread(() -> {

            try {
                timer.stop();
                for (int i = 0; i < frames.size(); i++) {
                    final Image imgI = frames.get(i);
                    Platform.runLater(() -> {
                        imgView.setImage(imgI);
                    });
                    if (i < frames.size() - 1)
                        Thread.sleep(50);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                Platform.runLater(() -> {
                    newStage.close();
                    timer.start();
                });
            }
        });
        catchAnimationThread.setDaemon(true);
        catchAnimationThread.start();
    }

    /**
     * Get the image of cell with given type
     * Randomly generate a image for pokemon, ensuring they are not all the same
     *
     * @param type type of the cell
     * @param n number of pokemon, 0 for other types
     * @return the image of cell with given type
     */
    private Image getImage(Type type, int n) {
        switch (type) {
            case WALL:
                return new Image(tree);
            case START:
                return new Image(start);
            case STATION:
                return new Image(ball);
            case DESTINATION:
                return new Image(exit);
            case POKEMON:
                Random rand = new Random();
                int i = rand.nextInt(7 + n * 31) % 6 + 6;
                String poke = new File("icons/" + i + ".png").toURI().toString();
                return new Image(poke);
            case UP:
                return new Image(avatarBack);
            case LEFT:
                return new Image(avatarLeft);
            case RIGHT:
                return new Image(avatarRight);
            case DOWN:
                return new Image(avatarFront);
            default:
                return null;
        }
    }

    /**
     * Get the direction of player's next move
     *
     * @param a cell a
     * @param b cell b
     * @return the direction of cell a to cell b
     */
    private Type getDirection(Cell a, Cell b) {
        if (a.getRow() == b.getRow() + 1 && a.getCol() == b.getCol())
            return Type.UP;
        else if (a.getRow() == b.getRow() - 1 && a.getCol() == b.getCol())
            return Type.DOWN;
        else if (a.getCol() == b.getCol() + 1)
            return Type.LEFT;
        else
            return Type.RIGHT;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

enum Type {
    STATION, POKEMON, START, DESTINATION, PATH, WALL, UP, RIGHT, DOWN, LEFT
}