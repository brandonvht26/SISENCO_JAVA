package com.sisenco.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableModel;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Clase de utilidad (utility class) para exportar los datos de un {@link JTable} a un archivo de Microsoft Excel (.xlsx).
 * <p>
 * Utiliza la librería Apache POI para la manipulación de archivos de Excel.
 * Esta clase encapsula toda la lógica de selección de archivo y escritura de datos.
 *
 * @author Brandon Huera Torres (GitHub: brandonvht26)
 * @version 1.0
 */
public class ExcelExporter {

    /**
     * Constructor privado para prevenir la instanciación de esta clase de utilidad.
     * Se debe acceder a sus métodos de forma estática.
     */
    private ExcelExporter() {}

    /**
     * Abre un diálogo para que el usuario elija una ubicación y exporta el contenido de la JTable a un archivo .xlsx.
     * <p>
     * El proceso es el siguiente:
     * 1. Muestra un {@link JFileChooser} para que el usuario seleccione dónde guardar el archivo.
     * 2. Crea un libro de trabajo de Excel en memoria.
     * 3. Escribe los encabezados de la tabla en la primera fila de la hoja.
     * 4. Itera sobre los datos de la tabla y los escribe en las filas subsiguientes.
     * 5. Guarda el libro de trabajo en el archivo seleccionado por el usuario.
     * 6. Muestra un mensaje de éxito o error.
     *
     * @param table  La {@link JTable} cuyos datos se van to exportar.
     * @param parent El componente padre sobre el cual se mostrarán los diálogos (JFileChooser, JOptionPane).
     *               Esto asegura que los diálogos aparezcan centrados y modales respecto a la ventana principal.
     */
    public static void exportToExcel(JTable table, Component parent) {
        // --- MÓDULO DE INTERACCIÓN CON EL USUARIO ---
        // Se crea un selector de archivos para que el usuario elija la ruta y el nombre del archivo.
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar como archivo de Excel");
        // Se establece un filtro para que por defecto solo se puedan guardar archivos con extensión .xlsx.
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos de Excel (*.xlsx)", "xlsx"));

        // Se muestra el diálogo de "Guardar" y se espera la acción del usuario.
        int userSelection = fileChooser.showSaveDialog(parent);

        // Si el usuario aprueba la selección (hace clic en "Guardar").
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            // --- MÓDULO DE PREPARACIÓN DEL ARCHIVO ---
            // Se asegura de que el nombre del archivo termine con la extensión .xlsx.
            String filePath = fileToSave.getAbsolutePath();
            if (!filePath.toLowerCase().endsWith(".xlsx")) {
                fileToSave = new File(filePath + ".xlsx");
            }

            // --- MÓDULO DE ESCRITURA DE EXCEL (usando Apache POI) ---
            // Se utiliza un bloque try-with-resources para asegurar que el workbook se cierre correctamente.
            try (XSSFWorkbook workbook = new XSSFWorkbook()) {
                // Se crea una nueva hoja en el libro de Excel.
                XSSFSheet sheet = workbook.createSheet("Reporte SISENCO");
                TableModel model = table.getModel();

                // 1. Crear la fila de encabezados.
                Row headerRow = sheet.createRow(0);
                for (int i = 0; i < model.getColumnCount(); i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(model.getColumnName(i));
                }

                // 2. Escribir los datos de la tabla en las filas siguientes.
                for (int i = 0; i < model.getRowCount(); i++) {
                    Row row = sheet.createRow(i + 1); // Se empieza desde la fila 1 (después del encabezado).
                    for (int j = 0; j < model.getColumnCount(); j++) {
                        Cell cell = row.createCell(j);
                        Object value = model.getValueAt(i, j);
                        // Se convierte el valor a String para guardarlo en la celda. Se maneja el caso de valores nulos.
                        cell.setCellValue(value != null ? value.toString() : "");
                    }
                }

                // 3. Escribir el libro de trabajo (workbook) en el archivo físico.
                // Se usa otro try-with-resources para el FileOutputStream.
                try (FileOutputStream outputStream = new FileOutputStream(fileToSave)) {
                    workbook.write(outputStream);
                }

                // --- MÓDULO DE NOTIFICACIÓN ---
                // Se informa al usuario que la exportación fue exitosa.
                JOptionPane.showMessageDialog(parent, "Reporte exportado a Excel exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            } catch (IOException e) {
                // Si ocurre un error durante la escritura del archivo, se notifica al usuario.
                JOptionPane.showMessageDialog(parent, "Error al exportar a Excel: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
}