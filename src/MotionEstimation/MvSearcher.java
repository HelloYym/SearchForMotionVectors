package MotionEstimation;

import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.plaf.SliderUI;
import javax.swing.text.html.HTMLDocument.HTMLReader.BlockAction;

public class MvSearcher extends JFrame {

	private ImagePanel drawPanel;
	private JPanel topToolbar, leftToolbar;

	public JButton SequentialMethonBtn;
	public JButton LogarithmicMethonBtn;
	public JButton HierarchicalMethonBtn;
	public JButton RunBtn;

	public MontionVectorsSearcher searcher = new SequentialSearchMethod();

	BufferedImage targetImg = null;
	BufferedImage referenceImg = null;

	public ImagePanel targetImgPanel;
	public ImagePanel referenceImgPanel;
	
	public JTextField messageField;

	public MvSearcher() throws FileNotFoundException, IOException {

		setLayout(new BorderLayout());

		leftToolbar = new JPanel();
		leftToolbar.setLayout(new GridLayout(1, 5));
		leftToolbar.setBackground(Color.WHITE);
		leftToolbar.setBorder(BorderFactory.createLineBorder(Color.CYAN, 3));

		leftToolbar.add(new JLabel("Search Method", JLabel.CENTER));
		leftToolbar.add(SequentialMethonBtn = new JButton("Sequential"));
		leftToolbar.add(LogarithmicMethonBtn = new JButton("Logarithmic"));
		leftToolbar.add(HierarchicalMethonBtn = new JButton("Hierarchical"));
		leftToolbar.add(RunBtn = new JButton("Run"));
		
		RunBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				long startTime = System.currentTimeMillis();// 获取当前时间
				searcher.searchMotionVectors();
				long endTime = System.currentTimeMillis();
				messageField.setText("程序运行时间：" + (endTime - startTime) + "ms");
			}
		});
		
		SequentialMethonBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searcher = new SequentialSearchMethod();
				searcher.setTargetFrame(targetImg);
				searcher.setReferenceFrame(referenceImg);
			}
		});

		LogarithmicMethonBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searcher = new LogarithmicSearchMethod();
				searcher.setTargetFrame(targetImg);
				searcher.setReferenceFrame(referenceImg);
			}
		});

		HierarchicalMethonBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searcher = new HierarchicalSearchMethod();
				searcher.setTargetFrame(targetImg);
				searcher.setReferenceFrame(referenceImg);
			}
		});

		targetImgPanel = new ImagePanel();
		referenceImgPanel = new ImagePanel();

		JPanel referenceFramePanel = new JPanel(new BorderLayout());
		referenceFramePanel.add(new JLabel("Reference Frame", JLabel.CENTER), BorderLayout.NORTH);
		referenceFramePanel.add(referenceImgPanel, BorderLayout.CENTER);
		JButton referenceImportBtn = new JButton("Import");
		referenceFramePanel.add(referenceImportBtn, BorderLayout.SOUTH);
		referenceImportBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser("~/");
				jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				jfc.showOpenDialog(null);
				try {
					referenceImg = ImageIO.read(jfc.getSelectedFile());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				searcher.setReferenceFrame(referenceImg);
				referenceImgPanel.setImage(referenceImg);
				repaint();
			}
		});

		JPanel targetFramePanel = new JPanel(new BorderLayout());
		targetFramePanel.add(new JLabel("Target Frame", JLabel.CENTER), BorderLayout.NORTH);
		targetFramePanel.add(targetImgPanel, BorderLayout.CENTER);
		JButton targetImportBtn = new JButton("Import");
		targetFramePanel.add(targetImportBtn, BorderLayout.SOUTH);
		targetImportBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser("~/");
				jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				jfc.showOpenDialog(null);
				try {
					targetImg = ImageIO.read(jfc.getSelectedFile());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				searcher.setTargetFrame(targetImg);
				targetImgPanel.setImage(targetImg);
				repaint();
			}
		});
		
		targetImgPanel.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				MotionVector mv = searcher.searchMotionVector((int) (e.getX()/targetImgPanel.scaledRatio) - 8, (int) (e.getY() / targetImgPanel.scaledRatio -8));
				messageField.setText("x = " + mv.u + " y = " + mv.v);
			}
		});

		JPanel displayPanel = new JPanel(new GridLayout(1, 2));
		displayPanel.add(referenceFramePanel);
		displayPanel.add(targetFramePanel);

		add(leftToolbar, BorderLayout.NORTH);
		add(displayPanel, BorderLayout.CENTER);
		
		
		messageField = new JTextField("Hello");
		JPanel textPanel = new JPanel(new BorderLayout());
		textPanel.add(messageField, BorderLayout.CENTER);
		textPanel.setSize(600, 200);
		add(textPanel, BorderLayout.SOUTH);
	}

	public static void main(String[] args) throws FileNotFoundException, IOException {
		MvSearcher drawingFrame = new MvSearcher();
		drawingFrame.setSize(1500, 768);
		drawingFrame.setTitle("Motion Vectors Searcher");
		drawingFrame.setLocationRelativeTo(null);
		drawingFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		drawingFrame.setVisible(true);
	}

	class ImagePanel extends JPanel {
		private Image image;
		public double scaledRatio;

		public ImagePanel() {
			setBorder(BorderFactory.createLineBorder(Color.CYAN, 3));
		}

		public void setImage(BufferedImage image) {
			int width = image.getWidth();
			int height = image.getHeight();
			scaledRatio = 640 / (double)width;
			System.out.println(scaledRatio);
			this.image = image.getScaledInstance(640, (int) (height * scaledRatio), Image.SCALE_SMOOTH);
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			if (image == null)
				return;
			g.drawImage(image, 0, 0, null);
		}

	}
}
