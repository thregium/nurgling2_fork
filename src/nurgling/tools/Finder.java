package nurgling.tools;

import haven.*;
import nurgling.*;
import nurgling.areas.*;
import nurgling.pf.*;
import nurgling.tasks.*;

import java.util.*;

public class Finder
{
    static final Comparator<Gob> x_comp = new Comparator<Gob> () {
        @Override
        public int compare(
                Gob lhs,
                Gob rhs
        ) {
            // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
            return (lhs.rc.x > rhs.rc.x) ? -1 : ((lhs.rc.x < rhs.rc.x) ? 1 : (lhs.rc.y > rhs.rc.y) ? -1 : (
                    lhs.rc.y < rhs.rc.y) ? 1 : 0);
        }
    };

    static final Comparator<Gob> y_comp = new Comparator<Gob> () {
        @Override
        public int compare(
                Gob lhs,
                Gob rhs
        ) {
            // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
            return (lhs.rc.y > rhs.rc.y) ? -1 : ((lhs.rc.y < rhs.rc.y) ? 1 : (lhs.rc.x > rhs.rc.x) ? -1 : (
                    lhs.rc.x < rhs.rc.x) ? 1 : 0);
        }
    };

    static void sort(ArrayList<Gob> gobs)
    {
        if(!gobs.isEmpty())
        {
            Coord2d min = new Coord2d(gobs.get(0).rc.x,gobs.get(0).rc.y);
            Coord2d max = new Coord2d(gobs.get(0).rc.x,gobs.get(0).rc.y);
            for(Gob gob: gobs)
            {
                max.x = Math.max(gob.rc.x,max.x);
                max.y = Math.max(gob.rc.y,max.y);
                min.x = Math.min(gob.rc.x,min.x);
                min.y = Math.min(gob.rc.y,min.y);
            }
            if(Math.abs(max.y-min.y) > Math.abs(max.x - min.x))
                gobs.sort(x_comp);
            else
                gobs.sort(y_comp);
        }
    }

    public static ArrayList<Gob> findGobs(NArea area, NAlias name) throws InterruptedException
    {
        Pair<Coord2d,Coord2d> space = area.getRCArea();
        return findGobs(space,name);
    }

    public static ArrayList<Gob> findGobs(Pair<Coord2d,Coord2d> space, NAlias name) throws InterruptedException
    {
        ArrayList<Gob> result = new ArrayList<> ();
        synchronized ( NUtils.getGameUI().ui.sess.glob.oc ) {
            for ( Gob gob : NUtils.getGameUI().ui.sess.glob.oc ) {
                if (!(gob instanceof OCache.Virtual))
                {
                    if (gob.rc.x >= space.a.x && gob.rc.y >= space.a.y && gob.rc.x <= space.b.x && gob.rc.y <= space.b.y)
                    {
                        if (NParser.isIt(gob, name))
                        {
                            result.add(gob);
                        }
                    }
                }
            }
        }
        sort(result);
        return result;
    }

    public static ArrayList<Gob> findGobs(Area area, NAlias name) throws InterruptedException
    {
        Coord2d b = area.ul.mul(MCache.tilesz);
        Coord2d e = area.br.mul(MCache.tilesz).add(MCache.tilesz);
        Pair<Coord2d,Coord2d> space = new Pair<>(b,e);
        ArrayList<Gob> result = new ArrayList<> ();
        synchronized ( NUtils.getGameUI().ui.sess.glob.oc ) {
            for ( Gob gob : NUtils.getGameUI().ui.sess.glob.oc ) {
                if (!(gob instanceof OCache.Virtual))
                {
                    if (gob.rc.x >= space.a.x && gob.rc.y >= space.a.y && gob.rc.x <= space.b.x && gob.rc.y <= space.b.y)
                    {
                        if (NParser.isIt(gob, name))
                        {
                            result.add(gob);
                        }
                    }
                }
            }
        }
        sort(result);
        return result;
    }



    public static ArrayList<Gob> findGobs(NArea area, NAlias name, int mattr) throws InterruptedException
    {
        Pair<Coord2d,Coord2d> space = area.getRCArea();
        ArrayList<Gob> result = new ArrayList<> ();
        synchronized ( NUtils.getGameUI().ui.sess.glob.oc ) {
            for ( Gob gob : NUtils.getGameUI().ui.sess.glob.oc ) {
                if (!(gob instanceof OCache.Virtual))
                {
                    if (gob.rc.x >= space.a.x && gob.rc.y >= space.a.y && gob.rc.x <= space.b.x && gob.rc.y <= space.b.y)
                    {
                        if (NParser.isIt(gob, name) && gob.ngob.getModelAttribute() == mattr)
                        {
                            result.add(gob);
                        }
                    }
                }
            }
        }
        sort(result);
        return result;
    }

