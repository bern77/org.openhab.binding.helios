package org.openhab.binding.helios.internal;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.LinkedHashMap;
import java.util.Iterator;

import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.StringType;

/**
 * This class represents a the possible variables of the Helios modbus.
 * 
 * @author Bernhard Bauer
 * @since 1.8.0
 */
public class HeliosVariableMap {

	/**
	 * The map holding the variable meta info
	 */
	private Map<String, HeliosVariable> vMap;
	
	/**
	 * Constructor to generate the variable map
	 */
	public HeliosVariableMap() {
		String[] descriptions;
		this.vMap = new HashMap<String, HeliosVariable>();
		
		this.vMap.put("article_description", new HeliosVariable(0, HeliosVariable.ACCESS_RW, 31, 20, HeliosVariable.TYPE_STRING));
		this.vMap.put("ref_no", new HeliosVariable(1, HeliosVariable.ACCESS_RW, 16, 12, HeliosVariable.TYPE_STRING));
		this.vMap.put("mac_address", new HeliosVariable(2, HeliosVariable.ACCESS_R, 18, 13, HeliosVariable.TYPE_STRING));
		this.vMap.put("language", new HeliosVariable(3, HeliosVariable.ACCESS_RW, 2, 5, HeliosVariable.TYPE_STRING));
		this.vMap.put("date", new HeliosVariable(4, HeliosVariable.ACCESS_RW, 10, 9, HeliosVariable.TYPE_STRING));
		this.vMap.put("time", new HeliosVariable(5, HeliosVariable.ACCESS_RW, 10, 9, HeliosVariable.TYPE_STRING));
		this.vMap.put("summer_winter", new HeliosVariable(6, HeliosVariable.ACCESS_RW, 1, 5, HeliosVariable.TYPE_INTEGER));
		this.vMap.put("auto_sw_update", new HeliosVariable(7, HeliosVariable.ACCESS_RW, 1, 5, HeliosVariable.TYPE_INTEGER));
		this.vMap.put("access_helios_portal", new HeliosVariable(8, HeliosVariable.ACCESS_RW, 1, 5, HeliosVariable.TYPE_INTEGER));
		
		descriptions = new String[] {
				"volt_fan_s1_ea",
				"volt_fan_s1_sa",
				"volt_fan_s2_ea",
				"volt_fan_s2_sa",
				"volt_fan_s3_ea",
				"volt_fan_s3_sa",
				"volt_fan_s4_ea",
				"volt_fan_s4_sa"
		};
		for (int i = 12; i <= 19; i++) this.vMap.put(descriptions[i - 12], new HeliosVariable(i, HeliosVariable.ACCESS_RW, 3, 6, HeliosVariable.TYPE_FLOAT, 1.6, 10.0));
		
		this.vMap.put("min_fan_stage", new HeliosVariable(20, HeliosVariable.ACCESS_RW, 1, 5, HeliosVariable.TYPE_INTEGER, 0, 1));
		this.vMap.put("kwl_be", new HeliosVariable(21, HeliosVariable.ACCESS_RW, 1, 5, HeliosVariable.TYPE_INTEGER, 0, 1));
		this.vMap.put("kwl_bec", new HeliosVariable(22, HeliosVariable.ACCESS_RW, 1, 5, HeliosVariable.TYPE_INTEGER, 0, 1));
		this.vMap.put("unit_config", new HeliosVariable(23, HeliosVariable.ACCESS_RW, 1, 5, HeliosVariable.TYPE_INTEGER, 0, 1));
		this.vMap.put("pre-heater_status", new HeliosVariable(24, HeliosVariable.ACCESS_RW, 1, 5, HeliosVariable.TYPE_INTEGER, 0, 1));
		
		for (int i = 25; i <= 32; i++) this.vMap.put("kwl_ftf_config" + (i - 25), new HeliosVariable(i, HeliosVariable.ACCESS_RW, 1, 5, HeliosVariable.TYPE_INTEGER, 1, 3));
		
		this.vMap.put("humidity_control_status", new HeliosVariable(33, HeliosVariable.ACCESS_RW, 1, 5, HeliosVariable.TYPE_INTEGER, 0, 2));
		this.vMap.put("humidity_control_set_value", new HeliosVariable(34, HeliosVariable.ACCESS_RW, 2, 5, HeliosVariable.TYPE_INTEGER, 20, 80));
		this.vMap.put("humidity_control_steps", new HeliosVariable(35, HeliosVariable.ACCESS_RW, 2, 5, HeliosVariable.TYPE_INTEGER, 5, 20));
		this.vMap.put("humidity_control_stop_time", new HeliosVariable(36, HeliosVariable.ACCESS_RW, 2, 5, HeliosVariable.TYPE_INTEGER, 0, 24));
		
		this.vMap.put("co2_control_status", new HeliosVariable(37, HeliosVariable.ACCESS_RW, 1, 5, HeliosVariable.TYPE_INTEGER, 0, 2));
		this.vMap.put("co2_control_set_value", new HeliosVariable(38, HeliosVariable.ACCESS_RW, 4, 6, HeliosVariable.TYPE_INTEGER, 300, 2000));
		this.vMap.put("co2_control_steps", new HeliosVariable(39, HeliosVariable.ACCESS_RW, 3, 6, HeliosVariable.TYPE_INTEGER, 50, 400));
		
		this.vMap.put("voc_control_status", new HeliosVariable(40, HeliosVariable.ACCESS_RW, 1, 5, HeliosVariable.TYPE_INTEGER, 0, 2));
		this.vMap.put("voc_control_set_value", new HeliosVariable(41, HeliosVariable.ACCESS_RW, 4, 6, HeliosVariable.TYPE_INTEGER, 300, 2000));
		this.vMap.put("voc_control_steps", new HeliosVariable(42, HeliosVariable.ACCESS_RW, 3, 6, HeliosVariable.TYPE_INTEGER, 50, 400));
		
		this.vMap.put("comfort_temp", new HeliosVariable(43, HeliosVariable.ACCESS_RW, 4, 6, HeliosVariable.TYPE_INTEGER, 10, 25));
		
		this.vMap.put("time_zone_diff_to_gmt", new HeliosVariable(51, HeliosVariable.ACCESS_RW, 3, 6, HeliosVariable.TYPE_INTEGER, -12, 14));
		this.vMap.put("date_format", new HeliosVariable(52, HeliosVariable.ACCESS_RW, 1, 5, HeliosVariable.TYPE_INTEGER, 0, 2));
		
		this.vMap.put("heat_exchanger_type", new HeliosVariable(53, HeliosVariable.ACCESS_RW, 1, 5, HeliosVariable.TYPE_INTEGER, 0, 3));
		
		this.vMap.put("party-mode_duration", new HeliosVariable(91, HeliosVariable.ACCESS_RW, 3, 6, HeliosVariable.TYPE_INTEGER, 5, 180));
		this.vMap.put("party-mode_fan_stage", new HeliosVariable(92, HeliosVariable.ACCESS_RW, 3, 5, HeliosVariable.TYPE_INTEGER, 0, 4));
		this.vMap.put("party-mode_remaining_time", new HeliosVariable(93, HeliosVariable.ACCESS_R, 3, 6, HeliosVariable.TYPE_INTEGER, 0, 180));
		this.vMap.put("party-mode", new HeliosVariable(94, HeliosVariable.ACCESS_RW, 1, 5, HeliosVariable.TYPE_INTEGER, 0, 1));
		
		this.vMap.put("standby-mode_duration", new HeliosVariable(96, HeliosVariable.ACCESS_RW, 3, 6, HeliosVariable.TYPE_INTEGER, 5, 180));
		this.vMap.put("standby-mode_fan_stage", new HeliosVariable(97, HeliosVariable.ACCESS_RW, 1, 5, HeliosVariable.TYPE_INTEGER, 0, 4));
		this.vMap.put("standby-mode_remaining_time", new HeliosVariable(98, HeliosVariable.ACCESS_R, 3, 6, HeliosVariable.TYPE_INTEGER, 0, 180));
		this.vMap.put("standby-mode", new HeliosVariable(99, HeliosVariable.ACCESS_RW, 1, 5, HeliosVariable.TYPE_INTEGER, 0, 1));
		
		this.vMap.put("operating_mode", new HeliosVariable(101, HeliosVariable.ACCESS_RW, 1, 5, HeliosVariable.TYPE_INTEGER, 0, 1));
		this.vMap.put("fan_stage", new HeliosVariable(102, HeliosVariable.ACCESS_RW, 3, 5, HeliosVariable.TYPE_INTEGER, 0, 4));
		this.vMap.put("percentage_fan_stage", new HeliosVariable(103, HeliosVariable.ACCESS_R, 3, 6, HeliosVariable.TYPE_INTEGER, 0, 100));
		
		this.vMap.put("temperature_outside_air", new HeliosVariable(104, HeliosVariable.ACCESS_R, 7, 8, HeliosVariable.TYPE_INTEGER, -27, 9998));
		this.vMap.put("temperature_supply_air", new HeliosVariable(105, HeliosVariable.ACCESS_R, 7, 8, HeliosVariable.TYPE_INTEGER, -27, 9998));
		this.vMap.put("temperature_outgoing_air", new HeliosVariable(106, HeliosVariable.ACCESS_R, 7, 8, HeliosVariable.TYPE_INTEGER, -27, 9998));
		this.vMap.put("temperature_extract_air", new HeliosVariable(107, HeliosVariable.ACCESS_R, 7, 8, HeliosVariable.TYPE_INTEGER, -27, 9998));
		
		this.vMap.put("vhz_duct_sensor", new HeliosVariable(108, HeliosVariable.ACCESS_R, 7, 8, HeliosVariable.TYPE_INTEGER, -27, 9998));
		this.vMap.put("nhz_return_sensor", new HeliosVariable(110, HeliosVariable.ACCESS_R, 7, 8, HeliosVariable.TYPE_INTEGER, -27, 9998));
		
		for (int i = 111; i <= 118; i++) this.vMap.put("ext_sensor_kwl-ftf_humid_" + (i - 110), new HeliosVariable(i, HeliosVariable.ACCESS_R, 4, 6, HeliosVariable.TYPE_INTEGER, 0, 9998));
		for (int i = 119; i <= 126; i++) this.vMap.put("ext_sensor_kwl-ftf_temp_" + (i - 118), new HeliosVariable(i, HeliosVariable.ACCESS_R, 7, 8, HeliosVariable.TYPE_INTEGER, -27, 9998));
		for (int i = 128; i <= 135; i++) this.vMap.put("ext_sensor_kwl-co2_" + (i - 127), new HeliosVariable(i, HeliosVariable.ACCESS_R, 4, 6, HeliosVariable.TYPE_INTEGER, 0, 9998));
		for (int i = 136; i <= 143; i++) this.vMap.put("ext_sensor_kwl-voc_" + (i - 135), new HeliosVariable(i, HeliosVariable.ACCESS_R, 4, 6, HeliosVariable.TYPE_INTEGER, 0, 9998));
		
		this.vMap.put("nhz_duct_sensor", new HeliosVariable(146, HeliosVariable.ACCESS_R, 7, 8, HeliosVariable.TYPE_INTEGER, -27, 9998));
		this.vMap.put("week_profile_nhz", new HeliosVariable(201, HeliosVariable.ACCESS_RW, 1, 5, HeliosVariable.TYPE_INTEGER, 0, 6));
		
		this.vMap.put("ser_no", new HeliosVariable(303, HeliosVariable.ACCESS_RW, 16, 12, HeliosVariable.TYPE_STRING));
		this.vMap.put("prod_code", new HeliosVariable(304, HeliosVariable.ACCESS_RW, 13, 11, HeliosVariable.TYPE_STRING));
		
		this.vMap.put("supply_air_rpm", new HeliosVariable(348, HeliosVariable.ACCESS_R, 4, 6, HeliosVariable.TYPE_INTEGER, 0, 9999));
		this.vMap.put("extract_air_rpm", new HeliosVariable(349, HeliosVariable.ACCESS_R, 4, 6, HeliosVariable.TYPE_INTEGER, 0, 9999));
		this.vMap.put("logout", new HeliosVariable(403, HeliosVariable.ACCESS_W, 1, 5, HeliosVariable.TYPE_INTEGER, 1, 1));
		
		this.vMap.put("holiday_programme", new HeliosVariable(601, HeliosVariable.ACCESS_RW, 1, 5, HeliosVariable.TYPE_INTEGER, 0, 2));
		this.vMap.put("holiday_programme_fan_stage", new HeliosVariable(602, HeliosVariable.ACCESS_RW, 1, 5, HeliosVariable.TYPE_INTEGER, 1, 4));
		this.vMap.put("holiday_programme_start", new HeliosVariable(603, HeliosVariable.ACCESS_RW, 10, 9, HeliosVariable.TYPE_STRING));
		this.vMap.put("holiday_programme_end", new HeliosVariable(604, HeliosVariable.ACCESS_RW, 10, 9, HeliosVariable.TYPE_STRING));
		this.vMap.put("holiday_programme_interval", new HeliosVariable(605, HeliosVariable.ACCESS_RW, 2, 5, HeliosVariable.TYPE_INTEGER, 1, 24));
		this.vMap.put("holiday_programme_activation_time", new HeliosVariable(606, HeliosVariable.ACCESS_RW, 3, 6, HeliosVariable.TYPE_INTEGER, 5, 300));
		
		this.vMap.put("vhz_type", new HeliosVariable(1010, HeliosVariable.ACCESS_RW, 1, 5, HeliosVariable.TYPE_INTEGER, 1, 4));
		this.vMap.put("function_type_kwl-em", new HeliosVariable(1017, HeliosVariable.ACCESS_RW, 1, 5, HeliosVariable.TYPE_INTEGER, 1, 2));
		this.vMap.put("run-on_time_vhz_nhz", new HeliosVariable(1019, HeliosVariable.ACCESS_RW, 3, 6, HeliosVariable.TYPE_INTEGER, 60, 120));
		
		this.vMap.put("external_contact", new HeliosVariable(1020, HeliosVariable.ACCESS_RW, 1, 5, HeliosVariable.TYPE_INTEGER, 1, 6));
		this.vMap.put("error_output_function", new HeliosVariable(1021, HeliosVariable.ACCESS_RW, 1, 5, HeliosVariable.TYPE_INTEGER, 1, 2));
		
		this.vMap.put("filter_change", new HeliosVariable(1031, HeliosVariable.ACCESS_RW, 1, 5, HeliosVariable.TYPE_INTEGER, 0, 1));
		this.vMap.put("filter_change_interval", new HeliosVariable(1032, HeliosVariable.ACCESS_RW, 2, 5, HeliosVariable.TYPE_INTEGER, 0, 12));
		this.vMap.put("filter_change_remaining_time", new HeliosVariable(1033, HeliosVariable.ACCESS_R, 10, 9, HeliosVariable.TYPE_INTEGER, 2, 4294967295L));
		
		this.vMap.put("bypass_room_temp", new HeliosVariable(1035, HeliosVariable.ACCESS_RW, 2, 5, HeliosVariable.TYPE_INTEGER, 10, 40));
		this.vMap.put("bypass_min_outside_temp", new HeliosVariable(1036, HeliosVariable.ACCESS_RW, 2, 5, HeliosVariable.TYPE_INTEGER, 5, 20));
		
		this.vMap.put("factory_setting_wzu", new HeliosVariable(1037, HeliosVariable.ACCESS_RW, 2, 5, HeliosVariable.TYPE_INTEGER, 3, 10));
		this.vMap.put("factory_reset", new HeliosVariable(1041, HeliosVariable.ACCESS_RW, 1, 5, HeliosVariable.TYPE_INTEGER, 1, 1));
		
		this.vMap.put("supply_air_fan_stage", new HeliosVariable(1050, HeliosVariable.ACCESS_RW, 1, 5, HeliosVariable.TYPE_INTEGER, 1, 4));
		this.vMap.put("extract_air_fan_stage", new HeliosVariable(1051, HeliosVariable.ACCESS_RW, 1, 5, HeliosVariable.TYPE_INTEGER, 1, 4));
		
		int stage = 0;
		for (int i = 1061; i <= 1065; i++) {
			this.vMap.put("fan_stages_stepped_range" + stage + "-" + (stage + 2), new HeliosVariable(i, HeliosVariable.ACCESS_RW, 1, 5, HeliosVariable.TYPE_INTEGER, 0, 2)); // range for values might be different - there seems to be a mistake in the Helios document
			stage = stage + 2; 
		}
		
		this.vMap.put("offset_extract_air", new HeliosVariable(1066, HeliosVariable.ACCESS_RW, 10, 9, HeliosVariable.TYPE_FLOAT));
		this.vMap.put("fan_stages_stepped_vs_1-10v", new HeliosVariable(1068, HeliosVariable.ACCESS_RW, 1, 5, HeliosVariable.TYPE_INTEGER, 0, 1));
		
		for (int i = 1071; i <= 1078; i++) this.vMap.put("sensor_name_humidity+temp" + (i - 1070), new HeliosVariable(i, HeliosVariable.ACCESS_RW, 15, 12, HeliosVariable.TYPE_STRING));
		for (int i = 1081; i <= 1088; i++) this.vMap.put("sensor_name_co2" + (i - 1080), new HeliosVariable(i, HeliosVariable.ACCESS_RW, 15, 12, HeliosVariable.TYPE_STRING));
		for (int i = 1091; i <= 1098; i++) this.vMap.put("sensor_name_voc" + (i - 1090), new HeliosVariable(i, HeliosVariable.ACCESS_RW, 15, 12, HeliosVariable.TYPE_STRING));
		
		this.vMap.put("sw_version", new HeliosVariable(1101, HeliosVariable.ACCESS_R, 5, 7, HeliosVariable.TYPE_FLOAT, 0, 99.99));
		
		descriptions = new String[] {
				"operating_hours_supply_air_vent",
				"operating_hours_extract_air_vent",
				"operating_hours_vhz",
				"operating_hours_nhz"
		};
		for (int i = 1103; i <= 1106; i++) this.vMap.put(descriptions[i - 1103], new HeliosVariable(i, HeliosVariable.ACCESS_R, 10, 9, HeliosVariable.TYPE_INTEGER, 0, 4294967295L));
		
		descriptions = new String[] {
				"output_power_vhz_percent",
				"output_power_nhz_percent"
		};
		for (int i = 1108; i <= 1109; i++) this.vMap.put(descriptions[i - 1108], new HeliosVariable(i, HeliosVariable.ACCESS_R, 10, 9, HeliosVariable.TYPE_INTEGER, 0, 4294967295L));

		// TODO: codings?
		this.vMap.put("reset_flag", new HeliosVariable(1120, HeliosVariable.ACCESS_R, 1, 5, HeliosVariable.TYPE_INTEGER, 1, 1));
		this.vMap.put("errors", new HeliosVariable(1123, HeliosVariable.ACCESS_R, 10, 9, HeliosVariable.TYPE_INTEGER, 0, 4294967295L));
		this.vMap.put("warnings", new HeliosVariable(1124, HeliosVariable.ACCESS_R, 3, 6, HeliosVariable.TYPE_INTEGER, 0, 255));
		this.vMap.put("infos", new HeliosVariable(1125, HeliosVariable.ACCESS_R, 3, 6, HeliosVariable.TYPE_INTEGER, 0, 255));
		this.vMap.put("number_of_errors", new HeliosVariable(1300, HeliosVariable.ACCESS_R, 2, 5, HeliosVariable.TYPE_INTEGER, 0, 32));
		this.vMap.put("number_of_warnings", new HeliosVariable(1301, HeliosVariable.ACCESS_R, 1, 5, HeliosVariable.TYPE_INTEGER, 0, 8));
		this.vMap.put("number_of_infos", new HeliosVariable(1302, HeliosVariable.ACCESS_R, 1, 5, HeliosVariable.TYPE_INTEGER, 0, 8));
		this.vMap.put("errors_string", new HeliosVariable(1303, HeliosVariable.ACCESS_R, 32, 20, HeliosVariable.TYPE_STRING));
		this.vMap.put("warnings_string", new HeliosVariable(1304, HeliosVariable.ACCESS_R, 8, 8, HeliosVariable.TYPE_STRING));
		this.vMap.put("infos_string", new HeliosVariable(1305, HeliosVariable.ACCESS_R, 8, 8, HeliosVariable.TYPE_STRING));
		this.vMap.put("status_flags", new HeliosVariable(1306, HeliosVariable.ACCESS_R, 32, 20, HeliosVariable.TYPE_STRING));
		
		for (int i = 2020; i <= 2027; i++) this.vMap.put("kw_ftf_config" + (i - 2019), new HeliosVariable(i, HeliosVariable.ACCESS_R, 1, 5, HeliosVariable.TYPE_INTEGER, 0, 1));
		
		this.vMap.put("global_manual_web-update", new HeliosVariable(2013, HeliosVariable.ACCESS_RW, 1, 5, HeliosVariable.TYPE_INTEGER, 1, 1));
		this.vMap.put("portal_latest_error", new HeliosVariable(2014, HeliosVariable.ACCESS_R, 3, 6, HeliosVariable.TYPE_INTEGER, 1, 255));
		this.vMap.put("clear_error", new HeliosVariable(2015, HeliosVariable.ACCESS_W, 1, 5, HeliosVariable.TYPE_INTEGER, 1, 1));		
	}
	
