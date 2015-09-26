package vexatos.manualtab.proxy;

/**
 * @author Vexatos
 */
public class ClientProxy extends CommonProxy {

	@Override
	public void throwBadConfigException(String icon) {
		//throw new vexatos.manualtab.util.client.BadConfigException(icon);
		super.throwBadConfigException(icon);
	}

	@Override
	public void throwBadConfigException(String icon, Throwable t) {
		//throw new vexatos.manualtab.util.client.BadConfigException(icon, t);
		super.throwBadConfigException(icon, t);
	}

}