    public static Gob findGob(NArea area, NAlias name) throws InterruptedException
    {
        NUtils.getUI().core.addTask(new FindPlayer());
        Pair<Coord2d,Coord2d> space = area.getRCArea();
        Gob result = null;
        double dist = 10000;
        synchronized ( NUtils.getGameUI().ui.sess.glob.oc ) {
            for ( Gob gob : NUtils.getGameUI().ui.sess.glob.oc ) {
                if (!(gob instanceof OCache.Virtual))
                {
                    if (gob.rc.x >= space.a.x && gob.rc.y >= space.a.y && gob.rc.x <= space.b.x && gob.rc.y <= space.b.y)
                    {
                        if (NParser.isIt(gob, name) && NUtils.player()!=null)
                        {
                            double new_dist;
                            if((new_dist = gob.rc.dist(NUtils.player().rc))<dist)
                            {
                                dist = new_dist;
                                result = gob;
                            }
                        }
                    }
                }
            }
        }
        return result;
    }


    public static Gob findGob(NAlias name) throws InterruptedException
    {
        NUtils.getUI().core.addTask(new FindPlayer());
        Gob result = null;
        double dist = 10000;
        synchronized (NUtils.getGameUI().ui.sess.glob.oc)
        {
            for (Gob gob : NUtils.getGameUI().ui.sess.glob.oc)
            {
                if (!(gob instanceof OCache.Virtual || gob.attr.isEmpty() || gob.getClass().getName().contains("GlobEffector")))
                {
                    if (NParser.isIt(gob, name) && NUtils.player() != null)
                    {
                        double new_dist;
                        if ((new_dist = gob.rc.dist(NUtils.player().rc)) < dist)
                        {
                            dist = new_dist;
                            result = gob;
                        }
                    }
                }
            }
        }
        return result;
    }

    public static Gob findGob(long gobid)
    {
        if(gobid == -1)
        {
            if(NUtils.getGameUI().map.placing!=null)
                return NUtils.getGameUI().map.placing.get();
            return null;
        }
        else
        {
            return NUtils.getGameUI().ui.sess.glob.oc.getgob(gobid);
        }
    }

    public static Gob findGob(Coord pos) {
        Pair<Coord2d,Coord2d> space = new Pair<>(new Coord2d(pos.x*MCache.tilesz.x,pos.y*MCache.tilesz.y),new Coord2d((pos.x + 1) *MCache.tilesz.x,(pos.y+1)*MCache.tilesz.y));
//        NUtils.getGameUI().msg(space.a + " " +  space.b);
        synchronized (NUtils.getGameUI().ui.sess.glob.oc)
        {
            for (Gob gob : NUtils.getGameUI().ui.sess.glob.oc)
            {
                if (!(gob instanceof OCache.Virtual || gob.attr.isEmpty() || gob.getClass().getName().contains("GlobEffector")))
                {
                    // Только внутри тайла, без пересечений
                    if (gob.id!= NUtils.playerID() && gob.rc.x >=space.a.x && gob.rc.y >=space.a.y && gob.rc.x <=space.b.x && gob.rc.y <=space.b.y)
                    {
                        return gob;
                    }
                }
            }
        }
        return null;
    }

    public static Gob findGob(Coord2d pos) {
        synchronized (NUtils.getGameUI().ui.sess.glob.oc)
        {
            for (Gob gob : NUtils.getGameUI().ui.sess.glob.oc)
            {
                if (!(gob instanceof OCache.Virtual || gob.attr.isEmpty() || gob.getClass().getName().contains("GlobEffector")))
                {
                    if (gob.id!= NUtils.playerID() && gob.rc.dist(pos)<0.5)
                    {
                        return gob;
                    }
                }
            }
        }
        return null;
    }



    public static Gob findGob(Coord pos, NAlias exc){
        Pair<Coord2d,Coord2d> space = new Pair<>(new Coord2d(pos.x*MCache.tilesz.x,pos.y*MCache.tilesz.y),new Coord2d((pos.x + 1) *MCache.tilesz.x,(pos.y+1)*MCache.tilesz.y));
//        NUtils.getGameUI().msg(space.a + " " +  space.b);
        synchronized (NUtils.getGameUI().ui.sess.glob.oc)
        {
            for (Gob gob : NUtils.getGameUI().ui.sess.glob.oc)
            {
                if(gob.ngob!=null && gob.ngob.name!=null && !NParser.checkName(gob.ngob.name,exc)) {
                    if (!(gob instanceof OCache.Virtual || gob.attr.isEmpty() || gob.getClass().getName().contains("GlobEffector"))) {
                        // Только внутри тайла, без пересечений
                        if (gob.id != NUtils.playerID() && gob.rc.x >= space.a.x && gob.rc.y >= space.a.y && gob.rc.x <= space.b.x && gob.rc.y <= space.b.y) {
                            return gob;
                        }
                    }
                }
            }
        }
        return null;
    }


