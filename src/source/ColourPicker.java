package source;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.Dimension;
import javax.swing.JPanel;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Robot;

import javax.swing.JTextField;
import javax.swing.ImageIcon;
import java.awt.Component;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Cursor;

/**
 * Colour Picker Project.
 * 
 * @author Hamish Dickson - 550167
 *
 */

public class ColourPicker {

	private JFrame frame;
	private JTextField txtHTML;
	private JTextField txtRGB;

	/**
	 * global variable to store the mouseMotionListener
	 */
	private MouseMotionListener listener;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ColourPicker window = new ColourPicker();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ColourPicker() {
		initialise();
	}

	/**
	 * Changes the colour of the UI based on current mouse position
	 * 
	 * @param e
	 *            MouseEvent to be passed in
	 * @param lblColourWheel
	 *            Label to have background changed
	 * @param panel
	 *            panel to have background changed
	 */
	public void changeColour(MouseEvent e, JLabel lblColourWheel, JPanel panel) {
		// create new robot
		Robot robot = null;
		try {
			robot = new Robot();
		} catch (AWTException f) {
			f.printStackTrace();
		}

		// get the colour at the current mouse pointer location
		Color colour = robot.getPixelColor(e.getXOnScreen(), e.getYOnScreen());

		// set the colour of the background and the panel to the colour at the
		// mouse pointer
		lblColourWheel.setBackground(colour);
		panel.setBackground(colour);
	}

	/**
	 * Code executed when the mouse is clicked. if the left mouse button was
	 * clicked then the colour is changed, the text fields are changed and a
	 * mouseMotionListener is removed. if any other mouse button was clicked
	 * then reset the program to default state
	 * 
	 * @param e
	 *            MouseEvent to be passed in
	 * @param lblColourWheel
	 *            Label to be edited
	 * @param panel
	 *            panel to be edited
	 */
	public void colourClicked(MouseEvent e, JLabel lblColourWheel, JPanel panel) {
		if (e.getButton() == 1) {
			lblColourWheel.removeMouseMotionListener(listener);
			changeColour(e, lblColourWheel, panel);
			setText(e);
		} else {
			reset(lblColourWheel);
		}
	}

	/**
	 * Method to set the text of the text fields
	 * 
	 * @param e
	 *            MouseEvent to be passed in
	 */
	public void setText(MouseEvent e) {
		// create a new robot
		Robot robot = null;
		try {
			robot = new Robot();
		} catch (AWTException f) {
			f.printStackTrace();
		}

		// get the colour at the mouse pointer location
		Color colour = robot.getPixelColor(e.getXOnScreen(), e.getYOnScreen());	
		// get the red, green and blue values from the colour
		int red = colour.getRed();
		int green = colour.getGreen();
		int blue = colour.getBlue();

		// create a string with a format
		String htmlValue = ("#" + Integer.toHexString(colour.getRGB() & 0x00ffffff));

		// set the text to the colour values
		txtRGB.setText("" + red + ", " + green + ", " + blue);
		txtHTML.setText(htmlValue);
	}

	/**
	 * resets the program to default state
	 * 
	 * @param lblColourWheel
	 *            The colourWheel Panel
	 */
	public void reset(JLabel lblColourWheel) {
		// reset the labels and text fields
		lblColourWheel.addMouseMotionListener(listener);
		lblColourWheel.setIcon(new ImageIcon(ColourPicker.class.getResource("/source/Colour Wheel.png")));
		txtRGB.setText("");
		txtHTML.setText("");
	}

