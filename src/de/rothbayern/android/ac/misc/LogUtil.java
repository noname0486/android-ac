/*
 *  "Analog Compass" is an application for devices based on android os. 
 *  The application shows the orientation based on the intern magnetic sensor.   
 *  Copyright (C) 2009-2010  Dieter Roth
 *
 *  This program is free software; you can redistribute it and/or modify it under the terms of the 
 *  GNU General Public License as published by the Free Software Foundation; either version 3 of 
 *  the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 *  without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 *  See the GNU General Public License for more details.

 *  You should have received a copy of the GNU General Public License along with this program; 
 *  if not, see <http://www.gnu.org/licenses/>.
 */
package de.rothbayern.android.ac.misc;

import android.util.Log;

/**
 * @author Dieter Roth
 *
 * Wrapper for logging
 */
public class LogUtil {

	 public static void v(String tag, String msg) {
		 if(Log.isLoggable(tag, Log.VERBOSE)){
			 Log.v(tag, msg);
		 }
	 }

	 public static void d(String tag, String msg) {
		 if(Log.isLoggable(tag, Log.DEBUG)){
			 Log.d(tag, msg);
		 }
	 }
	
	 public static void i(String tag, String msg) {
		 if(Log.isLoggable(tag, Log.INFO)){
			 Log.i(tag, msg);
		 }

	 }


     public static void w(String tag, String msg) {
		 if(Log.isLoggable(tag, Log.WARN)){
			 Log.w(tag, msg);
		 }
	 }

     public static void w(String tag, String msg, Throwable ex) {
		 if(Log.isLoggable(tag, Log.WARN)){
			 Log.w(tag, msg, ex);
		 }
	 }

	 public static void e(String tag, String msg) {
		 if(Log.isLoggable(tag, Log.ERROR)){
			 Log.e(tag, msg);
		 }
	 }

	 public static void e(String tag, String msg,Throwable ex) {
		 if(Log.isLoggable(tag, Log.ERROR)){
			 Log.e(tag, msg, ex);
		 }
	 }

}
