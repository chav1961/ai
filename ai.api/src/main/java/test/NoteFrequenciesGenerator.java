package test;


public class NoteFrequenciesGenerator {
    private static final double ANCHOR = 440;
    private static final double STEP = Math.pow(2, 1/12.0);
    private static final double INV_STEP = 1 / STEP;

    public static void main(String[] args) {
        double[] frequencies = generateFrequencies(20, 8000);
        
        for (double freq : frequencies) {
            System.err.println(freq);
        }
    }

    public static double[] generateFrequencies(final double minFreq, final double maxFreq) {
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
}