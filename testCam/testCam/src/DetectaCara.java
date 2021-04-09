

import static org.opencv.objdetect.Objdetect.CASCADE_SCALE_IMAGE;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import javax.imageio.ImageIO;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.remote.ev3.*;
import lejos.robotics.SampleProvider;


@SuppressWarnings("serial")
public class DetectaCara extends javax.swing.JFrame {        
	
	
	
    String cara_haarcascades = "C:\\opencv\\sources\\data\\haarcascades\\haarcascade_frontalface_alt2.xml";
    CascadeClassifier faceDetector = new CascadeClassifier(cara_haarcascades);
    RemoteEV3 ev3;
    static RMIRegulatedMotor motorA;
    static RMIRegulatedMotor motorB;
    static RMIRegulatedMotor motorC;
    static RMIRegulatedMotor motorD;
    public EV3ColorSensor colorSensor;
    public SampleProvider colorRGBSensor;
    public float[] sample;
    
    public double w, h;
    public boolean opened = false;
    public Point center = new Point(0,0);
    public int screenWidth, screenHeight = 10;
    public int initialized = 0;
    public int sampleSize;
    public DetectaCara() throws RemoteException, MalformedURLException, NotBoundException {        
        initComponents();
            
    }

    @SuppressWarnings({ })
   private void initComponents() throws RemoteException, MalformedURLException, NotBoundException {
    	RemoteEV3 ev3 = new RemoteEV3("10.0.1.1"); 
		ev3.isLocal();
		ev3.setDefault();
		System.out.println("hey");
		//get ports
		
		
		ev3.getPort("B");
		ev3.getPort("C");
		ev3.getPort("D");
		
		System.out.println("hey");
		//create motors
		                                         
		if(motorB==null){motorB = ev3.createRegulatedMotor("B", 'L');}
		if(motorC==null){motorC = ev3.createRegulatedMotor("C", 'L');}
		if(motorD==null){motorD = ev3.createRegulatedMotor("D", 'L');}
		Port s4 = ev3.getPort("S4");
		  
	
		
		colorSensor= new EV3ColorSensor(s4);
		
		colorRGBSensor = colorSensor.getMode("Red");;
		
			
			
			sampleSize = colorRGBSensor.sampleSize();   
			sample = new float[sampleSize];
	
		motorD.setSpeed(4);
        jPanel1 = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 286, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 260, Short.MAX_VALUE)
        );

        jMenu1.setText("Archivo");

        jMenuItem1.setText("Iniciar");
        jMenuItem2.setText("close");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }
    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {
        (new Thread(){
            public void run(){
            	
            
				
                VideoCapture capture = new VideoCapture(0);
                MatOfRect rostros = new MatOfRect();
                MatOfByte mem = new MatOfByte();
                
                Mat frame = new Mat();
                Mat frame_gray = new Mat();
                screenWidth = frame_gray.width();
                screenHeight = frame_gray.height();
                
                Rect[] facesArray;//[][][][][]             
                Graphics g;
                BufferedImage buff = null;
              
              int foundFace = 0;
              int  noFace = 0;
   
                
                while(capture.read(frame)){
                    if(frame.empty()){
                       
                        break;
                    }else{
                    	
                        try {
                        	if (opened == false){
                          opened = true;}
                        	
                            g = jPanel1.getGraphics();
                            //Image processing
                            //remove gaps and too sharp edges
                            Imgproc.erode(frame, frame, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(4,4)));
                            Imgproc.dilate(frame, frame, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(4,4)));
                            //convert to black and white picture for better edge detection
                            Imgproc.cvtColor(frame, frame_gray, Imgproc.COLOR_BGR2GRAY);
                            //equalize histogram, balance white and black values (adapts to luminosity)
                           Imgproc.equalizeHist(frame_gray, frame_gray);
                            
                           
                            
                            faceDetector.detectMultiScale(frame_gray, rostros, 1.2, 5, 0 |CASCADE_SCALE_IMAGE, new Size(10, 10), new Size(w, h) );
                            facesArray = rostros.toArray();
                            
                            System.out.println("Faces Found: "+facesArray.length);//Nr of faces
                            Mat faceROI = new Mat();
                            
                            
                    		if (facesArray.length < 1 ){
                    			noFace++;
                    			foundFace = 0;
                    		}
                    		else{
                    			
                    			System.out.println("Found Face : " + foundFace);noFace = 0;
                    		foundFace++;	
                    		}
                    		
                    		
                    		foundFace++;
                    		if(noFace > 10){
                    			colorRGBSensor.fetchSample(sample, 0);
                        		System.out.println("Red reflection: "+sample[0]);
                        		 if(sample[0]> 0.5){motorC.setSpeed(-40);motorB.setSpeed(200);motorC.backward();
                    			motorB.forward();}
                        		else if(sample[0]> 0.4){motorC.setSpeed(0);motorB.setSpeed(200);motorC.forward();
                    			motorB.forward();}
                        		else if(sample[0]> 0.3){motorC.setSpeed(120);motorB.setSpeed(200);motorC.forward();
                    			motorB.forward();}
                        		else if(sample[0]< 0.2){motorC.setSpeed(200);motorB.setSpeed(120);motorC.forward();
                    			motorB.forward();}
                        		else if(sample[0]< 0.15){motorC.setSpeed(200);motorB.setSpeed(0);motorC.forward();
                    			motorB.forward();}
                        		else if(sample[0]< 0.05){motorC.setSpeed(200);motorB.setSpeed(40);motorC.forward();
                     			motorB.backward();}
                        		else{motorC.setSpeed(200);motorB.setSpeed(200); motorB.forward(); motorC.forward();}
                    		
                    		}
                    		else {
                    			if(foundFace > 8){
                    				System.out.println("Face detected!!!");
                    				BufferedImage shot = mat2Img(frame_gray);
                    			File f = new File("MyFile.png");
                    			ImageIO.write(shot, "PNG", f);
                    			System.out.println("Image Saved");
                    			new email();}
                    			
                    			motorB.stop(true);
                    			motorC.stop(true);
                    			
                    		}
                    		
                    		
                            for (int i = 0; i < facesArray.length; i++) {
                               
               
                            	center = new Point((facesArray[i].x + facesArray[i].width * 0.5), 
                                        (facesArray[i].y + facesArray[i].height * 0.5));
                                
                                
                              
                            	Imgproc.ellipse(frame_gray, new RotatedRect(new Point(facesArray[i].x + (facesArray[i].width/2),facesArray[i].y + (facesArray[i].height/2)), new Size(facesArray[i].width,facesArray[i].height),
                            	10), new Scalar(255, 0, 0), 5);
                                faceROI = frame_gray;
                                
                                
                                Imgproc.putText(frame_gray, "Ancho Cara: "+faceROI.width()+" Alto Cara: "+faceROI.height()+" X = "+facesArray[i].x+" Y = "+facesArray[i].y, new Point(facesArray[i].x, facesArray[i].y-20), 1, 1, new Scalar(255,255,255));
                            }
                            Imgcodecs.imencode(".bmp", faceROI, mem);
                            Image im = ImageIO.read(new ByteArrayInputStream(mem.toArray()));
                            buff = (BufferedImage) im;
                            
                			
                			if(center.x > (frame_gray.width()/2)+150){motorD.setSpeed( 20);motorD.forward();}
                            else if(center.x > (frame_gray.width()/2)){motorD.setSpeed(4);motorD.forward();}
                			else if(center.x > ((frame_gray.width()/2)-20) && center.x < ((frame_gray.width()/2)+20)){motorD.stop(true);} 
                			
                			else if(center.x < (frame_gray.width()/2)-150){motorD.setSpeed(20);motorD.backward();}
                			else if(center.x < (frame_gray.width()/2)){motorD.setSpeed(4);motorD.backward();}
                            if(g.drawImage(buff, 0, 0, jPanel1.getWidth(), jPanel1.getHeight() , 0, 0, buff.getWidth(), buff.getHeight(), null)){
                            }
                        } catch (Exception ex) {
                        	try {
								motorD.stop(true);
							} catch (RemoteException e) {
								
								e.printStackTrace();
							}
                           
                        }
                    }
                }
            }

			

		
        }).start();
    }
    public static BufferedImage mat2Img(Mat in)
    {
    	BufferedImage out;
        byte[] data = new byte[640 * 480 * (int)in.elemSize()];
        int type;
        in.get(0, 0, data);

        if(in.channels() == 1)
            type = BufferedImage.TYPE_BYTE_GRAY;
        else
            type = BufferedImage.TYPE_3BYTE_BGR;

        out = new BufferedImage(640, 480, type);

        out.getRaster().setDataElements(0, 0, 640, 480, data);
        return out;
    } 
    
    public static void main(String args[]) {
    	System.out.println(System.getProperty("java.library.path"));
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DetectaCara.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DetectaCara.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DetectaCara.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DetectaCara.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
       
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
					new DetectaCara().setVisible(true);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NotBoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
    }
  
    //display variable
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    
}
