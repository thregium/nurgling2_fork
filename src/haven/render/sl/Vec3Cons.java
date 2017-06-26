/*
 *  This file is part of the Haven & Hearth game client.
 *  Copyright (C) 2009 Fredrik Tolf <fredrik@dolda2000.com>, and
 *                     Björn Johannessen <johannessen.bjorn@gmail.com>
 *
 *  Redistribution and/or modification of this file is subject to the
 *  terms of the GNU Lesser General Public License, version 3, as
 *  published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  Other parts of this source tree adhere to other copying
 *  rights. Please see the file `COPYING' in the root directory of the
 *  source tree for details.
 *
 *  A copy the GNU Lesser General Public License is distributed along
 *  with the source tree of which this file is a part in the file
 *  `doc/LPGL-3'. If it is missing for any reason, please see the Free
 *  Software Foundation's website at <http://www.fsf.org/>, or write
 *  to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 *  Boston, MA 02111-1307 USA
 */

package haven.render.sl;

public class Vec3Cons extends Expression {
    public static final Vec3Cons z = new Vec3Cons(FloatLiteral.z, FloatLiteral.z, FloatLiteral.z);
    public static final Vec3Cons u = new Vec3Cons(FloatLiteral.u, FloatLiteral.u, FloatLiteral.u);
    public final Expression[] els;

    public Vec3Cons(Expression... els) {
	if((els.length < 1) || (els.length > 3))
	    throw(new RuntimeException("Invalid number of arguments for vec3: " + els.length));
	this.els = els;
    }

    public void walk(Walker w) {
	for(Expression el : els)
	    w.el(el);
    }

    public void output(Output out) {
	out.write("vec3(");
	els[0].output(out);
	for(int i = 1; i < els.length; i++) {
	    out.write(", ");
	    els[i].output(out);
	}
	out.write(")");
    }
}
