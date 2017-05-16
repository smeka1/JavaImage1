package multi1;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.concurrent.TimeUnit;

import javax.swing.*;

public class ImageReaderOrg {

        JFrame frame;
        JLabel lbIm1;
        JLabel lbIm2;
        BufferedImage img;
        
        public ImageReaderOrg() {
			// TODO Auto-generated constructor stub
		}

        public ImageReaderOrg(String[] ss) throws Exception, InterruptedException
        {
          Player(ss,Integer.parseInt(ss[1]), Integer.parseInt(ss[2]) );
        }
        
		public void Player(String[] args,int Width,int Height) throws InterruptedException{
                int width = Width;
                int height = Height;
                
                img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                BufferedImage[] frames = new BufferedImage[4];
                
                int p = 1000/Integer.parseInt(args[3]);
                try {
                        File file = new File(args[0]);
                        InputStream is = new FileInputStream(file);

                        long len = file.length();
                       byte[] bytes = new byte[(int)len];

                        int offset = 0;
                        int numRead = 0;
                        while (offset < bytes.length && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
                                offset += numRead;
                        }

                        frame = new JFrame();
                        
                        String vidinfo = String.format("Video height: %d, width: %d", height, width);
                        GridBagLayout gLayout = new GridBagLayout();
                        frame.getContentPane().setLayout(gLayout);
                        
                        JLabel lbText1 = new JLabel(vidinfo);
                        
                        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                        lbText1.setHorizontalAlignment(SwingConstants.CENTER);
                       System.out.println("Size of bytes is " + bytes.length);
                       System.out.println("No of frames is" + (bytes.length)/(3*width*height));
                        int index = 0;
                        
                        while(true){                               
                        	for(int z=0;  z<  len; z += 3*height*width) {
                        		   
                                        index = z;
                                        for(int y = 0; y < height; y++) {

                                                for(int x = 0; x < width; x++) {
                                    // byte - [rrrrrrrrrrrrggggggggggggggbbbbbbbbbbbbb]
                                                   byte r = bytes[index];
                                                   byte g = bytes[index+height*width];
                                                   byte b = bytes[index+height*width*2];
                                                   int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
                                                   img.setRGB(x,y,pix);
                                                   index++;
                                                }
                                        }
                                        //frames[z]= img;
                                        GridBagConstraints c = new GridBagConstraints();
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
                                        TimeUnit.MILLISECONDS.sleep(p);
                                }
                       }
        

                } catch (FileNotFoundException e) {
                        e.printStackTrace();
                } catch (IOException e) {
                        e.printStackTrace();
                }catch (ArrayIndexOutOfBoundsException e) {
                        e.printStackTrace();
                }

                // Use labels to display the images
        }

        public static void main(String[] args) throws Exception {
                //ImageReaderOrg play = new ImageReaderOrg();
                 new ImageReaderOrg(args);
        }
}