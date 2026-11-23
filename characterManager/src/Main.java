import com.unpri.charactermanager.CharacterManager;
import com.unpri.charactermanager.model.Character;
import com.unpri.charactermanager.model.CharacterType;
import com.unpri.charactermanager.view.MainWindow;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        CharacterManager manager = new CharacterManager();

        // 3 Karakter awal dari sprint sebelumnya
        manager.addCharacter(new Character("Arthur", CharacterType.WARRIOR, 5, 200, 40, 50));
        manager.addCharacter(new Character("Gandalf", CharacterType.MAGE, 10, 300, 80, 200));
        manager.addCharacter(new Character("Legolas", CharacterType.ARCHER, 3, 160, 30, 80));

        // 3 Karakter tambahan Sprint #3 & #4
        manager.addCharacter(new Character("Casanova", CharacterType.MAGE, 7, 240, 25, 150));
        manager.addCharacter(new Character("Caraxes", CharacterType.HEALER, 12, 340, 95, 250));
        manager.addCharacter(new Character("Zerstear", CharacterType.WARRIOR, 15, 400, 110, 40));

        // 3 Karakter baru untuk TP12 (Total 9 karakter dengan variasi role & level)
        manager.addCharacter(new Character("Artemis", CharacterType.ARCHER, 6, 220, 48, 90));
        manager.addCharacter(new Character("Lyra", CharacterType.HEALER, 2, 140, 18, 120));
        manager.addCharacter(new Character("Gatotkaca", CharacterType.WARRIOR, 9, 320, 75, 60));

        SwingUtilities.invokeLater(() -> {
            MainWindow window = new MainWindow(manager);
            window.setVisible(true);
        });
    }
}