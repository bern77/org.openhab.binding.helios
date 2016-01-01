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

import org.openhab.binding.helios.HeliosBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.types.State;
import org.openhab.core.types.Type;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;


/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author Bernhard Bauer
 * @since 1.8.0
 */
public class HeliosGenericBindingProvider extends AbstractGenericBindingProvider implements HeliosBindingProvider {

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "helios";
	}

	/**
	 * @{inheritDoc}
	 */
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		//if (!(item instanceof SwitchItem || item instanceof DimmerItem)) {
		//	throw new BindingConfigParseException("item '" + item.getName()
		//			+ "' is of type '" + item.getClass().getSimpleName()
		//			+ "', only Switch- and DimmerItems are allowed - please check your *.items configuration");
		//}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		HeliosBindingConfig config = new HeliosBindingConfig();
		config.variableName = bindingConfig;
		config.dataTypes = item.getAcceptedDataTypes();
		addBindingConfig(item, config);
	}
	
	
	/**
	 * This is a helper class holding binding specific configuration details
	 * 
	 * @author Bernhard Bauer
	 * @since 1.8.0
	 */
	public class HeliosBindingConfig implements BindingConfig {
		public String variableName;
		public List<Class<? extends State>> dataTypes;
	}
	
	/**
	 * Returns the specified item's binding configuration
	 */
	public HeliosBindingConfig getConfig(String itemName) {
		return (HeliosBindingConfig) this.bindingConfigs.get(itemName);
	}
	
}
