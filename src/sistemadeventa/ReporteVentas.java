/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package sistemadeventa;
import java.util.Calendar;
import java.util.GregorianCalendar;
import Conexion.Conexion;
import com.itextpdf.text.BaseColor;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import static sistemadeventa.ReporteClientes.fechalbl;
import static sistemadeventa.ReporteClientes.horalbl;

/**
 *
 * @author Fanny
 */
public class ReporteVentas extends javax.swing.JFrame implements Runnable{

    /**
     * Creates new form ReporteVentas
     */
    public static Conexion objeto = new Conexion();
    public static DefaultTableModel modelo, modelo2;

    public ReporteVentas() {
        initComponents();
        this.setSize(new Dimension(1200, 740));

        this.setLocationRelativeTo(null);
        this.setTitle("Reporte de Ventas - SISTEMA DE VENTAS");
        scroll.setOpaque(false); // Hacer transparente el JScrollPane
        scroll.getViewport().setOpaque(false); // Hacer transparente el JViewport
        scroll.setBorder(null);

        try {
            String fecha = (ffecha.getDate() != null) ? new java.sql.Date(ffecha.getDate().getTime()).toString() : "";
            ConsultarDatos(fid.getText(), fnombre.getText(), fecha);
        } catch (SQLException ex) {
            Logger.getLogger(GestionarVentas.class.getName()).log(Level.SEVERE, null, ex);
        }
    //FECHA Y HORA
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


    static void ConsultarDatos(String id, String nombre, String fecha) throws SQLException {

        PreparedStatement stmt = null;
        ResultSet respuesta = null;
        try {
            // Reutilizar la conexión estática
            Connection cx = objeto.conexionReturn();

            // Preparar la consulta base
            StringBuilder query = new StringBuilder("SELECT tb_venta.idventa, "
                    + "tb_cliente.nombre, tb_venta.fechaventa, tb_venta.total "
                    + "FROM tb_venta join tb_cliente on tb_venta.idcliente=tb_cliente.idcliente WHERE 1=1");
            List<Object> params = new ArrayList<>();

            // Agregar filtros según los parámetros
            if (!id.equals("")) {
                query.append(" AND idventa LIKE ?");
                params.add(id + "%");
            }
            if (!nombre.equals("")) {
                query.append(" AND nombre LIKE ?");
                params.add(nombre + "%");
            }

            if (!fecha.equals("")) {
                query.append(" AND fechaventa = ?");
                params.add(fecha);
            }

            // Preparar la consulta SQL
            stmt = cx.prepareStatement(query.toString());
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            respuesta = stmt.executeQuery();

            // Modelo para la tabla de aluminio
            Object[] usuarios = new Object[4];
            modelo = (DefaultTableModel) tproducto.getModel();
            while (respuesta.next()) {
                usuarios[0] = respuesta.getInt(1);
                usuarios[1] = respuesta.getString(2);
                usuarios[2] = respuesta.getString(3);
                usuarios[3] = respuesta.getString(4);

                modelo.addRow(usuarios);
            }
            tproducto.setModel(modelo);

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

    public static void limpiarDatos() {

    }

    void limpiarDatosTabla() {
        int fila = tproducto.getRowCount();
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

        jLabel1 = new javax.swing.JLabel();
        scroll = new javax.swing.JScrollPane();
        tproducto = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        fid = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        fnombre = new javax.swing.JTextField();
        limpiarfiltro = new javax.swing.JButton();
        ffecha = new com.toedter.calendar.JDateChooser();
        jButton1 = new javax.swing.JButton();
        fechalbl = new javax.swing.JLabel();
        horalbl = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
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
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Reporte de Ventas");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 1200, -1));

        tproducto.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tproducto.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Cliente", "Fecha", "Total"
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
        tproducto.setRowHeight(30);
        tproducto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tproductoMouseClicked(evt);
            }
        });
        scroll.setViewportView(tproducto);
        if (tproducto.getColumnModel().getColumnCount() > 0) {
            tproducto.getColumnModel().getColumn(0).setMinWidth(70);
            tproducto.getColumnModel().getColumn(0).setPreferredWidth(70);
            tproducto.getColumnModel().getColumn(0).setMaxWidth(70);
            tproducto.getColumnModel().getColumn(2).setMinWidth(150);
            tproducto.getColumnModel().getColumn(2).setPreferredWidth(150);
            tproducto.getColumnModel().getColumn(2).setMaxWidth(150);
            tproducto.getColumnModel().getColumn(3).setMinWidth(120);
            tproducto.getColumnModel().getColumn(3).setPreferredWidth(120);
            tproducto.getColumnModel().getColumn(3).setMaxWidth(120);
        }

        getContentPane().add(scroll, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 190, 870, 360));

