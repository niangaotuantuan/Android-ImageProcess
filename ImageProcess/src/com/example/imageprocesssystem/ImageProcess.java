package com.example.imageprocesssystem;

import android.graphics.Bitmap;

public class ImageProcess {
	public ImageProcess() {
		// TODO Auto-generated constructor stub
	}
	
	public Bitmap addSaltAndPepperNoise(float SNR, Bitmap myBitmap) {
		int width = myBitmap.getWidth();
        int height = myBitmap.getHeight();
        //Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        //if ( dst == null )
          //  dst = createCompatibleDestImage( src, null );

        int[] pixNew = new int[width * height];
    	int[] pixOld = new int[width * height];
    	myBitmap.getPixels(pixNew, 0, width, 0, 0, width, height);
    	myBitmap.getPixels(pixOld, 0, width, 0, 0, width, height);
        
        int index = 0;
        //float SNR = 0.5f;
        int size = (int)(pixOld.length * (1-SNR));

        for(int i=0; i<size; i++) {
        	int row = (int)(Math.random() * (double)height);
        	int col = (int)(Math.random() * (double)width);
        	index = row * width + col;
        	pixNew[index] = (255 << 24) | (255 << 16) | (255 << 8) | 255;
        }

     // Change bitmap to use new array
    	Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
    	bitmap.setPixels(pixNew, 0, width, 0, 0, width, height);
    	myBitmap = null;
    	pixOld = null;
    	pixNew = null;
    	return bitmap;
	}

	/*private Bitmap gaussianNoise(Bitmap myBitmap) {
		int width = myBitmap.getWidth();
        int height = myBitmap.getHeight();

        int[] pixNew = new int[width * height];
    	int[] pixOld = new int[width * height];
    	myBitmap.getPixels(pixNew, 0, width, 0, 0, width, height);
    	myBitmap.getPixels(pixOld, 0, width, 0, 0, width, height);
    	
    	
        //int[] inPixels = new int[width*height];
        int[][][] tempPixels = new int[height][width][4]; 
        //int[] outPixels = new int[width*height];
        //getRGB( src, 0, 0, width, height, inPixels );
        int index = 0;
        float inMax = 0;
        float outMax = 0;
        for(int row=0; row<height; row++) {
        	int ta = 0, tr = 0, tg = 0, tb = 0;
        	for(int col=0; col<width; col++) {
        		index = row * width + col;
        		ta = (pixOld[index] >> 24) & 0xff;
                tr = (pixOld[index] >> 16) & 0xff;
                tg = (pixOld[index] >> 8) & 0xff;
                tb = pixOld[index] & 0xff;
                if(inMax < tr) {
                	inMax = tr;
                }
                if(inMax < tg) {
                	inMax = tg;
                }
                if(inMax < tb) {
                	inMax = tb;
                }
                tr = (int)((float)tr + getGaussianValue() + this.means);
                tg = (int)((float)tg + getGaussianValue() + this.means);
                tb = (int)((float)tb + getGaussianValue() + this.means);
                if(outMax < tr) {
                	outMax = tr;
                }
                if(outMax < tg) {
                	outMax = tg;
                }
                if(outMax < tb) {
                	outMax = tb;
                }
                tempPixels[row][col][0] = ta;
                tempPixels[row][col][1] = tr;
                tempPixels[row][col][2] = tg;
                tempPixels[row][col][3] = tb;
        	}
        }

        // Normalization
        index = 0;
        float rate = inMax/outMax;
        for(int row=0; row<height; row++) {
        	int ta = 0, tr = 0, tg = 0, tb = 0;
        	for(int col=0; col<width; col++) {
        		index = row * width + col;
        		ta = tempPixels[row][col][0];
        		tr = tempPixels[row][col][1];
        		tg = tempPixels[row][col][2];
        		tb = tempPixels[row][col][3];

        		tr = (int)((float)tr * rate);
        		tg = (int)((float)tg * rate);
        		tb = (int)((float)tb * rate);
        		pixNew[index] = (ta << 24) | (tr << 16) | (tg << 8) | tb;
        	}
        }
     // Change bitmap to use new array
    	Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
    	bitmap.setPixels(pixNew, 0, width, 0, 0, width, height);
    	myBitmap = null;
    	pixOld = null;
    	pixNew = null;
    	return bitmap;
	}
    */
	
