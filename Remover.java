import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Dimension;
import javax.swing.JLabel;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.Scanner;
import java.util.stream.Stream;


//Template.java


class ClockLabel extends JLabel implements ActionListener
{
	String type;
	SimpleDateFormat sdf;

	public ClockLabel(String type)
	{
		this.type = type;
		setForeground(Color.blue);

		switch (type)
		{
			case "date" : sdf = new SimpleDateFormat(" MMMM dd yyyy");
				setFont(new Font("sans-serif", Font.PLAIN, 12));
				setHorizontalAlignment(SwingConstants.LEFT);
				break;
			case "time" : sdf = new SimpleDateFormat("hh:mm:ss a");
				setFont(new Font("sans-serif", Font.PLAIN, 40));
				setHorizontalAlignment(SwingConstants.CENTER);
				break;
			case "day" : sdf = new SimpleDateFormat("EEEE ");
				setFont(new Font("sans-serif", Font.PLAIN, 16));
				setHorizontalAlignment(SwingConstants.RIGHT);
				break;
			default : sdf = new SimpleDateFormat();
				break;
		}

		Timer t = new Timer(1000, this);
		t.start();
	}

	public void actionPerformed(ActionEvent ae)
	{
		Date d = new Date();
		setText(sdf.format(d));
	}
}

class Template extends JFrame implements Serializable , ActionListener
{
	JPanel _header;
	JPanel _content;
	JPanel _top;

	ClockLabel dayLable;
	ClockLabel timeLable;
	ClockLabel dateLable;

	JButton minimize , exit;

	public Template()
	{
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		GridBagLayout grid = new GridBagLayout();
		setLayout(grid);

		_top = new JPanel();
		_top.setBackground(Color.LIGHT_GRAY);

		_top.setLayout(null);

		getContentPane().add(_top,new GridBagConstraints(0,0,1,1,1,5,GridBagConstraints.BASELINE,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0));

		_header = new JPanel();
		_header.setLayout(null);

		_header.setBackground(Color.white);

		getContentPane().add(_header,new GridBagConstraints(0,1,1,1,1,20,GridBagConstraints.BASELINE,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0));

		_content = new JPanel();
		_content.setLayout(null);
		_content.setBackground(new Color(0,50,120));
		JScrollPane jsp = new JScrollPane(_content,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		jsp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

		getContentPane().add(jsp,new GridBagConstraints(0,2,1,1,1,75,GridBagConstraints.BASELINE,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0));
		setTitle("Duplicate file Search-Remover");

		Clock();
		CloseAndMin();
	}

	void CloseAndMin()
	{
		minimize=new JButton("-");
		minimize.setBackground(Color.LIGHT_GRAY);
		minimize.setBounds(MAXIMIZED_HORIZ,0,45,20 );

		exit=new JButton("X");
		exit.setHorizontalAlignment(SwingConstants.CENTER);
		exit.setBackground(Color.LIGHT_GRAY);
		exit.setHorizontalTextPosition(0);
		exit.setBounds(MAXIMIZED_HORIZ+45,0,45,20 );

		_top.add(minimize);
		_top.add(exit);

		exit.addActionListener(this);
		minimize.addActionListener(this);
	}

	public void actionPerformed(ActionEvent ae)
	{
		if ( ae.getSource() == exit )
		{
			this.setVisible(false);
			System.exit(0);
		}

		if ( ae.getSource() == minimize )
		{
			setState(JFrame.ICONIFIED);
		}
	}


	void Clock ()
	{
		dateLable = new ClockLabel("date");
		timeLable = new ClockLabel("time");
		dayLable = new ClockLabel("day");

		dateLable.setForeground (Color.blue);
		timeLable.setForeground (Color.blue);
		dayLable.setForeground (Color.blue);

		dayLable.setFont(new Font("Century",Font.BOLD,15));

		dayLable.setBounds(700,10,200, 100);

		dateLable.setFont(new Font("Century",Font.BOLD,15));

		dateLable.setBounds(800,-40,200, 100);

		timeLable.setFont(new Font("Century",Font.BOLD,15));

		timeLable.setBounds(760,-15,200, 100);

		_header.add(dateLable);
		_header.add(timeLable);
		_header.add(dayLable);
	}

	void ClockHome()
	{
		dateLable = new ClockLabel("date");
		timeLable = new ClockLabel("time");
		dayLable = new ClockLabel("day");

		dateLable.setForeground (Color.blue);
		timeLable.setForeground (Color.blue);
		dayLable.setForeground (Color.blue);
		dayLable.setFont(new Font("Century",Font.BOLD,15));
		dayLable.setBounds(200,20,200, 100);
		dateLable.setFont(new Font("Century",Font.BOLD,15));
		dateLable.setBounds(300,-40,200, 100);

		timeLable.setFont(new Font("Century",Font.BOLD,15));
		timeLable.setBounds(260,-10,200, 100);

		_header.add(dateLable);
		_header.add(timeLable);
		_header.add(dayLable);
	}
}

// DuplicateFindFront

class InvalidFileException extends Exception
{
	public InvalidFileException(String str)
	{
		super(str);
	}
}

class DuplicateFindFront extends Template implements ActionListener
{
	JButton SUBMIT ,PREVIOUS;
	JLabel label1,label2, title ;
	final JTextField text1 ;

