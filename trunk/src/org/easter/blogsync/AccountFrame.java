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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

public class AccountFrame extends JFrame {

  private static final long serialVersionUID = 1L;
  private JPanel jContentPane = null;
  private JLabel wpUserLabel = null;
  private JPanel WordpressPanel = null;
  private JLabel wpPasswdLabel = null;
  private JLabel wpUrlLabel = null;
  private JTextField wpUserTextField = null;
  private JTextField wpUrlTextField = null;
  private JPanel BloggerPanel = null;
  private JLabel bgUserLabel1 = null;
  private JLabel bgPasswdLabel1 = null;
  private JLabel bgBlogidLabel1 = null;
  private JTextField bgUserTextField1 = null;
  private JTextField bgBlogidTextField1 = null;
  private JButton saveButton = null;
  private JPasswordField wpPasswordField = null;
  private JPasswordField bgPasswordField = null;

  Properties p = null;

  public AccountFrame() {
    super();
    initialize();
    loadData();
  }

  private void loadData() {
    p = new Properties();
    try {
      FileInputStream fis = new FileInputStream(BlogHelper.settingFile);
      p.load(fis);
      getWpUserTextField().setText(p.getProperty(BlogHelper.wpUsername));
      getWpPasswordField().setText(p.getProperty(BlogHelper.wpPassword));
      getWpUrlTextField().setText(p.getProperty(BlogHelper.wpUrl));
      getBgUserTextField1().setText(p.getProperty(BlogHelper.bgUsername));
      getBgPasswordField().setText(p.getProperty(BlogHelper.bgPassword));
      getBgBlogidTextField1().setText(p.getProperty(BlogHelper.bgBlogid));
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
    p.setProperty(BlogHelper.wpUsername, getWpUserTextField().getText());
    p.setProperty(BlogHelper.wpPassword, new String(
        getWpPasswordField().getPassword()));
    p.setProperty(BlogHelper.wpUrl, getWpUrlTextField().getText());
    p.setProperty(BlogHelper.bgUsername, getBgUserTextField1().getText());
    p.setProperty(BlogHelper.bgPassword, new String(
        getBgPasswordField().getPassword()));
    p.setProperty(BlogHelper.bgBlogid, getBgBlogidTextField1().getText());
    try {
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

  private void initialize() {
    this.setSize(559, 491);
    this.setLocation(new Point(200, 200));
    this.setMinimumSize(new Dimension(559, 491));
    this.setPreferredSize(new Dimension(559, 491));
    this.setResizable(false);
    this.setContentPane(getJContentPane());
    this.setTitle("Account Setting");
    this.setVisible(true);
  }

  private JPanel getJContentPane() {
    if (jContentPane == null) {
      wpUserLabel = new JLabel();
      wpUserLabel.setText("UserName");
      wpUserLabel.setHorizontalAlignment(SwingConstants.RIGHT);
      wpUserLabel.setBounds(new Rectangle(16, 29, 79, 31));
      jContentPane = new JPanel();
      jContentPane.setLayout(null);
      jContentPane.add(getWordpressPanel(), null);
      jContentPane.add(getBloggerPanel(), null);
      jContentPane.add(getSaveButton(), null);
    }
    return jContentPane;
  }

  private JPanel getWordpressPanel() {
    if (WordpressPanel == null) {
      wpUrlLabel = new JLabel();
      wpUrlLabel.setBounds(new Rectangle(14, 119, 82, 31));
      wpUrlLabel.setHorizontalAlignment(SwingConstants.RIGHT);
      wpUrlLabel.setText("WP URL");
      wpPasswdLabel = new JLabel();
      wpPasswdLabel.setBounds(new Rectangle(14, 77, 79, 30));
      wpPasswdLabel.setHorizontalAlignment(SwingConstants.RIGHT);
      wpPasswdLabel.setText("Password");
      WordpressPanel = new JPanel();
      WordpressPanel.setLayout(null);
      WordpressPanel.setBounds(new Rectangle(16, 15, 504, 169));
      WordpressPanel.setBorder(BorderFactory.createTitledBorder(null,
          "Wordpress Account", TitledBorder.DEFAULT_JUSTIFICATION,
          TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12),
          new Color(51, 51, 51)));
      WordpressPanel.add(wpUserLabel, null);
      WordpressPanel.add(wpPasswdLabel, null);
      WordpressPanel.add(wpUrlLabel, null);
      WordpressPanel.add(getWpUserTextField(), null);
      WordpressPanel.add(getWpPasswordField(), null);
      WordpressPanel.add(getWpUrlTextField(), null);
    }
    return WordpressPanel;
  }

  private JTextField getWpUserTextField() {
    if (wpUserTextField == null) {
      wpUserTextField = new JTextField();
      wpUserTextField.setBounds(new Rectangle(120, 28, 339, 33));
    }
    return wpUserTextField;
  }

  /**
   * This method initializes wpUrlTextField
   * 
   * @return javax.swing.JTextField
   */
  private JTextField getWpUrlTextField() {
    if (wpUrlTextField == null) {
      wpUrlTextField = new JTextField();
      wpUrlTextField.setBounds(new Rectangle(120, 118, 339, 33));
    }
    return wpUrlTextField;
  }

  /**
   * This method initializes BloggerPanel
   * 
   * @return javax.swing.JPanel
   */
  private JPanel getBloggerPanel() {
    if (BloggerPanel == null) {
      bgBlogidLabel1 = new JLabel();
      bgBlogidLabel1.setBounds(new Rectangle(14, 119, 82, 31));
      bgBlogidLabel1.setText("blogid");
      bgBlogidLabel1.setHorizontalAlignment(SwingConstants.RIGHT);
      bgPasswdLabel1 = new JLabel();
      bgPasswdLabel1.setBounds(new Rectangle(14, 77, 79, 30));
      bgPasswdLabel1.setText("Password");
      bgPasswdLabel1.setHorizontalAlignment(SwingConstants.RIGHT);
      bgUserLabel1 = new JLabel();
      bgUserLabel1.setBounds(new Rectangle(16, 29, 79, 31));
      bgUserLabel1.setText("UserName");
      bgUserLabel1.setHorizontalAlignment(SwingConstants.RIGHT);
      BloggerPanel = new JPanel();
      BloggerPanel.setLayout(null);
      BloggerPanel.setBounds(new Rectangle(16, 210, 504, 175));
      BloggerPanel.setBorder(BorderFactory.createTitledBorder(null, "Blogger Account", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
      BloggerPanel.add(bgUserLabel1, null);
      BloggerPanel.add(bgPasswdLabel1, null);
      BloggerPanel.add(bgBlogidLabel1, null);
      BloggerPanel.add(getBgUserTextField1(), null);
      BloggerPanel.add(getBgPasswordField(), null);
      BloggerPanel.add(getBgBlogidTextField1(), null);
    }
    return BloggerPanel;
  }

  /**
   * This method initializes bgUserTextField1
   * 
   * @return javax.swing.JTextField
   */
  private JTextField getBgUserTextField1() {
    if (bgUserTextField1 == null) {
      bgUserTextField1 = new JTextField();
      bgUserTextField1.setBounds(new Rectangle(120, 26, 340, 35));
    }
    return bgUserTextField1;
  }

  /**
   * This method initializes bgUrlTextField1
   * 
   * @return javax.swing.JTextField
   */
  private JTextField getBgBlogidTextField1() {
    if (bgBlogidTextField1 == null) {
      bgBlogidTextField1 = new JTextField();
      bgBlogidTextField1.setBounds(new Rectangle(120, 118, 340, 35));
    }
    return bgBlogidTextField1;
  }

  /**
   * This method initializes saveButton
   * 
   * @return javax.swing.JButton
   */
  private JButton getSaveButton() {
    if (saveButton == null) {
      saveButton = new JButton();
      saveButton.setBounds(new Rectangle(192, 406, 132, 36));
      saveButton.setText("save");
      saveButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent e) {
          saveData();
        }
      });
    }
    return saveButton;
  }

  /**
   * This method initializes wpPasswordField
   * 
   * @return javax.swing.JPasswordField
   */
  private JPasswordField getWpPasswordField() {
    if (wpPasswordField == null) {
      wpPasswordField = new JPasswordField();
      wpPasswordField.setBounds(new Rectangle(120, 75, 339, 33));
    }
    return wpPasswordField;
  }

  /**
   * This method initializes bgPasswordField
   * 
   * @return javax.swing.JPasswordField
   */
  private JPasswordField getBgPasswordField() {
    if (bgPasswordField == null) {
      bgPasswordField = new JPasswordField();
      bgPasswordField.setBounds(new Rectangle(120, 76, 340, 35));
    }
    return bgPasswordField;
  }

} // @jve:decl-index=0:visual-constraint="10,10"
