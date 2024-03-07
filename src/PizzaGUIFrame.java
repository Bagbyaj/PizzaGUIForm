import javax.swing.*;
import java.awt.*;
import javax.swing.SwingUtilities;

public class PizzaGUIFrame extends JFrame {
    private final JTextArea orderTextArea;
    private final JComboBox<String> sizeComboBox;
    private final JRadioButton thinCrust, regularCrust, deepDishCrust;
    private final JCheckBox[] toppingsCheckBoxes;
    private final ButtonGroup crustGroup;

    public PizzaGUIFrame() {
        setTitle("Pizza Order Form");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel crustPanel = new JPanel();
        crustPanel.setBorder(BorderFactory.createTitledBorder("Crust Type"));
        thinCrust = new JRadioButton("Thin");
        regularCrust = new JRadioButton("Regular");
        deepDishCrust = new JRadioButton("Deep-dish");
        crustGroup = new ButtonGroup();
        crustGroup.add(thinCrust);
        crustGroup.add(regularCrust);
        crustGroup.add(deepDishCrust);
        crustPanel.add(thinCrust);
        crustPanel.add(regularCrust);
        crustPanel.add(deepDishCrust);

        // Size selection
        String[] sizes = {"Small", "Medium", "Large", "Super"};
        sizeComboBox = new JComboBox<>(sizes);
        JPanel sizePanel = new JPanel();
        sizePanel.setBorder(BorderFactory.createTitledBorder("Size"));
        sizePanel.add(sizeComboBox);

        JPanel toppingsPanel = new JPanel();
        toppingsPanel.setBorder(BorderFactory.createTitledBorder("Toppings"));
        String[] toppings = {"Pepperoni", "Sausage", "Bacon", "Pineapple", "Peppers", "Olives"};
        toppingsCheckBoxes = new JCheckBox[toppings.length];
        for (int i = 0; i < toppings.length; i++) {
            toppingsCheckBoxes[i] = new JCheckBox(toppings[i]);
            toppingsPanel.add(toppingsCheckBoxes[i]);
        }

        orderTextArea = new JTextArea(10, 30);
        orderTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(orderTextArea);
        JPanel orderPanel = new JPanel();
        orderPanel.setBorder(BorderFactory.createTitledBorder("Order Summary"));
        orderPanel.add(scrollPane);

        JButton orderButton = new JButton("Order");
        JButton clearButton = new JButton("Clear");
        JButton quitButton = new JButton("Quit");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(orderButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(quitButton);

        orderButton.addActionListener(e -> compileOrder());
        clearButton.addActionListener(e -> clearForm());
        quitButton.addActionListener(e -> quitApplication());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(1, 3));
        topPanel.add(crustPanel);
        topPanel.add(sizePanel);
        topPanel.add(toppingsPanel);

        add(topPanel, BorderLayout.CENTER);
        add(orderPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setSize(600, 600);
        setLocationRelativeTo(null);
    }

    private void compileOrder() {
        StringBuilder order = new StringBuilder();
        double baseCost = switch (sizeComboBox.getSelectedItem().toString()) {
            case "Small" -> 8.00;
            case "Medium" -> 12.00;
            case "Large" -> 16.00;
            case "Super" -> 20.00;
            default -> 0.00;
        };
        double toppingsCost = 0;
        final String lineFormat = "%-20s $%.2f\n";
        for (JCheckBox checkBox : toppingsCheckBoxes) {
            if (checkBox.isSelected()) {
                toppingsCost += 1.00;
                order.append(checkBox.getText()).append("\t$1.00\n");
            }
        }

        String crustType = thinCrust.isSelected() ? "Thin" : regularCrust.isSelected() ? "Regular" : "Deep-dish";
        order.insert(0, crustType + " " + sizeComboBox.getSelectedItem() + "\t$" + baseCost + "\n");

        double subTotal = baseCost + toppingsCost;
        double tax = subTotal * 0.07;
        double total = subTotal + tax;

        order.append("\n");
        order.append(String.format(lineFormat, "Sub-total:", subTotal))
                .append(String.format(lineFormat, "Tax:", tax))
                .append("-----------------------------------------\n")
                .append(String.format(lineFormat, "Total:", total));

        orderTextArea.setText(order.toString());
    }

    private void clearForm() {
        crustGroup.clearSelection();
        sizeComboBox.setSelectedIndex(0);
        for (JCheckBox checkBox : toppingsCheckBoxes) {
            checkBox.setSelected(false);
        }
        orderTextArea.setText("");
    }

    private void quitApplication() {
        int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to quit?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PizzaGUIFrame().setVisible(true);
        });
    }
}