    public static Gob findLiftedbyPlayer() {
        long plid;
        Following fl;
        if ((plid = NUtils.playerID()) != -1) {
            synchronized (NUtils.getGameUI().ui.sess.glob.oc) {
                for (Gob gob : NUtils.getGameUI().ui.sess.glob.oc) {
                    if ((fl = gob.getattr(Following.class)) != null) {

                        if (fl.tgt == plid) {
                            return gob;
                        }
                    }
                }
            }
        }
        return null;
    }

    public static Gob findGob(Coord pos, NAlias crop, int stage) {
        Pair<Coord2d,Coord2d> space = new Pair<>(new Coord2d(pos.x*MCache.tilesz.x,pos.y*MCache.tilesz.y),new Coord2d((pos.x + 1) *MCache.tilesz.x,(pos.y+1)*MCache.tilesz.y));
//        NUtils.getGameUI().msg(space.a + " " +  space.b);
        synchronized (NUtils.getGameUI().ui.sess.glob.oc)
        {
            for (Gob gob : NUtils.getGameUI().ui.sess.glob.oc)
            {
                if (!(gob instanceof OCache.Virtual || gob.attr.isEmpty() || gob.getClass().getName().contains("GlobEffector")))
                {
                    // Только внутри тайла, без пересечений
                    if (gob.id!= NUtils.playerID() && gob.rc.x >=space.a.x && gob.rc.y >=space.a.y && gob.rc.x <=space.b.x && gob.rc.y <=space.b.y && NParser.checkName(gob.ngob.name,crop) && gob.ngob.getModelAttribute()==stage)
                    {
                        return gob;
                    }
                }
            }
        }
        return null;
    }

    public static ArrayList<Gob> findGobs(Area area, NAlias name, int stage) {
        Coord2d b = area.ul.mul(MCache.tilesz);
        Coord2d e = area.br.mul(MCache.tilesz).add(MCache.tilesz);
        Pair<Coord2d,Coord2d> space = new Pair<>(b,e);
        ArrayList<Gob> result = new ArrayList<> ();
        synchronized ( NUtils.getGameUI().ui.sess.glob.oc ) {
            for ( Gob gob : NUtils.getGameUI().ui.sess.glob.oc ) {
                if (!(gob instanceof OCache.Virtual))
                {
                    if (gob.rc.x >= space.a.x && gob.rc.y >= space.a.y && gob.rc.x <= space.b.x && gob.rc.y <= space.b.y)
                    {
                        if (gob.ngob.name!=null && NParser.checkName(gob.ngob.name, name) && gob.ngob.getModelAttribute() == stage )
                        {
                            result.add(gob);
                        }
                    }
                }
            }
        }
        sort(result);
        return result;
    }

    public static Coord2d getFreePlace(Pair<Coord2d,Coord2d> area, Gob placed) {
        return getFreePlace(area,placed.ngob.hitBox);
    }

    public static Coord2d getFreePlace(Pair<Coord2d,Coord2d> area, NHitBox hitBox) {
        Coord2d pos = null;


        ArrayList<NHitBoxD> significantGobs = new ArrayList<> ();
        NHitBoxD chekerOfArea = new NHitBoxD(area.a, area.b);

        NHitBoxD temporalGobBox = new NHitBoxD(hitBox.begin, hitBox.end, Coord2d.of(0),0);
        if(chekerOfArea.c[2].sub(chekerOfArea.c[0]).x < temporalGobBox.getCircumscribedBR().sub(temporalGobBox.getCircumscribedUL()).x ||
                chekerOfArea.c[2].sub(chekerOfArea.c[0]).y < temporalGobBox.getCircumscribedBR().sub(temporalGobBox.getCircumscribedUL()).y )
            return null;

        synchronized ( NUtils.getGameUI().ui.sess.glob.oc ) {
            for ( Gob gob : NUtils.getGameUI().ui.sess.glob.oc ) {
                if (!(gob instanceof OCache.Virtual || gob.attr.isEmpty() || gob.getClass().getName().contains("GlobEffector")))
                    if(gob.ngob.hitBox != null && gob.getattr(Following.class)==null){
                        NHitBoxD gobBox = new NHitBoxD(gob.ngob.hitBox.begin, gob.ngob.hitBox.end, gob.rc, gob.a);
                        if (gobBox.intersectsGreedy(chekerOfArea))
                            significantGobs.add(gobBox);
                }
            }
        }

        Coord inchMax = area.b.sub(area.a).floor();
        Coord margin =  hitBox.end.sub(hitBox.begin).floor(2,2);
        for (int i = margin.x; i < inchMax.x - margin.x; i++)
        {
            for (int j = margin.y; j < inchMax.y - margin.y; j++)
            {
                boolean passed = true;
                NHitBoxD testGobBox = new NHitBoxD(hitBox.begin, hitBox.end, area.a.add(i,j),0);
                for ( NHitBoxD significantHitbox : significantGobs )
                    if(significantHitbox.intersectsGreedy(testGobBox))
                        passed = false;
                if(passed)
                    return Coord2d.of(testGobBox.rc.x, testGobBox.rc.y);
            }
        }
        return pos;
    }

}
