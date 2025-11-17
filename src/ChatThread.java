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
    }
}
