
/**
    @author
          ______         _                  _
         |  ____|       (_)           /\   | |
         | |__ __ _ _ __ _ ___       /  \  | | __ _ ___ _ __ ___   __ _ _ __ _   _
         |  __/ _` | '__| / __|     / /\ \ | |/ _` / __| '_ ` _ \ / _` | '__| | | |
         | | | (_| | |  | \__ \    / ____ \| | (_| \__ \ | | | | | (_| | |  | |_| |
         |_|  \__,_|_|  |_|___/   /_/    \_\_|\__,_|___/_| |_| |_|\__,_|_|   \__, |
                                                                              __/ |
                                                                             |___/
            Email: farisalasmary@gmail.com
            Date:  Jun 22, 2018
*/


import java.awt.event.*;
import java.io.*;

import javax.swing.*;
import java.util.Scanner;


public class LocationSettings extends JFrame implements ActionListener{

	private static final long serialVersionUID = -7948549056759090108L;

	private double Latitudes[];
	private double Longitudes[];
	private double GMTs[];
	private String citys[];
	private String settingsFileName = "settings.pryt", locationsFile = "locations";
	private JPanel locationPanel;
	private JLabel LatitudeLbl, LongitudeLbl, GMTLbl, cityLbl;
	private JButton saveAndUpdate;
	private JTextField Latitude, Longitude, GMT, city;
	private JComboBox<String> locationsComboBox;
	
	private MainGUI mainGUI;

	
	public LocationSettings(MainGUI mainGUI){
		super("Location Settings");
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setSize(300, 205);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setLayout(null);
		
		this.setMainGUI(mainGUI);
		
		constructPanels();
		constructLabelsAndTextFields();
		constructButtons();
		LoadInfo();
		LoadLocations();
		
		this.setVisible(true);
		
	}
	
	private void constructButtons(){
		this.saveAndUpdate = new JButton("Save & Update");
		this.saveAndUpdate.setActionCommand("save&update");
		this.saveAndUpdate.addActionListener(this);
		this.saveAndUpdate.setBounds(this.getSize().width / 2 - this.saveAndUpdate.getPreferredSize().width / 2,
									 this.locationPanel.getSize().height + 10,
									 this.saveAndUpdate.getPreferredSize().width,
									 this.saveAndUpdate.getPreferredSize().height);
		this.add(this.saveAndUpdate);
		
	}
	
	private void constructLabelsAndTextFields(){

		this.LatitudeLbl  = new JLabel("Latitude    :"); 
		this.LongitudeLbl = new JLabel("Longitude :"); 
		this.GMTLbl 	  = new JLabel("GMT            :");
		this.cityLbl	  = new JLabel("City            :");
		
		this.Latitude  = new JTextField(15);
		this.Longitude = new JTextField(15);
		this.GMT 	   = new JTextField(15);
		this.city	   = new JTextField(15);
		
		this.LatitudeLbl.setBounds(10, 25, this.LatitudeLbl.getPreferredSize().width, this.LatitudeLbl.getPreferredSize().height);
		this.Latitude.setBounds(this.LatitudeLbl.getX() + this.LatitudeLbl.getPreferredSize().width + 5, this.LatitudeLbl.getY(), this.Latitude.getPreferredSize().width, this.Latitude.getPreferredSize().height);
		this.LongitudeLbl.setBounds(this.LatitudeLbl.getX(), this.LatitudeLbl.getY() + 20, this.LongitudeLbl.getPreferredSize().width, this.LongitudeLbl.getPreferredSize().height);
		this.Longitude.setBounds(this.LatitudeLbl.getX() + this.LatitudeLbl.getPreferredSize().width + 5, this.LatitudeLbl.getY() + 20, this.Longitude.getPreferredSize().width, this.Longitude.getPreferredSize().height);
		this.GMTLbl.setBounds(this.LatitudeLbl.getX(), this.LongitudeLbl.getY() + 20, this.GMTLbl.getPreferredSize().width, this.GMTLbl.getPreferredSize().height);
		this.GMT.setBounds(this.LatitudeLbl.getX() + this.LatitudeLbl.getPreferredSize().width + 5, this.LongitudeLbl.getY() + 20, this.GMT.getPreferredSize().width, this.GMT.getPreferredSize().height);
		this.cityLbl.setBounds(this.LatitudeLbl.getX(), this.GMTLbl.getY() + 20, this.cityLbl.getPreferredSize().width, this.cityLbl.getPreferredSize().height);	
		this.city.setBounds(this.LatitudeLbl.getX() + this.LatitudeLbl.getPreferredSize().width + 5, this.GMT.getY() + 20, this.city.getPreferredSize().width, this.city.getPreferredSize().height);	
		
		this.locationPanel.add(this.LatitudeLbl );
		this.locationPanel.add(this.Latitude    );
		this.locationPanel.add(this.LongitudeLbl);
		this.locationPanel.add(this.Longitude   );
		this.locationPanel.add(this.GMTLbl		);
		this.locationPanel.add(this.GMT		    );
		this.locationPanel.add(this.cityLbl	    );
		this.locationPanel.add(this.city	    );
	
	}
	
