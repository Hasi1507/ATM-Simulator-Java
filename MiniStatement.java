package atm.simulator.system;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;

public class MiniStatement extends JFrame {

    String cardno;
    JPanel card;

    MiniStatement(String cardno) {
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
        card.setSize(720, 400);
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
        panel.add(card);

        // Center card
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent e) {
                centerCard();
            }
        });

        JLabel heading = new JLabel("📄 Mini Statement");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 22));
        heading.setBounds(250, 10, 300, 30);
        card.add(heading);

        JLabel cardLabel = new JLabel(
                "Card: XXXX-XXXX-XXXX-" + cardno.substring(cardno.length() - 4));
        cardLabel.setBounds(30, 50, 400, 20);
        cardLabel.setForeground(Color.GRAY);
        card.add(cardLabel);

        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 14));
        area.setBackground(new Color(245, 245, 245));

        JScrollPane pane = new JScrollPane(area);
        pane.setBounds(30, 80, 660, 230);
        pane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        card.add(pane);

        int balance = 0;

        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery(
                    "select * from bank where card_number = '" + cardno + "'"
            );

            area.append(String.format("%-25s %-12s %-10s\n", "Date", "Type", "Amount"));
            area.append("--------------------------------------------------------------\n");

            while (rs.next()) {

                String date = rs.getString("date");
                String type = rs.getString("type");
                String amount = rs.getString("amount");

                area.append(String.format("%-25s %-12s ₹%-10s\n",
                        date, type, amount));

                if (type.equals("Deposit")) {
                    balance += Integer.parseInt(amount);
                } else {
                    balance -= Integer.parseInt(amount);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        JLabel bal = new JLabel("Current Balance: ₹ " + balance);
        bal.setFont(new Font("Segoe UI", Font.BOLD, 16));
        bal.setForeground(new Color(0, 153, 76));
        bal.setBounds(30, 330, 300, 25);
        card.add(bal);

        JButton back = new JButton("⬅ BACK");
        back.setBounds(560, 330, 130, 40);
        styleButton(back, new Color(128, 128, 128));
        card.add(back);

        back.addActionListener(e -> {
            setVisible(false);
            new Transactions(cardno).setVisible(true);
        });

        setSize(800, 480);
        setLocationRelativeTo(null);

        // initial center
        centerCard();

        setVisible(true);
    }

    void centerCard() {
        card.setLocation(
                (getWidth() - card.getWidth()) / 2,
                (getHeight() - card.getHeight()) / 2
        );
    }

    void styleButton(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(color.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(color);
            }
        });
    }
}