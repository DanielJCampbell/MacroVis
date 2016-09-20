package macrovis.gui;

import java.awt.Font;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import macrovis.util.Util;

/**
 *
 * Alternate version of the GUI aimed for use in control runs of the experiment.
 * Works with the pretty-expanded Rust command.
 */
@SuppressWarnings("serial")
public class ControlVisualiser extends javax.swing.JFrame {

	private String expandedText;
	private String source;
	private String filename;
	private boolean changed;

    public ControlVisualiser() {
        initComponents();
    }

    private void initComponents() {
        rootPane = new javax.swing.JPanel();
        splitPane = new javax.swing.JSplitPane();
        consolePane = new javax.swing.JPanel();
        consoleScrollPane = new javax.swing.JScrollPane();
        consoleText = new javax.swing.JTextPane();
        consoleLabel = new javax.swing.JLabel();
        tabbedPane = new javax.swing.JTabbedPane();
        sourcePane = new javax.swing.JPanel();
        sourceScrollPane = new javax.swing.JScrollPane();
        sourceText = new javax.swing.JTextPane();
        compileButton = new javax.swing.JButton();
        viewerPane = new javax.swing.JPanel();
        backButton = new javax.swing.JButton();
        stepButton = new javax.swing.JButton();
        viewerScrollPane = new javax.swing.JScrollPane();
        viewText = new javax.swing.JTextPane();
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
        setPreferredSize(new java.awt.Dimension(1080, 800));

        rootPane.setPreferredSize(new java.awt.Dimension(1080, 800));

        splitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        splitPane.setResizeWeight(0.5);
        splitPane.setPreferredSize(new java.awt.Dimension(1082, 800));

        consoleScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        consoleScrollPane.setAlignmentX(0.0F);
        consoleScrollPane.setAlignmentY(1.0F);
        consoleScrollPane.setPreferredSize(new java.awt.Dimension(1080, 80));

        consoleText.setEditable(false);
        consoleText.setPreferredSize(new java.awt.Dimension(1080, 80));
        consoleScrollPane.setViewportView(consoleText);

        consoleLabel.setText("Console Output:");

        javax.swing.GroupLayout consolePaneLayout = new javax.swing.GroupLayout(consolePane);
        consolePane.setLayout(consolePaneLayout);
        consolePaneLayout.setHorizontalGroup(
            consolePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(consolePaneLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(consoleLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(consoleScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 1078, Short.MAX_VALUE)
        );
        consolePaneLayout.setVerticalGroup(
            consolePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(consolePaneLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(consoleLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(consoleScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        splitPane.setRightComponent(consolePane);

        tabbedPane.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        tabbedPane.setAlignmentX(0.0F);
        tabbedPane.setAlignmentY(0.0F);
        tabbedPane.setPreferredSize(new java.awt.Dimension(1080, 700));

        sourcePane.setPreferredSize(new java.awt.Dimension(1080, 680));

        sourceScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        sourceScrollPane.setPreferredSize(new java.awt.Dimension(1080, 540));
        sourceScrollPane.setViewportView(sourceText);
        sourceText.setFont(new java.awt.Font("Consolas", Font.PLAIN, 12));

        sourceText.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) { changed = true; }
			public void removeUpdate(DocumentEvent e) { changed = true; }
			public void changedUpdate(DocumentEvent e) { changed = true; }
        });

        compileButton.setText("Compile & Run");
        compileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                compile();
            }
        });
        compileButton.setEnabled(false);

        javax.swing.GroupLayout sourcePaneLayout = new javax.swing.GroupLayout(sourcePane);
        sourcePane.setLayout(sourcePaneLayout);
        sourcePaneLayout.setHorizontalGroup(
            sourcePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(sourceScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 1073, Short.MAX_VALUE)
            .addGroup(sourcePaneLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(compileButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        sourcePaneLayout.setVerticalGroup(
            sourcePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sourcePaneLayout.createSequentialGroup()
                .addComponent(sourceScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 508, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(compileButton, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        tabbedPane.addTab("Source Editor", sourcePane);

        viewerPane.setPreferredSize(new java.awt.Dimension(1080, 680));

        backButton.setFont(new java.awt.Font("Dialog", 1, 18));
        backButton.setText("◄");
        backButton.addActionListener(new java.awt.event.ActionListener() {
        	public void actionPerformed(java.awt.event.ActionEvent evt) {
        		step(false);
        	}
        });

        stepButton.setFont(new java.awt.Font("Dialog", 1, 18));
        stepButton.setText("►");
        stepButton.addActionListener(new java.awt.event.ActionListener() {
        	public void actionPerformed(java.awt.event.ActionEvent evt) {
        		step(true);
        	}
        });

        stepButton.setEnabled(false);
		backButton.setEnabled(false);

        viewerScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        viewerScrollPane.setPreferredSize(new java.awt.Dimension(840, 630));

        viewText.setPreferredSize(new java.awt.Dimension(600, 580));
        viewText.setEditable(false);
        viewText.setFont(new java.awt.Font("Consolas", Font.PLAIN, 12));
        viewerScrollPane.setViewportView(viewText);

        javax.swing.GroupLayout viewerPaneLayout = new javax.swing.GroupLayout(viewerPane);
        viewerPane.setLayout(viewerPaneLayout);
        viewerPaneLayout.setHorizontalGroup(
            viewerPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(viewerScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 1073, Short.MAX_VALUE)
            .addGroup(viewerPaneLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(backButton, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(stepButton, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        viewerPaneLayout.setVerticalGroup(
            viewerPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(viewerPaneLayout.createSequentialGroup()
                .addComponent(viewerScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 508, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(viewerPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(stepButton)
                    .addComponent(backButton))
                .addContainerGap())
        );

        tabbedPane.addTab("Expansion Viewer", viewerPane);

        splitPane.setLeftComponent(tabbedPane);
        tabbedPane.getAccessibleContext().setAccessibleName("TabPanel");

        javax.swing.GroupLayout rootPaneLayout = new javax.swing.GroupLayout(rootPane);
        rootPane.setLayout(rootPaneLayout);
        rootPaneLayout.setHorizontalGroup(
            rootPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(splitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 1080, Short.MAX_VALUE)
        );
        rootPaneLayout.setVerticalGroup(
            rootPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rootPaneLayout.createSequentialGroup()
                .addComponent(splitPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

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
                .addComponent(rootPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        pack();
        splitPane.setDividerLocation(0.75);
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
        					source = "";
        					sourceText.setText("");
        					viewText.setText("");
        					consoleText.setText("");
        					changed = false;
        					stepButton.setEnabled(false);
        					backButton.setEnabled(false);
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
					source = "";
					sourceText.setText("");
					viewText.setText("");
					consoleText.setText("");
					changed = false;
					stepButton.setEnabled(false);
					backButton.setEnabled(false);
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
            		source = new String(Files.readAllBytes(Paths.get(filename)));
            		sourceText.setText(source);
					changed = false;
					stepButton.setEnabled(false);
					backButton.setEnabled(false);
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
        		source = sourceText.getText();
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
    	// Save the current file so we have something to run on
    	save(false);
    	// Find the location of the shell script - directory of the jar + /script/rustcompile.sh
    	String scriptDir = Paths.get("").toAbsolutePath().toString();
    	ProcessBuilder pb = new ProcessBuilder(scriptDir + "/script/controlcompile.sh");
    	pb.environment().put("FILENAME", filename);
    	pb.directory(new File(parent));
    	//pb.redirectErrorStream(true);
    	try {
    		Process p = pb.start();
    		consoleText.setText("Compile Output:");
    		Util.ProcessStream stream = new Util.ProcessStream(p.getInputStream(), viewText);
    		Util.ProcessStream err = new Util.ProcessStream(p.getErrorStream(), consoleText);
    		stream.start();
    		err.start();
    		int result = p.waitFor();
    		expandedText = viewText.getText();
    		backButton.setEnabled(true);
    		if (result == 0) {
    			pb.command(exeName);
    			pb.redirectErrorStream(true);
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

    private void step(boolean forward) {
    	if (forward) {
    		stepButton.setEnabled(false);
    		backButton.setEnabled(true);
    		viewText.setText(expandedText);
    	}
    	else {
    		stepButton.setEnabled(true);
    		backButton.setEnabled(false);
    		viewText.setText(source);
    	}
    }

    public static void main(String args[]) {
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ControlVisualiser().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify
    private javax.swing.JButton backButton;
    private javax.swing.JButton compileButton;
    private javax.swing.JLabel consoleLabel;
    private javax.swing.JPanel consolePane;
    private javax.swing.JScrollPane consoleScrollPane;
    private javax.swing.JTextPane consoleText;
    private javax.swing.JMenu menu;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem newItem;
    private javax.swing.JMenuItem openItem;
    private javax.swing.JPanel rootPane;
    private javax.swing.JMenuItem saveAsItem;
    private javax.swing.JMenuItem saveItem;
    private javax.swing.JPanel sourcePane;
    private javax.swing.JScrollPane sourceScrollPane;
    private javax.swing.JTextPane sourceText;
    private javax.swing.JSplitPane splitPane;
    private javax.swing.JButton stepButton;
    private javax.swing.JTabbedPane tabbedPane;
    private javax.swing.JTextPane viewText;
    private javax.swing.JPanel viewerPane;
    private javax.swing.JScrollPane viewerScrollPane;
    // End of variables declaration
}
