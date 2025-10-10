package audio;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundManager {

    private static Clip tingClip;

    // Khởi tạo âm thanh — gọi một lần ở đầu game (ví dụ trong Level hoặc Main)
    public static void init() {
        try {
            File soundFile = new File("res/ting.wav"); // đường dẫn đến file âm thanh
            if (!soundFile.exists()) {
                System.err.println("⚠️ Không tìm thấy file âm thanh: " + soundFile.getAbsolutePath());
                return;
            }

            // Đọc file wav
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);

            // Tạo Clip (một kiểu âm thanh ngắn, có thể phát lại nhiều lần)
            tingClip = AudioSystem.getClip();
            tingClip.open(audioIn);

            System.out.println("✅ Đã tải âm thanh ting.wav thành công!");
        } catch (UnsupportedAudioFileException e) {
            System.err.println("⚠️ Định dạng file âm thanh không được hỗ trợ!");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("⚠️ Lỗi đọc file âm thanh!");
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            System.err.println("⚠️ Thiết bị âm thanh không khả dụng!");
            e.printStackTrace();
        }
    }

    // Phát âm thanh "ting" khi ghi điểm
    public static void playTing() {
        if (tingClip == null) return;

        // Dừng clip nếu đang chạy để đảm bảo có thể phát lại nhanh
        if (tingClip.isRunning()) {
            tingClip.stop();
        }

        // Quay lại từ đầu và phát lại
        tingClip.setFramePosition(0);
        tingClip.start();
    }

    // Dọn tài nguyên khi thoát game
    public static void cleanup() {
        if (tingClip != null) {
            tingClip.close();
        }
    }
}
