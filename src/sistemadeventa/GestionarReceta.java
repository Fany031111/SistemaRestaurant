/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package sistemadeventa;

import Conexion.Conexion;
import java.awt.Color;
import javax.swing.table.DefaultTableModel;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import static sistemadeventa.GestionarProductos.tproducto;

/**
 *
 * @author Fanny
 */
public class GestionarReceta extends javax.swing.JFrame {

    /**
     * Creates new form GestionarReceta
     */
    public static Conexion objeto = new Conexion();
    public static DefaultTableModel modelo, modelo2;

    public GestionarReceta() {
        initComponents();
        this.setSize(new Dimension(1040, 640));

        this.setLocationRelativeTo(null);
        this.setTitle("Gestionar Receta - SISTEMA DE VENTAS");
        scroll.setOpaque(false); // Hacer transparente el JScrollPane
        scroll.getViewport().setOpaque(false); // Hacer transparente el JViewport
        scroll.setBorder(null);
        scroll1.setOpaque(false); // Hacer transparente el JScrollPane
        scroll1.getViewport().setOpaque(false); // Hacer transparente el JViewport
        scroll1.setBorder(null);
        txtid.setVisible(false);

        //TITULOS
        int fila = GestionarProductos.tproducto.getSelectedRow();
        txtid.setText((GestionarProductos.tproducto.getValueAt(fila, 0).toString()));
        txtproducto.setText((GestionarProductos.tproducto.getValueAt(fila, 1).toString()));

        try {
            ConsultarDatosIngredientes(fid.getText(), fnombre.getText());
            ConsultarDatosReceta();
        } catch (SQLException ex) {
            Logger.getLogger(GestionarUsuarios.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    static void ConsultarDatosIngredientes(String id, String puesto) throws SQLException {

        PreparedStatement stmt = null;
        ResultSet respuesta = null;
        try {
            // Reutilizar la conexión estática
            Connection cx = objeto.conexionReturn();

            // Preparar la consulta base
            StringBuilder query = new StringBuilder("SELECT tb_inventario.idingrediente,tb_inventario.nombre,"
                    + "tb_inventario.cantidad, tb_unidad.unidad "
                    + "from tb_inventario join tb_unidad on "
                    + "tb_inventario.idunidad=tb_unidad.idunidad WHERE 1=1");
            List<Object> params = new ArrayList<>();

            // Agregar filtros según los parámetros
            if (!id.equals("")) {
                query.append(" AND idingrediente LIKE ?");
                params.add(id + "%");
            }
            if (!puesto.equals("")) {
                query.append(" AND nombre LIKE ?");
                params.add(puesto + "%");
            }

            // Preparar la consulta SQL
            stmt = cx.prepareStatement(query.toString());
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            respuesta = stmt.executeQuery();

            Object[] cliente = new Object[4];
            modelo = (DefaultTableModel) tingre.getModel();
            while (respuesta.next()) {
                cliente[0] = respuesta.getInt(1);
                cliente[1] = respuesta.getString(2);
                cliente[2] = respuesta.getString(3);
                cliente[3] = respuesta.getString(4);

                modelo.addRow(cliente);
            }
            tingre.setModel(modelo);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Cerrar recursos, pero no la conexión
            if (respuesta != null) try {
                respuesta.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (stmt != null) try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    static void ConsultarDatosReceta() throws SQLException {

        PreparedStatement stmt = null;
        ResultSet respuesta = null;
        try {
            // Reutilizar la conexión estática
            Connection cx = objeto.conexionReturn();

            // Preparar la consulta base
            String query = "Select tb_recetario.idingrediente, tb_inventario.nombre, "
                    + "tb_recetario.cantidad from tb_recetario join tb_inventario on tb_recetario.idingrediente="
                    + "tb_inventario.idingrediente where tb_recetario.idproducto=" + txtid.getText();

            // Preparar la consulta SQL
            stmt = cx.prepareStatement(query);

            respuesta = stmt.executeQuery();

            Object[] cliente = new Object[3];
            modelo2 = (DefaultTableModel) treceta.getModel();
            while (respuesta.next()) {
                cliente[0] = respuesta.getInt(1);
                cliente[1] = respuesta.getString(2);
                cliente[2] = respuesta.getString(3);

                modelo2.addRow(cliente);
            }
            treceta.setModel(modelo2);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Cerrar recursos, pero no la conexión
            if (respuesta != null) try {
                respuesta.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (stmt != null) try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    void limpiarDatosTabla() {
        int fila = tingre.getRowCount();
        for (int i = fila - 1; i >= 0; i--) {
            modelo.removeRow(i);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        txtproducto = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        fid = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        fnombre = new javax.swing.JTextField();
        limpiarfiltro = new javax.swing.JButton();
        scroll1 = new javax.swing.JScrollPane();
        tingre = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        scroll = new javax.swing.JScrollPane();
        treceta = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        txtid = new javax.swing.JLabel();
        fondo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Asignar ingredientes para el producto:");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 20, 460, 30));

        txtproducto.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        txtproducto.setForeground(new java.awt.Color(255, 255, 255));
        txtproducto.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        getContentPane().add(txtproducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 60, 710, 40));

        jPanel1.setBackground(new java.awt.Color(0, 153, 204));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Filtrar ingredientes por:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        fid.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        fid.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                fidKeyReleased(evt);
            }
        });
        jPanel1.add(fid, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 30, 40, 30));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("ID:");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, -1, 30));

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Ingrediente:");
        jPanel1.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 30, -1, 30));

        fnombre.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        fnombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fnombreActionPerformed(evt);
            }
        });
        fnombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                fnombreKeyReleased(evt);
            }
        });
        jPanel1.add(fnombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 30, 170, 30));

        limpiarfiltro.setBackground(new java.awt.Color(255, 204, 204));
        limpiarfiltro.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        limpiarfiltro.setText("x");
        limpiarfiltro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                limpiarfiltroActionPerformed(evt);
            }
        });
        jPanel1.add(limpiarfiltro, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 30, 40, 30));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, 430, 80));

        tingre.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tingre.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Ingrediente", "Existencias", "Unidad"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tingre.setRowHeight(30);
        tingre.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tingreMouseClicked(evt);
            }
        });
        scroll1.setViewportView(tingre);

        getContentPane().add(scroll1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 210, 430, 330));

        jButton1.setBackground(new java.awt.Color(204, 255, 204));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton1.setText(">>");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 310, 70, 60));

        jButton4.setBackground(new java.awt.Color(255, 204, 204));
        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton4.setText("<<");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 380, 70, 60));

        treceta.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        treceta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Ingrediente", "Cantidad"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        treceta.setRowHeight(30);
        treceta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                trecetaMouseClicked(evt);
            }
        });
        scroll.setViewportView(treceta);

        getContentPane().add(scroll, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 210, 430, 330));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Digite la cantidad del ingrediente a utilizar:");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 130, 430, 30));

        jButton2.setBackground(new java.awt.Color(255, 204, 204));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton2.setText("Cancelar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 560, 90, 30));

        jButton3.setBackground(new java.awt.Color(204, 255, 204));
        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton3.setText("Guardar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 560, 90, 30));
        getContentPane().add(txtid, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 50, 200, 40));

        fondo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/fondo.jpg"))); // NOI18N
        getContentPane().add(fondo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1250, 650));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void fidKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fidKeyReleased
        // TODO add your handling code here:
        try {
            limpiarDatosTabla();
            ConsultarDatosIngredientes(fid.getText(), fnombre.getText());
        } catch (SQLException ex) {
            Logger.getLogger(GestionarUsuarios.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_fidKeyReleased

    private void fnombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fnombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fnombreActionPerformed

    private void fnombreKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fnombreKeyReleased
        // TODO add your handling code here:
        try {
            limpiarDatosTabla();
            ConsultarDatosIngredientes(fid.getText(), fnombre.getText());
        } catch (SQLException ex) {
            Logger.getLogger(GestionarUsuarios.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_fnombreKeyReleased

    private void limpiarfiltroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_limpiarfiltroActionPerformed
        // TODO add your handling code here:
        fnombre.setText("");
        fid.setText("");
    }//GEN-LAST:event_limpiarfiltroActionPerformed

    private void tingreMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tingreMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tingreMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        int fila = tingre.getSelectedRow();
        modelo2 = (DefaultTableModel) treceta.getModel();
        modelo2.addRow(new Object[]{
            tingre.getValueAt(fila, 0).toString(),
            tingre.getValueAt(fila, 1).toString()});
        treceta.setModel(modelo2);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        int fila = treceta.getSelectedRow();
        modelo2 = (DefaultTableModel) treceta.getModel();
        modelo2.removeRow(fila);
        treceta.setModel(modelo2);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void trecetaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_trecetaMouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_trecetaMouseClicked

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        int a = JOptionPane.YES_NO_OPTION;
        int resul = JOptionPane.showConfirmDialog(this, "¿Desea actualizar la receta?", "¿Confirmar?", a);
        if (resul == 0) {

            //AGREGA LOS INGREDIENTES EN TB_RECETARIO
            try {
                //obtener idproducto
                int idproduc = 0;
                ResultSet respuesta = objeto.consultarRegistros("Select idproducto from tb_producto where nombre='"
                        + txtproducto.getText() + "'");
                respuesta.next();
                idproduc = (respuesta.getInt("idproducto"));
                //recorro la tabla y agrego fila por fila
                objeto.eliminarReceta(idproduc);
                if (modelo2.getRowCount() != 0) {
                    for (int i = 0; i < modelo2.getRowCount(); i++) {
                        try {
                            objeto.agregarReceta(idproduc,
                                    Integer.parseInt(modelo2.getValueAt(i, 0).toString()),
                                    Integer.parseInt(modelo2.getValueAt(i, 2).toString()));
                        } catch (SQLException ex) {
                            Logger.getLogger(AsignarIngredientes.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(AsignarIngredientes.class.getName()).log(Level.SEVERE, null, ex);
            }
            GestionarProductos.limpiarDatos();
            this.dispose();

        }

    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        this.dispose();

    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GestionarReceta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GestionarReceta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GestionarReceta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GestionarReceta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GestionarReceta().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField fid;
    private javax.swing.JTextField fnombre;
    private javax.swing.JLabel fondo;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton limpiarfiltro;
    private static javax.swing.JScrollPane scroll;
    private static javax.swing.JScrollPane scroll1;
    public static javax.swing.JTable tingre;
    public static javax.swing.JTable treceta;
    public static javax.swing.JLabel txtid;
    private javax.swing.JLabel txtproducto;
    // End of variables declaration//GEN-END:variables
}
