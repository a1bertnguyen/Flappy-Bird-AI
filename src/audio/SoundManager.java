//package audio;
//
//import javax.sound.sampled.*;
//import java.io.File;
//import java.io.IOException;
//
//public class SoundManager {
//
//    private static Clip tingClip;
//
//    // Khởi tạo âm thanh — gọi một lần ở đầu game (ví dụ trong Level hoặc Main)
//    public static void init() {
//        try {
//            File soundFile = new File("res/ting.wav"); // đường dẫn đến file âm thanh
//            if (!soundFile.exists()) {
//                System.err.println("⚠️ Không tìm thấy file âm thanh: " + soundFile.getAbsolutePath());
//                return;
//            }
//
//            // Đọc file wav
//            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
//
//            // Tạo Clip (một kiểu âm thanh ngắn, có thể phát lại nhiều lần)
//            tingClip = AudioSystem.getClip();
//            tingClip.open(audioIn);
//
//            System.out.println("✅ Đã tải âm thanh ting.wav thành công!");
//        } catch (UnsupportedAudioFileException e) {
//            System.err.println("⚠️ Định dạng file âm thanh không được hỗ trợ!");
//            e.printStackTrace();
//        } catch (IOException e) {
//            System.err.println("⚠️ Lỗi đọc file âm thanh!");
//            e.printStackTrace();
//        } catch (LineUnavailableException e) {
//            System.err.println("⚠️ Thiết bị âm thanh không khả dụng!");
//            e.printStackTrace();
//        }
//    }
//
//    // Phát âm thanh "ting" khi ghi điểm
//    public static void playTing() {
//        if (tingClip == null) return;
//
//        // Dừng clip nếu đang chạy để đảm bảo có thể phát lại nhanh
//        if (tingClip.isRunning()) {
//            tingClip.stop();
//        }
//
//        // Quay lại từ đầu và phát lại
//        tingClip.setFramePosition(0);
//        tingClip.start();
//    }
//
//    // Dọn tài nguyên khi thoát game
//    public static void cleanup() {
//        if (tingClip != null) {
//            tingClip.close();
//        }
//    }
//}


package audio;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class SoundManager {

    private static Clip flapClip;
    private static Clip backgroundClip;
    private static Clip gameOverClip;
    private static Clip tingClip;

    // Khởi tạo tất cả âm thanh — gọi một lần ở đầu game
    public static void init() {
        flapClip = loadClip("res/flap.wav");
        backgroundClip = loadClip("res/background.wav");
        gameOverClip = loadClip("res/gameover.wav");
        tingClip = loadClip("res/ting.wav");

        System.out.println("✅ Tất cả âm thanh đã được tải!");
    }

    // Hàm tiện ích để tải clip
    private static Clip loadClip(String path) {
        try {
            File soundFile = new File(path);
            if (!soundFile.exists()) {
                System.err.println("⚠️ Không tìm thấy file: " + soundFile.getAbsolutePath());
                return null;
            }

            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            return clip;
        } catch (Exception e) {
            System.err.println("⚠️ Lỗi tải âm thanh: " + path);
            e.printStackTrace();
            return null;
        }
    }

    // ====== CÁC HÀM PHÁT ÂM THANH ======

    public static void playFlap() {
        playOnce(flapClip);
    }

    public static void playTing() {
        playOnce(tingClip);
    }

    public static void playGameOver() {
        playOnce(gameOverClip);
    }

    public static void playBackground() {
        if (backgroundClip == null) return;
        backgroundClip.loop(Clip.LOOP_CONTINUOUSLY); // phát lặp vô hạn
        backgroundClip.start();
    }

    public static void stopBackground() {
        if (backgroundClip != null && backgroundClip.isRunning()) {
            backgroundClip.stop();
        }
    }

    // Hàm dùng chung để phát 1 lần
//    private static void playOnce(Clip clip) {
//        if (clip == null) return;
//
//        if (clip.isRunning()) clip.stop();
//        clip.setFramePosition(0);
//        clip.start();
//    }
    
    private static void playOnce(Clip clip) {
        if (clip == null) return;

        try {
            if (clip.isRunning()) clip.stop();
            clip.flush();               // 🧹 xóa buffer cũ
            clip.setFramePosition(0);   // tua về đầu
            clip.start();               // phát ngay
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Giải phóng tài nguyên khi thoát game
    public static void cleanup() {
        if (flapClip != null) flapClip.close();
        if (tingClip != null) tingClip.close();
        if (gameOverClip != null) gameOverClip.close();
        if (backgroundClip != null) backgroundClip.close();
    }
}

