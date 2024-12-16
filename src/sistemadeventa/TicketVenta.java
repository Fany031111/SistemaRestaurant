/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package sistemadeventa;


import br.com.adilson.util.Extenso;
import br.com.adilson.util.PrinterMatrix;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import static sistemadeventa.AgregarVenta.hora;
import static sistemadeventa.AgregarVenta.minutos;
import static sistemadeventa.AgregarVenta.segundos;

/**
 *
 * @author Fanny
 */
public class TicketVenta {

    void imprimirTicket(JTable jtbl_venta, String total, String pedido) {
        try {
            PrinterMatrix printer = new PrinterMatrix();
            Extenso e = new Extenso();

            e.setNumber(20.30);
            //Definir el tamanho del papel para la impresion de dinamico y 32 columnas
            int filas = jtbl_venta.getRowCount();
            int tamaño = filas + 40;
            printer.setOutSize(tamaño, 80);

            Calendar calendario = new GregorianCalendar();
            hora = calendario.get(Calendar.HOUR_OF_DAY);
            minutos = calendario.get(Calendar.MINUTE);
            segundos = calendario.get(Calendar.SECOND);

            //Imprimir = 1ra linea de la columa de 1 a 32
            printer.printTextWrap(0, 1, 5, 80, "===================================================================");
            printer.printTextWrap(1, 1, 30, 80, "R Í O - T O N A L Á"); //Nombre establecimiento
            printer.printTextWrap(2, 1, 33, 80, "Restaurante"); //Nombre establecimiento
            printer.printTextWrap(3, 1, 31, 80, ""); //Barrio
            printer.printTextWrap(4, 1, 35, 80, "Datos"); //Codigo Postal
            printer.printTextWrap(5, 1, 16, 41, "Fecha: " + AgregarVenta.fechalbl.getText()); //Aqui va la fecha de recibo
            if (hora < 10 && minutos < 10 && segundos < 10) {
                printer.printTextWrap(5, 1, 44, 80, "Hora: " + "0" + hora + ":0" + minutos + ":0" + segundos);
            } else if (hora < 10 && minutos < 10) {
                printer.printTextWrap(5, 1, 44, 80, "Hora: " + "0" + hora + ":0" + minutos + ":" + segundos);
            } else if (hora < 10 && segundos < 10) {
                printer.printTextWrap(5, 1, 44, 80, "Hora: " + "0" + hora + ":" + minutos + ":0" + segundos);
            } else if (minutos < 10 && segundos < 10) {
                printer.printTextWrap(5, 1, 44, 80, "Hora: " + hora + ":0" + minutos + ":0" + segundos);
            } else if (hora < 10) {
                printer.printTextWrap(5, 1, 44, 80, "Hora: " + "0" + hora + ":" + minutos + ":" + segundos);
            } else if (minutos < 10) {
                printer.printTextWrap(5, 1, 44, 80, "Hora: " + hora + ":0" + minutos + ":" + segundos);
            } else if (segundos < 10) {
                printer.printTextWrap(5, 1, 44, 80, "Hora: " + hora + ":" + minutos + ":0" + segundos);
            } else {
                printer.printTextWrap(5, 1, 44, 80, "Hora: " + hora + ":" + minutos + ":" + segundos);
            }
            printer.printTextWrap(7, 1, 32, 80, "VENTA NO. "+pedido); //Nombre establecimiento

            //printer.printTextWrap(6, 1, 38, 80, "Hora: "+); //Aqui va la hora de recibo
            printer.printTextWrap(8, 1, 5, 80, "——————————–——————————–——————————–——————————–——–———–——–—–———–——–———");
            printer.printTextWrap(9, 1, 7, 80, "ID      NOMBRE         CANT       PRECIO       SUBTOTAL");
            printer.printTextWrap(10, 1, 0, 80, " ");
            for (int i = 0; i < filas; i++) {
                int p = 11 + i; //Fila
                printer.printTextWrap(p, 1, 7, 19, jtbl_venta.getValueAt(i, 0).toString());
                printer.printTextWrap(p, 1, 12, 42, jtbl_venta.getValueAt(i, 1).toString());
                printer.printTextWrap(p, 1, 30, 49, jtbl_venta.getValueAt(i, 2).toString());
                String pre = printer.alinharADireita(10, jtbl_venta.getValueAt(i, 3).toString());
                printer.printTextWrap(p, 1, 40, 80, pre);
                
                String subt = printer.alinharADireita(10, jtbl_venta.getValueAt(i, 6).toString());
                printer.printTextWrap(p, 1, 57, 80, subt);

                //String inp= printer.alinharADireita(7,punto_Venta.jtbl_venta.getValueAt(i,6).toString());
                //printer.printTextWrap(p , 1, 25, 32, inp);
            }
            DecimalFormat formateador = new DecimalFormat("########.##");

            printer.printTextWrap(filas + 15, 1, 5, 80, "——————————–——————————–——————————–——————————–——–———–——–—–———–——–———");
            String tot = printer.alinharADireita(10, total);
            printer.printTextWrap(filas + 16, 1, 51, 80, "Total:$ " + tot);
            //printer.printTextWrap(filas + 18, 1, 20, 80, "$" + tot);

            /*String efe = printer.alinharADireita(10, pago);
            printer.printTextWrap(filas + 17, 1, 49, 80, "Pagaste:$ " + efe);

            String cam = printer.alinharADireita(10, restante);
            printer.printTextWrap(filas + 18, 1, 50, 80, "Cambio:$ " + cam);*/

            printer.printTextWrap(filas + 19, 1, 5, 80, "——————————–——————————–——————————–——————————–——–———–——–—–———–——–———");
            printer.printTextWrap(filas + 20, 1, 23, 80, "¡Gracias por su preferencia!");
            printer.printTextWrap(filas + 21, 1, 30, 80, "R Í O - T O N A L Á");
            printer.printTextWrap(filas + 22, 1, 24, 80, "");
            printer.printTextWrap(filas + 23, 1, 5, 80, "===================================================================");

            ///CREAR ARCHIVO EN CARPETA DEL PROYECTO PARA PEDIDOS
            String ruta = System.getProperty("user.home");
            printer.toFile(ruta + "/Venta_" +pedido+ ".txt");
            FileInputStream inputStream = null;
            try {
                inputStream = new FileInputStream(ruta + "/Venta_" + pedido + ".txt");
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al guardar");
            }
            if (inputStream == null) {
                return;
            }

            DocFlavor docFormat = DocFlavor.INPUT_STREAM.AUTOSENSE;
            Doc document = new SimpleDoc(inputStream, docFormat, null);
            PrintRequestAttributeSet attributeSet = new HashPrintRequestAttributeSet();
            PrintService defaultPrintService = PrintServiceLookup.lookupDefaultPrintService();

            if (defaultPrintService != null) {
                DocPrintJob printJob = defaultPrintService.createPrintJob();
                try {
                    printJob.print(document, attributeSet);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                System.err.println("No existen impresoras instaladas");
            }

            inputStream.close();
            //imprimirFin(subTotal, total, dineroR, devolucion); //ESTE METODO NO SE UTILIZARA

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al imprimir " + e);
        }
        /**
         * @param args the command line arguments
         */
    }

    public static void main(String[] args) {
        // TODO code application logic here
    }

}
