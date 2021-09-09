package application.main.weather;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class weatherCrawl {
	
	// ³¯¾¾ Å©·Ñ¸µ
	public static void Crawling(weatherVO wVO) throws IOException{
		
		String url = "https://weather.naver.com/";
		Document doc = null;
		
		try {
			doc = Jsoup.connect(url).get();

		}catch(Exception e) {
			e.printStackTrace();
		}
		
		Elements element = doc.select("strong.current");
		wVO.setLocation(doc.select("strong.location_name").get(0).text());
		wVO.setTemperature(Integer.parseInt((element.get(0).text()).substring(5,7)));
		wVO.setStatus(doc.select("span.weather.before_slash").get(0).text());
		
	}

}