        jPanel1.setBackground(new java.awt.Color(0, 153, 204));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Filtrar ventas por:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 18), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        fid.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        fid.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                fidKeyReleased(evt);
            }
        });
        jPanel1.add(fid, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 30, 90, 30));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("ID:");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, -1, 30));

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Fecha:");
        jPanel1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 30, -1, 30));

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Cliente:");
        jPanel1.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 30, -1, 30));

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
        jPanel1.add(fnombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 30, 260, 30));

        limpiarfiltro.setBackground(new java.awt.Color(255, 204, 204));
        limpiarfiltro.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        limpiarfiltro.setText("x");
        limpiarfiltro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                limpiarfiltroActionPerformed(evt);
            }
        });
        jPanel1.add(limpiarfiltro, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 30, 40, 30));

        ffecha.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                ffechaPropertyChange(evt);
            }
        });
        jPanel1.add(ffecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 30, 230, 30));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 100, 870, 80));

        jButton1.setBackground(new java.awt.Color(255, 204, 204));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/pdf stock.png"))); // NOI18N
        jButton1.setText("Descargar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 570, 150, 40));

        fechalbl.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        fechalbl.setForeground(new java.awt.Color(255, 255, 255));
        fechalbl.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        getContentPane().add(fechalbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 620, 90, 20));

        horalbl.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        horalbl.setForeground(new java.awt.Color(255, 255, 255));
        horalbl.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        getContentPane().add(horalbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 620, 90, 20));

        jSeparator1.setForeground(new java.awt.Color(255, 255, 255));
        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        getContentPane().add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 620, 20, 20));

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

    private void tproductoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tproductoMouseClicked
        // TODO add your handling code here:


    }//GEN-LAST:event_tproductoMouseClicked

    private void fidKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fidKeyReleased
        // TODO add your handling code here:
        try {
            limpiarDatosTabla();
            limpiarDatos();
            String fecha = (ffecha.getDate() != null) ? new java.sql.Date(ffecha.getDate().getTime()).toString() : "";
            ConsultarDatos(fid.getText(), fnombre.getText(), fecha);

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
            limpiarDatos();

            String fecha = (ffecha.getDate() != null) ? new java.sql.Date(ffecha.getDate().getTime()).toString() : "";
            ConsultarDatos(fid.getText(), fnombre.getText(), fecha);

        } catch (SQLException ex) {
            Logger.getLogger(GestionarUsuarios.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_fnombreKeyReleased

    private void limpiarfiltroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_limpiarfiltroActionPerformed
        // TODO add your handling code here:
        limpiarDatos();

        fnombre.setText("");
        fid.setText("");
        ffecha.setDate(null);
    }//GEN-LAST:event_limpiarfiltroActionPerformed

    private void ffechaPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_ffechaPropertyChange
        // TODO add your handling code here:
        try {
            limpiarDatosTabla();
            limpiarDatos();

            String fecha = (ffecha.getDate() != null) ? new java.sql.Date(ffecha.getDate().getTime()).toString() : "";
            ConsultarDatos(fid.getText(), fnombre.getText(), fecha);

        } catch (SQLException ex) {
            Logger.getLogger(GestionarUsuarios.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_ffechaPropertyChange

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar PDF");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Archivo PDF", "pdf"));

        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();

            // Verifica si el archivo tiene extensión ".pdf", si no la tiene, se la agrega
            if (!archivo.getAbsolutePath().endsWith(".pdf")) {
                archivo = new File(archivo + ".pdf");
            }

            // Crear el documento PDF en la ubicación seleccionada
            Document documento = new Document(PageSize.A4, 10, 10, 10, 10);
            try {
                PdfWriter.getInstance(documento, new FileOutputStream(archivo));
                documento.open();

                // Cargar el logo de la empresa
                try {
                    // Cargar el logo de la empresa
                    com.itextpdf.text.Image logo = com.itextpdf.text.Image.getInstance(getClass().getResource("/Imagenes/logoFINAL.png"));

                    // Ajusta la posición absoluta del logo (esquina superior derecha)
                    logo.setAbsolutePosition(440, 715); // Ajusta las coordenadas X, Y para ponerlo en la posición deseada (ajusta estos valores según sea necesario)
                    logo.scaleToFit(200, 100); // Ajustar el tamaño del logo (ancho x alto)

                    // Añadir el logo al documento (esto estará "por detrás" del texto)
                    documento.add(logo);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Para título grande
                Font f = new Font();
                f.setFamily(Font.FontFamily.HELVETICA.name());
                f.setStyle(Font.BOLD);
                f.setSize(20);
                // Para título
                Font f3 = new Font();
                f3.setFamily(Font.FontFamily.HELVETICA.name());
                f3.setStyle(Font.BOLD);
                f3.setSize(18);

                // Para subtítulo
                Font f2 = new Font();
                f2.setFamily(Font.FontFamily.HELVETICA.name());
                f2.setStyle(Font.BOLDITALIC);
                f2.setSize(10);

                // Título principal
                Paragraph titulo = new Paragraph();
                titulo.setAlignment(Element.ALIGN_CENTER);
                titulo.setFont(f);
                titulo.add("Restaurante 'Río Tonalá'");

                // Título 
                Paragraph sep = new Paragraph();
                sep.setAlignment(Element.ALIGN_CENTER);
                sep.setFont(f3);
                sep.add("-");

                // Título 
                Paragraph titulo2 = new Paragraph();
                titulo2.setAlignment(Element.ALIGN_CENTER);
                titulo2.setFont(f3);
                titulo2.add("Reporte de ventas");

                // Espacio
                Paragraph espacio = new Paragraph();
                espacio.setAlignment(Element.ALIGN_CENTER);
                espacio.add("\n");

                // Agregar la fecha y hora como pie de página, alineados a la derecha
                Paragraph fechaHora = new Paragraph();
                fechaHora.setAlignment(Element.ALIGN_CENTER);
                fechaHora.setFont(f2);
                fechaHora.add("Fecha: " + fechalbl.getText() + "   Hora: " + horalbl.getText());
                documento.add(espacio);
                documento.add(espacio);
                documento.add(espacio);

                documento.add(titulo);
                documento.add(sep);

                documento.add(titulo2);

                documento.add(fechaHora);

                documento.add(espacio);
                documento.add(espacio);

                // Fuente más pequeña para la tabla
                Font fontTabla = new Font();
                fontTabla.setFamily(Font.FontFamily.HELVETICA.name());
                fontTabla.setSize(8); // Tamaño de fuente reducido

                // Crear la tabla con las columnas seleccionadas
                PdfPTable tabla = new PdfPTable(4);
                float[] columnWidths = {1f, 3f, 1f, 1f};
                tabla.setWidths(columnWidths);

                // Agregar encabezados
                PdfPCell header = new PdfPCell(new Phrase("ID de venta", fontTabla));
                header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                tabla.addCell(header);

                header = new PdfPCell(new Phrase("Nombre del cliente", fontTabla));
                header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                tabla.addCell(header);

                header = new PdfPCell(new Phrase("Fecha", fontTabla));
                header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                tabla.addCell(header);

                header = new PdfPCell(new Phrase("Total", fontTabla));
                header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                tabla.addCell(header);

                // Agregar las filas seleccionadas desde el modelo `modelostock`
                for (int i = 0; i < modelo.getRowCount(); i++) {
                    tabla.addCell(new Phrase((modelo.getValueAt(i, 0) == null ? "" : modelo.getValueAt(i, 0).toString()), fontTabla)); // Columna 1
                    tabla.addCell(new Phrase((modelo.getValueAt(i, 1) == null ? "" : modelo.getValueAt(i, 1).toString()), fontTabla)); // Columna 2
                    tabla.addCell(new Phrase((modelo.getValueAt(i, 2) == null ? "" : modelo.getValueAt(i, 2).toString()), fontTabla)); // Columna 4
                    tabla.addCell(new Phrase((modelo.getValueAt(i, 3) == null ? "" : modelo.getValueAt(i, 3).toString()), fontTabla)); // Columna 5
                }

                // Añadir la tabla al documento
                documento.add(tabla);
                documento.add(espacio);

                // Cerrar el documento
                documento.close();
                JOptionPane.showMessageDialog(null, "Reporte creado en: " + archivo.getAbsolutePath());

            } catch (DocumentException | FileNotFoundException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al crear el reporte.");
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(ReporteVentas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ReporteVentas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ReporteVentas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ReporteVentas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ReporteVentas().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JLabel fechalbl;
    private com.toedter.calendar.JDateChooser ffecha;
    private javax.swing.JTextField fid;
    private javax.swing.JTextField fnombre;
    private javax.swing.JLabel fondo;
    public static javax.swing.JLabel horalbl;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
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
    private javax.swing.JButton limpiarfiltro;
    private static javax.swing.JScrollPane scroll;
    public static javax.swing.JTable tproducto;
    // End of variables declaration//GEN-END:variables
}
