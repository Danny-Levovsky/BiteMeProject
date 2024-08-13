package server;


/**
 * The NotifyThread class represents a thread that can be controlled 
 * to run or stop based on a flag. This thread is designed to perform 
 * periodic tasks but currently has no task implemented in the run method.
 * 
 * @author yosra
 */
public class NotifyThread implements Runnable {
	
	/**
     * Constant representing one second in milliseconds.
     */
	private final int second = 1000;
	
	/**
     * Constant representing one minute in milliseconds.
     */
	private final int minute = second * 60;
	
	/**
     * A volatile boolean flag used to control the execution of the thread.
     * The thread will continue running while this flag is true.
     */
	private volatile boolean running = true; 

	/**
     * Method to stop the thread gracefully by setting the running flag to false.
     */
    public void stopThread() {
        running = false;
    }

    /**
     * The run method of the thread. This method is invoked when the thread is started.
     * Currently, it does not perform any actions.
     */
	@Override
	public void run() {	
	}

}

