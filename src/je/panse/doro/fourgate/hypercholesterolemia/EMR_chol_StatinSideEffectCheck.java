package je.panse.doro.fourgate.hypercholesterolemia;

import javax.swing.*;	
import je.panse.doro.GDSEMR_frame;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

public class EMR_chol_StatinSideEffectCheck extends JFrame {
    private JCheckBox[] sideEffectBoxes;
    private JCheckBox[] emergencyBoxes;
    private JTextArea resultArea;
    private final Color BROWN = new Color(139, 69, 19);
    private final Font TITLE_FONT = new Font("Arial", Font.BOLD, 11);
    private boolean[] isNegativeEffect;
    private boolean[] isNegativeEmergency;

    public EMR_chol_StatinSideEffectCheck() {
        setTitle("Statin Side Effect Checker");
        setLayout(new BorderLayout());

        JPanel northPanel = new JPanel(new GridLayout(0, 1));
        northPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 5));
        
        String[] sideEffects = {
            "All other side effects have been denied.",
            "Muscle Pain & Weakness",
            "Rhabdomyolysis",
            "Liver Problems",
            "Cognitive Issues",
            "Diabetes Risk",
            "Digestive Issues",
            "Nerve Problems",
            "All side effects have been denied.",
        };

        String[] emergencySymptoms = {
            "All other emergency side effects have been denied.",
            "Severe muscle pain or weakness",
            "Dark urine",
            "Yellowing of skin/eyes",
            "Unexplained fever",
            "Severe stomach pain",
            "Allergic reactions",
            "All emergency side effects have been denied.",
        };

        sideEffectBoxes = new JCheckBox[sideEffects.length];
        emergencyBoxes = new JCheckBox[emergencySymptoms.length];
        isNegativeEffect = new boolean[sideEffects.length];
        isNegativeEmergency = new boolean[emergencySymptoms.length];

        JLabel commonLabel = new JLabel("Common Side Effects: Statin/Ezetimibe");
        commonLabel.setFont(TITLE_FONT);
        commonLabel.setForeground(BROWN);
        northPanel.add(commonLabel);

        for (int i = 0; i < sideEffects.length; i++) {
            final int index = i;
            sideEffectBoxes[i] = new JCheckBox("    " + sideEffects[i]);
            sideEffectBoxes[i].addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2 && sideEffectBoxes[index].isSelected()) {
                        isNegativeEffect[index] = !isNegativeEffect[index];
                        updateTextArea();
                    }
                }
            });
            northPanel.add(sideEffectBoxes[i]);
            sideEffectBoxes[i].addActionListener(e -> updateTextArea());
        }

        JLabel emergencyLabel = new JLabel("Emergency Symptoms: Statin/Ezetimibe");
        emergencyLabel.setFont(TITLE_FONT);
        emergencyLabel.setForeground(BROWN);
        northPanel.add(emergencyLabel);

        for (int i = 0; i < emergencySymptoms.length; i++) {
            final int index = i;
            emergencyBoxes[i] = new JCheckBox("    " + emergencySymptoms[i]);
            emergencyBoxes[i].addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2 && emergencyBoxes[index].isSelected()) {
                        isNegativeEmergency[index] = !isNegativeEmergency[index];
                        updateTextArea();
                    }
                }
            });
            northPanel.add(emergencyBoxes[i]);
            emergencyBoxes[i].addActionListener(e -> updateTextArea());
        }

        JPanel southPanel = new JPanel(new BorderLayout());
        
        resultArea = new JTextArea(10, 40);
        resultArea.setEditable(true);
        JScrollPane scrollPane = new JScrollPane(resultArea);
        southPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton clearButton = new JButton("Clear");
        JButton saveButton = new JButton("Save");
        JButton quitButton = new JButton("Quit");

        clearButton.addActionListener(e -> {
            for (JCheckBox box : sideEffectBoxes) box.setSelected(false);
            for (JCheckBox box : emergencyBoxes) box.setSelected(false);
            Arrays.fill(isNegativeEffect, false);
            Arrays.fill(isNegativeEmergency, false);
            resultArea.setText("");
        });

        saveButton.addActionListener(e -> {
            StringBuilder formattedText = new StringBuilder();
            formattedText.append("     < Statin Side Effect Check Lists> ------------\n");
            formattedText.append("          • Muscle Pain & Weakness - includes cramps and tenderness\n");
            formattedText.append("          • Rhabdomyolysis - Severe muscle breakdown\n"); 
            formattedText.append("          • Liver Problems - Inflammation and dysfunction\n");
            formattedText.append("          • Cognitive Issues - Memory problems, confusion\n");
            formattedText.append("          • Diabetes Risk - Increased type 2 diabetes risk\n");
            formattedText.append("          • Digestive Issues - Nausea, diarrhea, stomach pain\n");
            formattedText.append("          • Nerve Problems - Peripheral neuropathy, tingling, numbness\n\n");
            
            String currentText = resultArea.getText();
            if (!currentText.trim().isEmpty()) {
                String[] lines = currentText.split("\n");
                for (String line : lines) {
                    formattedText.append("     ").append(line).append("\n");
                }
            }
            
            GDSEMR_frame.setTextAreaText(1, formattedText.toString());
            dispose();
        });

        quitButton.addActionListener(e -> dispose());

        buttonPanel.add(clearButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(quitButton);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(northPanel, BorderLayout.NORTH);
        add(southPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void updateTextArea() {
        StringBuilder text = new StringBuilder("***Selected Side Effects:  Statin/Ezetimibe***\n");
        for (int i = 0; i < sideEffectBoxes.length; i++) {
            if (sideEffectBoxes[i].isSelected()) {
                text.append("   [").append(isNegativeEffect[i] ? " - " : " + ").append("]")
                    .append(sideEffectBoxes[i].getText().trim()).append("\n");
            }
        }
        
        text.append("\n***Selected Emergency Symptoms:  Statin/Ezetimibe***\n");
        for (int i = 0; i < emergencyBoxes.length; i++) {
            if (emergencyBoxes[i].isSelected()) {
                text.append("   [").append(isNegativeEmergency[i] ? " - " : " + ").append("]")
                    .append(emergencyBoxes[i].getText().trim()).append("\n");
            }
        }
        resultArea.setText(text.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EMR_chol_StatinSideEffectCheck());
    }
}
