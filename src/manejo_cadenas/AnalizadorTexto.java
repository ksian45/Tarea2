/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package manejo_cadenas;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author UMG
 */




public class AnalizadorTexto extends javax.swing.JFrame {
    private File archivoActual = null;
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(AnalizadorTexto.class.getName());

    /**
     * Creates new form AnalizadorTexto
     */
    public AnalizadorTexto() {
        initComponents();

    // Inicializa el menú
    crearMenu();

    // Opcional: si quieres que el área de resultados no se edite
    resultadoArea.setEditable(false);
    resultadoArea.setBackground(new java.awt.Color(240, 240, 240));

    }

    private void crearMenu() {
    JMenuBar menuBar = new JMenuBar();

    // Menú Archivo
    JMenu menuArchivo = new JMenu("Archivo");
    JMenuItem abrir = new JMenuItem("Abrir");
    JMenuItem guardar = new JMenuItem("Guardar");
    JMenuItem guardarComo = new JMenuItem("Guardar como");

    abrir.addActionListener(e -> abrirArchivo());
    guardar.addActionListener(e -> guardarArchivo());
    guardarComo.addActionListener(e -> guardarArchivoComo());

    menuArchivo.add(abrir);
    menuArchivo.add(guardar);
    menuArchivo.add(guardarComo);

    // Menú Editar
    JMenu menuEditar = new JMenu("Editar");
    JMenuItem copiar = new JMenuItem("Copiar");
    JMenuItem cortar = new JMenuItem("Cortar");
    JMenuItem pegar = new JMenuItem("Pegar");
    JMenuItem buscar = new JMenuItem("Buscar");
    JMenuItem reemplazar = new JMenuItem("Reemplazar");

    copiar.addActionListener(e -> textArea.copy());
    cortar.addActionListener(e -> textArea.cut());
    pegar.addActionListener(e -> textArea.paste());
    buscar.addActionListener(e -> buscarTexto());
    reemplazar.addActionListener(e -> reemplazarTexto());

    menuEditar.add(copiar);
    menuEditar.add(cortar);
    menuEditar.add(pegar);
    menuEditar.add(buscar);
    menuEditar.add(reemplazar);

    menuBar.add(menuArchivo);
    menuBar.add(menuEditar);

    setJMenuBar(menuBar);
}

private void abrirArchivo() {
    JFileChooser fc = new JFileChooser();
    fc.setFileFilter(new FileNameExtensionFilter("Archivos de texto", "txt"));
    if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
        archivoActual = fc.getSelectedFile();
        try {
            String contenido = Files.readString(archivoActual.toPath());
            textArea.setText(contenido);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al abrir el archivo");
        }
    }
}

private void guardarArchivo() {
    if (archivoActual != null) {
        // Si ya existe un archivo asociado, lo sobrescribe
        try (FileWriter fw = new FileWriter(archivoActual)) {
            fw.write(textArea.getText());
            JOptionPane.showMessageDialog(this, "Archivo guardado exitosamente");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar el archivo", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } else {
        // Si no hay archivo asociado, abre el diálogo para guardar como
        guardarArchivoComo();
    }
}

private void guardarArchivoComo() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Guardar archivo como...");
    fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos de texto (*.txt)", "txt"));
    
    int seleccion = fileChooser.showSaveDialog(this);
    if (seleccion == JFileChooser.APPROVE_OPTION) {
        archivoActual = fileChooser.getSelectedFile();
        // Asegurar que tenga la extensión .txt
        if (!archivoActual.getName().toLowerCase().endsWith(".txt")) {
            archivoActual = new File(archivoActual.getAbsolutePath() + ".txt");
        }
        guardarArchivo(); // Llama al método guardar ahora que ya tenemos archivoActual
    }
}

private void buscarTexto() {
    String palabra = JOptionPane.showInputDialog(this, "Ingrese la palabra a buscar:");
    if (palabra != null && !palabra.isEmpty()) {
        String contenido = textArea.getText();
        int index = contenido.indexOf(palabra);
        if (index >= 0) {
            textArea.setCaretPosition(index);
            textArea.select(index, index + palabra.length());
        } else {
            JOptionPane.showMessageDialog(this, "Palabra no encontrada");
        }
    }
}

private void reemplazarTexto() {
    String buscar = JOptionPane.showInputDialog(this, "Palabra a reemplazar:");
    String reemplazo = JOptionPane.showInputDialog(this, "Reemplazar por:");
    if (buscar != null && reemplazo != null) {
        textArea.setText(textArea.getText().replace(buscar, reemplazo));
    }
}

