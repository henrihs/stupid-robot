package edu.wsu.modelling;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

public class Frame extends JFrame {

	
	private JTable board;

	public Frame(TableModel table) {
		board = new JTable(table) {
		    @Override
		    public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
		        Component comp = super.prepareRenderer(renderer, row, col);
		        String value = (String) getModel().getValueAt(row, col);
		        value = value.substring(0, 1);
		        Color color;
		        if (value.equals("#")) {
		        	color = Color.black;
	            } else if (value.equals("B")) {
	            	color = Color.green;
	            } else if (value.equals("+")) {
	            	color = Color.yellow;
	            } else if (value.equals(" ")) {
	            	color = Color.white;
	            } else if (value.equals("X")) {
	            	color = Color.blue;
	            } else {
	            	color = Color.lightGray;
	            }
		        getModel().setValueAt("", row, col);
		        comp.setSize(2, 2);
		        comp.setBackground(color);
		        comp.setForeground(color);
		        return comp;
		    }			
		};
		initGui();
	}
	
	private void initGui(){
		board.setRowHeight(8);
		for (int i = 0; i < board.getColumnCount(); i++) {
			board.getColumnModel().getColumn(i).setPreferredWidth(8);
		}
		board.setShowGrid(false);
		board.setIntercellSpacing(new Dimension(0, 0));
		
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(board, BorderLayout.CENTER);
		pack();
		
	    int centerX = (int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() - getSize().width) / 2;
	    int centerY = (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight() - getSize().height) / 2;
	    setLocation(centerX, centerY);
	    setVisible(true);		
		
		
		
//		board.setFont(board.getFont().deriveFont(8.0f));
//	    board.setRowHeight(8);
//	    board.setCellSelectionEnabled(true);
//	    for (int i = 0; i < board.getColumnCount(); i++)
//	      board.getColumnModel().getColumn(i).setPreferredWidth(8);
//	    board.setGridColor(Color.WHITE);
//	    board.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//	    DefaultTableCellRenderer dtcl = new DefaultTableCellRenderer();
//	    dtcl.setHorizontalAlignment(SwingConstants.CENTER);
//	    board.setDefaultRenderer(Object.class, dtcl);
//	    
//	    Container contentPane = getContentPane();
//	    contentPane.setLayout(new BorderLayout());
//	    contentPane.add(board, BorderLayout.CENTER);
//	    pack();
	    

	}
}
