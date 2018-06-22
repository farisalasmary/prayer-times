
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


import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.jar.*;
import java.io.*;

import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.Timer;

public class MainGUI extends JFrame implements ActionListener{

	private static final long serialVersionUID = 6411499808530678723L;
	private static final double VERSION = 1.1;
	private static final String PROGRAM_NAME = "Prayer Times";

	
	private JMenu  fileMenu;
	private String settingsFileName = "settings.pryt", programName = "Prayer Times";
	private String prayerNames[] = {"Fajr", "Shrouq", "Dhuhr", "Asr", "Maghrib", "Isha"};
	private JPanel prayerTimesPanel, dateAndLocationPanel;
	private JLabel prayerLbls[], prayers[], hijriDate, gregorianDate, city, clock;
	private JMenuBar menuBar;
	private JMenuItem exitItm, hideItm, aboutItm;
	private PrayerTimes p;
	private JMenuItem locationSettings;
	private boolean isFirstTime = true;

	public MainGUI(){
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		this.setSize(220, 360);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setLayout(null);
		this.setTitle(this.programName);
		
		constructPanels();
		constructMenus();
		constructLabelsAndTextFields();
		constructSystemTray();
		LoadInfo();
		updatePrayerTimesLabels();
		constructTimer();
		
		this.setVisible(true);
		
	}
	
