package nurgling;

import haven.*;
import static haven.Inventory.sqsz;
import haven.res.ui.tt.defn.*;

public class NGItem extends GItem
{
    String name = null;
    private Float quality = null;

    public NGItem(Indir<Resource> res, Message sdt)
    {
        super(res, sdt);
    }

    public NGItem(Indir<Resource> res)
    {
        super(res);
    }

    public String name()
    {
        return name;
    }

    public static class NContent
    {
        private double quality = -1;
        private String name = null;

        public NContent(double quality, String name)
        {
            this.quality = quality;
            this.name = name;
        }

        public double quality()
        {
            return quality;
        }

        public String name()
        {
            return name;
        }

    }

    private NContent content = null;

    public Coord sprsz()
    {
        if (spr != null)
        {
            return spr.sz().div(new Coord(sqsz.x - UI.scale(1), sqsz.y - UI.scale(1)));
        }
        return null;
    }

    @Override
    public void tick(double dt)
    {
        super.tick(dt);
        if (name == null && rawinfo != null)
        {
            for (Object o : rawinfo.data)
            {
                if (o instanceof Object[])
                {
                    Object[] a = (Object[]) o;
                    if (a[0] instanceof Integer)
                    {
                        String resName = NUtils.getUI().sess.getResName((Integer) a[0]);
                        if (resName != null)
                        {
                            if (resName.equals("ui/tt/q/quality"))
                            {
                                if (a.length > 2) quality = (Float) a[1];
                            }
                            else if (resName.equals("ui/tt/cont"))
                            {
                                double q = -1;
                                String name = null;
                                for (Object so : a)
                                {
                                    if (so instanceof Object[])
                                    {
                                        Object[] cont = (Object[]) so;
                                        for (Object sso : cont)
                                        {
                                            if (sso instanceof Object[])
                                            {
                                                Object[] b = (Object[]) sso;
                                                if (b[0] instanceof Integer)
                                                {
                                                    String resName2 = NUtils.getUI().sess.getResName((Integer) b[0]);
                                                    if (b.length > 2) if (resName2 != null)
                                                    {
                                                        if (resName2.equals("ui/tt/cn"))
                                                        {
                                                            name = (String) b[1];
                                                        }
                                                        else if (resName2.equals("ui/tt/q/quality"))
                                                        {
                                                            q = (Float) b[1];
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                if (name != null && q != -1)
                                {
                                    content = new NContent(q, name);
                                }
                            }
                            else if (resName.equals("ui/tt/coin"))
                            {
                                name = (String) a[1];
                            }
                            if (spr != null)
                            {
                                if (!res.get().name.contains("coin"))
                                {
                                    if (res.get() != null)
                                    {
                                        name = DefName.getname(this);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    @Override
    public void wdgmsg(String msg, Object... args)
    {
        if (name != null)
        {
            if (msg.equals("take"))
            {
                NUtils.getGameUI().getCharInfo().setCandidate(name());
            }
            else if (msg.equals("iact"))
            {
                NUtils.getGameUI().getCharInfo().setFlowerCandidate(this);
            }
        }
        super.wdgmsg(msg, args);
    }

}
