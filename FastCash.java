package atm.simulator.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.util.Date;

public class FastCash extends JFrame implements ActionListener {

    JButton rs100, rs500, rs1000, rs2000, rs5000, back;
    String cardno;
    JPanel card;

    FastCash(String cardno) {
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
        card.setSize(680, 320); // only size
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

        JLabel text = new JLabel("⚡ Fast Cash");
        text.setFont(new Font("Segoe UI", Font.BOLD, 24));
        text.setBounds(260, 20, 300, 30);
        card.add(text);

        JLabel subText = new JLabel("Select Withdrawal Amount");
        subText.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subText.setBounds(260, 50, 250, 20);
        subText.setForeground(Color.GRAY);
        card.add(subText);

        rs100 = createButton("₹ 100", 120, 100, new Color(0, 153, 76));
        rs500 = createButton("₹ 500", 360, 100, new Color(0, 153, 76));
        rs1000 = createButton("₹ 1000", 120, 160, new Color(0, 153, 76));
        rs2000 = createButton("₹ 2000", 360, 160, new Color(0, 153, 76));
        rs5000 = createButton("₹ 5000", 120, 220, new Color(0, 153, 76));
        back = createButton("⬅ BACK", 360, 220, new Color(128, 128, 128));

        card.add(rs100);
        card.add(rs500);
        card.add(rs1000);
        card.add(rs2000);
        card.add(rs5000);
        card.add(back);

        rs100.addActionListener(this);
        rs500.addActionListener(this);
        rs1000.addActionListener(this);
        rs2000.addActionListener(this);
        rs5000.addActionListener(this);
        back.addActionListener(this);

        setSize(800, 420);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    JButton createButton(String text, int x, int y, Color color) {
        JButton btn = new JButton(text);
        btn.setBounds(x, y, 200, 45);
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

        return btn;
    }

    public void actionPerformed(ActionEvent ae) {

        if (ae.getSource() == back) {
            setVisible(false);
            new Transactions(cardno).setVisible(true);
            return;
        }

        String text = ((JButton) ae.getSource()).getText().replace("₹", "").trim();
        int amount = Integer.parseInt(text);

        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery(
                    "select * from bank where card_number = '" + cardno + "'"
            );

            int balance = 0;

            while (rs.next()) {
                if (rs.getString("type").equals("Deposit")) {
                    balance += Integer.parseInt(rs.getString("amount"));
                } else {
                    balance -= Integer.parseInt(rs.getString("amount"));
                }
            }

            if (balance < amount) {
                JOptionPane.showMessageDialog(null, "Insufficient Balance");
                return;
            }

            JOptionPane.showMessageDialog(null, "Processing...");

            Date date = new Date();

            String query = "insert into bank values('" + cardno + "','" + date +
                    "','Withdraw','" + amount + "')";

            c.s.executeUpdate(query);

            JOptionPane.showMessageDialog(null,
                    "₹ " + amount + " Withdrawn Successfully");

            setVisible(false);
            new Transactions(cardno).setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}