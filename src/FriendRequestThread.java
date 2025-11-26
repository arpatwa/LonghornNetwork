import java.util.concurrent.Semaphore;

/**
 * Runnable task that simulates sending friend requests from one {@link UniversityStudent}
 * to another.
 * Many instances of this class can be run on different threads.
 */
public class FriendRequestThread implements Runnable {
    /**The student sending the friends request*/
    private UniversityStudent sender;
    /** Student who the friend request is sent to*/
    private UniversityStudent receiver;
    /** Semaphore to track thread safe friend requesting*/
    private static final Semaphore sem = new Semaphore(1);
    /**
     * Constructor for a new friend request thread
     * @param sender is the student who sends the friend request
     * @param receiver is the student who gets the friend request
     */
    public FriendRequestThread(UniversityStudent sender, UniversityStudent receiver) {
        // Constructor
        this.sender = sender;
        this.receiver = receiver;
    }

    /**
     * Executes friend requests.
     * Will update friends/network state for students.
     */
    @Override
    public void run() {
        // Method signature only
        // Null sender/receiver
        if (sender == null || receiver == null) {
            System.out.println("Friend request thread has been terminated due to null sender or receiver.");
            return;
        }
        // Trying to send friend requests to self
        if (sender == receiver) {
            System.out.println("Friend request thread has been terminated due to trying to request self.");
            return;
        }



        try {
            sem.acquire();
            // Friend request processing
            System.out.println(sender.getName() + " has sent a friend request to " + receiver.getName());
            if (sender.getFriends().contains(receiver)) {
                System.out.println(sender.getName() + " is already friends with " + receiver.getName());
            } else {
                // Bidirectional friendship
                sender.addFriend(receiver);
                receiver.addFriend(sender);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Interrupt restoration
            e.printStackTrace();
            System.out.println("Friend request thread has been interrupted.");
        } finally {
            sem.release();
        }
    }
}
