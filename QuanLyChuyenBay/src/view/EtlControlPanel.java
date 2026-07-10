package view;

import main.EtlToStaging;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.OutputStream;
import java.io.PrintStream;

public class EtlControlPanel extends JPanel {

    private JTextArea consoleTextArea;
    private JButton btnRunEtl;
    private JProgressBar progressBar;

    public EtlControlPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 1. Header & Controls
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel lblTitle = new JLabel("ETL CONTROLLER - DATA WAREHOUSE");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        
        btnRunEtl = new JButton("🚀 RUN FULL ETL (OLTP -> STG -> DIM -> FACT)");
        btnRunEtl.setFont(new Font("Arial", Font.BOLD, 14));
        btnRunEtl.setBackground(new Color(46, 204, 113));
        btnRunEtl.setForeground(Color.WHITE);
        btnRunEtl.setFocusPainted(false);
        btnRunEtl.setPreferredSize(new Dimension(400, 40));

        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setVisible(false);
        progressBar.setStringPainted(true);
        progressBar.setString("Đang xử lý ETL...");

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        controlPanel.add(progressBar);
        controlPanel.add(btnRunEtl);

        topPanel.add(lblTitle, BorderLayout.WEST);
        topPanel.add(controlPanel, BorderLayout.EAST);

        // 2. Console Area
        consoleTextArea = new JTextArea();
        consoleTextArea.setEditable(false);
        consoleTextArea.setBackground(Color.BLACK);
        consoleTextArea.setForeground(Color.GREEN);
        consoleTextArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(consoleTextArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY), 
                "System Console Log", 
                TitledBorder.LEFT, TitledBorder.TOP, 
                new Font("Arial", Font.BOLD, 12), Color.DARK_GRAY));

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // 3. Sự kiện bấm chạy
        btnRunEtl.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                runEtlProcess();
            }
        });
    }

    private void runEtlProcess() {
        // Vô hiệu hóa nút trong lúc chạy
        btnRunEtl.setEnabled(false);
        btnRunEtl.setBackground(Color.GRAY);
        progressBar.setVisible(true);
        consoleTextArea.setText("Đang khởi động hệ thống...\n");

        // Chuyển hướng luồng System.out và System.err vào JTextArea
        PrintStream standardOut = System.out;
        PrintStream standardErr = System.err;
        
        try {
            PrintStream printStream = new PrintStream(new CustomOutputStream(consoleTextArea), true, "UTF-8");
            System.setOut(printStream);
            System.setErr(printStream);
        } catch (java.io.UnsupportedEncodingException ex) {
            System.err.println("Không hỗ trợ UTF-8: " + ex.getMessage());
        }

        // Sử dụng SwingWorker để chạy background, không làm đơ UI
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                try {
                    // Gọi luồng chính của hệ thống ETL
                    EtlToStaging.main(new String[]{});
                } catch (Exception ex) {
                    System.err.println("Lỗi nghiêm trọng: " + ex.getMessage());
                }
                return null;
            }

            @Override
            protected void done() {
                // Trả lại luồng xuất chuẩn
                System.setOut(standardOut);
                System.setErr(standardErr);
                
                // Khôi phục giao diện
                btnRunEtl.setEnabled(true);
                btnRunEtl.setBackground(new Color(46, 204, 113));
                progressBar.setVisible(false);
                JOptionPane.showMessageDialog(EtlControlPanel.this, 
                        "Tiến trình ETL đã hoàn tất! Vui lòng kiểm tra log.", 
                        "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        };

        worker.execute();
    }

    // Lớp hỗ trợ in ra JTextArea
    private class CustomOutputStream extends OutputStream {
        private JTextArea textArea;

        public CustomOutputStream(JTextArea textArea) {
            this.textArea = textArea;
        }

        @Override
        public void write(int b) {
            textArea.append(String.valueOf((char) (b & 0xFF)));
            textArea.setCaretPosition(textArea.getDocument().getLength());
        }

        @Override
        public void write(byte[] b, int off, int len) {
            try {
                // Giải mã chính xác dữ liệu mảng byte thành chuỗi UTF-8 tiếng Việt
                String text = new String(b, off, len, "UTF-8");
                SwingUtilities.invokeLater(() -> {
                    textArea.append(text);
                    textArea.setCaretPosition(textArea.getDocument().getLength());
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
