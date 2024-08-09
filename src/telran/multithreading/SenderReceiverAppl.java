package telran.multithreading;

import java.util.Arrays;
import java.util.concurrent.*;
import java.util.stream.IntStream;

public class SenderReceiverAppl {
    private static final int N_MESSAGES = 2000;
    private static final int N_RECEIVERS = 10;

    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<String> evenMessageBox = new LinkedBlockingQueue<>();
        BlockingQueue<String> oddMessageBox = new LinkedBlockingQueue<>();
        
        ProducerSender sender = startSender(evenMessageBox, oddMessageBox, N_MESSAGES);
        
        ConsumerReceiver[] evenReceivers = startReceivers(evenMessageBox, N_RECEIVERS);
        ConsumerReceiver[] oddReceivers = startReceivers(oddMessageBox, N_RECEIVERS);
        
        sender.join();
        
        stopReceivers(evenReceivers);
        stopReceivers(oddReceivers);
        
        displayResult(evenReceivers, oddReceivers);
    }

    private static void displayResult(ConsumerReceiver[] evenReceivers, ConsumerReceiver[] oddReceivers) {
        long evenProcessedCount = Arrays.stream(evenReceivers)
                                        .mapToLong(r -> ConsumerReceiver.getMessagesCounter())
                                        .sum();
        long oddProcessedCount = Arrays.stream(oddReceivers)
                                       .mapToLong(r -> ConsumerReceiver.getMessagesCounter())
                                       .sum();
        
        System.out.printf("Counter of processed even messages is %d\n", evenProcessedCount);
        System.out.printf("Counter of processed odd messages is %d\n", oddProcessedCount);
    }

    private static void stopReceivers(ConsumerReceiver[] receivers) throws InterruptedException {
        for (ConsumerReceiver receiver : receivers) {
            receiver.interrupt();
            receiver.join();
        }
    }

    private static ConsumerReceiver[] startReceivers(BlockingQueue<String> messageBox, int nReceivers) {
        ConsumerReceiver[] receivers = IntStream.range(0, nReceivers)
                                                 .mapToObj(i -> {
                                                     ConsumerReceiver receiver = new ConsumerReceiver();
                                                     receiver.setMessageBox(messageBox);
                                                     return receiver;
                                                 })
                                                 .toArray(ConsumerReceiver[]::new);
        Arrays.stream(receivers).forEach(ConsumerReceiver::start);
        return receivers;
    }

    private static ProducerSender startSender(BlockingQueue<String> evenMessageBox, BlockingQueue<String> oddMessageBox, int nMessages) {
        ProducerSender sender = new ProducerSender(evenMessageBox, oddMessageBox, nMessages);
        sender.start();
        return sender;
    }
}
