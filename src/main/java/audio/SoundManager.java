package audio;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.DataInput;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.*;

/** Phát nhạc nền và hiệu ứng.
 *  - BMG: Lặp 1 Clip.
 *  - SFX: Phát độc lập, tái sử dụng để buffer đỡ trễ.
 */

public class SoundManager {
    private static final SoundManager Instance = new SoundManager();

    // Volume mặc định (dB): 0.0 dB là nguyên bản, còn âm dB là nhỏ hơn.
    private volatile float bgmGainDb = -6.0f;
    private volatile float sfxGainDb = -3.0f;

    private Clip currentBgm;
    private SoundId currentBgmId;

    // Cache audio data, tránh mở sẵn Clip cho SFX (Chiếm line).
    private final Map<SoundId, byte[]> audioCache = new ConcurrentHashMap<>();
    private final Map<SoundId, AudioFormat> formatCache = new ConcurrentHashMap<>();

    // Để việc phát âm thanh cho luồng phụ, để luồng chính (EDT) xử lí game mượt, tránh bị đơ.
    private final ExecutorService sfxPool = Executors.newCachedThreadPool(r -> {
        Thread t = new Thread(r, "SFX-Player");
        t.setDaemon(true);
        return t;
    });


    private SoundManager() {}

    public static SoundManager get(){
        return Instance;
    }

    /*
    -----------------API------------------
     */

    // Phát BGM mới. Nếu đang có thì fade (crossfade) sang bài mới.
    public synchronized void playBgm(SoundId id, int fadeMs) {
        if(!id.isBgm()) throw new IllegalArgumentException("Not a BGM id: " + id);
        if(id == currentBgmId) return;

        Clip next = createClip(id);
        if(next == null) return;

        setGain(next, bgmGainDb);
        next.loop(Clip.LOOP_CONTINUOUSLY);

        if(currentBgm != null && currentBgm.isOpen()) {
            // Crossfade (Nhạc menu giảm dần + nhạc game tăng dần)
            Clip old = currentBgm;
            currentBgm = next;
            currentBgmId = id;
            currentBgm.start();
            crossFade(old, currentBgm, fadeMs);
        }else {
            currentBgm = next;
            currentBgmId = id;
            currentBgm.start();
            fadeIn(currentBgm, fadeMs);
        }
    }

    // Dừng BGM hiện tại.
    public synchronized void stopBgm(int fadeMs) {
        if(currentBgm != null && currentBgm.isOpen()) {
            Clip old = currentBgm;
            currentBgm = null;
            currentBgmId = null;
            fadeOutThenClose(old, fadeMs);
        }
    }

    // Phát 1 effect.
    public void playSfx(SoundId id) {
        if (id.isBgm()) throw new IllegalArgumentException("Use playBgm for BGM");
        sfxPool.execute(() -> {
            Clip c = createClip(id);
            if (c == null) return;
            setGain(c, sfxGainDb);
            c.start();
            // Đợi xong thì đóng để giải phóng line
            c.addLineListener(ev -> {
                if (ev.getType() == LineEvent.Type.STOP) {
                    c.close();
                }
            });
        });
    }

    // Điều chỉnh volume (dB).
    public synchronized void setBgmGainDb(float gainDb) {
        this.bgmGainDb = gainDb;
        if (currentBgm != null) setGain(currentBgm, gainDb);
    }
    public void setSfxGainDb(float gainDb) { this.sfxGainDb = gainDb; }

    // Tắt/mở volume nhanh.
    public void muteAll(boolean mute) {
        setBgmGainDb(mute ? -80f : -6f);
        setSfxGainDb(mute ? -80f : -3f);
    }

    // Giải phóng.
    public synchronized void shutdown() {
        stopBgm(0);
        sfxPool.shutdownNow();
        audioCache.clear();
        formatCache.clear();
    }

    /*
    --------------------------------------------
     */

    private Clip createClip(SoundId id) {
        try{
            AudioFormat fmt = formatCache.computeIfAbsent(id, this::loadFormat);
            byte[] data = audioCache.computeIfAbsent(id, this::loadBytes);

            if(fmt == null || data == null) return null;

            DataLine.Info info = new DataLine.Info(Clip.class, fmt);
            Clip clip = (Clip) AudioSystem.getLine(info);
            clip.open(fmt, data, 0, data.length);
            return clip;
        } catch (LineUnavailableException e) {
            System.err.println("Audio line unavailable for " + id + ": " + e.getMessage());
            return null;
        }
    }