	public Bitmap brighten(int brightenOffset,Bitmap myBitmap)
    {
    	// Create new array
    	int width = myBitmap.getWidth();
    	int height = myBitmap.getHeight();
    	int[] pix = new int[width * height];
    	myBitmap.getPixels(pix, 0, width, 0, 0, width, height);
    	
    	// Apply pixel-by-pixel change
    	int index = 0;
    	for (int y = 0; y < height; y++)
    	{
    		for (int x = 0; x < width; x++)
    		{
    			int r = (pix[index] >> 16) & 0xff;
    			int g = (pix[index] >> 8) & 0xff;
    			int b = pix[index] & 0xff;
    			r = Math.max(0, Math.min(255, r + brightenOffset));
    			g = Math.max(0, Math.min(255, g + brightenOffset));
    			b = Math.max(0, Math.min(255, b + brightenOffset));
    			pix[index] = 0xff000000 | (r << 16) | (g << 8) | b;
    			index++;
    		} // x
    	} // y
    	
    	// Change bitmap to use new array
    	Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
    	bitmap.setPixels(pix, 0, width, 0, 0, width, height);    	
    	myBitmap = null;
    	pix = null;
    	return bitmap;
    }
    
    // filterWidth and filterHeight must be odd numbers
    public Bitmap averageFilter(int filterWidth, int filterHeight,Bitmap myBitmap)
    {
    	// Create new array
    	int width = myBitmap.getWidth();
    	int height = myBitmap.getHeight();
    	int[] pixNew = new int[width * height];
    	int[] pixOld = new int[width * height];
    	myBitmap.getPixels(pixNew, 0, width, 0, 0, width, height);
    	myBitmap.getPixels(pixOld, 0, width, 0, 0, width, height);
    	
    	// Apply pixel-by-pixel change
    	int filterHalfWidth = filterWidth/2;
    	int filterHalfHeight = filterHeight/2;
    	int filterArea = filterWidth * filterHeight;
    	for (int y = filterHalfHeight; y < height-filterHalfHeight; y++)
    	{
    		for (int x = filterHalfWidth; x < width-filterHalfWidth; x++)
    		{
    			// Accumulate values in neighborhood
    			int accumR = 0, accumG = 0, accumB = 0;
    			for (int dy = -filterHalfHeight; dy <= filterHalfHeight; dy++)
    			{
    				for (int dx = -filterHalfWidth; dx <= filterHalfWidth; dx++)
    				{
    					int index = (y+dy)*width + (x+dx);
    					accumR += (pixOld[index] >> 16) & 0xff;
    					accumG += (pixOld[index] >> 8) & 0xff;
    					accumB += pixOld[index] & 0xff;
    				} // dx
    			} // dy
    			
    			// Normalize
    			accumR /= filterArea;
    			accumG /= filterArea;
    			accumB /= filterArea;
    			int index = y*width + x;
    			pixNew[index] = 0xff000000 | (accumR << 16) | (accumG << 8) | accumB;
    		} // x
    	} // y
    	
    	// Change bitmap to use new array
    	Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
    	bitmap.setPixels(pixNew, 0, width, 0, 0, width, height);
    	myBitmap = null;
    	pixOld = null;
    	pixNew = null;
    	return bitmap;
    }
    
    // filterWidth and filterHeight must be odd numbers
    public Bitmap medianFilter(int filterWidth, int filterHeight,Bitmap myBitmap)
    {
    	// Create new array
    	int width = myBitmap.getWidth();
    	int height = myBitmap.getHeight();
    	int[] pixNew = new int[width * height];
    	int[] pixOld = new int[width * height];
    	myBitmap.getPixels(pixNew, 0, width, 0, 0, width, height);
    	myBitmap.getPixels(pixOld, 0, width, 0, 0, width, height);
    	
    	// Apply pixel-by-pixel change
    	int filterHalfWidth = filterWidth/2;
    	int filterHalfHeight = filterHeight/2;
    	int filterArea = filterWidth * filterHeight;
    	for (int y = filterHalfHeight; y < height-filterHalfHeight; y++)
    	{
    		for (int x = filterHalfWidth; x < width-filterHalfWidth; x++)
    		{
    			// Accumulate values in neighborhood
    			int accumR = 0, accumG = 0, accumB = 0;
    			for (int dy = -filterHalfHeight; dy <= filterHalfHeight; dy++)
    			{
    				for (int dx = -filterHalfWidth; dx <= filterHalfWidth; dx++)
    				{
    					int index = (y+dy)*width + (x+dx);
    					accumR += (pixOld[index] >> 16) & 0xff;
    					accumG += (pixOld[index] >> 8) & 0xff;
    					accumB += pixOld[index] & 0xff;
    				} // dx
    			} // dy
    			
    			// Normalize
    			accumR /= filterArea;
    			accumG /= filterArea;
    			accumB /= filterArea;
    			int index = y*width + x;
    			pixNew[index] = 0xff000000 | (accumR << 16) | (accumG << 8) | accumB;
    		} // x
    	} // y
    	
    	// Change bitmap to use new array
    	Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
    	bitmap.setPixels(pixNew, 0, width, 0, 0, width, height);
    	myBitmap = null;
    	pixOld = null;
    	pixNew = null;
    	return bitmap;
    }
}
