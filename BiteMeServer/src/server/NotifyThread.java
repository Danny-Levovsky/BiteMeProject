package server;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import entites.*;
import ocsf.server.ConnectionToClient;
//import JDBC.DbController;
import enums.Commands;



public class NotifyThread implements Runnable {

	private final int second = 1000;
	private final int minute = second * 60;
	
	private volatile boolean running = true; // Flag to control the execution of the thread

    // Method to stop the thread gracefully
    public void stopThread() {
        running = false;
    }


	@Override
	public void run() {	
	}

}

