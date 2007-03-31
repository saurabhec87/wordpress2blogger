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

import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

import org.apache.xmlrpc.XmlRpcException;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class SyncFrame extends JFrame {

  private static final long serialVersionUID = 1L;
  private JPanel jContentPane = null;
  private JPanel settingPanel = null;
  private JPanel syncPanel = null;
  private JButton accountButton = null;
  private JPanel optionPanel = null;
  private JRadioButton recentRadioButton = null;
  private JLabel recentFromLabel = null;
  private JTextField recentFromTextField = null;
  private JLabel recentToLabel = null;
  private JTextField recentToTextField = null;
  private JRadioButton postidRadioButton1 = null;
  private JLabel postidFromLabel1 = null;
  private JTextField postidFromTextField1 = null;
  private JLabel postidToLabel1 = null;
  private JTextField postidToTextField1 = null;
  private JRadioButton postinRadioButton = null;
  private JTextArea postinTextArea = null;
  private JButton readButton = null;
  private JPanel postsPanel = null;
  private JTable postsTable = null;
  private JPanel logPanel = null;
  private JButton importButton = null;
  private JButton saveOptionButton = null;
  private JScrollPane postsScrollPane = null;

  Properties p = null; // @jve:decl-index=0:
  Object[] columnIdentifiers = new String[] {"postid", "title", "date"};
  private JTextArea logTextArea = null;
  private JScrollPane logScrollPane = null;

  public SyncFrame() {
    super();
    initialize();
    loadData();
  }

  private void loadData() {
    p = new Properties();
    try {
      FileInputStream fis = new FileInputStream(BlogHelper.settingFile);
      p.load(fis);
      if ("true".equalsIgnoreCase(p.getProperty(BlogHelper.isRecentOption))) {
        getRecentRadioButton().setSelected(true);
      } else if ("true".equalsIgnoreCase(p.getProperty(BlogHelper.isPostidOption))) {
        getPostidRadioButton1().setSelected(true);
      } else if ("true".equalsIgnoreCase(p.getProperty(BlogHelper.isPostidInOption))) {
        getPostinRadioButton().setSelected(true);
      }
      getRecentFromTextField().setText(
          p.getProperty(BlogHelper.optionRecentFrom));
      getRecentToTextField().setText(p.getProperty(BlogHelper.optionRecentTo));
      getPostidFromTextField1().setText(
          p.getProperty(BlogHelper.optionPostidFrom));
      getPostidToTextField1().setText(p.getProperty(BlogHelper.optionPostidTo));
      getPostinTextArea().setText(p.getProperty(BlogHelper.optionPostidIn));
    } catch (FileNotFoundException e) {
      JOptionPane.showMessageDialog(this, "Can not find the setting file \""
          + BlogHelper.settingFile + "\"\nA new one will be created!");
      BlogHelper.logError(e);
    } catch (IOException e) {
      JOptionPane.showMessageDialog(this, "When reading the setting file \""
          + BlogHelper.settingFile + "\", error occurs:\n" + e.getMessage());
      BlogHelper.logError(e);
    }
  }

  private void saveData() {
    if (!checkData()) {
      return;
    }
    try {
      p.setProperty(BlogHelper.isRecentOption,
          getRecentRadioButton().isSelected() + "");
      p.setProperty(BlogHelper.optionRecentFrom,
          getRecentFromTextField().getText());
      p.setProperty(BlogHelper.optionRecentTo, getRecentToTextField().getText());
      p.setProperty(BlogHelper.isPostidOption,
          getPostidRadioButton1().isSelected() + "");
      p.setProperty(BlogHelper.optionPostidFrom,
          getPostidFromTextField1().getText());
      p.setProperty(BlogHelper.optionPostidTo, getPostidToTextField1().getText());
      p.setProperty(BlogHelper.isPostidInOption,
          getPostinRadioButton().isSelected() + "");
      p.setProperty(BlogHelper.optionPostidIn, getPostinTextArea().getText());
      FileOutputStream fos = new FileOutputStream(BlogHelper.settingFile);
      p.store(fos, "settings");
      JOptionPane.showMessageDialog(this, "save successfully!");
    } catch (FileNotFoundException e) {
      JOptionPane.showMessageDialog(this, "Can not create the setting file \""
          + BlogHelper.settingFile + "\"\nplease check if the file is in use!");
      BlogHelper.logError(e);
    } catch (IOException e) {
      JOptionPane.showMessageDialog(this, "When saving the setting file \""
          + BlogHelper.settingFile + "\", error occurs:\n" + e.getMessage()
          + "\nplease check if the file is in use!");
      BlogHelper.logError(e);
    }
  }

  private boolean checkData() {
    if (getRecentRadioButton().isSelected()) {
      if (isNumber(getRecentFromTextField().getText())
          && isNumber(getRecentToTextField().getText())) {
        if (Integer.parseInt(getRecentFromTextField().getText()) < Integer.parseInt(getRecentToTextField().getText())) {
          return true;
        } else {
          JOptionPane.showMessageDialog(this, "'from' must less than 'to'");
          return false;
        }
      } else {
        JOptionPane.showMessageDialog(this,
            "please input positive integer or 0");
        return false;
      }
    } else if (getPostidRadioButton1().isSelected()) {
      if (isNumber(getPostidFromTextField1().getText())
          && isNumber(getPostidToTextField1().getText())) {
        if (Integer.parseInt(getPostidFromTextField1().getText()) < Integer.parseInt(getPostidToTextField1().getText())) {
          return true;
        } else {
          JOptionPane.showMessageDialog(this, "'from' must less than 'to'");
          return false;
        }
      } else {
        JOptionPane.showMessageDialog(this,
            "please input positive integer or 0");
        return false;
      }
    } else if (getPostinRadioButton().isSelected()) {
      if (isNumberList(getPostinTextArea().getText())) {
        return true;
      } else {
        JOptionPane.showMessageDialog(this,
            "please input wordpress postid list splited by \",\"");
        return false;
      }
    }
    return false;
  }

  private boolean isNumber(String text) {
    int i = -1;
    try {
      i = Integer.parseInt(text);
    } catch (Exception e) {
      return false;
    }
    if (i < 0)
      return false;
    return true;
  }

  private boolean isNumberList(String text) {
    String[] list = text.split(",");
    for (int i = 0; i < list.length; i++) {
      if (!isNumber(list[i])) {
        return false;
      }
    }
    return true;
  }

  protected void readPosts() {
    List<WordpressEntry> posts;
    posts = readPostsByOption();
    if (posts == null || posts.size() == 0) {
      JOptionPane.showMessageDialog(this, "No valid post");
      return;
    }
    DefaultTableModel model = new DefaultTableModel();
    Object[][] data = new Object[posts.size()][3];
    SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd HH:mm:ss");
    for (int i = 0; i < posts.size(); i++) {
      WordpressEntry we = posts.get(i);
      data[i][0] = we.getPostid();
      data[i][1] = we.getTitle();
      data[i][2] = sdf.format(we.getDateCreated());
    }
    model.setDataVector(data, columnIdentifiers);
    getPostsTable().setModel(model);
  }

  private List<WordpressEntry> readPostsByOption() {
    WordpressClient wc = new WordpressClient();
    wc.setBlogurl(p.getProperty(BlogHelper.wpUrl));
    wc.setUsername(p.getProperty(BlogHelper.wpUsername));
    wc.setPassword(p.getProperty(BlogHelper.wpPassword));
    List<WordpressEntry> posts = new ArrayList<WordpressEntry>();
    try {
      if (getRecentRadioButton().isSelected()) {
        int from = Integer.parseInt(getRecentFromTextField().getText());
        int to = Integer.parseInt(getRecentToTextField().getText());
        int num = to - from;
        posts = wc.getRecentPosts(to);
        for (int i = 0; i < from; i++) {
          posts.remove(0);
        }
        log(num + " post(s) loaded.");
      } else if (getPostidRadioButton1().isSelected()) {
        int from = Integer.parseInt(getPostidFromTextField1().getText());
        int to = Integer.parseInt(getPostidToTextField1().getText());
        log("start to read posts, postid from " + from + " to " + to);
        for (int i = from; i < to; i++) {
          WordpressEntry e = wc.getPostById(i + "");
          if (e != null) {
            posts.add(e);
            log("1 post loaded: postid=" + e.getPostid() + ";title="
                + e.getTitle());
          } else {
            log("no such post whose postid = " + i);
          }
        }
        log("read successfully.");
      } else if (getPostinRadioButton().isSelected()) {
        String text = getPostinTextArea().getText();
        String[] listStr = text.split(",");
        log("start to read posts, postid in " + text);
        for (int i = 0; i < listStr.length; i++) {
          WordpressEntry e = wc.getPostById(listStr[i]);
          if (e != null) {
            posts.add(e);
            log("1 post loaded: postid=" + e.getPostid() + ";title="
                + e.getTitle());
          } else {
            log("no such post whose postid = " + listStr[i]);
          }
        }
        log("read successfully.");
      }
      DefaultTableModel model = new DefaultTableModel();
      Object[][] data = new Object[posts.size()][3];
      SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd HH:mm:ss");
      for (int i = 0; i < posts.size(); i++) {
        WordpressEntry we = posts.get(i);
        data[i][0] = we.getPostid();
        data[i][1] = we.getTitle();
        data[i][2] = sdf.format(we.getDateCreated());
      }
      model.setDataVector(data, columnIdentifiers);
      getPostsTable().setModel(model);
    } catch (MalformedURLException e) {
      JOptionPane.showMessageDialog(this, "The wordpress url is invalid!\n"
          + e.getMessage());
      BlogHelper.logError(e);
    } catch (XmlRpcException e) {
      JOptionPane.showMessageDialog(this,
          "when reading posts from wordpress, error occurs:\n" + e.getMessage());
      BlogHelper.logError(e);
    }
    return posts;
  }

  protected void importPosts() {
    TableModel model = getPostsTable().getModel();
    BloggerClient bg = new BloggerClient();
    bg.setBlogid(p.getProperty(BlogHelper.bgBlogid));
    bg.setUsername(p.getProperty(BlogHelper.bgUsername));
    bg.setPassword(p.getProperty(BlogHelper.bgPassword));

    WordpressClient wc = new WordpressClient();
    wc.setBlogurl(p.getProperty(BlogHelper.wpUrl));
    wc.setUsername(p.getProperty(BlogHelper.wpUsername));
    wc.setPassword(p.getProperty(BlogHelper.wpPassword));
    List<String> notaddList = new ArrayList<String>();
    log("start to import posts:");
    for (int i = 0; i < model.getRowCount(); i++) {
      String postid = (String) model.getValueAt(i, 0);
      try {
        WordpressEntry entry = wc.getPostById(postid);
        log("start postid=" + entry.getPostid() + ";title=" + entry.getTitle());
        if (null == bg.newWordpressPost(entry)) {
          log("post unsuccessfully");
          notaddList.add(postid);
        } else {
          log("post successfully");
        }
      } catch (MalformedURLException e) {
        JOptionPane.showMessageDialog(this, "The wordpress url is invalid!\n"
            + e.getMessage());
        BlogHelper.logError(e);
      } catch (XmlRpcException e) {
        JOptionPane.showMessageDialog(this,
            "when reading posts from wordpress, error occurs:\n"
                + e.getMessage());
        BlogHelper.logError(e);
      } catch (AuthenticationException e) {
        JOptionPane.showMessageDialog(this,
            "can not log in with blogger account:\n" + e.getMessage());
        BlogHelper.logError(e);
      } catch (IOException e) {
        JOptionPane.showMessageDialog(this,
            "when publishing post to blogger, error occurs:\n" + e.getMessage());
        BlogHelper.logError(e);
      } catch (ServiceException e) {
        JOptionPane.showMessageDialog(this,
            "when publishing post to blogger, error occurs:\n" + e.getMessage());
        BlogHelper.logError(e);
      }
    }
    if (notaddList.size() > 0) {
      log("these posts not added:");
      log(notaddList.toString());
    }
  }

  private void log(String string) {
    getLogTextArea().setText(getLogTextArea().getText() + "\n" + string);
  }

  /**
   * This method initializes this
   * 
   * @return void
   */
  private void initialize() {
    this.setResizable(false);
    this.setLocation(new Point(150, 150));
    this.setSize(new Dimension(800, 600));
    this.setMinimumSize(new Dimension(800, 600));
    this.setPreferredSize(new Dimension(800, 600));
    this.setContentPane(getJContentPane());
    this.setTitle("BlogSync");
    this.setVisible(true);
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
  }

  /**
   * This method initializes jContentPane
   * 
   * @return javax.swing.JPanel
   */
  private JPanel getJContentPane() {
    if (jContentPane == null) {
      jContentPane = new JPanel();
      jContentPane.setLayout(null);
      jContentPane.add(getSettingPanel(), null);
      jContentPane.add(getSyncPanel(), null);
    }
    return jContentPane;
  }

  /**
   * This method initializes settingPanel
   * 
   * @return javax.swing.JPanel
   */
  private JPanel getSettingPanel() {
    if (settingPanel == null) {
      settingPanel = new JPanel();
      settingPanel.setLayout(null);
      settingPanel.setBounds(new Rectangle(7, 17, 227, 536));
      settingPanel.setBorder(BorderFactory.createTitledBorder(null, "Setting",
          TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
          new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
      settingPanel.add(getAccountButton(), null);
      settingPanel.add(getOptionPanel(), null);
    }
    return settingPanel;
  }

  /**
   * This method initializes syncPanel
   * 
   * @return javax.swing.JPanel
   */
  private JPanel getSyncPanel() {
    if (syncPanel == null) {
      syncPanel = new JPanel();
      syncPanel.setLayout(null);
      syncPanel.setBounds(new Rectangle(243, 17, 526, 536));
      syncPanel.setBorder(BorderFactory.createTitledBorder(null, "Sync",
          TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
          new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
      syncPanel.add(getReadButton(), null);
      syncPanel.add(getPostsPanel(), null);
      syncPanel.add(getLogPanel(), null);
      syncPanel.add(getImportButton(), null);
    }
    return syncPanel;
  }

  /**
   * This method initializes accountButton
   * 
   * @return javax.swing.JButton
   */
  private JButton getAccountButton() {
    if (accountButton == null) {
      accountButton = new JButton();
      accountButton.setBounds(new Rectangle(39, 30, 138, 30));
      accountButton.setText("Account Setting");
      accountButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent e) {
          AccountFrame frame = new AccountFrame();
          frame.pack();
          frame.setVisible(true);
        }
      });
    }
    return accountButton;
  }

  /**
   * This method initializes optionPanel
   * 
   * @return javax.swing.JPanel
   */
  private JPanel getOptionPanel() {
    if (optionPanel == null) {
      postidToLabel1 = new JLabel();
      postidToLabel1.setBounds(new Rectangle(110, 166, 19, 22));
      postidToLabel1.setText("to");
      postidFromLabel1 = new JLabel();
      postidFromLabel1.setBounds(new Rectangle(26, 166, 38, 22));
      postidFromLabel1.setText("from");
      recentToLabel = new JLabel();
      recentToLabel.setBounds(new Rectangle(110, 90, 19, 22));
      recentToLabel.setText("to");
      recentFromLabel = new JLabel();
      recentFromLabel.setBounds(new Rectangle(25, 90, 36, 22));
      recentFromLabel.setText("from");
      optionPanel = new JPanel();
      optionPanel.setLayout(null);
      optionPanel.setBounds(new Rectangle(16, 67, 196, 458));
      optionPanel.setBorder(BorderFactory.createTitledBorder(null,
          "import option", TitledBorder.DEFAULT_JUSTIFICATION,
          TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12),
          new Color(51, 51, 51)));
      optionPanel.add(getRecentRadioButton(), null);
      optionPanel.add(recentFromLabel, null);
      optionPanel.add(getRecentFromTextField(), null);
      optionPanel.add(recentToLabel, null);
      optionPanel.add(getRecentToTextField(), null);
      optionPanel.add(getPostidRadioButton1(), null);
      optionPanel.add(postidFromLabel1, null);
      optionPanel.add(getPostidFromTextField1(), null);
      optionPanel.add(postidToLabel1, null);
      optionPanel.add(getPostidToTextField1(), null);
      optionPanel.add(getPostinRadioButton(), null);
      optionPanel.add(getPostinTextArea(), null);
      optionPanel.add(getSaveOptionButton(), null);

      ButtonGroup group = new ButtonGroup();
      group.add(getRecentRadioButton());
      group.add(getPostidRadioButton1());
      group.add(getPostinRadioButton());
      getRecentRadioButton().setSelected(true);
    }
    return optionPanel;
  }

  /**
   * This method initializes recentRadioButton
   * 
   * @return javax.swing.JRadioButton
   */
  private JRadioButton getRecentRadioButton() {
    if (recentRadioButton == null) {
      recentRadioButton = new JRadioButton();
      recentRadioButton.setBounds(new Rectangle(6, 66, 119, 21));
      recentRadioButton.setText("Recent Posts");
    }
    return recentRadioButton;
  }

  /**
   * This method initializes recentFromTextField
   * 
   * @return javax.swing.JTextField
   */
  private JTextField getRecentFromTextField() {
    if (recentFromTextField == null) {
      recentFromTextField = new JTextField();
      recentFromTextField.setBounds(new Rectangle(61, 90, 48, 22));
    }
    return recentFromTextField;
  }

  /**
   * This method initializes recentToTextField
   * 
   * @return javax.swing.JTextField
   */
  private JTextField getRecentToTextField() {
    if (recentToTextField == null) {
      recentToTextField = new JTextField();
      recentToTextField.setBounds(new Rectangle(132, 90, 48, 22));
    }
    return recentToTextField;
  }

  /**
   * This method initializes postidRadioButton1
   * 
   * @return javax.swing.JRadioButton
   */
  private JRadioButton getPostidRadioButton1() {
    if (postidRadioButton1 == null) {
      postidRadioButton1 = new JRadioButton();
      postidRadioButton1.setBounds(new Rectangle(6, 132, 153, 28));
      postidRadioButton1.setText("Wordpress Post ID");
    }
    return postidRadioButton1;
  }

  /**
   * This method initializes postidFromTextField1
   * 
   * @return javax.swing.JTextField
   */
  private JTextField getPostidFromTextField1() {
    if (postidFromTextField1 == null) {
      postidFromTextField1 = new JTextField();
      postidFromTextField1.setBounds(new Rectangle(64, 166, 45, 22));
    }
    return postidFromTextField1;
  }

  /**
   * This method initializes postidToTextField1
   * 
   * @return javax.swing.JTextField
   */
  private JTextField getPostidToTextField1() {
    if (postidToTextField1 == null) {
      postidToTextField1 = new JTextField();
      postidToTextField1.setBounds(new Rectangle(134, 166, 47, 22));
    }
    return postidToTextField1;
  }

  /**
   * This method initializes postinRadioButton
   * 
   * @return javax.swing.JRadioButton
   */
  private JRadioButton getPostinRadioButton() {
    if (postinRadioButton == null) {
      postinRadioButton = new JRadioButton();
      postinRadioButton.setBounds(new Rectangle(9, 206, 162, 26));
      postinRadioButton.setText("Wordpress Post ID in");
    }
    return postinRadioButton;
  }

  /**
   * This method initializes postinTextArea
   * 
   * @return javax.swing.JTextArea
   */
  private JTextArea getPostinTextArea() {
    if (postinTextArea == null) {
      postinTextArea = new JTextArea();
      postinTextArea.setBounds(new Rectangle(24, 231, 156, 204));
      postinTextArea.setBackground(Color.lightGray);
      postinTextArea.setLineWrap(true);
    }
    return postinTextArea;
  }

  /**
   * This method initializes readButton
   * 
   * @return javax.swing.JButton
   */
  private JButton getReadButton() {
    if (readButton == null) {
      readButton = new JButton();
      readButton.setText("Read Posts from Wordpress");
      readButton.setBounds(new Rectangle(17, 31, 214, 28));
      readButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent e) {
          readPosts();
        }
      });
    }
    return readButton;
  }

  /**
   * This method initializes postsPanel
   * 
   * @return javax.swing.JPanel
   */
  private JPanel getPostsPanel() {
    if (postsPanel == null) {
      GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
      gridBagConstraints1.fill = GridBagConstraints.BOTH;
      gridBagConstraints1.gridy = 0;
      gridBagConstraints1.weightx = 1.0;
      gridBagConstraints1.weighty = 1.0;
      gridBagConstraints1.gridx = 0;
      postsPanel = new JPanel();
      postsPanel.setLayout(new GridBagLayout());
      postsPanel.setBounds(new Rectangle(18, 70, 496, 260));
      postsPanel.setBorder(BorderFactory.createTitledBorder(null,
          "choose posts to import, delete posts by Right Click",
          TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
          new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
      postsPanel.add(getPostsScrollPane(), gridBagConstraints1);
    }
    return postsPanel;
  }

  /**
   * This method initializes postsTable
   * 
   * @return javax.swing.JTable
   */
  private JTable getPostsTable() {
    if (postsTable == null) {
      postsTable = new JTable();
      postsTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
      postsTable.setShowGrid(true);
      DefaultTableModel model = new DefaultTableModel();
      model.setDataVector(null, columnIdentifiers);
      postsTable.setModel(model);
      postsTable.addMouseListener(new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
          if (e.getButton() == MouseEvent.BUTTON3) {
            JTable table = (JTable) e.getSource();
            int[] list = table.getSelectedRows();
            if (list.length == 0) {
              JOptionPane.showMessageDialog(table,
                  "Please choose at least one post!");
              return;
            }
            int o = JOptionPane.showConfirmDialog(table,
                "Are you sure to remove the selected posts", "choose posts",
                JOptionPane.YES_NO_OPTION);
            if (o == JOptionPane.YES_OPTION) {
              TableModel model = table.getModel();
              DefaultTableModel newModel = new DefaultTableModel();
              Object[][] data = new Object[model.getRowCount() - list.length][3];
              int i = 0;
              for (int j = 0; j < model.getRowCount(); j++) {
                boolean flag = true;
                for (int t = 0; t < list.length; t++) {
                  if (list[t] == j) {
                    flag = false;
                  }
                }
                if (flag) {
                  data[i][0] = model.getValueAt(j, 0);
                  data[i][1] = model.getValueAt(j, 1);
                  data[i][2] = model.getValueAt(j, 2);
                  i++;
                }
              }
              newModel.setDataVector(data, columnIdentifiers);
              table.setModel(newModel);
            }
          }
        }
      });
    }
    return postsTable;
  }

  /**
   * This method initializes logPanel
   * 
   * @return javax.swing.JPanel
   */
  private JPanel getLogPanel() {
    if (logPanel == null) {
      GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
      gridBagConstraints11.fill = GridBagConstraints.BOTH;
      gridBagConstraints11.gridy = 0;
      gridBagConstraints11.weightx = 1.0;
      gridBagConstraints11.weighty = 1.0;
      gridBagConstraints11.gridx = 0;
      logPanel = new JPanel();
      logPanel.setLayout(new GridBagLayout());
      logPanel.setBounds(new Rectangle(18, 339, 495, 188));
      logPanel.setBorder(BorderFactory.createTitledBorder(null, "log",
          TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
          new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
      logPanel.add(getLogScrollPane(), gridBagConstraints11);
    }
    return logPanel;
  }

  /**
   * This method initializes importButton
   * 
   * @return javax.swing.JButton
   */
  private JButton getImportButton() {
    if (importButton == null) {
      importButton = new JButton();
      importButton.setBounds(new Rectangle(255, 31, 196, 28));
      importButton.setText("import");
      importButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent e) {
          importPosts();
        }
      });
    }
    return importButton;
  }

  /**
   * This method initializes saveOptionButton
   * 
   * @return javax.swing.JButton
   */
  private JButton getSaveOptionButton() {
    if (saveOptionButton == null) {
      saveOptionButton = new JButton();
      saveOptionButton.setBounds(new Rectangle(33, 31, 128, 28));
      saveOptionButton.setText("Save as Default");
      saveOptionButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent e) {
          saveData();
        }
      });
    }
    return saveOptionButton;
  }

  /**
   * This method initializes postsScrollPane
   * 
   * @return javax.swing.JScrollPane
   */
  private JScrollPane getPostsScrollPane() {
    if (postsScrollPane == null) {
      postsScrollPane = new JScrollPane();
      postsScrollPane.setViewportView(getPostsTable());
    }
    return postsScrollPane;
  }

  /**
   * This method initializes logTextArea
   * 
   * @return javax.swing.JTextArea
   */
  private JTextArea getLogTextArea() {
    if (logTextArea == null) {
      logTextArea = new JTextArea();
      logTextArea.setText("start to log:");
    }
    return logTextArea;
  }

  /**
   * This method initializes logScrollPane
   * 
   * @return javax.swing.JScrollPane
   */
  private JScrollPane getLogScrollPane() {
    if (logScrollPane == null) {
      logScrollPane = new JScrollPane();
      logScrollPane.setViewportView(getLogTextArea());
    }
    return logScrollPane;
  }

} // @jve:decl-index=0:visual-constraint="10,10"
