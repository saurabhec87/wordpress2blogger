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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

public class WordpressClient {

  private String username = "";

  private String password = "";

  private String blogurl = "";

  public String getBlogurl() {
    return blogurl;
  }

  public void setBlogurl(String blogurl) {
    this.blogurl = blogurl;
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

  /**
   * get post by postid
   * 
   * @param postid
   * @return
   * @throws MalformedURLException
   * @throws XmlRpcException
   */
  public WordpressEntry getPostById(String postid)
      throws MalformedURLException, XmlRpcException {
    XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
    config.setServerURL(new URL(getBlogurl() + "/xmlrpc.php"));
    XmlRpcClient client = new XmlRpcClient();
    client.setConfig(config);
    Object[] params = new Object[] {postid, getUsername(), getPassword()};
    HashMap hm = null;
    try {
      hm = (HashMap) client.execute("metaWeblog.getPost", params);
    } catch (XmlRpcException e) {
      if (e.getMessage().indexOf("no such post") < 0) {
        throw e;
      } else {
        return null;
      }
    }
    WordpressEntry entry = new WordpressEntry();
    entry.setCategories((Object[]) hm.get("categories"));
    entry.setDateCreated((Date) hm.get("dateCreated"));
    entry.setDescription((String) hm.get("description"));
    entry.setPostid((String) hm.get("postid"));
    entry.setTitle((String) hm.get("title"));
    entry.setUserid((String) hm.get("userid"));
    return entry;
  }

  /**
   * get recent posts
   * 
   * @param num set how many recent posts do you want to get
   * @param draft true when get both draft and published posts; false when get
   *          only published posts
   * @return
   * @throws MalformedURLException
   * @throws XmlRpcException
   */
  public List<WordpressEntry> getRecentPosts(int num, boolean draft)
      throws MalformedURLException, XmlRpcException {
    List<WordpressEntry> ret = new ArrayList<WordpressEntry>();
    XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
    config.setServerURL(new URL(getBlogurl() + "/xmlrpc.php"));
    XmlRpcClient client = new XmlRpcClient();
    client.setConfig(config);
    Object[] params = new Object[] {"1", getUsername(), getPassword(), num};
    Object[] result = (Object[]) client.execute("metaWeblog.getRecentPosts",
        params);
    for (int i = 0; i < result.length; i++) {
      WordpressEntry entry = new WordpressEntry();
      HashMap hm = (HashMap) result[i];
      entry.setCategories((Object[]) hm.get("categories"));
      entry.setDateCreated((Date) hm.get("dateCreated"));
      entry.setDescription((String) hm.get("description"));
      entry.setPostid((String) hm.get("postid"));
      entry.setTitle((String) hm.get("title"));
      entry.setUserid((String) hm.get("userid"));
      if (draft || !isDraft(entry)) {
        ret.add(entry);
      }
    }
    return ret;
  }

  public List<WordpressEntry> getRecentPosts(int num)
      throws MalformedURLException, XmlRpcException {
    return getRecentPosts(num, true);
  }

  /**
   * whether the post is a draft post
   * 
   * @param we
   * @return true when we is a draft post
   */
  private boolean isDraft(WordpressEntry we) {
    return we.getDateCreated().getTime() == 943891200000l;
  }
}
