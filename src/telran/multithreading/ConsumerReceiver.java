package telran.multithreading;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

public class ConsumerReceiver extends Thread {
    private BlockingQueue<String> messageBox;
    private static AtomicLong messagesCounter = new AtomicLong();
	public void setMessageBox(BlockingQueue<String> messageBox) {
		this.messageBox = messageBox;
	}
    public void run() {
    	boolean running = true;
    	while(running) {
    		try {
				String message = messageBox.take();
				processMessage(message);
			} catch (InterruptedException e) {
				running = false;
				while(!messageBox.isEmpty()) {
					processMessage(messageBox.remove());
				}
			}
    	}
    }
	private void processMessage(String message) {
		System.out.printf("Thread %s - %s\n", getName(), message);
		messagesCounter.getAndIncrement();
		
	}
	public static long getMessagesCounter() {
		return messagesCounter.get();
	}
}