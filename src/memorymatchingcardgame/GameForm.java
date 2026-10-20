package memorymatchingcardgame;

// Import layout manager:
import java.awt.GridLayout; // GridLayout -> for sorting the grid/row-column
import java.awt.FlowLayout; // FlowLayout -> for sorting the horizontal component

import javax.swing.JButton; // JButton -> card button component
import javax.swing.BorderFactory; // BorderFactory -> make a border/padding panel

import java.util.ArrayList; // ArrayList -> store the card list
import java.util.Collections; // Collections -> for card shuffle 
import java.util.List; // List -> store the card list
import javax.swing.Timer; // Timer -> setting the game time and card delay

// Import personal-made class
import memorymatchingcardgame.dao.PlayHistoryDAO; // PlayHistoryDAO -> store a session result to database
import memorymatchingcardgame.util.CardImageConfig; // CardImageConfig -> setting the card image
import memorymatchingcardgame.model.SessionManager; // SessionManager -> store the current user login session
import memorymatchingcardgame.model.User; // User -> user data model

public class GameForm extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(GameForm.class.getName());
    private static final String CARD_BACK_TEXT = "❓";
    private static final int PREVIEW_DURATION_MS = 3000;

    private int userId;
    private String username;
    private String difficulty;

    private int rows;
    private int cols;
    private int totalPairs;

    private int score;
    private int elapsedSeconds;
    private int matchedPairs;

    private boolean isBusy;
    private boolean gameFinished;

    private Timer gameTimer;

    private final List<CardItem> cards =
            new ArrayList<>();

    private CardItem firstFlipped;
    private CardItem secondFlipped;

    private final PlayHistoryDAO
            playHistoryDAO =
                    new PlayHistoryDAO();

    private final CardImageConfig
            cardImageConfig =
                    CardImageConfig
                            .getInstance();
    
    public GameForm() {

        this(
                resolveUserId(),
                resolveUsername(),
                "Easy"
        );
    }

    public GameForm(int userId, String username, String difficulty) {

        initComponents();
        
        this.userId = userId;
        this.username = username;
        this.difficulty = difficulty;

        int[] grid =
                resolveGrid(difficulty);

        rows = grid[0];
        cols = grid[1];

        totalPairs =
                (rows * cols) / 2;

        setLocationRelativeTo(null);

        CardImageConfig.createSampleImagesIfMissing();

        cardImageConfig.reload();

        formatPanelInfo();
        formatPanelButtons();

        attachResizeListener();

        initGame();
    }

    private static int resolveUserId() {
        User user =
                SessionManager
                        .getInstance()
                        .getCurrentUser();

        return user != null
                ? user.getId()
                : 0;
    }

    private static String resolveUsername() {
        User user =
                SessionManager
                        .getInstance()
                        .getCurrentUser();

        return user != null
                ? user.getUsername()
                : "Guest";
    }
    
    private int[] resolveGrid(String level) {

        switch (
                level.toLowerCase()
        ) {

            case "medium":
                return new int[]{
                        4, 6
                };

            case "hard":
                return new int[]{
                        6, 6
                };

            default:
                return new int[]{
                        4, 4
                };
        }
    }
    
    private void initGame() {

        score = 0;
        elapsedSeconds = 0;
        matchedPairs = 0;

        isBusy = false;
        gameFinished = false;

        firstFlipped = null;
        secondFlipped = null;

        cards.clear();

        lblUsername.setText(
                username
        );

        lblLevel.setText(
                difficulty
        );

        updateInfoLabels();

        setupLevelBoard();

        showInitialPreview();
    }
    
    private void showInitialPreview() {

        isBusy = true;

        for (
                CardItem card
                : cards
        ) {

            applyCardFront(
                    card
            );
        }

        Timer timer =
                new Timer(
                        PREVIEW_DURATION_MS,
                        e -> {

                            for (
                                    CardItem card
                                    : cards
                            ) {

                                if (
                                        !card.isMatched()
                                ) {

                                    applyCardBack(
                                            card
                                    );
                                }
                            }

                            isBusy =
                                    false;

                            startGameTimer();
                        }
                );

        timer.setRepeats(false);

        timer.start();
    }
    
    private void setupLevelBoard() {

            pnlBoard.removeAll();

            pnlBoard.setLayout(
                    new SquareCardGridLayout(
                            rows,
                            cols,
                            8,
                            8
                    )
            );

            pnlBoard.setBorder(
                    BorderFactory
                            .createEmptyBorder(
                                    10,
                                    10,
                                    10,
                                    10
                            )
            );

            List<String> pairIds =
                    cardImageConfig
                            .getCardIdsForPairs(
                                    totalPairs
                            );

            List<String> deck =
                    new ArrayList<>();

            for (
                    String id
                    : pairIds
            ) {

                deck.add(id);
                deck.add(id);
            }

            Collections.shuffle(deck);

            for (
                    String cardId
                    : deck
            ) {

                JButton btn =
                        createCardButton();

                CardItem card =
                        new CardItem(
                                cardId,
                                btn
                        );

                cards.add(card);

                pnlBoard.add(btn);

                applyCardBack(card);
            }

            pnlBoard.revalidate();
            pnlBoard.repaint();
        }

    private static class SquareCardGridLayout implements java.awt.LayoutManager {

        private final int rows;
        private final int cols;
        private final int hgap;
        private final int vgap;

        SquareCardGridLayout(
                int rows,
                int cols,
                int hgap,
                int vgap
        ) {

            this.rows = rows;
            this.cols = cols;
            this.hgap = hgap;
            this.vgap = vgap;
        }

        @Override
        public void addLayoutComponent(
                String name,
                java.awt.Component comp
        ) {}

        @Override
        public void removeLayoutComponent(
                java.awt.Component comp
        ) {}

        @Override
        public java.awt.Dimension preferredLayoutSize(
                java.awt.Container parent
        ) {

            java.awt.Insets insets =
                    parent.getInsets();

            int cardSize = 140;

            int width =
                    insets.left
                    + insets.right
                    + (cols * cardSize)
                    + ((cols - 1) * hgap);

            int height =
                    insets.top
                    + insets.bottom
                    + (rows * cardSize)
                    + ((rows - 1) * vgap);

            return new java.awt.Dimension(
                    width,
                    height
            );
        }

        @Override
        public java.awt.Dimension minimumLayoutSize(
                java.awt.Container parent
        ) {

            java.awt.Insets insets =
                    parent.getInsets();

            int cardSize = 60;

            int width =
                    insets.left
                    + insets.right
                    + (cols * cardSize)
                    + ((cols - 1) * hgap);

            int height =
                    insets.top
                    + insets.bottom
                    + (rows * cardSize)
                    + ((rows - 1) * vgap);

            return new java.awt.Dimension(
                    width,
                    height
            );
        }

        @Override
        public void layoutContainer(
                java.awt.Container parent
        ) {

            java.awt.Insets insets =
                    parent.getInsets();

            int availableWidth =
                    parent.getWidth()
                    - insets.left
                    - insets.right
                    - ((cols - 1) * hgap);

            int availableHeight =
                    parent.getHeight()
                    - insets.top
                    - insets.bottom
                    - ((rows - 1) * vgap);

            int cardSize =
                    Math.max(
                            1,
                            Math.min(
                                    availableWidth / cols,
                                    availableHeight / rows
                            )
                    );

            int gridWidth =
                    (cols * cardSize)
                    + ((cols - 1) * hgap);

            int gridHeight =
                    (rows * cardSize)
                    + ((rows - 1) * vgap);

            int startX =
                    insets.left
                    + Math.max(
                            0,
                            (
                                    parent.getWidth()
                                    - insets.left
                                    - insets.right
                                    - gridWidth
                            ) / 2
                    );

            int startY =
                    insets.top
                    + Math.max(
                            0,
                            (
                                    parent.getHeight()
                                    - insets.top
                                    - insets.bottom
                                    - gridHeight
                            ) / 2
                    );

            int count =
                    parent.getComponentCount();

            for (
                    int i = 0;
                    i < count;
                    i++
            ) {

                java.awt.Component component =
                        parent.getComponent(i);

                int row = i / cols;
                int col = i % cols;

                component.setBounds(
                        startX
                        + (col * (cardSize + hgap)),

                        startY
                        + (row * (cardSize + vgap)),

                        cardSize,
                        cardSize
                );
            }
        }
    }
    
    private JButton createCardButton() {

        JButton btn =
            new JButton();

        btn.setFocusPainted(false);

        btn.addActionListener(
                e ->
                        onCardClicked(
                                btn
                        )
        );

        btn.addComponentListener(
                new java.awt.event.ComponentAdapter() {

                    @Override
                    public void componentResized(
                            java.awt.event.ComponentEvent e
                    ) {

                        CardItem card =
                                findCardByButton(
                                        btn
                                );

                        if (
                                card != null
                        ) {

                            refreshCardDisplay(
                                    card
                            );
                        }
                    }
                }
        );

        return btn;
    }
    
    private void onCardClicked(JButton button) {

        if (
                isBusy
                || gameFinished
        ) {
            return;
        }

        CardItem card =
                findCardByButton(
                        button
                );

        if (
                card == null
                || card.isMatched()
                || card.isFaceUp()
        ) {
            return;
        }

        applyCardFront(card);

        if (
                firstFlipped
                == null
        ) {

            firstFlipped =
                    card;

            return;
        }

        secondFlipped =
                card;

        checkMatch();
    }
   
    private void checkMatch() {

        if (
                firstFlipped
                        .getCardId()
                        .equals(
                                secondFlipped
                                        .getCardId()
                        )
        ) {

            firstFlipped
                    .setMatched(true);

            secondFlipped
                    .setMatched(true);

            matchedPairs++;

            score += 100;

            updateInfoLabels();

            resetSelection();

            if (
                    matchedPairs
                    == totalPairs
            ) {
                handleWin();
            }

        } else {

            isBusy = true;

            Timer timer =
                    new Timer(
                            800,
                            e -> {

                                applyCardBack(
                                        firstFlipped
                                );

                                applyCardBack(
                                        secondFlipped
                                );

                                resetSelection();

                                isBusy =
                                        false;
                            }
                    );

            timer.setRepeats(
                    false
            );

            timer.start();
        }
    }
    
    private void startGameTimer() {

        gameTimer =
                new Timer(
                        1000,
                        e -> {

                            elapsedSeconds++;

                            updateInfoLabels();
                        }
                );

        gameTimer.start();
    }
    
    private void updateInfoLabels() {

        lblScore.setText(
                String.valueOf(
                        score
                )
        );

        lblTime.setText(
                String.format(
                        "%02d:%02d",
                        elapsedSeconds / 60,
                        elapsedSeconds % 60
                )
        );
    }
    
    private class CardItem {

        private final String cardId;
        private final JButton button;

        private boolean matched;
        private boolean faceUp;

        CardItem(
                String cardId,
                JButton button
        ) {

            this.cardId =
                    cardId;

            this.button =
                    button;
        }

        String getCardId() {
            return cardId;
        }

        JButton getButton() {
            return button;
        }

        boolean isMatched() {
            return matched;
        }

        void setMatched(
                boolean matched
        ) {
            this.matched =
                    matched;
        }

        boolean isFaceUp() {
            return faceUp;
        }
    }

    private CardItem findCardByButton(JButton button) {

        for (
                CardItem card
                : cards
        ) {

            if (
                    card.getButton()
                    == button
            ) {
                return card;
            }
        }

        return null;
    }
    
    private void applyCardFront(CardItem card) {

        card.faceUp =
                true;

        refreshCardDisplay(
                card
        );
    }
    
    private void applyCardBack(CardItem card) {

        card.faceUp =
                false;

        refreshCardDisplay(
                card
        );
    }
    
    private void refreshCardDisplay(CardItem card) {

        JButton btn =
                card.getButton();

        int iconWidth =
                Math.max(
                        40,
                        btn.getWidth() - 10
                );

        int iconHeight =
                Math.max(
                        40,
                        btn.getHeight() - 10
                );

        if (
                card.faceUp
                || card.matched
        ) {

            if (
                    cardImageConfig
                            .isUsingImages()
            ) {

                javax.swing.ImageIcon
                        frontIcon =
                        cardImageConfig
                                .getFrontIcon(
                                        card.cardId,
                                        iconWidth,
                                        iconHeight
                                );

                btn.setIcon(
                        frontIcon
                );

                btn.setText("");

            } else {

                btn.setIcon(null);

                btn.setText(
                        card.cardId
                );
            }

        } else {

            if (
                    cardImageConfig
                            .isUsingImages()
            ) {

                javax.swing.ImageIcon
                        backIcon =
                        cardImageConfig
                                .getBackIcon(
                                        iconWidth,
                                        iconHeight
                                );

                btn.setIcon(
                        backIcon
                );

                btn.setText("");

            } else {

                btn.setIcon(null);

                btn.setText(
                        CARD_BACK_TEXT
                );
            }
        }
    }
    
    private void refreshAllCardDisplays() {

        for (
                CardItem card
                : cards
        ) {

            refreshCardDisplay(
                    card
            );
        }
    }
    
    private void attachResizeListener() {

        addComponentListener(
                new java.awt.event.ComponentAdapter() {

                    @Override
                    public void componentResized(
                            java.awt.event.ComponentEvent e
                    ) {

                        refreshAllCardDisplays();
                    }
                }
        );
    }
    
    private void formatPanelInfo() {

        pnlInfo.setLayout(
                new GridLayout(2,4,10,0)
        );

        pnlInfo.setBorder(
            BorderFactory.createEmptyBorder(
                10,10,10,10
            )
        );

        pnlInfo.add(lblUsernameText);
        pnlInfo.add(lblUsername);

        pnlInfo.add(lblLevelText);
        pnlInfo.add(lblLevel);

        pnlInfo.add(lblScoreText);
        pnlInfo.add(lblScore);

        pnlInfo.add(lblTimeText);
        pnlInfo.add(lblTime);


    }
    
    private void formatPanelButtons() {
        
        pnlButton.setLayout(
            new FlowLayout(
                FlowLayout.CENTER,
                100,
                10
            )
        );
        
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlInfo = new javax.swing.JPanel();
        lblUsernameText = new javax.swing.JLabel();
        lblUsername = new javax.swing.JLabel();
        lblScoreText = new javax.swing.JLabel();
        lblScore = new javax.swing.JLabel();
        lblLevelText = new javax.swing.JLabel();
        lblLevel = new javax.swing.JLabel();
        lblTimeText = new javax.swing.JLabel();
        lblTime = new javax.swing.JLabel();
        pnlButton = new javax.swing.JPanel();
        btnRestart = new javax.swing.JButton();
        btnLeaderboard = new javax.swing.JButton();
        btnExit = new javax.swing.JButton();
        pnlBoard = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Game - Memory Matching Card");
        setResizable(false);

        pnlInfo.setPreferredSize(new java.awt.Dimension(0, 60));
        pnlInfo.setLayout(new java.awt.GridLayout(1, 0));

        lblUsernameText.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        lblUsernameText.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblUsernameText.setText("Username :");
        lblUsernameText.setAlignmentX(2.0F);
        lblUsernameText.setAlignmentY(5.0F);
        pnlInfo.add(lblUsernameText);

        lblUsername.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        lblUsername.setText("username");
        pnlInfo.add(lblUsername);

        lblScoreText.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        lblScoreText.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblScoreText.setText("Score :");
        pnlInfo.add(lblScoreText);

        lblScore.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        lblScore.setText("score");
        pnlInfo.add(lblScore);

        lblLevelText.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        lblLevelText.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblLevelText.setText("Level :");
        pnlInfo.add(lblLevelText);

        lblLevel.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        lblLevel.setText("level");
        pnlInfo.add(lblLevel);

        lblTimeText.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        lblTimeText.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblTimeText.setText("Time :");
        pnlInfo.add(lblTimeText);

        lblTime.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        lblTime.setText("time");
        pnlInfo.add(lblTime);

        getContentPane().add(pnlInfo, java.awt.BorderLayout.PAGE_START);

        pnlButton.setMinimumSize(new java.awt.Dimension(0, 40));
        pnlButton.setPreferredSize(new java.awt.Dimension(0, 40));

        btnRestart.setFont(new java.awt.Font("Comic Sans MS", 1, 12)); // NOI18N
        btnRestart.setText("Restart");
        btnRestart.setAlignmentY(0.0F);
        btnRestart.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnRestart.setMargin(new java.awt.Insets(5, 15, 5, 15));
        btnRestart.setName(""); // NOI18N
        btnRestart.setPreferredSize(new java.awt.Dimension(125, 25));
        btnRestart.addActionListener(this::btnRestartActionPerformed);
        pnlButton.add(btnRestart);

        btnLeaderboard.setFont(new java.awt.Font("Comic Sans MS", 1, 12)); // NOI18N
        btnLeaderboard.setText("Leaderboard");
        btnLeaderboard.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnLeaderboard.setMargin(new java.awt.Insets(5, 15, 5, 15));
        btnLeaderboard.setPreferredSize(new java.awt.Dimension(150, 25));
        btnLeaderboard.addActionListener(this::btnLeaderboardActionPerformed);
        pnlButton.add(btnLeaderboard);

        btnExit.setFont(new java.awt.Font("Comic Sans MS", 1, 12)); // NOI18N
        btnExit.setText("Exit");
        btnExit.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnExit.setMargin(new java.awt.Insets(5, 15, 5, 15));
        btnExit.setPreferredSize(new java.awt.Dimension(100, 25));
        btnExit.addActionListener(this::btnExitActionPerformed);
        pnlButton.add(btnExit);

        getContentPane().add(pnlButton, java.awt.BorderLayout.SOUTH);

        javax.swing.GroupLayout pnlBoardLayout = new javax.swing.GroupLayout(pnlBoard);
        pnlBoard.setLayout(pnlBoardLayout);
        pnlBoardLayout.setHorizontalGroup(
            pnlBoardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 711, Short.MAX_VALUE)
        );
        pnlBoardLayout.setVerticalGroup(
            pnlBoardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 329, Short.MAX_VALUE)
        );

        getContentPane().add(pnlBoard, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void resetSelection() {

        firstFlipped =
                null;

        secondFlipped =
                null;
    }

    private void handleWin() {

        gameFinished =
                true;

        if (
                gameTimer != null
        ) {

            gameTimer.stop();
        }

        javax.swing.JOptionPane
                .showMessageDialog(
                        this,
                        "You Win!\n"
                        + "Score: "
                        + score
                        + "\nTime: "
                        + lblTime.getText()
                        + "\nDifficulty: "
                        + difficulty
                );

        try {

            playHistoryDAO
                    .saveResult(
                            userId,
                            score,
                            elapsedSeconds,
                            difficulty
                    );

        } catch (
                Exception e
        ) {

            e.printStackTrace();
        }
    }
    
    private void btnRestartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRestartActionPerformed
        if (gameTimer != null) {
            gameTimer.stop();
        }

        new GameForm(
                userId,
                username,
                difficulty
        ).setVisible(true);

        dispose();
    }//GEN-LAST:event_btnRestartActionPerformed

    private void btnLeaderboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLeaderboardActionPerformed
        new LeaderboardForm()
                .setVisible(true);

        dispose();
    }//GEN-LAST:event_btnLeaderboardActionPerformed

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
        if (
                gameTimer != null
        ) {
            gameTimer.stop();
        }

        new MainMenu()
                .setVisible(true);

        dispose();
    }//GEN-LAST:event_btnExitActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new GameForm().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnLeaderboard;
    private javax.swing.JButton btnRestart;
    private javax.swing.JLabel lblLevel;
    private javax.swing.JLabel lblLevelText;
    private javax.swing.JLabel lblScore;
    private javax.swing.JLabel lblScoreText;
    private javax.swing.JLabel lblTime;
    private javax.swing.JLabel lblTimeText;
    private javax.swing.JLabel lblUsername;
    private javax.swing.JLabel lblUsernameText;
    private javax.swing.JPanel pnlBoard;
    private javax.swing.JPanel pnlButton;
    private javax.swing.JPanel pnlInfo;
    // End of variables declaration//GEN-END:variables
}
