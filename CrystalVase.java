import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class CrystalVase
{
    private static final int NUM_VIEWERS = 50;

    public static void main(String[] args) throws InterruptedException
    {
        VaseViewer[] vaseViewers = new VaseViewer[NUM_VIEWERS];
        VaseViewer.setNumViewers(NUM_VIEWERS);
        int nextViewer = new Random().nextInt(0, NUM_VIEWERS);

        long startTimeNanoseconds = System.nanoTime();

        for (int i = 0; i < NUM_VIEWERS; i++)
        {
            vaseViewers[i] = new VaseViewer(i);
            vaseViewers[i].start();
        }

        for (int i = 0; i < NUM_VIEWERS; i++)
        {
            vaseViewers[i].join();
        }

        long endTimeNanoseconds = System.nanoTime();

        long runTimeMilliseconds = (endTimeNanoseconds - startTimeNanoseconds) / 1000000;

        System.out.println("All viewers have visited the vase at least once. Runtime was " + runTimeMilliseconds + " ms");
    }
}

class Sign
{
    private final AtomicBoolean isBusy = new AtomicBoolean(true);

    private void enterShowroom(VaseViewer vaseViewer)
    {
        VaseViewer.setViewerCounter(VaseViewer.getViewerCounter() + 1);
        vaseViewer.setHasViewedVase(true);
        System.out.println("Viewer " + vaseViewer.getViewerNumber() + " has seen the vase");
        isBusy.set(true);
    }

    private void leaveShowroom()
    {
        isBusy.set(false);
    }

    public boolean getIsBusy()
    {
        return isBusy.get();
    }

    public synchronized void goToSign(VaseViewer vaseViewer)
    {
        enterShowroom(vaseViewer);
        leaveShowroom();
    }
}

class VaseViewer extends Thread
{
    private static final Sign sign = new Sign();
    private static int NUM_VIEWERS;
    private static int viewerCounter; // Program will stop after all people have seen vase
    private final int viewerNumber;
    private boolean hasViewedVase;

    public VaseViewer(int viewerNumber)
    {
        this.viewerNumber = viewerNumber;
    }

    public static void setNumViewers(int numViewers)
    {
        NUM_VIEWERS = numViewers;
    }

    public static int getViewerCounter()
    {
        return viewerCounter;
    }

    public static void setViewerCounter(int viewerCounter)
    {
        VaseViewer.viewerCounter = viewerCounter;
    }

    public void run()
    {
        while (viewerCounter < NUM_VIEWERS)
        {
            if (!sign.getIsBusy() && !hasViewedVase)
            {
                sign.goToSign(this);
            }
        }
    }

    public int getViewerNumber()
    {
        return viewerNumber;
    }

    public void setHasViewedVase(boolean hasViewedVase)
    {
        this.hasViewedVase = hasViewedVase;
    }
}
