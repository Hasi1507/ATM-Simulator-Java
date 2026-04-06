package atm.simulator.system;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import com.toedter.calendar.JDateChooser;
import java.awt.event.*;

public class SignupOne extends JFrame implements ActionListener {

    long random;
    JTextField nameTextField, fnameTextField, emailTextField,
            addressTextField, cityTextField, stateTextField, pinTextField;
    JButton next;
    JRadioButton male, female, other, married, unmarried;
    JDateChooser dateChooser;

    SignupOne() {

        setTitle("Signup - Page 1");

        // ===== BACKGROUND =====
        JPanel panel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g;

                int w = getWidth();
                int h = getHeight();

                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(15,32,39),
                        w, h, new Color(44,83,100)
                );

                g2.setPaint(gp);
                g2.fillRect(0,0,w,h);

                g2.setColor(new Color(255,255,255,40));
                g2.fillOval(-200,-200,500,500);
                g2.fillOval(w-300,h-300,400,400);
            }
        };

        panel.setLayout(null);
        setContentPane(panel);

        // ===== GLASS CARD =====
        JPanel card = new JPanel(){
            protected void paintComponent(Graphics g){
                Graphics2D g2 = (Graphics2D) g;

                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(new Color(0,0,0,70));
                g2.fillRoundRect(15,15,getWidth()-15,getHeight()-15,30,30);

                g2.setColor(new Color(255,255,255,180));
                g2.fillRoundRect(0,0,getWidth()-15,getHeight()-15,30,30);
            }
        };

        card.setLayout(null);
        card.setOpaque(false);
        card.setBounds(200,40,700,760);
        panel.add(card);

        // center card
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                card.setLocation(
                        (getWidth()-card.getWidth())/2,
                        (getHeight()-card.getHeight())/2
                );
            }
        });

        JLabel step = new JLabel("Step 1 of 3");
        step.setBounds(520, 10, 120, 20);
        step.setFont(new Font("Segoe UI", Font.BOLD, 14));
        card.add(step);

        Random ran = new Random();
        random = Math.abs((ran.nextLong() % 9000L) + 1000L);

        JLabel formno = new JLabel("APPLICATION FORM NO. " + random);
        formno.setBounds(120, 40, 400, 30);
        formno.setFont(new Font("Segoe UI", Font.BOLD, 20));
        card.add(formno);

        JLabel title = new JLabel("Personal Details");
        title.setBounds(200, 80, 300, 30);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        card.add(title);

        int y = 140;

        card.add(label("👤 Name", 50, y));
        nameTextField = field(250, y);
        card.add(nameTextField);

        y += 50;
        card.add(label("👨 Father's Name", 50, y));
        fnameTextField = field(250, y);
        card.add(fnameTextField);

        y += 50;
        card.add(label("🎂 Date of Birth", 50, y));
        dateChooser = new JDateChooser();
        dateChooser.setBounds(250, y, 300, 30);
        card.add(dateChooser);

        y += 50;
        card.add(label("🚻 Gender", 50, y));

        male = new JRadioButton("Male");
        female = new JRadioButton("Female");

        male.setBounds(250,y,80,30);
        female.setBounds(350,y,100,30);

        male.setBackground(Color.WHITE);
        female.setBackground(Color.WHITE);

        ButtonGroup gendergroup = new ButtonGroup();
        gendergroup.add(male);
        gendergroup.add(female);

        card.add(male);
        card.add(female);

        y += 50;
        card.add(label("📧 Email", 50, y));
        emailTextField = field(250, y);
        card.add(emailTextField);

        y += 50;
        card.add(label("💍 Marital Status", 50, y));

        married = new JRadioButton("Married");
        unmarried = new JRadioButton("Unmarried");
        other = new JRadioButton("Other");

        married.setBounds(250,y,100,30);
        unmarried.setBounds(350,y,120,30);
        other.setBounds(480,y,80,30);

        married.setBackground(Color.WHITE);
        unmarried.setBackground(Color.WHITE);
        other.setBackground(Color.WHITE);

        ButtonGroup maritalgroup = new ButtonGroup();
        maritalgroup.add(married);
        maritalgroup.add(unmarried);
        maritalgroup.add(other);

        card.add(married);
        card.add(unmarried);
        card.add(other);

        y += 50;
        card.add(label("🏠 Address", 50, y));
        addressTextField = field(250, y);
        card.add(addressTextField);

        y += 50;
        card.add(label("🌆 City", 50, y));
        cityTextField = field(250, y);
        card.add(cityTextField);

        y += 50;
        card.add(label("🏙 State", 50, y));
        stateTextField = field(250, y);
        card.add(stateTextField);

        y += 50;
        card.add(label("📍 PIN Code", 50, y));
        pinTextField = field(250, y);
        card.add(pinTextField);

        y += 70;
        next = new JButton("NEXT ➜");
        next.setBounds(250, y, 160, 45);
        next.setBackground(new Color(15,32,39));
        next.setForeground(Color.WHITE);
        next.setFont(new Font("Segoe UI", Font.BOLD, 15));
        next.setFocusPainted(false);
        next.addActionListener(this);
        card.add(next);

        setSize(1000,820);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    JLabel label(String text, int x, int y) {
        JLabel l = new JLabel(text);
        l.setBounds(x, y, 180, 25);
        l.setFont(new Font("Segoe UI", Font.BOLD, 14));
        return l;
    }

    JTextField field(int x, int y) {
        JTextField f = new JTextField();
        f.setBounds(x, y, 300, 30);
        f.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        return f;
    }

    public void actionPerformed(ActionEvent ae) {

        String formno = "" + random;

        try {
            Conn c = new Conn();

            String query = "insert into signup values('" + formno + "','" +
                    nameTextField.getText() + "','" +
                    fnameTextField.getText() + "','" +
                    ((JTextField) dateChooser.getDateEditor().getUiComponent()).getText() + "','" +
                    (male.isSelected() ? "Male" : "Female") + "','" +
                    emailTextField.getText() + "','" +
                    (married.isSelected() ? "Married" : unmarried.isSelected() ? "Unmarried" : "Other") + "','" +
                    addressTextField.getText() + "','" +
                    cityTextField.getText() + "','" +
                    pinTextField.getText() + "','" +
                    stateTextField.getText() + "')";

            c.s.executeUpdate(query);

            setVisible(false);
            new SignupTwo(formno);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}