package org.example;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.*;

public class WebpageMonitorGUI extends JFrame {
    private JButton startButton;
    private JButton stopButton;
    private JButton updateButton;

    public WebpageMonitorGUI() {
        setTitle("Webpage Monitor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLayout(new FlowLayout());

        startButton = new JButton("Start");
        stopButton = new JButton("Stop");
        updateButton = new JButton("Update Webhook");

        add(startButton);
        add(stopButton);
        add(updateButton);

        // Action listeners
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WebpageMonitor monitor = new WebpageMonitor();
                WebpageMonitor.setRunning(true);
                try {
                    monitor.main();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WebpageMonitor.setRunning(false);
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = JOptionPane.showInputDialog("Enter the new webhook URL:");
                if (input != null && !input.isEmpty()) {
                    WebpageMonitor.setWebhook(input);
                }
                else {
                    JOptionPane.showMessageDialog(null, "Error: InvalidInput", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public static void main(String[] args) {
        WebpageMonitorGUI gui = new WebpageMonitorGUI();
        gui.setVisible(true);
    }
}
