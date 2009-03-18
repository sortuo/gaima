package gamebase;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.PhysicsSpace;
import com.physicalneuro.neuro.NeuroMovement;
import com.physicalneuro.neuro.NeuroMovement3d;

public abstract class PhysicalNeuroControl extends PhysicalNeuroGame {

	private NeuroMovement3d neuroMovement;

	@Override
	protected void update(float interpolation) {
		super.update(interpolation);

		if (null == neuroMovement) {
			neuroMovement = new NeuroMovement3d();
		}

		if (!pause) {
			float tpf = this.tpf;
			if (tpf > 0.2 || Float.isNaN(tpf)) {
				Logger
						.getLogger(PhysicsSpace.LOGGER_NAME)
						.warning(
								"Maximum physics update interval is 0.2 seconds - capped.");
				tpf = 0.2f;
			}
			// getPhysicsSpace().update( tpf * physicsSpeed );
			updateNeuroContacts();
		}
	}

	private ArrayList<NeuroMovement> neuroContacts = new ArrayList<NeuroMovement>();
	
	public void updateNeuroContacts() {
		for (NeuroMovement neuroObject : neuroContacts) {
			neuroObject.updateNetwork(getPhysicsSpace());
		}

	}

	public void addNeuroContact(DynamicPhysicsNode neuroObject) {
		NeuroMovement newContact = new NeuroMovement();
		newContact.setObject(neuroObject);
		neuroContacts.add(newContact);

	}
}
