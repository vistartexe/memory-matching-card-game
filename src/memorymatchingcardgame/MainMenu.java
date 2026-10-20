package memorymatchingcardgame;

import java.awt.Font;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import memorymatchingcardgame.model.SessionManager;
import memorymatchingcardgame.model.User;

public class MainMenu extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(MainMenu.class.getName());

    public MainMenu() {
        initComponents();

        setLocationRelativeTo(null);
        
        User user = SessionManager.getInstance().getCurrentUser();
        if (user != null) {
            lblUsername.setText(user.getUsername());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnPlay = new javax.swing.JButton();
        lblMainMenu = new javax.swing.JLabel();
        btnLeaderboard = new javax.swing.JButton();
        btnExit = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        lblUsername = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        btnLogout = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Memory Matching Card");
        setBackground(new java.awt.Color(204, 51, 255));
        setResizable(false);

        btnPlay.setFont(new java.awt.Font("Comic Sans MS", 1, 12)); // NOI18N
        btnPlay.setText("PLAY");
        btnPlay.setToolTipText("");
        btnPlay.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnPlay.addActionListener(this::btnPlayActionPerformed);

        lblMainMenu.setFont(new java.awt.Font("Comic Sans MS", 1, 26)); // NOI18N
        lblMainMenu.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblMainMenu.setText("MEMORY MATCHING CARD");
        lblMainMenu.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        btnLeaderboard.setFont(new java.awt.Font("Comic Sans MS", 1, 12)); // NOI18N
        btnLeaderboard.setText("LEADERBOARD");
        btnLeaderboard.setToolTipText("");
        btnLeaderboard.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnLeaderboard.addActionListener(this::btnLeaderboardActionPerformed);

        btnExit.setFont(new java.awt.Font("Comic Sans MS", 1, 12)); // NOI18N
        btnExit.setText("EXIT");
        btnExit.setToolTipText("");
        btnExit.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnExit.addActionListener(this::btnExitActionPerformed);

        jLabel1.setFont(new java.awt.Font("Comic Sans MS", 0, 12)); // NOI18N
        jLabel1.setText("Welcome,");

        lblUsername.setFont(new java.awt.Font("Comic Sans MS", 1, 12)); // NOI18N
        lblUsername.setText("username");

        jLabel3.setFont(new java.awt.Font("Comic Sans MS", 0, 12)); // NOI18N

        btnLogout.setFont(new java.awt.Font("Comic Sans MS", 1, 12)); // NOI18N
        btnLogout.setText("< Logout");
        btnLogout.addActionListener(this::btnLogoutActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnLogout)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel3))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblUsername, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(108, 108, 108)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnExit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnLeaderboard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnPlay, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addComponent(lblMainMenu)
                .addGap(0, 55, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(jLabel3))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel1)
                                .addComponent(lblUsername))
                            .addComponent(btnLogout))))
                .addGap(33, 33, 33)
                .addComponent(lblMainMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(btnPlay, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnLeaderboard, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnExit, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(34, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        
        int confirm = javax.swing.JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to logout?",
                "Logout",
                javax.swing.JOptionPane.YES_NO_OPTION
        );

        if (confirm == javax.swing.JOptionPane.YES_OPTION) {

            SessionManager.getInstance().clear();

            new LoginForm().setVisible(true);
            this.dispose();
        }
    }//GEN-LAST:event_btnLogoutActionPerformed

    // Button PLAY: choose a difficulty then open GameForm
    private void btnPlayActionPerformed(java.awt.event.ActionEvent evt) {
        User user = SessionManager.getInstance().getCurrentUser();
        if (user == null) {
            JOptionPane.showMessageDialog(this, "Please login first.", "Session is not valid",
                    JOptionPane.WARNING_MESSAGE);
            new LoginForm().setVisible(true);
            dispose();
            return;
        }

        String difficulty = showDifficultyDialog();
        if (difficulty == null) {
            return;
        }

        new GameForm(user.getId(), user.getUsername(), difficulty).setVisible(true);
        dispose();
    }

    // Display difficulty dialog
    private String showDifficultyDialog() {
        JComboBox<String> combo = new JComboBox<>(new String[]{"Easy", "Medium", "Hard"});
        combo.setFont(new Font("Comic Sans MS", Font.PLAIN, 12));
        JPanel panel = new JPanel();
        panel.add(combo);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Choose the difficulty",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            return (String) combo.getSelectedItem();
        }
        return null;
    }

    // Button LEADERBOARD: open the LeaderboardForm
    private void btnLeaderboardActionPerformed(java.awt.event.ActionEvent evt) {
        new LeaderboardForm().setVisible(true);
        dispose();
    }

    // Button EXIT: exit from the application
    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {
        SessionManager.getInstance().clear();
        ConnectionHelper.closeConnection();
        System.exit(0);
    }

    public static void main(String[] args) {
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

        java.awt.EventQueue.invokeLater(() -> new MainMenu().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnLeaderboard;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnPlay;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel lblMainMenu;
    private javax.swing.JLabel lblUsername;
    // End of variables declaration//GEN-END:variables
}
