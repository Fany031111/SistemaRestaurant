package Conexion;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class Conexion {

    String bd = "bd_sistema_ventas";
    //CAMBIAR USER Y PASSWORD
    String url = "jdbc:mysql://localhost:3306/bd_sistema_ventas?user=root&password=root987";

    String driver = "com.mysql.cj.jdbc.Driver";
    Connection cx;

    public Conexion() {
        try {
            Class.forName(driver);
            cx = DriverManager.getConnection(url);
            cx.setAutoCommit(false);
            System.out.println("Se conecto a la bd: " + bd);
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("No se conectó a la bd " + bd);
        }
    }

    public Connection conexionReturn() {
        return cx;
    }

    public void desconter() throws SQLException {
        cx.close();
    }

    public int ejecutarSentenciaSQL(String sentenciaSQL) {
        try {
            PreparedStatement pstm = cx.prepareStatement(sentenciaSQL);
            pstm.execute();
            return 1;
        } catch (SQLException e) {
            System.out.println(e);
            return 0;
        }
    }

    public ResultSet consultarRegistros(String sentenciaSQL) {
        try {
            PreparedStatement pstm = cx.prepareStatement(sentenciaSQL);
            ResultSet respuesta = pstm.executeQuery();
            cx.commit();
            return respuesta;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    //TB_USUARIO}
    public void agregarUsuario(String nombre, String apellido, String usuario, String password,
            String telefono, int idtipousuario, String rutaFoto) throws SQLException {
        String consulta = "INSERT INTO tb_usuario (nombre, apellido, usuario, password, telefono, idtipousuario, rutaFoto) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = cx.prepareStatement(consulta)) {
            ps.setString(1, nombre);
            ps.setString(2, apellido);
            ps.setString(3, usuario);
            ps.setString(4, password);
            ps.setString(5, telefono);
            ps.setInt(6, idtipousuario);
            ps.setString(7, rutaFoto);

            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Usuario registrado con éxito.");
            cx.commit();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al intentar agregar el usuario: " + e.getMessage());
            if (cx != null) {
                cx.rollback();
            }
        }
    }

    public void eliminarUsuario(int idusuario) throws SQLException {
        String consulta = "DELETE FROM tb_usuario WHERE idusuario = ?";
        int confirm = JOptionPane.showConfirmDialog(null,
                "¿Está seguro de que desea eliminar este usuario?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (PreparedStatement ps = cx.prepareStatement(consulta)) {
                ps.setInt(1, idusuario);

                ps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Usuario eliminado con éxito.");
                cx.commit();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error al intentar eliminar el usuario: " + e.getMessage());
                if (cx != null) {
                    cx.rollback();
                }
            }
        }
    }

    public void actualizarUsuario(int idusuario, String nombre, String apellido, String usuario,
            String password, String telefono, int idtipousuario, String rutaFoto) throws SQLException {
        String consulta = "UPDATE tb_usuario SET nombre = ?, apellido = ?, usuario = ?, password = ?, "
                + "telefono = ?, idtipousuario = ?, rutaFoto = ? WHERE idusuario = ?";
        try (PreparedStatement ps = cx.prepareStatement(consulta)) {
            ps.setString(1, nombre);
            ps.setString(2, apellido);
            ps.setString(3, usuario);
            ps.setString(4, password);
            ps.setString(5, telefono);
            ps.setInt(6, idtipousuario);
            ps.setString(7, rutaFoto);
            ps.setInt(8, idusuario);

            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Usuario actualizado con éxito.");
            cx.commit();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al intentar actualizar el usuario: " + e.getMessage());
            if (cx != null) {
                cx.rollback();
            }
        }
    }

    //TB_TIPOUSUARIO
    public void agregarTipoUsuario(String tipo) throws SQLException {
        String consulta = "INSERT INTO tb_tipousuario (tipo) VALUES (?)";
        try (PreparedStatement ps = cx.prepareStatement(consulta)) {
            ps.setString(1, tipo);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Tipo de usuario agregado con éxito.");
            cx.commit();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al intentar agregar el tipo de usuario: " + e.getMessage());
            if (cx != null) {
                cx.rollback();
            }
        }
    }

    public void eliminarTipoUsuario(int idtipousuario) throws SQLException {
        String consulta = "DELETE FROM tb_tipousuario WHERE idtipousuario = ?";
        int confirm = JOptionPane.showConfirmDialog(null,
                "¿Está seguro de que desea eliminar este tipo de usuario?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (PreparedStatement ps = cx.prepareStatement(consulta)) {
                ps.setInt(1, idtipousuario);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Tipo de usuario eliminado con éxito.");
                cx.commit();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error al intentar eliminar el tipo de usuario: " + e.getMessage());
                if (cx != null) {
                    cx.rollback();
                }
            }
        }
    }

    public void actualizarTipoUsuario(int idtipousuario, String nuevoTipo) throws SQLException {
        String consulta = "UPDATE tb_tipousuario SET tipo = ? WHERE idtipousuario = ?";
        try (PreparedStatement ps = cx.prepareStatement(consulta)) {
            ps.setString(1, nuevoTipo);
            ps.setInt(2, idtipousuario);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Tipo de usuario actualizado con éxito.");
            cx.commit();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al intentar actualizar el tipo de usuario: " + e.getMessage());
            if (cx != null) {
                cx.rollback();
            }
        }
    }

    public boolean puestoYaExiste(String puesto) throws SQLException {
        String consulta = "SELECT COUNT(*) FROM tb_tipousuario WHERE tipo = ?";
        try (PreparedStatement ps = cx.prepareStatement(consulta)) {
            ps.setString(1, puesto);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Retorna true si ya existe
                }
            }
        }
        return false; // Retorna false si no existe
    }

    //TB_CLIENTE
    public void agregarCliente(String nombre, String apellido, String cedula, String telefono, String direccion) throws SQLException {
        String consulta = "INSERT INTO tb_cliente (nombre, apellido, cedula, telefono, direccion) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = cx.prepareStatement(consulta)) {
            ps.setString(1, nombre);
            ps.setString(2, apellido);
            ps.setString(3, cedula);
            ps.setString(4, telefono);
            ps.setString(5, direccion);

            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Cliente agregado con éxito.");
            cx.commit();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al intentar agregar el cliente: " + e.getMessage());
            if (cx != null) {
                cx.rollback();
            }
        }
    }

    public void eliminarCliente(int idcliente) throws SQLException {
        String consulta = "DELETE FROM tb_cliente WHERE idcliente = ?";
        int confirm = JOptionPane.showConfirmDialog(null,
                "¿Está seguro de que desea eliminar este cliente?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (PreparedStatement ps = cx.prepareStatement(consulta)) {
                ps.setInt(1, idcliente);

                ps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Cliente eliminado con éxito.");
                cx.commit();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error al intentar eliminar el cliente: " + e.getMessage());
                if (cx != null) {
                    cx.rollback();
                }
            }
        }
    }

    public void actualizarCliente(int idcliente, String nombre, String apellido, String cedula, String telefono, String direccion) throws SQLException {
        String consulta = "UPDATE tb_cliente SET nombre = ?, apellido = ?, cedula = ?, telefono = ?, direccion = ? WHERE idcliente = ?";
        try (PreparedStatement ps = cx.prepareStatement(consulta)) {
            ps.setString(1, nombre);
            ps.setString(2, apellido);
            ps.setString(3, cedula);
            ps.setString(4, telefono);
            ps.setString(5, direccion);
            ps.setInt(6, idcliente);

            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Cliente actualizado con éxito.");
            cx.commit();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al intentar actualizar el cliente: " + e.getMessage());
            if (cx != null) {
                cx.rollback();
            }
        }
    }

    //TB_CATEGORIA
    public void agregarCategoria(String descripcion) throws SQLException {
        String consulta = "INSERT INTO tb_categoria (descripcion) VALUES (?)";
        try (PreparedStatement ps = cx.prepareStatement(consulta)) {
            ps.setString(1, descripcion);

            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Categoría agregada con éxito.");
            cx.commit();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al intentar agregar la categoría: " + e.getMessage());
            if (cx != null) {
                cx.rollback();
            }
        }
    }

    public void eliminarCategoria(int idcategoria) throws SQLException {
        String consulta = "DELETE FROM tb_categoria WHERE idcategoria = ?";
        int confirm = JOptionPane.showConfirmDialog(null,
                "¿Está seguro de que desea eliminar esta categoría?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (PreparedStatement ps = cx.prepareStatement(consulta)) {
                ps.setInt(1, idcategoria);

                ps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Categoría eliminada con éxito.");
                cx.commit();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error al intentar eliminar la categoría: " + e.getMessage());
                if (cx != null) {
                    cx.rollback();
                }
            }
        }
    }

    public void actualizarCategoria(int idcategoria, String descripcion) throws SQLException {
        String consulta = "UPDATE tb_categoria SET descripcion = ? WHERE idcategoria = ?";
        try (PreparedStatement ps = cx.prepareStatement(consulta)) {
            ps.setString(1, descripcion);
            ps.setInt(2, idcategoria);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Categoría actualizada con éxito.");
            cx.commit();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al intentar actualizar la categoría: " + e.getMessage());
            if (cx != null) {
                cx.rollback();
            }
        }
    }

    //TB_PRODUCTO
    public void agregarProducto(String nombre, double precio, String descripcion, int iva, int idcategoria) throws SQLException {
        String consulta = "INSERT INTO tb_producto (nombre, precio, descripcion, iva, idcategoria) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = cx.prepareStatement(consulta)) {
            ps.setString(1, nombre);
            ps.setDouble(2, precio);
            ps.setString(3, descripcion);
            ps.setInt(4, iva);
            ps.setInt(5, idcategoria);

            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Producto agregado con éxito.");
            cx.commit();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al intentar agregar el producto: " + e.getMessage());
            if (cx != null) {
                cx.rollback();
            }
        }
    }

    public void eliminarProducto(int idproducto) throws SQLException {
        String consulta = "DELETE FROM tb_producto WHERE idproducto = ?";
        int confirm = JOptionPane.showConfirmDialog(null,
                "¿Está seguro de que desea eliminar este producto?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (PreparedStatement ps = cx.prepareStatement(consulta)) {
                ps.setInt(1, idproducto);

                ps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Producto eliminado con éxito.");
                cx.commit();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error al intentar eliminar el producto: " + e.getMessage());
                if (cx != null) {
                    cx.rollback();
                }
            }
        }
    }

    public void actualizarProducto(int idproducto, String nombre, double precio, String descripcion, int iva, int idcategoria) throws SQLException {
        String consulta = "UPDATE tb_producto SET nombre = ?, precio = ?, descripcion = ?, iva = ?, idcategoria = ? WHERE idproducto = ?";
        try (PreparedStatement ps = cx.prepareStatement(consulta)) {
            ps.setString(1, nombre);
            ps.setDouble(2, precio);
            ps.setString(3, descripcion);
            ps.setInt(4, iva);
            ps.setInt(5, idcategoria);
            ps.setInt(6, idproducto);

            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Producto actualizado con éxito.");
            cx.commit();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al intentar actualizar el producto: " + e.getMessage());
            if (cx != null) {
                cx.rollback();
            }
        }
    }

    // TB_RECETARIO
    public void agregarReceta(int idproducto, int idingrediente, int cantidad) throws SQLException {
        String consulta = "INSERT INTO tb_recetario (idproducto, idingrediente, cantidad) VALUES (?, ?, ?)";
        try (PreparedStatement ps = cx.prepareStatement(consulta)) {
            ps.setInt(1, idproducto);
            ps.setInt(2, idingrediente);
            ps.setInt(3, cantidad);

            ps.executeUpdate();
            //JOptionPane.showMessageDialog(null, "Receta agregada con éxito.");
            cx.commit();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al intentar agregar la receta: " + e.getMessage());
            if (cx != null) {
                cx.rollback();
            }
        }
    }

    public void eliminarReceta(int idproducto, int idingrediente) throws SQLException {
        String consulta = "DELETE FROM tb_recetario WHERE idproducto = ? AND idingrediente = ?";
        int confirm = JOptionPane.showConfirmDialog(null,
                "¿Está seguro de que desea eliminar esta receta?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (PreparedStatement ps = cx.prepareStatement(consulta)) {
                ps.setInt(1, idproducto);
                ps.setInt(2, idingrediente);

                ps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Receta eliminada con éxito.");
                cx.commit();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error al intentar eliminar la receta: " + e.getMessage());
                if (cx != null) {
                    cx.rollback();
                }
            }
        }
    }

    public void eliminarReceta(int idproducto) throws SQLException {
        String consulta = "DELETE FROM tb_recetario WHERE idproducto = ?";
        //int confirm = JOptionPane.showConfirmDialog(null,
        //    "¿Está seguro de que desea eliminar esta receta?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        //if (confirm == JOptionPane.YES_OPTION) {
        try (PreparedStatement ps = cx.prepareStatement(consulta)) {
            ps.setInt(1, idproducto);

            ps.executeUpdate();
            //JOptionPane.showMessageDialog(null, "Receta eliminada con éxito.");
            cx.commit();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al intentar eliminar la receta: " + e.getMessage());
            if (cx != null) {
                cx.rollback();
            }
        }
        //}
    }

    public void actualizarReceta(int idproducto, int idingrediente, int cantidad) throws SQLException {
        String consulta = "UPDATE tb_recetario SET cantidad = ? WHERE idproducto = ? AND idingrediente = ?";
        try (PreparedStatement ps = cx.prepareStatement(consulta)) {
            ps.setInt(1, cantidad);
            ps.setInt(2, idproducto);
            ps.setInt(3, idingrediente);

            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Receta actualizada con éxito.");
            cx.commit();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al intentar actualizar la receta: " + e.getMessage());
            if (cx != null) {
                cx.rollback();
            }
        }
    }

    // TB_INVENTARIO
    public void agregarIngrediente(String nombre, int cantidad, int idunidad) throws SQLException {
        String consulta = "INSERT INTO tb_inventario (nombre, cantidad, idunidad, fechaactualizacion) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = cx.prepareStatement(consulta)) {
            ps.setString(1, nombre);
            ps.setInt(2, cantidad);
            ps.setInt(3, idunidad);
            ps.setDate(4, new java.sql.Date(System.currentTimeMillis()));

            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Ingrediente agregado con éxito.");
            cx.commit();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al intentar agregar el ingrediente: " + e.getMessage());
            if (cx != null) {
                cx.rollback();
            }
        }
    }

    public void eliminarIngrediente(int idingrediente) throws SQLException {
        String consulta = "DELETE FROM tb_inventario WHERE idingrediente = ?";
        int confirm = JOptionPane.showConfirmDialog(null,
                "¿Está seguro de que desea eliminar este ingrediente?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (PreparedStatement ps = cx.prepareStatement(consulta)) {
                ps.setInt(1, idingrediente);

                ps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Ingrediente eliminado con éxito.");
                cx.commit();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error al intentar eliminar el ingrediente: " + e.getMessage());
                if (cx != null) {
                    cx.rollback();
                }
            }
        }
    }

    public void actualizarIngrediente(int idingrediente, String nombre, int cantidad, int unidad) throws SQLException {
        String consulta = "UPDATE tb_inventario SET nombre = ?, cantidad = ?, idunidad = ?, fechaactualizacion = ? "
                + "WHERE idingrediente = ?";
        try (PreparedStatement ps = cx.prepareStatement(consulta)) {
            ps.setString(1, nombre);
            ps.setInt(2, cantidad);
            ps.setInt(3, unidad);
            ps.setDate(4, new java.sql.Date(System.currentTimeMillis()));
            ps.setInt(5, idingrediente);

            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Ingrediente actualizado con éxito.");
            cx.commit();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al intentar actualizar el ingredidente: " + e.getMessage());
            if (cx != null) {
                cx.rollback();
            }
        }
    }

    public void actualizarInvVenta(int idingrediente, int cantidad) throws SQLException {
        String consulta = "UPDATE tb_inventario SET cantidad = ?, fechaactualizacion = ? "
                + "WHERE idingrediente = ?";
        try (PreparedStatement ps = cx.prepareStatement(consulta)) {
            ps.setInt(1, cantidad);
            ps.setDate(2, new java.sql.Date(System.currentTimeMillis()));
            ps.setInt(3, idingrediente);

            ps.executeUpdate();
            cx.commit();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al intentar actualizar el ingredidente: " + e.getMessage());
            if (cx != null) {
                cx.rollback();
            }
        }
    }

    public void agregarDetalleVenta(int idventa, int idproducto, int cantidad, double preciou,
            double subtotal, int iva, double total) throws SQLException {
        String consulta = "INSERT INTO tb_detalleventa (idventa, idproducto, cantidad, preciou, subtotal, iva, total) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = cx.prepareStatement(consulta)) {
            cx.setAutoCommit(false); // Desactivar auto-commit para la transacción

            ps.setInt(1, idventa);
            ps.setInt(2, idproducto);
            ps.setInt(3, cantidad);
            ps.setDouble(4, preciou);
            ps.setDouble(5, subtotal);
            ps.setInt(6, iva);
            ps.setDouble(7, total);

            ps.executeUpdate();
            cx.commit();
            JOptionPane.showMessageDialog(null, "Detalle de venta agregado con éxito.");
        } catch (SQLException e) {
            if (cx != null) {
                cx.rollback();
            }
            JOptionPane.showMessageDialog(null, "Error al intentar agregar el detalle de venta: " + e.getMessage());
        } finally {
            cx.setAutoCommit(true); // Restaurar el auto-commit
        }
    }

    public void eliminarDetalleVenta(int idventa, int idproducto) throws SQLException {
        String consulta = "DELETE FROM tb_detalleventa WHERE idventa = ? AND idproducto = ?";
        int confirm = JOptionPane.showConfirmDialog(null,
                "¿Está seguro de que desea eliminar este detalle de venta?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try (PreparedStatement ps = cx.prepareStatement(consulta)) {
                cx.setAutoCommit(false); // Desactivar auto-commit para la transacción

                ps.setInt(1, idventa);
                ps.setInt(2, idproducto);
                ps.executeUpdate();
                cx.commit();
                JOptionPane.showMessageDialog(null, "Detalle de venta eliminado con éxito.");
            } catch (SQLException e) {
                if (cx != null) {
                    cx.rollback();
                }
                JOptionPane.showMessageDialog(null, "Error al intentar eliminar el detalle de venta: " + e.getMessage());
            } finally {
                cx.setAutoCommit(true); // Restaurar el auto-commit
            }
        }
    }

    public void actualizarDetalleVenta(int idventa, int idproducto, int cantidad, double preciou,
            double subtotal, int descuento, int iva, double total) throws SQLException {
        String consulta = "UPDATE tb_detalleventa SET cantidad = ?, preciou = ?, subtotal = ?, descuento = ?, "
                + "iva = ?, total = ? WHERE idventa = ? AND idproducto = ?";

        try (PreparedStatement ps = cx.prepareStatement(consulta)) {
            cx.setAutoCommit(false); // Desactivar auto-commit para la transacción

            ps.setInt(1, cantidad);
            ps.setDouble(2, preciou);
            ps.setDouble(3, subtotal);
            ps.setInt(4, descuento);
            ps.setInt(5, iva);
            ps.setDouble(6, total);
            ps.setInt(7, idventa);
            ps.setInt(8, idproducto);

            ps.executeUpdate();
            cx.commit();
            JOptionPane.showMessageDialog(null, "Detalle de venta actualizado con éxito.");
        } catch (SQLException e) {
            if (cx != null) {
                cx.rollback();
            }
            JOptionPane.showMessageDialog(null, "Error al intentar actualizar el detalle de venta: " + e.getMessage());
        } finally {
            cx.setAutoCommit(true); // Restaurar el auto-commit
        }
    }

    public void agregarVenta(int idcliente, double total) throws SQLException {
        String consulta = "INSERT INTO tb_venta (idcliente, fechaventa, total) VALUES (?, ?, ?)";
        try (PreparedStatement ps = cx.prepareStatement(consulta)) {
            ps.setInt(1, idcliente);
            // Fecha automática
            ps.setDate(2, new java.sql.Date(System.currentTimeMillis()));
            ps.setDouble(3, total);

            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Venta registrada con éxito.");
            cx.commit();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al intentar agregar la venta: " + e.getMessage());
            if (cx != null) {
                cx.rollback();
            }
        }
    }

    public void eliminarVenta(int idventa) throws SQLException {
        String consulta = "DELETE FROM tb_venta WHERE idventa = ?";
        int confirm = JOptionPane.showConfirmDialog(null,
                "¿Está seguro de que desea eliminar esta venta?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (PreparedStatement ps = cx.prepareStatement(consulta)) {
                ps.setInt(1, idventa);

                ps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Venta eliminada con éxito.");
                cx.commit();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error al intentar eliminar la venta: " + e.getMessage());
                if (cx != null) {
                    cx.rollback();
                }
            }
        }
    }

    public void actualizarVenta(int idventa, int idcliente, int idusuario, double total) throws SQLException {
        String consulta = "UPDATE tb_venta SET idcliente = ?, idusuario = ?, fechaventa = ?, total = ? WHERE idventa = ?";
        try (PreparedStatement ps = cx.prepareStatement(consulta)) {
            ps.setInt(1, idcliente);
            ps.setInt(2, idusuario);
            // Fecha automática al actualizar
            ps.setDate(3, new java.sql.Date(System.currentTimeMillis()));
            ps.setDouble(4, total);
            ps.setInt(5, idventa);

            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Venta actualizada con éxito.");
            cx.commit();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al intentar actualizar la venta: " + e.getMessage());
            if (cx != null) {
                cx.rollback();
            }
        }
    }

    public void agregarDetalleVentav(int idventa, int idproducto, int cantidad, double preciou,
            double subtotal, int iva, double total) throws SQLException {
        String consulta = "INSERT INTO tb_detalleventa (idventa, idproducto, cantidad, preciou, subtotal, iva, total) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = cx.prepareStatement(consulta)) {
            ps.setInt(1, idventa);
            ps.setInt(2, idproducto);
            ps.setInt(3, cantidad);
            ps.setDouble(4, preciou);
            ps.setDouble(5, subtotal);
            ps.setInt(6, iva);
            ps.setDouble(7, total);

            ps.executeUpdate();
        }
    }

    public void agregarVentav(int idcliente, double total) throws SQLException {
        String consulta = "INSERT INTO tb_venta (idcliente, fechaventa, total) VALUES (?, ?, ?)";
        try (PreparedStatement ps = cx.prepareStatement(consulta)) {
            ps.setInt(1, idcliente);
            ps.setDate(2, new java.sql.Date(System.currentTimeMillis()));
            ps.setDouble(3, total);

            ps.executeUpdate();
        }
    }

    public void actualizarInvVentav(int idingrediente, int cantidad) throws SQLException {
        String consulta = "UPDATE tb_inventario SET cantidad = ?, fechaactualizacion = ? WHERE idingrediente = ?";
        try (PreparedStatement ps = cx.prepareStatement(consulta)) {
            ps.setInt(1, cantidad);
            ps.setDate(2, new java.sql.Date(System.currentTimeMillis()));
            ps.setInt(3, idingrediente);

            ps.executeUpdate();
        }
    }

}