	/**
	 * Opens a fileChooser for the user to upload an image
	 * 
	 * @param lblColourWheel
	 *            the colourWheel Panel
	 */
	public void uploadImage(JLabel lblColourWheel) {
		ImageIcon scaledImage = null;
		try {
			// create a fileChooser for the user to select a file
			JFileChooser fc = new JFileChooser("upload");
			fc.setAcceptAllFileFilterUsed(false);

			// create filters for the files so that only images can be uploaded
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fc.addChoosableFileFilter(new FileNameExtensionFilter("Images", "jpg", "png", "bmp", "gif"));

			// store the return value in a variable from the fileChooser
			int returnValue = fc.showOpenDialog(null);
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();

				// get the original image from the file path selected
				ImageIcon originalImage = new ImageIcon(file.getAbsolutePath());

				// scale the image appropriately if needed
				if (originalImage.getIconHeight() >= 600 || originalImage.getIconWidth() >= 600) {
					scaledImage = new ImageIcon(originalImage.getImage()
							.getScaledInstance(originalImage.getIconWidth() / 2, originalImage.getIconHeight() / 2, 0));
				} else {
					scaledImage = originalImage;
				}

				// set the image to the selected image and reset the text and
				// actionListener
				lblColourWheel.setIcon(scaledImage);
				lblColourWheel.addMouseMotionListener(listener);
				txtRGB.setText("");
				txtHTML.setText("");
			}

		} catch (Exception e) { // shouldn't catch any errors as the file type
								// is restricted in JFileChooser
			System.out.println("Error Occured! Please try again!");
		}
	}

	/**
	 * Initialise the contents of the frame.
	 */
	private void initialise() {
		frame = new JFrame();
		frame.setPreferredSize(new Dimension(1920, 1080));
		frame.setResizable(false);
		frame.setMaximumSize(new Dimension(1280, 720));
		frame.setMinimumSize(new Dimension(1280, 720));
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Colour Picker");
		
		
		JPanel panel = new JPanel();

		frame.getContentPane().add(panel, BorderLayout.NORTH);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 536, 192, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_panel.rowHeights = new int[] { 0, 0, 0, 56, 0 };
		gbl_panel.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0,
				Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		panel.setLayout(gbl_panel);

		JLabel lblColourPicker = new JLabel("Colour Picker");
		lblColourPicker.setFont(new Font("Agency FB", Font.PLAIN, 46));
		GridBagConstraints gbc_lblColourPicker = new GridBagConstraints();
		gbc_lblColourPicker.insets = new Insets(0, 0, 5, 5);
		gbc_lblColourPicker.anchor = GridBagConstraints.NORTHWEST;
		gbc_lblColourPicker.gridx = 1;
		gbc_lblColourPicker.gridy = 0;
		panel.add(lblColourPicker, gbc_lblColourPicker);

		JPanel panel_1 = new JPanel();
		panel_1.setOpaque(false);
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.gridheight = 4;
		gbc_panel_1.insets = new Insets(0, 0, 5, 0);
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 13;
		gbc_panel_1.gridy = 0;
		panel.add(panel_1, gbc_panel_1);

		JLabel lblDeignedAndDeveloped = new JLabel("Designed And Developed By: ");
		lblDeignedAndDeveloped.setFont(new Font("Agency FB", Font.PLAIN, 20));
		panel_1.add(lblDeignedAndDeveloped);

		JLabel lblHamishDickson = new JLabel("Hamish Dickson - ");
		lblHamishDickson.setFont(new Font("Agency FB", Font.PLAIN, 20));
		lblHamishDickson.setDoubleBuffered(true);
		panel_1.add(lblHamishDickson);

		JLabel label = new JLabel("550167");
		label.setFont(new Font("Agency FB", Font.PLAIN, 20));
		panel_1.add(label);

		JButton btnUploadImage = new JButton("Upload Image");

		btnUploadImage.setFont(new Font("Agency FB", Font.PLAIN, 18));
		GridBagConstraints gbc_btnUploadImage = new GridBagConstraints();
		gbc_btnUploadImage.insets = new Insets(0, 0, 5, 5);
		gbc_btnUploadImage.gridx = 1;
		gbc_btnUploadImage.gridy = 1;
		panel.add(btnUploadImage, gbc_btnUploadImage);

		JLabel lblRgb = new JLabel("RGB:");
		lblRgb.setFont(new Font("Agency FB", Font.PLAIN, 18));
		GridBagConstraints gbc_lblRgb = new GridBagConstraints();
		gbc_lblRgb.anchor = GridBagConstraints.EAST;
		gbc_lblRgb.insets = new Insets(0, 0, 5, 5);
		gbc_lblRgb.gridx = 0;
		gbc_lblRgb.gridy = 2;
		panel.add(lblRgb, gbc_lblRgb);

		txtRGB = new JTextField();
		txtRGB.setFont(new Font("Agency FB", Font.PLAIN, 18));
		txtRGB.setEditable(false);
		txtRGB.setColumns(10);
		GridBagConstraints gbc_txtRGB = new GridBagConstraints();
		gbc_txtRGB.insets = new Insets(0, 0, 5, 5);
		gbc_txtRGB.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtRGB.gridx = 1;
		gbc_txtRGB.gridy = 2;
		panel.add(txtRGB, gbc_txtRGB);

		JLabel lblHtml = new JLabel("HTML:");
		lblHtml.setFont(new Font("Agency FB", Font.PLAIN, 18));
		GridBagConstraints gbc_lblHtml = new GridBagConstraints();
		gbc_lblHtml.anchor = GridBagConstraints.EAST;
		gbc_lblHtml.insets = new Insets(0, 0, 0, 5);
		gbc_lblHtml.gridx = 0;
		gbc_lblHtml.gridy = 3;
		panel.add(lblHtml, gbc_lblHtml);

		txtHTML = new JTextField();
		txtHTML.setEditable(false);
		txtHTML.setFont(new Font("Agency FB", Font.PLAIN, 18));
		GridBagConstraints gbc_txtHTML = new GridBagConstraints();
		gbc_txtHTML.insets = new Insets(0, 0, 0, 5);
		gbc_txtHTML.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtHTML.gridx = 1;
		gbc_txtHTML.gridy = 3;
		panel.add(txtHTML, gbc_txtHTML);
		txtHTML.setColumns(10);
		
		JLabel lblColourWheel = new JLabel("");
		lblColourWheel.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		lblColourWheel.setOpaque(true);
		listener = new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				changeColour(e, lblColourWheel, panel);
			}
		};
		lblColourWheel.addMouseMotionListener(listener);
		lblColourWheel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				colourClicked(e, lblColourWheel, panel);
			}
		});
		lblColourWheel.setHorizontalAlignment(SwingConstants.CENTER);
		lblColourWheel.setDoubleBuffered(true);
		lblColourWheel.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblColourWheel.setIcon(new ImageIcon(ColourPicker.class.getResource("/source/Colour Wheel.png")));
		frame.getContentPane().add(lblColourWheel, BorderLayout.CENTER);

		btnUploadImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				uploadImage(lblColourWheel);
			}
		});
		
		JOptionPane.showMessageDialog(frame,"Left click to pick a colour.\nRight click to reset the program.");
	}
}
