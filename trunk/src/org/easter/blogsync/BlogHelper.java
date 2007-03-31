/* Copyright (c) 2007 Easter
 * Author Yichao Zhang
 * Email Yichao.Zhang@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.easter.blogsync;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BlogHelper {
  public final static String settingFile = "settings.properties";
  public final static String errorLog = "error.log";

  public final static String wpUsername = "wp.username";
  public final static String wpPassword = "wp.password";
  public final static String wpUrl = "wp.url";
  public final static String bgUsername = "bg.username";
  public final static String bgPassword = "bg.password";
  public final static String bgBlogid = "bg.blogid";

  public final static String isRecentOption = "isRecentOption";
  public final static String isPostidOption = "isPostidOption";
  public final static String isPostidInOption = "isPostidInOption";

  public final static String optionRecentFrom = "optionRecentFrom";
  public final static String optionRecentTo = "optionRecentTo";
  public final static String optionPostidFrom = "optionPostidFrom";
  public final static String optionPostidTo = "optionPostidTo";
  public final static String optionPostidIn = "optionPostidIn";

  private static PrintWriter logpw = null;

  public static void logError(String log) {
    if (null == logpw) {
      String filePath = BlogHelper.errorLog;
      File f = new File(filePath);
      try {
        logpw = new PrintWriter(new FileOutputStream(f));
      } catch (FileNotFoundException e1) {
      }
    }
    long current = System.currentTimeMillis();
    Date date = new Date(current);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss:SSS");
    logpw.println(sdf.format(date) + "\t" + log);
    logpw.flush();
  }

  public static void logError(Exception e) {
    if (null == logpw) {
      String filePath = BlogHelper.errorLog;
      File f = new File(filePath);
      try {
        logpw = new PrintWriter(new FileOutputStream(f));
      } catch (FileNotFoundException e1) {
      }
    }
    long current = System.currentTimeMillis();
    Date date = new Date(current);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss:SSS");
    logpw.println(sdf.format(date));
    e.printStackTrace(logpw);
    logpw.flush();
  }
}
