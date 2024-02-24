import java.util.Random;

public class MinotaurBirthdayParty
{
    private static final int NUM_GUESTS = 50;

    public static void main(String[] args) throws InterruptedException
    {
        Guest[] guests = new Guest[NUM_GUESTS];
        Guest.setNumGuests(NUM_GUESTS);
        int mazeGuest = new Random().nextInt(0, NUM_GUESTS);
        Guest.setGuestInLabyrinth(mazeGuest);

        for (int i = 0; i < NUM_GUESTS; i++)
        {
            guests[i] = new Guest(i);
            guests[i].start();
        }

        while (Guest.getCupcakeCounter() < NUM_GUESTS)
        {
            mazeGuest = new Random().nextInt(0, NUM_GUESTS);
            Guest.setGuestInLabyrinth(mazeGuest);
        }

        for (int i = 0; i < NUM_GUESTS; i++)
        {
            guests[i].join();
        }

        System.out.println("All guests have visited the labyrinth at least once.");
    }
}

class Labyrinth
{
    private boolean isCupcakePresent;

    public Labyrinth()
    {
        this.isCupcakePresent = true;
    }

    private void requestCupcake()
    {
        isCupcakePresent = true;
    }

    private void eatCupcake(Guest guest)
    {
        System.out.println(guest.getGuestNumber() + " ate the cupcake!");
        isCupcakePresent = false;
    }

    public synchronized void enterLabyrinth(Guest guest)
    {
        if (Guest.getGuestInLabyrinth() != guest.getGuestNumber())
        {
            return;
        }

        if (guest.getGuestNumber() == 0)
        {
            if (isCupcakePresent && guest.hasNotEatenCupcake())
            {
                eatCupcake(guest);
                guest.setHasEatenCupcake(true);
                Guest.incrementCupcakeCounter();
            } else if (!isCupcakePresent)
            {
                Guest.incrementCupcakeCounter();
                requestCupcake();
            }
        } else
        {
            if (isCupcakePresent && guest.hasNotEatenCupcake())
            {
                eatCupcake(guest);
                guest.setHasEatenCupcake(true);
            }
        }
    }
}

class Guest extends Thread
{
    private static final Labyrinth labyrinth = new Labyrinth();
    private static int NUM_GUESTS;
    private static int guestInLabyrinth;
    private static int cupcakeCounter; // only one designated person will count the cupcake, explained in readme
    private final int guestNumber;
    private boolean hasEatenCupcake;

    public Guest(int guestNumber)
    {
        this.guestNumber = guestNumber;
    }

    public static void setNumGuests(int numGuests)
    {
        NUM_GUESTS = numGuests;
    }

    public static int getGuestInLabyrinth()
    {
        return guestInLabyrinth;
    }

    public static void setGuestInLabyrinth(int guestInLabyrinth)
    {
        Guest.guestInLabyrinth = guestInLabyrinth;
    }

    public static int getCupcakeCounter()
    {
        return cupcakeCounter;
    }

    public static void incrementCupcakeCounter()
    {
        cupcakeCounter++;
    }

    public int getGuestNumber()
    {
        return guestNumber;
    }

    public boolean hasNotEatenCupcake()
    {
        return !hasEatenCupcake;
    }

    public void setHasEatenCupcake(boolean hasEatenCupcake)
    {
        this.hasEatenCupcake = hasEatenCupcake;
    }

    public void run()
    {
        while (Guest.getCupcakeCounter() < NUM_GUESTS)
        {
            labyrinth.enterLabyrinth(this);
        }
    }
}