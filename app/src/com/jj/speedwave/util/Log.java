package com.jj.speedwave.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Custom log implementation that supports global en- and disabling,
 * based on log levels.
 * 
 * It leverages the <code>android.util.Log</code> levels debug,
 * error, info, verbose and warning. The Logging tag is created
 * the caller's class name, method name and line number.
 * 
 * @author Michael Jess
 *
 */
public class Log {
	
	/**
	 * Variable used to control the global log level. For instance, if the
	 * global log level is set to <code>LogLevel.ERROR</code>, all
	 * <code>Log</code> instances would log messages with priority of
	 * <code>LogLevel.ERROR</code> or higher.
	 * 
	 *  See <b>iShouldNotLog()</b> for the interplay between global and local
	 *  log level.
	 */
	private static LogLevel globalLogLevel = LogLevel.NONE;
	
	
	/**
	 * Variable used to control the local log level. For instance, if the
	 * local log level is set to <code>LogLevel.ERROR</code>, all
	 * <code>Log</code> instances would log messages with priority of
	 * <code>LogLevel.ERROR</code> or higher.
	 * 
	 *  See <b>iShouldNotLog()</b> for the interplay between global and local
	 *  log level.
	 */
	private LogLevel logLevel = LogLevel.NONE;
	
	/**
	 * Regular expression used to extract the caller's class name from the
	 * stack trace.
	 */
	private static final Pattern CLASS_PATTERN = Pattern.compile("[a-zA-Z0-9]+$");
	
	
	/**
	 * Send a debug log message.
	 * 
	 * @param message The message to log
	 */
	public void d(Object message) {
		if(iShouldNotLog(LogLevel.DEBUG)) return;
		android.util.Log.d(this.getTag(), this.getMessage(message));
	}
	
	/**
	 * Send a debug log message and its cause
	 * 
	 * @param message The message to log
	 * @param cause The cause of the error
	 */
	public void d(Object message, Throwable cause) {
		if(iShouldNotLog(LogLevel.DEBUG)) return;
		android.util.Log.d(this.getTag(), this.getMessage(message), cause);
	}
	
	/**
	 * Send an error log message.
	 * 
	 * @param message The message to log
	 */
	public void e(Object message) {
		if(iShouldNotLog(LogLevel.ERROR)) return;
		android.util.Log.e(this.getTag(), this.getMessage(message));
	}
	
	/**
	 * Send an error log message and its cause
	 * 
	 * @param message The message to log
	 * @param cause The cause of the error
	 */
	public void e(Object message, Throwable cause) {
		if(iShouldNotLog(LogLevel.ERROR)) return;
		android.util.Log.e(this.getTag(), this.getMessage(message), cause);
	}
	
	/**
	 * Send an information log message.
	 * 
	 * @param message The message to log
	 */
	public void i(Object message) {
		if(iShouldNotLog(LogLevel.INFO)) return;
		android.util.Log.i(this.getTag(), this.getMessage(message));
	}
	
	/**
	 * Send an information log message and its cause
	 * 
	 * @param message The message to log
	 * @param cause The cause of the error
	 */
	public void i(Object message, Throwable cause) {
		if(iShouldNotLog(LogLevel.INFO)) return;
		android.util.Log.i(this.getTag(), this.getMessage(message), cause);
	}
	
	/**
	 * Send a verbose log message.
	 * 
	 * @param message The message to log
	 */
	public void v(Object message) {
		if(iShouldNotLog(LogLevel.VERBOSE)) return;
		android.util.Log.v(getTag(), this.getMessage(message));
	}
	
	/**
	 * Send a verbose log message and its cause
	 * 
	 * @param message The message to log
	 * @param cause The cause of the error
	 */
	public void v(Object message, Throwable cause) {
		if(iShouldNotLog(LogLevel.VERBOSE)) return;
		android.util.Log.v(this.getTag(), this.getMessage(message), cause);
	}
	
	/**
	 * Send a warning log message.
	 * 
	 * @param message The message to log
	 */
	public void w(Object message) {
		if(iShouldNotLog(LogLevel.WARN)) return;
		android.util.Log.w(getTag(), this.getMessage(message));
	}
	
