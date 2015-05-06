package org.tripledip.dipcloud.local.behavior;

import org.tripledip.dipcloud.local.contract.Crudable;
import org.tripledip.dipcloud.local.contract.DipAccess;
import org.tripledip.dipcloud.local.contract.Smashable;
import org.tripledip.dipcloud.local.contract.SmashableBuilder;
import org.tripledip.dipcloud.local.model.Atom;
import org.tripledip.dipcloud.local.model.Molecule;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Ben on 2/23/15.
 */
public class SuperDip implements DipAccess {
    private final Crudable<Atom> nimbase;
    private final Atomizer atomizer;
    private final ScrudNotifier<Atom> idListeners;
    private final ScrudNotifier<Molecule> channelListeners;
    private final ScrudNotifier<Smashable> smashableListeners;
    private final Map<String, Smashable> smashableTemplates;

    public SuperDip(Crudable<Atom> nimbase) {
        this.nimbase = nimbase;
        this.atomizer = new Atomizer(nimbase);
        this.idListeners = new ScrudNotifier<>();
        this.channelListeners = new ScrudNotifier<>();
        this.smashableListeners = new ScrudNotifier<>();
        this.smashableTemplates = new HashMap<>();
    }

    protected void add(Molecule molecule) {
        molecule.setAction(Molecule.Action.ADD);
        Set<Atom> changed = atomizer.add(molecule);
        if (!changed.isEmpty()) {
            molecule.addOrReplaceAll(changed);
            notifyAtomsAdded(molecule, changed);
        }
    }

    protected void notifyAtomsAdded(Molecule molecule, Set<Atom> changed) {
        for (Atom atom : changed) {
            idListeners.notifyAdded(atom.getId(), atom);
        }
        channelListeners.notifyAdded(molecule.getChannel(), molecule);

        Smashable smashable = unsmashWithBuilder(molecule);
        if (null != smashable) {
            smashableListeners.notifyAdded(smashable.getChannel(), smashable);
        }
    }

    protected void update(Molecule molecule) {
        molecule.setAction(Molecule.Action.UPDATE);
        Set<Atom> changed = atomizer.update(molecule);
        if (!changed.isEmpty()) {
            molecule.addOrReplaceAll(changed);
            notifyAtomsUpdated(molecule, changed);
        }
    }

    protected void notifyAtomsUpdated(Molecule molecule, Set<Atom> changed) {
        for (Atom atom : changed) {
            idListeners.notifyUpdated(atom.getId(), atom);
        }
        channelListeners.notifyUpdated(molecule.getChannel(), molecule);

        Smashable smashable = unsmashWithBuilder(molecule);
        if (null != smashable) {
            smashableListeners.notifyUpdated(smashable.getChannel(), smashable);
        }
    }

    protected void remove(Molecule molecule) {
        molecule.setAction(Molecule.Action.REMOVE);
        Set<Atom> changed = atomizer.remove(molecule);
        if (!changed.isEmpty()) {
            molecule.addOrReplaceAll(changed);
            notifyAtomsRemoved(molecule, changed);
        }
    }

    protected void notifyAtomsRemoved(Molecule molecule, Set<Atom> changed) {
        for (Atom atom : changed) {
            idListeners.notifyRemoved(atom.getId(), atom);
        }
        channelListeners.notifyRemoved(molecule.getChannel(), molecule);

        Smashable smashable = unsmashWithBuilder(molecule);
        if (null != smashable) {
            smashableListeners.notifyRemoved(smashable.getChannel(), smashable);
        }
    }

    protected void send(Molecule molecule) {
        molecule.setAction(Molecule.Action.SEND);
        notifyAtomsSent(molecule);
    }

    protected void notifyAtomsSent(Molecule molecule) {
        for (Atom atom : molecule) {
            idListeners.notifySent(atom.getId(), atom);
        }
        channelListeners.notifySent(molecule.getChannel(), molecule);

        Smashable smashable = unsmashWithBuilder(molecule);
        if (null != smashable) {
            smashableListeners.notifySent(smashable.getChannel(), smashable);
        }

    }

    @Override
    public Crudable<Atom> getNimbase() {
        return nimbase;
    }

    @Override
    public ScrudNotifier<Atom> getIdListeners() {
        return idListeners;
    }

    @Override
    public ScrudNotifier<Molecule> getChannelListeners() {
        return channelListeners;
    }

    @Override
    public ScrudNotifier<Smashable> getSmashableListeners() {
        return smashableListeners;
    }

    @Override
    public void registerSmashable(Smashable smashable) {
        smashableTemplates.put(smashable.getChannel(), smashable);
    }

    @Override
    public void proposeAdd(Molecule molecule) {
        add(molecule);
    }

    @Override
    public void proposeAdd(Smashable smashable) {
        add(smashWithBuilder(smashable));
    }

    @Override
    public void proposeUpdate(Molecule molecule) {
        update(molecule);
    }

    @Override
    public void proposeUpdate(Smashable smashable) {
        update(smashWithBuilder(smashable));
    }

    @Override
    public void proposeRemove(Molecule molecule) {
        remove(molecule);
    }

    @Override
    public void proposeRemove(Smashable smashable) {
        remove(smashWithBuilder(smashable));

    }

    @Override
    public void proposeSend(Molecule molecule) {
        send(molecule);
    }

    @Override
    public void proposeSend(Smashable smashable) {
        send(smashWithBuilder(smashable));
    }

    protected Smashable unsmashWithBuilder(Molecule molecule) {

        Smashable smashable = smashableTemplates.get(molecule.getChannel());
        if (null == smashable) {
            return null;
        }

        smashable = smashable.newInstance();

        smashable.setInstanceId(molecule.getInstanceId());

        SmashableBuilder smashableBuilder =
                new SmashableBuilder(nimbase.nextSequenceNumber(), molecule);

        smashable.unsmashMe(smashableBuilder);

        return smashable;

    }

    protected Molecule smashWithBuilder(Smashable smashable) {

        Molecule molecule = new Molecule(smashable.getChannel());

        // Set instance id to the user given id or generate if none is given
        // should usually only be null when new smashables are added
        String instanceId = smashable.getInstanceId();
        if (null == instanceId) {
            instanceId = UUID.randomUUID().toString();
            smashable.setInstanceId(instanceId);
        }
        molecule.setInstanceId(instanceId);

        SmashableBuilder smashableBuilder =
                new SmashableBuilder(nimbase.nextSequenceNumber(), molecule);
        smashable.smashMe(smashableBuilder);

        return smashableBuilder.getMolecule();

    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
    }
}
