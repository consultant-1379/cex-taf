import com.ericsson.oss.common.cm.comparemo.ui.CompareMoHandler;
import com.ericsson.oss.common.cm.comparemo.ui.ICompareMoHandler;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class SendToCompareMo{

	ICompareMoHandler compareMOHandler= new CompareMoHandler();
	Collection<Object> itemCollection = new ArrayList<Object>();

	public def sendCellToCompareMo(String cell){

		itemCollection.add(cell);

		try {
			compareMOHandler.sendToCompareMoView(itemCollection);
			waitForTopologyUpdate(3000);
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "OK";
	}
	
	protected void waitForTopologyUpdate(final int milliseconds){
		try {
			sleep(milliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}