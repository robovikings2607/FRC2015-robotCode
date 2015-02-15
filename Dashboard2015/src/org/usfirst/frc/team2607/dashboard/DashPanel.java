package org.usfirst.frc.team2607.dashboard;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollBar;
import javax.swing.UIManager;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Color;
import java.awt.Font;

import javax.swing.SwingConstants;

import java.awt.FlowLayout;

import javax.swing.BoxLayout;

import net.miginfocom.swing.MigLayout;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class DashPanel extends JFrame {

	private JPanel contentPane;
	static NetworkTable t;

	static double elevatorHeight = 0.0;
	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DashPanel frame = new DashPanel();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		NetworkTable.setClientMode();
		NetworkTable.setIPAddress("roborio-2607.local");
		t = NetworkTable.getTable("SmartDashboard");
		
		new Thread(new Runnable(){

			@Override
			public void run() {
				elevatorHeight = t.getNumber("elevatorHeight", 0.0);
				
			}
			
		}).start();
	}

	/**
	 * Create the frame.
	 */
	public DashPanel() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 955, 634);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel pnlToteStack = new JPanel();
		pnlToteStack.setBounds(641, 12, 119, 561);
		contentPane.add(pnlToteStack);
		
		JLabel lblTote6 = new JLabel("6");
		lblTote6.setHorizontalAlignment(SwingConstants.CENTER);
		lblTote6.setFont(new Font("Segoe UI", Font.BOLD, 50));
		lblTote6.setForeground(Color.GREEN);
		lblTote6.setBackground(Color.GRAY);
		lblTote6.setOpaque(true);
		
		JLabel lblTote5 = new JLabel("5");
		lblTote5.setOpaque(true);
		lblTote5.setHorizontalAlignment(SwingConstants.CENTER);
		lblTote5.setForeground(Color.GREEN);
		lblTote5.setFont(new Font("Segoe UI", Font.BOLD, 50));
		lblTote5.setBackground(Color.GRAY);
		
		JLabel lblTote4 = new JLabel("4");
		lblTote4.setOpaque(true);
		lblTote4.setHorizontalAlignment(SwingConstants.CENTER);
		lblTote4.setForeground(Color.GREEN);
		lblTote4.setFont(new Font("Segoe UI", Font.BOLD, 50));
		lblTote4.setBackground(Color.GRAY);
		
		JLabel lblTote3 = new JLabel("3");
		lblTote3.setOpaque(true);
		lblTote3.setHorizontalAlignment(SwingConstants.CENTER);
		lblTote3.setForeground(Color.GREEN);
		lblTote3.setFont(new Font("Segoe UI", Font.BOLD, 50));
		lblTote3.setBackground(Color.GRAY);
		
		JLabel lblTote2 = new JLabel("2");
		lblTote2.setOpaque(true);
		lblTote2.setHorizontalAlignment(SwingConstants.CENTER);
		lblTote2.setForeground(Color.GREEN);
		lblTote2.setFont(new Font("Segoe UI", Font.BOLD, 50));
		lblTote2.setBackground(Color.GRAY);
		
		JLabel lblTote1 = new JLabel("1");
		lblTote1.setOpaque(true);
		lblTote1.setHorizontalAlignment(SwingConstants.CENTER);
		lblTote1.setForeground(Color.GREEN);
		lblTote1.setFont(new Font("Segoe UI", Font.BOLD, 50));
		lblTote1.setBackground(Color.GRAY);
		GroupLayout gl_pnlToteStack = new GroupLayout(pnlToteStack);
		gl_pnlToteStack.setHorizontalGroup(
			gl_pnlToteStack.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlToteStack.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_pnlToteStack.createParallelGroup(Alignment.LEADING)
						.addComponent(lblTote6, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE)
						.addComponent(lblTote5, GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE)
						.addComponent(lblTote4, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE)
						.addComponent(lblTote3, GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE)
						.addComponent(lblTote2, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE)
						.addComponent(lblTote1, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_pnlToteStack.setVerticalGroup(
			gl_pnlToteStack.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlToteStack.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblTote6, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblTote5, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblTote4, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblTote3, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblTote2, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblTote1, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		pnlToteStack.setLayout(gl_pnlToteStack);
		
		JPanel pnlElevator = new JPanel();
		pnlElevator.setBounds(772, 23, 111, 550);
		contentPane.add(pnlElevator);
		pnlElevator.setLayout(null);
		
		JPanel pnlArms = new JPanel();
		pnlArms.setBounds(12, 457, 87, 81);
		pnlElevator.add(pnlArms);
		pnlArms.setLayout(null);
		
		JLabel lblArms = new JLabel("   ");
		lblArms.setBackground(Color.BLACK);
		lblArms.setBounds(0, 0, 87, 5);
		lblArms.setOpaque(true);
		pnlArms.add(lblArms);
		
		JLabel lblPickedUpTote = new JLabel("    ");
		lblPickedUpTote.setBackground(Color.YELLOW);
		lblPickedUpTote.setBounds(0, 0, 87, 81);
		lblPickedUpTote.setOpaque(true);
		pnlArms.add(lblPickedUpTote);
	}
}
