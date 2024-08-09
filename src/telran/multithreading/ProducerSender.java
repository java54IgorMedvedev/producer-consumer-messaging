package telran.multithreading;

import java.util.concurrent.BlockingQueue;
import java.util.stream.IntStream;

public class ProducerSender extends Thread {
    private BlockingQueue<String> evenMessageBox; 
    private BlockingQueue<String> oddMessageBox;  
    private int nMessages;

    public ProducerSender(BlockingQueue<String> evenMessageBox, BlockingQueue<String> oddMessageBox, int nMessages) {
        this.evenMessageBox = evenMessageBox;
        this.oddMessageBox = oddMessageBox;
        this.nMessages = nMessages;
    }

    public void run() {
        IntStream.rangeClosed(1, nMessages)
                 .mapToObj(i -> "message" + i)
                 .forEach(m -> {
                     try {
                         int messageIndex = Integer.parseInt(m.substring(7));
                         if (messageIndex % 2 == 0) {
                             evenMessageBox.put(m); 
                         } else {
                             oddMessageBox.put(m); 
                         }
                     } catch (InterruptedException e) {
                         
                         return;
                     }
                 });
    }
}
