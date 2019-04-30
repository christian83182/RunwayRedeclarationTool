package uk.ac.soton.view;

import uk.ac.soton.controller.ViewController;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.KeyEvent;

class ViewCalculationsWindow extends JFrame{

    private ViewController controller;
    private AppView appView;

    ViewCalculationsWindow(ViewController controller, AppView appView){

        super("Calculation Breakdowns");
        this.controller = controller;
        this.appView = appView;
        init();
    }

    private void init(){
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setPreferredSize(new Dimension(400,185));
        this.setResizable(false);
        this.setLayout(new GridLayout(1,1));

        ImageIcon icon = new ImageIcon(getClass().getResource("/uk/ac/soton/resources/images/Applicationicon.png"));
        this.setIconImage(icon.getImage());

        String selectedRunway = appView.getSelectedRunway();

        JTabbedPane tabs = new JTabbedPane();

        // TORA tab
        JComponent panelTora = setPanelContent(controller.getTORABreakdown(selectedRunway),
                controller.getTORAOriginal(selectedRunway), controller.getTORARedeclared(selectedRunway));
        tabs.addTab("TORA", panelTora);
        tabs.setMnemonicAt(0, KeyEvent.VK_1);

        // TODA tab
        JComponent panelToda = setPanelContent(controller.getTODABreakdown(selectedRunway),
                controller.getTODAOriginal(selectedRunway), controller.getTODARedeclared(selectedRunway));
        tabs.addTab("TODA", panelToda);
        tabs.setMnemonicAt(1, KeyEvent.VK_2);

        // ASDA tab
        JComponent panelAsda = setPanelContent(controller.getASDABreakdown(selectedRunway),
                controller.getASDAOriginal(selectedRunway), controller.getASDARedeclared(selectedRunway));
        tabs.addTab("ASDA", panelAsda);
        tabs.setMnemonicAt(2, KeyEvent.VK_3);

        // LDA tab
        JComponent panelLda = setPanelContent(controller.getLDABreakdown(selectedRunway),
                controller.getLDAOriginal(selectedRunway), controller.getLDARedeclared(selectedRunway));
        tabs.addTab("LDA", panelLda);
        tabs.setMnemonicAt(3, KeyEvent.VK_4);

        this.add(tabs);

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    // Creates a content panel for a tab including the parameter breakdown and original/redeclared values.
    private JComponent setPanelContent(String text, Integer original, Integer redeclared){

        JPanel panel = new JPanel();
        JTextArea breakdown = new JTextArea(text);
        breakdown.setLineWrap(true);
        breakdown.setWrapStyleWord(true);
        breakdown.setEditable(false);

        JTable table;
        String[] headings = new String[]{"Original", "Redeclared"};

        if(redeclared == null){
            table = new JTable(new Object[][]{{original, "-"}}, headings);
        }
        else{
            table = new JTable(new Object[][]{{original, redeclared}}, headings);
        }

        // In order to make the cells non-editable
        table.setEnabled(false);

        JPanel tableContainer = new JPanel();
        tableContainer.setLayout(new BorderLayout());
        tableContainer.add(table.getTableHeader(), BorderLayout.NORTH);
        tableContainer.add(table, BorderLayout.CENTER);

        table.setPreferredScrollableViewportSize(new Dimension(400, 70));
        table.setFillsViewportHeight(true);

        panel.setLayout(new BorderLayout());
        panel.add(breakdown, BorderLayout.CENTER);
        panel.add(tableContainer, BorderLayout.SOUTH);

        Border line = BorderFactory.createLineBorder(Color.black);
        panel.setBorder(line);

        return panel;
    }
}
