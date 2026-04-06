package atm.simulator.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.util.Date;

public class Withdraw extends JFrame implements ActionListener {

    JTextField amountField;
    JButton withdraw, back;
    String cardno;
    JPanel card;

    Withdraw(String cardno) {
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
        card.setSize(440, 300);   // only size, no fixed position
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
        JLabel heading = new JLabel("💸 Cash Withdrawal");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 22));
        heading.setBounds(110, 30, 300, 30);
        card.add(heading);

        // Amount Label
        JLabel amountLabel = new JLabel("Enter Amount");
        amountLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        amountLabel.setBounds(50, 100, 150, 25);
        card.add(amountLabel);

        // Amount Field
        amountField = new JTextField();
        amountField.setBounds(200, 100, 180, 35);
        amountField.setFont(new Font("Segoe UI", Font.BOLD, 16));
        amountField.setHorizontalAlignment(JTextField.CENTER);
        addFocusEffect(amountField);
        card.add(amountField);

        // Withdraw Button
        withdraw = new JButton("WITHDRAW ✔");
        withdraw.setBounds(80, 180, 130, 45);
        styleButton(withdraw, new Color(0, 153, 76));
        card.add(withdraw);

        // Back Button
        back = new JButton("BACK ⬅");
        back.setBounds(230, 180, 130, 45);
        styleButton(back, new Color(128, 128, 128));
        card.add(back);

        withdraw.addActionListener(this);
        back.addActionListener(this);

        setSize(600, 420);
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

    void addFocusEffect(JTextField field) {
        field.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createLineBorder(new Color(0, 92, 151), 2));
            }

            public void focusLost(FocusEvent e) {
                field.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            }
        });
    }

    public void actionPerformed(ActionEvent ae) {

        if (ae.getSource() == withdraw) {

            String amount = amountField.getText().trim();

            if (amount.equals("")) {
                JOptionPane.showMessageDialog(null, "Please enter amount");
                return;
            }

            if (!amount.matches("\\d+")) {
                JOptionPane.showMessageDialog(null, "Enter valid numeric amount");
                return;
            }

            int withdrawAmount = Integer.parseInt(amount);

            if (withdrawAmount <= 0) {
                JOptionPane.showMessageDialog(null, "Amount must be greater than 0");
                return;
            }

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

                if (balance < withdrawAmount) {
                    JOptionPane.showMessageDialog(null, "Insufficient Balance 💸");
                    return;
                }

                JOptionPane.showMessageDialog(null, "Processing...");

                Date date = new Date();

                String query = "insert into bank values('" + cardno + "','" + date +
                        "','Withdraw','" + withdrawAmount + "')";

                c.s.executeUpdate(query);

                JOptionPane.showMessageDialog(null,
                        "Rs. " + withdrawAmount + " Withdrawn Successfully");

                setVisible(false);
                new Transactions(cardno).setVisible(true);

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (ae.getSource() == back) {
            setVisible(false);
            new Transactions(cardno).setVisible(true);
        }
    }
}