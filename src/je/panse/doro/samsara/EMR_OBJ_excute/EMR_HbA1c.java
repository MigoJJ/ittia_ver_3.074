package je.panse.doro.samsara.EMR_OBJ_excute;

import javax.swing.*;
import java.awt.*;
import je.panse.doro.GDSEMR_frame;

public class EMR_HbA1c extends JFrame {
    private static final String[] LABELS = {
        "FBS / PP2 time", "Glucose mg/dL", "HbA1c %"
    };
    
    private static final String[][] GLUCOSE_STATUS = {
        {"9.0", "Very poor"},
        {"8.5", "Poor"},
        {"7.5", "Fair"},
        {"6.5", "Good"},
        {"0.0", "Excellent"}
    };
    
    private final JTextArea outputArea = new JTextArea(4, 20);
    private final JTextField[] inputs = new JTextField[LABELS.length];

    public EMR_HbA1c() {
        setupFrame();
        createUI();
        setVisible(true);
    }

    private void setupFrame() {
        setTitle("HbA1c EMR");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 230);
        setLocation(0, 60);
        setUndecorated(true);
        setLayout(new BorderLayout());
    }

    private void createUI() {
        add(outputArea, BorderLayout.NORTH);
        add(createInputPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2));
        
        for (int i = 0; i < LABELS.length; i++) {
            panel.add(new JLabel("      " + LABELS[i]));
            inputs[i] = createStyledTextField(i);
            panel.add(inputs[i]);
        }
        return panel;
    }

    private JTextField createStyledTextField(int index) {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(field.getPreferredSize().width, 30));
        field.setHorizontalAlignment(JTextField.CENTER);
        field.addActionListener(e -> processInput(index));
        return field;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        for (String name : new String[]{"Clear", "Save", "Save and Quit"}) {
            JButton button = new JButton(name);
            button.addActionListener(e -> handleButton(name));
            panel.add(button);
        }
        return panel;
    }

    private void processInput(int index) {
        String value = inputs[index].getText();
        
        if (index == 0) {
            outputArea.append("\n   " + (value.equals("0") ? "FBS" : "PP" + value));
        } else if (index == 1) {
            outputArea.append("  [    " + value + "   ] mg/dL");
        } else if (index == 2) {
            processHbA1c(value);
        }
        
        inputs[index].setText("");
        if (index < inputs.length - 1) {
            inputs[index + 1].requestFocus();
        }
    }

    private void processHbA1c(String value) {
        double hba1c = Double.parseDouble(value);
        outputArea.append("   HbA1c       [    " + value + "   ] %\n");
        calculateHbA1c(hba1c);
        save();
        clear();
    }

    private void calculateHbA1c(double hba1c) {
        double ifcc = (hba1c - 2.15) * 10.929;
        double eagMgDl = (28.7 * hba1c) - 46.7;
        double eagMmolL = eagMgDl / 18.01559;

        outputArea.append(String.format(
            "\n\tIFCC HbA1c: [  %.0f  ] mmol/mol" +
            "\n\teAG: [  %.0f  ] mg/dL" +
            "\n\teAG: [  %.2f  ] mmol/l\n",
            ifcc, eagMgDl, eagMmolL));

        determineGlucoseStatus(hba1c);
    }

    private void determineGlucoseStatus(double hba1c) {
        for (String[] status : GLUCOSE_STATUS) {
            if (hba1c > Double.parseDouble(status[0])) {
                GDSEMR_frame.setTextAreaText(8, 
                    "\n...now [ " + status[1] + " ] controlled glucose status");
                break;
            }
        }
    }

    private void handleButton(String name) {
        switch (name) {
            case "Clear" -> clear();
            case "Save" -> save();
            case "Save and Quit" -> {save(); dispose();}
        }
    }

    private void clear() {
        outputArea.setText("");
        for (JTextField field : inputs) field.setText("");
    }

    private void save() {
        GDSEMR_frame.setTextAreaText(5, outputArea.getText());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EMR_HbA1c::new);
    }
}