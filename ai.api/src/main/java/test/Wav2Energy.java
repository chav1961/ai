package test;

import java.util.Arrays;

public class Wav2Energy {
    private static final double ANCHOR = 440;
    private static final double STEP = Math.pow(2, 1/12.0);
    private static final double INV_STEP = 1 / STEP;

    private final double[]		buffer;
    private final double[]		freqs;
    private final double[][]	koeffs;
    private final double[][]	windows;
    private int					currentWindow = 0;
    
    public Wav2Energy(final double lowFreq, final double highFreq, final double samplesFreq, final int order, final int window) {
    	if (lowFreq <= 0 || lowFreq > highFreq || lowFreq > samplesFreq/2) {
    		throw new IllegalArgumentException("Low frequency ["+lowFreq+"] must be in range [1.."+Math.min(highFreq, samplesFreq/2));
    	}
    	else if (highFreq <= 0 || highFreq < lowFreq || highFreq > samplesFreq/2) {
    		throw new IllegalArgumentException("High frequency ["+highFreq+"] must be in range ["+lowFreq+".."+(samplesFreq/2));
    	}
    	else if (samplesFreq/2 < lowFreq || samplesFreq/2 < highFreq) {
    		throw new IllegalArgumentException("Samples frequency ["+samplesFreq+"] must be greater than "+Math.max(2*lowFreq, 2*highFreq));
    	}
    	else if (order <= 1) {
    		throw new IllegalArgumentException("Filter order ["+order+"] must me greater than 1");
    	}
    	else if (window <= samplesFreq / 50) {
    		throw new IllegalArgumentException("Window size ["+window+"] must me greater than "+(int)(samplesFreq / 50));
    	}
    	else {
	    	this.buffer = new double[order];
	    	this.freqs = generateFrequencies(lowFreq, highFreq);
	    	this.koeffs = new double[freqs.length-1][];
	    	this.windows = new double[window][koeffs.length];
	    	
	    	for(int index = 0; index < koeffs.length; index++) {
	    		koeffs[index] = buildBandpassFilter(freqs[index], freqs[index+1], samplesFreq, order);
	    	}
    	}
	}
	
	public int getNumberOfBars() {
		return koeffs.length;
	}

	public void calcEnergies(final double currentVal, final double[] energies) {
		if (energies == null || energies.length != koeffs.length) {
			throw new IllegalArgumentException("Energies array can't be null and must have length="+koeffs.length);
		}
		else {
			double[] temp = windows[currentWindow];
			
			Arrays.fill(energies, 0);
			calcAmplitude(currentVal, temp);
			
			for (int index = 0; index < koeffs.length; index++) {
				temp = windows[index];
				
				for(int barIndex = 0; barIndex < energies.length; barIndex++) {
					double val = temp[barIndex];
					
					energies[barIndex] += val * val;  
				}
			}
			if (++currentWindow >= windows.length) {
				currentWindow = 0;
			}
		}
	}
	
	private void calcAmplitude(final double currentVal, final double[] amplitudes) {
		final double[] temp = buffer;
		
		System.arraycopy(temp, 0, temp, 1, temp.length-1);
		temp[0] = currentVal;
		
		for (int index = 0; index < koeffs.length; index++) {
			final double[] currentK = koeffs[index];
			double	sum = 0;
					
			for(int kIndex = 0; kIndex < currentK.length; kIndex++) {
				sum += temp[kIndex] * currentK[index];
			}
			amplitudes[index] = sum;
		}
	}
	
    private static double[] generateFrequencies(final double minFreq, final double maxFreq) {
    	final int startNote = calcStep(minFreq), endNote = calcStep(maxFreq);
    	final int steps = endNote-startNote;
    	final double[] result = new double[steps];
    	
        for (int index = 0; index < result.length; index++) {
        	result[index] = ANCHOR * Math.pow(2, (index + startNote) / 12.0);
        }
        return result;
    }

	private static int calcStep(double freq) {
		int	result = 0;
		
		if (freq > ANCHOR) {
			for(; freq > ANCHOR; freq *= INV_STEP, result++) {
			}
		}
		else if (freq < ANCHOR) {
			for(; freq < ANCHOR; freq *= STEP, result--) {
			}
		}
		return result;
	}
	
    private static double[] buildBandpassFilter(final double fLow, final double fHigh, final double fs, final int order) {
        final double[]	result = new double[order + 1];
        final double 	omegaLow = 2 * Math.PI * fLow / fs;
        final double 	omegaHigh = 2 * Math.PI * fHigh / fs;
        final int 		center = order / 2;

        for (int index = 0; index <= order; index++) {
            final int 		shift = index - center;
            final double	window = 0.54 - 0.46 * Math.cos(2 * Math.PI * index / order);
            final double	k;
            
            if (shift == 0) {
            	k = (omegaHigh - omegaLow) / Math.PI;
            } else {
            	k = (Math.sin(omegaHigh * shift) - Math.sin(omegaLow * shift)) / (Math.PI * shift);
            }
            result[index] = k * window;
        }
        return result;
    }
	
}
