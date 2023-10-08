package nurgling;

import haven.*;

import java.awt.*;

public class NFlowerMenu extends FlowerMenu
{
    public static final Tex bl = Resource.loadtex("nurgling/hud/flower/left");
    public static final Tex bm = Resource.loadtex("nurgling/hud/flower/mid");
    public static final Tex br = Resource.loadtex("nurgling/hud/flower/right");

    public static final Tex bhl = Resource.loadtex("nurgling/hud/flower/hleft");
    public static final Tex bhm = Resource.loadtex("nurgling/hud/flower/hmid");
    public static final Tex bhr = Resource.loadtex("nurgling/hud/flower/hright");

    NPetal[] nopts;

    int len = 0;
    public NFlowerMenu(String[] opts)
    {
        super();
        nopts = new NPetal[opts.length];
        int y = 0;

        for(int i = 0; i < opts.length; i++)
        {
            add(nopts[i] = new NPetal(opts[i], i + 1), new Coord(0,y));
            nopts[i].num = i;
            y+=bl.sz().y + UI.scale(2);
            len = Math.max(nopts[i].sz.x,len);
        }
        for(int i = 0; i < opts.length; i++)
        {
            nopts[i].resize(len, bl.sz().y);
        }
    }

    public void nchoose(NPetal option)
    {
        if (option == null)
        {
            wdgmsg("cl", -1);
        }
        else
        {
            wdgmsg("cl", option.num, ui.modflags());
        }
    }

    public class NPetal extends Widget {
        public String name;
        public int num;
        private Text text;
        private Text textnum;

        public NPetal(String name, int num) {
            super(Coord.z);
            this.name = name;
            this.num = num;
            text = NStyle.flower.render(name);
            textnum = NStyle.flower.render(String.valueOf(num));
            resize(text.sz().x + bl.sz().x + br.sz().x + UI.scale(30), ph);
        }

        public void draw(GOut g)
        {
            g.image((isHighligted) ? bhl : bl, new Coord(0, 0));

            Coord pos = new Coord(0, 0);
            for (pos.x = bl.sz().x; pos.x + bm.sz().x <= len - br.sz().x; pos.x += bm.sz().x)
            {
                g.image((isHighligted) ? bhm : bm, pos);
            }
            g.image((isHighligted) ? bhm : bm, pos, new Coord(sz.x - pos.x - br.sz().x, br.sz().y));
            g.image(textnum.tex(), new Coord(bl.sz().x/2 - textnum.tex().sz().x/2 - UI.scale(1), br.sz().y / 2 - textnum.tex().sz().y / 2));
            g.image(text.tex(), new Coord(br.sz().x + bl.sz().x + UI.scale(10), br.sz().y / 2 - text.tex().sz().y / 2));
            g.image((isHighligted) ? bhr : br, new Coord(len - br.sz().x, 0));
        }

        public boolean mousedown(Coord c, int button) {
            nchoose(this);
            return(true);
        }

        @Override
        public void mousemove(Coord c)
        {
            isHighligted = c.isect(Coord.z, sz);
            super.mousemove(c);
        }

        boolean isHighligted = false;
    }

    protected void added()
    {
        if (c.equals(-1, -1))
            c = parent.ui.lcc;
        mg = ui.grabmouse(this);
        kg = ui.grabkeys(this);
    }

    public void uimsg(String msg, Object... args)
    {
        if (msg.equals("cancel") || msg.equals("act"))
        {
            mg.remove();
            kg.remove();
            ui.destroy(NFlowerMenu.this);
        }
    }

    public boolean keydown(java.awt.event.KeyEvent ev) {
        char key = ev.getKeyChar();
        if((key >= '0') && (key <= '9')) {
            int opt = (key == '0')?10:(key - '1');
            if(opt < nopts.length) {
                nchoose(nopts[opt]);
                kg.remove();
            }
            return(true);
        } else if(key_esc.match(ev)) {
            nchoose(null);
            kg.remove();
            return(true);
        }
        return(false);
    }
}
