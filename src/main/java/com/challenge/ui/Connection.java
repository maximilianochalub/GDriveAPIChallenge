package com.challenge.ui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.challenge.connection.DriveConnection;
import com.challenge.controllers.Application;

import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import java.awt.Font;

@SpringBootApplication
public class Connection extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	// ejecucion 
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Connection frame = new Connection();
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// creamos frame
	public Connection() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 339, 288);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		contentPane.setBorder(null);
		
		JButton btnWordSearch = new JButton("Buscar Palabra");
		btnWordSearch.setBounds(45, 100, 228, 45);
		contentPane.add(btnWordSearch);
		btnWordSearch.addMouseListener(new MouseAdapter() { // acciones al presionar boton buscar palabra
			@Override
			public void mouseClicked(MouseEvent e) {
				
				if (DriveConnection.connect(0)) { // verificamos resultado de la conexion
					
					JOptionPane.showMessageDialog(null, "Conectado\n\nPresione Aceptar para comenzar el booteo", "Conexión a Google Drive - Búsqueda de una palabra", 1);
					setVisible(false); 
					dispose();
					
					SpringApplication.run(Application.class); // booteamos la aplicacion con spring boot 
					
				} else { 
					
					JOptionPane.showMessageDialog(null, "No conectado", "Conexión a Google Drive - Búsqueda de una palabra", 0);
					
				}
				
			}
		});

		JButton btnCreateFile = new JButton("Crear Archivo");
		btnCreateFile.setBounds(45, 168, 228, 45);
		contentPane.add(btnCreateFile);
		btnCreateFile.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				if (DriveConnection.connect(1)) { // verificamos resultado de la conexion
					
					JOptionPane.showMessageDialog(null, "Conectado!\n\nPresione Aceptar para comenzar el booteo", "Conexión a Google Drive - Creación de un archivo", 1);
					SpringApplication.run(Application.class);
					setVisible(false); 
					dispose();
				
				} else { 
					
					JOptionPane.showMessageDialog(null, "No conectado", "Conexión a Google Drive - Creación de un archivo", 0);
					
				}
				
			}
		});

		JLabel lblWelcome = new JLabel("Bienvenido!");
		lblWelcome.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblWelcome.setBounds(114, 11, 118, 28);
		contentPane.add(lblWelcome);
		
		JLabel lblOperationSelection = new JLabel("Para autenticarse, seleccione la operación que desee realizar");
		lblOperationSelection.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblOperationSelection.setBounds(22, 63, 334, 14);
		contentPane.add(lblOperationSelection);
	}
}