	private void constructPanels(){
		this.locationPanel	  = new JPanel(null);

		this.locationPanel.setBorder(BorderFactory.createTitledBorder("Location"));
		this.locationPanel.setSize(this.getSize().width, 140);
		
		this.add(this.locationPanel);
			
	}
	
	private void SaveInfo(){
		try{
			if(!this.Latitude.getText().equals("") && !this.Longitude.getText().equals("") && !this.GMT.getText().equals("") && !this.city.getText().equals("")){
				PrintWriter pw = new PrintWriter(new FileOutputStream(this.settingsFileName));
				pw.print(this.Latitude.getText()  + " " );
				pw.print(this.Longitude.getText() + " " );
				pw.print(this.GMT.getText()       + " " );
				pw.print(this.city.getText()	  + "\n");
				pw.close();
				
				for(int i = 0; i < this.citys.length; i++)
					if(this.city.getText().equalsIgnoreCase(this.citys[i])){
						JOptionPane.showMessageDialog(null, "All changes have been saved!", "Info",
								  JOptionPane.INFORMATION_MESSAGE);
						return; // to exit the method
					}
				
				pw =  new PrintWriter(new FileOutputStream(this.locationsFile, true)); 
				pw.print(this.Latitude.getText()  + " " );
				pw.print(this.Longitude.getText() + " " );
				pw.print(this.GMT.getText()       + " " );
				pw.print(this.city.getText()	  + "\n");
				pw.close();
				
				JOptionPane.showMessageDialog(null, "All changes have been saved!", "Info",
						  JOptionPane.INFORMATION_MESSAGE);
				
			}else{
				JOptionPane.showMessageDialog(null, "You MUST fill all fields", "Error",
											  JOptionPane.ERROR_MESSAGE);
				return; // to exit the method
			}
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	
	private void LoadLocations(){
		int size = this.numberOfLines(this.locationsFile);
		
		this.Latitudes  = new double[size];
		this.Longitudes = new double[size];
		this.GMTs		= new double[size];
		this.citys		= new String[size];

		
		try{
			Scanner scan = new Scanner(new FileInputStream(this.locationsFile));
			for(int i = 0; i < size; i++){
				this.Latitudes[i]  = scan.nextDouble();
				this.Longitudes[i] = scan.nextDouble();
				this.GMTs[i]	   = scan.nextDouble();	
				this.citys[i]  = scan.nextLine().trim();
			}
			scan.close();
			
			this.locationsComboBox = new JComboBox<>(this.citys);
			this.locationsComboBox.setBounds(this.LatitudeLbl.getX() + 
								this.LatitudeLbl.getPreferredSize().width + 5,
								this.city.getY() + 20, this.city.getPreferredSize().width,
								this.locationsComboBox.getPreferredSize().height);	
			
			this.locationsComboBox.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					int selected = locationsComboBox.getSelectedIndex();
					Latitude.setText("" + Latitudes[selected]);
					Longitude.setText("" + Longitudes[selected]);
					GMT.setText("" + GMTs[selected]);
					city.setText("" + citys[selected]);
				}
			});
			
			this.locationPanel.add(this.locationsComboBox);
			
		}catch(IOException e){
			this.mainGUI.extractFromJar("locations");
			LoadLocations();
		}
		
	}
	
	private int numberOfLines(String fileName){
		int count = 0;
		
		try{
			Scanner scan = new Scanner(new FileInputStream(fileName));
			while(scan.hasNextLine()){
				scan.nextLine(); // dummy
				count++;
			}
			scan.close();
		}catch(IOException e){
			
		}
		
		return count;
	}
	
	private void LoadInfo(){
		try{
			Scanner scan = new Scanner(new FileInputStream(this.settingsFileName));
			this.Latitude.setText("" + scan.nextDouble());
			this.Longitude.setText("" + scan.nextDouble());
			this.GMT.setText("" + scan.nextDouble());
			this.city.setText("" + scan.nextLine().trim());
			scan.close();
		}catch(Exception ex){
			// It will never happen because the settings file is extracted before by the MainGUI class!!
		}	
	}
	
	private void Update(){
		try{
			this.mainGUI.setPrayerTimes(
					new PrayerTimes( Double.parseDouble(this.Latitude.getText()),
									 Double.parseDouble(this.Longitude.getText()),
									 Double.parseDouble(this.GMT.getText()), 
									 this.city.getText()));	
			
			this.mainGUI.getCity().setText(this.city.getText());
			this.mainGUI.getGregorianDate().setText(this.mainGUI.getPrayerTimes().getGregorianDate());
			this.mainGUI.getHijriDate().setText(this.mainGUI.getPrayerTimes().getHijriDate());
			
			String prayers[] = this.mainGUI.getPrayerTimes().getPrayerTimes();
			
			
			for(int i = 0; i < prayers.length; i++)
				this.mainGUI.getPrayers()[i].setText(prayers[i]);

			SaveInfo();
		}catch(Exception ex){
			if(ex.getMessage().equalsIgnoreCase(""))
				ex.printStackTrace();
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equalsIgnoreCase("save&update")){
			Update();
		}
	}

	public MainGUI getMainGUI() {
		return this.mainGUI;
	}

	public void setMainGUI(MainGUI mainGUI) {
		if(mainGUI == null)
			throw new NullPointerException();
		this.mainGUI = mainGUI;
	}
}
