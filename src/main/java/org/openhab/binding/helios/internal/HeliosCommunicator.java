package org.openhab.binding.helios.internal;

import com.ghgande.j2mod.modbus.io.ModbusTCPTransaction;
import com.ghgande.j2mod.modbus.io.ModbusTCPTransport;
import com.ghgande.j2mod.modbus.msg.ReadMultipleRegistersRequest;
import com.ghgande.j2mod.modbus.msg.ReadMultipleRegistersResponse;
import com.ghgande.j2mod.modbus.msg.WriteMultipleRegistersRequest;
import com.ghgande.j2mod.modbus.msg.WriteMultipleRegistersResponse;
import com.ghgande.j2mod.modbus.net.TCPMasterConnection;
import com.ghgande.j2mod.modbus.procimg.Register;
import com.ghgande.j2mod.modbus.procimg.SimpleRegister;

import java.net.InetAddress;
import java.util.Arrays;


/**
 * This class is responsible for communicating with the Helios modbus.
 * 
 * @author Bernhard Bauer
 * @since 1.8.0
 */
public class HeliosCommunicator {
	
	/**
	 * Default port
	 */
	public static final int DEFAULT_PORT = 502;
	
	/**
	 * Default unit ID
	 */
	public static final int DEFAULT_UNIT = 180;
	
	/**
	 * Default start address
	 */
	public static final int DEFAULT_START_ADDRESS = 1;
	
	/**
	 * The host's IP address
	 */
	private String host;
	
	/**
	 * The host's port
	 */
	private int port;
	
	/**
	 * The hosts modbus unit ID
	 */
	private int unit;
	
	/**
	 * The starting address
	 */
	private int startAddress;
	
	/**
	 * The variable map
	 */
	private HeliosVariableMap vMap;
	
	private TCPMasterConnection conn;
	
	/**
	 * Constructor to set the member variables
	 * @param host IP Address
	 * @param port Port (502)
	 * @param address Modbus address (180)
	 * @param startAddress Start address (1)
	 */
	public HeliosCommunicator(String host, int port, int unit, int startAddress) {
		this.host = host;
		this.port = port;
		this.unit = unit;
		this.startAddress = startAddress;
		this.vMap = new HeliosVariableMap();
		try {
			this.conn = new TCPMasterConnection(InetAddress.getByName(host));
			this.conn.setPort(port);
			this.conn.connect();
			//((ModbusTCPTransport) this.conn.getModbusTransport()).setHeadless();
		} catch (Exception e) {
			System.err.println(e.getClass().toString());
		}
	}
	
	/**
	 * Constructor to set the member variables (using default values for anything but the host IP address
	 * @param host IP Address
	 */
	public HeliosCommunicator(String host) {
		this(host, HeliosCommunicator.DEFAULT_PORT, HeliosCommunicator.DEFAULT_UNIT, HeliosCommunicator.DEFAULT_START_ADDRESS);
	}
	
	
	/**
	 * Sets a variable in the Helios device
	 * @param variableName The variable name
	 * @param value The new value
	 * @return The value if the transaction succeeded, <tt>null</tt> otherwise
	 * @throws HeliosException 
	 */
	public String setValue(String variableName, String value) throws HeliosException {
		
		HeliosVariable v = this.vMap.getVariable(variableName);
		
		// check range if applicable
		if ((v.getAccess() == HeliosVariable.ACCESS_W) || (v.getAccess() == HeliosVariable.ACCESS_RW)) { // changing value is allowed
			
			boolean inAllowedRange = false;
			
			if (v.getMinVal() != null) { // min and max value are set
				if (v.getMinVal() instanceof Integer) {
					inAllowedRange = (((Integer) v.getMinVal()).intValue() <= Integer.parseInt(value));
					if (v.getMaxVal() instanceof Integer) {
						inAllowedRange = inAllowedRange && (((Integer) v.getMaxVal()).intValue() >= Integer.parseInt(value));
					} else { // Long
						inAllowedRange = inAllowedRange && (((Long) v.getMaxVal()).longValue() >= Long.parseLong(value));
					}
				} else if (v.getMinVal() instanceof Double) {
					inAllowedRange = (((Double) v.getMinVal()).doubleValue() <= Double.parseDouble(value))
							&& (((Double) v.getMaxVal()).doubleValue() >= Double.parseDouble(value));
				}
			} else {
				inAllowedRange = true; // no range to check
			}
			
			if (inAllowedRange) {
				String payload = v.getVariableString() + "=" + value;
				
				// create request
				WriteMultipleRegistersRequest request = new WriteMultipleRegistersRequest(this.startAddress, this.preparePayload(payload));
				request.setUnitID(this.unit);
				
				// communicate with modbus
				try {
					ModbusTCPTransaction trans = new ModbusTCPTransaction(this.conn);
					
					// send request
					trans.setRequest(request);
					trans.setReconnecting(true);
					trans.execute();
					
					return value;
					
				} catch (Exception e) {
					throw new HeliosException("Communication with Helios device failed");
				}
			} else {
				throw new HeliosException("Value is outside of allowed range");
			}
		} else {
			throw new HeliosException("Variable is read-only");
		}
	}
	
