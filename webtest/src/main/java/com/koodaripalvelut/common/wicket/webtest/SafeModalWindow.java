/*
 * Copyright 2010 Kindleit Technologies. All rights reserved. This file, all
 * proprietary knowledge and algorithms it details are the sole property of
 * Kindleit Technologies unless otherwise specified. The software this file
 * belong with is the confidential and proprietary information of Kindleit
 * Technologies. ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the terms
 * of the license agreement you entered into with Kindleit.
 */
package com.koodaripalvelut.common.wicket.webtest;

import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.model.IModel;

/** SafeModalWindow is responsible of
 * @author rhansen@kitsd.com
 */
public class SafeModalWindow extends ModalWindow {

  public SafeModalWindow(final String id, final IModel<?> model) {
    super(id, model);
  }

  public SafeModalWindow(final String id) {
    super(id);
  }

  @Override
  protected Object getShowJavascript() {
    return "var win;\n" //
    + "try {\n"
    + " win = window.parent.Wicket.Window;\n"
    + "} catch (ignore) {\n"
    + "}\n"
    + "if (typeof(win) == \"undefined\" || typeof(win.current) == \"undefined\") {\n"
    + "  try {\n"
    + "     win = window.Wicket.Window;\n"
    + "  } catch (ignore) {\n"
    + "  }\n"
    + "}\n"
    + "if (typeof(win.windows) == 'undefined') { win.windows = { }; };\n"
    + "win = win.windows['" + getMarkupId() + "'] = win.create(settings);\n"
    + "win.show();\n";
  }

  @Override
  protected String getCloseJavacript() {
    return "var win;\n" //
    + "try {\n"
    + " win = window.parent.Wicket.Window;\n"
    + "} catch (ignore) {\n"
    + "}\n"
    + "if (typeof(win) == \"undefined\" || typeof(win.current) == \"undefined\") {\n"
    + "  try {\n"
    + "     win = window.Wicket.Window;\n"
    + "  } catch (ignore) {\n"
    + "  }\n"
    + "}\n"
    + "if (typeof(win) != \"undefined\" && typeof(win.current) != \"undefined\") {\n"
    + " var close = function(w) { w.setTimeout(function() {\n"
    + "   win.windows['" + getMarkupId() + "'].close();\n"
    + "   delete win.windows['" + getMarkupId() + "'];\n"
    + " }, 0);  } \n"
    + " try { close(window.parent); } catch (ignore) { close(window); };\n" + "}";
  }
}
