package vexatos.manualtab.proxy;

import vexatos.manualtab.util.BadConfigException;

/**
 * @author Vexatos
 */
public class CommonProxy {

	public void throwBadConfigException(String icon) {
		throw new BadConfigException(icon);
	}

	public void throwBadConfigException(String icon, Throwable t) {
		throw new BadConfigException(icon, t);
	}

}
