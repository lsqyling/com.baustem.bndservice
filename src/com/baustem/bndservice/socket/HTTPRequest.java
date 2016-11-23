package com.baustem.bndservice.socket;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baustem.bndservice.utils.OBRConfigUtil;


public class HTTPRequest 
{
	private static final Log LOG = LogFactory.getLog(HTTPRequest.class);
	public  static byte[] get(String urlPath,Map<String,String> headerMap)throws Exception
	{
			ByteArrayOutputStream arrayOUT = new ByteArrayOutputStream();
			HttpURLConnection urlCon = null;
			InputStream urlIn = null;
			try {
				URL locationURL=new URL(urlPath);
				String host = locationURL.getHost();
				int port = locationURL.getPort();
				urlCon = (HttpURLConnection)locationURL.openConnection();
				urlCon.setRequestMethod("GET");
				urlCon.setConnectTimeout(2000);
				urlCon.setReadTimeout(15000);
				if(headerMap!=null)
				{
					Iterator<String> it=headerMap.keySet().iterator();
					while(it.hasNext())
					{
						String key=it.next();
						String value=headerMap.get(key);
						urlCon.setRequestProperty(key, value);
					}
				}
				if (host != null)urlCon.setRequestProperty("HOST", host+((port>-1)?":"+port:""));
				urlIn = urlCon.getInputStream();
				byte[] buffer=new byte[8192];
				int c_len=urlCon.getContentLength();
				int r_len=0;
				while(c_len>0)
				{
					int c=urlIn.read(buffer);
					if(c==-1)break;
					r_len+=c;
					arrayOUT.write(buffer, 0, c);
					if(c_len<=r_len)break;
				}
				urlIn.close();
				urlCon.disconnect();
			} catch (Exception e) {
//				LOG.error("http get error:",e);
//				throw e;
			}finally{
				if(urlIn!=null)
					urlIn.close();
				if(urlCon!=null)
				urlCon.disconnect();
			}
			return arrayOUT.toByteArray();
	}

	public static byte[] post(String urlPath,byte[] content,Map<String,String> headerMap)throws Exception
	{
			ByteArrayOutputStream arrayOUT = new ByteArrayOutputStream();
			HttpURLConnection urlCon = null;
			InputStream urlIn = null;
			try {
				URL locationURL=new URL(urlPath);
				String host = locationURL.getHost();
				int port = locationURL.getPort();
				urlCon = (HttpURLConnection)locationURL.openConnection();
				urlCon.setRequestMethod("POST");
				urlCon.setConnectTimeout(2000);
				urlCon.setReadTimeout(15000);
				if(headerMap!=null)
				{
					Iterator<String> it=headerMap.keySet().iterator();
					while(it.hasNext())
					{
						String key=it.next();
						String value=headerMap.get(key);
						urlCon.setRequestProperty(key, value);
					}
				}
				if (host != null)urlCon.setRequestProperty("HOST", host+((port>-1)?":"+port:""));
				urlCon.setDoOutput(true);
				urlCon.setDoInput(true);
				urlCon.setDefaultUseCaches(false);
				urlCon.setAllowUserInteraction(true);
				
				OutputStream out=urlCon.getOutputStream();
				if(content!=null)out.write(content);
				out.flush();
				
				urlIn= urlCon.getInputStream();
				byte[] buffer=new byte[8192];
				int c_len=urlCon.getContentLength();
				int r_len=0;
				while(c_len>0)
				{
					int c=urlIn.read(buffer);
					if(c==-1)break;
					r_len+=c;
					arrayOUT.write(buffer, 0, c);
					if(c_len<=r_len)break;
				}

			
			} catch (Exception e) {
				LOG.error("http post error:",e);
				throw e;
			}finally{
				if(urlIn!=null)
					urlIn.close();
				if(urlCon!=null)
				urlCon.disconnect();
			}
			
			return arrayOUT.toByteArray();
	}
	
