import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utils {

	/**
	 * Convert date to string with format "yyyy-MM-dd"
	 *
	 * @param sdate
	 *            object date
	 * @return string
	 */
	public static String dtoYYYY_MM_DD(Date sdate) {
		SimpleDateFormat formatedate = new SimpleDateFormat("yyyy-MM-dd");
		// formatedate.setLenient(false);
		return formatedate.format(sdate);
	}

	/**
	 * Convert date to string with format "dd/mm/yyyy"
	 *
	 * @param sdate
	 *            object date
	 * @return string
	 */
	public static String dtoDD_MM_YYYY(Date sdate) {
		SimpleDateFormat formatedate = new SimpleDateFormat("dd/MM/yyyy");
		// formatedate.setLenient(false);
		return formatedate.format(sdate);
	}

	/**
	 * Suma (o resta si dias <0) los días recibidos a la fecha
	 *
	 * @param fecha
	 * @param dias
	 *            : si >0 suma esa cantidad a fecha. Resta en caso de dias<0
	 * @return
	 */
	public static Date sumarRestarDiasFecha(Date fecha, int dias) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha);
		// numero de días a añadir, o restar en caso de días<0
		calendar.add(Calendar.DAY_OF_YEAR, dias);
		return calendar.getTime();
	}

}
