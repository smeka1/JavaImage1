

import java.awt.*;
import java.awt.image.*;
import java.io.*;
//import java.util.concurrent.TimeUnit;
import java.awt.Color;

import javax.swing.*;


public class Part3Conv {

        JFrame frame;
        JLabel lbIm1;
        JLabel lbIm2;
        BufferedImage img;
        //averaging(int[] p, int a);
        public void showIms(String[] args) throws InterruptedException{
               
        	String outputname = new String(args[1]);
            String sdorhd =  new String(args[2]);
            //String sss = "\""+args[0]+ "\"";
                outputname = outputname.replace('/', '\\');
                //System.out.println(outputname);
        	    int width = 0, height = 0;
        	    int w2=0, h2=0;
        	    if (sdorhd.equalsIgnoreCase("SD2HD") )
        	    {
        	      width = 176; height = 144;
        	      w2 = 960; h2 =540;
        	    }
        	    if (sdorhd.equalsIgnoreCase("HD2SD") )
        	    {
        	      width = 960; height = 540;
        	      w2 = 176; h2 =144;
        	    }
        	    
                int fil = Integer.parseInt(args[3]);
                
                img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

                try {
                        File file = new File(args[0]);
                        InputStream is = new FileInputStream(file);
                        long len = file.length();
                        byte[] bytes = new byte[(int)len];
                        int pixels[] = new int[width*height];
                        int offset = 0;
                        int numRead = 0;
                        while (offset < bytes.length && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
                                offset += numRead;
                        }
                        
                        frame = new JFrame();
                        GridBagLayout gLayout = new GridBagLayout();
                        frame.getContentPane().setLayout(gLayout);
                        String result = String.format("Video height: %d, width: %d", height, width);
                        JLabel lbText1 = new JLabel(result);
                        lbText1.setHorizontalAlignment(SwingConstants.CENTER);
                        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                       
                       byte[] outp = new byte[(int) ((w2*h2)*(len/(width*height)) ) ];
                       int fr= (int) (len / (width*height)) ;
                       int ind = 0;
                        int[] temp = new int[w2*h2] ;
                        double x_ratio = width/(double)w2 ;
                        double y_ratio = height/(double)h2 ;
                        double px, py ; int pp=0; int pix=0; int z2=0;
                               
                        for(int z=0;  z<  len; z += 3*height*width) {
                                        ind = z;
                                        for(int y = 0; y < height; y++) {
                                                for(int x = 0; x < width; x++) {
                                                   byte r = bytes[ind];
                                                   byte g = bytes[ind+height*width];
                                                   byte b = bytes[ind+height*width*2];
                                                    pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
                                                  //int pix = ((a << 24) + (r << 16) + (g << 8) + b);           
                                                   img.setRGB(x,y,pix);
                                                   pixels[pp] = pix; pp++;
                                                   ind++;
                                                }         
                                          }  
                                            pp=0;
                                            
                                            //3x3 Averaging function   
                                    if(fil == 1)
                                         pixels = averaging (pixels,width,height);
                                      
                                            int oi=z2;
                                            for (int i=0;i<h2;i++) { 
                                                for (int j=0;j<w2;j++) {
                                                    px = Math.floor(j*x_ratio) ;
                                                    py = Math.floor(i*y_ratio) ;
                                               Color c = new Color( pixels[(int)((py*width)+px)] );
                                                
                                                            outp[oi] =  (byte) c.getRed();
                                                            outp[oi+w2*h2] = (byte) c.getGreen();
                                                            outp[oi+w2*h2*2] = (byte) c.getBlue();
                                                            oi++;
                                                } //System.out.println((int)((py*width)+px));
                                            }
                                         z2+= 3*w2*h2;                                               
                                     /*   GridBagConstraints c = new GridBagConstraints();
                                        c.fill = GridBagConstraints.HORIZONTAL;
                                        c.anchor = GridBagConstraints.CENTER;
                                        c.weightx = 0.5;
                                        c.gridx = 0;
                                        c.gridy = 0;
                                        frame.getContentPane().add(lbText1, c);
                                        lbIm1 = new JLabel(new ImageIcon(img));
                                        c.fill = GridBagConstraints.HORIZONTAL;
                                        c.gridx = 0;
                                        c.gridy = 1;
                                        frame.getContentPane().add(lbIm1, c);
                                        frame.pack();
                                        frame.setVisible(true);   
                                        TimeUnit.MILLISECONDS.sleep(p);             */
                                }    // Close ALL frames  
                                //System.out.println("Done maybe\n");
                                FileOutputStream FO = new FileOutputStream(outputname); 
                                FO.write(outp);
                                
                            /*    for(int z=0;  z<  len; z += 3*h2*w2) {
                                    ind = z;
                                    for(int y = 0; y < h2; y++) {
                                            for(int x = 0; x < w2; x++) {
                                              	
                                              
                                            }
                                    }
                                    }*/
                                
                } catch (FileNotFoundException e) {
                        e.printStackTrace();
                } catch (IOException e) {
                        e.printStackTrace();
                }catch (ArrayIndexOutOfBoundsException e) {
                        e.printStackTrace();
                }       
                // Use labels to display the images
        }
//Averaging using 3x3 
        public int[] averaging (int[] pp,int w,int h)
        {   
        	int[][] p = new int[h][w];  int pix=0;
     	    for (int i=0; i<h ;i++) {
     		   for(int j=0;j<w ; j++) {
     			   p[i][j] = pp[i*w + j];
     		    }   
     	    }
     		   
        	for(int i=0; i <h;i++) {
     	    	for(int j=0;j<w;j++) {
     	    		Color c1,c2,c3,c4,c5,c6,c7,c8,c9;  byte r,g,b;
     	    		/* if (j+1<h) (i+1<w)  (i-1 >=0) (j-1>=0)            */   
     	    		
     	    		if ( (j+1<w) && (i+1<h) &&  (i-1 >=0) && (j-1>=0) )
     	    		{  
     	    		 //	average( p[i][j] , p[i][j-1], p[i][j+1], p[i-1][j-1], p[i-1][j], p[i-1][j+1], p[i+1][j-1],p[i+1][j], p[i+1][j+1] )  
     	    		  c1 = new Color(p[i][j]);	
     	    		  c2 = new Color(p[i][j-1]);
     	    		  c3 = new Color(p[i][j+1]);	
    	    		  c4 = new Color(p[i-1][j-1]);
    	    		  c5 = new Color(p[i-1][j]);	
     	    		  c6 = new Color(p[i-1][j+1]);
     	    		  c7 = new Color(p[i+1][j-1]);
   	    		      c8 = new Color(p[i+1][j]);	
    	    		  c9 = new Color(p[i+1][j+1]);	
    	    		  r= (byte) (( c1.getRed()+c2.getRed()+c3.getRed()+c4.getRed()+c5.getRed()+c6.getRed()+c7.getRed()+c8.getRed()+c9.getRed() )/9) ;
    	    		  b = (byte) (( c1.getBlue()+c2.getBlue()+c3.getBlue()+c4.getBlue()+c5.getBlue()+c6.getBlue()+c7.getBlue()+c8.getBlue()+c9.getBlue() )/9);
    	    		  g = (byte) (( c1.getGreen()+c2.getGreen()+c3.getGreen()+c4.getGreen()+c5.getGreen()+c6.getGreen()+c7.getGreen()+c8.getGreen()+c9.getGreen() )/9);
    	    		  pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
    	    		  p[i][j] = pix;
     	    		}
     	    		
     	    	else if ( (j+1 <w) && (i+1<h) && (i-1 >=0) && (j-1<0) )
     	    		{ //average( p[i][j] , p[i][j+1], p[i-1][j], p[i-1][j+1], p[i+1][j], p[i+1][j+1] ) 
     	    		c1 = new Color(p[i][j]);	
   	    		  c2 = new Color(p[i][j+1]);
   	    		  c3 = new Color(p[i+1][j+1]);	
  	    		  c4 = new Color(p[i+1][j]);
  	    		  c5 = new Color(p[i-1][j]);	
   	    		  c6 = new Color(p[i-1][j+1]);
   	    		  
   	    		r= (byte) (( c1.getRed()+c2.getRed()+c3.getRed()+c4.getRed()+c5.getRed()+c6.getRed() )/6) ;
	    		  b = (byte) (( c1.getBlue()+c2.getBlue()+c3.getBlue()+c4.getBlue()+c5.getBlue()+c6.getBlue() )/6);
	    		  g = (byte) (( c1.getGreen()+c2.getGreen()+c3.getGreen()+c4.getGreen()+c5.getGreen()+c6.getGreen() )/6);
	    		  pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
	    		  p[i][j] = pix;	
     	    		}
     	    		
     	    		else if ( (j+1 <w) && (i+1<h) && (j-1>=0) && (i-1<0) )
     	    		{ //average( p[i][j] , p[i][j-1], p[i][j+1], p[i+1][j-1],p[i+1][j], p[i+1][j+1] )   
     	    			c1 = new Color(p[i][j]);	
     	    		  c2 = new Color(p[i][j-1]);
     	    		  c3 = new Color(p[i+1][j+1]);	
    	    		  c4 = new Color(p[i+1][j-1]);
    	    		  c5 = new Color(p[i+1][j]);	
     	    		  c6 = new Color(p[i][j+1]);
     	    		  
     	    		r= (byte) (( c1.getRed()+c2.getRed()+c3.getRed()+c4.getRed()+c5.getRed()+c6.getRed() )/6) ;
  	    		  b = (byte) (( c1.getBlue()+c2.getBlue()+c3.getBlue()+c4.getBlue()+c5.getBlue()+c6.getBlue() )/6);
  	    		  g = (byte) (( c1.getGreen()+c2.getGreen()+c3.getGreen()+c4.getGreen()+c5.getGreen()+c6.getGreen() )/6);
  	    		  pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
  	    		  p[i][j] = pix;	
     	    		
     	    		}
     	    		
     	    /*		else if ( (j+1<w) && (i+1<h)&& (j-1<0) && (i-1<0) )
     	    		{  average( p[i][j] , p[i][j+1], p[i+1][j], p[i+1][j+1] )  }  
     	    		else if ( (j+1>w) && (i+1<h) &&  (i-1 >=0) && (j-1>=0) )
     	    		{  average( p[i][j] , p[i][j-1], p[i-1][j-1], p[i-1][j], p[i+1][j-1],p[i+1][j] )  }
     	    		else if ( (j+1<w) && (i+1>h) &&  (i-1 >=0) && (j-1>=0) )
     	    		{  average( p[i][j] , p[i][j-1], p[i][j+1], p[i-1][j-1], p[i-1][j], p[i-1][j+1] )  }
     	    		else if ( (j+1>w) && (i+1>h) &&  (i-1 >=0) && (j-1>=0) )
     	    		{  average( p[i][j] , p[i][j-1], p[i-1][j-1], p[i-1][j]  )  }
     	    		else{}  */
     	    	}
     	    }
        	
        	 for (int i=0; i<h ;i++) {
       		   for(int j=0;j<w ; j++) {
       			   pp[i*w + j]=  p[i][j];
       		    }   
       	    }
        	return pp;
        }
        
        public static void main(String[] args) throws InterruptedException, IOException {
                Part3Conv ren = new Part3Conv();
                ren.showIms(args);
        }

}