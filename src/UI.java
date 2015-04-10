import java.util.Calendar;
import java.util.Scanner;


public class UI {
	
	private int today_day;
	private int today_month;
	private int today_year;
	private int[] normal_max_days = new int[]{31,28,31,30,31,30,31,31,30,31,30,31};
	private int[] erb_max_days = new int[]{31,29,31,30,31,30,31,31,30,31,30,31};
	
	public static void main(String[] args){
		new UI();
	}
	public UI(){
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		String input;
		String[] values;
		while(true){
			System.out.println("Input date like this: 2015,02,05");
			input = scanner.nextLine();
			values = input.split(",");
			try {
				today_day = Integer.parseInt(values[2].trim());
				today_month = Integer.parseInt(values[1].trim());
				today_year = Integer.parseInt(values[0].trim());
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("Input Error!");
				continue;
			}
			// 不合法
			if (!isLegal()) {
				System.out.println("Input not Legal!");
				continue;
			}
			// 输出今天的完整日期
			System.out.println("输入日期是: "+outputDate(0)+" "+outputWeek(0));
			outputLunar(0);
			// 输出明天的完整日期
			System.out.println("第二天是： "+outputDate(1)+" "+outputWeek(1));
			outputLunar(1);
			// 输出下一周的完整日期
			System.out.println("7天后是： "+outputDate(7)+" "+outputWeek(7));
			outputLunar(7);
		}
	}
	
	public void outputLunar(int offset){
		try {
			Calendar today = Calendar.getInstance();
	        today.setTime(Lunar.chineseDateFormat.parse(outputDate(offset)));
	        Lunar lunar = new Lunar(today);
	        System.out.println("北京时间：" + Lunar.chineseDateFormat.format(today.getTime()) + "　农历" + lunar);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Lunar parse exception");
		}
	}
	
	public boolean isLeapYear(int time){
		if (time%4 == 0 && time%100 != 0) {
			return true;
		}else if(time%400 == 0){
			return true;
		}
		return false;
	}
	
	public boolean isLegal(){
		// 年份输入正确
		if (today_year >= 1900 && today_year <= 2100) {
			if (today_month >= 1 && today_month <= 12) {
				if (isLeapYear(today_year)) {
					if (today_day >= 1 && today_day <= erb_max_days[today_month-1]) {
						return true;
					}
				}else if (today_day >= 1 && today_day <= normal_max_days[today_month-1]) {
					return true;
				}
			}
		}return false;
	}
	
	public String outputDate(int offset){
		int today_offset = 0;
		if (isLeapYear(today_year)) {
			for (int i = 1; i < today_month; i++) {
				today_offset += erb_max_days[i-1];
			}
		}else {
			for (int i = 1; i < today_month; i++) {
				today_offset += normal_max_days[i-1];
			}
		}
		today_offset += today_day;
		
		int day_offset = today_offset+offset;
		int year = today_year;
		int month = 0;
		int day = 0;
		if (isLeapYear(today_year)) {
			// 需要跨年
			if (day_offset > 366) {
				year++;
				day_offset -= 366;
			}
			// 减出月份
			for (int i = 0; i < 12; i++) {
				if (day_offset <= erb_max_days[i]) {
					month = i+1;
					day = day_offset;
					break;
				}
				day_offset -= erb_max_days[i];
			}
		}else {
			// 需要跨年
			if (day_offset > 365) {
				year++;
				day_offset -= 365;
			}
			// 减出月份
			for (int i = 0; i < 12; i++) {
				if (day_offset <= normal_max_days[i]) {
					month = i+1;
					day = day_offset;
					break;
				}
				day_offset -= normal_max_days[i];
			}
		}
		
		return year+"年 "+month+"月 "+day+"日 ";
	}
	
	public String outputWeek(int offset){
		int week_offset = 0;
		// 计算往年
		for (int i = 1900; i < today_year; i++) {
			if (isLeapYear(i)) {
				week_offset += 366;
			}else {
				week_offset += 365;
			}
		}
		// 计算当年
		if (isLeapYear(today_year)) {
			for (int i = 0; i < today_month-1; i++) {
					week_offset += erb_max_days[i];
			}
		}else {
			for (int i = 0; i < today_month-1; i++) {
				week_offset += normal_max_days[i];
			}
		}
		
		// 计算当月
		week_offset += today_day;
		// 加偏移
		week_offset += offset;
		
		return "星期："+(week_offset%7>0 ? week_offset%7:7);
	}
}
