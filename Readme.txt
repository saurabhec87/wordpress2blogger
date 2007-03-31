Readme.txt

0. What is it used for?
This is a simple tool to import all posts from wordpress to blogger/blogspot.

1. How to use it?
0) set java in your path.
for windows users, run run.bat
for unix/mac users, run run.sh
1) set up your wordpress and blogger account.
2) choose one import option from option panel.
3) read posts from wordpress.
4) check posts and remove any posts that you do not want to import by selecting and right-clicking.
5) import now by just clicking the import button.

2. what is my blogger blogid?
It's in your blogspot Dashboard url, for example: http://www2.blogger.com/posts.g?blogID=18083698
"18083698" is your blogid.

3. what may cause this error?
[Fatal Error] :1444:21: Invalid byte 1 of 1-byte UTF-8 sequence.
org.apache.xmlrpc.client.XmlRpcClientException: Failed to parse
servers response: Invalid byte 1 of 1-byte UTF-8 sequence.

One of your posts may be an invalid xml.
please correct it and make sure its source code is valid xml, then import again.

4. any other questions?
welcome to send your feedback to Yichao.Zhang@gmail.com
or leave me a comment on http://zeaster.blogspot.com