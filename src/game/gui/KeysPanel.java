package game.gui;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class KeysPanel extends JDialog implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1820059510973932243L;
	Frame parent;
	JButton exit, upKeyButton, downKeyButton, leftKeyButton, rightKeyButton,
					eagleKeyButton, surrenderKeyButton;
	
	public KeysPanel(Frame parent, String title)  {
		super(parent, title, true);
		this.parent = parent;
		if(parent != null) {
			Dimension parentSize = parent.getSize();     // Parent size
			Point p = parent.getLocation();              // Parent position
			setLocation(p.x+parentSize.width/4,p.y+parentSize.height/4); 
		}

		setPreferredSize(new Dimension(270, 300));

		// Create the button pane
		JPanel buttonPane = new JPanel();
		buttonPane.setBounds(0, 239, 400, 39);
		exit = new JButton("Kthxbai");
		buttonPane.add(exit);
		exit.addActionListener(this);
		getContentPane().setLayout(null);
		getContentPane().add(buttonPane);

		JLabel lblMoveUp = new JLabel("Move up");
		lblMoveUp.setBounds(17, 53, 61, 16);
		getContentPane().add(lblMoveUp);

		JLabel lblMoveLeft = new JLabel("Move left");
		lblMoveLeft.setBounds(17, 81, 61, 16);
		getContentPane().add(lblMoveLeft);

		JLabel lblMoveRight_1 = new JLabel("Move right");
		lblMoveRight_1.setBounds(17, 137, 77, 16);
		getContentPane().add(lblMoveRight_1);

		JLabel lblModeDown = new JLabel("Mode down");
		lblModeDown.setBounds(17, 109, 77, 16);
		getContentPane().add(lblModeDown);

		JLabel lblSendEagle = new JLabel("Send eagle");
		lblSendEagle.setBounds(17, 165, 77, 16);
		getContentPane().add(lblSendEagle);

		JLabel lblGiveUp = new JLabel("Give up");
		lblGiveUp.setBounds(17, 193, 61, 16);
		getContentPane().add(lblGiveUp);
		
		upKeyButton = new JButton(getKeyString(GameKeys.upKey));
		upKeyButton.setBounds(128, 48, 117, 29);
		getContentPane().add(upKeyButton);		
		upKeyButton.addActionListener(this);
		
		leftKeyButton = new JButton(getKeyString(GameKeys.leftKey));
		leftKeyButton.setBounds(128, 76, 117, 29);
		getContentPane().add(leftKeyButton);		
		leftKeyButton.addActionListener(this);
		
		downKeyButton = new JButton(getKeyString(GameKeys.downKey));
		downKeyButton.setBounds(128, 104, 117, 29);
		getContentPane().add(downKeyButton);		
		downKeyButton.addActionListener(this);
		
		rightKeyButton = new JButton(getKeyString(GameKeys.rightKey));
		rightKeyButton.setBounds(128, 132, 117, 29);
		getContentPane().add(rightKeyButton);		
		rightKeyButton.addActionListener(this);

		eagleKeyButton = new JButton(getKeyString(GameKeys.eagleKey));
		eagleKeyButton.setBounds(128, 160, 117, 29);
		getContentPane().add(eagleKeyButton);		
		eagleKeyButton.addActionListener(this);
		
		surrenderKeyButton = new JButton(getKeyString(GameKeys.surrenderKey));
		surrenderKeyButton.setBounds(128, 188, 117, 29);
		getContentPane().add(surrenderKeyButton);		
		surrenderKeyButton.addActionListener(this);
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);		
		pack();  
		
		setVisible(true);
	}
	
	public class ChooseKey extends JDialog implements KeyListener {

		int keypressed = -1;
		String key;
		
		public ChooseKey(Frame parent, String key) {
			super(parent, "Key Configuration", true);
			this.key = key;
			setSize(300, 150);
			JLabel message = new JLabel("Press the new key");
			message.setBounds(17, 53, 61, 16);
			getContentPane().add(message);
			
			setResizable(false);
			setLocationRelativeTo(null);
			addKeyListener(this);
			setVisible(true);
			setFocusable(true);
		}
	
		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() != KeyEvent.VK_ESCAPE){
				GameKeys.changeKey(key, e.getKeyCode());
				this.dispose();
			}
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
		}
		
		
	}

	public void actionPerformed(ActionEvent e)  {
		if (e.getSource() == exit){
			setVisible(false);                         
			dispose();                 
		}
		else if (e.getSource() == upKeyButton){
			new ChooseKey(parent, "upKey");
			upKeyButton.setText(getKeyString(GameKeys.upKey));
		}
		else if (e.getSource() == leftKeyButton){
			new ChooseKey(parent, "leftKey");
			leftKeyButton.setText(getKeyString(GameKeys.leftKey));
		}
		else if (e.getSource() == rightKeyButton){
			new ChooseKey(parent, "rightKey");
			rightKeyButton.setText(getKeyString(GameKeys.rightKey));
		}
		else if (e.getSource() == downKeyButton){
			new ChooseKey(parent, "downKey");
			downKeyButton.setText(getKeyString(GameKeys.downKey));
		}
		else if (e.getSource() == eagleKeyButton){
			new ChooseKey(parent, "eagleKey");
			eagleKeyButton.setText(getKeyString(GameKeys.eagleKey));
		}
		else if (e.getSource() == surrenderKeyButton){
			new ChooseKey(parent, "surrenderKey");
			surrenderKeyButton.setText(getKeyString(GameKeys.surrenderKey));
		}
	}

	public static String getKeyString(int keyCode) {
		switch (keyCode) {

		/* Keyboard and Mouse Masks */
		case KeyEvent.VK_ALT:
			return "ALT";
		case KeyEvent.VK_SHIFT:
			return "SHIFT";
		case KeyEvent.VK_CONTROL:
			return "CONTROL";

			/* Non-Numeric Keypad Keys */
		case KeyEvent.VK_UP:
			return "ARROW_UP";
		case KeyEvent.VK_DOWN:
			return "ARROW_DOWN";
		case KeyEvent.VK_LEFT:
			return "ARROW_LEFT";
		case KeyEvent.VK_RIGHT:
			return "ARROW_RIGHT";
		case KeyEvent.VK_PAGE_UP:
			return "PAGE_UP";
		case KeyEvent.VK_PAGE_DOWN:
			return "PAGE_DOWN";
		case KeyEvent.VK_HOME:
			return "HOME";
		case KeyEvent.VK_END:
			return "END";
		case KeyEvent.VK_INSERT:
			return "INSERT";

			/* Virtual and Ascii Keys */
		case KeyEvent.VK_DELETE:
			return "DEL";
		case KeyEvent.VK_ESCAPE:
			return "ESC";
		case KeyEvent.VK_TAB:
			return "TAB";

			/* Functions Keys */
		case KeyEvent.VK_F1:
			return "F1";
		case KeyEvent.VK_F2:
			return "F2";
		case KeyEvent.VK_F3:
			return "F3";
		case KeyEvent.VK_F4:
			return "F4";
		case KeyEvent.VK_F5:
			return "F5";
		case KeyEvent.VK_F6:
			return "F6";
		case KeyEvent.VK_F7:
			return "F7";
		case KeyEvent.VK_F8:
			return "F8";
		case KeyEvent.VK_F9:
			return "F9";
		case KeyEvent.VK_F10:
			return "F10";
		case KeyEvent.VK_F11:
			return "F11";
		case KeyEvent.VK_F12:
			return "F12";
		case KeyEvent.VK_F13:
			return "F13";
		case KeyEvent.VK_F14:
			return "F14";
		case KeyEvent.VK_F15:
			return "F15";

			/* Numeric Keypad Keys */
		case KeyEvent.VK_ADD:
			return "KEYPAD_ADD";
		case KeyEvent.VK_SUBTRACT:
			return "KEYPAD_SUBTRACT";
		case KeyEvent.VK_MULTIPLY:
			return "KEYPAD_MULTIPLY";
		case KeyEvent.VK_DIVIDE:
			return "KEYPAD_DIVIDE";
		case KeyEvent.VK_DECIMAL:
			return "KEYPAD_DECIMAL";
		case KeyEvent.VK_0:
			return "KEYPAD_0";
		case KeyEvent.VK_1:
			return "KEYPAD_1";
		case KeyEvent.VK_2:
			return "KEYPAD_2";
		case KeyEvent.VK_3:
			return "KEYPAD_3";
		case KeyEvent.VK_4:
			return "KEYPAD_4";
		case KeyEvent.VK_5:
			return "KEYPAD_5";
		case KeyEvent.VK_6:
			return "KEYPAD_6";
		case KeyEvent.VK_7:
			return "KEYPAD_7";
		case KeyEvent.VK_8:
			return "KEYPAD_8";
		case KeyEvent.VK_9:
			return "KEYPAD_9";
		case KeyEvent.VK_EQUALS:
			return "=";

			/* Other keys */
		case KeyEvent.VK_CAPS_LOCK:
			return "CAPS_LOCK";
		case KeyEvent.VK_NUM_LOCK:
			return "NUM_LOCK";
		case KeyEvent.VK_SCROLL_LOCK:
			return "SCROLL_LOCK";
		case KeyEvent.VK_PAUSE:
			return "PAUSE";
		case KeyEvent.VK_PRINTSCREEN:
			return "PRINT_SCREEN";
		case KeyEvent.VK_HELP:
			return "HELP";
		}
		return Character.toString((char) keyCode);
	}
}
