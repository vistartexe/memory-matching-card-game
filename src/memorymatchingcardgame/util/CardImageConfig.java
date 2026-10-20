package memorymatchingcardgame.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * Configuration and loader card image PNG from project folder.
 */
public class CardImageConfig {

    public static final String CARDS_DIR = "assets" + File.separator + "cards";
    public static final String FRONT_DIR = CARDS_DIR + File.separator + "front";
    public static final String BACK_DIR = CARDS_DIR + File.separator + "back";
    public static final String BACK_FILE = BACK_DIR + File.separator + "card_back.png";

    private static CardImageConfig instance;

    private final List<String> frontCardIds = new ArrayList<>();
    private ImageIcon originalBackIcon;
    private boolean useImages;

    private CardImageConfig() {
        loadImages();
    }

    // Get a singleton instance configuration of card image
    public static synchronized CardImageConfig getInstance() {
        if (instance == null) {
            instance = new CardImageConfig();
        }
        return instance;
    }

    // Reload image from the folder (e.g. after the user changes PNG file)
    public synchronized void reload() {
        frontCardIds.clear();
        originalBackIcon = null;
        useImages = false;
        loadImages();
    }

    // Load all of the PNG file from front and back folder
    private void loadImages() {
        ensureFoldersExist();

        File frontFolder = new File(FRONT_DIR);
        File[] frontFiles = frontFolder.listFiles((dir, name) ->
                name.toLowerCase().endsWith(".png"));

        if (frontFiles != null && frontFiles.length > 0) {
            Arrays.sort(frontFiles, Comparator.comparing(File::getName));
            for (File file : frontFiles) {
                String id = file.getName().replace(".png", "").replace(".PNG", "");
                frontCardIds.add(id);
            }
        }

        File backFile = new File(BACK_FILE);
        if (backFile.exists()) {
            originalBackIcon = new ImageIcon(backFile.getAbsolutePath());
        }

        useImages = !frontCardIds.isEmpty() && originalBackIcon != null;
    }

    // Make an assets/cards folder if not already
    private void ensureFoldersExist() {
        new File(FRONT_DIR).mkdirs();
        new File(BACK_DIR).mkdirs();
    }

    // Check whether the application uses PNG image or fallback emoji
    public boolean isUsingImages() {
        return useImages;
    }

    // Take list of card ID for the amount of pair needed
    public List<String> getCardIdsForPairs(int pairCount) {
        List<String> ids = new ArrayList<>();
        if (useImages) {
            for (int i = 0; i < pairCount; i++) {
                ids.add(frontCardIds.get(i % frontCardIds.size()));
            }
        } else {
            String[] symbols = {
                "A", "B", "C", "D", "E", "F", "G", "H",
                "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R"
            };
            for (int i = 0; i < pairCount; i++) {
                ids.add(symbols[i % symbols.length]);
            }
        }
        return ids;
    }

    // Get front icon of the card with particular size
    public ImageIcon getFrontIcon(String cardId, int width, int height) {
        if (!useImages) {
            return null;
        }
        File file = new File(FRONT_DIR, cardId + ".png");
        if (!file.exists()) {
            return null;
        }
        ImageIcon raw = new ImageIcon(file.getAbsolutePath());
        return scaleIcon(raw, width, height);
    }

    // Get back icon of the card with particular size
    public ImageIcon getBackIcon(int width, int height) {
        if (!useImages || originalBackIcon == null) {
            return null;
        }
        return scaleIcon(originalBackIcon, width, height);
    }

    // Scaling the icon so that it'll fit with the size of the card button
    public ImageIcon scaleIcon(ImageIcon source, int width, int height) {
        if (source == null || width <= 0 || height <= 0) {
            return source;
        }
        Image scaled = source.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    // Make an image sample as default if the folder was still empty
    public static void createSampleImagesIfMissing() {
        File frontFolder = new File(FRONT_DIR);
        File backFile = new File(BACK_FILE);
        frontFolder.mkdirs();
        new File(BACK_DIR).mkdirs();

        boolean hasFront = frontFolder.listFiles((d, n) -> n.toLowerCase().endsWith(".png")) != null
                && frontFolder.listFiles((d, n) -> n.toLowerCase().endsWith(".png")).length > 0;

        if (!hasFront) {
            Color[] colors = {
                new Color(231, 76, 60), new Color(46, 204, 113), new Color(52, 152, 219),
                new Color(155, 89, 182), new Color(241, 196, 15), new Color(230, 126, 34),
                new Color(26, 188, 156), new Color(149, 165, 166), new Color(192, 57, 43),
                new Color(41, 128, 185), new Color(142, 68, 173), new Color(39, 174, 96),
                new Color(211, 84, 0), new Color(22, 160, 133), new Color(127, 140, 141),
                new Color(243, 156, 18), new Color(52, 73, 94), new Color(231, 76, 60)
            };
            for (int i = 0; i < colors.length; i++) {
                writeCardPng(new File(frontFolder, String.format("card_%02d.png", i + 1)),
                        colors[i], String.valueOf(i + 1));
            }
        }

        if (!backFile.exists()) {
            writeCardPng(backFile, new Color(44, 62, 80), "?");
        }
    }

    // Write one PNG file of the card with color and label
    private static void writeCardPng(File target, Color bg, String label) {
        int size = 128;
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(bg);
        g.fillRoundRect(0, 0, size, size, 20, 20);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 48));
        int textWidth = g.getFontMetrics().stringWidth(label);
        g.drawString(label, (size - textWidth) / 2, (size + 40) / 2);
        g.dispose();
        try {
            ImageIO.write(image, "png", target);
        } catch (IOException e) {
            System.err.println("The making of the sample image was failed: " + e.getMessage());
        }
    }
}