	private void constructTimer(){
		Timer timer = new Timer(1000, new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				String timeNow = new SimpleDateFormat("hh:mm a").format(new Date());
				clock.setText(timeNow);
				
				p = new PrayerTimes(p.getLatitude(), p.getLongitude(), p.getGMT(), p.getName());
				hijriDate.setText(p.getHijriDate());
				gregorianDate.setText(p.getGregorianDate());

				if(timeNow.equals(p.getFajr())){
					if(isFirstTime){
						Athan();
						JOptionPane.showMessageDialog(null, "Fajr prayer time is now!", "Fajr Prayer", JOptionPane.INFORMATION_MESSAGE);
						isFirstTime = false;
					}
					return; // It MUST return to avoid executing the code more than once
					
				}else if(timeNow.equals(p.getShrouq())){
					if(isFirstTime){
						JOptionPane.showMessageDialog(null, "Shrouq time is now!", "Shrouq Prayer", JOptionPane.INFORMATION_MESSAGE);
						isFirstTime = false;
					}
					return; // It MUST return to avoid executing the code more than once
					
				}else if(timeNow.equals(p.getDhuhr())){
					if(isFirstTime){
						Athan();
						JOptionPane.showMessageDialog(null, "Dhuhr prayer time is now!", "Dhuhr Prayer", JOptionPane.INFORMATION_MESSAGE);
						isFirstTime = false;
					}
					return; // It MUST return to avoid executing the code more than once
					
				}else if(timeNow.equals(p.getAsr())){
					if(isFirstTime){
						Athan();
						JOptionPane.showMessageDialog(null, "Asr prayer time is now!", "Asr Prayer", JOptionPane.INFORMATION_MESSAGE);
						isFirstTime = false;
					}
					return; // It MUST return to avoid executing the code more than once
					
				}else if(timeNow.equals(p.getMaghrib())){
					if(isFirstTime){
						Athan();
						JOptionPane.showMessageDialog(null, "Maghrib prayer time is now!", "Maghrib Prayer", JOptionPane.INFORMATION_MESSAGE);
						isFirstTime = false;
					}
					return; // It MUST return to avoid executing the code more than once
					
				}else if(timeNow.equals(p.getIsha())){
					if(isFirstTime){
						Athan();
						JOptionPane.showMessageDialog(null, "Isha prayer time is now!", "Isha Prayer", JOptionPane.INFORMATION_MESSAGE);
						isFirstTime = false;
					}
					return; // It MUST return to avoid executing the code more than once
					
				}else if(!isFirstTime){
					isFirstTime = true;
				}
				
//				// to update the date and therefore the prayer times
//				if(timeNow.equals("12:00 AM")){
//					p = new PrayerTimes(p.getLatitude(), p.getLongitude(), p.getGMT(), p.getName());
//					hijriDate.setText(p.getHijriDate());
//					gregorianDate.setText(p.getGregorianDate());
//				}
			
			}
		});
		
		timer.setInitialDelay(1); // to setup the date and other stuffs
		timer.start();
	}
	
	protected void extractFromJar(String name){
		
		try {
			JarFile jarFile = new JarFile(this.getProgramName());
			JarEntry file = jarFile.getJarEntry(name);
			InputStream inputStream = jarFile.getInputStream(file); // get the input stream
			FileOutputStream output = new FileOutputStream(System.getProperty("user.dir") + File.separator + file.getName());
			
			while (inputStream.available() > 0) {  // write contents of 'inputStream' to 'output'
				output.write(inputStream.read());
			}
				
			output.close();
			inputStream.close();
			jarFile.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Couldn't extract files!!", "Error", JOptionPane.ERROR_MESSAGE);
		}
		
//		try {
//			JarFile jarFile = new JarFile(this.getProgramName());
//			Enumeration<JarEntry> enumEntries = jarFile.entries();
//			while(enumEntries.hasMoreElements()) {
//				JarEntry file = enumEntries.nextElement();
//				File f = new File(System.getProperty("user.dir") + File.separator + file.getName());
//				if (file.isDirectory()) { // if its a directory, create it
//					f.mkdir();
//					continue;
//				}
//				InputStream is = jarFile.getInputStream(file); // get the input stream
//				FileOutputStream fos = new FileOutputStream(f);
//				while (is.available() > 0) {  // write contents of 'is' to 'fos'
//					fos.write(is.read());
//				}
//				
//				fos.close();
//				is.close();
//				jarFile.close();
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	
	}
	
	private String getProgramName(){
		return new File(this.getClass().getProtectionDomain()
				  .getCodeSource()
				  .getLocation()
				  .getPath())
				  .getName();
	}
	
	private void Athan(){
	    // specify the sound to play
	    // (assuming the sound can be played by the audio system)
		try {
			File soundFile = new File("./athan.wav");//f.getSelectedFile();

			AudioInputStream sound = AudioSystem.getAudioInputStream(soundFile);
			
			// load the sound into memory (a Clip)
			DataLine.Info info = new DataLine.Info(Clip.class, sound.getFormat());
			Clip clip = (Clip) AudioSystem.getLine(info);
			
			clip.open(sound);
			clip.start();
			
		} catch (UnsupportedAudioFileException | IOException e) {
		//	e.printStackTrace();
			this.extractFromJar("athan.wav");
			this.Athan();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}
	
	private void constructSystemTray(){
	    if(!SystemTray.isSupported()){
	   //     System.out.println("System tray is not supported !!! ");
			this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	        return ;
	    }

	    SystemTray systemTray = SystemTray.getSystemTray();

	    Image image = new ImageIcon(this.getClass().getResource("/icon.png")).getImage();

	    PopupMenu trayPopupMenu = new PopupMenu();

	    MenuItem show = new MenuItem("show");
	    show.addActionListener(this);     
	    show.setActionCommand("show");
	    trayPopupMenu.add(show);

	    MenuItem hide = new MenuItem("hide");
	    hide.addActionListener(this);     
	    hide.setActionCommand("hide");
	    trayPopupMenu.add(hide);

	    MenuItem exit = new MenuItem("Exit");
	    exit.addActionListener(this);
	    exit.setActionCommand("exit");  
	    trayPopupMenu.addSeparator();
	    trayPopupMenu.add(exit);

	    TrayIcon trayIcon = new TrayIcon(image, this.programName, trayPopupMenu);
	    trayIcon.setImageAutoSize(true);

	    try{
	        systemTray.add(trayIcon);
	    }catch(AWTException awtException){
	        awtException.printStackTrace();
	    }

	}
	
	private void constructLabelsAndTextFields(){
		
		this.city		   = new JLabel("Ballasmer");
		this.clock  	   = new JLabel("00:00 AM");
		this.hijriDate     = new JLabel("01/01/1436 H");
		this.gregorianDate = new JLabel("01/01/2015");
		
		int width = 150;
		this.city.setBounds(this.getSize().width / 2 - 100 / 2, 20, width, this.city.getPreferredSize().height);
		this.hijriDate.setBounds(this.city.getX(), this.city.getY() + 20, width, this.hijriDate.getPreferredSize().height);
		this.gregorianDate.setBounds(this.city.getX(), this.hijriDate.getY() + 15, width, this.gregorianDate.getPreferredSize().height);
		
		
		this.dateAndLocationPanel.add(this.city);
		this.dateAndLocationPanel.add(this.hijriDate);
		this.dateAndLocationPanel.add(this.gregorianDate);
		
		
		this.prayers    = new JLabel[6];
		this.prayerLbls = new JLabel[6];
		
		for(int i = 0; i < prayers.length; i++){
			this.prayers[i]	   = new JLabel("00:00 AM"    );
			this.prayerLbls[i] = new JLabel(prayerNames[i]);

			if(i != 0){
				this.prayerLbls[i].setBounds(30, this.prayerLbls[i-1].getY() + 30, this.prayerLbls[i].getPreferredSize().width, this.prayerLbls[i].getPreferredSize().height);
				this.prayers[i].setBounds(this.prayerLbls[i].getX() + 100, this.prayers[i-1].getY() + 30, this.prayers[i].getPreferredSize().width, this.prayers[i].getPreferredSize().height);
			}else{
				this.clock.setBounds(this.prayerTimesPanel.getSize().width / 2 - this.clock.getPreferredSize().width / 2, 30, this.clock.getPreferredSize().width, this.clock.getPreferredSize().height);
				this.prayerTimesPanel.add(this.clock);
				this.prayerLbls[i].setBounds(30, this.clock.getY() + 30, this.prayerLbls[i].getPreferredSize().width, this.prayerLbls[i].getPreferredSize().height);
				this.prayers[i].setBounds(this.prayerLbls[i].getX() + 100, this.prayerLbls[i].getY(), this.prayers[i].getPreferredSize().width, this.prayers[i].getPreferredSize().height);

			}
			
			this.prayerTimesPanel.add(this.prayerLbls[i]);
			this.prayerTimesPanel.add(this.prayers[i]);
		}
	}
	
	
	private void constructPanels(){
		this.prayerTimesPanel     = new JPanel(null);
		this.dateAndLocationPanel = new JPanel(null);
		
		this.dateAndLocationPanel.setBorder(BorderFactory.createTitledBorder("Date & Location"));
		this.dateAndLocationPanel.setBounds(0, 0, this.getSize().width, 80);
		this.prayerTimesPanel.setBorder(BorderFactory.createTitledBorder("Prayer Times"));
		this.prayerTimesPanel.setBounds(0, this.dateAndLocationPanel.getSize().height, this.getSize().width, 240);
		
		this.add(this.dateAndLocationPanel);
		this.add(this.prayerTimesPanel);
			
	}

	private void constructMenus(){
		this.menuBar = new JMenuBar();
		
		this.fileMenu = new JMenu("File");
		
		this.aboutItm		  = new JMenuItem("About...");
		this.locationSettings = new JMenuItem("Location Settings"); 
		this.hideItm		  = new JMenuItem("Hide"); 
		this.exitItm 		  = new JMenuItem("Exit");
		
		this.aboutItm.addActionListener(this);
		this.aboutItm.setActionCommand("about");
		this.locationSettings .addActionListener(this);
		this.locationSettings .setActionCommand("showLocationSettings");
		this.hideItm.addActionListener(this);
		this.hideItm.setActionCommand("hide");
		this.exitItm.addActionListener(this);
		this.exitItm.setActionCommand("exit");
		
		this.fileMenu.add(this.locationSettings);
		if(SystemTray.isSupported()) this.fileMenu.add(this.hideItm);
		this.fileMenu.addSeparator();
		this.fileMenu.add(this.exitItm);
		
		this.menuBar.add(this.fileMenu);
		this.menuBar.add(this.aboutItm);
		
		this.setJMenuBar(this.menuBar);
		
	}

	private void LoadInfo(){
		try{
			Scanner scan = new Scanner(new FileInputStream(this.settingsFileName));
			this.setPrayerTimes(new PrayerTimes(scan.nextDouble(), scan.nextDouble(), scan.nextDouble(), scan.nextLine()));
			scan.close();
		}catch(Exception ex){
			this.extractFromJar(this.settingsFileName);
			this.LoadInfo();
		}	
	}
	
	private void updatePrayerTimesLabels(){
		try{
			
			this.gregorianDate.setText(this.p.getGregorianDate());
			this.hijriDate.setText(this.p.getHijriDate());
			this.city.setText(this.p.getName());
			
			String prayers[] = this.p.getPrayerTimes();
			
			for(int i = 0; i < prayers.length; i++)
				this.prayers[i].setText(prayers[i]);

		}catch(Exception ex){
			if(ex.getMessage() != null)
				JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			return;
			
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equalsIgnoreCase("showLocationSettings")){
			new LocationSettings(this);
			
		}else if(e.getActionCommand().equalsIgnoreCase("hide")){
			this.setVisible(false);
			
		}else if(e.getActionCommand().equalsIgnoreCase("exit")){
        	int choice = JOptionPane.showConfirmDialog(null, "Are you sure ??", "Info",
   				 JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
		   	if(choice == JOptionPane.YES_OPTION)
		       	System.exit(0);  
		   	
		}else if(e.getActionCommand().equalsIgnoreCase("show")){
			this.setVisible(true);
			
		}else if(e.getActionCommand().equalsIgnoreCase("about")){
			String info = PROGRAM_NAME + "\n"			  +
						  "Version : " + VERSION + "\n\n" +
						  "Developed By :\n" 	 		  +
						  "Faris Abdullah Alasmary\n\n"   +
						  "Email :\n" 					  +
						  "farisalasmary@gmail.com";
			
			JOptionPane.showMessageDialog(null, info, "About...", JOptionPane.INFORMATION_MESSAGE);
			
		}
	}
	
	public String[] getPrayerNames(){
		return this.prayerNames;
	}
	
	public JLabel[] getPrayers(){
		return this.prayers;
	}
	
	public PrayerTimes getPrayerTimes() {
		return this.p;
	}

	public void setPrayerTimes(PrayerTimes p) {
		if(p == null)
			throw new NullPointerException();
		this.p = p;
	}
	
	public JLabel getHijriDate() {
		return hijriDate;
	}

	public void setHijriDate(JLabel hijriDate) {
		this.hijriDate = hijriDate;
	}

	public JLabel getGregorianDate() {
		return gregorianDate;
	}

	public void setGregorianDate(JLabel gregorianDate) {
		this.gregorianDate = gregorianDate;
	}

	public JLabel getCity() {
		return city;
	}

	public void setCity(JLabel city) {
		this.city = city;
	}
	

	public static void main(String[] args){
		SwingUtilities.invokeLater(new Runnable(){
			@Override
			public void run(){
				new MainGUI();				
			}
		});
	}
}


