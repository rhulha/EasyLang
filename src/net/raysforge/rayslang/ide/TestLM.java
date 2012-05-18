package net.raysforge.rayslang.ide;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

import javax.swing.JFrame;

public class TestLM {
	
	
	public TestLM() {
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(800, 600);
		f.setLocationRelativeTo(null);
		
		LayoutManager lm = new LayoutManager() {
			
			@Override
			public void removeLayoutComponent(Component arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public Dimension preferredLayoutSize(Container arg0) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Dimension minimumLayoutSize(Container arg0) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public void layoutContainer(Container arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void addLayoutComponent(String arg0, Component arg1) {
				// TODO Auto-generated method stub
				
			}
		};
		
		f.getContentPane().setLayout(lm);
		
		f.setVisible(true);
			
	}
	
	public static void main(String[] args) {
		new TestLM();
	}

}
