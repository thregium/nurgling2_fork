package nurgling.actions.test;

import haven.Coord2d;
import haven.Gob;
import nurgling.NGameUI;
import nurgling.NUtils;
import nurgling.actions.*;
import nurgling.areas.NArea;
import nurgling.pf.NPFMap;
import nurgling.tasks.ChangeModelAtrib;
import nurgling.tools.Finder;
import nurgling.tools.NAlias;

import java.util.ArrayList;

/*
* Lift/Place test
* */

public class TESTLiftDrop extends Test
{

    public TESTLiftDrop()
    {
        this.num = 100;
    }

    @Override
    public void body(NGameUI gui) throws InterruptedException
    {
        NPFMap npf = new NPFMap(NUtils.player().rc, Finder.findGob(new NAlias("gfx/kritter/cattle/calf")).rc, 2);
        npf.build();
        npf.print(npf.getSize(), npf.getCells());

//        Gob trough = Finder.findGob(new NAlias("gfx/terobjs/cattle/calf"));
//        Coord2d pos = trough.rc;
//        double a = trough.a;
//        Gob cistern  = Finder.findGob(new NAlias("gfx/terobjs/cistern"));
//        new LiftObject(trough).run(gui);
//        new PathFinder ( cistern ).run(gui);
//        NUtils.activateGob ( cistern );
//        NUtils.getUI().core.addTask(new ChangeModelAtrib(trough, 7));
//        new FindPlaceAndAction(trough, NArea.findSpec("trough")).run(gui);
//        new LiftObject(trough).run(gui);
//        new PathFinder ( cistern ).run(gui);
//        NUtils.activateGob ( cistern );
//        NUtils.getUI().core.addTask(new ChangeModelAtrib(trough, 0));
//        new FindPlaceAndAction(trough, NArea.findSpec("trough")).run(gui);
    }
}
