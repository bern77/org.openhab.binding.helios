/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.helios;

import org.openhab.binding.helios.internal.HeliosGenericBindingProvider.HeliosBindingConfig;
import org.openhab.core.binding.BindingProvider;

/**
 * @author Bernhard Bauer
 * @since 1.8.0
 */
public interface HeliosBindingProvider extends BindingProvider {
	
	HeliosBindingConfig getConfig(String itemName);

}
