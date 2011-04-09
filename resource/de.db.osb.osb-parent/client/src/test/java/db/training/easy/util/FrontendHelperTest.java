package db.training.easy.util;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Test;

public class FrontendHelperTest {

	@Test
	public void testCastStringToTimeHours() {
		String timeString = "10";
		Date result = new GregorianCalendar(1970, 0, 1, 10, 0).getTime();
		Date date = FrontendHelper.castStringToTime(timeString);
		assertEquals(result, date);
	}

	@Test
	public void testCastStringToTimeMinutes() {
		String timeString = "10:11";
		Date result = new GregorianCalendar(1970, 0, 1, 10, 11).getTime();
		Date date = FrontendHelper.castStringToTime(timeString);
		assertEquals(result, date);
	}

	@Test
	public void testCastStringToTimeSeconds() {
		String timeString = "10:11:12";
		Date result = new GregorianCalendar(1970, 0, 1, 10, 11, 12).getTime();
		Date date = FrontendHelper.castStringToTime(timeString);
		assertEquals(result, date);
	}

	@Test
	public void testCastIntegerToString() {
		int intValue = 1000;
		String actual = FrontendHelper.castIntegerToString(intValue);
		assertEquals("1.000", actual);
	}

	@Test
	public void testCastIntegerToStringStandard() {
		int intValue = 1000;
		String actual = FrontendHelper.castIntegerToStringStandard(intValue);
		assertEquals("1000", actual);
	}

	@Test
	public void testCastMinutesToTimeString30Minutes() {
		int minutes = 30;
		String result = FrontendHelper.castMinutesToTimeString(minutes);
		assertEquals("00:30", result);
	}

	@Test
	public void testCastMinutesToTimeString60Minutes() {
		int minutes = 60;
		String result = FrontendHelper.castMinutesToTimeString(minutes);
		assertEquals("01:00", result);
	}

	@Test
	public void testCastMinutesToTimeString90Minutes() {
		int minutes = 90;
		String result = FrontendHelper.castMinutesToTimeString(minutes);
		assertEquals("01:30", result);
	}

	@Test
	public void testCastMinutesToTimeString930Minutes() {
		int minutes = 930;
		String result = FrontendHelper.castMinutesToTimeString(minutes);
		assertEquals("15:30", result);
	}

	@Test
	public void testCastTimeStringToMinutes30Minutes1() {
		String time = "00:30";
		int result = FrontendHelper.castTimeStringToMinutes(time);
		assertEquals(30, result);
	}

	@Test
	public void testCastTimeStringToMinutes30Minutes2() {
		String time = "0:30";
		int result = FrontendHelper.castTimeStringToMinutes(time);
		assertEquals(30, result);
	}

	@Test
	public void testCastTimeStringToMinutes30Minutes3() {
		String time = ":30";
		int result = FrontendHelper.castTimeStringToMinutes(time);
		assertEquals(30, result);
	}

	@Test
	public void testCastTimeStringToMinutes30Minutes4() {
		String time = "30";
		int result = FrontendHelper.castTimeStringToMinutes(time);
		assertEquals(30, result);
	}

	@Test
	public void testCastTimeStringToMinutes60Minutes1() {
		String time = "01:00";
		int result = FrontendHelper.castTimeStringToMinutes(time);
		assertEquals(60, result);
	}

	@Test
	public void testCastTimeStringToMinutes60Minutes2() {
		String time = "1:00";
		int result = FrontendHelper.castTimeStringToMinutes(time);
		assertEquals(60, result);
	}

	@Test
	public void testCastTimeStringToMinutes60Minutes3() {
		String time = "1:0";
		int result = FrontendHelper.castTimeStringToMinutes(time);
		assertEquals(60, result);
	}

	@Test
	public void testCastTimeStringToMinutes60Minutes4() {
		String time = "1:";
		int result = FrontendHelper.castTimeStringToMinutes(time);
		assertEquals(60, result);
	}

	@Test
	public void testCastTimeStringToMinutes90Minutes() {
		String time = "01:30";
		int result = FrontendHelper.castTimeStringToMinutes(time);
		assertEquals(90, result);
	}

	@Test
	public void testCastTimeStringToMinutes930Minutes() {
		String time = "15:30";
		int result = FrontendHelper.castTimeStringToMinutes(time);
		assertEquals(930, result);
	}

	@Test
	public void testCastDateTimeKommaSeparatedStringToDate() {
		String timeString = "2008-03-31, 16:30";
		Date expected = new GregorianCalendar(2008, GregorianCalendar.MARCH, 31).getTime();
		assertEquals(expected, FrontendHelper.castStringToDate(timeString));
	}

	@Test
	public void testMatch() {
		String timeString = "2008-03-31, 16:30";
		assertEquals(
		    true,
		    timeString
		        .matches("^([0-9]{3,4}){1}[-]{1}(0?[0-9]|1[0-2]){1}[-]{1}(0?[1-9]|[12][0-9]|3[01]){1}"
		            + "[,] " + "(0?[0-9]|[1][0-9]|[2][0-3]){1}[:]{1}(0?[0-9]|[0-5][0-9]){1}" + "?"));
	}
}
