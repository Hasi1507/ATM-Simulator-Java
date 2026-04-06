package atm.simulator.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.util.Date;

public class NetBanking extends JFrame implements ActionListener {

    JTextField receiverField, amountField, otpField;
    JButton generateOTP, transfer, back;

    String cardno;
    int generatedOTP;
    long otpTime;

    JPanel card;

    NetBanking(String cardno) {
        this.cardno = cardno;

        // SAME DARK ATM Gradient
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
        card.setSize(540, 340);
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
        panel.add(card);

        // Center card dynamically
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                centerCard();
            }
        });

        // Title
        JLabel heading = new JLabel("🌐 Net Banking Transfer");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 20));
        heading.setBounds(140, 20, 300, 30);
        card.add(heading);

        // Receiver
        JLabel r = new JLabel("Receiver Card No");
        r.setBounds(50, 80, 150, 25);
        card.add(r);

        receiverField = new JTextField();
        receiverField.setBounds(220, 80, 250, 35);
        styleField(receiverField);
        card.add(receiverField);

        // Amount
        JLabel a = new JLabel("Amount");
        a.setBounds(50, 130, 150, 25);
        card.add(a);

        amountField = new JTextField();
        amountField.setBounds(220, 130, 250, 35);
        styleField(amountField);
        card.add(amountField);

        // OTP Button
        generateOTP = new JButton("Generate OTP");
        generateOTP.setBounds(220, 180, 150, 35);
        styleButton(generateOTP, new Color(0, 102, 204));
        card.add(generateOTP);

        // OTP Field
        JLabel o = new JLabel("Enter OTP");
        o.setBounds(50, 230, 150, 25);
        card.add(o);

        otpField = new JTextField();
        otpField.setBounds(220, 230, 150, 35);
        styleField(otpField);
        card.add(otpField);

        // Buttons
        transfer = new JButton("TRANSFER ✔");
        transfer.setBounds(120, 280, 150, 40);
        styleButton(transfer, new Color(0, 153, 76));

        back = new JButton("⬅ BACK");
        back.setBounds(300, 280, 150, 40);
        styleButton(back, new Color(128, 128, 128));

        card.add(transfer);
        card.add(back);

        generateOTP.addActionListener(this);
        transfer.addActionListener(this);
        back.addActionListener(this);

        setSize(700, 450);
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

    void styleField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.BOLD, 14));
        field.setHorizontalAlignment(JTextField.CENTER);
        field.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                field.setBorder(
                        BorderFactory.createLineBorder(new Color(0, 102, 204), 2));
            }

            public void focusLost(FocusEvent e) {
                field.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            }
        });
    }

    void styleButton(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
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

        if (ae.getSource() == generateOTP) {
            generatedOTP = (int) (Math.random() * 9000) + 1000;
            otpTime = System.currentTimeMillis();

            JOptionPane.showMessageDialog(null,
                    "Your OTP is: " + generatedOTP + "\n(Simulation Only)");
        }

        else if (ae.getSource() == transfer) {

            String receiver = receiverField.getText().trim();
            String amountText = amountField.getText().trim();
            String otpText = otpField.getText().trim();

            if (receiver.isEmpty() || amountText.isEmpty() || otpText.isEmpty()) {
                JOptionPane.showMessageDialog(null, "All fields are required");
                return;
            }

            if (!receiver.matches("\\d{16}")) {
                JOptionPane.showMessageDialog(null, "Enter valid 16-digit card number");
                return;
            }

            if (!amountText.matches("\\d+")) {
                JOptionPane.showMessageDialog(null, "Enter valid amount");
                return;
            }

            int amount = Integer.parseInt(amountText);

            try {
                int enteredOTP = Integer.parseInt(otpText);

                if (enteredOTP != generatedOTP) {
                    JOptionPane.showMessageDialog(null, "Invalid OTP ❌");
                    return;
                }

                if (System.currentTimeMillis() - otpTime > 60000) {
                    JOptionPane.showMessageDialog(null, "OTP Expired ⏱");
                    return;
                }

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
                    JOptionPane.showMessageDialog(null, "Insufficient Balance 💸");
                    return;
                }

                Date date = new Date();

                c.s.executeUpdate(
                        "insert into bank values('" + cardno + "','" + date +
                                "','Online Transfer','" + amount + "')"
                );

                c.s.executeUpdate(
                        "insert into bank values('" + receiver + "','" + date +
                                "','Online Received','" + amount + "')"
                );

                JOptionPane.showMessageDialog(null, "✅ Transfer Successful");

                setVisible(false);
                new Transactions(cardno).setVisible(true);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Transaction Failed");
                e.printStackTrace();
            }
        }

        else if (ae.getSource() == back) {
            setVisible(false);
            new Transactions(cardno).setVisible(true);
        }
    }
}