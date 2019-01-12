/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.helios.internal;

import java.util.List;
import java.util.Map;

import org.openhab.binding.helios.*;
import org.openhab.binding.helios.internal.HeliosGenericBindingProvider.HeliosBindingConfig;
import org.apache.commons.lang.StringUtils;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.Type;
import org.openhab.core.library.types.*;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Implement this class if you are going create an actively polling service
 * like querying a Website/Device.
 * 
 * @author Bernhard Bauer
 * @since 1.8.0
 */
public class HeliosBinding extends AbstractActiveBinding<HeliosBindingProvider> {

	private static final Logger logger = 
		LoggerFactory.getLogger(HeliosBinding.class);

	/**
	 * The BundleContext. This is only valid when the bundle is ACTIVE. It is set in the activate()
	 * method and must not be accessed anymore once the deactivate() method was called or before activate()
	 * was called.
	 */
	private BundleContext bundleContext;

	
	/** 
	 * the refresh interval which is used to poll values from the Helios
	 * server (optional, defaults to 60000ms)
	 */
	private long refreshInterval = 60000;
	
	
	/**
	 * The IP Address of the Helios device
	 */
	private String host;
	
	/**
	 * The port of the Helios device
	 */
	private int port;
	
	/**
	 * The unit address of the Helios device
	 */
	private int unit;
	
	/**
	 * The start address when reading/writing from/to the modbus
	 */
	private int startAddress;
	
	
	public HeliosBinding() {
	}
		
	
	/**
	 * Called by the SCR to activate the component with its configuration read from CAS
	 * 
	 * @param bundleContext BundleContext of the Bundle that defines this component
	 * @param configuration Configuration properties for this component obtained from the ConfigAdmin service
	 */
	public void activate(final BundleContext bundleContext, final Map<String, Object> configuration) {
		this.bundleContext = bundleContext;

		String refreshIntervalString = (String) configuration.get("refresh");
		if (StringUtils.isNotBlank(refreshIntervalString)) {
			this.refreshInterval = Long.parseLong(refreshIntervalString);
		}

		this.host = (String) configuration.get("host");
		
		// optional config settings
		String c;
		// port
		c = (String) configuration.get("port"); 
		this.port = c == null ? HeliosCommunicator.DEFAULT_PORT : Integer.parseInt(c);
		// unit ID
		c = (String) configuration.get("unit");
		this.unit = c == null ? HeliosCommunicator.DEFAULT_UNIT : Integer.parseInt(c);
		// start address
		c = (String) configuration.get("startAddress");
		this.startAddress = c == null ? HeliosCommunicator.DEFAULT_START_ADDRESS : Integer.parseInt(c);		

		setProperlyConfigured(true);
	}
	
	/**
	 * Called by the SCR when the configuration of a binding has been changed through the ConfigAdmin service.
	 * @param configuration Updated configuration properties
	 */
	public void modified(final Map<String, Object> configuration) {
		this.activate(this.bundleContext, configuration);
	}
	
	/**
	 * Called by the SCR to deactivate the component when either the configuration is removed or
	 * mandatory references are no longer satisfied or the component has simply been stopped.
	 * @param reason Reason code for the deactivation:<br>
	 * <ul>
	 * <li> 0 ... Unspecified
     * <li> 1 ... The component was disabled
     * <li> 2 ... A reference became unsatisfied
     * <li> 3 ... A configuration was changed
     * <li> 4 ... A configuration was deleted
     * <li> 5 ... The component was disposed
     * <li> 6 ... The bundle was stopped
     * </ul>
	 */
	public void deactivate(final int reason) {
		this.bundleContext = null;
		
	}

	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected String getName() {
		return "Helios Refresh Service";
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void execute() {
		HeliosCommunicator heliosComm = new HeliosCommunicator(this.host, this.port, this.unit, this.startAddress);
		for (HeliosBindingProvider provider : providers) {
			for (String item : provider.getItemNames()) {
				HeliosBindingConfig config = provider.getConfig(item);
				HeliosVariableMap vMap = new HeliosVariableMap();
				try {
					String v = heliosComm.getValue(config.variableName);
					eventPublisher.postUpdate(item, (State) toType(v, vMap.getVariable(config.variableName).getType(), config.dataTypes));
				} catch (HeliosException e) {
					this.logger.info(e.getMessage());
				}
			}
		}
	}
	
	/**
	 * Maps the Helios value to the corresponding openHAB command
	 * @param heliosValue The Helios value
	 * @param heliosType The Helios data type
	 * @param dataTypes The allows data types of the openHAB item
	 * @return The command
	 */
	private Type toType(String heliosValue, int heliosType, List<Class<? extends State>> dataTypes) {
		switch (heliosType) {
			case HeliosVariable.TYPE_INTEGER:
			case HeliosVariable.TYPE_FLOAT:
				if (dataTypes.contains(OnOffType.class)) return heliosValue.equals("1") ? OnOffType.ON : OnOffType.OFF;
				else return DecimalType.valueOf(heliosValue);
			case HeliosVariable.TYPE_STRING:
				return StringType.valueOf(heliosValue);
			default:
				return null;
		}
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		HeliosCommunicator heliosComm = new HeliosCommunicator(this.host, this.port, this.unit, this.startAddress);
		for (HeliosBindingProvider provider : providers) {
			if (provider.providesBindingFor(itemName)) {
				HeliosBindingConfig config = provider.getConfig(itemName);
				try {
					heliosComm.setValue(config.variableName, this.toHeliosValue(command));
				} catch (HeliosException e) {
					this.logger.info(e.getMessage());
				}
			}
		}
	}
	
	/**
	 * Maps the openHAB command to the corresponding Helios value
	 * @param command The command
	 * @return The Helios value
	 */
	private String toHeliosValue(Command command) {

		if (command instanceof OnOffType) {
			return command == OnOffType.ON ? "1" : "0";
		}
		
		return command.toString();
	}
	
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void internalReceiveUpdate(String itemName, State newState) {
		// TODO
		this.logger.info("internalReceiveUpdate called for HeliosBinding: no implementation available (use internalReceiveCommand instead)");
	}

}
