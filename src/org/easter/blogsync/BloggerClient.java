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

import com.google.gdata.client.GoogleService;
import com.google.gdata.data.Category;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.Entry;
import com.google.gdata.data.Feed;
import com.google.gdata.data.Person;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class BloggerClient {
  private String username = "";

  private String password = "";

  private String blogid = "";

  private GoogleService myService = null;

  private GoogleService getGoogleService() throws AuthenticationException {
    if (myService == null) {
      myService = new GoogleService("blogger", "easter.blogsync");
      myService.setUserCredentials(getUsername(), getPassword());
    }
    return myService;
  }

  public List<Entry> geteRecentPosts(int num) throws AuthenticationException,
      IOException, ServiceException {
    URL feedUrl = new URL("http://www.blogger.com/feeds/" + getBlogid()
        + "/posts/full?max-results=" + num);
    Feed myFeed = getGoogleService().getFeed(feedUrl, Feed.class);
    return myFeed.getEntries();
  }

  public Entry newPost(Entry entry) throws AuthenticationException,
      IOException, ServiceException {
    URL postUrl = new URL("http://www.blogger.com/feeds/" + getBlogid()
        + "/posts/default");
    return getGoogleService().insert(postUrl, entry);
  }

  public Entry newWordpressPost(WordpressEntry we)
      throws AuthenticationException, IOException, ServiceException {
    Entry myEntry = new Entry();
    myEntry.setTitle(new PlainTextConstruct(we.getTitle()));
    myEntry.setContent(new PlainTextConstruct(we.getDescription()));
    Person author = new Person(getUsername(), null, getUsername());
    myEntry.getAuthors().add(author);
    Object[] categories = we.getCategories();
    for (int i = 0; i < categories.length; i++) {
      Category category = new Category();
      category.setTerm((String) categories[i]);
      category.setScheme("http://www.blogger.com/atom/ns#");
      myEntry.getCategories().add(category);
    }
    DateTime dt = new DateTime(we.getDateCreated());
    dt.setTzShift(8);
    myEntry.setPublished(dt);
    Entry entry = newPost(myEntry);
    int tryCounter = 3;
    while (tryCounter > 0 && null == entry) {
      entry = newPost(myEntry);
      tryCounter--;
    }
    return entry;
  }

  public String getBlogid() {
    return blogid;
  }

  public void setBlogid(String blogid) {
    this.blogid = blogid;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }
}
