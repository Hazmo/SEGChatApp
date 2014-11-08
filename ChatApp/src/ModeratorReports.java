package src;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ModeratorReports extends JFrame {

	private JList reportsList = new JList();
	private JTextArea reportDesc = new JTextArea();
	private JButton resolveIssue = new JButton("Resolve Issue");
	private JButton backButton = new JButton("Back");
	private ArrayList<ReportClass> reports;


	Socket socket;
    ObjectOutputStream out;
    ObjectInputStream in;

	ModeratorReports(Socket socket, ObjectOutputStream out, ObjectInputStream in) {
        this.socket = socket;
        this.out = out;
        this.in = in;
		initGUI();
	}
	
	void initGUI() {
		JPanel centrePanel = new JPanel(new BorderLayout());
		reportsList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				if(!reportsList.isSelectionEmpty()) {					
					ReportClass rep = reports.get(reportsList.getSelectedIndex());
					reportDesc.setText(rep.getReportMessage());
				}
			}
			
		});
		JScrollPane rplPane = new JScrollPane(reportsList);
		JScrollPane textPane = new JScrollPane(reportDesc);
		reportDesc.setEditable(false);
		centrePanel.setBorder(BorderFactory.createEmptyBorder(10,10,0,10));
		centrePanel.add(rplPane, BorderLayout.CENTER);
		centrePanel.add(textPane, BorderLayout.SOUTH);
		add(centrePanel, BorderLayout.CENTER);

        try{    //out.writeObject(new String("READ"));
            out.writeObject(new MessageClass("get_reports", ""));
            reports = (ArrayList<ReportClass>) in.readObject();
        } catch (Exception e) {
            if (e.getClass().equals(IOException.class)) {
                System.out.println("Accept failed: 4457");
                System.exit(-1);
            }
            else if (e.getClass().equals(ClassNotFoundException.class))
                e.printStackTrace();
        }
		
		setReports();
		
		JPanel bottomPane = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		resolveIssue.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(reportsList.isSelectionEmpty()) {
					JOptionPane.showMessageDialog(null, "Please select a report first.");
				} else {
					int repElem = reportsList.getSelectedIndex();
					ReportClass rep = reports.get(repElem);
					
					if(rep.isOpen()) {
						rep.setResolved();
						setResolved();
						setReports();						
					} else {
						JOptionPane.showMessageDialog(null, "Has already been resolved.");
					}
				}
			}
		});
		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		bottomPane.add(resolveIssue);
		bottomPane.add(backButton);
		
		add(bottomPane, BorderLayout.SOUTH);
		setTitle("Moderator Reports");
		setResizable(false);
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(400, 200);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	void setReports() {
		DefaultListModel<String> reportList = new DefaultListModel<String>();
		for(ReportClass rep : reports) {
			reportList.addElement(rep.getListDesc());
		}
		
		reportsList.setModel(reportList);
	}

    void setResolved() {
        try {
            //out.writeObject(new String("RESOLVE"));
            out.writeObject(new MessageClass("resolve_report", ""));
            out.writeObject(reports);
        } catch (Exception e) {
            if (e.getClass().equals(IOException.class)) {
                System.out.println("Accept failed: 4457");
                System.exit(-1);
            }
            else if (e.getClass().equals(ClassNotFoundException.class))
                e.printStackTrace();
        }
    }

}
