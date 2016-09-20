package macrovis.gui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.Element;

import macrovis.util.ExpansionTrace;
import macrovis.util.Util;

public class MacroVisualiser extends JFrame {

	private List<ExpansionTrace> traces = new ArrayList<ExpansionTrace>();

	private byte[] sourceBytes;
	private int traceDepth = 0;
	private int depth = 0;
	private ExpansionTrace traceExpn = null;
	private HashMap<ExpansionTrace, Integer> sizes = new HashMap<ExpansionTrace, Integer>();
	public String filename = null;
	public boolean changed = false;

	private static final long serialVersionUID = 1L;

	public MacroVisualiser() {
		initComponents();
	}

    private void initComponents() {
        rootPane = new javax.swing.JPanel();
        tabbedPane = new javax.swing.JTabbedPane();
        sourcePane = new javax.swing.JPanel();
        sourceScrollPane = new javax.swing.JScrollPane();
        sourceSplitPane = new javax.swing.JSplitPane();
        sourceText = new javax.swing.JTextPane();
        consolePane = new javax.swing.JPanel();
        consoleScrollPane = new javax.swing.JScrollPane();
        consoleText = new javax.swing.JTextPane();
        consoleLabel = new javax.swing.JLabel();
        compileButton = new javax.swing.JButton();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(250, 32), new java.awt.Dimension(0, 0));
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(355, 32), new java.awt.Dimension(0, 0));
        viewerPane = new javax.swing.JPanel();
        viewerScrollPane = new javax.swing.JScrollPane();
        viewerSplitPane = new javax.swing.JSplitPane();
        viewText = new javax.swing.JTextPane();
        viewButtons[0] = new javax.swing.JButton();
        viewButtons[1] = new javax.swing.JButton();
        viewButtons[2] = new javax.swing.JButton();
        viewButtons[3] = new javax.swing.JButton();
        macroPane = new javax.swing.JPanel();
        nameLabel = new javax.swing.JLabel();
        traceScrollPanel = new javax.swing.JScrollPane();
        traceText = new javax.swing.JTextArea();
        traceLabel = new javax.swing.JLabel();
        traceButtons[0] = new javax.swing.JButton();
        traceButtons[1] = new javax.swing.JButton();
        traceButtons[2] = new javax.swing.JButton();
        traceButtons[3] = new javax.swing.JButton();
        showChangesBox = new javax.swing.JCheckBox();
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 32), new java.awt.Dimension(150, 32), new java.awt.Dimension(32767, 32));
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 32), new java.awt.Dimension(387, 32), new java.awt.Dimension(32767, 32));
        menuBar = new javax.swing.JMenuBar();
        menu = new javax.swing.JMenu();
        newItem = new javax.swing.JMenuItem();
        openItem = new javax.swing.JMenuItem();
        saveItem = new javax.swing.JMenuItem();
        saveAsItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowListener() {
			public void windowClosing(WindowEvent e) {
				if (changed) {
					int choice = JOptionPane.showConfirmDialog(rootPane,
															   "You have unsaved changes, are you sure you wish to close?",
															   "Warning",
															   JOptionPane.OK_CANCEL_OPTION);
					if (choice == JOptionPane.CANCEL_OPTION)
						return;
				}
				System.exit(0);
			}
			public void windowOpened(WindowEvent e) {}
			public void windowClosed(WindowEvent e) {}
			public void windowIconified(WindowEvent e) {}
			public void windowDeiconified(WindowEvent e) {}
			public void windowActivated(WindowEvent e) {}
			public void windowDeactivated(WindowEvent e) {}
        });
        setTitle("Macro Visualiser");
        //setPreferredSize(new java.awt.Dimension(1080, 720));

        rootPane.setPreferredSize(new java.awt.Dimension(1080, 700));

        tabbedPane.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        tabbedPane.setAlignmentX(0.0F);
        tabbedPane.setAlignmentY(0.0F);
        tabbedPane.setPreferredSize(new java.awt.Dimension(1080, 700));

        sourcePane.setPreferredSize(new java.awt.Dimension(1080, 680));

        sourceSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        sourceSplitPane.setResizeWeight(0.5);

        sourceScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        sourceScrollPane.setPreferredSize(new java.awt.Dimension(1080, 540));
        sourceText.setFont(new java.awt.Font("Consolas", Font.PLAIN, 12));

        sourceText.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) { changed = true; }
			public void removeUpdate(DocumentEvent e) { changed = true; }
			public void changedUpdate(DocumentEvent e) { changed = true; }
        });

        sourceScrollPane.setViewportView(sourceText);
        sourceSplitPane.setLeftComponent(sourceScrollPane);

        consoleScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        consoleScrollPane.setAlignmentX(0.0F);
        consoleScrollPane.setAlignmentY(1.0F);
        consoleScrollPane.setPreferredSize(new java.awt.Dimension(1080, 80));

        consoleText.setEditable(false);
        consoleText.setFont(new java.awt.Font("Consolas", Font.PLAIN, 12));
        consoleText.setPreferredSize(new java.awt.Dimension(1080, 80));
        consoleScrollPane.setViewportView(consoleText);

        consoleLabel.setText("Console Output:");

        compileButton.setText("Compile & Run");
        compileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                compile();
            }
        });
        compileButton.setEnabled(false);

        javax.swing.GroupLayout consolePaneLayout = new javax.swing.GroupLayout(consolePane);
        consolePane.setLayout(consolePaneLayout);
        consolePaneLayout.setHorizontalGroup(
            consolePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(consolePaneLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(consoleLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(filler2, javax.swing.GroupLayout.DEFAULT_SIZE, 351, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(compileButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(filler1, javax.swing.GroupLayout.DEFAULT_SIZE, 456, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(consoleScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        consolePaneLayout.setVerticalGroup(
            consolePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(consolePaneLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(consolePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(consolePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(filler1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(filler2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(compileButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(consoleLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(consoleScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE))
        );

        sourceSplitPane.setRightComponent(consolePane);

        javax.swing.GroupLayout sourcePaneLayout = new javax.swing.GroupLayout(sourcePane);
        sourcePane.setLayout(sourcePaneLayout);
        sourcePaneLayout.setHorizontalGroup(
            sourcePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sourcePaneLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(sourceSplitPane)
                .addGap(0, 0, 0))
        );
        sourcePaneLayout.setVerticalGroup(
            sourcePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sourcePaneLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(sourceSplitPane)
                .addGap(0, 0, 0))
        );

        tabbedPane.addTab("Source Editor", sourcePane);

        viewerPane.setPreferredSize(new java.awt.Dimension(1080, 680));

        viewerSplitPane.setResizeWeight(0.5);
        viewerScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        viewerScrollPane.setPreferredSize(new java.awt.Dimension(840, 630));

        viewText.setEditable(false);
        viewText.setFont(new java.awt.Font("Consolas", Font.PLAIN, 12));
        viewText.setPreferredSize(new java.awt.Dimension(600, 580));
        viewText.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				viewClicked(e);
			}
			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}

        });
        viewerScrollPane.setViewportView(viewText);
        viewerSplitPane.setLeftComponent(viewerScrollPane);

        viewButtons[0].setFont(new java.awt.Font("Dialog", 1, 18));
        viewButtons[0].setText("◄◄");

        viewButtons[1].setFont(new java.awt.Font("Dialog", 1, 18));
        viewButtons[1].setText("◄");

        viewButtons[2].setFont(new java.awt.Font("Dialog", 1, 18));
        viewButtons[2].setText("►");

        viewButtons[3].setFont(new java.awt.Font("Dialog", 1, 18));
        viewButtons[3].setText("►►");

        macroPane.setMinimumSize(new java.awt.Dimension(250, 0));
        macroPane.setPreferredSize(new java.awt.Dimension(250, 630));

        nameLabel.setText("Name: No Macro Selected");
        nameLabel.setEnabled(false);

        traceScrollPanel.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        traceScrollPanel.setPreferredSize(new java.awt.Dimension(228, 78));

        traceText.setEditable(false);
        traceText.setFont(new java.awt.Font("Consolas", Font.PLAIN, 12));
        traceText.setColumns(18);
        traceText.setRows(5);
        traceScrollPanel.setViewportView(traceText);

        traceLabel.setText("Trace:");
        traceLabel.setEnabled(true);

        traceButtons[0].setText("◄◄");
        traceButtons[0].setEnabled(false);
        traceButtons[0].setPreferredSize(new java.awt.Dimension(42, 24));

        traceButtons[1].setText("◄");
        traceButtons[1].setEnabled(false);
        traceButtons[1].setPreferredSize(new java.awt.Dimension(42, 24));

        traceButtons[2].setText("►");
        traceButtons[2].setEnabled(false);
        traceButtons[2].setPreferredSize(new java.awt.Dimension(42, 24));

        traceButtons[3].setText("►►");
        traceButtons[3].setEnabled(false);
        traceButtons[3].setPreferredSize(new java.awt.Dimension(42, 24));


        viewButtons[0].setEnabled(false);
        viewButtons[1].setEnabled(false);
        viewButtons[2].setEnabled(false);
        viewButtons[3].setEnabled(false);

        viewButtons[0].addActionListener(new java.awt.event.ActionListener() {
        	public void actionPerformed(java.awt.event.ActionEvent evt) {
        		step(0, false);
        	}
        });
        traceButtons[0].addActionListener(new java.awt.event.ActionListener() {
        	public void actionPerformed(java.awt.event.ActionEvent evt) {
        		step(0, true);
        	}
        });
        viewButtons[1].addActionListener(new java.awt.event.ActionListener() {
        	public void actionPerformed(java.awt.event.ActionEvent evt) {
        		step(1, false);
        	}
        });
        traceButtons[1].addActionListener(new java.awt.event.ActionListener() {
        	public void actionPerformed(java.awt.event.ActionEvent evt) {
        		step(1, true);
        	}
        });
        viewButtons[2].addActionListener(new java.awt.event.ActionListener() {
        	public void actionPerformed(java.awt.event.ActionEvent evt) {
        		step(2, false);
        	}
        });
        traceButtons[2].addActionListener(new java.awt.event.ActionListener() {
        	public void actionPerformed(java.awt.event.ActionEvent evt) {
        		step(2, true);
        	}
        });
        viewButtons[3].addActionListener(new java.awt.event.ActionListener() {
        	public void actionPerformed(java.awt.event.ActionEvent evt) {
        		step(3, false);
        	}
        });
        traceButtons[3].addActionListener(new java.awt.event.ActionListener() {
        	public void actionPerformed(java.awt.event.ActionEvent evt) {
        		step(3, true);
        	}
        });

        showChangesBox.setText("Show Changes in Main View");
        showChangesBox.setEnabled(false);
        showChangesBox.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        showChangesBox.setIconTextGap(15);
        showChangesBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (showChangesBox.isSelected()) {
					traceExpn.setDepth(traceDepth);
					setViewText();
					toggleViewButtons();
				}
			}
        });

        javax.swing.GroupLayout macroPaneLayout = new javax.swing.GroupLayout(macroPane);
        macroPane.setLayout(macroPaneLayout);
        macroPaneLayout.setHorizontalGroup(
            macroPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(macroPaneLayout.createSequentialGroup()
                .addGroup(macroPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(macroPaneLayout.createSequentialGroup()
                        .addComponent(traceButtons[0], javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(traceButtons[1], javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(traceButtons[2], javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(traceButtons[3], javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(traceScrollPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(macroPaneLayout.createSequentialGroup()
                    		.addContainerGap()
                            .addGroup(macroPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(nameLabel)
                                .addComponent(traceLabel))
                            .addGap(0, 0, Short.MAX_VALUE)))
                    .addContainerGap())
                .addGroup(macroPaneLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(showChangesBox)
                    .addContainerGap(32, Short.MAX_VALUE))
        );
        macroPaneLayout.setVerticalGroup(
            macroPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(macroPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(nameLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(showChangesBox)
                .addGap(6, 6, 6)
                .addComponent(traceLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(traceScrollPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 508, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(macroPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(traceButtons[1], javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                    .addComponent(traceButtons[0], javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(traceButtons[2], javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(traceButtons[3], javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );

        viewerSplitPane.setRightComponent(macroPane);

        javax.swing.GroupLayout viewerPaneLayout = new javax.swing.GroupLayout(viewerPane);
        viewerPane.setLayout(viewerPaneLayout);
        viewerPaneLayout.setHorizontalGroup(
            viewerPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(viewerPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(filler3, javax.swing.GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(viewButtons[0], javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(viewButtons[1], javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(viewButtons[2], javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(viewButtons[3], javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(filler4, javax.swing.GroupLayout.DEFAULT_SIZE, 507, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(viewerSplitPane)
        );
        viewerPaneLayout.setVerticalGroup(
            viewerPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(viewerPaneLayout.createSequentialGroup()
                .addComponent(viewerSplitPane)
                .addGap(8, 8, 8)
                .addGroup(viewerPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(viewerPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(viewButtons[1], javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(viewButtons[0], javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(viewButtons[2], javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(viewButtons[3]))
                    .addComponent(filler3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(filler4, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0))
        );

        tabbedPane.addTab("Expansion Viewer", viewerPane);

        javax.swing.GroupLayout rootPaneLayout = new javax.swing.GroupLayout(rootPane);
        rootPane.setLayout(rootPaneLayout);
        rootPaneLayout.setHorizontalGroup(
            rootPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        rootPaneLayout.setVerticalGroup(
            rootPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 699, Short.MAX_VALUE)
        );

        tabbedPane.getAccessibleContext().setAccessibleName("TabPanel");

        menu.setText("File");

        newItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        newItem.setText("New File");
        newItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                open(true);
            }
        });
        menu.add(newItem);

        openItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        openItem.setText("Open File");
        openItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                open(false);
            }
        });
        menu.add(openItem);

        saveItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        saveItem.setText("Save File");
        saveItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                save(false);
            }
        });
        menu.add(saveItem);

        saveAsItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        saveAsItem.setText("Save As");
        saveAsItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                save(true);
            }
        });
        menu.add(saveAsItem);

        menuBar.add(menu);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(rootPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(rootPane, javax.swing.GroupLayout.DEFAULT_SIZE, 699, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        pack();
        viewerSplitPane.setDividerLocation(0.75);
        sourceSplitPane.setDividerLocation(0.75);
    }

    private void open(boolean newFile) {
    	if (changed) {
    		int choice = JOptionPane.showConfirmDialog(rootPane,
					   "You have unsaved changes, are you sure you wish to abandom them?",
					   "Warning",
					   JOptionPane.OK_CANCEL_OPTION);
    		if (choice == JOptionPane.CANCEL_OPTION)
    			return;
    	}
    	JFileChooser fc = new JFileChooser();
    	if (filename != null) {
    		fc.setCurrentDirectory(Paths.get(filename).getParent().toFile());
    	}
    	fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
    	String title = (newFile) ? "Open New File" : "Open File";
    	fc.setDialogTitle(title);
    	fc.setFileFilter(new FileNameExtensionFilter("Rust files", "rs", "rlib"));
    	int choice = fc.showOpenDialog(rootPane);
    	if (choice == JFileChooser.APPROVE_OPTION) {
    		File f = fc.getSelectedFile();
    		if (newFile) {
    			if (f.exists()) {
    				choice = JOptionPane.showConfirmDialog(rootPane,
    													   "This action will overwrite an existing file. Do you wish to continue?",
    													   "Warning",
    													   JOptionPane.OK_CANCEL_OPTION);
    				if (choice == JOptionPane.OK_OPTION) {
    					try {
    						Files.delete(Paths.get(f.getAbsolutePath()));
    						f.createNewFile();
    						filename = f.getAbsolutePath();
        					sourceBytes = new byte[0];
        					sourceText.setText("");
        					viewText.setText("");
        					consoleText.setText("");
        					changed = false;
        					traceDepth = 0;
        					depth = 0;
        					traceExpn = null;
        					traces = new ArrayList<ExpansionTrace>();
        					sizes = new HashMap<ExpansionTrace, Integer>();
        					for (int i = 0; i < 4; i++) {
        						viewButtons[i].setEnabled(false);
        						traceButtons[i].setEnabled(false);
        					}
        					nameLabel.setText("No macro selected");
        					traceText.setText("");
        					nameLabel.setEnabled(false);
        					showChangesBox.setEnabled(false);
        					compileButton.setEnabled(true);
    					} catch(IOException e) {
    						JOptionPane.showMessageDialog(rootPane, "Could not overwrite file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    					}
    				}
    				return;
    			}
    			changed = false;
    			try {
    				f.createNewFile();
					filename = f.getAbsolutePath();
					sourceBytes = new byte[0];
					sourceText.setText("");
					viewText.setText("");
					consoleText.setText("");
					changed = false;
					traceDepth = 0;
					depth = 0;
					traceExpn = null;
					traces = new ArrayList<ExpansionTrace>();
					sizes = new HashMap<ExpansionTrace, Integer>();
					for (int i = 0; i < 4; i++) {
						viewButtons[i].setEnabled(false);
						traceButtons[i].setEnabled(false);
					}
					nameLabel.setText("No macro selected");
					traceText.setText("");
					nameLabel.setEnabled(false);
					showChangesBox.setEnabled(false);
					compileButton.setEnabled(true);
    			} catch(IOException e) {
    				JOptionPane.showMessageDialog(rootPane, "Could not create file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    			}
            } else {
            	if (!f.exists()) {
            		JOptionPane.showMessageDialog(rootPane, "Can not open file, does not exist", "Error", JOptionPane.ERROR_MESSAGE);
            		return;
            	}
            	try {
            		filename = f.getAbsolutePath();
            		sourceBytes = Files.readAllBytes(Paths.get(filename));
            		sourceText.setText(new String(sourceBytes));
					changed = false;
					traceDepth = 0;
					depth = 0;
					traceExpn = null;
					traces = new ArrayList<ExpansionTrace>();
					sizes = new HashMap<ExpansionTrace, Integer>();
					for (int i = 0; i < 4; i++) {
						viewButtons[i].setEnabled(false);
						traceButtons[i].setEnabled(false);
					}
					nameLabel.setText("No macro selected");
					traceText.setText("");
					nameLabel.setEnabled(false);
					showChangesBox.setEnabled(false);
            		compile();
            	} catch (IOException e) {
            		JOptionPane.showMessageDialog(rootPane, "Error reading file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            	}
            }
    	}
    }

    private void save(boolean newFile) {
        if (newFile) {
        	JFileChooser fc = new JFileChooser();
        	if (filename != null) {
        		fc.setCurrentDirectory(Paths.get(filename).getParent().toFile());
        	}
        	fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        	fc.setDialogTitle("Save File As");
        	fc.setFileFilter(new FileNameExtensionFilter("Rust files", "rs", "rlib"));
        	int choice = fc.showOpenDialog(rootPane);
        	if (choice == JFileChooser.APPROVE_OPTION) {
        		File f = fc.getSelectedFile();
        		if (f.exists()) {
    				choice = JOptionPane.showConfirmDialog(rootPane,
    													   "This action will overwrite an existing file. Do you wish to continue?",
    													   "Warning",
    													   JOptionPane.OK_CANCEL_OPTION);
    				if (choice != JOptionPane.OK_OPTION) {
    					return;
    				}
    			}
        		filename = f.getAbsolutePath();
        		changed = true;
        		save(false);
        	}
        }
        else {
        	if (!changed) {
        		return;
        	}
        	try {
        		Files.write(Paths.get(filename), sourceText.getText().getBytes());
        		sourceBytes = sourceText.getText().getBytes();
        		changed = false;
        	} catch (IOException e) {
        		JOptionPane.showMessageDialog(rootPane, "Error saving file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        	}
        }
    }

	private void compile() {
    	// Need to disable the compile button until we're done
    	consoleText.setText("Compiling...");
    	compileButton.setEnabled(false);

    	// Clear existing files, that way we know if the run was successful
    	String exeName = filename.substring(0, filename.lastIndexOf('.'));
    	File exe = new File(exeName);
    	if (exe.exists() && exe.canExecute()) {
    		exe.delete();
    	}
    	String parent = exe.getParent();
    	if (parent == null) parent = "";
    	String folder = parent + "/save-analysis-temp/";

    	File json = new File(folder + exe.getName() + ".json");
    	if (json.exists()) json.delete();

    	// Save the current file so we have something to run on
    	save(false);
    	// Find the location of the shell script - directory of the jar + /script/rustcompile.sh
    	String scriptDir = Paths.get("").toAbsolutePath().toString();
    	ProcessBuilder pb = new ProcessBuilder(scriptDir + "/script/rustcompile.sh");
    	pb.environment().put("FILENAME", filename);
    	pb.directory(new File(parent));
    	pb.redirectErrorStream(true);
    	try {
    		Process p = pb.start();
    		consoleText.setText("Compile Output:");
    		Util.ProcessStream stream = new Util.ProcessStream(p.getInputStream(), consoleText);
    		stream.start();
    		int result = p.waitFor();

    		if (json.exists()) {
    			traces = Util.readAnalysis(json.getAbsolutePath());
    			setBaseSizes();
                setViewText();
                viewButtons[2].setEnabled(true);
                viewButtons[3].setEnabled(true);
    		}
    		if (result == 0) {
    			pb.command(exeName);
    			consoleText.setText(consoleText.getText() + "\n- - -\nRun Output:\n");
    			stream = new Util.ProcessStream(p.getInputStream(), consoleText);
    			Process run = pb.start();
    			stream.start();
    			run.waitFor();
    		}
    	} catch(Exception e) {
    		JOptionPane.showMessageDialog(rootPane,
    									  "Error: Could not compile code:" + e.getMessage(),
    									  "Error",
    									  JOptionPane.ERROR_MESSAGE);
    		e.printStackTrace();
    	} finally {
            compileButton.setEnabled(true);
    	}
    }

    private void viewClicked(java.awt.event.MouseEvent evt) {
    	int pos = viewText.viewToModel(evt.getPoint());
    	Element map = viewText.getDocument().getDefaultRootElement();
    	int line = map.getElementIndex(pos);
    	int col = (pos - map.getElement(line).getStartOffset());
    	// Java line/col start from 0, Rust start from 1
    	line += 1; col += 1;
    	int size = Integer.MAX_VALUE;
    	ExpansionTrace prev = traceExpn;

    	// We want the smallest possible encompassing span
    	// (to allow for the case of macro calls inside macro args)
    	for (ExpansionTrace t: traces) {
    		if (t.encompasses(line, col) && sizes.get(t) < size) {
    			traceExpn = t;
    			size = t.span.size();
    		}
    	}
    	if (prev == traceExpn) {
    		traceExpn = null;
    		traceDepth = 0;
    		traceText.setText("");
    		nameLabel.setText("No macro selected");
    		nameLabel.setEnabled(false);
    		showChangesBox.setEnabled(false);
    		showChangesBox.setSelected(false);
    		for (int i = 0; i < 4; i++) {
    			traceButtons[i].setEnabled(false);
    		}
    	}
    	if (traceExpn != null) {
    		traceText.setText(traceExpn.getString());
    		nameLabel.setText("Name: " + traceExpn.name());
    		nameLabel.setEnabled(true);
    		traceDepth = traceExpn.depth();
    		showChangesBox.setEnabled(true);
    		toggleTraceButtons(traceExpn.size()-1);
    	}
    }

    private void step(int type, boolean isTrace) {
    	step(type, isTrace, true);
    }

    private void toggleTraceButtons(int maxIndex) {
    	if (traceDepth == 0) {
			traceButtons[0].setEnabled(false);
			traceButtons[1].setEnabled(false);
		} else {
			traceButtons[0].setEnabled(true);
			traceButtons[1].setEnabled(true);
		}
		if (traceDepth == maxIndex) {
			traceButtons[2].setEnabled(false);
			traceButtons[3].setEnabled(false);
		} else {
			traceButtons[2].setEnabled(true);
			traceButtons[3].setEnabled(true);
		}
    }

    private void step(int type, boolean isTrace, boolean setView) {
    	if (isTrace) {
    		int maxIndex = traceExpn.size()-1;
    		if (type == 0) traceDepth = 0;
    		if (type == 1) traceDepth -= (traceDepth > 0) ? 1 : 0;
    		if (type == 2) traceDepth += (traceDepth < maxIndex) ? 1: 0;
    		if (type == 3) traceDepth = maxIndex;
    		traceText.setText(traceExpn.getString(traceDepth));

    		// Toggle buttons appropriately
    		toggleTraceButtons(maxIndex);

    		// If applicable, apply changes to the left side as well
        	if (showChangesBox.isSelected() && setView) {
        		traceExpn.step(type);
        		setViewText();
        		toggleViewButtons();
        	}
    	}
    	else {
    		for (ExpansionTrace t: traces) {
    			if (!(depth >= t.size() && type == 1))
    				t.step(type);
    		}
    		if (showChangesBox.isSelected()) {
    			step(type, true, false);
    		}
    		toggleViewButtons();
    		setViewText();
    	}
    }

    private void toggleViewButtons() {
    	boolean start = true;
		boolean end = true;
		depth = 0;
		for (ExpansionTrace t: traces) {
			start &= t.start();
			end &= t.end();
			depth = Math.max(depth, t.depth());
		}

		// Limit buttons
		if (start) {
			viewButtons[0].setEnabled(false);
			viewButtons[1].setEnabled(false);
		} else {
			viewButtons[0].setEnabled(true);
			viewButtons[1].setEnabled(true);
		}
		if (end) {
			viewButtons[2].setEnabled(false);
			viewButtons[3].setEnabled(false);
		} else {
			viewButtons[2].setEnabled(true);
			viewButtons[3].setEnabled(true);
		}
    }

    // Given the source bytes, we iterate through every expansion,
	// inserting the replacement bytes where appropriate.
    private void setViewText() {
    	int index = 0; //Index into traces list
    	ArrayList<Byte> bytes = new ArrayList<Byte>();
    	for (int i = 0; i < sourceBytes.length; i++) {
    		if (index < traces.size() && traces.get(index).span.byteStart == i) {
    			ExpansionTrace t = traces.get(index);
    			//Add new bytes
    			for (byte b: t.getString().getBytes()) {
    				bytes.add(b);
    			}
    			index += 1;
    			i += sizes.get(t);
    		}
    		else {
    			bytes.add(sourceBytes[i]);
    		}
    	}
    	byte[] byteString = new byte[bytes.size()];
    	for (int i = 0; i < bytes.size(); i++) {
    		byteString[i] = bytes.get(i);
    	}
    	viewText.setText(new String(byteString));
    }

    // For every expansion trace, store its base length in bytes.
    // Needs to be done as the span only stores the name, not the arguments
    private void setBaseSizes() {
    	for (ExpansionTrace t: traces) {
    		int pos = t.span.byteEnd+1;
    		char openBraceType = (char)sourceBytes[pos];
    		char closeBraceType = ' ';
    		if (openBraceType == '(') closeBraceType = ')';
    		if (openBraceType == '[') closeBraceType = ']';
    		if (openBraceType == '{') closeBraceType = '}';
    		int braceCount = 1;
    		while (braceCount > 0 && pos < sourceBytes.length) {
    			char cur = (char)sourceBytes[++pos];
    			if (cur == openBraceType) braceCount += 1;
    			if (cur == closeBraceType) braceCount -= 1;
    		}
    		// Need to account for expression macros with semicolons
    		if (pos+1 < sourceBytes.length && (char)sourceBytes[pos+1] == ';')
    			pos += 1;
    		sizes.put(t, pos-t.span.byteStart);
    	}
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MacroVisualiser().setVisible(true);
            }
        });
    }

    // Variables declaration
    private javax.swing.JButton compileButton;
    private javax.swing.JLabel consoleLabel;
    private javax.swing.JPanel consolePane;
    private javax.swing.JScrollPane consoleScrollPane;
    private javax.swing.JTextPane consoleText;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler3;
    private javax.swing.Box.Filler filler4;
    private javax.swing.JCheckBox showChangesBox;
    private javax.swing.JPanel macroPane;
    private javax.swing.JMenu menu;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JMenuItem newItem;
    private javax.swing.JMenuItem openItem;
    private javax.swing.JPanel rootPane;
    private javax.swing.JMenuItem saveAsItem;
    private javax.swing.JMenuItem saveItem;
    private javax.swing.JPanel sourcePane;
    private javax.swing.JSplitPane sourceSplitPane;
    private javax.swing.JScrollPane sourceScrollPane;
    private javax.swing.JTextPane sourceText;
    private javax.swing.JTabbedPane tabbedPane;
    private javax.swing.JLabel traceLabel;
    private javax.swing.JScrollPane traceScrollPanel;
    private javax.swing.JTextArea traceText;
    private javax.swing.JTextPane viewText;
    private javax.swing.JPanel viewerPane;
    private javax.swing.JSplitPane viewerSplitPane;
    private javax.swing.JScrollPane viewerScrollPane;
    // Ordered left-to-right, jump-to-start, back, step, jump-to-end
    private javax.swing.JButton[] viewButtons = new javax.swing.JButton[4];
    private javax.swing.JButton[] traceButtons = new javax.swing.JButton[4];
    // End of variables declaration
}
