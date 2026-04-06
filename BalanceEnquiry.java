package atm.simulator.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;

public class BalanceEnquiry extends JFrame implements ActionListener {

    JButton back;
    String cardno;
    JPanel card;

    BalanceEnquiry(String cardno) {
        this.cardno = cardno;

        // Modern ATM Gradient Background
        JPanel panel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(8, 32, 50),
                        getWidth(), getHeight(),
                        new Color(0, 92, 151)
                );

                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        panel.setLayout(null);
        setContentPane(panel);

        // Card Panel
        card = new JPanel();
        card.setLayout(null);
        card.setBackground(Color.WHITE);
        card.setSize(440, 280); // only size
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
        panel.add(card);

        // Center card dynamically
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                card.setLocation(
                        (getWidth() - card.getWidth()) / 2,
                        (getHeight() - card.getHeight()) / 2
                );
            }
        });

        // Title
        JLabel heading = new JLabel("💰 Balance Enquiry");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 22));
        heading.setBounds(110, 30, 300, 30);
        card.add(heading);

        // Balance Label
        JLabel balanceLabel = new JLabel();
        balanceLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        balanceLabel.setForeground(new Color(0, 153, 76));
        balanceLabel.setBounds(100, 100, 300, 40);
        balanceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(balanceLabel);

        // Back Button
        back = new JButton("BACK ⬅");
        back.setBounds(150, 180, 140, 45);
        styleButton(back, new Color(128, 128, 128));
        card.add(back);

        back.addActionListener(this);

        int balance = 0;

        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery(
                    "select * from bank where card_number = '" + cardno + "'"
            );

            while (rs.next()) {
                if (rs.getString("type").equals("Deposit")) {
                    balance += Integer.parseInt(rs.getString("amount"));
                } else {
                    balance -= Integer.parseInt(rs.getString("amount"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Display Balance
        balanceLabel.setText("₹ " + balance);

        setSize(600, 400);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    void styleButton(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(color.darker());
            }

            public void mouseExited(MouseEvent e) {
                btn.setBackground(color);
            }
        });
    }

    public void actionPerformed(ActionEvent ae) {
        setVisible(false);
        new Transactions(cardno).setVisible(true);
    }
}