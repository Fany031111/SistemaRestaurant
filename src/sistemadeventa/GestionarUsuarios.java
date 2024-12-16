/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package sistemadeventa;

import Conexion.Conexion;
import java.awt.Dimension;
import javax.swing.table.DefaultTableModel;
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
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Fanny
 */
public class GestionarUsuarios extends javax.swing.JFrame implements Runnable {

    /**
     * Creates new form GestionarUsuarios
     */
    public static Conexion objeto = new Conexion();
    public static DefaultTableModel modelo;
    static String rutaFoto;

    public GestionarUsuarios() {
        initComponents();
                this.setSize(new Dimension(1200, 740));

        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setTitle("Gestionar Usuarios - SISTEMA DE VENTAS");
        this.ocultar.setVisible(false);
        scroll.setOpaque(false); // Hacer transparente el JScrollPane
        scroll.getViewport().setOpaque(false); // Hacer transparente el JViewport
        scroll.setBorder(null);

        try {

            ConsultarDatos(fid.getText(), fnombre.getText(), fape.getText(), fpuesto.getSelectedItem().toString());

        } catch (SQLException ex) {
            Logger.getLogger(GestionarUsuarios.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            ResultSet respuesta = objeto.consultarRegistros("SELECT tipo from tb_tipousuario");
            while (respuesta.next()) {
                cbxtipo.addItem(respuesta.getString(1));
                fpuesto.addItem(respuesta.getString(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(AgregarUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
        h1 = new Thread((Runnable) this);
        h1.start();
        //fecha
        Calendar fecha = Calendar.getInstance();
        int año = fecha.get(Calendar.YEAR);
        int mes = fecha.get(Calendar.MONTH) + 1;
        int dia = fecha.get(Calendar.DAY_OF_MONTH);
        if (mes < 10 && dia < 10) {
            fechalbl.setText("0" + String.valueOf(dia) + "-0" + String.valueOf(mes) + "-" + String.valueOf(año));
        } else if (mes < 10) {
            fechalbl.setText(String.valueOf(dia) + "-0" + String.valueOf(mes) + "-" + String.valueOf(año));
        } else if (dia < 10) {
            fechalbl.setText("0" + String.valueOf(dia) + "-" + String.valueOf(mes) + "-" + String.valueOf(año));
        } else {
            fechalbl.setText(String.valueOf(dia) + "-" + String.valueOf(mes) + "-" + String.valueOf(año));
        }
        //termina fecha
        //termina hora

    }
    static int hora, minutos, segundos;
    Calendar calendario;
    static Thread h1;

    static public void calcula() {
        Calendar calendario = new GregorianCalendar();
        hora = calendario.get(Calendar.HOUR_OF_DAY);
        hora = hora;
        minutos = calendario.get(Calendar.MINUTE);
        segundos = calendario.get(Calendar.SECOND);
    }

    @Override
    public void run() {
        Thread ct = Thread.currentThread();
        while (ct == h1) {
            calcula();
            if (hora < 10 && minutos < 10 && segundos < 10) {
                horalbl.setText("0" + hora + ":0" + minutos + ":0" + segundos);
            } else if (hora < 10 && minutos < 10) {
                horalbl.setText("0" + hora + ":0" + minutos + ":" + segundos);
            } else if (hora < 10 && segundos < 10) {
                horalbl.setText("0" + hora + ":" + minutos + ":0" + segundos);
            } else if (minutos < 10 && segundos < 10) {
                horalbl.setText(hora + ":0" + minutos + ":0" + segundos);
            } else if (hora < 10) {
                horalbl.setText("0" + hora + ":" + minutos + ":" + segundos);
            } else if (minutos < 10) {
                horalbl.setText(hora + ":0" + minutos + ":" + segundos);
            } else if (segundos < 10) {
                horalbl.setText(hora + ":" + minutos + ":0" + segundos);
            } else {
                horalbl.setText(hora + ":" + minutos + ":" + segundos);
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
        }
    }

    static void ConsultarDatos(String id, String nombre, String ape, String puesto) throws SQLException {

        PreparedStatement stmt = null;
        ResultSet respuesta = null;
        try {
            // Reutilizar la conexión estática
            Connection cx = objeto.conexionReturn();

            // Preparar la consulta base
            StringBuilder query = new StringBuilder("SELECT * FROM vista_usuario WHERE 1=1");
            List<Object> params = new ArrayList<>();

            // Agregar filtros según los parámetros
            if (!id.equals("")) {
                query.append(" AND idusuario LIKE ?");
                params.add(id + "%");
            }
            if (!nombre.equals("")) {
                query.append(" AND nombre LIKE ?");
                params.add(nombre + "%");
            }
            if (!ape.equals("")) {
                query.append(" AND apellido LIKE ?");
                params.add(ape + "%");
            }
            if (!puesto.equals("Todos")) {
                query.append(" AND tipo = ?");
                params.add(puesto);
            }

            // Preparar la consulta SQL
            stmt = cx.prepareStatement(query.toString());
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            respuesta = stmt.executeQuery();

            // Modelo para la tabla de aluminio
            Object[] usuarios = new Object[8];
            modelo = (DefaultTableModel) tusuarios.getModel();
            while (respuesta.next()) {
                usuarios[0] = respuesta.getInt(1);
                usuarios[1] = respuesta.getString(2);
                usuarios[2] = respuesta.getString(3);
                usuarios[3] = respuesta.getString(4);
                usuarios[4] = respuesta.getString(5);
                usuarios[5] = respuesta.getString(6);
                usuarios[6] = respuesta.getString(7);
                usuarios[7] = respuesta.getString(8);

                modelo.addRow(usuarios);
            }
            tusuarios.setModel(modelo);

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

    void limpiarDatos() {
        txtid.setText("");
        txtnombre.setText("");
        txtape.setText("");
        txtusuario.setText("");
        txtcontra.setText("");
        txttelefono.setText("");
        cbxtipo.setSelectedIndex(0);

        lfoto.setIcon(null);

        rutaFoto = "";
    }

    void limpiarDatosTabla() {
        int fila = tusuarios.getRowCount();
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

        scroll = new javax.swing.JScrollPane();
        tusuarios = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        lfoto = new javax.swing.JLabel();
        subirfoto = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtid = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtape = new javax.swing.JTextField();
        txttelefono = new javax.swing.JTextField();
        txtnombre = new javax.swing.JTextField();
        cbxtipo = new javax.swing.JComboBox<>();
        btneliminar = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        txtusuario = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtcontra = new javax.swing.JPasswordField();
        ocultar = new javax.swing.JLabel();
        ver = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        fid = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        fape = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        fnombre = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        fpuesto = new javax.swing.JComboBox<>();
        limpiarfiltro = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        btnactualizar = new javax.swing.JButton();
        btnlimpiar = new javax.swing.JButton();
        fechalbl = new javax.swing.JLabel();
        horalbl = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        fondo = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem_nuevo_usuario = new javax.swing.JMenuItem();
        jMenuItem_gestionar_usuario = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem_nuevo_producto = new javax.swing.JMenuItem();
        jMenuItem_gestionar_producto = new javax.swing.JMenuItem();
        jMenuItem_actualizar_stock = new javax.swing.JMenuItem();
        jMenuItem_actualizar_stock1 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem_nuevo_cliente = new javax.swing.JMenuItem();
        jMenuItem_gestionar_cliente = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem_nueva_categoria = new javax.swing.JMenuItem();
        jMenuItem_gestionar_categoria = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem_nueva_venta = new javax.swing.JMenuItem();
        jMenuItem_gestionar_ventas = new javax.swing.JMenuItem();
        jMenu6 = new javax.swing.JMenu();
        jMenuItem_reportes_clientes = new javax.swing.JMenuItem();
        jMenuItem_reportes_productos = new javax.swing.JMenuItem();
        jMenuItem_reportes_ventas = new javax.swing.JMenuItem();
        jMenu7 = new javax.swing.JMenu();
        jMenuItem_ver_historial = new javax.swing.JMenuItem();
        jMenu8 = new javax.swing.JMenu();
        jMenuItem_cerrar_sesion = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tusuarios.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tusuarios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nombre(s)", "Apellidos", "Usuario", "Contraseña", "Teléfono", "Puesto", "rutaFoto"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tusuarios.setRowHeight(30);
        tusuarios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tusuariosMouseClicked(evt);
            }
        });
        scroll.setViewportView(tusuarios);
        if (tusuarios.getColumnModel().getColumnCount() > 0) {
            tusuarios.getColumnModel().getColumn(0).setMinWidth(50);
            tusuarios.getColumnModel().getColumn(0).setPreferredWidth(50);
            tusuarios.getColumnModel().getColumn(0).setMaxWidth(50);
            tusuarios.getColumnModel().getColumn(5).setMinWidth(100);
            tusuarios.getColumnModel().getColumn(5).setPreferredWidth(100);
            tusuarios.getColumnModel().getColumn(5).setMaxWidth(100);
            tusuarios.getColumnModel().getColumn(6).setMinWidth(100);
            tusuarios.getColumnModel().getColumn(6).setPreferredWidth(100);
            tusuarios.getColumnModel().getColumn(6).setMaxWidth(100);
            tusuarios.getColumnModel().getColumn(7).setMinWidth(0);
            tusuarios.getColumnModel().getColumn(7).setPreferredWidth(0);
            tusuarios.getColumnModel().getColumn(7).setMaxWidth(0);
        }

        getContentPane().add(scroll, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 180, 850, 410));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Gestionar Usuarios");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 1200, -1));

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        getContentPane().add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 80, 20, 540));

        lfoto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        lfoto.setPreferredSize(new java.awt.Dimension(250, 250));
        getContentPane().add(lfoto, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 130, 150, 150));

        subirfoto.setBackground(new java.awt.Color(248, 198, 79));
        subirfoto.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        subirfoto.setText("*");
        subirfoto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                subirfotoActionPerformed(evt);
            }
        });
        getContentPane().add(subirfoto, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 160, 40, 40));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Foto de perfil:");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 130, -1, -1));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("ID del usuario:");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 80, -1, 30));

        txtid.setEditable(false);
        txtid.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtid.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        getContentPane().add(txtid, new org.netbeans.lib.awtextra.AbsoluteConstraints(1020, 80, 140, 30));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Apellidos:");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 340, -1, 30));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Teléfono:");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 490, -1, 30));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Puesto:");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 530, -1, 30));

        txtape.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        getContentPane().add(txtape, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 370, 260, 30));

        txttelefono.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txttelefono.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txttelefonoActionPerformed(evt);
            }
        });
        getContentPane().add(txttelefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 490, 170, 30));

        txtnombre.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        getContentPane().add(txtnombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 310, 260, 30));

        cbxtipo.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cbxtipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecciona" }));
        cbxtipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxtipoActionPerformed(evt);
            }
        });
        getContentPane().add(cbxtipo, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 530, 170, 30));

        btneliminar.setBackground(new java.awt.Color(255, 204, 204));
        btneliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/eliminar.png"))); // NOI18N
        btneliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btneliminarActionPerformed(evt);
            }
        });
        getContentPane().add(btneliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 570, 50, 50));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Usuario:");
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 410, -1, 30));

        txtusuario.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtusuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtusuarioActionPerformed(evt);
            }
        });
        getContentPane().add(txtusuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 410, 170, 30));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Contraseña:");
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 450, -1, 30));

        txtcontra.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtcontra.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtcontraKeyReleased(evt);
            }
        });
        getContentPane().add(txtcontra, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 450, 170, 30));

        ocultar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ocultar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/ocultar_32px.png"))); // NOI18N
        ocultar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ocultarMouseClicked(evt);
            }
        });
        getContentPane().add(ocultar, new org.netbeans.lib.awtextra.AbsoluteConstraints(1160, 450, 30, 30));

        ver.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ver.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/ver_32px.png"))); // NOI18N
        ver.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                verMouseClicked(evt);
            }
        });
        getContentPane().add(ver, new org.netbeans.lib.awtextra.AbsoluteConstraints(1160, 450, 30, 30));

        jPanel1.setBackground(new java.awt.Color(0, 153, 204));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Filtrar usuario por:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        fid.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        fid.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                fidKeyReleased(evt);
            }
        });
        jPanel1.add(fid, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 30, 50, 30));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("ID:");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, -1, 30));

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Puesto:");
        jPanel1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 30, -1, 30));

        fape.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        fape.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fapeActionPerformed(evt);
            }
        });
        fape.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                fapeKeyReleased(evt);
            }
        });
        jPanel1.add(fape, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 30, 160, 30));

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Nombre(s):");
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
        jPanel1.add(fnombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 30, 150, 30));

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Apellidos:");
        jPanel1.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 30, -1, 30));

        fpuesto.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        fpuesto.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos" }));
        fpuesto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fpuestoActionPerformed(evt);
            }
        });
        jPanel1.add(fpuesto, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 30, 140, 30));

        limpiarfiltro.setBackground(new java.awt.Color(255, 204, 204));
        limpiarfiltro.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        limpiarfiltro.setText("x");
        limpiarfiltro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                limpiarfiltroActionPerformed(evt);
            }
        });
        jPanel1.add(limpiarfiltro, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 30, 40, 30));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 850, 80));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Nombre(s):");
        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 280, -1, 30));

        btnactualizar.setBackground(new java.awt.Color(255, 255, 204));
        btnactualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/actualizar.png"))); // NOI18N
        btnactualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnactualizarActionPerformed(evt);
            }
        });
        getContentPane().add(btnactualizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 570, 50, 50));

        btnlimpiar.setBackground(new java.awt.Color(204, 255, 255));
        btnlimpiar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/limpiar.png"))); // NOI18N
        btnlimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnlimpiarActionPerformed(evt);
            }
        });
        getContentPane().add(btnlimpiar, new org.netbeans.lib.awtextra.AbsoluteConstraints(1070, 570, 50, 50));

        fechalbl.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        fechalbl.setForeground(new java.awt.Color(255, 255, 255));
        fechalbl.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        getContentPane().add(fechalbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 620, 90, 20));

        horalbl.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        horalbl.setForeground(new java.awt.Color(255, 255, 255));
        horalbl.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        getContentPane().add(horalbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 620, 90, 20));

        jSeparator2.setForeground(new java.awt.Color(255, 255, 255));
        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);
        getContentPane().add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 620, 20, 20));

        fondo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/fondo.jpg"))); // NOI18N
        getContentPane().add(fondo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1250, 650));

        jMenu1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, null, new java.awt.Color(51, 204, 255), null, null));
        jMenu1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/usuario.png"))); // NOI18N
        jMenu1.setText("Usuario");
        jMenu1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jMenu1.setPreferredSize(new java.awt.Dimension(150, 50));

        jMenuItem_nuevo_usuario.setFont(new java.awt.Font("Maku", 1, 16)); // NOI18N
        jMenuItem_nuevo_usuario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/nuevo-cliente.png"))); // NOI18N
        jMenuItem_nuevo_usuario.setText("Nuevo Usuario");
        jMenuItem_nuevo_usuario.setPreferredSize(new java.awt.Dimension(240, 30));
        jMenuItem_nuevo_usuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_nuevo_usuarioActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem_nuevo_usuario);

        jMenuItem_gestionar_usuario.setFont(new java.awt.Font("Maku", 1, 16)); // NOI18N
        jMenuItem_gestionar_usuario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/configuraciones.png"))); // NOI18N
        jMenuItem_gestionar_usuario.setText("Gestionar Usuarios");
        jMenuItem_gestionar_usuario.setPreferredSize(new java.awt.Dimension(240, 30));
        jMenuItem_gestionar_usuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_gestionar_usuarioActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem_gestionar_usuario);

        jMenuItem1.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cliente.png"))); // NOI18N
        jMenuItem1.setText("Gestionar Puestos");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        jMenu2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, null, new java.awt.Color(51, 204, 255), null, null));
        jMenu2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/producto.png"))); // NOI18N
        jMenu2.setText("Producto");
        jMenu2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jMenu2.setPreferredSize(new java.awt.Dimension(150, 50));

        jMenuItem_nuevo_producto.setFont(new java.awt.Font("Maku", 1, 16)); // NOI18N
        jMenuItem_nuevo_producto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/nuevo-producto.png"))); // NOI18N
        jMenuItem_nuevo_producto.setText("Nuevo Producto");
        jMenuItem_nuevo_producto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_nuevo_productoActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem_nuevo_producto);

        jMenuItem_gestionar_producto.setFont(new java.awt.Font("Maku", 1, 16)); // NOI18N
        jMenuItem_gestionar_producto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/anadir.png"))); // NOI18N
        jMenuItem_gestionar_producto.setText("Gestionar Producto");
        jMenuItem_gestionar_producto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_gestionar_productoActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem_gestionar_producto);

        jMenuItem_actualizar_stock.setFont(new java.awt.Font("Maku", 1, 16)); // NOI18N
        jMenuItem_actualizar_stock.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/carrito.png"))); // NOI18N
        jMenuItem_actualizar_stock.setText("Agregar Stock");
        jMenuItem_actualizar_stock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_actualizar_stockActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem_actualizar_stock);

        jMenuItem_actualizar_stock1.setFont(new java.awt.Font("Maku", 1, 16)); // NOI18N
        jMenuItem_actualizar_stock1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/producto.png"))); // NOI18N
        jMenuItem_actualizar_stock1.setText("Gestionar Stock");
        jMenuItem_actualizar_stock1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_actualizar_stock1ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem_actualizar_stock1);

        jMenuBar1.add(jMenu2);

        jMenu3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, null, new java.awt.Color(51, 204, 255), null, null));
        jMenu3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cliente.png"))); // NOI18N
        jMenu3.setText("Clientes");
        jMenu3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jMenu3.setPreferredSize(new java.awt.Dimension(150, 50));

        jMenuItem_nuevo_cliente.setFont(new java.awt.Font("Maku", 1, 16)); // NOI18N
        jMenuItem_nuevo_cliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/nuevo-cliente.png"))); // NOI18N
        jMenuItem_nuevo_cliente.setText("Nuevo Cliente");
        jMenuItem_nuevo_cliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_nuevo_clienteActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem_nuevo_cliente);

        jMenuItem_gestionar_cliente.setFont(new java.awt.Font("Maku", 1, 16)); // NOI18N
        jMenuItem_gestionar_cliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cliente.png"))); // NOI18N
        jMenuItem_gestionar_cliente.setText("Gestionar Clientes");
        jMenuItem_gestionar_cliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_gestionar_clienteActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem_gestionar_cliente);

        jMenuBar1.add(jMenu3);

        jMenu4.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, null, new java.awt.Color(51, 204, 255), null, null));
        jMenu4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/categorias.png"))); // NOI18N
        jMenu4.setText("Categoría");
        jMenu4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jMenu4.setPreferredSize(new java.awt.Dimension(150, 50));

        jMenuItem_nueva_categoria.setFont(new java.awt.Font("Maku", 1, 16)); // NOI18N
        jMenuItem_nueva_categoria.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/nuevo.png"))); // NOI18N
        jMenuItem_nueva_categoria.setText("Nueva Categoría");
        jMenuItem_nueva_categoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_nueva_categoriaActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem_nueva_categoria);

        jMenuItem_gestionar_categoria.setFont(new java.awt.Font("Maku", 1, 16)); // NOI18N
        jMenuItem_gestionar_categoria.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/categorias.png"))); // NOI18N
        jMenuItem_gestionar_categoria.setText("Gestionar Categoría");
        jMenuItem_gestionar_categoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_gestionar_categoriaActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem_gestionar_categoria);

        jMenuBar1.add(jMenu4);

        jMenu5.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, null, new java.awt.Color(51, 204, 255), null, null));
        jMenu5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/carrito.png"))); // NOI18N
        jMenu5.setText("Ventas");
        jMenu5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jMenu5.setMinimumSize(new java.awt.Dimension(120, 50));
        jMenu5.setPreferredSize(new java.awt.Dimension(150, 50));

        jMenuItem_nueva_venta.setFont(new java.awt.Font("Maku", 1, 16)); // NOI18N
        jMenuItem_nueva_venta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/anadir.png"))); // NOI18N
        jMenuItem_nueva_venta.setText("Nueva Venta");
        jMenuItem_nueva_venta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_nueva_ventaActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem_nueva_venta);

        jMenuItem_gestionar_ventas.setFont(new java.awt.Font("Maku", 1, 16)); // NOI18N
        jMenuItem_gestionar_ventas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/configuraciones.png"))); // NOI18N
        jMenuItem_gestionar_ventas.setText("Gestionar Ventas");
        jMenuItem_gestionar_ventas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_gestionar_ventasActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem_gestionar_ventas);

        jMenuBar1.add(jMenu5);

        jMenu6.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, null, new java.awt.Color(51, 204, 255), null, null));
        jMenu6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/reporte1.png"))); // NOI18N
        jMenu6.setText("Reportes");
        jMenu6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jMenu6.setPreferredSize(new java.awt.Dimension(150, 50));

        jMenuItem_reportes_clientes.setFont(new java.awt.Font("Maku", 1, 16)); // NOI18N
        jMenuItem_reportes_clientes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/reporte1.png"))); // NOI18N
        jMenuItem_reportes_clientes.setText("Reportes Clientes");
        jMenuItem_reportes_clientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_reportes_clientesActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem_reportes_clientes);

        jMenuItem_reportes_productos.setFont(new java.awt.Font("Maku", 1, 16)); // NOI18N
        jMenuItem_reportes_productos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/reporte1.png"))); // NOI18N
        jMenuItem_reportes_productos.setText("Reportes Productos");
        jMenuItem_reportes_productos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_reportes_productosActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem_reportes_productos);

        jMenuItem_reportes_ventas.setFont(new java.awt.Font("Maku", 1, 16)); // NOI18N
        jMenuItem_reportes_ventas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/reporte1.png"))); // NOI18N
        jMenuItem_reportes_ventas.setText("Reportes Ventas");
        jMenuItem_reportes_ventas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_reportes_ventasActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem_reportes_ventas);

        jMenuBar1.add(jMenu6);

        jMenu7.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, null, new java.awt.Color(51, 204, 255), null, null));
        jMenu7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/historial1.png"))); // NOI18N
        jMenu7.setText("Historial");
        jMenu7.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jMenu7.setPreferredSize(new java.awt.Dimension(150, 50));

        jMenuItem_ver_historial.setFont(new java.awt.Font("Maku", 1, 16)); // NOI18N
        jMenuItem_ver_historial.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/historial1.png"))); // NOI18N
        jMenuItem_ver_historial.setText("Ver Historial");
        jMenuItem_ver_historial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_ver_historialActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem_ver_historial);

        jMenuBar1.add(jMenu7);

        jMenu8.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, null, new java.awt.Color(51, 204, 255), null, null));
        jMenu8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cerrar-sesion.png"))); // NOI18N
        jMenu8.setText("Cerrar Sesión");
        jMenu8.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jMenu8.setPreferredSize(new java.awt.Dimension(150, 50));

        jMenuItem_cerrar_sesion.setFont(new java.awt.Font("Maku", 1, 16)); // NOI18N
        jMenuItem_cerrar_sesion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cerrar-sesion.png"))); // NOI18N
        jMenuItem_cerrar_sesion.setText("Cerrar Sesion");
        jMenuItem_cerrar_sesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_cerrar_sesionActionPerformed(evt);
            }
        });
        jMenu8.add(jMenuItem_cerrar_sesion);

        jMenuBar1.add(jMenu8);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void subirfotoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_subirfotoActionPerformed
        // TODO add your handling code here:
        rutaFoto = "";
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        Component parent = null;
        int returnVal = chooser.showSaveDialog(parent);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String selectPath = chooser.getSelectedFile().getPath();
            //System.out.println("El directorio que elija es:" + selectPath);
            rutaFoto = selectPath.replaceAll("\\\\", "\\\\\\\\");

            // Cargar y redimensionar la imagen
            ImageIcon originalIcon = new ImageIcon(rutaFoto);
            Image originalImage = originalIcon.getImage();
            Image resizedImage = originalImage.getScaledInstance(250, 250, Image.SCALE_SMOOTH);

            // Establecer el nuevo icono redimensionado al JLabel
            lfoto.setIcon(new ImageIcon(resizedImage));
        }
    }//GEN-LAST:event_subirfotoActionPerformed

    private void txttelefonoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txttelefonoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txttelefonoActionPerformed

    private void cbxtipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxtipoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxtipoActionPerformed

    private void btneliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btneliminarActionPerformed
        // TODO add your handling code here:
        if ("".equals(txtid.getText())) {
            JOptionPane.showMessageDialog(null, "Error: no se ha seleccionado un usuario.");
        } else {
            // Validar si el ID es numérico y existe en la base de datos
            try {
                int idusuario = Integer.parseInt(txtid.getText());

                // Verificar si el usuario existe antes de proceder
                ResultSet respuesta = objeto.consultarRegistros("SELECT idusuario FROM tb_usuario WHERE idusuario = " + idusuario);
                if (respuesta.next()) {
                    // Llamar al método eliminarUsuario
                    objeto.eliminarUsuario(idusuario);
                    limpiarDatos();
                } else {
                    JOptionPane.showMessageDialog(null, "El usuario con ID " + idusuario + " no existe.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Error: El ID de usuario debe ser un número válido.");
            } catch (SQLException ex) {
                Logger.getLogger(GestionarUsuarios.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            limpiarDatosTabla();
            ConsultarDatos(fid.getText(), fnombre.getText(), fape.getText(), fpuesto.getSelectedItem().toString());
        } catch (SQLException ex) {
            Logger.getLogger(GestionarUsuarios.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_btneliminarActionPerformed

    private void txtusuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtusuarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtusuarioActionPerformed

    private void txtcontraKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtcontraKeyReleased
        // TODO add your handling code here:
        char[] passwordChars = txtcontra.getPassword();
        String password = new String(passwordChars);

        if (password.length() >= 8
                && password.matches(".*[A-Z].*") // Contiene al menos una mayúscula
                && password.matches(".*\\d.*")) { // Contiene al menos un número

        } else {

        }
    }//GEN-LAST:event_txtcontraKeyReleased

    private void ocultarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ocultarMouseClicked
        // TODO add your handling code here:
        ver.setVisible(true);
        ocultar.setVisible(false);
        txtcontra.setEchoChar('*');
    }//GEN-LAST:event_ocultarMouseClicked

    private void verMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_verMouseClicked
        // TODO add your handling code here:
        ver.setVisible(false);
        ocultar.setVisible(true);
        txtcontra.setEchoChar((char) 0);
    }//GEN-LAST:event_verMouseClicked

    private void fapeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fapeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fapeActionPerformed

    private void fnombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fnombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fnombreActionPerformed

    private void btnactualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnactualizarActionPerformed
        // TODO add your handling code here:
        if ("".equals(txtnombre.getText())
                || "".equals(txtape.getText())
                || "".equals(txtusuario.getText())
                || "".equals(txtcontra.getText())
                || "".equals(txttelefono.getText())
                || cbxtipo.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(null, "Error: llene los espacios correspondientes.");
        } else if (!txttelefono.getText().matches("^\\+?\\d{10,15}$")) {
            JOptionPane.showMessageDialog(null, "Error: ingrese un número de teléfono válido.");
        } else if (rutaFoto == null || rutaFoto.isEmpty()) {
            ResultSet respuesta2 = objeto.consultarRegistros("Select usuario from tb_usuario where usuario='" + txtusuario.getText() + "'");
            try {
                if (!respuesta2.next()) {
                    JOptionPane.showMessageDialog(null, "El usuario no existe.");
                } else {
                    int tipousuario = 0;
                    ResultSet respuesta = objeto.consultarRegistros("Select idtipousuario from tb_tipousuario where tipo='" + cbxtipo.getSelectedItem().toString() + "'");
                    respuesta.next();
                    tipousuario = (respuesta.getInt("idtipousuario"));

                    // Verificar si la contraseña es válida antes de proceder
                    char[] passwordChars = txtcontra.getPassword();
                    String password = new String(passwordChars);

                    if (password.length() >= 8
                            && password.matches(".*[A-Z].*")
                            && password.matches(".*\\d.*")) {
                        // Si no se ha especificado una nueva foto, mantenemos la foto predeterminada
                        if (rutaFoto == null || rutaFoto.isEmpty()) {
                            rutaFoto = getClass().getResource("/Imagenes/perfil-de-usuario.png").getPath();
                        }

                        // Usar txtid directamente
                        int idusuario = Integer.parseInt(txtid.getText());

                        // Actualizar los datos del usuario
                        objeto.actualizarUsuario(idusuario,
                                txtnombre.getText(),
                                txtape.getText(),
                                txtusuario.getText(),
                                password,
                                txttelefono.getText(),
                                tipousuario,
                                rutaFoto); // Foto actualizada o mantenida
                        limpiarDatos();
                    } else {
                        JOptionPane.showMessageDialog(null, "La contraseña debe contener al menos 8 caracteres, una mayúscula y un número.");
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(GestionarUsuarios.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (rutaFoto != null) {
            ResultSet respuesta2 = objeto.consultarRegistros("Select usuario from tb_usuario where usuario='" + txtusuario.getText() + "'");
            try {
                if (!respuesta2.next()) {
                    JOptionPane.showMessageDialog(null, "El usuario no existe.");
                } else {
                    int tipousuario = 0;
                    ResultSet respuesta = objeto.consultarRegistros("Select idtipousuario from tb_tipousuario where tipo='" + cbxtipo.getSelectedItem().toString() + "'");
                    respuesta.next();
                    tipousuario = (respuesta.getInt("idtipousuario"));

                    char[] passwordChars = txtcontra.getPassword();
                    String password = new String(passwordChars);

                    if (password.length() >= 8
                            && password.matches(".*[A-Z].*")
                            && password.matches(".*\\d.*")) {
                        // Usar txtid directamente
                        int idusuario = Integer.parseInt(txtid.getText());

                        // Actualizar los datos del usuario
                        objeto.actualizarUsuario(idusuario,
                                txtnombre.getText(),
                                txtape.getText(),
                                txtusuario.getText(),
                                password,
                                txttelefono.getText(),
                                tipousuario,
                                rutaFoto); // Foto actualizada
                        limpiarDatos();
                    } else {
                        JOptionPane.showMessageDialog(null, "La contraseña debe contener al menos 8 caracteres, una mayúscula y un número.");
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(GestionarUsuarios.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            limpiarDatosTabla();
            ConsultarDatos(fid.getText(), fnombre.getText(), fape.getText(), fpuesto.getSelectedItem().toString());
        } catch (SQLException ex) {
            Logger.getLogger(GestionarUsuarios.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_btnactualizarActionPerformed

    private void fidKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fidKeyReleased
        // TODO add your handling code here:
        try {
            limpiarDatosTabla();
            ConsultarDatos(fid.getText(), fnombre.getText(), fape.getText(), fpuesto.getSelectedItem().toString());
        } catch (SQLException ex) {
            Logger.getLogger(GestionarUsuarios.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_fidKeyReleased

    private void fnombreKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fnombreKeyReleased
        // TODO add your handling code here:
        try {
            limpiarDatosTabla();
            ConsultarDatos(fid.getText(), fnombre.getText(), fape.getText(), fpuesto.getSelectedItem().toString());
        } catch (SQLException ex) {
            Logger.getLogger(GestionarUsuarios.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_fnombreKeyReleased

    private void fapeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fapeKeyReleased
        // TODO add your handling code here:
        try {
            limpiarDatosTabla();
            ConsultarDatos(fid.getText(), fnombre.getText(), fape.getText(), fpuesto.getSelectedItem().toString());
        } catch (SQLException ex) {
            Logger.getLogger(GestionarUsuarios.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_fapeKeyReleased

    private void fpuestoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fpuestoActionPerformed
        // TODO add your handling code here:
        try {
            limpiarDatosTabla();
            ConsultarDatos(fid.getText(), fnombre.getText(), fape.getText(), fpuesto.getSelectedItem().toString());
        } catch (SQLException ex) {
            Logger.getLogger(GestionarUsuarios.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_fpuestoActionPerformed

    private void limpiarfiltroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_limpiarfiltroActionPerformed
        // TODO add your handling code here:
        fnombre.setText("");
        fape.setText("");
        fid.setText("");
        fpuesto.setSelectedIndex(0);
    }//GEN-LAST:event_limpiarfiltroActionPerformed

    private void tusuariosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tusuariosMouseClicked
        // TODO add your handling code here:
        int fila = tusuarios.getSelectedRow();
        txtid.setText(tusuarios.getValueAt(fila, 0).toString());
        txtnombre.setText(tusuarios.getValueAt(fila, 1).toString());
        txtape.setText(tusuarios.getValueAt(fila, 2).toString());
        txtusuario.setText(tusuarios.getValueAt(fila, 3).toString());
        txtcontra.setText(tusuarios.getValueAt(fila, 4).toString());
        txttelefono.setText(tusuarios.getValueAt(fila, 5).toString());
        for (int i = cbxtipo.getItemCount() - 1; i > 0; i--) {
            if (cbxtipo.getItemAt(i).equals(tusuarios.getValueAt(fila, 6))) {
                cbxtipo.setSelectedIndex(i);
            }
        }

        // Cargar y redimensionar la imagen
        rutaFoto = tusuarios.getValueAt(fila, 7).toString();
        ImageIcon originalIcon = new ImageIcon(rutaFoto);
        Image originalImage = originalIcon.getImage();
        Image resizedImage = originalImage.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        lfoto.setIcon(new ImageIcon(resizedImage));
        rutaFoto = tusuarios.getValueAt(fila, 7).toString();
        rutaFoto = rutaFoto.replaceAll("\\\\", "\\\\\\\\");

    }//GEN-LAST:event_tusuariosMouseClicked

    private void btnlimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnlimpiarActionPerformed
        // TODO add your handling code here:
        limpiarDatos();
    }//GEN-LAST:event_btnlimpiarActionPerformed

    private void jMenuItem_nuevo_usuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_nuevo_usuarioActionPerformed
        // TODO add your handling code here:
        AgregarUsuario n = new AgregarUsuario();
        n.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jMenuItem_nuevo_usuarioActionPerformed

    private void jMenuItem_gestionar_usuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_gestionar_usuarioActionPerformed
        // TODO add your handling code here:
        GestionarUsuarios n = new GestionarUsuarios();
        n.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jMenuItem_gestionar_usuarioActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        GestionarPuestos n = new GestionarPuestos();
        n.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem_nuevo_productoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_nuevo_productoActionPerformed
        // TODO add your handling code here:
        AgregarProducto n = new AgregarProducto();
        n.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jMenuItem_nuevo_productoActionPerformed

    private void jMenuItem_gestionar_productoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_gestionar_productoActionPerformed
        // TODO add your handling code here:
        GestionarProductos n = new GestionarProductos();
        n.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jMenuItem_gestionar_productoActionPerformed

    private void jMenuItem_actualizar_stockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_actualizar_stockActionPerformed
        // TODO add your handling code here:
        AgregarInventario ai = new AgregarInventario();
        ai.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jMenuItem_actualizar_stockActionPerformed

    private void jMenuItem_actualizar_stock1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_actualizar_stock1ActionPerformed
        // TODO add your handling code here:
        GestionarInventario n = new GestionarInventario();
        n.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jMenuItem_actualizar_stock1ActionPerformed

    private void jMenuItem_nuevo_clienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_nuevo_clienteActionPerformed
        // TODO add your handling code here:
        AgregarCliente n = new AgregarCliente();
        n.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jMenuItem_nuevo_clienteActionPerformed

    private void jMenuItem_gestionar_clienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_gestionar_clienteActionPerformed
        // TODO add your handling code here:
        GestionarClientes n = new GestionarClientes();
        n.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jMenuItem_gestionar_clienteActionPerformed

    private void jMenuItem_nueva_categoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_nueva_categoriaActionPerformed
        AgregarCategoria n = new AgregarCategoria();
        n.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jMenuItem_nueva_categoriaActionPerformed

    private void jMenuItem_gestionar_categoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_gestionar_categoriaActionPerformed
        // TODO add your handling code here:
        GestionarCategorias n = new GestionarCategorias();
        n.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jMenuItem_gestionar_categoriaActionPerformed

    private void jMenuItem_nueva_ventaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_nueva_ventaActionPerformed
        // TODO add your handling code here:
        AgregarVenta n = new AgregarVenta();
        n.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jMenuItem_nueva_ventaActionPerformed

    private void jMenuItem_gestionar_ventasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_gestionar_ventasActionPerformed
        // TODO add your handling code here:
        GestionarVentas n = new GestionarVentas();
        n.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jMenuItem_gestionar_ventasActionPerformed

    private void jMenuItem_reportes_clientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_reportes_clientesActionPerformed
        // TODO add your handling code here:
        ReporteClientes b = new ReporteClientes();
        b.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jMenuItem_reportes_clientesActionPerformed

    private void jMenuItem_reportes_productosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_reportes_productosActionPerformed
        // TODO add your handling code here:
        ReporteProductos b = new ReporteProductos();
        b.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jMenuItem_reportes_productosActionPerformed

    private void jMenuItem_reportes_ventasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_reportes_ventasActionPerformed
        // TODO add your handling code here:
        ReporteVentas b = new ReporteVentas();
        b.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jMenuItem_reportes_ventasActionPerformed

    private void jMenuItem_ver_historialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_ver_historialActionPerformed
        // TODO add your handling code here:
        Bitacora b = new Bitacora();
        b.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jMenuItem_ver_historialActionPerformed

    private void jMenuItem_cerrar_sesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_cerrar_sesionActionPerformed
        // TODO add your handling code here:
        int a = JOptionPane.YES_NO_OPTION;
        int resul = JOptionPane.showConfirmDialog(null, "¿Desea cerrar sesión?", "Cerrar sesión", a);
        if (resul == 0) {
            Login l = new Login();
            l.setVisible(true);
            this.dispose();
        }
    }//GEN-LAST:event_jMenuItem_cerrar_sesionActionPerformed

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
            java.util.logging.Logger.getLogger(GestionarUsuarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GestionarUsuarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GestionarUsuarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GestionarUsuarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GestionarUsuarios().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnactualizar;
    private javax.swing.JButton btneliminar;
    private javax.swing.JButton btnlimpiar;
    private javax.swing.JComboBox<String> cbxtipo;
    private javax.swing.JTextField fape;
    public static javax.swing.JLabel fechalbl;
    private javax.swing.JTextField fid;
    private javax.swing.JTextField fnombre;
    private javax.swing.JLabel fondo;
    private javax.swing.JComboBox<String> fpuesto;
    public static javax.swing.JLabel horalbl;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenu jMenu8;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem_actualizar_stock;
    private javax.swing.JMenuItem jMenuItem_actualizar_stock1;
    private javax.swing.JMenuItem jMenuItem_cerrar_sesion;
    private javax.swing.JMenuItem jMenuItem_gestionar_categoria;
    private javax.swing.JMenuItem jMenuItem_gestionar_cliente;
    private javax.swing.JMenuItem jMenuItem_gestionar_producto;
    private javax.swing.JMenuItem jMenuItem_gestionar_usuario;
    private javax.swing.JMenuItem jMenuItem_gestionar_ventas;
    private javax.swing.JMenuItem jMenuItem_nueva_categoria;
    private javax.swing.JMenuItem jMenuItem_nueva_venta;
    private javax.swing.JMenuItem jMenuItem_nuevo_cliente;
    private javax.swing.JMenuItem jMenuItem_nuevo_producto;
    private javax.swing.JMenuItem jMenuItem_nuevo_usuario;
    private javax.swing.JMenuItem jMenuItem_reportes_clientes;
    private javax.swing.JMenuItem jMenuItem_reportes_productos;
    private javax.swing.JMenuItem jMenuItem_reportes_ventas;
    private javax.swing.JMenuItem jMenuItem_ver_historial;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel lfoto;
    private javax.swing.JButton limpiarfiltro;
    private javax.swing.JLabel ocultar;
    private static javax.swing.JScrollPane scroll;
    private javax.swing.JButton subirfoto;
    public static javax.swing.JTable tusuarios;
    private javax.swing.JTextField txtape;
    public static javax.swing.JPasswordField txtcontra;
    private javax.swing.JTextField txtid;
    private javax.swing.JTextField txtnombre;
    private javax.swing.JTextField txttelefono;
    private javax.swing.JTextField txtusuario;
    private javax.swing.JLabel ver;
    // End of variables declaration//GEN-END:variables
}
