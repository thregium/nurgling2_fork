package haven;

import haven.render.Location;
import haven.render.Pipe;

public class GobSizing implements Gob.SetupMod {
    Gob gob;

    private Pipe.Op op = null;

    public void update(Gob gob) {
        this.gob = gob;
        if(gob.getres() == null || gob.getres().name == null) {
            op = null;
            return;
        }
        update();
    }

    private void update() {

        op = makeScale();

    }

    private Pipe.Op makeScale() {
        if (gob.getres() != null) {
            String resName = gob.getres().name;
            if(Config.flatcupboards && resName.equals("gfx/terobjs/cupboard"))
                return Pipe.Op.compose(Location.rot(new Coord3f(0, 1, 0), 4.712f), Location.scale(0.01f, 1, 0.62f), Location.xlate(new Coord3f(6,0,-8.8f)));
            else if ((resName.startsWith("gfx/terobjs/arch/palisade") || resName.startsWith("gfx/terobjs/arch/brickwall")) && Config.flatwalls) {
                return Pipe.Op.compose(Location.scale(1, 1, resName.contains("gate") ? 0.5f : 0.2f));
            }
            else
                return null;
        } else
            return null;
    }

    @Override
    public Pipe.Op gobstate() {
        return op;
    }
}