private void procesarTexto() {
    String texto = textArea.getText().trim();
    if (texto.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No hay texto para procesar");
        return;
    }

    // Longitud del texto
    int longitudTexto = texto.length();
    
    // Total de palabras
    String[] palabrasArray = texto.split("\\s+");
    int totalPalabras = palabrasArray.length;
    
    // Primera y última letra del texto
    char primeraLetra = texto.charAt(0);
    char ultimaLetra = texto.charAt(texto.length() - 1);
    
    // Letra central del texto
    char letraCentral = texto.charAt(texto.length() / 2);
    
    // Primera, central y última palabra
    String primeraPalabra = palabrasArray[0];
    String palabraCentral = palabrasArray[palabrasArray.length / 2];
    String ultimaPalabra = palabrasArray[palabrasArray.length - 1];
    
    // Contar repeticiones de vocales específicas
    int repeticionesA = contarRepeticiones(texto, "aáAÁ");
    int repeticionesE = contarRepeticiones(texto, "eéEÉ");
    int repeticionesI = contarRepeticiones(texto, "iíIÍ");
    int repeticionesO = contarRepeticiones(texto, "oóOÓ");
    int repeticionesU = contarRepeticiones(texto, "uúUÚ");
    
    // Contar palabras con cantidad de caracteres par e impar
    int palabrasPar = 0;
    int palabrasImpar = 0;
    for (String palabra : palabrasArray) {
        if (palabra.length() % 2 == 0) {
            palabrasPar++;
        } else {
            palabrasImpar++;
        }
    }

    // Traducción clave murciélago (mantiene mayúsculas)
    String clave = "murcielago";
    StringBuilder traducido = new StringBuilder();
    for (char c : texto.toCharArray()) {
        char lowerC = Character.toLowerCase(c);
        int pos = clave.indexOf(lowerC);
        if (pos != -1) {
            traducido.append(pos);
        } else {
            traducido.append(c);
        }
    }

    // Construir el resultado
    StringBuilder resultado = new StringBuilder();
    resultado.append("Longitud del texto: ").append(longitudTexto).append("\n");
    resultado.append("Total de palabras: ").append(totalPalabras).append("\n");
    resultado.append("Primera letra del texto: '").append(primeraLetra).append("'\n");
    resultado.append("Última letra del texto: '").append(ultimaLetra).append("'\n");
    resultado.append("Letra central del texto: '").append(letraCentral).append("'\n");
    resultado.append("Primera palabra: \"").append(primeraPalabra).append("\"\n");
    resultado.append("Palabra central: \"").append(palabraCentral).append("\"\n");
    resultado.append("Última palabra: \"").append(ultimaPalabra).append("\"\n");
    resultado.append("Repeticiones de A/a/á: ").append(repeticionesA).append("\n");
    resultado.append("Repeticiones de E/e/é: ").append(repeticionesE).append("\n");
    resultado.append("Repeticiones de I/i/í: ").append(repeticionesI).append("\n");
    resultado.append("Repeticiones de O/o/ó: ").append(repeticionesO).append("\n");
    resultado.append("Repeticiones de U/u/ú: ").append(repeticionesU).append("\n");
    resultado.append("Palabras con cantidad de caracteres par: ").append(palabrasPar).append("\n");
    resultado.append("Palabras con cantidad de caracteres impar: ").append(palabrasImpar).append("\n\n");
    resultado.append("TRADUCCIÓN CLAVE MURCIÉLAGO:\n");
    resultado.append(traducido.toString());

    resultadoArea.setText(resultado.toString());
}

// Método auxiliar para contar repeticiones de caracteres
private int contarRepeticiones(String texto, String caracteres) {
    int count = 0;
    for (char c : texto.toCharArray()) {
        if (caracteres.indexOf(c) != -1) {
            count++;
        }
    }
    return count;
}



    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        textArea = new javax.swing.JTextArea();
        btnProcesar = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        resultadoArea = new javax.swing.JTextArea();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane4.setViewportView(jTextArea1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(new java.awt.Dimension(470, 650));

        textArea.setColumns(20);
        textArea.setRows(5);
        jScrollPane2.setViewportView(textArea);

        btnProcesar.setText("Procesar");
        btnProcesar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcesarActionPerformed(evt);
            }
        });

        resultadoArea.setEditable(false);
        resultadoArea.setBackground(new java.awt.Color(240, 240, 240));
        resultadoArea.setColumns(20);
        resultadoArea.setRows(5);
        jScrollPane3.setViewportView(resultadoArea);

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(181, 181, 181)
                        .addComponent(btnProcesar))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
                            .addComponent(jScrollPane3))))
                .addGap(10, 10, 10))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnProcesar)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 303, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnProcesarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcesarActionPerformed
        // TODO add your handling code here:
        procesarTexto();
    }//GEN-LAST:event_btnProcesarActionPerformed

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
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
       // java.awt.EventQueue.invokeLater(() -> new AnalizadorTexto().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnProcesar;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea resultadoArea;
    private javax.swing.JTextArea textArea;
    // End of variables declaration//GEN-END:variables
}