	/**
	 * Send a warning log message and its cause
	 * 
	 * @param message The message to log
	 * @param cause The cause of the error
	 */
	public void w(Object message, Throwable cause) {
		if(iShouldNotLog(LogLevel.WARN)) return;
		android.util.Log.w(this.getTag(), this.getMessage(message), cause);
	}
	
	/**
	 * Create the log tag for the current message. It consists of
	 * the caller's class and method name and line number.
	 * 
	 * @return The log tag
	 */
	private String getTag() {
		StackTraceElement callerStackTraceElement =
				this.getCallerStackTraceElement();
		
		String callerInfo = this.getCallerInfo(callerStackTraceElement);
		
		return callerInfo;
	}
	
	private String getMessage(Object message) {
		return message == null ? "(empty log message)" : message.toString();
	}
	
	/**
	 * Retrieves the current stack trace and extracts the caller's stack trace
	 * element from it.
	 * 
	 * @return The caller stack trace element
	 */
	private StackTraceElement getCallerStackTraceElement() {
		StackTraceElement[] stackTraceElements = Thread.currentThread()
													   .getStackTrace();
		
		for(StackTraceElement stackTraceElement : stackTraceElements) {
			if(isNeitherLogNorSystem(stackTraceElement.getClassName())) {
				return stackTraceElement;
			}
		}
		
		// Pretty much impossible
		return null;
	}
	
	/**
	 * 
	 * Checks if the given fully qualified <code>className</code> is equal to
	 * the fully qualified <code>Thread</code>, <code>Log</code> or <code>VMStack</code>
	 * class name and returns true if this is not the case.
	 * 
	 * @param className The class name to check
	 * @return true, if it is neither Log nor Thread (fully qualified)
	 */
	private boolean isNeitherLogNorSystem(String className) {
		return !className.equals(Log.class.getName()) &&
				!className.equals(Thread.class.getName()) &&
				!className.equals("dalvik.system.VMStack");
	}
	
	/**
	 * Takes a <code>StackTraceElement</code> and extracts the caller class and
	 * method name and line number from it, and returns that information as a
	 * String.
	 * 
	 * @param callerStackTraceElement The stack trace element to inspect
	 * @return A string like this: ClassName.MethodName:LineNumber
	 */
	private String getCallerInfo(StackTraceElement callerStackTraceElement) {
		Matcher callerNameMatcher = CLASS_PATTERN.matcher(
				callerStackTraceElement.getClassName());
		
		callerNameMatcher.find();
		String className = callerNameMatcher.group(0);
		
		String methodName = callerStackTraceElement.getMethodName();
		int lineNumber = callerStackTraceElement.getLineNumber();
		
		return className + "." + methodName + ":" + lineNumber;
	}
	
	/**
	 * Sets the global log level. All existing <code>Log</code> instances will
	 * log messages with priority at least as high as the given log level.
	 * For instance, setting LogLevel.WARN would also enable LogLevel.ERROR. 
	 * 
	 * @param logLevel The minimum log level to set
	 */
	public static void setGlobalLogLevel(LogLevel logLevel) {
		if(logLevel != null) {
			Log.globalLogLevel = logLevel;
		}
	}
	
	/**
	 * Sets the log level of this instance. Only this instance will
	 * log messages with priority at least as high as the given log level.
	 * For instance, setting LogLevel.WARN would also enable LogLevel.ERROR.
	 * Other loggers are not affected by this.
	 * 
	 * @param logLevel The minimum log level to set
	 */
	public void setLogLevel(LogLevel logLevel) {
		if(logLevel != null) {
			this.logLevel = logLevel;
		}
	}
	
	/**
	 * Checks if the logger should log, given the currently set
	 * <code>LogLevel</code> and the <code>logLevel</code> parameter.
	 * 
	 * @param logLevel The log level to compare to
	 * @return true, if log level parameter is not lower than global/local log level
	 */
	private boolean iShouldNotLog(LogLevel logLevel) {
		return this.logLevel.getValue() > logLevel.getValue() &&
				Log.globalLogLevel.getValue() > logLevel.getValue();
	}
	
}