    private AudioFormat loadFormat(SoundId id) {
        try (AudioInputStream ais = openStream(id.path())) {
            AudioFormat base = ais.getFormat();
            // Chuyển về PCM_SIGNED nếu cần.
            AudioFormat pcm = toPcmSigned(base);
            return pcm;
        } catch (Exception e) {
            System.err.println("Cannot read format " + id + ": " + e.getMessage());
            return null;
        }
    }

    private byte[] loadBytes(SoundId id) {
        try (AudioInputStream ais0 = openStream(id.path());
             AudioInputStream ais = convertToPcmIfNeeded(ais0)) {
            return ais.readAllBytes();
        } catch (Exception e) {
            System.err.println("Cannot read audio bytes " + id + ": " + e.getMessage());
            return null;
        }
    }

    private AudioInputStream openStream(String path) throws IOException, UnsupportedAudioFileException {
        InputStream in = SoundManager.class.getClassLoader().getResourceAsStream(path);
        if (in == null) throw new IOException("Resource not found: " + path);
        return AudioSystem.getAudioInputStream(new BufferedInputStream(in));
    }

    private AudioInputStream convertToPcmIfNeeded(AudioInputStream ais) {
        AudioFormat base = ais.getFormat();
        if (base.getEncoding() == AudioFormat.Encoding.PCM_SIGNED) return ais;
        AudioFormat target = toPcmSigned(base);
        return AudioSystem.getAudioInputStream(target, ais);
    }

    private AudioFormat toPcmSigned(AudioFormat src) {
        return new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,
                src.getSampleRate(),
                16,
                src.getChannels(),
                src.getChannels() * 2,
                src.getSampleRate(),
                false
        );
    }

    private void setGain(Clip clip, float gainDb) {
        try {
            FloatControl gain = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gain.setValue(clamp(gainDb, gain.getMinimum(), gain.getMaximum()));
        } catch (IllegalArgumentException ignored) { }
    }

    private float clamp(float v, float min, float max) {
        return Math.max(min, Math.min(max, v));
    }

    private void fadeIn(Clip clip, int ms) {
        if (ms <= 0) return;
        try {
            FloatControl gain = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float target = bgmGainDb;
            float min = Math.max(gain.getMinimum(), -80f);
            gain.setValue(min);
            clip.start();
            tweenGain(gain, min, target, ms);
        } catch (IllegalArgumentException ignored) { }
    }

    private void fadeOutThenClose(Clip clip, int ms) {
        if (clip == null) return;
        new Thread(() -> {
            try {
                FloatControl gain = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                float start = gain.getValue();
                float end = Math.max(gain.getMinimum(), -80f);
                tweenGain(gain, start, end, ms);
            } catch (IllegalArgumentException ignored) { }
            clip.stop();
            clip.close();
        }, "BGM-FadeOut").start();
    }

    private void crossFade(Clip oldClip, Clip newClip, int ms) {
        new Thread(() -> {
            FloatControl gOld = null, gNew = null;
            try {
                gOld = (FloatControl) oldClip.getControl(FloatControl.Type.MASTER_GAIN);
                gNew = (FloatControl) newClip.getControl(FloatControl.Type.MASTER_GAIN);
            } catch (IllegalArgumentException ignored) { }
            long steps = Math.max(1, ms / 16);
            for (int i = 0; i <= steps; i++) {
                float t = i / (float) steps;
                if (gOld != null) gOld.setValue(lerp(bgmGainDb, -80f, t));
                if (gNew != null) gNew.setValue(lerp(-80f, bgmGainDb, t));
                sleep(16);
            }
            oldClip.stop();
            oldClip.close();
        }, "BGM-CrossFade").start();
    }

    private void tweenGain(FloatControl gain, float from, float to, int ms) {
        long steps = Math.max(1, ms / 16);
        for (int i = 0; i <= steps; i++) {
            float t = i / (float) steps;
            gain.setValue(lerp(from, to, t));
            sleep(16);
        }
    }

    private float lerp(float a, float b, float t) { return a + (b - a) * t; }

    private void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {

        }
    }

    public synchronized void fadeOutBgm(int durationMs) {
        if (currentBgm != null && currentBgm.isOpen()) {
            Clip old = currentBgm;
            currentBgm = null;
            currentBgmId = null;
            fadeOutThenClose(old, durationMs);
        }
    }

}

