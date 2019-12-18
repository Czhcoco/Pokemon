import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

/**
 * Handles audio related events.
 */
public class AudioManager {

    private static final AudioManager INSTANCE = new AudioManager();
    /**
     * Set of all currently playing sounds.
     *
     * <p>
     * Use this to keep a reference to the sound until it finishes playing.
     * </p>
     */
    private final Set<MediaPlayer> soundPool = new HashSet<>();

    /**
     * Enumeration of known sound resources.
     */
    public enum SoundRes {
        STATION, POKEMON, MAP, GAMEOVER;

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }
    }

    /**
     * Default Constructor
     */
    private AudioManager() {
    }

    /**
     * Get instance of audio manager class
     *
     * @return instance of this class
     */
    public static AudioManager getInstance() {
        return INSTANCE;
    }

    /**
     * Plays the sound. If disabled, simply return.
     *
     * <p>
     * Hint:
     * <ul>
     * <li>Use {@link MediaPlayer#play()} and {@link MediaPlayer#dispose()}.</li>
     * <li>When creating a new MediaPlayer object, add it into {@link AudioManager#soundPool} before playing it.</li>
     * <li>Set a callback for when the sound has completed playing. This is to remove it from the soundpool, and
     * dispose the player using a daemon thread.</li>
     * </ul>
     *
     * @param name the name of the sound file to be played, excluding .mp3
     */
    private void playFile(final String name) {
        Path RES_PATH = Paths.get("/media");
        Path path = Paths.get((RES_PATH).resolve(name+".mp3").toString());
        if (path.toFile()==null){
            System.out.println("Coco is not bad");
        }
        String s = getClass().getResource(RES_PATH.resolve(name+".mp3").toString()).toExternalForm();

        MediaPlayer media = new MediaPlayer(new Media(s));

        soundPool.add(media);
        media.play();

        media.setOnEndOfMedia(() -> {
                soundPool.remove(media);
                media.dispose();
        });
    }

    /**
     * Stop all the sounds currently played
     */
    void stopSound() {
        soundPool.clear();
    }

    /**
     * Plays a sound.
     *
     * @param name Enumeration of the sound, given by {@link SoundRes}.
     */
    void playSound(final SoundRes name) {
        playFile(name.toString());
    }
}