/*
 * Copyright (C) 2012 eXo Platform SAS.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.crsh.text.formatter;

import org.crsh.text.Color;
import org.crsh.text.LineReader;
import org.crsh.text.RenderAppendable;
import org.crsh.text.Renderer;

import java.lang.management.MemoryUsage;

public class MemoryUsageRenderer extends Renderer {

  /** . */
  private final MemoryUsage usage;

  public MemoryUsageRenderer(MemoryUsage usage) {
    this.usage = usage;
  }

  @Override
  public int getActualWidth() {
    return 32;
  }

  @Override
  public int getMinWidth() {
    return 4;
  }

  @Override
  public int getMinHeight(int width) {
    return 1;
  }

  @Override
  public int getActualHeight(int width) {
    return 2;
  }

  @Override
  public LineReader reader(int width) {
    return reader(width, 2);
  }

  @Override
  public LineReader reader(final int width, final int height) {
    return new LineReader() {

      /** . */
      private int index = 0;

      public boolean hasLine() {
        return index < height;
      }
      public void renderLine(RenderAppendable to) throws IllegalStateException {
        if (!hasLine()) {
          throw new IllegalStateException();
        }
        Color previous = null;
        char c;
        for (int i = 0;i < width;i++) {
          long v = (i * usage.getMax()) / width;
          Color current;
          if (v < usage.getInit()) {
            current = Color.black;
          } else if (v < usage.getUsed()) {
            current = Color.red;
          } else if (v < usage.getCommitted()) {
            current = Color.blue;
          } else {
            current = Color.green;
          }
          if (previous != current) {
            if (previous != null) {
              to.leaveStyle();
            }
            to.enterStyle(current.bg());
            previous = current;
          }
          to.append(' ');
        }
        if (previous != null) {
          to.leaveStyle();
        }
        index++;
      }
    };
  }
}
