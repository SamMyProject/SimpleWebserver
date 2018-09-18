package webServer;

import java.io.*;
import java.util.*;

import java.net.*;

public class sev {
	ServerSocket serverSocket;
	String root = "C:\\Users\\user\\Desktop";
	String fullPath = "";
	static int h = 0;
	static HttpCookie cookie = new HttpCookie("foo", "bar");

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		// cookie.getName();
		// cookie.getValue();
		// System.out.println(cookie.getName());
		sev sev = new sev();
		sev.start();
	}

	void output(DataOutputStream out, String str) throws Exception {
		out.writeBytes(str + "\r\n");// 寫到socket裡面
		System.out.println(str);
		out.flush();
	}

	public static String innerText(String pText, String beginMark, String endMark) {
		int beginStart = pText.indexOf(beginMark);
		if (beginStart < 0)
			return null;
		int beginEnd = beginStart + beginMark.length();
		int endStart = pText.indexOf(endMark, beginEnd);
		if (endStart < 0)
			return null;
		return pText.substring(beginEnd, endStart);
	}

	protected void start() throws IOException {
		System.out.println("Start");
		// cookie.setMaxAge(1000);
		try {
			serverSocket = new ServerSocket(90);

		} catch (Exception e) {
			System.out.println("error" + e);
		}
		System.out.println("wait for con.......");
		while (true) {
			try {
				Socket socket = serverSocket.accept();
				System.out.println("con ok!");

				DataOutputStream out = new DataOutputStream(socket.getOutputStream()); // 取得傳送資料的輸出檔。
				DataInputStream in = new DataInputStream(socket.getInputStream()); // 取得接收資料的輸入檔。
				String request = "";

				fullPath = "login.html";

				while (true) {
					String line = in.readLine();
					if (line == null)
						break;
					request += line + "\r\n";
					if (line.length() == 0)
						break;
				}
				System.out.println(request);

				if (request.indexOf("HTTP/1.") < 0) {
					output(out, "HTTP/1.1 505 error"); // 傳回成功訊息。
					output(out, "Content-Type:text/html "); // 傳回檔案類型。

					output(out, "");
				} else {
					File file = null;

					if (request.indexOf("GET") >= 0 && (h == 1 || fullPath.equals("login.html"))) {

						if (!innerText(request, "GET ", "HTTP/").trim().equals("/"))
							fullPath = innerText(request, "GET /", "HTTP/").trim();

						// System.out.print(fullPath+"l");
						file = new File(fullPath);// 在此處打開index1.html(開啟檔案)
						// System.out.println(fullPath);
						if (!file.exists()) {
							output(out, "HTTP/1.1 404 Error"); // 傳回成功訊息。
							output(out, "Content-Type:text/html "); // 傳回檔案類型。
							// 傳回檔案類型。
							output(out, "");
						} else {
							if (fullPath.indexOf("yy.html") >= 0) {
								output(out, "HTTP/1.1 301 Moved Permanently"); // 傳回成功訊息。
								output(out, "Content-Type:text/html "); // 傳回檔案類型。
								output(out, "");
							} else {
								output(out, "HTTP/1.1 200 OK"); // 傳回成功訊息。
								output(out, "Content-Type:text/html "); // 傳回檔案類型。
								output(out, "Content-Length: " + file.length()); // 傳回內容長度。
								output(out, "Server: LAB312");
								output(out, "");
							} // else
							byte[] buffer = new byte[4096];
							FileInputStream is = new FileInputStream(fullPath);
							while (true) {
								int len = is.read(buffer);
								out.write(buffer, 0, len);
								if (len < 4096)
									break;
							}
						} // else

					} else if (request.indexOf("HEAD") >= 0 && h == 1) {
						System.out.println(request);
						if (!innerText(request, "HEAD ", "HTTP/").trim().equals("/"))
							fullPath = innerText(request, "HEAD /", "HTTP/").trim();
						file = new File(fullPath);// 在此處打開index1.html(開啟檔案)
						if (!file.exists())
							throw new Exception("File not found !");
						if (fullPath.indexOf("yy.html") >= 0) {
							output(out, "HTTP/1.1 301 Moved Permanently"); // 傳回成功訊息。
							output(out, "Content-Type:text/html "); // 傳回檔案類型。
							output(out, "");
						} else {
							output(out, "HTTP/1.1 200 OK"); // 傳回成功訊息。
							output(out, "Content-Type: "); // 傳回檔案類型。
							output(out, "Content-Length: " + file.length()); // 傳回內容長度。
							output(out, "");
						} // else
					} // else if
					else if (request.indexOf("POST") >= 0) {
						System.out.println(request);
						String lengthStr = innerText(request.toLowerCase(), "content-length:", "\r\n");
						String post = "";
						if (lengthStr != null) {
							int contentLength = Integer.parseInt(lengthStr.trim());
							byte[] bytes = new byte[contentLength];
							in.read(bytes);
							post = new String(bytes);
							request += post;
						}
						System.out.println(post);

						if (post.contains("user") && post.contains("password")) {
							String user = innerText(post, "user=", "&").trim();
							String password = post.substring(post.lastIndexOf("=") + 1); // password
							if (request.indexOf("Cookie") >= 0) {
								if (!user.equals("12345") || !password.equals("125")) {
									output(out, "refresh:0;URL=text.html");
									output(out, "HTTP/1.1 200 OK"); // 傳回成功訊息。
									output(out, "Content-Type: text/html"); // 傳回檔案類型。
									output(out, "Content-Length: "); // 傳回內容長度
									output(out, "Server: LAB312");
									output(out, "");
									h = 1;
								} // if
								else {
									output(out, "HTTP/1.1 200 OK"); // 傳回成功訊息。
									output(out, "Content-Type: text/html"); // 傳回檔案類型。
									output(out, "Content-Length: "); // 傳回內容長度。
									output(out, "refresh:0;URL=2.html");
									output(out, "Server: LAB312");
									output(out, "");
								} // else
							} // if
							else {
								if (user.equals("12345") && password.equals("125")) {
									output(out, "HTTP/1.1 200 OK"); // 傳回成功訊息。
									output(out, "Content-Type: text/html"); // 傳回檔案類型。
									output(out, "Content-Length: "); // 傳回內容長度。
									output(out, "Set-Cookie: user=12345;Max-Age=600");
									output(out, "Set-Cookie: password=125;Max-Age=600");
									output(out, "refresh:0;URL=2.html");
									output(out, "Server: LAB312");
									output(out, "");
									h = 1;
								} // if
								else {
									output(out, "refresh:0;URL=login.html");
									output(out, "HTTP/1.1 200 OK"); // 傳回成功訊息。
									output(out, "Server: LAB312");
									output(out, "");
								} // else
							} // else
						} // if
						else {
							output(out, "HTTP/1.1 200 OK"); // 傳回成功訊息。
							output(out, "Content-Type: text/html"); // 傳回檔案類型。
							output(out, "Server: LAB312");
							output(out, "");

							String name = null;
							if (post.indexOf("filename") >= 0) {
								name = innerText(request.toLowerCase(), "filename=\"", "\"");
								System.out.print(name);
							}
							else if(post.indexOf("name") >= 0) {
								name = innerText(request.toLowerCase(), "name=\"", "\"");
								System.out.print(name);
							}
							file = new File(name);
							if (!file.exists())
								file.createNewFile();

							FileWriter fw = new FileWriter(name, true);
							String getin = innerText(post.toLowerCase(), "\r\n\r\n", "------");

							fw.write(getin + "\r\n");
							fw.close();
//							byte[] buffer = new byte[4096];
//							FileInputStream is = new FileInputStream("login.html");
//							while (true) {
//								int len = is.read(buffer);
//								out.write(buffer, 0, len);
//								if (len < 4096)
//									break;
//							}//while
						} // else
					} // else if

					else if (request.indexOf("PUT") >= 0 && h == 1) {
						String name = null;
						String lengthStr = innerText(request.toLowerCase(), "content-length:", "\r\n");
						String post = "";
						if (lengthStr != null) {
							int contentLength = Integer.parseInt(lengthStr.trim());
							byte[] bytes = new byte[contentLength];
							in.read(bytes);
							post = new String(bytes);
							request += post;
						}
						System.out.print(post);
						if (post.indexOf("filename") >= 0) {
							name = innerText(request.toLowerCase(), "filename=\"", "\"");
							System.out.print(name);
						}
						file = new File(name);
						if (!file.exists())
							file.createNewFile();

						FileWriter fw = new FileWriter(name);
						String getin = innerText(post.toLowerCase(), "\r\n\r\n", "------");
						System.out.print(getin);
						fw.write(getin + "\r\n");
						fw.close();
						output(out, "HTTP/1.1 200 OK"); // 傳回成功訊息。
						output(out, "Content-Type: text/html"); // 傳回檔案類型。
						output(out, "Content-Length: " + file.length()); // 傳回內容長度。
						output(out, "Server: LAB312");
						output(out, "");
						// if (fullPath.indexOf("yy.html") >= 0) {
						// output(out, "HTTP/1.1 301 Moved Permanently"); //
						// 傳回成功訊息。
						// output(out, "Content-Type:text/html "); // 傳回檔案類型。
						// // output(out, "Location:http://youtube.com"); //
						// // 傳回檔案類型。
						// output(out, "");
						// // throw new Exception("HTTP/1.1 301 Moved
						// // Permanently");
						// } else {
						// output(out, "HTTP/1.1 200 OK"); // 傳回成功訊息。
						// output(out, "Content-Type: text/html"); // 傳回檔案類型。
						// output(out, "Content-Length: " + file.length()); //
						// 傳回內容長度。
						// output(out, "Server: LAB312");
						// output(out, "");
						// } // else
						// byte[] buffer = new byte[4096];
						// FileInputStream is = new FileInputStream(fullPath);
						// while (true) {
						// int len = is.read(buffer);
						// out.write(buffer, 0, len);
						// if (len < 4096)
						// break;
						// }

					} // else if
					else if (request.indexOf("DELETE") >= 0 && h == 1) {
						if (!innerText(request, "DELETE ", "HTTP/").trim().equals("/"))
							fullPath = innerText(request, "DELETE /", "HTTP/").trim();
						try {
							file = new File(fullPath);// 在此處打開index1.html(開啟檔案)
							if (!file.exists()) {
								output(out, "HTTP/1.1 404 Error"); // 傳回成功訊息。
								output(out, "Content-Type:text/html "); // 傳回檔案類型。
								output(out, "");
							} else {
								if (file.delete()) {
									output(out, "HTTP/1.1 204 No Content!");
									output(out, "Content-Type:text/html "); // 傳回檔案類型。
									output(out, "Content-Length: " + 0); // 傳回內容長度。
									output(out, "Server: LAB312");
									output(out, "");
								}
							} // else
						} catch (Exception e) {
							System.out.println("error" + e);
							output(out, "HTTP/1.1 404 File not found !");
						}

					} // else if
					else {

						output(out, "HTTP/1.1 400 Bad Request!");
						output(out, "Content-Type:text/html "); // 傳回檔案類型。
						output(out, "Content-Length: "); // 傳回內容長度。
						output(out, "Server: LAB312");
						output(out, "");

					}
				}
				out.flush();

				socket.close();
			} catch (Exception e) {
				System.out.println("error" + e);
			}

		}
	}

}