	public DuplicateFindFront()
	{

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		title = new JLabel("Dupicate File Search");
		Dimension size = title.getPreferredSize();
		title.setBounds(40,50, size.width + 60, size.height);
		title.setFont(new Font("Century",Font.BOLD,17));
		title.setForeground (Color.blue);

		label1 = new JLabel();
		label1.setText("Directory Name");
		label1.setForeground(Color.white);
		label1.setBounds(350,50, size.width, size.height);

		text1 = new JTextField(15);
		Dimension tsize = text1.getPreferredSize();
		text1.setBounds(500,50, tsize.width, tsize.height);
		text1.setToolTipText("Enter name of directory ");


		SUBMIT=new JButton("find_Dupicate file");
		Dimension bsize = SUBMIT.getPreferredSize();
		SUBMIT.setBounds(350,200, bsize.width, bsize.height);
		SUBMIT.addActionListener(this);

		PREVIOUS = new JButton("PREVIOUS .");
		Dimension b2size = PREVIOUS.getPreferredSize();
		PREVIOUS.setBounds(600, 200, b2size.width, b2size.height);
		PREVIOUS.addActionListener(this);

		_header.add(title);
		_content.add(label1);
		_content.add(text1);
		_content.add(SUBMIT);
		_content.add(PREVIOUS);

		this.setSize(1000,400);
		this.setResizable(false);
		this.setVisible(true);
		text1.requestFocusInWindow();
	}

	public void actionPerformed(ActionEvent ae)
	{
		if ( ae.getSource() == exit )
		{
			this.setVisible(false);
			System.exit(0);
		}

		if ( ae.getSource() == minimize )
		{
			this.setState(this.ICONIFIED);
		}

		if ( ae.getSource() == SUBMIT )
		{
			try
                        {
                                FileCheckSum obj = new FileCheckSum(text1.getText());
				this.dispose();
                               
				JOptionPane.showMessageDialog(new JFrame(), "files are succesfully Deleted","Information", JOptionPane.INFORMATION_MESSAGE);

			}
		
			catch(Exception e)
			{
			}
		}

		if ( ae.getSource() == PREVIOUS )
		{
			this.setVisible(false);
			this.dispose();
		}
	}
}

 class FileCheckSum
  {
    public FileCheckSum(String str)throws Exception
    {
	   checksum(str);
           ArrayP(str,i);	
     }
		   
   static String arr[]=new String[6000];
    static String brr[]=new String[6000];
     static int i=0;
				  
   public static void checksum(String filepath)throws Exception
   {
	   MessageDigest md = MessageDigest.getInstance("MD5");
	   try(Stream <Path> paths=Files.walk(Paths.get(filepath)))
           {
            paths.forEach(filePath->
	    {
		 if(Files.isRegularFile(filePath))
		 {
		   File file=new File(filePath.toString());
		   String ffile=filePath.getFileName().toString();
		   String str=file.getAbsolutePath();
		   Check(str,md,ffile);
		 }
	    });
          }
         catch(Exception e)
         {
	   System.out.println(e);
         }
     }
     public static void Check(String file,MessageDigest md,String ffile)
     {
		 try(InputStream fis=new FileInputStream(file))
		 {
		   byte[]buffer=new byte[1024];
		   int nread;
	           while((nread=fis.read(buffer))!=-1)
	 	   {
		    md.update(buffer,0,nread);
		   }
									 
		   StringBuilder  result=new StringBuilder();
	           for(byte b:md.digest())
	           {
	             result.append(String.format("%02X",b));
                   }
	         String str=result.toString();
											  
		  arr[i]=str;
		  brr[i]=ffile;
		  i++;
	        //  System.out.println(ffile+""+str);
                 }
	      catch(IOException e)
              {
	        System.out.println(e);
	      }

        }
   public static void ArrayP(String file1,int iCnt)throws Exception
   {
      int k=0;
      String F=new String();
      for(int j=0;j<=iCnt;j++)
      {
         F=arr[j];
	 for(int i=j+1;i<=iCnt;i++)
	  {
	     if(F.equals(arr[i]))
	     {
	          k++;
                  System.out.println(brr[i]);
                 File file = new File(file1+"\\"+brr[j]); 
                  CopyDelete(file1+"\\"+brr[j]);
	     
                file.delete(); 
	      }
	    }
	 }
         System.out.println("there are total" + k + "Duplicate file present.");
      }
    
