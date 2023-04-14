import java.awt.Graphics;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Font;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AdminPanel extends javax.swing.JFrame {
        private AdminPanelListener listener;
        List<DroneDot> DroneDotList = new ArrayList<>();
        List<FireDot> FireDotList = new ArrayList<>();

        public void setListener(AdminPanelListener listener) {
                this.listener = listener;
        }

        public void printText(String txt) {
                jTextField1.setText("[~]  " + txt);
        }

        private void UpdateMap() {

                FireDotList.clear();
                DroneDotList.clear();
                try {
                        File myObj = new File("./data/fires.csv");
                        Scanner myReader = new Scanner(myObj);

                        // String header = myReader.nextLine();

                        while (myReader.hasNextLine()) {
                                String line = myReader.nextLine();
                                String[] fireData = line.split(",");

                                int X = Integer.parseInt(fireData[1]);
                                int Y = Integer.parseInt(fireData[2]);
                                int Id = Integer.parseInt(fireData[0]);

                                FireDot fireDot = new FireDot(X, Y, Id);

                                FireDotList.add(fireDot);
                        }

                        myReader.close();
                } catch (FileNotFoundException e) {
                        System.out.println("An error occurred.");
                        e.printStackTrace();
                }
                try {
                        RandomAccessFile raf = new RandomAccessFile("./data/drones.bin", "r");
                        int droneCount = (int) raf.length() / 12;

                        for (int i = 0; i < droneCount; i++) {
                                int id = raf.readInt();
                                int x = raf.readInt();
                                int y = raf.readInt();

                                DroneDot droneDot = new DroneDot(x, y, id);

                                DroneDotList.add(droneDot);
                        }
                        raf.close();

                } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("An error occurred.");
                }

                map.repaint();
        }

        public AdminPanel() {
                initComponents();

                Runnable helloRunnable = new Runnable() {
                        public void run() {
                                UpdateMap();
                        }
                };
                ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
                executor.scheduleAtFixedRate(helloRunnable, 0, 2, TimeUnit.SECONDS);

        }

        @SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated Code">
        private void initComponents() {

                map = new javax.swing.JPanel() {
                        Image backgroundImage = new ImageIcon("data/map-background.png").getImage();

                        @Override
                        protected void paintComponent(Graphics g) {
                                super.paintComponent(g);

                                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

                                map.setPreferredSize(new Dimension(backgroundImage.getWidth(null),
                                                backgroundImage.getHeight(null)));

                                for (DroneDot droneDot : DroneDotList) {

                                        System.out.println(droneDot.toString());

                                        g.setColor(Color.BLUE);
                                        g.fillRect(droneDot.getX(), droneDot.getY(), 20, 20);
                                        g.setColor(Color.RED);
                                        g.setFont(new Font("Arial", Font.PLAIN, 10));
                                        g.drawString("Drone " + droneDot.getID(), droneDot.getX(),
                                                        droneDot.getY());

                                }

                                for (FireDot fireDot : FireDotList) {
                                        g.setColor(Color.GREEN);
                                        g.fillRect(fireDot.getX(), fireDot.getY(), 20, 20);
                                        g.setColor(Color.RED);
                                        g.setFont(new Font("Arial", Font.PLAIN, 10));
                                        g.drawString("Fire " + fireDot.getID(), fireDot.getX(), fireDot.getY());
                                }

                        }

                };
                control = new javax.swing.JPanel();
                btnRecall = new javax.swing.JButton();
                btnStopServer = new javax.swing.JButton();
                txtXCordinate = new javax.swing.JTextField();
                txtYCordinate = new javax.swing.JTextField();
                jLabel1 = new javax.swing.JLabel();
                jLabel2 = new javax.swing.JLabel();
                btnChangeCordinates = new javax.swing.JButton();
                txtDroneId = new javax.swing.JTextField();
                jLabel3 = new javax.swing.JLabel();
                btnDeleteFireReport = new javax.swing.JButton();
                txtFireId = new javax.swing.JTextField();
                jLabel4 = new javax.swing.JLabel();
                console = new javax.swing.JPanel();

                jTextField1 = new javax.swing.JTextField();

                setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
                setResizable(false);
                setSize(new java.awt.Dimension(100, 100));

                javax.swing.GroupLayout mapLayout = new javax.swing.GroupLayout(map);
                map.setLayout(mapLayout);
                mapLayout.setHorizontalGroup(
                                mapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGap(0, 546, Short.MAX_VALUE));
                mapLayout.setVerticalGroup(
                                mapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGap(0, 540, Short.MAX_VALUE));

                control.setBackground(new java.awt.Color(204, 204, 204));

                btnRecall.setText("Recall Drones");
                btnRecall.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btnRecallActionPerformed(evt);
                        }
                });

                btnStopServer.setText("Stop Server");
                btnStopServer.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btnStopServerActionPerformed(evt);
                        }
                });

                jLabel1.setText("     X");

                jLabel2.setText("     Y");

                btnChangeCordinates.setText("Change coordinates");
                btnChangeCordinates.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btnChangeCordinatesActionPerformed(evt);
                        }
                });

                jLabel3.setText("     Drone ID");

                btnDeleteFireReport.setText("Delete Fire Report");
                btnDeleteFireReport.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btnDeleteFireReportActionPerformed(evt);
                        }
                });

                jLabel4.setText("     Fire ID");

                javax.swing.GroupLayout controlLayout = new javax.swing.GroupLayout(control);
                control.setLayout(controlLayout);
                controlLayout.setHorizontalGroup(
                                controlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(controlLayout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addGroup(controlLayout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(btnChangeCordinates,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE)
                                                                                .addGroup(controlLayout
                                                                                                .createSequentialGroup()
                                                                                                .addGroup(controlLayout
                                                                                                                .createParallelGroup(
                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                                .addGroup(controlLayout
                                                                                                                                .createSequentialGroup()
                                                                                                                                .addGroup(controlLayout
                                                                                                                                                .createParallelGroup(
                                                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                                                                .addComponent(jLabel1,
                                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                                36,
                                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                                                .addComponent(jLabel2,
                                                                                                                                                                javax.swing.GroupLayout.Alignment.TRAILING,
                                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                                36,
                                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                                                                .addGap(0, 0, Short.MAX_VALUE))
                                                                                                                .addComponent(jLabel3,
                                                                                                                                javax.swing.GroupLayout.Alignment.TRAILING,
                                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                Short.MAX_VALUE))
                                                                                                .addGap(18, 18, 18)
                                                                                                .addGroup(controlLayout
                                                                                                                .createParallelGroup(
                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                                .addComponent(txtDroneId)
                                                                                                                .addComponent(txtXCordinate)
                                                                                                                .addComponent(txtYCordinate,
                                                                                                                                javax.swing.GroupLayout.Alignment.TRAILING)))
                                                                                .addComponent(btnStopServer,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE)
                                                                                .addComponent(btnRecall,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE)
                                                                                .addComponent(btnDeleteFireReport,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE)
                                                                                .addGroup(controlLayout
                                                                                                .createSequentialGroup()
                                                                                                .addComponent(jLabel4,
                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                121,
                                                                                                                Short.MAX_VALUE)
                                                                                                .addGap(18, 18, 18)
                                                                                                .addComponent(txtFireId,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                112,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                                .addContainerGap()));
                controlLayout.setVerticalGroup(
                                controlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(controlLayout.createSequentialGroup()
                                                                .addGap(15, 15, 15)
                                                                .addComponent(btnRecall)
                                                                .addGap(15, 15, 15)
                                                                .addComponent(btnStopServer)
                                                                .addGap(36, 36, 36)
                                                                .addGroup(controlLayout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(txtDroneId,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(jLabel3))
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(controlLayout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(txtXCordinate,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(jLabel1))
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addGroup(controlLayout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(txtYCordinate,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(jLabel2))
                                                                .addGap(18, 18, 18)
                                                                .addComponent(btnChangeCordinates)
                                                                .addGap(51, 51, 51)
                                                                .addGroup(controlLayout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(txtFireId,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(jLabel4))
                                                                .addGap(18, 18, 18)
                                                                .addComponent(btnDeleteFireReport)
                                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)));

                javax.swing.GroupLayout consoleLayout = new javax.swing.GroupLayout(console);
                console.setLayout(consoleLayout);
                consoleLayout.setHorizontalGroup(
                                consoleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(consoleLayout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addComponent(jTextField1,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                819,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)));
                consoleLayout.setVerticalGroup(
                                consoleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, consoleLayout
                                                                .createSequentialGroup()
                                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                .addComponent(jTextField1,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                137,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addContainerGap()));

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
                getContentPane().setLayout(layout);
                layout.setHorizontalGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addComponent(control,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(map, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                .addGap(18, 18, 18))
                                                .addComponent(console, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
                layout.setVerticalGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                                .addGroup(layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(map,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addGroup(layout.createSequentialGroup()
                                                                                                .addContainerGap()
                                                                                                .addComponent(control,
                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                Short.MAX_VALUE)))
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(console,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)));

                pack();
        }// </editor-fold>

        private void btnRecallActionPerformed(java.awt.event.ActionEvent evt) {

                if (listener != null) {
                        listener.btnRecallClicked();
                }
        }

        private void btnStopServerActionPerformed(java.awt.event.ActionEvent evt) {
                if (listener != null) {
                        listener.btnStopServerClicked();
                }
        }

        private void btnChangeCordinatesActionPerformed(java.awt.event.ActionEvent evt) {
                if (listener != null) {
                        int X = Integer.parseInt(txtXCordinate.getText());
                        int Y = Integer.parseInt(txtYCordinate.getText());
                        int DroneId = Integer.parseInt(txtDroneId.getText());
                        listener.btnChangeCordinatesClicked(X, Y, DroneId);
                }
        }

        private void btnDeleteFireReportActionPerformed(java.awt.event.ActionEvent evt) {
                if (listener != null) {

                        // try {
                        int fireId = Integer.parseInt(txtFireId.getText());
                        listener.btnDeleteFireReportClicked(fireId);
                        // } catch (Exception e) {
                        // System.out.println("Invalid ID");
                        // }

                }
        }

        /**
         * @param args the command line arguments
         */
        public static void main(String args[]) {
                /* Set the Nimbus look and feel */
                // <editor-fold defaultstate="collapsed" desc=" Look and feel setting code
                // (optional) ">
                /*
                 * If Nimbus (introduced in Java SE 6) is not available, stay with the default
                 * look and feel.
                 * For details see
                 * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
                 */
                try {
                        for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager
                                        .getInstalledLookAndFeels()) {
                                if ("Nimbus".equals(info.getName())) {
                                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                                        break;
                                }
                        }
                } catch (ClassNotFoundException ex) {
                        java.util.logging.Logger.getLogger(AdminPanel.class.getName())
                                        .log(java.util.logging.Level.SEVERE, null, ex);
                } catch (InstantiationException ex) {
                        java.util.logging.Logger.getLogger(AdminPanel.class.getName())
                                        .log(java.util.logging.Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                        java.util.logging.Logger.getLogger(AdminPanel.class.getName())
                                        .log(java.util.logging.Level.SEVERE, null, ex);
                } catch (javax.swing.UnsupportedLookAndFeelException ex) {
                        java.util.logging.Logger.getLogger(AdminPanel.class.getName())
                                        .log(java.util.logging.Level.SEVERE, null, ex);
                }
                // </editor-fold>

                /* Create and display the form */
                java.awt.EventQueue.invokeLater(new Runnable() {
                        public void run() {
                                new AdminPanel().setVisible(true);
                        }
                });
        }

        // Variables declaration - do not modify
        private javax.swing.JButton btnChangeCordinates;
        private javax.swing.JButton btnDeleteFireReport;
        private javax.swing.JButton btnRecall;
        private javax.swing.JButton btnStopServer;
        private javax.swing.JPanel console;
        private javax.swing.JPanel control;
        private javax.swing.JLabel jLabel1;
        private javax.swing.JLabel jLabel2;
        private javax.swing.JLabel jLabel3;
        private javax.swing.JLabel jLabel4;
        private javax.swing.JTextField jTextField1;
        private javax.swing.JPanel map;
        private javax.swing.JTextField txtDroneId;
        private javax.swing.JTextField txtFireId;
        private javax.swing.JTextField txtXCordinate;
        private javax.swing.JTextField txtYCordinate;
        // End of variables declaration
}