	/**
	 * Returns the variable
	 * @param variableName Variable name
	 * @return The variable
	 */
	public HeliosVariable getVariable(String variableName) {
		return this.vMap.get(variableName);
	}
	
	private Map getSortedMap()
	{
		List list = new LinkedList(this.vMap.entrySet());
		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((Comparable) ((Map.Entry) (o1)).getValue())
							.compareTo(((Map.Entry) (o2)).getValue());
			}
		});
	 
		Map sortedMap = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}
	
	/**
	 * Returns an HTML formatted list of variables
	 * @return The HTML table
	 */
	public String getHtmlList() {
		String html = new String();
		html += "<table class=\"helios\">";
		html += "<tr>";
		html += "<th>Description</th>";
		html += "<th>Access</th>";
		html += "<th>Type</th>";
		html += "<th>Count</th>";
		html += "<th>Variable</th>";
		html += "<th>Min Value</th>";
		html += "<th>Max Value</th>";
		html += "</tr>";
		int i = 0;
		for (Map.Entry<String, HeliosVariable> e : ((Map<String, HeliosVariable>) this.getSortedMap()).entrySet()) {
			html += "<tr class=\"" + (i % 2 == 0 ? "even" : "odd") + "\">";
			// description
			html += "<td>" + e.getKey() + "</td>";

			HeliosVariable v = e.getValue();
			// Access
			html += "<td>";
			switch(v.getAccess()) {
				case HeliosVariable.ACCESS_R:
					html += "R";
					break;
				case HeliosVariable.ACCESS_W:
					html += "W";
					break;
				case HeliosVariable.ACCESS_RW:
					html += "RW";
					break;
				default:
					html += "N/A";
					break;
			}
			html += "</td>";
			// Type
			html += "<td>Char[" + v.getLength() + "]</td>";
			// Count
			html += "<td>" + v.getCount() + "</td>";
			// Variable
			html += "<td>" + v.getVariableString() + "</td>";
			// Min Value
			html += "<td>" + (v.getMinVal() == null ? "-" : v.getMinVal()) + "</td>";
			// Max Value
			html += "<td>" + (v.getMaxVal() == null ? "-" : v.getMaxVal()) + "</td>";
			html += "</tr>";
		}
		html += "</table>";
		return html;
	}
}