     public  static void CopyDelete(String path)
	{ 
		File copy = new File(path); 
		String name =copy.getName();
		// renaming the file and moving it to a new location 
		if(copy.renameTo 
		(new File("C:\\Users\\DHANASHRI\\Desktop\\project\\newdemo\\"+name))) 
		{  
			System.out.println("File moved successfully"); 
		} 
		else
		{ 
			System.out.println("Failed to move the file"); 
		} 
	
	} 

										 
  }


				 								
// Login

class Login extends Template implements ActionListener, Runnable
{
	JButton SUBMIT;
 	JLabel label1,label2,label3,TopLabel;
 	final JTextField text1,text2;
 	private static int attemp = 3;
	Login()
 	{
 		TopLabel = new JLabel();
 		TopLabel.setHorizontalAlignment(SwingConstants.CENTER);
 		TopLabel.setText(" Login DupliacteRemover");
 		TopLabel.setForeground(Color.BLUE);

 		Dimension topsize = TopLabel.getPreferredSize();
 		TopLabel.setBounds(50,40, topsize.width, topsize.height);
 		_header.add(TopLabel);

 		label1 = new JLabel();
 		label1.setText("Username:");
 		label1.setForeground(Color.white);

 		Dimension size = label1.getPreferredSize();

 		label1.setBounds(50,135,size.width,size.height);
 		label1.setHorizontalAlignment(SwingConstants.CENTER);

 		text1 = new JTextField(15);
 		Dimension tsize = text1.getPreferredSize();
 		text1.setBounds(200,135, tsize.width, tsize.height);

 		text1.setToolTipText("ENTER USERNAME");

 		label2 = new JLabel();
 		label2.setText("Password:");
 		label2.setBounds(50,175, size.width, size.height);
 		label2.setForeground(Color.white);
 		label2.setHorizontalAlignment(SwingConstants.CENTER);

 		text2 = new JPasswordField(15);
 		text2.setBounds(200,175, tsize.width, tsize.height);

 		text2.setToolTipText("ENTER PASSWORD");

 		text2.addFocusListener(new FocusListener()
 		{
 			public void focusGained(FocusEvent e)
 			{

 			}
 			public void focusLost(FocusEvent e)
 			{
 				label3.setText("");
 			}
 		});

 		SUBMIT=new JButton("SUBMIT");
 		SUBMIT.setHorizontalAlignment(SwingConstants.CENTER);

 		Dimension ssize = SUBMIT.getPreferredSize();

 		SUBMIT.setBounds(50,220,ssize.width,ssize.height );

 		label3 = new JLabel();
 		label3.setText("");

 		Dimension l3size = label3.getPreferredSize();

 		label3.setForeground(Color.RED);
 		label3.setBounds(50,250 , l3size.width , l3size.height);

 		Thread t = new Thread(this);
 		t.start();

 		_content.add(label1);
 		_content.add(text1);

 		_content.add(label2);
 		_content.add(text2);

 		_content.add(label3);
 		_content.add(SUBMIT);

 		pack();
 		validate();

 		ClockHome();
 		setVisible(true);
 		this.setSize(500,500);
 		this.setResizable(false);
 		setLocationRelativeTo(null);
 		SUBMIT.addActionListener(this);
 	}
 	public boolean Validate(String Username, String password)
 	{
 		if((Username.length()<8)||(password.length() < 8))
 			return false;
 		else
 			return true;
 	}

 	public void actionPerformed(ActionEvent ae)
 	{
 		String value1=text1.getText();
 		String value2=text2.getText();

 		if ( ae.getSource() == exit )
 		{
 			this.setVisible(false);
 			System.exit(0);
 		}

 		if ( ae.getSource() == minimize )
 		{
 			this.setState(this.ICONIFIED);
 		}

 		if(ae.getSource()==SUBMIT)
 		{
 			if(Validate(value1,value2) == false)
 			{
 				text1.setText("");
 				text2.setText("");
 				JOptionPane.showMessageDialog(this, "Short username","Duplicate File Remover", JOptionPane.ERROR_MESSAGE);
 			}
 			if (value1.equals("DuplicateRemover") && value2.equals("DuplicateRemover"))
 			{
 				
 				this.setVisible(false);
 				DuplicateFindFront obj = new DuplicateFindFront(); 
 			}
 			else
 			{
 				attemp--;

				if(attemp == 0)
 				{
 					JOptionPane.showMessageDialog(this, "Number of attempts finished","Duplicate Files Remover", JOptionPane.ERROR_MESSAGE);
 					this.dispose();
 					System.exit(0);
 				}

 				JOptionPane.showMessageDialog(this, "Incorrect login or password","Error", JOptionPane.ERROR_MESSAGE);
 			}
 		}
 	}

	public void run()
 	{
 		for(;;)
 		{
 			if(text2.isFocusOwner())
 			{
 				if( Toolkit.getDefaultToolkit().getLockingKeyState (KeyEvent.VK_CAPS_LOCK ) )
 				{
 					text2.setToolTipText("Warning : CAPS LOCK is on");
 				}
 				else
					text2.setToolTipText("");

 				if((text2.getText()).length() < 8)
 					label3.setText("Weak Password");
 				else
 					label3.setText("");
 			}
 		}
 	}
}
yyx               
class Remover
{
 	public static void main(String arg[])
 	{
 		try
 		{ 
                        Login frame=new Login();
                       frame.setVisible(true);
                               
 		}
 		catch(Exception e)
 		{
 			JOptionPane.showMessageDialog(null, e.getMessage());
		}
 	}
}