	public static byte[] postBySocket(String urlPath, byte[] content,
			Map<String, String> headerMap) throws Exception {

		LOG.info("post url======"+urlPath);
		if(content!=null){
			LOG.info("====================HTTPClient Request===================:\n"+ new String(content, "utf-8"));
		}
		LOG.info("=====================================");

		URL url = new URL(urlPath);
		String path = url.getPath();
		String ip = url.getHost();
		int port = url.getPort();
		if (port == -1) {
			port = 80;
		}
		Socket socket = null;
		byte[] data = null;
		try {
			socket = new Socket();
			
			String clientIP = "";
			NetworkInterface networkInterface=NetworkInterface.getByName(ConfigInfo.EROUTERNetwork);
			if(networkInterface!=null){
				Enumeration<InetAddress> inetAddresss=networkInterface.getInetAddresses();
				while(inetAddresss.hasMoreElements()){
					InetAddress inetAddress=inetAddresss.nextElement();
					if(inetAddress instanceof Inet4Address){
						clientIP=inetAddress.getHostAddress();
						break;
					}
				}
			}
			socket.bind(new InetSocketAddress(clientIP,0));
			socket.connect(new InetSocketAddress(ip, port), 5000);
			socket.setSoTimeout(30000);

			StringBuffer request = new StringBuffer();
			request.append("POST " + path + " HTTP/1.1");
			request.append("\r\n");
			request.append("HOST: " + ip+":"+port);
			request.append("\r\n");
			request.append("Content-Type: application/x-json; charset=utf-8");
			request.append("\r\n");

			if (headerMap != null) {
				Iterator<String> it = headerMap.keySet().iterator();
				while (it.hasNext()) {
					String key = it.next();
					String value = headerMap.get(key);
					request.append(key + ": " + value);
					request.append("\r\n");
				}
			}
			if(content!=null){
				request.append("Content-Length: " + content.length);
				request.append("\r\n");
				request.append("\r\n");
				request.append(new String(content, "UTF-8"));
			}else{
				request.append("\r\n");
			}

			socket.getOutputStream().write(request.toString().getBytes());
			socket.getOutputStream().flush();

			InputStream is = socket.getInputStream();
			Map<String, Object> xxxx = getMethod(is);
			byte[] result=(byte[])xxxx.get("message");
			if(result==null){
				String chunked=(String)xxxx.get("Transfer-Encoding");
				if(chunked!=null){
					if(chunked.trim().equals("chunked")){
						data= getTrunkData(is);
					}
				}
			}else{
				//String message = new String(result,"UTF-8");
				//data = message.getBytes("UTF-8");
				data=result;
			}

		} catch (Exception e) {
//			LOG.error("http post error:", e);
//			throw e;
		} finally {
			if (socket != null)
				socket.close();
		}
		
		if(data!=null){
//			 String s2 = new String(data, "UTF-8");
//			 byte[] converttoBytes = s2.getBytes("UTF-8");
			LOG.info("--------------------------HTTPClient Response----------------------:\n"+ new String(data, "UTF-8"));
		}
		LOG.info("------------------------------------------------");
		return data;
	}
	
	
	public static byte[] getBySocket(String urlPath, Map<String, String> headerMap) throws Exception {

//		LOG.info("get url======"+urlPath);
//		LOG.info("=====================================");

		URL url = new URL(urlPath);
		String path = url.getPath();
		String ip = url.getHost();
		int port = url.getPort();
		if (port == -1) {
			port = 80;
		}
		Socket socket = null;
		byte[] data = null;
		try {
			socket = new Socket();
			//  请求绑定
			String clientIP = "192.168.1.202";
			NetworkInterface networkInterface=NetworkInterface.getByName(ConfigInfo.EROUTERNetwork);
			if(networkInterface!=null){
				Enumeration<InetAddress> inetAddresss=networkInterface.getInetAddresses();
				while(inetAddresss.hasMoreElements()){
					InetAddress inetAddress=inetAddresss.nextElement();
					if(inetAddress instanceof Inet4Address){
						clientIP=inetAddress.getHostAddress();
						break;
					}
				}
			}
			socket.bind(new InetSocketAddress(clientIP,0));
			socket.connect(new InetSocketAddress(ip, port), 5000);
			socket.setSoTimeout(30000);

			StringBuffer request = new StringBuffer();
			request.append("GET " + path + " HTTP/1.1");
			request.append("\r\n");
			request.append("HOST: " + ip+":"+port);
			request.append("\r\n");
			request.append("Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*");
			request.append("Accept-Encoding: gzip,deflate,sdch");
			request.append("Accept-Language: zh-CN,zh");
			request.append("Connection: keep-alive");
			request.append("\r\n");

			if (headerMap != null) {
				Iterator<String> it = headerMap.keySet().iterator();
				while (it.hasNext()) {
					String key = it.next();
					String value = headerMap.get(key);
					request.append(key + ": " + value);
					request.append("\r\n");
				}
			}
			
			request.append("\r\n");

			socket.getOutputStream().write(request.toString().getBytes());
			socket.getOutputStream().flush();

			InputStream is = socket.getInputStream();
			Map<String, Object> xxxx = getMethod(is);
			byte[] result=(byte[])xxxx.get("message");
			if(result==null){
				String chunked=(String)xxxx.get("Transfer-Encoding");
				if(chunked!=null){
					if(chunked.trim().equals("chunked")){
						data= getTrunkData(is);
					}
				}
			}else{
				//data = message.getBytes("UTF-8");
				data=result;
			}

		} catch (Exception e) {
//			LOG.error("http get error:", e);
//			throw e;
		} finally {
			if (socket != null)
				socket.close();
		}
		
		if(data!=null){
//			 String s2 = new String(data, "UTF-8");
//			 byte[] converttoBytes = s2.getBytes("UTF-8");
//			LOG.info("--------------------------HTTPClient Response----------------------:\n"+ new String(data, "UTF-8"));
		}
//		LOG.info("------------------------------------------------");
		return data;
	}
	
	
	public static InputStream getBySocketInputStream(URL url, Map<String, String> headerMap) throws Exception {


		String path = url.getPath();
		String ip = url.getHost();
		int port = url.getPort();
		if (port == -1) {
			port = 80;
		}
		Socket socket = null;
		byte[] data = null;
		try {
			socket = new Socket();
			//  请求绑定
			String clientIP = OBRConfigUtil.getBindIp();
			LOG.info("HTTPRequest.getBySocketInputStream(URL url, Map<String, String> headerMap):clientIp="+clientIP);
			socket.bind(new InetSocketAddress(clientIP,0));
			socket.connect(new InetSocketAddress(ip, port), 5000);
			socket.setSoTimeout(30000);

			StringBuffer request = new StringBuffer();
			request.append("GET " + path + " HTTP/1.1");
			request.append("\r\n");
			request.append("HOST: " + ip+":"+port);
			request.append("\r\n");
			request.append("Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*");
			request.append("Accept-Encoding: gzip,deflate,sdch");
			request.append("Accept-Language: zh-CN,zh");
			request.append("Connection: keep-alive");
			request.append("\r\n");
			if (headerMap != null) {
				Iterator<String> it = headerMap.keySet().iterator();
				while (it.hasNext()) {
					String key = it.next();
					String value = headerMap.get(key);
					request.append(key + ": " + value);
					request.append("\r\n");
				}
			}
			request.append("\r\n");

			socket.getOutputStream().write(request.toString().getBytes());
			socket.getOutputStream().flush();

			InputStream is = socket.getInputStream();
			Map<String, Object> xxxx = getMethod(is);
			byte[] result=(byte[])xxxx.get("message");
			if(result==null){
				String chunked=(String)xxxx.get("Transfer-Encoding");
				if(chunked!=null){
					if(chunked.trim().equals("chunked")){
						data= getTrunkData(is);
					}
				}
			}else{
				data=result;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (socket != null)
				socket.close();
		}
		LOG.info("HttpRequest.getBySocketInputStream() run second last step");
		return new ByteArrayInputStream(data);
	}
	
	
	private static byte[] getTrunkData(InputStream is)throws Exception{
		ByteArrayOutputStream arrayOUT = new ByteArrayOutputStream();
		while(true){
			//TODO 读取长度
			byte[] lenData=readLineData(is);
			int len=0;
			String tt=new String(lenData);
			len=Integer.valueOf(tt,16);
			if(len==0){
				//TODO 跳过\r\n
				is.skip(2);
				break;
			}
			
			//TODO 读取数据部分
			byte[] tmp=new byte[len];
			int i=0;
			//LOG.info("len==="+len);
			
			byte[] temp=null;
			if(len>1024){
				temp=new byte[1024];
			}else{
				temp=new byte[len];
			}
			
			while(true){
				int j=is.read(temp);
				//LOG.info("jj===="+j);
				if(j==-1)
					continue;
				System.arraycopy(temp, 0, tmp, i, j);
				i+=j;
				//LOG.info("ii===="+i);
				if(i==len)
					break;
				if((len-i)>0){
					temp=new byte[len-i];
				}
			}
			
			//TODO 跳过\r\n
			//is.skip(2);
			int r=is.read();
			int n=is.read();
			arrayOUT.write(tmp);
		}
		return arrayOUT.toByteArray();
	}
	
	private static  Map<String, Object> getMethod(InputStream is) throws Exception {
		Map<String, Object> result=new HashMap<String, Object>();
		// TODO 分析HTTP头部信息
		String line = readLine(is);
		if (line == null) {
			return null;
		}
		line = line.trim();
		int contentLen = 0;
		while (true) {
			String header = readLine(is);
			if (header == null || header.equals("")) {
				if (contentLen > 0) {
					byte[] responseData=new byte[contentLen];
					int i=0;
					byte[] temp=new byte[1024];
					if(contentLen>1024){
						while(true){
							int j=is.read(temp);
							System.arraycopy(temp, 0, responseData, i, j);
							i+=j;
							if(i==contentLen)
								break;
						}
					}else{
						is.read(responseData);
					}
					
					//String entityIngo=new String(responseData,"UTF-8");
					//result.put("message", entityIngo);
					
					result.put("message", responseData);
				}
				break;
			}
			if (header.indexOf("Content-Length") > -1) {
				contentLen = Integer.valueOf(header.split(":")[1].trim());
			}
			String[] hh=header.split(":");
			if(hh.length==2){
				result.put(hh[0], hh[1]);
			}
		}

		if (line.startsWith("HTTP/1.1")) {
			// TODO HTTP/1.1 401 Unauthorized
			String code = line.split(" ")[1];
			result.put("code", code);
		}
		return result;
	}

	private static String readLine(InputStream in) throws Exception {

		String result = new String(readLineData(in), "UTF-8");
//		LOG.info(result);
		return result;
	}
	
	private static byte[] readLineData(InputStream in) throws Exception {

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		int a = in.read();
		while (true) {
			int b = in.read();
			if (b == -1)
				return null;
			if (a == 0x0D && b == 0x0A) {
				byte[] data = os.toByteArray();
				return data;
			} else {
				os.write(a);
			}
			a = b;
		}
	}
	
}
