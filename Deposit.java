package atm.simulator.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;

public class Deposit extends JFrame implements ActionListener {

    JTextField amount;
    JButton deposit, back;
    String cardno;

    Deposit(String cardno) {

        this.cardno = cardno;

        setTitle("Deposit");

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

        panel.setLayout(new GridBagLayout());
        setContentPane(panel);

        // Card Panel
        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(400, 300));
        card.setBackground(Color.WHITE);
        card.setLayout(null);
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(card);

        // Title
        JLabel heading = new JLabel("Deposit Money");
        heading.setBounds(100, 20, 250, 30);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 22));
        card.add(heading);

        // Instruction
        JLabel text = new JLabel("Enter Amount");
        text.setBounds(40, 80, 200, 25);
        text.setFont(new Font("Segoe UI", Font.BOLD, 14));
        card.add(text);

        // Amount Field
        amount = new JTextField();
        amount.setBounds(40, 110, 300, 35);
        amount.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        amount.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        // Focus Effect
        amount.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                amount.setBorder(BorderFactory.createLineBorder(new Color(0, 92, 151), 2));
            }

            public void focusLost(FocusEvent e) {
                amount.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            }
        });

        card.add(amount);

        // Deposit Button
        deposit = new JButton("DEPOSIT");
        deposit.setBounds(40, 180, 130, 40);
        styleButton(deposit);
        deposit.addActionListener(this);
        card.add(deposit);

        // Back Button
        back = new JButton("BACK");
        back.setBounds(210, 180, 130, 40);
        styleButton(back);
        back.addActionListener(this);
        card.add(back);

        setSize(700, 500);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Button Styling
    void styleButton(JButton btn) {
        btn.setBackground(new Color(0, 92, 151));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder());

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(btn.getBackground().darker());
            }

            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(0, 92, 151));
            }
        });
    }

    public void actionPerformed(ActionEvent ae) {

        if (ae.getSource() == deposit) {

            String amt = amount.getText().trim();
            Date date = new Date();

            if (amt.equals("")) {
                JOptionPane.showMessageDialog(null, "Please enter amount");
                return;
            }

            if (!amt.matches("\\d+")) {
                JOptionPane.showMessageDialog(null, "Enter valid numeric amount");
                return;
            }

            try {
                Conn c = new Conn();

                String query = "insert into bank values('" + cardno + "','" + date + "','Deposit','" + amt + "')";
                c.s.executeUpdate(query);

                JOptionPane.showMessageDialog(null, "Rs " + amt + " Deposited Successfully");

                setVisible(false);
                new Transactions(cardno).setVisible(true);

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error occurred");
            }

        } else if (ae.getSource() == back) {
            setVisible(false);
            new Transactions(cardno).setVisible(true);
        }
    }
}