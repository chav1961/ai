package test;

public class BandpassFilter {
    private final double[] b; // Коэффициенты фильтра
    private final double[] z; // Буфер задержки (память фильтра)
    private int m;            // Порядок фильтра

    public BandpassFilter(int order, double fLow, double fHigh, double fs) {
        this.m = order;
        this.b = new double[order + 1];
        this.z = new double[order + 1];

        final double omegaLow = 2 * Math.PI * fLow / fs;
        final double omegaHigh = 2 * Math.PI * fHigh / fs;
        final int center = order / 2;

        for (int n = 0; n <= order; n++) {
            int shift = n - center;
            
            if (shift == 0) {
                b[n] = (omegaHigh - omegaLow) / Math.PI;
            } else {
                b[n] = (Math.sin(omegaHigh * shift) - Math.sin(omegaLow * shift)) / (Math.PI * shift);
            }
            final double window = 0.54 - 0.46 * Math.cos(2 * Math.PI * n / order);
            
            b[n] *= window;
        }
    }

    /**
     * Обработка одного отсчета сигнала (Real-time filtering)
     */
    public double process(double input) {
        // Сдвиг буфера задержки
        System.arraycopy(z, 0, z, 1, m);
        z[0] = input;

        // Вычисление свертки
        double output = 0;
        for (int i = 0; i <= m; i++) {
            output += b[i] * z[i];
        }
        return output;
    }

    public static void main(String[] args) {
        // Параметры из вашего примера
        int order = 50; // Чем выше порядок, тем круче срез
        BandpassFilter filter = new BandpassFilter(order, 30.0, 40.0, 200.0);

        // Пример: входной сигнал (смесь 35Гц и 80Гц)
        for (int i = 0; i < 100; i++) {
            double t = i / 200.0;
            double signal = Math.sin(2 * Math.PI * 35 * t) + Math.sin(2 * Math.PI * 80 * t);
            double filtered = filter.process(signal);

            System.out.printf("Отсчет %d: Вход = %.2f, Выход (35Гц) = %.2f%n", i, signal, filtered);
        }
    }
}