	/**
	 * Read a variable from the Helios device 
	 * @param variableName The variable name
	 * @return The value
	 * @throws HeliosException 
	 */
	public String getValue(String variableName) throws HeliosException {
		
		HeliosVariable v = this.vMap.getVariable(variableName);
		String payload = v.getVariableString();
		
		// create request 1
		WriteMultipleRegistersRequest request1 = new WriteMultipleRegistersRequest(this.startAddress, this.preparePayload(payload));
		request1.setUnitID(this.unit);
		
		// create request 2
		ReadMultipleRegistersRequest request2 = new ReadMultipleRegistersRequest(this.startAddress, v.getCount());
		request2.setUnitID(this.unit);
		
		// communicate with modbus
		try {
			ModbusTCPTransaction trans = new ModbusTCPTransaction(this.conn);
			
			// send request 1
			trans.setRequest(request1);
			trans.setReconnecting(true);
			trans.execute();
			
			// receive response
			//WriteMultipleRegistersResponse response1 = (WriteMultipleRegistersResponse) trans.getResponse();
			
			// send request 2
			trans.setRequest(request2);
			trans.setReconnecting(true);
			trans.execute();
			
			// receive response
			ReadMultipleRegistersResponse response2 = (ReadMultipleRegistersResponse) trans.getResponse();
			String value = decodeResponse(response2.getRegisters());
			return value;
			
		} catch (Exception e) {
			throw new HeliosException("Communication with Helios device failed");
		}
	}
	
	
	/**
	 * Prepares the payload for the request
	 * @param payload The String representation of the payload
	 * @return The Register representation of the payload
	 */
	private Register[] preparePayload(String payload) {
		
		// determine number of registers
		int l = (payload.length() + 1) / 2; // +1 because we need to include at least one termination symbol 0x00
		if ((payload.length() + 1) % 2 != 0) l++;

		Register reg[] = new Register[l];
		byte[] b = payload.getBytes();
		int ch = 0;
		for (int i = 0; i < reg.length; i++) {
			byte b1 = ch < b.length ? b[ch] : (byte) 0x00; // terminate with 0x00 if at the end of the payload
			ch++;
			byte b2 = ch < b.length ? b[ch] : (byte) 0x00;
			ch++;
			reg[i] = new SimpleRegister(b1, b2);
		}
		return reg;
	}
	
	/**
	 * Decodes the Helios device' response and returns the actual value of the variable
	 * @param response The registers received from the Helios device
	 * @return The value or <tt>null</tt> if an error occurred
	 */
	private String decodeResponse(Register[] response) {
		byte[] b = new byte[response.length * 2];
		int actSize = 0; // track the actual size of the useable array (excluding any 0x00 characters)
		for (int i = 0; i < response.length; i++) {
			byte[] reg = response[i].toBytes();
			if (reg.length == 2) { // only add to the array if it's a useable character
				if (reg[0] != 0x00) b[actSize++] = reg[0];
				if (reg[1] != 0x00) b[actSize++] = reg[1];
			}
		}
		b = Arrays.copyOf(b, actSize); // before creating a string of it the array needs to be truncated
		String r = new String(b);
		String[] parts = r.split("="); // remove the part "vXXXX=" from the string
		if (parts.length == 2) return parts[1];
		else return null;
	}
}
