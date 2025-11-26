import java.util.concurrent.Semaphore;

/**
 * Runnable task that simulates sending chat messages between
 * two {@link UniversityStudent}s.
 */
public class ChatThread implements Runnable {
    /** The student who sends a message*/
    private UniversityStudent sender;
    /** Student who sends a message*/
    private UniversityStudent receiver;
    /** Message to send*/
    private String message;
    /**Semaphore for concurrent thread safety */
    private static final Semaphore sem =  new Semaphore(1);

    /**
     * This is the constructor for a ChatThread object
     * @param sender is the student who sends a message
     * @param receiver is the student who receives the message sent
     * @param message is the message to be sent
     */
    public ChatThread(UniversityStudent sender, UniversityStudent receiver, String message) {
        // Constructor
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
    }

    /**
     * Executes the chatting task.
     * Appends the message to the chat history between the sender and receiver.
     */
    @Override
    public void run() {
        // Method signature only
        // Null sender/receiver
        if (sender == null || receiver == null) {
            System.out.println("Chat thread has been terminated due to null sender or receiver.");
            return;
        }
        // Trying to send messages to self
        if (sender == receiver) {
            System.out.println("Chat thread has been terminated due to trying to message self.");
            return;
        }

        try{
            sem.acquire();
            // Friend request processing
            System.out.println(sender.getName() + " has sent a chat to " + receiver.getName());
            receiver.addChat(sender, message); // Show chat in chat history
            sender.addChat(receiver, message);
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
            System.out.println("Chat thread has been interrupted.");
        } finally{
            sem.release();
        }

    }
}
