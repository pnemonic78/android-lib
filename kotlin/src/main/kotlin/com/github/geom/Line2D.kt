package com.github.geom

class Line2D {
    companion object {
        /**
         * Tests if the line segment from {@code (x1,y1)} to
         * {@code (x2,y2)} intersects the line segment from {@code (x3,y3)}
         * to {@code (x4,y4)}.
         *
         * @param x1 the X coordinate of the start point of the first
         *           specified line segment
         * @param y1 the Y coordinate of the start point of the first
         *           specified line segment
         * @param x2 the X coordinate of the end point of the first
         *           specified line segment
         * @param y2 the Y coordinate of the end point of the first
         *           specified line segment
         * @param x3 the X coordinate of the start point of the second
         *           specified line segment
         * @param y3 the Y coordinate of the start point of the second
         *           specified line segment
         * @param x4 the X coordinate of the end point of the second
         *           specified line segment
         * @param y4 the Y coordinate of the end point of the second
         *           specified line segment
         * @return <code>true</code> if the first specified line segment
         *                  and the second specified line segment intersect
         *                  each other; <code>false</code> otherwise.
         * @since 1.2
         */
        fun linesIntersect(
            x1: Double,
            y1: Double,
            x2: Double,
            y2: Double,
            x3: Double,
            y3: Double,
            x4: Double,
            y4: Double
        ): Boolean {
            val r1 = relativeCCW(x1, y1, x2, y2, x3, y3)
            val r2 = relativeCCW(x1, y1, x2, y2, x4, y4)
            val r3 = relativeCCW(x3, y3, x4, y4, x1, y1)
            val r4 = relativeCCW(x3, y3, x4, y4, x2, y2)
            return r1 * r2 <= 0 && r3 * r4 <= 0
        }

        /**
         * Tests if the line segment from {@code (x1,y1)} to
         * {@code (x2,y2)} intersects the line segment from {@code (x3,y3)}
         * to {@code (x4,y4)}.
         *
         * @param x1 the X coordinate of the start point of the first
         *           specified line segment
         * @param y1 the Y coordinate of the start point of the first
         *           specified line segment
         * @param x2 the X coordinate of the end point of the first
         *           specified line segment
         * @param y2 the Y coordinate of the end point of the first
         *           specified line segment
         * @param x3 the X coordinate of the start point of the second
         *           specified line segment
         * @param y3 the Y coordinate of the start point of the second
         *           specified line segment
         * @param x4 the X coordinate of the end point of the second
         *           specified line segment
         * @param y4 the Y coordinate of the end point of the second
         *           specified line segment
         * @return <code>true</code> if the first specified line segment
         *                  and the second specified line segment intersect
         *                  each other; <code>false</code> otherwise.
         */
        fun linesIntersect(
            x1: Int,
            y1: Int,
            x2: Int,
            y2: Int,
            x3: Int,
            y3: Int,
            x4: Int,
            y4: Int
        ): Boolean {
            val r1 = relativeCCW(x1, y1, x2, y2, x3, y3)
            val r2 = relativeCCW(x1, y1, x2, y2, x4, y4)
            val r3 = relativeCCW(x3, y3, x4, y4, x1, y1)
            val r4 = relativeCCW(x3, y3, x4, y4, x2, y2)
            return r1 * r2 <= 0 && r3 * r4 <= 0
        }

        /**
         * Returns an indicator of where the specified point
         * {@code (px,py)} lies with respect to the line segment from
         * {@code (x1,y1)} to {@code (x2,y2)}.
         * The return value can be either 1, -1, or 0 and indicates
         * in which direction the specified line must pivot around its
         * first end point, {@code (x1,y1)}, in order to point at the
         * specified point {@code (px,py)}.
         * <p>A return value of 1 indicates that the line segment must
         * turn in the direction that takes the positive X axis towards
         * the negative Y axis.  In the default coordinate system used by
         * Java 2D, this direction is counterclockwise.
         * <p>A return value of -1 indicates that the line segment must
         * turn in the direction that takes the positive X axis towards
         * the positive Y axis.  In the default coordinate system, this
         * direction is clockwise.
         * <p>A return value of 0 indicates that the point lies
         * exactly on the line segment.  Note that an indicator value
         * of 0 is rare and not useful for determining colinearity
         * because of floating point rounding issues.
         * <p>If the point is colinear with the line segment, but
         * not between the end points, then the value will be -1 if the point
         * lies "beyond {@code (x1,y1)}" or 1 if the point lies
         * "beyond {@code (x2,y2)}".
         *
         * @param x1 the X coordinate of the start point of the
         *           specified line segment
         * @param y1 the Y coordinate of the start point of the
         *           specified line segment
         * @param x2 the X coordinate of the end point of the
         *           specified line segment
         * @param y2 the Y coordinate of the end point of the
         *           specified line segment
         * @param px the X coordinate of the specified point to be
         *           compared with the specified line segment
         * @param py the Y coordinate of the specified point to be
         *           compared with the specified line segment
         * @return an integer that indicates the position of the third specified
         *                  coordinates with respect to the line segment formed
         *                  by the first two specified coordinates.
         * @since 1.2
         */
        fun relativeCCW(
            x1: Double,
            y1: Double,
            x2: Double,
            y2: Double,
            px: Double,
            py: Double
        ): Int {
            val dx = x2 - x1
            val dy = y2 - y1
            var dpx = px - x1
            var dpy = py - y1
            var ccw = dpx * dy - dpy * dx
            if (ccw == 0.0) {
                // The point is colinear, classify based on which side of
                // the segment the point falls on.  We can calculate a
                // relative value using the projection of px,py onto the
                // segment - a negative value indicates the point projects
                // outside of the segment in the direction of the particular
                // endpoint used as the origin for the projection.
                ccw = dpx * dx + dpy * dy
                if (ccw > 0.0) {
                    // Reverse the projection to be relative to the original x2,y2
                    // x2 and y2 are simply negated.
                    // px and py need to have (x2 - x1) or (y2 - y1) subtracted
                    //    from them (based on the original values)
                    // Since we really want to get a positive answer when the
                    //    point is "beyond (x2,y2)", then we want to calculate
                    //    the inverse anyway - thus we leave x2 & y2 negated.
                    dpx -= dx
                    dpy -= dy
                    ccw = dpx * dx + dpy * dy
                    if (ccw < 0.0) {
                        ccw = 0.0
                    }
                }
            }
            return if (ccw < 0.0) -1 else if (ccw > 0.0) 1 else 0
        }

        /**
         * Returns an indicator of where the specified point
         * {@code (px,py)} lies with respect to the line segment from
         * {@code (x1,y1)} to {@code (x2,y2)}.
         * The return value can be either 1, -1, or 0 and indicates
         * in which direction the specified line must pivot around its
         * first end point, {@code (x1,y1)}, in order to point at the
         * specified point {@code (px,py)}.
         * <p>A return value of 1 indicates that the line segment must
         * turn in the direction that takes the positive X axis towards
         * the negative Y axis.  In the default coordinate system used by
         * Java 2D, this direction is counterclockwise.
         * <p>A return value of -1 indicates that the line segment must
         * turn in the direction that takes the positive X axis towards
         * the positive Y axis.  In the default coordinate system, this
         * direction is clockwise.
         * <p>A return value of 0 indicates that the point lies
         * exactly on the line segment.  Note that an indicator value
         * of 0 is rare and not useful for determining colinearity
         * because of floating point rounding issues.
         * <p>If the point is colinear with the line segment, but
         * not between the end points, then the value will be -1 if the point
         * lies "beyond {@code (x1,y1)}" or 1 if the point lies
         * "beyond {@code (x2,y2)}".
         *
         * @param x1 the X coordinate of the start point of the
         *           specified line segment
         * @param y1 the Y coordinate of the start point of the
         *           specified line segment
         * @param x2 the X coordinate of the end point of the
         *           specified line segment
         * @param y2 the Y coordinate of the end point of the
         *           specified line segment
         * @param px the X coordinate of the specified point to be
         *           compared with the specified line segment
         * @param py the Y coordinate of the specified point to be
         *           compared with the specified line segment
         * @return an integer that indicates the position of the third specified
         *                  coordinates with respect to the line segment formed
         *                  by the first two specified coordinates.
         */
        fun relativeCCW(x1: Int, y1: Int, x2: Int, y2: Int, px: Int, py: Int): Int {
            val dx = (x2 - x1).toLong()
            val dy = (y2 - y1).toLong()
            var dpx = (px - x1).toLong()
            var dpy = (py - y1).toLong()
            var ccw = dpx * dy - dpy * dx
            if (ccw == 0L) {
                // The point is colinear, classify based on which side of
                // the segment the point falls on.  We can calculate a
                // relative value using the projection of px,py onto the
                // segment - a negative value indicates the point projects
                // outside of the segment in the direction of the particular
                // endpoint used as the origin for the projection.
                ccw = dpx * dx + dpy * dy
                if (ccw > 0L) {
                    // Reverse the projection to be relative to the original x2,y2
                    // x2 and y2 are simply negated.
                    // px and py need to have (x2 - x1) or (y2 - y1) subtracted
                    //    from them (based on the original values)
                    // Since we really want to get a positive answer when the
                    //    point is "beyond (x2,y2)", then we want to calculate
                    //    the inverse anyway - thus we leave x2 & y2 negated.
                    dpx -= dx
                    dpy -= dy
                    ccw = dpx * dx + dpy * dy
                    if (ccw < 0L) {
                        ccw = 0L
                    }
                }
            }
            return if (ccw < 0L) -1 else if (ccw > 0L) 1 else 0
        }
    }
}